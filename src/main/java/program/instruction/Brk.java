package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Brk extends ValidInstruction {
    private final int[] printableValues = new int[arguments.length];

    protected Brk(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        if (arguments.length == 0) {
            state.breakPrintingEntireMachine();
            return;
        }
        for (int i = 0; i < arguments.length; i++) {
            printableValues[i] = arguments[i].getValue();
        }
        state.breakPrinting(printableValues);
    }

    @Override
    protected int getMinimumArgumentCount() {
        return 0;
    }

    @Override
    protected int getMaximumArgumentCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getExactArgumentCount() {
        return 0;
    }

    @Override
    protected String getName() {
        return "BRK";
    }
}
