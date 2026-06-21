package main.java.program.state;

abstract class Register implements Argument {
    private int value = 0;

    @Override
    public int getAsInt() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    public void clear() {
        value = 0;
    }
}
