package com.marcowillemart.protobuf.editor.typinghooks;

import com.marcowillemart.common.lang.CharTyping;
import com.marcowillemart.common.lang.Document;
import com.marcowillemart.common.util.FailureException;
import com.marcowillemart.protobuf.Protobuf;
import com.marcowillemart.protobuf.ProtobufDocument;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.typinghooks.TypedTextInterceptor;

/**
 * ProtobufTypedTextInterceptor is a mutable interceptor which is called when
 * text is typed into a Protobuf document.
 *
 * @author mwi
 */
public final class ProtobufTypedTextInterceptor
        implements TypedTextInterceptor {

    private static final int ZERO = 0;

    private CharTyping charTyping;

    /**
     * @effects Makes this be a new ProtobufTypedTextInterceptor.
     */
    private ProtobufTypedTextInterceptor() {
        this.charTyping = null;
    }

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        return false;
    }

    @Override
    public void insert(MutableContext context) throws BadLocationException {
        char typedChar = context.getText().charAt(0);

        switch (typedChar) {
            case '(':
            case '{':
            case '[':
                charTyping = charTypingFrom(context, typedChar);
                charTyping.completeOpeningChar();
                break;
            case ')':
            case '}':
            case ']':
                charTyping = charTypingFrom(context, typedChar);
                charTyping.skipClosingChar();
                break;
            case '\"':
            case '\'':
                charTyping = charTypingFrom(context, typedChar);
                charTyping.completeQuote();
                break;
            default:
                charTyping = null;
        }

        if (charTyping != null) {
            context.setText(charTyping.insertionText(), ZERO);
        }
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
        if (charTyping != null) {
            context.getComponent().setCaretPosition(
                    charTyping.insertionOffset());
            charTyping = null;
        }
    }

    @Override
    public void cancelled(Context context) {
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @requires context != null
     * @return a new CharTyping from the givne context and typed char.
     */
    private static CharTyping charTypingFrom(
            MutableContext context,
            char typedChar) {

        return new CharTyping(
                documentFrom(context),
                context.getOffset(),
                typedChar);
    }

    /**
     * @requires context != null
     * @return a new Document from the given context.
     */
    private static Document documentFrom(MutableContext context) {
        try {
            return new ProtobufDocument(
                    context.getDocument()
                            .getText(ZERO, context.getDocument().getLength()));
        } catch (BadLocationException ex) {
            throw new FailureException(
                    "ProtobufTypedTextInterceptor.documentFrom", ex);
        }
    }

    ////////////////////
    // INNER CLASSES
    ////////////////////

    /**
     * Stateless factory responsible for creating TypedTextInterceptors for
     * Protobuf.
     */
    @MimeRegistration(
            mimeType = Protobuf.MIME_TYPE,
            service = TypedTextInterceptor.Factory.class)
    public static final class Factory implements TypedTextInterceptor.Factory {

        @Override
        public TypedTextInterceptor createTypedTextInterceptor(
                MimePath mimePath) {
            return new ProtobufTypedTextInterceptor();
        }
    } // end Factory
}
