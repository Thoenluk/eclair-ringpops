package main.java.program.state;

import java.util.HashMap;
import java.util.Map;

public record InvalidArgument(String message) implements Argument {
    private static final Map<String, InvalidArgument> CACHE = new HashMap<>();

    public static InvalidArgument forMessage(final String message) {
        return CACHE.computeIfAbsent(message, InvalidArgument::new);
    }

    @Override
    public int getAsInt() {
        return 0;
    }

    @Override
    public boolean isInvalid() {
        return true;
    }

    @Override
    public String getInvalidMessage() {
        return message;
    }
}
