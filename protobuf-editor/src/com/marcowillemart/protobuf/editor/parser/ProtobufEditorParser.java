package com.marcowillemart.protobuf.editor.parser;

import com.marcowillemart.common.lang.ParsingError;
import com.marcowillemart.common.lang.ParsingErrorListener;
import com.marcowillemart.common.util.Assert;
import com.marcowillemart.protobuf.parser.ProtobufLexer;
import com.marcowillemart.protobuf.parser.ProtobufParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;

/**
 * ProtobufEditorParser represents a mutable parser for the Protobuf editor.
 *
 * @author mwi
 */
public final class ProtobufEditorParser extends Parser {

    private final List<ParsingError> errors;

    private Snapshot snapshot;

    /**
     * @effects Makes this be a new Protobuf editor parser.
     */
    public ProtobufEditorParser() {
        this.errors = new LinkedList<>();
        this.snapshot = null;
    }

    @Override
    public void parse(
            Snapshot snapshot,
            Task task,
            SourceModificationEvent event) throws ParseException {

        Assert.notNull(snapshot);

        this.snapshot = snapshot;

        CharStream input =
                new ANTLRInputStream(snapshot.getText().toString());

        Lexer lexer = new ProtobufLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProtobufParser parser = new ProtobufParser(tokens);

        parser.removeErrorListeners();
        ParsingErrorListener listener = new ParsingErrorListener();
        parser.addErrorListener(listener);

        ParseTree tree = parser.proto();
        // TODO def and ref phases

        this.errors.clear();
        this.errors.addAll(listener.errors());
    }

    @Override
    public Result getResult(Task task) throws ParseException {
        return new ProtobufEditorParserResult(snapshot, errors);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
    }

    ////////////////////
    // INNER CLASSES
    ////////////////////

    /**
     * Represents result of Protobuf parsing created for one specific Task.
     */
    public static final class ProtobufEditorParserResult extends ParserResult {

        private final List<ParsingError> errors;
        private boolean valid;

        /**
         * @requires snapshot != null && parser != null
         * @effects Makes this be a new result with the given snapshot and
         *          parsing errors.
         */
        ProtobufEditorParserResult(
                Snapshot snapshot,
                List<ParsingError> errors) {

            super(snapshot);

            this.errors = new ArrayList<>(errors);
            this.valid = true;
        }

        /**
         * @requires this has not been invalidated yet
         * @return a read-only view of the errors of this
         */
        public List<ParsingError> errors() {
            Assert.isTrue(valid);

            return Collections.unmodifiableList(errors);
        }

        @Override
        protected void invalidate() {
            valid = false;
        }

        @Override
        public List<? extends Error> getDiagnostics() {
            return Collections.emptyList();
        }
    } // end ProtobufEditorParserResult
}
