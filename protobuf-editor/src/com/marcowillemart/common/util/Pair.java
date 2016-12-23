package com.marcowillemart.common.util;

/**
 * Pair represents an immutable pair consisting of two Object elements.
 *
 * Although the implementation is immutable, there is no restriction on the
 * objects that may be stored. If mutable objects are stored in the pair, then
 * the pair itself effectively becomes mutable.
 *
 * @specfield left : L  // The left element of type L of the pair.
 * @specfield right : R // The right element of type R of the pair.
 *
 * @author mwi
 */
public final class Pair<L,R> {

    private final L left;
    private final R right;

    /**
     * @requires left and right are not null
     * @effects Makes this be a new pair p with p = (left, right).
     */
    public Pair(L left, R right) {
        Assert.notNull(left);
        Assert.notNull(right);

        this.left = left;
        this.right = right;
    }

    /**
     * @return this.left
     */
    public L left() {
        return left;
    }

    /**
     * @return this.right
     */
    public R right() {
        return right;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + left.hashCode();
        hash = 23 * hash + right.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Pair)) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        return left.equals(other.left) && right.equals(other.right);
    }

    @Override
    public String toString() {
        return toString("(%s, %s)");
    }

    /**
     * @requires format != null && format is a format string containing exactly
     *           two string variables
     * @return the string representation of this where this.left is substituted
     *         to the first occurence of %s and this.right to the second
     *         occurence of %s.
     */
    public String toString(String format) {
        return String.format(format, left.toString(), right.toString());
    }

    /**
     * @requires left != null && right != null
     * @return a new pair p = (left, right)
     */
    public static <L,R> Pair<L,R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
