package org.boudnik.acl;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Sergey_Kalinov
 * @author Alexandre_Boudnik
 * @version 1.0
 * @since 20-Jun-2017 18:43:29
 */
public class Actor {
    private String name;
    private Map<String, Function<?, ?>> methods;

    public Actor(String name, Map<String, Function<?, ?>> methods) {
        this.name = name;
        this.methods = methods;
    }

    public Map<String, Function<?, ?>> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", methods=" + methods.keySet() +
                '}';
    }
}