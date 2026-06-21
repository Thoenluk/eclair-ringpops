package main.java.program.state;

import java.util.function.IntSupplier;

public interface Argument extends IntSupplier {
    default int getValue() {
        return getAsInt();
    }

    default boolean isLiteral() {
        return false;
    }

    default boolean isInvalid() {
        return false;
    }

    default String getInvalidMessage() {
        return null;
    }
}
