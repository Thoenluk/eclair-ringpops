package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Mov extends ValidInstruction {
    protected Mov(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        state.setRegisterValue(address, getSecondArgumentValue());
    }

    @Override
    protected int getExactArgumentCount() {
        return 2;
    }

    @Override
    protected String getName() {
        return "MOV";
    }
}
