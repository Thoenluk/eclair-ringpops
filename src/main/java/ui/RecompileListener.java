package main.java.ui;

import main.java.program.Line;
import main.java.program.Program;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RecompileListener implements DocumentListener {
    final Program program;
    final TooltipAdapter adapter;
    final ValidationHighlighter validationHighlighter;
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> task = null;

    public RecompileListener(final Program program, final TooltipAdapter adapter, final ValidationHighlighter validationHighlighter) {
        this.program = program;
        this.adapter = adapter;
        this.validationHighlighter = validationHighlighter;
    }

    private void scheduleRecompile(final String programText) {
        adapter.clearMessages();
        if (task != null) {
            task.cancel(false);
        }
        task = scheduler.schedule(() -> recompileAndUpdateHighlights(programText), 500, TimeUnit.MILLISECONDS);
    }

    private void recompileAndUpdateHighlights(final String programText) {
        final long startTime = System.nanoTime() / 1000L;
        program.recompile(programText);
        final Line[] lines = program.getLines();
        adapter.setMessages(Arrays.stream(lines).map(Line::getValidationMessage).toArray(String[]::new));
        validationHighlighter.highlight(lines);
        final long endTime = System.nanoTime() / 1000L;
        System.out.printf("Took %dus to update!%n", endTime - startTime);
    }

    private static String findProgramText(final DocumentEvent e) {
        try {
            final Document document = e.getDocument();
            return document.getText(0, document.getLength());
        } catch (final BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        scheduleRecompile(findProgramText(e));
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        scheduleRecompile(findProgramText(e));
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
        // Only fires when the document's properties change. Since I don't do that, don't gotta care!
    }
}
