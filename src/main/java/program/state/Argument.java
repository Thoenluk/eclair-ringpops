package main.java.program.state;

import java.util.function.IntSupplier;

public interface Argument extends IntSupplier {
    String prettyPrint();

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
