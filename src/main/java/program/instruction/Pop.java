package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Pop extends ValidInstruction {
    protected Pop(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        state.setRegisterValue(address, state.pop());
    }

    @Override
    protected int getExactArgumentCount() {
        return 1;
    }

    @Override
    protected String getName() {
        return "POP";
    }
}
