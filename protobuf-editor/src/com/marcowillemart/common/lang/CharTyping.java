package com.marcowillemart.common.lang;

import com.marcowillemart.common.util.Assert;
import java.util.Arrays;
import java.util.List;

/**
 * CharTyping represents the mutable typing of a character in a structered text.
 *
 * The typing of a character can be completed if appropriate. For example the
 * opening char '(' will typically be completed by the closing char ')' and the
 * caret placed between both. Similarly, a ')' typed before another ')' may be
 * ignored and the caret placed after the existing character.
 *
 * @specfield text : String          // The edited text that will receive
 *                                      insertionText.
 * @specfield offset : int           // The typing offset, i.e., the offset in
 *                                      text where typedChar was typed.
 * @specfield typedChar : char       // The character typed by the user.
 * @specfield insertionText : String // The text to insert in text at offset,
 *                                      i.e., either typedChar or its
 *                                      modification.
 * @specfield insertionOffset : int  // The offset of the text insertion caret
 *                                      in text after insertionText is inserted,
 *                                      i.e., either offset + 1 or a modified
 *                                      offset.
 *
 * @invariant text must contain only \n as line terminators
 * @invariant offset in [0..text.length]
 * @invariant insertionOffset in [0..text.length + insertionText.length]
 *
 * @author mwi
 */
public final class CharTyping {

    private static final String EMPTY = "";

    private static final char ESCAPE_CHAR = '\\';
    private static final char WHITESPACE = ' ';

    private static final List<Character> OPENING_CHARS =
            Arrays.asList('(', '{', '[');

    private static final List<Character> CLOSING_CHARS =
            Arrays.asList(')', '}', ']');

    private static final List<Character> QUOTES =
            Arrays.asList('\'', '"');

    private final Document document;
    private final int offset;
    private final char typedChar;
    private final StringBuilder insertionText;
    private final int insertionOffset;

    /*
     * Abstraction Function:
     *   text = document.text
     *   offset = offset
     *   typedChar = typedChar
     *   insertionText = insertionText.toString()
     *   insertionOffset = insertionOffset
     *
     * Representation Invariant:
     *   document != null
     *   offset in [0..document.text.length]
     *   insertionText != null
     *   insertionOffset in [0..document.text.length + insertionText.length]
     */

    /**
     * @effects Asserts the rep invariant holds for this.
     */
    private void checkRep() {
        Assert.notNull(document);
        Assert.isTrue(0 <= offset);
        Assert.isTrue(offset <= document.text().length());
        Assert.notNull(insertionText);
        Assert.isTrue(0 <= insertionOffset);
        Assert.isTrue(
                insertionOffset <= document.text().length()
                        + insertionText.length());
    }

    /**
     * @requires document != null && offset in [0..document.text.length]
     * @effects Makes this be a new CharTyping t with t.text = document.text,
     *         t.offset = offset, t.typedChar = typedChar,
     *         t.insertionText = typedChar and t.insertionOffset = offset + 1
     */
    public CharTyping(Document document, int offset, char typedChar) {
        this.document = document;
        this.offset = offset;
        this.typedChar = typedChar;

        this.insertionText = new StringBuilder();
        this.insertionText.append(this.typedChar);

        this.insertionOffset = this.offset + 1;

        checkRep();
    }

    /**
     * @return this.insertionText
     */
    public String insertionText() {
        return insertionText.toString();
    }

    /**
     * @return this.insertionOffset
     */
    public int insertionOffset() {
        return insertionOffset;
    }

    /**
     * @requires this.typedChar is either '(', '{', or '['
     * @modifies this
     * @effects Completes this.typedChar by adding the appropriate closing
     *          char to this.insertionText iff this.typedChar is not in a string
     *          or in a comment.
     */
    public void completeOpeningChar() {
        Assert.isTrue(OPENING_CHARS.contains(typedChar));

        if (!document.isInString(offset)
                && !(document.isInComment(offset))) {
            insertionText.append(closingCharFor(typedChar));
        }
    }

    /**
     * @requires this.typedChar is either ')', '}', or ']'
     * @modifies this
     * @effects Skips this.typedChar by removing it from this.insertionText iff
     *          this.typedChar is not in a string or in a comment.
     */
    public void skipClosingChar() {
        Assert.isTrue(CLOSING_CHARS.contains(typedChar));

        if (offset < document.text().length()
                && typedChar == document.text().charAt(offset)
                && !document.isInString(offset)
                && !document.isInComment(offset)) {
            clearInsertionText();
        }
    }

    /**
     * @requires this.typedChar is either ' or "
     * @modifies this
     * @effects Completes this.typedChar by adding the appropriate quote to or
     *          removing it from this.insertionText iff this.typedChar is not
     *          part of an escape sequence or in a comment.
     */
    public void completeQuote() {
        Assert.isTrue(QUOTES.contains(typedChar));

        if (!document.isInString(offset)
                && !document.isInComment(offset)
                && !isTypedCharEscaped()
                && !isNextEqualsToTypedChar()) {
            insertionText.append(typedChar);
        }

        if (document.isInString(offset)
                && typedChar == document.text().charAt(offset)
                && !isTypedCharEscaped()) {
            clearInsertionText();
        }
    }

    @Override
    public String toString() {
        return "CharTyping{"
                + "text=" + document
                + ", offset=" + offset
                + ", typedChar=" + typedChar
                + ", insertionText=" + insertionText
                + ", insertionOffset=" + insertionOffset
                + '}';
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @return true iff this.typedChar is escaped in this.text
     */
    private boolean isTypedCharEscaped() {
        return offset > 0 && document.text().charAt(offset - 1) == ESCAPE_CHAR;
    }

    /**
     * @return true iff the first non-whitespace char from this.offset to the
     *         offset of the first newline in this.text is equals to
     *         this.typedChar
     */
    private boolean isNextEqualsToTypedChar() {
        for (int i = offset; i < document.text().length(); i++) {
            char ch = document.text().charAt(i);

            if (ch == typedChar) {
                return true;
            }

            if (ch != WHITESPACE) {
                break;
            }
        }

        return false;
    }

    /**
     * @modifies this
     * @effects Clears this.insertionText
     */
    private void clearInsertionText() {
        insertionText.replace(0, insertionText.length(), EMPTY);
    }

    /**
     * @return the appropriate closing character for the given opening character
     *         if it can be matched, else returns the empty char
     */
    private static char closingCharFor(char opening) {
        final char closing;

        switch (opening) {
            case '{':
                closing = '}';
                break;
            case '(':
                closing = ')';
                break;
            case '[':
                closing = ']';
                break;
            case '\"':
                closing = '\"';
                break;
            case '\'':
                closing = '\'';
                break;
            default:
                closing = ' ';
        }

        return closing;
    }
}
