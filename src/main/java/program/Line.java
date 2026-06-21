package main.java.program;

import main.java.program.instruction.Instruction;
import main.java.program.instruction.InstructionParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Line {
    private static final Pattern COMMENT_START = Pattern.compile("\\s+?[^a-zA-Z1-9]");

    private final String text;
    private final int endOfCode;
    private final Instruction instruction;
    private LineValidationResult validationResult;

    public Line(final String text, final InstructionParser instructionParser) {
        this.text = text;
        this.endOfCode = findEndOfCode();
        this.instruction = parseInstruction(instructionParser);
        validate();
    }

    private int findEndOfCode() {
        final Matcher matcher = COMMENT_START.matcher(text);
        return matcher.matches() ? matcher.start() : text.length();
    }

    private Instruction parseInstruction(final InstructionParser instructionParser) {
        final String code = text.substring(0, endOfCode).trim();
        return instructionParser.parse(code);
    }

    void validate() {
        validationResult = instruction.getError()
                .map(LineValidationResult::error)
                .or(() -> instruction.checkSanityWarning().map(LineValidationResult::warning))
                .orElseGet(LineValidationResult::valid);
    }

    public int getEndOfCode() {
        return endOfCode;
    }

    public String getText() {
        return text;
    }

    public boolean hasError() {
        return validationResult.isError();
    }

    public boolean hasWarning() {
        return validationResult.isWarning();
    }

    public String getValidationMessage() {
        return validationResult.message();
    }

    Instruction getInstruction() {
        return instruction;
    }
}
