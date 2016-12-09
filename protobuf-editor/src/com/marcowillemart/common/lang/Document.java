package com.marcowillemart.common.lang;

/**
 * Document represents an immutable text whose tokens have been recognized,
 * usually by a lexer for a given language.
 *
 * @specfield text : String // The text of the document.
 *
 * @author mwi
 */
public interface Document {

    /**
     * @return this.text
     */
    String text();

    /**
     * @requires offset >= 0
     * @return true iff the given offset is in a string literal, meaning that if
     *         a character is inserted at the given offset it will be part of
     *         that string literal
     */
    boolean isInString(int offset);

    /**
     * @requires offset >= 0
     * @return true iff the given offset is in a comment, meaning that if a
     *         character is inserted at the given offset it will be part of that
     *         comment.
     */
    boolean isInComment(int offset);

    /**
     * @requires offset in [0..this.text.length]
     * @return the line at the given offset
     */
    Line lineAt(int offset);

    /**
     * @requires offset in [0..this.text.length]
     * @return true iff the given offset is in a block comment that should be
     *         closed when a break is typed.
     */
    boolean shouldCloseBlockComment(int offset);

    /**
     * @requires offset in [0..this.text.length]
     * @return true iff the given offset is in a block comment where a new line
     *         should be added when a break is typed.
     */
    boolean shouldAddLineToBlockComment(int offset);
}
