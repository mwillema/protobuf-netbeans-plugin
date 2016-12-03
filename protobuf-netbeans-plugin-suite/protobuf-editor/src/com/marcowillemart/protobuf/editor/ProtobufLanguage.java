package com.marcowillemart.protobuf.editor;

import com.marcowillemart.protobuf.editor.lexer.ProtobufLanguageHierarchy;
import com.marcowillemart.protobuf.editor.lexer.ProtobufTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

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
}
