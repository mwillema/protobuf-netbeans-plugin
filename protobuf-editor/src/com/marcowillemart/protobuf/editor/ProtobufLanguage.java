package com.marcowillemart.protobuf.editor;

import com.marcowillemart.protobuf.editor.lexer.ProtobufLanguageHierarchy;
import com.marcowillemart.protobuf.editor.lexer.ProtobufTokenId;
import com.marcowillemart.protobuf.editor.parser.ProtobufEditorParser;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.parsing.spi.Parser;

/**
 * Configuration that registers the Protobuf language.
 *
 * @author mwi
 */
@LanguageRegistration(mimeType = "application/x-protobuf")
public class ProtobufLanguage extends DefaultLanguageConfig {

    @Override
    public Language<ProtobufTokenId> getLexerLanguage() {
        return ProtobufLanguageHierarchy.instance();
    }

    @Override
    public String getDisplayName() {
        return "Protobuf";
    }

    @Override
    public Parser getParser() {
        return new ProtobufEditorParser();
    }
}
