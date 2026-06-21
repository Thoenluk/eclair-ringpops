package main.java.program.instruction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InvalidInstruction implements Instruction {
    private static final Map<String, InvalidInstruction> CACHE = new HashMap<>();

    public static InvalidInstruction forMessage(final String message) {
        return CACHE.computeIfAbsent(message, InvalidInstruction::new);
    }

    private final String formatError;

    private InvalidInstruction(final String formatError) {
        this.formatError = formatError;
    }

    @Override
    public String prettyPrint() {
        return "";
    }

    @Override
    public Optional<String> getError() {
        return Optional.ofNullable(formatError);
    }
}
