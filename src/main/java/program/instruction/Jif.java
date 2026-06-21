package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

// It's actually pronounced Gif
public class Jif extends ValidInstruction {
    protected Jif(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        if (getFirstArgumentValue() > 0) {
            state.setNextLineToExecute(getSecondArgumentValue());
        }
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument first = getFirstArgument();
        if (first.isLiteral()) {
            return Optional.of("Using JIF with a fixed condition (first argument) will either always or never execute. It is recommended to use JMP instead if you want a jump. Or, like, an empty line or a NOP if you don't?");
        }
        final Argument second = getSecondArgument();
        if (second.isLiteral() && second.getValue() < 0) {
            return Optional.of("Using JIF with a negative target instruction (second argument) will immediately cause the program to exit. It is recommended to use RET for this purpose instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 2;
    }

    @Override
    protected String getName() {
        return "JIF";
    }
}
