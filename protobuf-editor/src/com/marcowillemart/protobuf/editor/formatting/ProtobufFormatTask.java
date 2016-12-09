package com.marcowillemart.protobuf.editor.formatting;

import com.marcowillemart.protobuf.Protobuf;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

/**
 * ProtobufFormatTask represents a format task for the Protobuf language that
 * performs actual formatting wihtin offset bounds of a given context.
 *
 * Note that the format task will make a callback to the reformat() method when
 * Alt-Shift-F is pressed in the NetBeans editor.
 *
 * @author mwi
 */
public final class ProtobufFormatTask implements ReformatTask {

    private final Context context;

    /**
     * @requires context != null
     * @effects Makes this be a new format task with the given context.
     */
    private ProtobufFormatTask(Context context) {
        this.context = context;
    }

    @Override
    public void reformat() throws BadLocationException {
        // TODO
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }

    ////////////////////
    // INNER CLASSES
    ////////////////////

    /**
     * Stateless factory responsible for creating FormatTasks for Protobuf.
     */
    @MimeRegistration(
            mimeType = Protobuf.MIME_TYPE,
            service = ReformatTask.Factory.class)
    public static final class ProtobufFormatTaskFactory
            implements ReformatTask.Factory {

        @Override
        public ReformatTask createTask(Context context) {
            return new ProtobufFormatTask(context);
        }
    } // end Factory
}
