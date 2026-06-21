package main.java.program.instruction;

import java.util.Optional;

public class FunctionLabel extends Nop {
    private String conflictMessage = null;
    private final int label;

    protected FunctionLabel(final int label) {
        super();
        this.label = label;
    }

    public void setConflictMessage(final String conflictMessage) {
        this.conflictMessage = conflictMessage;
    }

    public int getLabel() {
        return label;
    }

    @Override
    public Optional<String> getError() {
        return Optional.ofNullable(conflictMessage).or(super::getError);
    }
}
