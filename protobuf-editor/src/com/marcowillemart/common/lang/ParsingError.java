package com.marcowillemart.common.lang;

import com.marcowillemart.common.util.Assert;

/**
 * ParsingError represents an immutable syntax or semantic error.
 *
 * @specfield message : String // The message of the error.
 * @specfield line : int       // The line at which the error occurred.
 * @specfield col : int        // The column at which the error occurred.
 *
 * @invariant message is not empty
 * @invariant line > 0
 * @invariant col > 0
 *
 * @author mwi
 */
public final class ParsingError {

    private final String message;
    private final int line;
    private final int col;

    /**
     * @requires message != null && message.length > 0 && line > 0 && col > 0
     * @effects Makes this be a new syntax error e with e.message = message,
     *          e.line = line, e.col = col.
     */
    public ParsingError(String message, int line, int col) {
        Assert.notEmpty(message);
        Assert.isTrue(line > 0);
        Assert.isTrue(col > 0);

        this.message = message;
        this.line = line;
        this.col = col;
    }

    /**
     * @return this.message
     */
    public String message() {
        return message;
    }

    /**
     * @return this.line
     */
    public int line() {
        return line;
    }

    /**
     * @return this.col
     */
    public int col() {
        return col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ParsingError)) {
            return false;
        }
        ParsingError other = (ParsingError) obj;
        return message.equals(other.message)
                && line == other.line
                && col == other.col;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + message.hashCode();
        hash = 59 * hash + line;
        hash = 59 * hash + col;
        return hash;
    }

    @Override
    public String toString() {
        return String.format("error at line %d and column %d : %s",
                line, col, message);
    }
}
