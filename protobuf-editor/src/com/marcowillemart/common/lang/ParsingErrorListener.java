package com.marcowillemart.common.lang;

import com.marcowillemart.common.util.Assert;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ParsingErrorListener represents a mutable listener for the syntactic and
 * semantic checking performed during the parsing process.
 *
 * @specfield errors : List<ParsingError> // The errors collected during
 *                                           the parsing.
 *
 * @author mwi
 */
public final class ParsingErrorListener extends BaseErrorListener {

    private static final Logger LOG =
            LoggerFactory.getLogger(ParsingErrorListener.class);

    private final List<ParsingError> errors;

    /**
     * @effects Makes this be a new parsing error listener l with l.errors = [].
     */
    public ParsingErrorListener() {
        super();

        this.errors = new LinkedList<>();
    }

    /**
     * @requires line != null  && line > 0  && charPositionInLine >= 0 &&
     *           message != null && message not empty
     * @modifies this
     * @effects Adds a new syntax error e with e.message = message,
     *          e.line = line and e.col = charPositionInLine + 1 to this.errors.
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
            int line, int charPositionInLine, String message,
            RecognitionException ex) {

        addError(message, line, charPositionInLine);
    }

    /**
     * @requires message != null && message not empty && token != null
     * @modifies this
     * @effects Adds a new semantic error e with e.message = message,
     *          e.line = token.line and e.col = token.charPositionInLine + 1 to
     *          this.errors.
     */
    public void semanticError(String message, Token token) {
        Assert.notNull(message);
        Assert.notNull(token);

        addError(message, token.getLine(), token.getCharPositionInLine());
    }

    /**
     * @return this.errors
     */
    public List<ParsingError> errors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * @return true iff this.errors.size > 0
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @requires message != null && message not empty && line > 0 &&
     *           charPositionInLine >= 0
     * @modifies this
     * @effects Adds a new parsing error e with e.message = message,
     *          e.line = line and e.col = charPositionInLine + 1 to this.errors.
     */
    private void addError(String message, int line, int charPositionInLine) {
        // Adds 1 because Antlr starts at col 0 instead of col 1
        errors.add(new ParsingError(message, line, charPositionInLine + 1));

        LOG.debug(errors.get(errors.size() - 1).toString());
    }
}
