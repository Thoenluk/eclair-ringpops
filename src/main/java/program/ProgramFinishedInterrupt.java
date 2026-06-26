package main.java.program;

public class ProgramFinishedInterrupt extends ProgramInterrupt {
    public ProgramFinishedInterrupt(final int returnValue) {
        final String message = String.format("Program finished successfully with return value %d!", returnValue);
        super(message);
    }
}
