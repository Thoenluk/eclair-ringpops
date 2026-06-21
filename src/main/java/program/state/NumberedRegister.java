package main.java.program.state;

public class NumberedRegister extends Register {
    private final int address;

    NumberedRegister(final int address) {
        super();
        this.address = address;
    }

    @Override
    public int getValue() {
        return super.getValue();
    }

    @Override
    public String prettyPrint() {
        return "R" + address;
    }
}
