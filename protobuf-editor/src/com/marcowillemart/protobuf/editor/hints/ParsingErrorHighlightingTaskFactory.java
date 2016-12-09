package com.marcowillemart.protobuf.editor.hints;

import com.marcowillemart.protobuf.Protobuf;
import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.SchedulerTask;
import org.netbeans.modules.parsing.spi.TaskFactory;

/**
 * ParsingErrorHighlightingTaskFactory is a stateless factory responsible for
 * creating parsing error highlighting tasks.
 *
 * @author mwi
 */
@MimeRegistration(
        mimeType = Protobuf.MIME_TYPE,
        service = TaskFactory.class)
public final class ParsingErrorHighlightingTaskFactory extends TaskFactory {

    /**
     * @effects Makes this be a new task factory.
     */
    public ParsingErrorHighlightingTaskFactory() {
    }

    @Override
    public Collection<? extends SchedulerTask> create (Snapshot snapshot) {
        return Collections.singleton(new ParsingErrorHighlightingTask());
    }
}
