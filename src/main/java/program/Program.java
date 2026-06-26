package main.java.program;

import main.java.program.instruction.FunctionLabel;
import main.java.program.instruction.InstructionParser;
import main.java.program.state.ArgumentParser;
import main.java.program.state.State;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Program {
    private static final String NEWLINE_REGEX = "\\r?\\n";

    private Map<String, Line> cache = new HashMap<>();
    private Line[] lines = new Line[0];
    private final State state = new State();
    private final InstructionParser instructionParser = new InstructionParser(new ArgumentParser(state));
    private int executionSpeed = Speed.MEDIUM;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> execution = null;
    private long start = 0;

    public void recompile(final String text) {
        final String[] unparsedLines = text.toUpperCase().split(NEWLINE_REGEX);
        final Map<String, Line> nextCache = new HashMap<>(unparsedLines.length);
        lines = new Line[unparsedLines.length];
        for (int i = 0; i < unparsedLines.length; i++) {
            final String unparsedLine = unparsedLines[i];
            lines[i] = nextCache.computeIfAbsent(unparsedLine, this::getFromCache);
        }
        cache = nextCache;
        processFunctionLabels();
    }

    private Line getFromCache(final String unparsed) {
        final Line line = cache.get(unparsed);
        return line != null ? line : new Line(unparsed, instructionParser);
    }

    private void processFunctionLabels() {
        // Must be done in a second pass, because lines with identical content are cached, thus InstructionParser is not
        // invoked for those identical lines.
        // Also, processing function labels here avoids a rather ugly dependency from InstructionParser to State.
        final Map<Integer, Integer> functionLabelsAlreadySeen = new HashMap<>();
        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            if (!(lines[lineNumber].getInstruction() instanceof final FunctionLabel functionLabel)) {
                continue;
            }
            final int label = functionLabel.getLabel();
            if (functionLabelsAlreadySeen.containsKey(label)) {
                final String message = String.format("Multiple functions with label %s (or numerical equivalent) found.", label);
                functionLabel.setConflictMessage(message);
            } else {
                state.addFunctionLabel(label, lineNumber);
                functionLabelsAlreadySeen.put(label, label);
            }
            lines[lineNumber].validate();
        }
    }

    public Line[] getLines() {
        return lines;
    }

    public void setExecutionSpeed(final int speed) {
        executionSpeed = speed;
    }

    public void run() {
        stop();
        reset();
        start = System.nanoTime() / 1000;
        if (executionSpeed == Speed.YEET) {
            execution = scheduler.scheduleAtFixedRate(this::step, 0, executionSpeed, TimeUnit.NANOSECONDS);
        }
        else {
            execution = scheduler.scheduleAtFixedRate(this::step, 0, executionSpeed, TimeUnit.MILLISECONDS);
        }
    }

    public void step() {
        try {
            executeStep();
        } catch (final ProgramInterrupt pi) {
            System.out.printf("Finished execution after %dus!%n", System.nanoTime() / 1000 - start);
            System.out.println(pi.getMessage());
            execution.cancel(true);
            reset();
        }
    }

    private void executeStep() throws ProgramInterrupt {
        final int i = state.getIpValue();
        if (i < 0 || lines.length <= i) {
            throw new ProgramFinishedInterrupt(state.getReturnValue());
        }
        lines[i].execute(state);
        state.advance();
    }

    public void stop() {
        if (execution != null) {
            execution.cancel(true);
        }
    }

    public void reset() {
        state.reset();
    }

    public String prettyPrint() {
        return Arrays.stream(getLines())
                .map(Line::prettyPrint)
                .collect(Collectors.joining("\n"));
    }

    public static final class Speed {
        public static final int SLOW = 250;
        public static final int MEDIUM = 150;
        public static final int FAST = 100;
        public static final int FASTER = 50;
        public static final int YEET = 1;

        private Speed() {
        }
    }
}
