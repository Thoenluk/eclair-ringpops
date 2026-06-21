package main.java.ui;

import main.java.program.Line;
import main.java.ui.libraries.SquigglePainter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.awt.*;

public class ValidationHighlighter {
    private final JTextArea programEditor;
    private final Highlighter highlighter;
    private final Highlighter.HighlightPainter errorPainter = new SquigglePainter(Color.RED);
    private final Highlighter.HighlightPainter warningPainter = new SquigglePainter(Color.YELLOW);

    public ValidationHighlighter(final JTextArea programEditor) {
        this.programEditor = programEditor;
        this.highlighter = programEditor.getHighlighter();
    }

    public void highlight(final Line[] lines) {
        highlighter.removeAllHighlights();
        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            highlight(lines[lineNumber], lineNumber);
        }
    }

    private void highlight(final Line line, final int lineNumber) {
        final int lineStart;
        try {
            lineStart = programEditor.getLineStartOffset(lineNumber);
            final int lineEnd = lineStart + line.getEndOfCode();
            if (line.hasError()) {
                highlighter.addHighlight(lineStart, lineEnd, errorPainter);
            } else if (line.hasWarning()) {
                highlighter.addHighlight(lineStart, lineEnd, warningPainter);
            }
        } catch (final BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
