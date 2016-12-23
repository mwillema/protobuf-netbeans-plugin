package com.marcowillemart.common.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Pair class.
 *
 * @author mwi
 */
public class PairTest {

    private static final String LEFT = "StrElement";
    private static final Integer RIGHT = 65;

    private Pair<String, Integer> target;

    @Before
    public void setUp() {
        target = new Pair<>(LEFT, RIGHT);
    }

    @Test
    public void testLeft() {
        assertEquals("left", LEFT, target.left());
    }

    @Test
    public void testRight() {
        assertEquals("right", RIGHT, target.right());
    }

    @Test
    public void testEquals_same() {
        // Setup
        Pair<String, Integer> same = new Pair<>(LEFT, RIGHT);

        // Exercise & Verify
        assertTrue(target.equals(same));
    }

    @Test
    public void testEquals_otherLeftElt() {
        // Setup
        final String otherLeft = "OtherLeftElement";
        Pair<String, Integer> other = new Pair<>(otherLeft, RIGHT);

        // Exercise & Verify
        assertFalse(target.equals(other));
    }

    @Test
    public void testEquals_otherRightElt() {
        // Setup
        final Integer otherRight = 3;
        Pair<String, Integer> other = new Pair<>(LEFT, otherRight);

        // Exercise & Verify
        assertFalse(target.equals(other));
    }

    @Test
    public void testEquals_otherType() {
        // Setup
        Pair<String, String> other = new Pair<>(LEFT, RIGHT.toString());

        // Exercise & Verify
        assertFalse(target.equals(other));
    }

    @Test
    public void testHashCode_same() {
        // Setup
        Pair<String, Integer> same = new Pair<>(LEFT, RIGHT);

        // Exercise & Verify
        assertEquals("hashCode", same.hashCode(), target.hashCode());
    }

    @Test
    public void testHashCode_otherLeftElt() {
        // Setup
        final String otherLeft = "OtherLeftElement";
        Pair<String, Integer> other = new Pair<>(otherLeft, RIGHT);

        // Exercise & Verify
        assertTrue(other.hashCode() != target.hashCode());
    }

    @Test
    public void testHashCode_otherRightElt() {
        // Setup
        final Integer otherRight = 3;
        Pair<String, Integer> other = new Pair<>(LEFT, otherRight);

        // Exercise & Verify
        assertTrue(other.hashCode() != target.hashCode());
    }

    @Test
    public void testHashCode_otherType() {
        Pair<String, String> other;
        other = new Pair<>(LEFT, RIGHT.toString());

        // Exercise & Verify
        assertTrue(other.hashCode() != target.hashCode());
    }

    @Test
    public void testToString() {
        // Exercise
        String actual = target.toString();

        // Verify
        String expected = "(" + LEFT + ", " + RIGHT.toString() + ")";
        assertEquals("toString", expected, actual);
    }

    @Test
    public void testToString_withFormat() {
        // Setup
        String format = "%s + %s";

        // Exercise
        String actual = target.toString(format);

        // Verify
        String expected = LEFT + " + " + RIGHT.toString();
        assertEquals("toString", expected, actual);
    }

    @Test
    public void testOf() {
        assertEquals("new pair", target, Pair.of(LEFT, RIGHT));
    }
}
