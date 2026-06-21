package main.java.program.instruction;

import java.util.Optional;

public interface Instruction {
    String prettyPrint();

    Optional<String> getError();

    default Optional<String> checkSanityWarning() {
        return Optional.empty();
    }
}
