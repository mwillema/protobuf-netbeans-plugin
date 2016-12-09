package com.marcowillemart.protobuf;

import com.marcowillemart.common.lang.antlr.AntlrDocument;
import com.marcowillemart.protobuf.parser.ProtobufLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

/**
 * Protobuf implementation of the Document interface.
 *
 * @author mwi
 */
public final class ProtobufDocument extends AntlrDocument {

    /**
     * @requires input != null
     * @effects Makes this be a new Document d with d.text = text and d.tokens
     *          set to the tokens of text recognized by the Protobuf lexer.
     */
    public ProtobufDocument(String text) {
        super(text, new ProtobufLexer(new ANTLRInputStream(text)));

        checkRep();
    }

    @Override
    public boolean isString(Token token) {
        return token.getType() == ProtobufLexer.StrLit
                || token.getType() == ProtobufLexer.UnterminatedStrLit
                || token.getType() == ProtobufLexer.PROTO3_DOUBLE
                || token.getType() == ProtobufLexer.PROTO3_SINGLE;
    }

    @Override
    public boolean isUnclosedString(Token token) {
        return token.getType() == ProtobufLexer.UnterminatedStrLit;
    }

    @Override
    public boolean isComment(Token token) {
        return isBlockComment(token) || isLineComment(token);
    }

    @Override
    protected boolean isBlockComment(Token token) {
        return token.getType() == ProtobufLexer.BLOCK_COMMENT;
    }

    @Override
    protected boolean isUnclosedBlockComment(Token token) {
        if (!isBlockComment(token)) {
            return false;
        }

        if (containsBlockCommentOpeningSymbol(token)) {
            return true;
        }

        return token.getStopIndex() >= text().length() - 1;
    }

    @Override
    protected boolean isLineComment(Token token) {
        return token.getType() == ProtobufLexer.LINE_COMMENT;
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @requires token != null && token.type = BLOCK_COMMENT
     * @return true iff token contains another block comment opening symbol '/*'
     */
    private boolean containsBlockCommentOpeningSymbol(Token token) {
        int startOffset = token.getStartIndex() + 2;
        int stopOffset = token.getStopIndex() - 1;

        if (startOffset > stopOffset) {
            return false;
        }

        return text()
                .substring(startOffset, stopOffset)
                .contains(BLOCK_COMMENT_OPENING_SYMBOL);
    }
}
