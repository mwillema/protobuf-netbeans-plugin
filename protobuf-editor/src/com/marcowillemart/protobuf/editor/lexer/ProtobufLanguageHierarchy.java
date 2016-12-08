package com.marcowillemart.protobuf.editor.lexer;

import java.util.Collection;
import org.netbeans.api.lexer.Language;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * ProtobufLanguageHierarchy defines the Protobuf language along with its lexer.
 *
 * @author mwi
 */
public final class ProtobufLanguageHierarchy
        extends LanguageHierarchy<ProtobufTokenId> {

    /** The mime type of the TVL language. */
    public static final String MIME_TYPE = "application/x-protobuf";

    private static final Language<ProtobufTokenId> INSTANCE;

    static {
        INSTANCE = new ProtobufLanguageHierarchy().language();
    }
    
    /**
     * @effects Makes this be a new Protobuf language hierarchy.
     */
    private ProtobufLanguageHierarchy() {
        super();
    }

    @Override
    protected Collection<ProtobufTokenId> createTokenIds() {
        return ProtobufTokenIdSet.INSTANCE.tokenIds();
    }

    @Override
    protected synchronized Lexer<ProtobufTokenId> createLexer(
            LexerRestartInfo<ProtobufTokenId> info) {
        return new ProtobufEditorLexer(info);
    }

    @Override
    protected String mimeType() {
        return MIME_TYPE;
    }

    /**
     * @return the unique instance of the Protobuf language hierarchy.
     */
    public static Language<ProtobufTokenId> instance() {
        return INSTANCE;
    }
}
