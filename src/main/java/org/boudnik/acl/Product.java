package org.boudnik.acl;

import java.util.HashMap;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 06/30/17 17:22
 */
public class Product {
    private final String name;
    private final Actor[] actors;

    public Product(String name, Actor... actors) {
        this.name = name;
        this.actors = actors;
    }

    public static final class VRKO extends Product {
        public VRKO(String name, Actor... actors) {
            super(name, actors);
        }

        public static final String TO_STRING = "toString";
        public static final String TRANSFER = "transfer";
        public static final String CERTIFY = "certify";
        public static final Actor CONTROLLER =
                new Actor("Controller",
                        new HashMap<String, Function<?, ?>>() {{
                            put(TO_STRING, toString);
                            put(TRANSFER, transfer);
                            put(CERTIFY, certify);
                        }});
        public static final Actor ACCOUNTANT =
                new Actor("Accountant",
                        new HashMap<String, Function<?, ?>>() {{
                            put(TO_STRING, toString);
                            put(TRANSFER, transfer);
                        }});

        public static final VRKO PRODUCT =
                new VRKO("Валютное РКО",
                        CONTROLLER,
                        ACCOUNTANT
                );
    }

    public static final Function<Integer, String> toString = String::valueOf;
    public static final Function<Transaction, Double> transfer = o -> 0.0;
    public static final Function<Transaction, Double> certify = o -> 0.0;

}
