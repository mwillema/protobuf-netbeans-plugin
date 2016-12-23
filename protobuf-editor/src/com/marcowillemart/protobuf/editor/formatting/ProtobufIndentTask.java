package com.marcowillemart.protobuf.editor.formatting;

import com.marcowillemart.common.lang.Document;
import com.marcowillemart.common.lang.Line;
import com.marcowillemart.common.util.Assert;
import com.marcowillemart.common.util.FailureException;
import com.marcowillemart.protobuf.Protobuf;
import com.marcowillemart.protobuf.ProtobufDocument;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.api.IndentUtils;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.IndentTask;

/**
 * ProtobufIndentTask represents an indentation task for the Protobuf language.
 *
 * Note that the indent task will make a callback to the reindent() method when
 * the Enter key is pressed in the NetBeans editor.
 *
 * @author mwi
 */
public final class ProtobufIndentTask implements IndentTask {

    private static final int ZERO = 0;

    private final Context context;

    /**
     * @requires context != null
     * @effects Makes this be a new indentation task with the given context.
     */
    private ProtobufIndentTask(Context context) {
        Assert.notNull(context);

        this.context = context;
    }

    @Override
    public void reindent() throws BadLocationException {

        Document document = documentFrom(context);

        Line currentLine = document.lineAt(context.startOffset());

        if (currentLine.number() > 1) {
            Line previousLine = document.lineAt(currentLine.startOffset() - 1);
            int newIndent = context.lineIndent(previousLine.startOffset());

            if (previousLine.isLastOpeningBrace()) {
                newIndent += IndentUtils.indentLevelSize(context.document());
            } else if (previousLine.containsStartOfBlockComment()) {
                newIndent++;
            } else if (previousLine.containsEndOfBlockComment()) {
                newIndent--;
            }

            context.modifyIndent(currentLine.startOffset(), newIndent);

            if (isBlockComment(previousLine, currentLine)) {
                reindentBlockComment(newIndent);
            }
        }
    }

    @Override
    public ExtraLock indentLock() {
        return null;
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @requires newIndent >= 0
     * @modifies this
     * @effects Reindents the next line of the line to reindent in the context
     *          of this iff it contains the end of a block comment.
     * @throws BadLocationException if the offset of the line to reindent is not
     *         within the corresponding document's bounds
     */
    private void reindentBlockComment(int newIndent)
            throws BadLocationException {

        Document document = documentFrom(context);

        Line currentLine = document.lineAt(context.startOffset());
        Line nextLine = document.lineAt(currentLine.endOffset() + 1);

        if (nextLine.containsEndOfBlockComment()) {
            context.modifyIndent(nextLine.startOffset(), newIndent);
        }
    }

    /**
     * @requires line1 != null && line2 != null
     * @return true iff line1 is the start of a block comment and line2 an
     *         additional comment line
     */
    private static boolean isBlockComment(Line line1, Line line2) {
        return line1.containsStartOfBlockComment()
                && line2.isLineOfBlockComment();
    }

    /**
     * @requires context != null
     * @return a new Document from the given context.
     */
    private static Document documentFrom(Context context) {
        try {
            return new ProtobufDocument(
                    context.document()
                            .getText(ZERO, context.document().getLength()));
        } catch (BadLocationException ex) {
            throw new FailureException(
                    "ProtobufIndentTask.documentFrom", ex);
        }
    }

    ////////////////////
    // INNER CLASSES
    ////////////////////

    /**
     * Stateless factory responsible for creating IndentTasks for Protobuf.
     */
    @MimeRegistration(
            mimeType = Protobuf.MIME_TYPE,
            service = IndentTask.Factory.class)
    public static final class Factory implements IndentTask.Factory {

        @Override
        public IndentTask createTask(Context context) {
            return new ProtobufIndentTask(context);
        }
    } // end Factory
}
