package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

public class Acc extends ValidInstruction {
    protected Acc(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        state.setAccumulatorValue(getFirstArgumentValue());
    }

    @Override
    public int getExactArgumentCount() {
        return 1;
    }

    @Override
    public String getName() {
        return "ACC";
    }
}
