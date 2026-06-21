package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Optional;

public class Div extends ValidInstruction {
    protected Div(final Argument[] arguments) {
        super(arguments);
    }

    @Override
    public void execute(final State state) {
        final int address = getFirstArgumentValue();
        final int dividend = getSecondArgumentValue();
        final int result = dividend / getThirdArgumentValue();
        state.setRegisterValue(address, result);
    }

    @Override
    public Optional<String> checkSanityWarning() {
        final Argument second = getSecondArgument();
        final Argument third = getThirdArgument();
        if (isLiteralZero(second)) {
            return Optional.of("Dividing 0 (second argument) by anything will always result in 0.");
        }
        if (isLiteralZero(third)) {
            return Optional.of("Attempting to divide by 0 (third argument) will immediately cause the program to exit.");
        }
        if (isLiteralOne(third)) {
            return Optional.of("Dividing a value by 1 (third argument) will not change the value. It is recommended to use MOV <ADDRESS> ARG1 instead.");
        }
        if (second.isLiteral() && third.isLiteral()) {
            return Optional.of("Dividing with two literal inputs will always result in a constant value. It is recommended to compute the result yourself and use MOV <ADDRESS> <RESULT> instead.");
        }
        if (second.equals(third)) {
            return Optional.of("Dividing a value by itself will always result in 1. It is recommended to use MOV <ADDRESS> 1 instead.");
        }
        return super.checkSanityWarning();
    }

    @Override
    protected int getExactArgumentCount() {
        return 3;
    }

    @Override
    protected String getName() {
        return "DIV";
    }
}
