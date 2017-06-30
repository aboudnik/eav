package org.boudnik.acl;

import java.rmi.AccessException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Alexandre_Boudnik
 * @since 06/29/17 20:50
 */
public class Access {
    private final Map<String, Map<Function, Predicate>> matrix = implementation();

    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    public <T, R> R invoke(String user, Function<T, R> method, T arg) throws AccessException {
        Map<Function, Predicate> map = matrix.get(user);
        if (map == null)
            throw new AccessException(String.format("no such user %s", user));
        Predicate predicate = map.get(method);
        if (predicate == null)
            throw new AccessException(String.format("no such method %s in %s scope", method, user));
        //noinspection unchecked
        if (predicate.test(arg))
            return method.apply(arg);
        else
            throw new AccessException(String.format("access for %s to %s(%s) denied", user, method, arg));
    }

    private <K, V> Map<K, V> implementation() {
        return new HashMap<>();
    }

    @SuppressWarnings("WeakerAccess")
    public <T, R> void put(String user, Function<T, R> method, Predicate<T> predicate) {
        Map<Function, Predicate> map = matrix.get(user);
        if (map == null) {
            matrix.put(user, map = implementation());
        }
        map.put(method, predicate);
    }


    public static void main(String[] args) throws AccessException {
        Access access = new Access();

        String user = "shr";
        Function<Integer, Integer> method = o -> o + 1;

        access.put(user, method, o -> o < 6);

        Integer result = access.<Integer, Integer>invoke(user, i -> 1 + 6, 3);

        System.out.println("result = " + result);
    }
}
