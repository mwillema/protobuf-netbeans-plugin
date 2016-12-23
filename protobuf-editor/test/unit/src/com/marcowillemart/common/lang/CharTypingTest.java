package com.marcowillemart.common.lang;

import com.marcowillemart.protobuf.ProtobufDocument;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for the CharTyping class.
 *
 * @author mwi
 */
public class CharTypingTest {

    private static final char CARET = '|';

    private CharTyping target;

    private String text;
    private int offset;
    private char typedChar;

    @Test
    public void testConstructor() {
        // Setup
        String editedText = "class ";
        final String expectedInsertionText = "M";

        // Exercise
        target = new CharTyping(
                new ProtobufDocument(editedText),
                editedText.length(),
                expectedInsertionText.charAt(0));

        // Verify
        assertEquals(expectedInsertionText, target.insertionText());
        assertEquals(editedText.length() + 1, target.insertionOffset());
    }

    @Test
    public void testTypeLeftParenthesis() {
        // Setup
        setUp("m|");

        // Exercise
        typeChar('(');

        // Verify
        assertTextEquals("m(|)");
    }

    @Test
    public void testTypeLeftBracket() {
        // Setup
        setUp("m |");

        // Exercise
        typeChar('[');

        // Verify
        assertTextEquals("m [|]");
    }

    @Test
    public void testTypeLeftBrace() {
        // Setup
        setUp("|");

        // Exercise
        typeChar('{');

        // Verify
        assertTextEquals("{|}");
    }

    @Test
    public void testTypeOpeningChar_beforeString() {
        // Setup
        setUp("|\"proto\"");

        // Exercise
        typeChar('(');

        // Verify
        assertTextEquals("(|)\"proto\"");
    }

    @Test
    public void testTypeOpeningChar_afterString() {
        // Setup
        setUp("\"proto\"|");

        // Exercise
        typeChar('[');

        // Verify
        assertTextEquals("\"proto\"[|]");
    }

    @Test
    public void testTypeOpeningChar_startOfString() {
        // Setup
        setUp("syntax = \"|proto\"");

        // Exercise
        typeChar('{');

        // Verify
        assertTextEquals("syntax = \"{|proto\"");
    }

    @Test
    public void testTypeOpeningChar_endOfString() {
        // Setup
        setUp("syntax = \"proto|\"");

        // Exercise
        typeChar('(');

        // Verify
        assertTextEquals("syntax = \"proto(|\"");
    }

    @Test
    public void testTypeOpeningChar_insideString() {
        // Setup
        setUp("syntax = \"pro|to\"");

        // Exercise
        typeChar('[');

        // Verify
        assertTextEquals("syntax = \"pro[|to\"");
    }

    @Test
    public void testTypeOpeningChar_beforeLineComment() {
        // Setup
        setUp("\n|// a comment\n");

        // Exercise
        typeChar('{');

        // Verify
        assertTextEquals("\n{|}// a comment\n");
    }

    @Test
    public void testTypeOpeningChar_afterLineComment() {
        // Setup
        setUp("\n// a comment\n|");

        // Exercise
        typeChar('[');

        // Verify
        assertTextEquals("\n// a comment\n[|]");
    }

    @Test
    public void testTypeOpeningChar_startOfLineComment() {
        // Setup
        setUp("\n/|/ a comment\n");

        // Exercise
        typeChar('(');

        // Verify
        assertTextEquals("\n/(|/ a comment\n");
    }

    @Test
    public void testTypeOpeningChar_endOfLineComment() {
        // Setup
        setUp("\n// a comment|\n");

        // Exercise
        typeChar('{');

        // Verify
        assertTextEquals("\n// a comment{|\n");
    }

    @Test
    public void testTypeOpeningChar_insideLineComment() {
        // Setup
        setUp("\n// a |comment\n");

        // Exercise
        typeChar('[');

        // Verify
        assertTextEquals("\n// a [|comment\n");
    }

    @Test
    public void testTypeOpeningChar_beforeBlockComment() {
        // Setup
        setUp("|/**\n * \n */");

        // Exercise
        typeChar('[');

        // Verify
        assertTextEquals("[|]/**\n * \n */");
    }

    @Test
    public void testTypeOpeningChar_afterBlockComment() {
        // Setup
        setUp("/**\n * \n */|");

        // Exercise
        typeChar('(');

        // Verify
        assertTextEquals("/**\n * \n */(|)");
    }

    @Test
    public void testTypeOpeningChar_startOfBlockComment() {
        // Setup
        setUp("/|**\n * \n */");

        // Exercise
        typeChar('{');

        // Verify
        assertTextEquals("/{|**\n * \n */");
    }

    @Test
    public void testTypeOpeningChar_endOfBlockComment() {
        // Setup
        setUp("/**\n * \n *|/");

        // Exercise
        typeChar('(');

        // Verify
        assertTextEquals("/**\n * \n *(|/");
    }

    @Test
    public void testTypeOpeningChar_insideBlockComment() {
        // Setup
        setUp("/**\n * |\n */");

        // Exercise
        typeChar('[');

        // Verify
        assertTextEquals("/**\n * [|\n */");
    }

    @Test
    public void testTypeOpeningChar_betweenComments() {
        // Setup
        setUp("\n/* */|// my comment\n");

        // Exercise
        typeChar('{');

        // Verify
        assertTextEquals("\n/* */{|}// my comment\n");
    }

    @Test
    public void testTypeRightParenthesis() {
        // Setup
        setUp("m(|)");

        // Exercise
        typeChar(')');

        // Verify
        assertTextEquals("m()|");
    }

    @Test
    public void testTypeRightBracket() {
        // Setup
        setUp("m [|]");

        // Exercise
        typeChar(']');

        // Verify
        assertTextEquals("m []|");
    }

    @Test
    public void testTypeRightBrace() {
        // Setup
        setUp("{|}");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("{}|");
    }

    @Test
    public void testTypeClosingChar_startOfDocument() {
        // Setup
        setUp("|}");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("}|");
    }

    @Test
    public void testTypeClosingChar_endOfDocument() {
        // Setup
        setUp(")|");

        // Exercise
        typeChar(')');

        // Verify
        assertTextEquals("))|");
    }

    @Test
    public void testTypeClosingChar_emptyDocument() {
        // Setup
        setUp("|");

        // Exercise
        typeChar(']');

        // Verify
        assertTextEquals("]|");
    }

    @Test
    public void testTypeClosingChar_beforeString() {
        // Setup
        setUp("|\"proto\"");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("}|\"proto\"");
    }

    @Test
    public void testTypeClosingChar_afterString() {
        // Setup
        setUp("\"proto\"|)");

        // Exercise
        typeChar(')');

        // Verify
        assertTextEquals("\"proto\")|");
    }

    @Test
    public void testTypeClosingChar_startOfString() {
        // Setup
        setUp("syntax = \"|]proto\"");

        // Exercise
        typeChar(']');

        // Verify
        assertTextEquals("syntax = \"]|]proto\"");
    }

    @Test
    public void testTypeClosingChar_endOfString() {
        // Setup
        setUp("syntax = \"proto|\")");

        // Exercise
        typeChar(')');

        // Verify
        assertTextEquals("syntax = \"proto)|\")");
    }

    @Test
    public void testTypeClosingChar_insideString() {
        // Setup
        setUp("syntax = \"pro|}to\"");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("syntax = \"pro}|}to\"");
    }

    @Test
    public void testTypeClosingChar_beforeLineComment() {
        // Setup
        setUp("\n|// a comment\n");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("\n}|// a comment\n");
    }

    @Test
    public void testTypeClosingChar_afterLineComment() {
        // Setup
        setUp("\n// a comment\n|]");

        // Exercise
        typeChar(']');

        // Verify
        assertTextEquals("\n// a comment\n]|");
    }

    @Test
    public void testTypeClosingChar_startOfLineComment() {
        // Setup
        setUp("\n/|/ a comment\n");

        // Exercise
        typeChar(')');

        // Verify
        assertTextEquals("\n/)|/ a comment\n");
    }

    @Test
    public void testTypeClosingChar_endOfLineComment() {
        // Setup
        setUp("\n// a comment|\n}");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("\n// a comment}|\n}");
    }

    @Test
    public void testTypeClosingChar_insideLineComment() {
        // Setup
        setUp("\n// a |]comment\n");

        // Exercise
        typeChar(']');

        // Verify
        assertTextEquals("\n// a ]|]comment\n");
    }

    @Test
    public void testTypeClosingChar_beforeBlockComment() {
        // Setup
        setUp("|/**\n * \n */");

        // Exercise
        typeChar(']');

        // Verify
        assertTextEquals("]|/**\n * \n */");
    }

    @Test
    public void testTypeClosingChar_afterBlockComment() {
        // Setup
        setUp("/**\n * \n */|)");

        // Exercise
        typeChar(')');

        // Verify
        assertTextEquals("/**\n * \n */)|");
    }

    @Test
    public void testTypeClosingChar_startOfBlockComment() {
        // Setup
        setUp("/|**\n * \n */");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("/}|**\n * \n */");
    }

    @Test
    public void testTypeClosingChar_endOfBlockComment() {
        // Setup
        setUp("/**\n * \n *|/)");

        // Exercise
        typeChar(')');

        // Verify
        assertTextEquals("/**\n * \n *)|/)");
    }

    @Test
    public void testTypeClosingChar_insideBlockComment() {
        // Setup
        setUp("/**\n * |]\n */");

        // Exercise
        typeChar(']');

        // Verify
        assertTextEquals("/**\n * ]|]\n */");
    }

    @Test
    public void testTypeClosingChar_betweenComments() {
        // Setup
        setUp("\n/* */|// my comment\n");

        // Exercise
        typeChar('}');

        // Verify
        assertTextEquals("\n/* */}|// my comment\n");
    }

    @Test
    public void testTypeOpeningDoubleQuote() {
        // Setup
        setUp("syntax=|\n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("syntax=\"|\"\n");
    }

    @Test
    public void testTypeClosingDoubleQuote() {
        // Setup
        setUp("syntax=\"|\"\n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("syntax=\"\"|\n");
    }

    @Test
    public void testTypeEscapedClosingDoubleQuote() {
        // Setup
        setUp("syntax=\"\\|\"\n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("syntax=\"\\\"|\"\n");
    }

    @Test
    public void testTypeOpeningSingleQuote() {
        // Setup
        setUp("syntax = |\n");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("syntax = '|'\n");
    }

    @Test
    public void testTypeClosingSingleQuote() {
        // Setup
        setUp("syntax = '|'\n");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("syntax = ''|\n");
    }

    @Test
    public void testTypeEscapedClosingSingleQuote() {
        // Setup
        setUp("syntax = '\\|'\n");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("syntax = '\\'|'\n");
    }

    @Test
    public void testTypeQuote_beginningOfDocument() {
        // Setup
        setUp("|  ");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\"|\"  ");
    }

    @Test
    public void testTypeQuote_endOfDocument() {
        // Setup
        setUp("  |");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("  '|'");
    }

    @Test
    public void testTypeQuote_emptyDocument() {
        // Setup
        setUp("|");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\"|\"");
    }

    @Test
    public void testTypeQuote_beforeString() {
        // Setup
        setUp("|'proto'");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("'|'proto'");
    }

    @Test
    public void testTypeQuote_longBeforeString() {
        // Setup
        setUp("|  \"proto\"");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\"|  \"proto\"");
    }

    @Test
    public void testTypeQuote_afterString() {
        // Setup
        setUp("\"proto\"|");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\"proto\"\"|\"");
    }

    @Test
    public void testTypeQuote_startOfString() {
        // Setup
        setUp("syntax = \"|proto\"");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("syntax = \"\"|proto\"");
    }

    @Test
    public void testTypeQuote_endOfString() {
        // Setup
        setUp("syntax = 'proto|'");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("syntax = 'proto'|");
    }

    @Test
    public void testTypeQuote_insideString() {
        // Setup
        setUp("syntax = \"pro|to\"");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("syntax = \"pro'|to\"");
    }

    @Test
    public void testTypeQuote_beforeLineComment() {
        // Setup
        setUp("\n|// a comment\n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\n\"|\"// a comment\n");
    }

    @Test
    public void testTypeQuote_afterLineComment() {
        // Setup
        setUp("\n// a comment\n|");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("\n// a comment\n'|'");
    }

    @Test
    public void testTypeQuote_startOfLineComment() {
        // Setup
        setUp("\n/|/ a comment\n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\n/\"|/ a comment\n");
    }

    @Test
    public void testTypeQuote_endOfLineComment() {
        // Setup
        setUp("\n// a comment |\n");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("\n// a comment '|\n");
    }

    @Test
    public void testTypeQuote_insideLineComment() {
        // Setup
        setUp("\n// a |comment\n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\n// a \"|comment\n");
    }

    @Test
    public void testTypeQuote_beforeBlockComment() {
        // Setup
        setUp("|/**\n * \n */");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\"|\"/**\n * \n */");
    }

    @Test
    public void testTypeQuote_afterBlockComment() {
        // Setup
        setUp("/**\n * \n */|");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("/**\n * \n */'|'");
    }

    @Test
    public void testTypeQuote_startOfBlockComment() {
        // Setup
        setUp("/|**\n * \n */");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("/\"|**\n * \n */");
    }

    @Test
    public void testTypeQuote_endOfBlockComment() {
        // Setup
        setUp("/**\n * \n *|/");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("/**\n * \n *'|/");
    }

    @Test
    public void testTypeQuote_insideBlockComment() {
        // Setup
        setUp("/**\n * '|'\n */");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("/**\n * ''|'\n */");
    }

    @Test
    public void testTypeQuote_betweenComments() {
        // Setup
        setUp("\n/* */|// my comment\n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\n/* */\"|\"// my comment\n");
    }


    @Test
    public void testTypeQuote_whiteSpaceArea() {
        // Setup
        setUp("  |  ");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("  \"|\"  ");
    }

    @Test
    public void testTypeQuote_EOL() {
        // Setup
        setUp("  |\n");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("  '|'\n");
    }

    @Test
    public void tesTypeQuote_unterminated() {
        // Setup
        setUp("  \"unterminated string| \n");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("  \"unterminated string\"| \n");
    }

    @Test
    public void tesTypeQuote_unterminated_EOL() {
        // Setup
        setUp("  'unterminated string |\n  ");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("  'unterminated string '|\n  ");
    }

    @Test
    public void testTypeQuote_afterEscapeSequence() {
        // Setup
        setUp("\\|");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\\\"|");
    }

    @Test
    public void testTypeQuote_skipped() {
        // Setup
        setUp("'|'");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("''|");
    }

    @Test
    public void testTypeDoubleQuote_skipAtEndOfProto3Keyword() {
        // Setup
        setUp("\"proto3|\"");

        // Exercise
        typeChar('"');

        // Verify
        assertTextEquals("\"proto3\"|");
    }

    @Test
    public void testTypeSingleQuote_skipAtEndOfProto3Keyword() {
        // Setup
        setUp("'proto3|'");

        // Exercise
        typeChar('\'');

        // Verify
        assertTextEquals("'proto3'|");
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    private void setUp(String textWithCaret) {
        offset = textWithCaret.indexOf(CARET);
        assertTrue(offset != -1);

        text = textWithCaret.substring(0, offset)
                + textWithCaret.substring(offset + 1);
    }

    private void typeChar(char ch) {
        typedChar = ch;

        target = new CharTyping(new ProtobufDocument(text), offset, typedChar);

        switch (typedChar) {
            case '(':
            case '{':
            case '[':
                target.completeOpeningChar();
                break;
            case ')':
            case '}':
            case ']':
                target.skipClosingChar();
                break;
            case '\"':
            case '\'':
                target.completeQuote();
                break;
            default:
                // it doesn't match; that's fine, just continue
        }
    }

    private void assertTextEquals(String expectedWithPipe) {
        String actual = text.substring(0, offset)
                + target.insertionText()
                + text.substring(offset);

        String actualWithPipe = actual.substring(0, target.insertionOffset())
                + CARET
                + actual.substring(target.insertionOffset());

        assertEquals(expectedWithPipe, actualWithPipe);
    }
}
