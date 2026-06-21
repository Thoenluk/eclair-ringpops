package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Cal extends ValidInstruction {
    protected Cal(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        state.call(getFirstArgumentValue());
    }

    @Override
    protected int getExactArgumentCount() {
        return 1;
    }

    @Override
    protected String getName() {
        return "CAL";
    }
}
