package com.marcowillemart.protobuf.editor.parser;

import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.SchedulerTask;
import org.netbeans.modules.parsing.spi.TaskFactory;

/**
 * ParsingErrorHighlightingTaskFactory is a factory for creating
 * ParsingErrorHighlightingTasks.
 *
 * @author mwi
 */
@MimeRegistration(
        mimeType = "application/x-protobuf",
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
