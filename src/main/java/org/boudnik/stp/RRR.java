package org.boudnik.stp;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 05/30/2017
 */
public class RRR {
    String name;
    Function<Document, Boolean> check;
    boolean ok;
    String message;

    @SuppressWarnings("WeakerAccess")
    public RRR(String name, Function<Document, Boolean> check, String message) {
        this.name = name;
        this.check = check;
        this.message = message;
    }

    @Override
    public String toString() {
        return "RRR{" +
                /*"name='" + */name + '\'' +
//                ", check=" + check +
                '}';
    }
}
