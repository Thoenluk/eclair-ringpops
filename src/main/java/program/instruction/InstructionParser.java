package main.java.program.instruction;

import main.java.program.Parsing;
import main.java.program.ParsingException;
import main.java.program.state.Argument;
import main.java.program.state.ArgumentParser;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class InstructionParser {
    private static final String WHITESPACE = "\\s+";
    private static final Pattern FUNCTION_LABEL_PATTERN = Pattern.compile(Parsing.LITERAL_DESCRIPTION + ":");
    private static final Map<String, Function<Argument[], ValidInstruction>> CONSTRUCTORS = Map.ofEntries(
            entry("ACC", Acc::new),
            entry("ADD", Add::new),
            entry("AND", And::new),
            entry("BRK", Brk::new),
            entry("CAL", Cal::new),
            entry("DIV", Div::new),
            entry("JIF", Jif::new),
            entry("JIR", Jir::new),
            entry("JMP", Jmp::new),
            entry("JRL", Jrl::new),
            entry("LSH", Lsh::new),
            entry("MOV", Mov::new),
            entry("MUL", Mul::new),
            entry("NOP", Nop::new),
            entry("ORR", Orr::new),
            entry("POP", Pop::new),
            entry("PRC", Prc::new),
            entry("PRT", Prt::new),
            entry("PSH", Psh::new),
            entry("RET", Ret::new),
            entry("RSH", Rsh::new),
            entry("SUB", Sub::new),
            entry("XOR", Xor::new)
    );

    private final ArgumentParser argumentParser;

    public InstructionParser(final ArgumentParser argumentParser) {
        this.argumentParser = argumentParser;
    }

    public Instruction parse(final String code) {
        if (code.isBlank()) {
            return Nop.INSTANCE;
        }
        if (isFunctionLabel(code)) {
            return parseFunctionLabel(code);
        }
        final String[] parts = code.split(WHITESPACE);
        final String opcode = parts[0];
        if (!CONSTRUCTORS.containsKey(opcode)) {
            return InvalidInstruction.forMessage(String.format("%s is not a valid opcode", opcode));
        }
        final Argument[] arguments = parseArguments(parts);
        return CONSTRUCTORS.get(opcode).apply(arguments);
    }

    private boolean isFunctionLabel(final String code) {
        return FUNCTION_LABEL_PATTERN.matcher(code).matches();
    }

    private Instruction parseFunctionLabel(final String code) {
        final String number = code.substring(0, code.length() - 1);
        final int label;
        try {
            label = Parsing.toInt(number);
        } catch (final ParsingException pe) {
            return InvalidInstruction.forMessage(pe.getMessage());
        }
        return new FunctionLabel(label);
    }

    private Argument[] parseArguments(final String[] parts) {
        final Argument[] arguments = new Argument[parts.length - 1];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = argumentParser.parse(parts[i + 1]);
        }
        return arguments;
    }
}
