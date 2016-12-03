package com.marcowillemart.protobuf.editor.lexer.util;

import com.marcowillemart.common.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.misc.Interval;
import org.netbeans.spi.lexer.LexerInput;

/**
 * AntlrCharStream is a CharStream that allows the ANTLR lexer to use the
 * NetBeans lexer as input.
 *
 * @see http://www.antlr.org/api/Java/org/antlr/v4/runtime/CharStream.html
 *
 * @author mwi
 */
public final class AntlrCharStream implements CharStream {

    private static final Logger LOG =
            Logger.getLogger(AntlrCharStream.class.getName());

    private final LexerInput input;
    private final String name;

    private int index = 0;
    private int line = 1;
    private int charPositionInLine = 0;

    private List<CharStreamState> markers;
    private int markDepth = 0;

    /**
     * @requires input != null && name != null && name not empty
     * @effects Makes this be a new ANTLR char stream with the given input,
     *          name and the ignore case iff 'ignoreCase' is true.
     */
    public AntlrCharStream(LexerInput input, String name) {
        Assert.notNull(input);
        Assert.notEmpty(name);

        this.input = input;
        this.name = name;
    }

    @Override
    public String getText(Interval interval) {
        return input.readText(interval.a, interval.b).toString();
    }

    @Override
    public void consume() {
        int currentSymbol = input.read();

        index++;
        charPositionInLine++;

        if (currentSymbol == '\n') {
            line++;
            charPositionInLine = 0;
        }
    }

    @Override
    public int LA(int i) {
        if (i == 0) {
            // undefined behavior, precondition is violated
            return 0;
        }

        if (i < 0) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        int symbol = 0;

        for (int j = 0; j < i && symbol != IntStream.EOF; j++) {
            symbol = read();
        }

        backup(i);
        return symbol;
    }

    @Override
    public int mark() {
        if (markers == null) {
            markers = new ArrayList<>();
            markers.add(null); // depth 0 means no backtracking, leave blank
        }

        markDepth++;

        CharStreamState state;

        if (markDepth >= markers.size()) {
            state = new CharStreamState();
            markers.add(state);
        } else {
            state = markers.get(markDepth);
        }

        state.index = index;
        state.line = line;
        state.charPositionInLine = charPositionInLine;

        return markDepth;
    }

    @Override
    public void release(int marker) {
        // unwind any other markers made after 'marker' and release 'marker'
        markDepth = marker;
        // release this marker
        markDepth--;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public void seek(int index) {
        if (index < this.index) {
            backup(this.index - index);
            // just jump; don't update stream state (line, ...)
            this.index = index;
            return;
        }

        // seek forward, consume until p hits index
        while (this.index < index) {
            consume();
        }
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("size of stream is unknown");
    }

    @Override
    public String getSourceName() {
        return name;
    }

    ////////////////////
    // HELPER METHODS
    ////////////////////

    /**
     * @return a valid character from input or IntStream.EOF if there is no more
     *         characters available on input.
     */
    private int read() {
        int result = input.read();

        if (result == LexerInput.EOF) {
            result = IntStream.EOF;
        }

        return result;
    }

    /**
     * @requires count >=  0
     * @modifies
     * @effects Undo last count of read() operations.
     */
    private void backup(int count) {
        input.backup(count);
    }

    ////////////////////
    // INNER CLASSES
    ////////////////////

    /**
     * CharStreamState is a state of a char stream.
     */
    private static class CharStreamState {
        int index;
        int line;
        int charPositionInLine;
    } // end CharStreamState
}
