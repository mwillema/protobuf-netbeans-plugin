package com.marcowillemart.protobuf.editor.lexer;

import com.marcowillemart.common.util.Assert;
import com.marcowillemart.protobuf.editor.lexer.util.AntlrCharStream;
import com.marcowillemart.protobuf.editor.lexer.util.AntlrLexerState;
import com.marcowillemart.protobuf.parser.ProtobufLexer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * ProtobufEditorLexer represents a mutable lexer for the Protobuf editor.
 *
 * @see http://bits.netbeans.org/8.0.2/javadoc/org-netbeans-modules-lexer/org/netbeans/spi/lexer/Lexer.html
 *
 * @author mwi
 */
final class ProtobufEditorLexer implements Lexer<ProtobufTokenId> {

    private static final Logger LOG =
            Logger.getLogger(ProtobufEditorLexer.class.getName());

    private static final String SOURCE_NAME = "ProtobufEditor";

    private final LexerRestartInfo<ProtobufTokenId> info;
    private final ProtobufLexer lexer;

    /**
     * @requires info != null
     * @effects Makes this be a new ProtobufEditorLexer with 'info'.
     */
    ProtobufEditorLexer(LexerRestartInfo<ProtobufTokenId> info) {
        Assert.notNull(info);

        this.info = info;

        this.lexer =
                new ProtobufLexer(
                        new AntlrCharStream(info.input(), SOURCE_NAME));
        this.lexer.setChannel(1);

        AntlrLexerState state = (AntlrLexerState) info.state();

        if (state != null) {
            state.apply(lexer);
        }
    }

    /**
     * @return the next token recognized by the lexer or null if there are no
     *         more characters (available in the input) to be tokenized.
     */
    @Override
    public org.netbeans.api.lexer.Token<ProtobufTokenId> nextToken() {
        Token token = lexer.nextToken();

        ProtobufTokenId tokenId = null;

        if (token.getType() != ProtobufLexer.EOF) {
            tokenId  = ProtobufTokenIdSet.INSTANCE.get(token.getType());
            LOG.log(Level.INFO, "nextToken - {0}", tokenId.toString());
        }  else if (info.input().readLength() > 0) {
            // Remaining chars on the input should be tokenized
            // see https://netbeans.org/bugzilla/show_bug.cgi?id=240826
            tokenId = ProtobufTokenIdSet.INSTANCE.get(ProtobufLexer.WS);
            LOG.log(Level.INFO, "nextToken - ERROR (as WS)");
        }

        if (tokenId == null) {
            LOG.log(Level.INFO, "nextToken - EOF");
            return null;
        }

        // According to the method specification, this must *not* return any
        // other Token instances than those obtained from the TokenFactory.
        return info.tokenFactory().createToken(tokenId);
    }

    @Override
    public void release() {
    }

    @Override
    public Object state() {
        return new AntlrLexerState(lexer._mode, lexer._modeStack);
    }
}
