package com.marcowillemart.common.lang;

import com.marcowillemart.common.util.Assert;

/**
 * Abstract implementation of the Line interface proving implementations of the
 * equals, hashCode and toString methods.
 *
 * @author mwi
 */
public abstract class AbstractLine implements Line {

    /*
     * Representation Invariant:
     *   number() > 0
     *   number() = 1 <-> startOffset() = 0
     *   number() > 1 -> startOffset() > 0
     *   startOffset() <= endOffset()
     */

    /**
     * @effects Asserts the rep invariant holds for this.
     */
    protected void checkRep() {
        Assert.isTrue(number() > 0);
        if (number() == 1 || startOffset() == 0) {
            Assert.equals(1, number());
            Assert.equals(0, startOffset());
        }
        if (number() > 1) {
            Assert.isTrue(startOffset() > 0);
        }
        Assert.isTrue(startOffset() <= endOffset());
    }

    /**
     * @effects Makes this be a new Line.
     */
    protected AbstractLine() {
    }

    @Override
    public final int hashCode() {
        int hash = 5;
        hash = 43 * hash + number();
        hash = 43 * hash + startOffset();
        hash = 43 * hash + endOffset();
        hash = 43 * hash + text().hashCode();
        return hash;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Line)) {
            return false;
        }
        final Line other = (Line) obj;
        return number() == other.number()
                && startOffset() == other.startOffset()
                && endOffset() == other.endOffset()
                && text().equals(other.text());
    }

    @Override
    public final String toString() {
        return "Line{"
                + "number=" + number()
                + ", startOffset=" + startOffset()
                + ", endOffset=" + endOffset()
                + ", text='" + text() + "'"
                + '}';
    }
}
