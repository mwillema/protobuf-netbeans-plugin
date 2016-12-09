package com.marcowillemart.common.lang.antlr;

import com.marcowillemart.common.lang.AbstractLine;
import com.marcowillemart.common.util.Assert;
import java.util.List;
import java.util.ListIterator;
import org.antlr.v4.runtime.Token;

/**
 * ANTLR implementation of the Line interface.
 *
 * @author mwi
 */
public abstract class AntlrLine extends AbstractLine {

    private final List<Token> tokens;

    /*
     * Abstraction Function:
     *   number = tokens[0].line
     *   startOffset = tokens[0].startIndex
     *
     * Representation Invariant:
     *   super
     *   tokens != null
     *   tokens.size > 0
     *   for all t_i in tokens,
     *     t_i.line = tokens[0].line
     *     i > 0 -> t_i.startIndex > t_i-1.stopIndex
     *     (i < tokens.size - 1) -> t_i.stopIndex < t_i+1.startIndex
     */

    @Override
    protected void checkRep() {
        super.checkRep();
        Assert.notEmpty(tokens);
        for (Token token : tokens) {
            Assert.equals(tokens.get(0).getLine(), token.getLine());
        }
    }

    /**
     * @requires tokens != null &&
     *           tokens not empty &&
     *           for all t in tokens, t.line = tokens[0].line &&
     *           tokens is sorted in ascending order according to the start and
     *           stop indices
     * @effects Makes this be a new Line l with l.number = tokens[0].line,
     *          l.startOffset = tokens[0].startIndex and
     *          l.isLastOpeningBrace set by iterating over tokens in reverse
     *          order
     */
    protected AntlrLine(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public final int number() {
        return tokens.get(0).getLine();
    }

    @Override
    public final int startOffset() {
        return tokens.get(0).getStartIndex();
    }

    @Override
    public int endOffset() {
        return tokens.get(tokens.size() - 1).getStopIndex();
    }

    @Override
    public final boolean isLastOpeningBrace() {

        Token lastNonWhiteToken;

        ListIterator<Token> generator = tokens.listIterator(tokens.size());

        do {
            lastNonWhiteToken = generator.previous();
        } while (generator.hasPrevious() && isWhitespace(lastNonWhiteToken));

        return isOpeningBrace(lastNonWhiteToken);
    }

    /**
     * @requires token != null
     * @return true iff token is a whitespace
     */
    protected abstract boolean isWhitespace(Token token);

    /**
     * @requires token != null
     * @return true iff token is a '{'
     */
    protected abstract boolean isOpeningBrace(Token token);
}
