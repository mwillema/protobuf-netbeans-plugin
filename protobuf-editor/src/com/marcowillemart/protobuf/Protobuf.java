package com.marcowillemart.protobuf;

/**
 * Protobuf is a utility class that provides useful information about the
 * Protocol Buffers language.
 *
 * @author mwi
 */
public final class Protobuf {

    public static final String MIME_TYPE = "application/x-protobuf";

    /** this cannot be instantiated */
    private Protobuf() {
        throw new AssertionError();
    }
}
