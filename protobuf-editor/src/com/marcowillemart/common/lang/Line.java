package com.marcowillemart.common.lang;

/**
 * Line represents an immutable line of a text document.
 *
 * @specfield number : int              // The number of the line.
 * @specfield startOffset : int         // The offset of the start of the line.
 * @specfield endOffset : int           // The offset of the end of the line.
 * @specfield text : String             // The text of the line.
 *
 * @invariant number > 0
 * @invariant number = 1 <-> startOffset = 0
 * @invariant number > 1 -> startOffset > 0
 * @invariant startOffset <= endOffset
 * @invariant no '\n' in text
 *
 * @author mwi
 */
public interface Line {

    /**
     * @return this.number
     */
    int number();

    /**
     * @return this.startOffset
     */
    int startOffset();

    /**
     * @return this.endOffset
     */
    int endOffset();

    /**
     * @return this.text
     */
    String text();

    /**
     * @return true iff the last non-white character of this.text is a '{'
     */
    boolean isLastOpeningBrace();

    /**
     * @return true iff this.text contains the start of a block comment.
     */
    boolean containsStartOfBlockComment();

    /**
     * @return true iff this.text contains the end of a block comment.
     */
    boolean containsEndOfBlockComment();

    /**
     * @return true iff this.text is a line of a block comment, i.e., it's not
     *         the start or the end of a block comment but a line in between.
     */
    boolean isLineOfBlockComment();
}
