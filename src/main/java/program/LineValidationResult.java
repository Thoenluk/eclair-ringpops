package main.java.program;

import java.util.HashMap;
import java.util.Map;

public record LineValidationResult(Type type, String message) {
    private static final Map<String, LineValidationResult> CACHE = new HashMap<>();
    private static final LineValidationResult VALID = new LineValidationResult(Type.VALID, null);

    public static LineValidationResult valid() {
        return VALID;
    }

    public static LineValidationResult warning(final String message) {
        return CACHE.computeIfAbsent(message, LineValidationResult::createWarning);
    }

    private static LineValidationResult createWarning(final String message) {
        return new LineValidationResult(Type.WARNING, message);
    }

    public static LineValidationResult error(final String message) {
        return CACHE.computeIfAbsent(message, LineValidationResult::createError);
    }

    public static LineValidationResult createError(final String message) {
        return new LineValidationResult(Type.ERROR, message);
    }

    public boolean isWarning() {
        return type == Type.WARNING;
    }

    public boolean isError() {
        return type == Type.ERROR;
    }

    private enum Type {
        VALID,
        WARNING,
        ERROR
    }
}
