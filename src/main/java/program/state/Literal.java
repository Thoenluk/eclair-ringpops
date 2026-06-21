package main.java.program.state;

import java.util.HashMap;
import java.util.Map;

record Literal(int value) implements Argument {
    private static final Map<Integer, Literal> INSTANCES = new HashMap<>();

    public static Literal forValue(final int value) {
        return INSTANCES.computeIfAbsent(value, Literal::new);
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public int hashCode() {
        return getValue();
    }
}
