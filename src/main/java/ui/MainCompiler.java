package main.java.ui;

import main.java.program.Program;

import javax.swing.*;
import java.awt.*;

public class MainCompiler extends JFrame {
    private JPanel contentPane;
    private JTextArea programEditor;
    private JButton run;
    private JScrollPane programScroller;
    private JButton prettyPrint;

    static void main() {
        new MainCompiler(new Program());
    }

    public MainCompiler(final Program program) throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        programEditor.setFont(programEditor.getFont().deriveFont(24f));
        programEditor.setCaretColor(Color.WHITE);
        programEditor.setSelectionColor(new Color(50, 50, 200));

        final ValidationHighlighter validationHighlighter = new ValidationHighlighter(programEditor);
        final TooltipAdapter adapter = new TooltipAdapter(programEditor);
        programEditor.addMouseMotionListener(adapter);
        programEditor.getDocument().addDocumentListener(new RecompileListener(program, adapter, validationHighlighter));

        prettyPrint.addActionListener(_ -> programEditor.setText(program.prettyPrint()));
    }
}
