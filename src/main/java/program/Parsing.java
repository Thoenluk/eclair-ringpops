package main.java.program;

import static java.lang.Integer.parseInt;

public class Parsing {
    public static final String LITERAL_DESCRIPTION = "-?(?:0B[01](?:[01_]?[01])*|0X[\\dA-F](?:[\\dA-F_]?[\\dA-F])*|\\d(?:[\\d_]?\\d)*)";

    public static int toInt(final String text) throws ParsingException {
        final Long actualValue;
        try {
            if (text.length() == 1) {
                return parseInt(text);
            }
            actualValue = findActualValue(text);
        } catch (final NumberFormatException nfe) {
            throw new ParsingException(String.format("%s is not a valid number.", text));
        }
        if (actualValue == null) {
            throw new ParsingException(String.format("%s is not a valid radix indicator. The only supported radixes are 0B" +
                    " for binary, 0X for hexadecimal, or no prefix for decimal.", text.substring(0, 2)));
        } else if (actualValue < Integer.MIN_VALUE || actualValue > Integer.MAX_VALUE * 2L) {
            throw new ParsingException(String.format("%s is valid, but outside the bounds of a 32 bit integer (unsigned value %d).", text, actualValue));
        }
        // So fun fact, parseInt() and its binary-hating brother decode() do not permit Strings which describe a negative
        // number, i.e. greater than 0x7FFFFFFF or equivalent, despite these being valid representations. You are supposed
        // to pass in only positive numbers with an optional minus sign in front. Unfortunately, I already wrote the part
        // of the spec that says bitwise negative numbers are accepted. What was I going to do, change it?
        final long wrapped = actualValue <= Integer.MAX_VALUE
                ? actualValue
                : actualValue - 2L * Integer.MAX_VALUE;
        return (int) wrapped;
    }

    private static Long findActualValue(final String text) {
        final int radix;
        final int beginIndex;
        final char radixIndicator = text.charAt(1);
        if (radixIndicator == 'B') {
            radix = 2;
            beginIndex = 2;
        } else if (radixIndicator == 'X') {
            radix = 16;
            beginIndex = 2;
        } else if (radixIndicator >= '0' && radixIndicator <= '9') {
            radix = 10;
            beginIndex = 0;
        } else {
            return null;
        }
        final String underscoresRemoved = text.replace("_", "");
        return Long.parseLong(underscoresRemoved, beginIndex, text.length(), radix);
    }

    private Parsing() {
    }
}
