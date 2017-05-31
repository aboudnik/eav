package org.boudnik.stp;

/**
 * @author Alexandre_Boudnik
 * @since 05/30/2017
 */
public interface Document {
    String getString(final String field);

    Double getNumber(final String field);
}
