package main.java.program.state;

public class NamedRegister extends Register {
    private final String name;

    public NamedRegister(final String name) {
        this.name = name;
    }

    @Override
    public String prettyPrint() {
        return name;
    }
}
