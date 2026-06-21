package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Add extends ValidInstruction {
    protected Add(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        final int addend = getSecondArgumentValue();
        final int result = addend + getThirdArgumentValue();
        state.setRegisterValue(address, result);
    }

    @Override
    public Optional<String> checkSanityWarning() {
        if (isLiteralZero(getSecondArgument())) {
            return Optional.of("Adding 0 (second argument) to anything will always result in the other argument. It is recommended to use MOV <ADDRESS> <ARG2> instead.");
        }
        if (isLiteralZero(getThirdArgument())) {
            return Optional.of("Adding 0 (third argument) to anything will always result in the other argument. It is recommended to use MOV <ADDRESS> <ARG1> instead.");
        }
        if (getSecondArgument().isLiteral() && getThirdArgument().isLiteral()) {
            return Optional.of("Adding with two literal inputs will always result in a constant value. It is recommended to compute the result yourself and use MOV <ADDRESS> <RESULT> instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 3;
    }

    @Override
    protected String getName() {
        return "ADD";
    }
}
