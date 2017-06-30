package org.boudnik.acl.sample;

import org.boudnik.acl.Actor;

import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 06/22/17 14:35
 */
public class ACL {
    <T, R> R execute(Actor actor, Function<T, R> function, T object) {
        return function.apply(object);
    }
}
