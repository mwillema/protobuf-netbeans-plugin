package com.marcowillemart.common.lang;

/**
 * Simple implementation of the Line interface.
 *
 * @author mwi
 */
public final class SimpleLine extends AbstractLine {

    private static final char OPENING_BRACE = '{';
    private static final String BLOCK_COMMENT_START = "/*";
    private static final String BLOCK_COMMENT_END = "*/";
    private static final String BLOCK_COMMENT_LINE = "*";

    private final int number;
    private final int startOffset;
    private final int endOffset;
    private final String text;

    /**
     * @requires number > 0 &&
     *           number = 1 <-> startOffset = 0 &&
     *           number > 1 -> startOffset > 0
     * @effects Makes this be a new Line l with l.number = number,
     *          l.startOffset = startOffset and
     *          l.isLastOpeningBrace = isLastOpeningBrace.
     */
    public SimpleLine(
            int number,
            int startOffset,
            int endOffset,
            String text) {

        this.number = number;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.text = text;

        checkRep();
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public int startOffset() {
        return startOffset;
    }

    @Override
    public int endOffset() {
        return endOffset;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String indentation() {
        int i;

        for (i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                break;
            }
        }

        return text.substring(0, i);
    }

    @Override
    public boolean isLastOpeningBrace() {
        for (int i = text.length() - 1; i >= 0; i--) {
            char ch = text.charAt(i);

            if (ch == OPENING_BRACE) {
                return true;
            }

            if (!Character.isWhitespace(ch)) {
                break;
            }
        }

        return false;
    }

    @Override
    public boolean containsStartOfBlockComment() {
        return text.contains(BLOCK_COMMENT_START);
    }

    @Override
    public boolean containsEndOfBlockComment() {
        return text.contains(BLOCK_COMMENT_END);
    }

    @Override
    public boolean isLineOfBlockComment() {
        return text.contains(BLOCK_COMMENT_LINE)
                && !containsStartOfBlockComment()
                && !containsEndOfBlockComment();
    }
}
