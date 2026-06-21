package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Jrl extends ValidInstruction {
    protected Jrl(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        state.setNextLineToExecuteByOffset(getFirstArgumentValue());
    }

    @Override
    public Optional<String> checkSanityWarning() {
        if (isLiteralZero(getFirstArgument())) {
            return Optional.of("A JRL 0 instruction would loop forever on itself! Use JRL 1 to skip past the next line, or JRL -1 to jump to the previous line.");
        }
        return super.checkSanityWarning();
    }

    @Override
    public int getExactArgumentCount() {
        return 1;
    }

    @Override
    public String getName() {
        return "JRL";
    }
}
