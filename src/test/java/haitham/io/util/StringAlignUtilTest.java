package haitham.io.util;

import haitham.io.util.StringAlignUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringAlignUtilTest {

    @Test
    public void splitEmptyLine() {
        assertEquals("", new StringAlignUtil().alignCenter("", 9));
    }

    @Test
    public void splitNullLine() {
        assertNull(new StringAlignUtil().alignCenter(null, 9));
    }

    @Test
    public void splitLineWithBorderLessThanInputLength() {
        String input = "012 456 890 23_ 56_890";

        String expected = "012   456\n890   23_\n56_890";
        String actual = new StringAlignUtil().alignCenter(input, 9);

        assertEquals(expected, actual);
    }

    @Test
    public void splitLineWithBorderMoreThanInputLengthWithEvenInternalSpaces() {
        String input = "012 456 890 23_ 56_890";

        String expected = "012   456   890   23_   56_890";
        final int border = expected.length();
        String actual = new StringAlignUtil().alignCenter(input, border);

        assertEquals(expected, actual);
    }

    @Test
    public void splitLineWithBorderMoreThanInputLengthWithUnEvenInternalSpaces() {
        String input = "012 456 890 23_ 56_890";
        String expected = "012   456   890   23_   56_890";
        final int border = expected.length();

        assertEquals(expected, new StringAlignUtil().alignCenter(input, border));
    }

    @Test
    public void splitLineWithoutSpacesWithBorderEqualsToLineLength() {
        String input = "0000011111222223333344444";
        final int border = input.length();

        assertEquals(input, new StringAlignUtil().alignCenter(input, border));
    }

    @Test
    public void splitLineWithoutSpacesWithBorderLessThanLineLength() {
        String input = "0000011111222223333344444";
        String expected = "00000\n11111\n22222\n33333\n44444";
        final int border = 5;

        assertEquals(expected, new StringAlignUtil().alignCenter(input, border));
    }

    @Test
    public void splitLineWithoutSpacesWithBorderMoreThanLineLengthWithEvenSpaces() {
        String input = "0000011111222223333344444";
        String expected = "      0000011111222223333344444      ";
        final int border = input.length() + 12;

        assertEquals(expected, new StringAlignUtil().alignCenter(input, border));
    }

    @Test
    public void splitLineWithoutSpacesWithBorderMoreThanLineLengthWithOddSpaces() {
        String input = "0000011111222223333344444";
        final int border = input.length() + 13;

        String expected1 = "      0000011111222223333344444       ";
        String expected2 = "       0000011111222223333344444      ";
        String actual = new StringAlignUtil().alignCenter(input, border);

        assertTrue(actual.equals(expected1) || actual.equals(expected2));
    }
}
