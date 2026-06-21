package main.java.program.state;

import main.java.program.Parsing;
import main.java.program.ParsingException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ArgumentParser {
    private static final Map<String, Argument> LITERAL_CACHE = new HashMap<>();
    private static final Pattern LITERAL_PATTERN = Pattern.compile(Parsing.LITERAL_DESCRIPTION);
    private static final String ACC = "ACC";
    private static final String IP = "IP";
    private static final String STACK = "STK";
    private static final Pattern REGISTER_PATTERN = Pattern.compile("R+[^R]+");
    private static final char REGISTER_START = 'R';

    private final State state;

    public ArgumentParser(final State state) {
        this.state = state;
    }

    /**
     * @return An Argument whose behaviour is described by the given text, or an InvalidArgument if the text does not describe
     * a properly formatted argument.
     */
    public Argument parse(final String text) {
        if (isAcc(text)) {
            return state.getAcc();
        }
        if (isIp(text)) {
            return state.getIp();
        }
        if (isStack(text)) {
            return state.getStack();
        }
        if (isRegister(text)) {
            return createRegisterOrReference(text);
        }
        if (isLiteral(text)) {
            return parseLiteral(text);
        }
        return InvalidArgument.forMessage(String.format("%s is not a valid argument.", text));
    }

    private boolean isLiteral(final String text) {
        return LITERAL_PATTERN.matcher(text).matches();
    }

    private Argument parseLiteral(final String text) {
        return LITERAL_CACHE.computeIfAbsent(text, this::createLiteral);
    }

    private Argument createLiteral(final String text) {
        try {
            return Literal.forValue(Parsing.toInt(text));
        } catch (final ParsingException pe) {
            return InvalidArgument.forMessage(pe.getMessage());
        }
    }

    private boolean isAcc(final String text) {
        return ACC.equals(text);
    }

    private boolean isIp(final String text) {
        return IP.equals(text);
    }

    private boolean isStack(final String text) {
        return STACK.equals(text);
    }

    private boolean isRegister(final String text) {
        return REGISTER_PATTERN.matcher(text).matches();
    }

    private Argument createRegisterOrReference(final String text) {
        // Yes, there is a beautiful opportunity to make this recursively, but that would be terrible!
        // Not only would we have to run the same text minus one R through expensive checks several times, detecting whether
        // the innermost reference is even valid at the top level would be a pain. All because allowing RRRRRRACC seemed
        // funny at the time.
        // Instead, we use three cases:
        //  * Innermost argument isn't valid to begin with: Return an Invalid explaining what's wrong.
        //  * Register directly references a literal e.g. R0: Return the matching Register to save a lookup at runtime.
        //  * Else: Construct a chain of References, possibly shortcutting the last Register as in the above case.
        // This is unironically one of my favourite functions I have created in recent memory.
        final int numberOfNestedReferences = text.lastIndexOf(REGISTER_START);
        final Argument innermostArgument = parse(text.substring(numberOfNestedReferences + 1));
        if (innermostArgument.isInvalid()) {
            return InvalidArgument.forMessage(String.format("%s references an invalid value: %s", text, innermostArgument.getInvalidMessage()));
        }
        Argument outermostArgument = innermostArgument.isLiteral()
                ? state.getRegisterArgument(innermostArgument.getValue())
                : Reference.forArgument(innermostArgument, state);
        for (int i = 0; i < numberOfNestedReferences; i++) {
            outermostArgument = Reference.forArgument(outermostArgument, state);
        }
        return outermostArgument;
    }
}
