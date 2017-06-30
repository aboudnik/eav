package org.boudnik.acl;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Sergey_Kalinov
 * @author Alexandre_Boudnik
 * @version 1.0
 * @since 20-Jun-2017 18:43:31
 */
public class RuledMethod<T, R> implements Function<T, R> {
    private Function<T, R> method;
    private Predicate<T> rule;

    @Override
    public R apply(T o) {
        if (rule.test(o))
            return method.apply(o);
        throw new RuntimeException();
    }
}