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
    private final Map<User, Map<Function, Predicate>> matrix = implementation();
    private Map<Function, String> names = implementation();

    @SuppressWarnings("WeakerAccess")
    public <T, R> R invoke(User user, Function<T, R> method, T arg) throws AccessException {
        if (isPermissive(user, method, arg))
            return method.apply(arg);
        else
            throw new AccessException(String.format("access denied for %s@%s(%s) ", user, getName(method), arg));
    }

    @SuppressWarnings("WeakerAccess")
    public <T, R> boolean isPermissive(User user, Function<T, R> method, T arg) throws AccessException {
        Map<Function, Predicate> map = matrix.get(user);
        if (map == null)
            throw new AccessException(String.format("no such user %s", user));
        Predicate predicate = map.get(method);
        if (predicate == null)
            throw new AccessException(String.format("no such method %s in %s scope", getName(method), user));
        //noinspection unchecked
        return predicate.test(arg);
    }

    private <T, R> String getName(Function<T, R> method) {
        return names.get(method);
    }

    public static <K, V> Map<K, V> implementation() {
        return new HashMap<>();
    }

    @SuppressWarnings("WeakerAccess")
    public <T, R> void grant(User user, String name, Function<T, R> method, Predicate<T> predicate) {
        Map<Function, Predicate> map = matrix.get(user);
        if (map == null) {
            matrix.put(user, map = implementation());
        }
        map.put(method, predicate);
        names.put(method, name);
    }

    public <T, R> void grant(User user, Actor actor, Predicate<T> predicate) {
        for (Map.Entry<String, Function<?, ?>> entry : actor.getMethods().entrySet()) {
            grant(user, entry.getKey(), (Function<T, R>) entry.getValue(), predicate);
        }
        user.getActors().add(actor);
    }

    public <T, R> void revoke (User user, Actor actor, Predicate<T> predicate) {
        for (Actor a : user.getActors()) {
            Map<Function, Predicate> functionPredicateMap = matrix.get(user);
        }
        for (Map.Entry<String, Function<?, ?>> entry : actor.getMethods().entrySet()) {
            grant(user, entry.getKey(), (Function<T, R>) entry.getValue(), predicate);
        }
        user.getActors().remove(actor);
    }

    public static void main(String[] args) throws AccessException {
        Access access = new Access();

        User shr = new User("shr");
        Function<Integer, Integer> incrementInt = o -> o + 1;

        Predicate<Integer> p1 = o -> new HashSet<>(Arrays.asList(1, 3, 5, 7, 11, 17)).contains(o);
        Predicate<Integer> p2 = o -> new HashSet<>(Arrays.asList(2, 4, 5, 55, 100)).contains(o);

        access.grant(shr, "incrementInt", incrementInt, p1.or(p2));

        int i1 = access.invoke(shr, incrementInt, 1);
        int i2 = access.invoke(shr, incrementInt, 100);
        System.out.println(" incrementIntResult = " + i1);

        Group holding = new Group("Sun InterBrew", null, 1000_0001);

        Group plantKaluga = new Group("Калуга", holding, 1000_0002, Arrays.asList(1, 2.5, "светлое"));
        Group plantCrimea = new Group("Крым", holding, 1000_0003, Arrays.asList(10, "темное", "бархатное"));

        Function<Object, Object> buy = o -> o;

        access.grant(shr, "buy", buy, plantKaluga::contains);

        Object светлое = access.invoke(shr, buy, "светлое");
        System.out.println("светлое = " + светлое);

        Function<Transaction, Double> transfer = transaction -> transaction.amount;
        access.grant(shr, "transfer", transfer, o -> plantKaluga.hasAccount(o.debit));

        Double rest = access.invoke(shr, transfer, new Transaction(1000_0002, 1000_0003, 1000.0, new Date()));

        System.out.println("Access.main");
    }
}
