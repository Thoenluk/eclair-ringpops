package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Prc extends ValidInstruction {
    protected Prc(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final String codepoint = Character.toString(getFirstArgumentValue());
        state.print(codepoint);
    }

    @Override
    protected int getExactArgumentCount() {
        return 1;
    }

    @Override
    protected String getName() {
        return "PRC";
    }
}
