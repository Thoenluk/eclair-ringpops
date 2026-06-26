package main.java.ui;

import main.java.program.Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;

public class MainCompiler extends JFrame {
    private JPanel contentPane;
    private JMenuBar menuBar;
    private JTextArea programEditor;
    private JButton run;
    private JScrollPane programScroller;
    private JButton prettyPrint;

    private final Program program;
    private final Font font;

    static void main() {
        new MainCompiler(new Program());
    }

    public MainCompiler(final Program program) throws HeadlessException {
        this.program = program;
        this.font = programEditor.getFont().deriveFont(24f);
        runBoilerplateSetup();
        buildMenu();
        configureProgramEditor();

        run.addActionListener(_ -> SwingUtilities.invokeLater(program::run));
        prettyPrint.addActionListener(_ -> programEditor.setText(program.prettyPrint()));
        setVisible(true);
    }

    private void runBoilerplateSetup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    private void buildMenu() {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(-1, 30));
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        configureFileMenu();
        configureExecutionSpeedMenu();
        setJMenuBar(menuBar);
    }

    private void configureFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(setupMenuItem("Open", 'O', CTRL_DOWN_MASK, _ -> System.out.println("Open!")));
        fileMenu.add(setupMenuItem("Save", 'S', CTRL_DOWN_MASK, _ -> System.out.println("Save!")));
        fileMenu.add(setupMenuItem("Save as", 'S', CTRL_DOWN_MASK | SHIFT_DOWN_MASK, _ -> System.out.println("Save as!")));
        menuBar.add(fileMenu);
    }

    private void configureExecutionSpeedMenu() {
        final JMenu speedMenu = new JMenu("Execution Speed");
        speedMenu.setMnemonic('E');
        speedMenu.add(setupExecutionSpeedMenuItem("Slow", '1', Program.Speed.SLOW));
        speedMenu.add(setupExecutionSpeedMenuItem("Medium", '2', Program.Speed.MEDIUM));
        speedMenu.add(setupExecutionSpeedMenuItem("Fast", '3', Program.Speed.FAST));
        speedMenu.add(setupExecutionSpeedMenuItem("Faster", '4', Program.Speed.FASTER));
        speedMenu.add(setupExecutionSpeedMenuItem("Yeet it brother!", '5', Program.Speed.YEET));
        menuBar.add(speedMenu);
    }

    private JMenuItem setupExecutionSpeedMenuItem(final String name, final char shortcut, final int speed) {
        return setupMenuItem(name, shortcut, CTRL_DOWN_MASK, _ -> program.setExecutionSpeed(speed));
    }

    private JMenuItem setupMenuItem(final String name, final char shortcut, final int modifierMask, final ActionListener listener) {
        final JMenuItem item = new JMenuItem(name);
        item.setMnemonic(shortcut);
        item.setAccelerator(KeyStroke.getKeyStroke(shortcut, modifierMask));
        item.addActionListener(listener);
        return item;
    }

    private void configureProgramEditor() {
        programEditor.setFont(font);
        programEditor.setCaretColor(Color.WHITE);
        programEditor.setSelectionColor(new Color(50, 50, 200));

        final TooltipAdapter adapter = new TooltipAdapter(programEditor);
        programEditor.addMouseMotionListener(adapter);
        final ValidationHighlighter validationHighlighter = new ValidationHighlighter(programEditor);
        programEditor.getDocument().addDocumentListener(new RecompileListener(program, adapter, validationHighlighter));
    }
}
