package com.marcowillemart.protobuf.editor.lexer;

import com.marcowillemart.protobuf.parser.ProtobufLexer;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.api.lexer.TokenId;

/**
 * Unit tests for the ProtobufTokenIdSet class.
 *
 * @author mwi
 */
public class ProtobufTokenIdSetTest {

    private ProtobufTokenIdSet target;

    @Before
    public void setUp() {
        target = ProtobufTokenIdSet.INSTANCE;
    }

    @Test
    public void testGet() {
        // Exercise && Verify
        for (int i = 1; i < ProtobufLexer.VOCABULARY.getMaxTokenType(); i++) {
            TokenId actual = target.get(i);
            assertNotNull(
                    "TokenId with ordinal = " + i + " must not be null",
                    actual);
            assertEquals(
                    actual.name(),
                    ProtobufLexer.VOCABULARY.getSymbolicName(i));
        }
    }

    @Test
    public void testTokenIds() {
        // Exercise & Verify
        for (TokenId tokenId : ProtobufTokenIdSet.INSTANCE.tokenIds()) {
            String name =
                    ProtobufLexer.VOCABULARY.getSymbolicName(tokenId.ordinal());
            assertNotNull(name);
            assertEquals(name, tokenId.name());
        }
    }
}
