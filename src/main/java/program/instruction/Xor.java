package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Xor extends ValidInstruction {
    protected Xor(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        final int toXor = getSecondArgumentValue();
        final int result = toXor ^ getThirdArgumentValue();
        state.setRegisterValue(address, result);
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument second = getSecondArgument();
        if (isLiteralZero(second)) {
            return Optional.of("XORing 0 (second argument) with anything will always result in the other argument. It is recommended to use MOV <ADDRESS> <ARG2> instead.");
        }
        final Argument third = getThirdArgument();
        if (isLiteralZero(third)) {
            return Optional.of("XORing 0 (third argument) with anything will always result in the other argument. It is recommended to use MOV <ADDRESS> <ARG1> instead.");
        }
        if (second.isLiteral() && third.isLiteral()) {
            return Optional.of("XORing with two literal inputs will always result in a constant value. It is recommended to compute the result yourself and use MOV <ADDRESS> <RESULT> instead.");
        }
        if (second.equals(third)) {
            return Optional.of("XORing a value with itself will always result in 0. It is recommended to use MOV <ADDRESS> 0 instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 3;
    }

    @Override
    protected String getName() {
        return "ORR";
    }
}
