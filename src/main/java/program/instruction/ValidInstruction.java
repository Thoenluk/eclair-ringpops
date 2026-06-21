package main.java.program.instruction;

import main.java.program.state.Argument;
import main.java.program.state.State;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ValidInstruction implements Instruction {
    protected final Argument[] arguments;

    protected ValidInstruction(final Argument[] arguments) {
        this.arguments = arguments;
    }

    public abstract void execute(State state);

    protected abstract int getExactArgumentCount();

    protected abstract String getName();

    @Override
    public String prettyPrint() {
        return getName() + " " + Arrays.stream(arguments).map(Argument::prettyPrint).collect(Collectors.joining(" "));
    }

    @Override
    public Optional<String> getError() {
        return checkArgumentList();
    }

    /**
     * @return If the instruction was given an invalid argument list (usually, incorrect length or at least one invalid argument),
     * a present Optional containing a formatted String describing the error.
     * If the argument list is valid, an empty Optional.
     */
    public Optional<String> checkArgumentList() {
        return Arrays.stream(arguments)
                .filter(Argument::isInvalid)
                .findFirst()
                .map(Argument::getInvalidMessage)
                .or(this::checkArgumentCount);
    }

    private Optional<String> checkArgumentCount() {
        final int actual = getActualArgumentCount();
        if (actual < getMinimumArgumentCount()) {
            return Optional.of(String.format("%s takes at least %d arguments, but %d were provided!", getName(), getMinimumArgumentCount(), actual));
        } else if (actual > getMaximumArgumentCount()) {
            return Optional.of(String.format("%s takes at most %d arguments, but %d were provided!", getName(), getMaximumArgumentCount(), actual));
        }
        return Optional.empty();
    }

    protected int getMinimumArgumentCount() {
        return getExactArgumentCount();
    }

    protected int getMaximumArgumentCount() {
        return getExactArgumentCount();
    }

    private int getActualArgumentCount() {
        return arguments.length;
    }

    protected Argument getFirstArgument() {
        return arguments[0];
    }

    protected int getFirstArgumentValue() {
        return getFirstArgument().getValue();
    }

    protected Argument getSecondArgument() {
        return arguments[1];
    }

    protected int getSecondArgumentValue() {
        return getSecondArgument().getValue();
    }

    protected Argument getThirdArgument() {
        return arguments[2];
    }

    protected int getThirdArgumentValue() {
        return getThirdArgument().getValue();
    }

    protected boolean isLiteralZero(final Argument arg) {
        return isLiteralWithValue(arg, 0);
    }

    protected boolean isLiteralOne(final Argument arg) {
        return isLiteralWithValue(arg, 1);
    }

    protected boolean isLiteralAllOnes(final Argument arg) {
        return isLiteralWithValue(arg, 0xFFFFFFFF);
    }

    private static boolean isLiteralWithValue(final Argument arg, final int value) {
        return arg.isLiteral() && arg.getValue() == value;
    }

    protected boolean isLiteralNegative(final Argument arg) {
        return arg.isLiteral() && arg.getValue() < 0;
    }
}
