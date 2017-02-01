package com.marcowillemart.common.lang;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for the SimpleLine class.
 *
 * @author mwi
 */
public class SimpleLineTest {

    private Line target;

    @Test
    public void testIndentation_none() {
        // Setup
        target = createLine("message M {}");

        // Exercise & Verify
        assertTrue(target.indentation().isEmpty());
    }

    @Test
    public void testIndentation_space() {
        // Setup
        target = createLine("    message M {}");

        // Exercise & Verify
        assertEquals("    ", target.indentation());
    }

    @Test
    public void testIndentation_tab() {
        // Setup
        target = createLine("\tmessage M {}");

        // Exercise & Verify
        assertEquals("\t", target.indentation());
    }

    @Test
    public void testIndentation_emptyLine() {
        // Setup
        target = createLine("");

        // Exercise & Verify
        assertTrue(target.indentation().isEmpty());
    }

    @Test
    public void testIndentation_onlyWhitespaces() {
        // Setup
        target = createLine("  \t  ");

        // Exercise & Verify
        assertEquals("  \t  ", target.indentation());
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    private static Line createLine(String text)  {
        return new SimpleLine(1, 0, text.length(), text);
    }
}
