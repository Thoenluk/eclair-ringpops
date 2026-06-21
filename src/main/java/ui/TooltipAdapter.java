package main.java.ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TooltipAdapter extends MouseMotionAdapter {
    private static final String[] EMPTY = {};

    final JTextArea programEditor;
    private String[] messages = EMPTY;

    public TooltipAdapter(final JTextArea programEditor) {
        this.programEditor = programEditor;
    }

    public void setMessages(final String[] messages) {
        this.messages = messages;
    }

    public void clearMessages() {
        setMessages(EMPTY);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        final int offset = programEditor.viewToModel2D(e.getPoint());
        try {
            final int line = programEditor.getLineOfOffset(offset);
            programEditor.setToolTipText(findMessage(line));
        } catch (final BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String findMessage(final int lineNumber) {
        return lineNumber >= messages.length
                ? null
                : messages[lineNumber];
    }
}
