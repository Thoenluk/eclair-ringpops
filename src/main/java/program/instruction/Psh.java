package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Psh extends ValidInstruction {
    protected Psh(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        state.push(getFirstArgumentValue());
    }

    @Override
    protected int getExactArgumentCount() {
        return 1;
    }

    @Override
    protected String getName() {
        return "PSH";
    }
}
