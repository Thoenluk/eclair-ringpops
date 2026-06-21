package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Mul extends ValidInstruction {
    protected Mul(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        final int multiplicand = getSecondArgumentValue();
        final int result = multiplicand * getThirdArgumentValue();
        state.setRegisterValue(address, result);
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument second = getSecondArgument();
        if (isLiteralZero(second)) {
            return Optional.of("Multiplying 0 (second argument) by anything will always result in 0. It's recommended to use MOV <ADDRESS> 0 instead.");
        } else if (isLiteralOne(second)) {
            return Optional.of("Multiplying 1 (second argument) by anything will always result in the other argument. It's recommended to use MOV <ADDRESS> <ARG2> instead");
        }
        final Argument third = getThirdArgument();
        if (isLiteralZero(third)) {
            return Optional.of("Multiplying 0 (third argument) by anything will always result in 0. It's recommended to use MOV <ADDRESS> 0 instead.");
        } else if (isLiteralOne(third)) {
            return Optional.of("Multiplying 1 (third argument) by anything will always result in the other argument. It's recommended to use MOV <ADDRESS> <ARG1> instead");
        }
        if (second.isLiteral() && third.isLiteral()) {
            return Optional.of("Multiplying two literal inputs will always result in a constant value. It is recommended to compute the result yourself and use MOV <ADDRESS> <RESULT> instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 3;
    }

    @Override
    protected String getName() {
        return "MUL";
    }
}
