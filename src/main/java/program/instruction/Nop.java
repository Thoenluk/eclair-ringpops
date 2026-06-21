package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Nop extends ValidInstruction {
    static final Nop INSTANCE = new Nop();

    protected Nop() {
        this(new Argument[0]);
    }

    protected Nop(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
    }

    @Override
    public int getExactArgumentCount() {
        return 0;
    }

    @Override
    public String getName() {
        return "NOP";
    }
}
