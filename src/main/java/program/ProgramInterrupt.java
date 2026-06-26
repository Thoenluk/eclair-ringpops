package main.java.program;

public class ProgramInterrupt extends RuntimeException {
    public ProgramInterrupt(final String message) {
        super(message);
    }

    public ProgramInterrupt(final Throwable cause) {
        super(cause);
    }
}
