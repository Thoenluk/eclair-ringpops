package main.java.program.state;

import java.util.HashMap;
import java.util.Map;

record Reference(Argument argument, State state) implements Argument {
    private static final Map<Argument, Reference> REFERENCES = new HashMap<>();

    public static Reference forArgument(final Argument argument, final State state) {
        return REFERENCES.computeIfAbsent(argument, arg -> new Reference(arg, state));
    }

    @Override
    public int getAsInt() {
        return state.getRegisterValue(argument.getValue());
    }

    @Override
    public String prettyPrint() {
        return "R" + argument.prettyPrint();
    }
}
