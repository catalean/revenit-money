package ro.cb.finance.storage.util;

import java.util.UUID;

/**
 *
 */
public final class UUIDGenerator {

    /**
     * Constructor, private as this class declares only static methods and it should never be instantiated.
     */
    private UUIDGenerator() {
        //do nothing
    }

    /**
     * @return a new {@link UUID} as string of length 32 and all upper case characters.
     */
    public static String next() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(); // replace "-" 36 -> 32 char
    }
}
