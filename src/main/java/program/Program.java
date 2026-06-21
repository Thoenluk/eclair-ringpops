package main.java.program;

import main.java.program.instruction.FunctionLabel;
import main.java.program.instruction.InstructionParser;
import main.java.program.state.ArgumentParser;
import main.java.program.state.State;

import java.util.HashMap;
import java.util.Map;

public class Program {
    private static final String NEWLINE_REGEX = "\\r?\\n";

    private Map<String, Line> cache = new HashMap<>();
    private Line[] lines = new Line[0];
    private final State state = new State();
    private final InstructionParser instructionParser = new InstructionParser(new ArgumentParser(state));

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
}
