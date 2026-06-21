package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Ret extends ValidInstruction {
    protected Ret(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int returnValue = arguments.length == 0 ? 0 : getFirstArgumentValue();
        state.returnWith(returnValue);
    }

    @Override
    protected int getMinimumArgumentCount() {
        return 0;
    }

    @Override
    protected int getMaximumArgumentCount() {
        return 1;
    }

    @Override
    protected int getExactArgumentCount() {
        return 0;
    }

    @Override
    protected String getName() {
        return "RET";
    }
}
