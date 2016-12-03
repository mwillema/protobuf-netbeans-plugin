package com.marcowillemart.protobuf.editor.lexer.util;

import com.marcowillemart.common.util.Assert;
import java.util.Arrays;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.misc.IntegerStack;

/**
 * AntlrLexerState represents an immutable state of an ANTLR lexer, such that it
 * can be applied back to the lexer instance at a later time.
 *
 * @specfield mode : integer                  // The current lexer mode.
 * @specfield modeStack : List<Integer> [0-1] // The current lexer mode stack.
 *
 * @author mwi
 */
public final class AntlrLexerState {

    private final int mode;
    private final int[] modeStack;

    /*
     * Abstraction Function:
     *   mode = mode
     *   modeStack = modeStack
     *
     * Representation Invariant:
     *   true
     */

    /**
     * @effects Makes this be a new AntlrLexerState s with s.mode = mode and
     *          s.modeStack = modeStack if modeStack != null, else
     *          s.modeStack = nil
     */
    public AntlrLexerState(int mode, IntegerStack modeStack) {
        this.mode = mode;
        this.modeStack = modeStack != null ? modeStack.toArray() : null;
    }

    /**
     * @requires lexer != null
     * @modifies lexer
     * @effects Applies this to lexer
     */
    public void apply(Lexer lexer) {
        Assert.notNull(lexer);

        lexer._mode = mode;
        lexer._modeStack.clear();

        if (modeStack != null) {
            lexer._modeStack.addAll(modeStack);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.mode;
        hash = 71 * hash + Arrays.hashCode(this.modeStack);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AntlrLexerState)) {
            return false;
        }
        final AntlrLexerState other = (AntlrLexerState) obj;
        return mode == other.mode && Arrays.equals(modeStack, other.modeStack);
    }
}
