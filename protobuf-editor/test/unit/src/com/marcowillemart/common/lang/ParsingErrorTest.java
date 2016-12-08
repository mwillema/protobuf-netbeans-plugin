package com.marcowillemart.common.lang;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the ParsingError class.
 *
 * @author mwi
 */
public class ParsingErrorTest {

    private static final String MSG = "My error";
    private static final int LINE = 5;
    private static final int COL = 9;

    private ParsingError target;
    private ParsingError same;
    private ParsingError different1;
    private ParsingError different2;
    private ParsingError different3;

    @Before
    public void setUp() {
        target = new ParsingError(MSG, LINE, COL);
        same = new ParsingError(MSG, LINE, COL);
        different1 = new ParsingError("otherError", LINE,COL);
        different2 = new ParsingError(MSG, 2, COL);
        different3 = new ParsingError(MSG, LINE, 2);
    }

    @Test
    public void testMessage() {
        assertEquals("message", MSG, target.message());
    }

    @Test
    public void testLine() {
        assertEquals("line", LINE, target.line());
    }

    @Test
    public void testCol() {
        assertEquals("col", COL, target.col());
    }

    @Test
    public void testConstructor_boundaryCase() {
        // Setup
        final int line = 1;
        final int col = 1;

        // Exercise
        target = new ParsingError(MSG, line, col);

        // Verify
        assertEquals(line, target.line());
        assertEquals(col, target.col());
    }

    @Test
    public void testEquals_same() {
        // Exercise & Verify
        assertTrue(target.equals(same));
    }

    @Test
    public void testEquals_different() {
        // Exercise & Verify
        assertFalse(target.equals(different1));
        assertFalse(target.equals(different2));
        assertFalse(target.equals(different3));
    }

    @Test
    public void testHashCode_same() {
        // Exercise & Verify
        assertEquals("hashCode", same.hashCode(), target.hashCode());
    }

    @Test
    public void testHashCode_different(){
        // Exercise & Verify
        assertTrue(different1.hashCode() != target.hashCode());
        assertTrue(different2.hashCode() != target.hashCode());
        assertTrue(different3.hashCode() != target.hashCode());
    }

    @Test
    public void testToString() {
        // Exercise
        String str = target.toString();

        // Verify
        assertTrue(str.contains(MSG));
        assertTrue(str.contains(String.valueOf(LINE)));
        assertTrue(str.contains(String.valueOf(COL)));
    }
}
