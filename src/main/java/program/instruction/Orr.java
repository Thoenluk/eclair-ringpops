package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Orr extends ValidInstruction {
    protected Orr(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        final int toOr = getSecondArgumentValue();
        final int result = toOr | getThirdArgumentValue();
        state.setRegisterValue(address, result);
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument second = getSecondArgument();
        if (isLiteralZero(second)) {
            return Optional.of("ORRing 0 (second argument) with anything will always result in the other argument. It is recommended to use MOV <ADDRESS> <ARG2> instead.");
        }
        if (isLiteralAllOnes(second)) {
            return Optional.of("ORRing 0xFFFFFF (second argument) with anything will always result in 0xFFFFFFFF. It is recommended to use MOV <ADDRESS> 0xFFFFFFFF instead.");
        }
        final Argument third = getThirdArgument();
        if (isLiteralZero(third)) {
            return Optional.of("ORRing 0 (third argument) with anything will always result in the other argument. It is recommended to use MOV <ADDRESS> <ARG1> instead.");
        }
        if (isLiteralAllOnes(third)) {
            return Optional.of("ORRing 0xFFFFFF (third argument) with anything will always result in 0xFFFFFFFF. It is recommended to use MOV <ADDRESS> 0xFFFFFFFF instead.");
        }
        if (second.isLiteral() && third.isLiteral()) {
            return Optional.of("ORRing with two literal inputs will always result in a constant value. It is recommended to compute the result yourself and use MOV <ADDRESS> <RESULT> instead.");
        }
        if (second.equals(third)) {
            return Optional.of("ORRing a value with itself will always result in the same value. It is recommended to use MOV <ADDRESS> <ARG1> instead.");
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
