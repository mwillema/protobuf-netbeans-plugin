package com.marcowillemart.protobuf.editor.lexer;

import com.marcowillemart.common.util.Assert;
import org.netbeans.api.lexer.TokenId;

/**
 * ProtobufTokenId represents an immutable identifier of a Protobuf token.
 *
 * @specfield name : string            // Unique name of the token id.
 * @specfield ordinal : integer        // Unique integer identification of the
 *                                        token id.
 * @specfield primaryCategory : string // Name of primary token category into
 *                                        which the token id belogs.
 *
 * @invariant name not empty
 * @invariant name starts with an uppercase letter
 * @invariant primaryCategory not empty
 * @invariant primaryCateogry is lowercase
 *
 * @author mwi
 */
public final class ProtobufTokenId implements TokenId {

    private final String name;
    private final int ordinal;
    private final String primaryCategory;

    /*
     * Abstraction Function:
     *   name = name
     *   ordinal = ordinal
     *   primaryCategory = primaryCategory
     *
     * Representation Invariant:
     *   name != null
     *   name not empty
     *   name starts with an uppercase letter
     *   primaryCategory != null
     *   primaryCategory not empty
     *   primaryCategory is lowercase
     */

    /**
     * @effects Asserts the check rep holds for this.
     */
    private void checkRep() {
        Assert.notEmpty(name);
        Assert.equals(name.charAt(0), name.toUpperCase().charAt(0));
        Assert.notEmpty(primaryCategory);
        Assert.equals(primaryCategory, primaryCategory.toLowerCase());
    }

    /**
     * @requires name != null &&
     *           name not empty &&
     *           name starts with an uppercase letter &&
     *           primaryCategory != null &&
     *           prmaryCategory not empty &&
     *           primaryCategory is lowercase
     * @effects Makes this be a new ProtobufTokenId ti with ti.name = name,
     *          ti.ordinal = ordinal and ti.primaryCategory = primaryCategory.
     */
    ProtobufTokenId(String name, String primaryCategory, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
        this.primaryCategory = primaryCategory;

        checkRep();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + name.hashCode();
        hash = 59 * hash + ordinal;
        hash = 59 * hash + primaryCategory.hashCode();
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
        if (!(obj instanceof ProtobufTokenId)) {
            return false;
        }
        final ProtobufTokenId other = (ProtobufTokenId) obj;
        return name.equals(other.name)
                && ordinal == other.ordinal
                && primaryCategory.equals(other.primaryCategory);
    }

    @Override
    public String toString() {
        return "ProtobufTokenId{"
                + "name=" + name
                + ", ordinal=" + ordinal
                + ", primaryCategory=" + primaryCategory
                + '}';
    }
}
