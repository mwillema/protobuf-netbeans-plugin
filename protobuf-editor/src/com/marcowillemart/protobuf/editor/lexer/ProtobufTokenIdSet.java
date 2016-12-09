package com.marcowillemart.protobuf.editor.lexer;

import com.marcowillemart.common.util.Assert;
import com.marcowillemart.protobuf.parser.ProtobufLexer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.netbeans.api.lexer.TokenId;

/**
 * ProtobufTokenIdSet represents an immutable set of all the Protobuf TokenIds
 * that are used by the Protobuf lexer.
 *
 * @author mwi
 */
public enum ProtobufTokenIdSet {

    INSTANCE;

    private final Map<Integer, ProtobufTokenId> tokenIds;

    /*
     * Representation Invariant:
     *   tokenIds != null
     *   for all <k,v> in tokenIds,
     *     k != null
     *     v != null
     *     k = v.ordinal
     *     v.name = ProtobufLexer.symbolicNameOf(v.ordinal)
     */

    /**
     * @effects Asserts the rep invariant holds for this.
     */
    private void checkRep() {
        Assert.notNull(tokenIds);
        for (Entry<Integer, ProtobufTokenId> entry : tokenIds.entrySet()) {
            Integer k = entry.getKey();
            TokenId v = entry.getValue();
            Assert.notNull(k);
            Assert.notNull(v);
            Assert.isTrue(k == v.ordinal());
            Assert.equals(
                    v.name(),
                    ProtobufLexer.VOCABULARY.getSymbolicName(v.ordinal()));
        }
    }

    /**
     * @effects Makes this be a new ProtobufTokenIdSet initialized with the
     *          TokenIds for the Protobuf language.
     */
    private ProtobufTokenIdSet() {
        this.tokenIds = new HashMap<>();

        initTokenIds();

        checkRep();
    }

    /**
     * @return the TokenId ti with ti.ordinal = ordinal from this if such a
     *         TokenId exists, else returns null.
     */
    public ProtobufTokenId get(int ordinal) {
        return tokenIds.get(ordinal);
    }

    /**
     * @return a read-only collection of all the ProtobufTokenIds in this
     */
    public Collection<ProtobufTokenId> tokenIds() {
        return Collections.unmodifiableCollection(tokenIds.values());
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this.
     */
    private void initTokenIds() {
        initWhitespaceTokenIds();
        initCommentTokenIds();
        initTypeTokenIds();
        initKeywordTokenIds();
        initIdentifierTokenIds();
        initLiteralTokenIds();
        initSeparatorTokenIds();
        initOperatorTokenIds();
        addTokenId(ProtobufLexer.ANYCHAR, "error");
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to whitespaces
     */
    private void initWhitespaceTokenIds() {
        final String whitespace = "whitespace";

        addTokenId(ProtobufLexer.WS, whitespace);
        addTokenId(ProtobufLexer.NEWLINE, whitespace);
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to comments
     */
    private void initCommentTokenIds() {
        final String comment = "comment";

        addTokenId(ProtobufLexer.BLOCK_COMMENT, comment);
        addTokenId(ProtobufLexer.LINE_COMMENT, comment);
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to types
     */
    private void initTypeTokenIds() {
        final String type = "type";

        addTokenId(ProtobufLexer.BOOL, type);
        addTokenId(ProtobufLexer.BYTES, type);
        addTokenId(ProtobufLexer.DOUBLE, type);
        addTokenId(ProtobufLexer.FIXED32, type);
        addTokenId(ProtobufLexer.FIXED64, type);
        addTokenId(ProtobufLexer.FLOAT, type);
        addTokenId(ProtobufLexer.INT32, type);
        addTokenId(ProtobufLexer.INT64, type);
        addTokenId(ProtobufLexer.MAP, type);
        addTokenId(ProtobufLexer.SFIXED32, type);
        addTokenId(ProtobufLexer.SFIXED64, type);
        addTokenId(ProtobufLexer.SINT32, type);
        addTokenId(ProtobufLexer.SINT64, type);
        addTokenId(ProtobufLexer.STRING, type);
        addTokenId(ProtobufLexer.UINT32, type);
        addTokenId(ProtobufLexer.UINT64, type);
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to keywords
     */
    private void initKeywordTokenIds() {
        final String keyword = "keyword";

        addTokenId(ProtobufLexer.ENUM, keyword);
        addTokenId(ProtobufLexer.IMPORT, keyword);
        addTokenId(ProtobufLexer.MESSAGE, keyword);
        addTokenId(ProtobufLexer.ONEOF, keyword);
        addTokenId(ProtobufLexer.OPTION, keyword);
        addTokenId(ProtobufLexer.PACKAGE, keyword);
        addTokenId(ProtobufLexer.PUBLIC, keyword);
        addTokenId(ProtobufLexer.REPEATED, keyword);
        addTokenId(ProtobufLexer.RESERVED, keyword);
        addTokenId(ProtobufLexer.RETURNS, keyword);
        addTokenId(ProtobufLexer.RPC, keyword);
        addTokenId(ProtobufLexer.SERVICE, keyword);
        addTokenId(ProtobufLexer.STREAM, keyword);
        addTokenId(ProtobufLexer.SYNTAX, keyword);
        addTokenId(ProtobufLexer.TO, keyword);
        addTokenId(ProtobufLexer.WEAK, keyword);
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to identifiers
     */
    private void initIdentifierTokenIds() {
        final String identifier = "identifier";

        addTokenId(ProtobufLexer.Ident, identifier);
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to literals
     */
    private void initLiteralTokenIds() {
        final String number = "number";

        addTokenId(ProtobufLexer.IntLit, number);
        addTokenId(ProtobufLexer.FloatLit, number);

        final String bool = "boolean";

        addTokenId(ProtobufLexer.BoolLit, bool);

        final String str = "string";

        addTokenId(ProtobufLexer.StrLit, str);
        addTokenId(ProtobufLexer.UnterminatedStrLit, str);
        addTokenId(ProtobufLexer.Quote, str);
        addTokenId(ProtobufLexer.PROTO3_DOUBLE, str);
        addTokenId(ProtobufLexer.PROTO3_SINGLE, str);
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to separators
     */
    private void initSeparatorTokenIds() {
        final String separator = "separator";

        addTokenId(ProtobufLexer.LPAREN, separator);
        addTokenId(ProtobufLexer.RPAREN, separator);
        addTokenId(ProtobufLexer.LBRACE, separator);
        addTokenId(ProtobufLexer.RBRACE, separator);
        addTokenId(ProtobufLexer.LBRACK, separator);
        addTokenId(ProtobufLexer.RBRACK, separator);
        addTokenId(ProtobufLexer.LCHEVR, separator);
        addTokenId(ProtobufLexer.RCHEVR, separator);
        addTokenId(ProtobufLexer.SEMI, separator);
        addTokenId(ProtobufLexer.COMMA, separator);
        addTokenId(ProtobufLexer.DOT, separator);
        addTokenId(ProtobufLexer.MINUS, separator);
        addTokenId(ProtobufLexer.PLUS, separator);
    }

    /**
     * @modifies this
     * @effects Initializes the TokenIds of this related to operators
     */
    private void initOperatorTokenIds() {
        final String operator = "operator";

        addTokenId(ProtobufLexer.ASSIGN, operator);
    }

    /**
     * @requires tokenType is an existing type of token in the Protobuf lexer &&
     *           category not null and not empty && category is lowercase
     * @modifies this
     * @effects Adds a new Protobuf TokenId ti to this with
     *          ti.name = ProtobufLexer.symbolicNameOf(tokenType),
     *          ti.primaryCategory = category and ti.ordinal = tokenType
     */
    private void addTokenId(int tokenType, String category) {
        ProtobufTokenId tokenId =
                new ProtobufTokenId(
                        ProtobufLexer.VOCABULARY.getSymbolicName(tokenType),
                        category,
                        tokenType);

        tokenIds.put(tokenId.ordinal(), tokenId);
    }
}
