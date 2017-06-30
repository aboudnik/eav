package org.boudnik.acl.sample;


import java.util.function.Supplier;

/**
 * @author Alexandre_Boudnik
 * @since 06/22/17 14:22
 */
public class Lazy<T> implements Supplier<T> {
    private Supplier<T> supplier;
    private T instance = null;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return instance == null ? (instance = supplier.get()) : instance;
    }
}
