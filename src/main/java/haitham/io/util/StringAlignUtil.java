package haitham.io.util;

import java.util.Arrays;
import java.util.List;

public class StringAlignUtil {

    public String alignCenter(String text, int border) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        final int textLength = text.trim().length();
        final StringBuilder result = new StringBuilder();
        // split input text into tokens, with a space delimiter
        final List<String> tokens = Arrays.asList(text.split(" "));
        // store num of tokens
        final int numOfTokens = tokens.size();

        // handle long text with no spaces
        if (numOfTokens == 1) {

            if (border == textLength) {
                return text;
            }

            if (border < textLength) {
                for (int i = 0; i < text.length(); i++) {
                    if ((i != 0 && i % border == 0)) {
                        result.append('\n');
                    }
                    result.append(text.charAt(i));
                }
                return result.toString();
            }

//            if: border > text.length()
            final int spacesInGap = (border - textLength) / 2;
            return result.append(" ".repeat(spacesInGap)).append(text).append(" ".repeat(border - textLength - spacesInGap)).toString();
        }

        //
        int lineWidth = 0;
        int startIndex = 0;

        for (int currentTokenIndex = 0; currentTokenIndex < numOfTokens; currentTokenIndex++) {
            int tokenLength = tokens.get(currentTokenIndex).length();

            if (lineWidth + tokenLength > border) {
                // reset line width
                lineWidth = tokenLength;

                final String line = splitAtBorder(tokens.subList(startIndex, currentTokenIndex), border);
                result.append(line).append("\n");
                startIndex = currentTokenIndex;
            } else {
                lineWidth += tokenLength + 1; // add a single space after each token
            }
        }

        if (result.length() == 0) {
            result.append(splitAtBorder(tokens, border));
        } else {
            // append last token
            result.append(tokens.get(numOfTokens - 1));

        }

        return result.toString();
    }

    public String splitAtBorder(List<String> tokens, int border) {
        String line = String.join(" ", tokens);
        final int fullLength = line.length();
        final int numOfGaps = tokens.size() - 1;
        final int numOfTotalInnerSpacesNeeded = Math.abs(fullLength - border);

        if (numOfTotalInnerSpacesNeeded == 0) {
            return line;
        }

        final int gapLength = numOfTotalInnerSpacesNeeded / numOfGaps;
        return line.replaceAll(" ", " ".repeat(gapLength + 1));
    }
}
