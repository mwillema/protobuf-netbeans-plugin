package com.marcowillemart.common.lang.antlr;

import com.marcowillemart.common.lang.Document;
import com.marcowillemart.common.lang.Line;
import com.marcowillemart.common.lang.SimpleLine;
import com.marcowillemart.common.util.Assert;
import com.marcowillemart.common.util.Pair;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

/**
 * ANTLR implementation of the Document interface.
 *
 * @specfield tokens : List<Token> // The recognized tokens of the document.
 *
 * @invariant tokens not empty
 * @invariant tokens.last = EOF
 *
 * @author mwi
 */
public abstract class AntlrDocument implements Document {

    protected static final char NEWLINE = '\n';
    protected static final String EMPTY_STRING = "";
    protected static final String BLOCK_COMMENT_OPENING_SYMBOL = "/*";
    protected static final String BLOCK_COMMENT_LINE_SEPARATOR = "*";

    private static final boolean FORWARD_BIAS = true;
    private static final boolean BACKWARD_BIAS = !FORWARD_BIAS;

    private final String text;
    private final List<Token> tokens;

    /*
     * Abstraction Function:
     *   text = text
     *   tokens = tokens
     *
     * Representation Invariant:
     *   tokens != null
     *   tokens.size > 0
     *   tokens.last.type = EOF
     */

    /**
     * @effects Asserts the rep invariant holds for this.
     */
    protected void checkRep() {
        Assert.notNull(text);
        Assert.notEmpty(tokens);
        Assert.equals(Token.EOF, tokens.get(tokens.size() - 1).getType());
    }

    /**
     * @requires text != null && tokenSource != null
     * @effects Makes this be a new Document d with d.text = text and d.tokens
     *          set to the tokens produced by tokenSource
     */
    protected AntlrDocument(String text, TokenSource tokenSource) {
        Assert.notNull(text);
        Assert.notNull(tokenSource);

        this.text = text;
        this.tokens = new LinkedList<>();

        initTokens(tokenSource);
    }

    @Override
    public final String text() {
        return text;
    }

    @Override
    public final boolean isInString(int offset) {
        Token rhsToken = tokenAt(offset, FORWARD_BIAS);

        if (isString(rhsToken)) {
            return offset != rhsToken.getStartIndex();
        }

        if (offset <= 0) {
            return false;
        }

        Token lhsToken = tokenAt(offset, BACKWARD_BIAS);
        return isUnclosedString(lhsToken);
    }

    @Override
    public final boolean isInComment(int offset) {
        Token rhsToken = tokenAt(offset, FORWARD_BIAS);

        if (isComment(rhsToken)) {
            return offset != rhsToken.getStartIndex();
        }

        if (offset <= 0) {
            return false;
        }

        Token lhsToken = tokenAt(offset, BACKWARD_BIAS);
        return isLineComment(lhsToken);
    }

    @Override
    public boolean shouldCloseBlockComment(int offset) {
        if (offset <= 0) {
            return false;
        }

        Token token = tokenAt(offset, BACKWARD_BIAS);

        if (!isUnclosedBlockComment(token)) {
            return false;
        }

        return lineUpTo(offset).left().contains(BLOCK_COMMENT_OPENING_SYMBOL);
    }

    @Override
    public boolean shouldAddLineToBlockComment(int offset) {
        Token token = tokenAt(offset, FORWARD_BIAS);

        if (!isBlockComment(token)) {
            return false;
        }

        String lineBeforeOffset = lineUpTo(offset).left();

        if (isUnclosedBlockComment(token)) {
            return !lineBeforeOffset.contains(BLOCK_COMMENT_OPENING_SYMBOL)
                    && lineBeforeOffset.contains(BLOCK_COMMENT_LINE_SEPARATOR);
        }

        return lineBeforeOffset.contains(BLOCK_COMMENT_LINE_SEPARATOR);
    }

    @Override
    public Line lineAt(int offset) {
        Pair<String, Integer> prefix = lineUpTo(offset);
        Pair<String, Integer> suffix = lineFrom(offset);

        return new SimpleLine(
                lineNumberOf(prefix.right()),
                prefix.right(),
                suffix.right(),
                String.format("%s%s", prefix.left(), suffix.left()));
    }

    @Override
    public final String toString() {
        return "AntlrDocument{"
                + "text=" + text
                + ", tokens=" + tokens
                + '}';
    }

    /**
     * @requires token != null
     * @return true iff token is a string literal
     */
    protected abstract boolean isString(Token token);

    /**
     * @requires token != null
     * @return true iff token is a string that is not closed, i.e., without the
     *         appropriate closing character
     */
    protected abstract boolean isUnclosedString(Token token);

    /**
     * @requires token != null
     * @return true iff token is a comment
     */
    protected abstract boolean isComment(Token token);

    /**
     * @requires token != null
     * @return true iff token is a block comment
     */
    protected abstract boolean isBlockComment(Token token);

    /**
     * @requires token != null
     * @return true iff token is an unclosed block comment
     */
    protected abstract boolean isUnclosedBlockComment(Token token);

    /**
     * @requires token != null
     * @return true iff token is a comment spanning an entire line up to a
     *         newline character
     */
    protected abstract boolean isLineComment(Token token);

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @requires the tokens of this are not initialized yet && source != null
     * @modifies this
     * @effects Initializes the tokens of this with the given token source.
     */
    private void initTokens(TokenSource source) {
        Assert.isTrue(tokens.isEmpty());

        Token token;

        do {
            token = source.nextToken();
            tokens.add(token);
        } while (token.getType() != Token.EOF);
    }

    /**
     * @requires (forwardBias && offset >= 0) || (!forwardBias && offset > 0)
     * @return the token at the given character offset or the token immediately
     *         to the right (resp. left) if offset lies between two tokens and
     *         forwardBias is true (resp. false). If offset lies past the last
     *         offset of the EOF token, the operation behaves as though offset
     *         was the last offset of the EOF token.
     */
    private Token tokenAt(int offset, boolean forwardBias) {
        if (forwardBias) {
            Assert.isTrue(offset >= 0);
        } else {
            Assert.isTrue(offset > 0);
        }

        Iterator<Token> generator = tokens.iterator();

        Token token;

        do {
            token = generator.next();
        } while (generator.hasNext()
                && !containsOffset(token, offset, forwardBias));

        return token;
    }

    /**
     * @requires offset in [0..this.text.length]
     * @return a pair <s,o> where s is the substring of this.text starting at
     *         the start of the line of offset up to offset (excluded), and o
     *         the offset of the first character of the line.
     */
    private Pair<String, Integer> lineUpTo(int offset) {
        StringBuilder builder = new StringBuilder();

        int index;

        for (index = offset - 1; index >= 0; index--) {
            char ch = text.charAt(index);

            if (ch == NEWLINE) {
                break;
            }

            builder.append(ch);
        }

        return Pair.of(builder.reverse().toString(), index + 1);
    }

    /**
     * @requires offset in [0..this.text.length]
     * @return a pair <s,o> where s is the substring of this.text starting at
     *         offset (included) up to the the end of the line (excluded), and o
     *         the offset of the last character of the line.
     */
    private Pair<String, Integer> lineFrom(int offset) {
        if (offset >= text.length()) {
            return Pair.of(EMPTY_STRING, text.length());
        }

        StringBuilder builder = new StringBuilder();

        int index;

        for (index = offset; index < text.length(); index++) {
            char ch = text.charAt(index);

            if (ch == NEWLINE) {
                break;
            }

            builder.append(ch);
        }

        return Pair.of(builder.toString(), index);
    }

    /**
     * @requires offset in [0..this.text.length]
     * @return the line number at offset
     */
    private int lineNumberOf(int offset) {
        int line = 1;

        for (int i = 0; i < offset; i++) {
            if (text.charAt(i) == NEWLINE) {
                line++;
            }
        }
        return line;
    }

    /**
     * @requires token != null
     * @return true iff token contains offset or offset is at the start of token
     *         and forwardBias is true.
     */
    private static boolean containsOffset(
            Token token,
            int offset,
            boolean forwardBias) {

        int startIndex;
        int stopIndex;

        if (forwardBias) {
            startIndex = token.getStartIndex();
            stopIndex = token.getStopIndex();
        } else {
            startIndex = token.getStartIndex() + 1;
            stopIndex = token.getStopIndex() + 1;
        }

        return startIndex <= offset && offset <= stopIndex;
    }
}
