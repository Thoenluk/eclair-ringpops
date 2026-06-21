package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Sub extends ValidInstruction {
    protected Sub(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        final int minuend = getSecondArgumentValue();
        final int result = minuend - getThirdArgumentValue();
        state.setRegisterValue(address, result);
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument second = getSecondArgument();
        final Argument third = getThirdArgument();
        if (isLiteralZero(third)) {
            return Optional.of("Subtracting 0 (third argument) from anything will always result in the other argument. It is recommended to use MOV <ADDRESS> <ARG1> instead.");
        }
        if (second.isLiteral() && third.isLiteral()) {
            return Optional.of("Subtracting with two literal inputs will always result in a constant value. It is recommended to compute the result yourself and use MOV <ADDRESS> <RESULT> instead.");
        }
        if (second.equals(third)) {
            return Optional.of("Subtracting a value from itself will always result in 0. It is recommended to use MOV <ADDRESS> 0 instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 3;
    }

    @Override
    protected String getName() {
        return "SUB";
    }
}
