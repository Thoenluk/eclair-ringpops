package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Rsh extends ValidInstruction {
    protected Rsh(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        final int toShift = getSecondArgumentValue();
        final int result = toShift >> getThirdArgumentValue();
        state.setRegisterValue(address, result);
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument second = getSecondArgument();
        if (isLiteralZero(second)) {
            return Optional.of("Shifting 0 (second argument) right by anything will always result in 0. It is recommended to use MOV <ADDRESS> 0 instead.");
        }
        final Argument third = getThirdArgument();
        if (isLiteralZero(third)) {
            return Optional.of("Shifting anything by 0 (third argument) would not change the value. It is recommended to use MOV <ADDRESS> <ARG1> instead.");
        } else if (isLiteralNegative(third)) {
            return Optional.of("Shifting right by a negative offset (third argument) is possible, but effectively a right shift. It is recommended to use RSH <ADDRESS> <ARG1> <ARG2> instead with the positive value of ARG2.");
        }
        if (second.isLiteral() && third.isLiteral()) {
            return Optional.of("Shifting with two literal inputs will always result in a constant value. It is recommended to compute the result yourself and use MOV <ADDRESS> <RESULT> instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 3;
    }

    @Override
    protected String getName() {
        return "RSH";
    }
}
