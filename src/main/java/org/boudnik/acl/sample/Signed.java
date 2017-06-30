package org.boudnik.acl.sample;

import java.io.OutputStream;

/**
 * @author Alexandre_Boudnik
 * @since 06/22/17 17:01
 */
public interface Signed {
    OutputStream getDocument();

    byte[] getSignature();

    String getSigningAlgorithm();
}
