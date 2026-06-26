package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Jir extends ValidInstruction {
    protected Jir(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        if (getFirstArgumentValue() > 0) {
            state.setNextLineToExecuteByOffset(getSecondArgumentValue());
        }
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument first = getFirstArgument();
        if (first.isLiteral()) {
            return Optional.of("Using JIR with a fixed condition (first argument) will either always or never execute. It is recommended to use JRL instead if you want a jump. Or, like, an empty line or a NOP if you don't?");
        }
        final Argument second = getSecondArgument();
        if (second.isLiteral() && second.getValue() == 0) {
            return Optional.of("A JIR <CONDITION> 0 instruction would loop forever on itself! Use JIR 1 to skip past the next line, or JIR -1 to jump to the previous line.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 2;
    }

    @Override
    protected String getName() {
        return "JIR";
    }
}
