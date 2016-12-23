package com.marcowillemart.protobuf.editor.highlighting;

import com.marcowillemart.protobuf.Protobuf;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.bracesmatching.BracesMatcher;
import org.netbeans.spi.editor.bracesmatching.BracesMatcherFactory;
import org.netbeans.spi.editor.bracesmatching.MatcherContext;
import org.netbeans.spi.editor.bracesmatching.support.BracesMatcherSupport;

/**
 * ProtobufBracesMatcherFactory is a stateless factory responsible for creating
 * brace matcher tasks for the Protocol Buffers language.
 *
 * @author mwi
 */
@MimeRegistration(
        mimeType = Protobuf.MIME_TYPE,
        service = BracesMatcherFactory.class)
public final class ProtobufBracesMatcherFactory
        implements BracesMatcherFactory {

    /**
     * @effects Makes this be a new BracesMatcherFactory for Protobuf.
     */
    public ProtobufBracesMatcherFactory() {
    }

    @Override
    public BracesMatcher createMatcher(MatcherContext context) {
        return BracesMatcherSupport.defaultMatcher(context, -1, -1);
    }
}
