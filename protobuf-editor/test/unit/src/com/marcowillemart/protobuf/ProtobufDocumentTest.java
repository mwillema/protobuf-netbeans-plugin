package com.marcowillemart.protobuf;

import com.marcowillemart.common.lang.Document;
import com.marcowillemart.common.lang.Line;
import com.marcowillemart.common.lang.SimpleLine;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the ProtobufDocument class.
 *
 * @author mwi
 */
public class ProtobufDocumentTest {

    private static final String INPUT =
            "syntax=\"proto\";\n"
            + "// a comment\n"
            + "message M { \n"
            + "\n"
            + "}\n"
            + "/** end of file */\n";

    private Document target;

    @Before
    public void setUp() {
        target = new ProtobufDocument(INPUT);
    }

    @Test
    public void testLineAt_firstLine() {
        // Setup
        Line expected = new SimpleLine(1, 0, 15, "syntax=\"proto\";");

        // Exercise
        Line actual = target.lineAt(5);

        // Verify
        assertEquals(expected, actual);
        assertFalse(actual.isLastOpeningBrace());
        assertFalse(actual.containsStartOfBlockComment());
    }

    @Test
    public void testLineAt_firstLine_firstOffset() {
        // Setup
        Line expected = new SimpleLine(1, 0, 15, "syntax=\"proto\";");

        // Exercise
        Line actual = target.lineAt(0);

        // Verify
        assertEquals(expected, actual);
    }

    @Test
    public void testLineAt_firstLine_lastOffset() {
        // Setup
        Line expected = new SimpleLine(1, 0, 15, "syntax=\"proto\";");

        // Exercise
        Line actual = target.lineAt(15);

        // Verify
        assertEquals(expected, actual);
    }

    @Test
    public void testLineAt_lineWithOpeningBrace() {
        // Setup
        Line expected = new SimpleLine(3, 29, 41, "message M { ");

        // Exercise
        Line actual = target.lineAt(35);

        // Verify
        assertEquals(expected, actual);
        assertTrue(actual.isLastOpeningBrace());
        assertFalse(actual.containsStartOfBlockComment());
    }

    @Test
    public void testLineAt_lineWithOpeningBrace_firstOffset() {
        // Setup
        Line expected = new SimpleLine(3, 29, 41, "message M { ");

        // Exercise
        Line actual = target.lineAt(29);

        // Verify
        assertEquals(expected, actual);
    }

    @Test
    public void testLineAt_lineWithOpeningBrace_lastOffset() {
        // Setup
        Line expected = new SimpleLine(3, 29, 41, "message M { ");

        // Exercise
        Line actual = target.lineAt(41);

        // Verify
        assertEquals(expected, actual);
    }

    @Test
    public void testLineAt_emptyLine() {
        // Setup
        Line expected = new SimpleLine(4, 42, 42, "");

        // Exercise
        Line actual = target.lineAt(42);

        // Verify
        assertEquals(expected, actual);
        assertFalse(actual.isLastOpeningBrace());
        assertFalse(actual.containsStartOfBlockComment());
    }

    @Test
    public void testLineAt_tokenOnMultiLine() {
        // Setup
        final String input =
                "/** \n" +
                " * \n" +
                " */\n";
        target = new ProtobufDocument(input);
        Line expected = new SimpleLine(1, 0, 4, "/** ");

        // Exercise
        Line actual = target.lineAt(0);

        // Verify
        assertEquals(expected, actual);
        assertFalse(actual.isLastOpeningBrace());
        assertTrue(actual.containsStartOfBlockComment());
    }

    @Test
    public void testLineAt_EOF() {
        // Setup
        final String input =
                "/**\n";
        target = new ProtobufDocument(input);
        Line expected = new SimpleLine(2, 4, 4, "");

        // Exercise
        Line actual = target.lineAt(4);

        // Verify
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldCloseBlockComment_true() {
        // Setup
        final String input =
                "/** \n" +
                 "* \n";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertTrue(target.shouldCloseBlockComment(2));
        assertTrue(target.shouldCloseBlockComment(3));
        assertTrue(target.shouldCloseBlockComment(4));
    }

    @Test
    public void testShouldCloseBlockComment_false() {
        // Setup
        final String input =
                "/** \n" +
                 "* \n";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertFalse(target.shouldCloseBlockComment(0));
        assertFalse(target.shouldCloseBlockComment(1));
        assertFalse(target.shouldCloseBlockComment(5));
        assertFalse(target.shouldCloseBlockComment(6));
        assertFalse(target.shouldCloseBlockComment(7));
    }

    @Test
    public void testShouldCloseBlockComment_beforeAnotherBlockComment() {
        // Setup
        final String input =
                "/*\n" +
                "/* */\n";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertFalse(target.shouldCloseBlockComment(0));
        assertFalse(target.shouldCloseBlockComment(1));
        assertTrue(target.shouldCloseBlockComment(2));
    }

    @Test
    public void testShouldCloseBlockComment_beforeAnotherBlockCommentOpening() {
        // Setup
        final String input =
                "/*\n" +
                "/*\n";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertFalse(target.shouldCloseBlockComment(0));
        assertFalse(target.shouldCloseBlockComment(1));
        assertTrue(target.shouldCloseBlockComment(2));
    }

    @Test
    public void testShouldCloseBlockComment_inEmptyDocument() {
        // Setup
        final String input = "/*";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertFalse(target.shouldCloseBlockComment(0));
        assertFalse(target.shouldCloseBlockComment(1));
        assertTrue(target.shouldCloseBlockComment(2));
    }

    @Test
    public void testShouldAddLineToBlockComment_true() {
        // Setup
        final String input =
                "/**\n" +
                 "* \n" +
                 "*/\n";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertTrue(target.shouldAddLineToBlockComment(2));
        assertTrue(target.shouldAddLineToBlockComment(3));
        assertTrue(target.shouldAddLineToBlockComment(5));
        assertTrue(target.shouldAddLineToBlockComment(6));
        assertTrue(target.shouldAddLineToBlockComment(8));
    }

    @Test
    public void testShouldAddLineToBlockComment_false() {
        // Setup
        final String input =
                "/**\n" +
                 "* \n" +
                 "*/\n";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertFalse(target.shouldAddLineToBlockComment(0));
        assertFalse(target.shouldAddLineToBlockComment(1));
        assertFalse(target.shouldAddLineToBlockComment(4));
        assertFalse(target.shouldAddLineToBlockComment(7));
        assertFalse(target.shouldAddLineToBlockComment(9));
        assertFalse(target.shouldAddLineToBlockComment(10));
    }

    @Test
    public void testShouldAddLineToBlockComment_unclosed() {
        // Setup
        final String input =
                "/** \n" +
                 "* \n";
        target = new ProtobufDocument(input);

        // Exercise & Verify
        assertFalse(target.shouldAddLineToBlockComment(0));
        assertFalse(target.shouldAddLineToBlockComment(1));
        assertFalse(target.shouldAddLineToBlockComment(2));
        assertFalse(target.shouldAddLineToBlockComment(3));
        assertFalse(target.shouldAddLineToBlockComment(4));
        assertFalse(target.shouldAddLineToBlockComment(5));
        assertTrue(target.shouldAddLineToBlockComment(6));
        assertTrue(target.shouldAddLineToBlockComment(7));
        assertFalse(target.shouldAddLineToBlockComment(8));
    }
}
