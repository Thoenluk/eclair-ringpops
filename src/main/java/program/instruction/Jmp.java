package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Jmp extends ValidInstruction {
    protected Jmp(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        state.setNextLineToExecute(getFirstArgumentValue());
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument first = getFirstArgument();
        if (first.isLiteral() && first.getValue() < 0) {
            return Optional.of("Using JMP with a negative target instruction will immediately cause the program to exit. It is recommended to use RET for this purpose instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    public int getExactArgumentCount() {
        return 1;
    }

    @Override
    public String getName() {
        return "JMP";
    }
}
