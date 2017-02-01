package com.marcowillemart.protobuf.editor.typinghooks;

import com.marcowillemart.common.lang.Document;
import com.marcowillemart.common.util.FailureException;
import com.marcowillemart.protobuf.Protobuf;
import com.marcowillemart.protobuf.ProtobufDocument;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.typinghooks.TypedBreakInterceptor;

/**
 * ProtobufTypedBreakInterceptor is a mutable interceptor which is called when
 * a line break is typed into a Protobuf document.
 *
 * @author mwi
 */
public final class ProtobufTypedBreakInterceptor
        implements TypedBreakInterceptor {

    private static final int ZERO = 0;

    private static final String PAIR_OF_CURLY_BRACES = "{}";

    /**
     * @effects Makes this be a new ProtobufTypedBreakInterceptor.
     */
    private ProtobufTypedBreakInterceptor() {
    }

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        return false;
    }

    @Override
    public void insert(MutableContext context) throws BadLocationException {
        Document document = documentFrom(context);

        addExtraLineBetweenBraces(context, document);
        closeBlockComment(context, document);
        addLineToBlockComment(context, document);
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
    }

    @Override
    public void cancelled(Context context) {
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @requires context != null && document != null
     * @modifies context
     * @effects Adds an extra line if the line break is typed between two
     *          matching braces.
     */
    private static void addExtraLineBetweenBraces(
            MutableContext context,
            Document document) {

        int offset = context.getBreakInsertOffset();

        if (0 < offset && offset < context.getDocument().getLength()) {
            String surroundingchars = surroundingCharacters(context, offset);

            if (PAIR_OF_CURLY_BRACES.equals(surroundingchars)) {
                String indentation = document.lineAt(offset).indentation();

                context.setText(String.format("\n\n%s", indentation), 1, 1);
            }
        }
    }

    /**
     * @requires context != null && document != null
     * @modifies context
     * @effects Closes the block comment if the line break is typed in an
     *          unclosed block comment according to the given document.
     */
    private static void closeBlockComment(
            MutableContext context,
            Document document) {

        int offset = context.getBreakInsertOffset();

        if (document.shouldCloseBlockComment(offset)) {
            context.setText("\n * \n */", 0, 4);
        }
    }

    /**
     * @requires context != null && document != null
     * @modifies context
     * @effects Adds a line to the block comment if the line break is typed in
     *          a block comment already closed according to the given document.
     */
    private static void addLineToBlockComment(
            MutableContext context,
            Document document) {

        int offset = context.getBreakInsertOffset();

        if (document.shouldAddLineToBlockComment(offset)) {
            context.setText("\n * ", 0, 4);
        }
    }

    /**
     * @requires context != null && offset in ]0..context.document.length[
     * @return the char before and after offset in context.document
     */
    private static String surroundingCharacters(
            MutableContext context,
            int offset) {

        try {
            return context.getDocument().getText(offset - 1, 2);
        } catch (BadLocationException ex) {
            throw new FailureException(
                    "ProtobufTypedBreakInterceptor.surroundingCharacters", ex);
        }
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
                    "ProtobufTypedBreakInterceptor.documentFrom", ex);
        }
    }

    ////////////////////
    // INNER CLASSES
    ////////////////////

    /**
     * Stateless factory responsible for creating TypedBreakInterceptors for
     * Protobuf.
     */
    @MimeRegistration(
            mimeType = Protobuf.MIME_TYPE,
            service = TypedBreakInterceptor.Factory.class)
    public static final class Factory implements TypedBreakInterceptor.Factory {

        @Override
        public TypedBreakInterceptor createTypedBreakInterceptor(
                MimePath mimePath) {
            return new ProtobufTypedBreakInterceptor();
        }
    } // end Factory
}
