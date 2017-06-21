package org.boudnik.acl;

import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 06/20/17 17:01
 */
public class Test {

    public abstract static class AbstractMethod<T, R> implements Function<T, R> {

    }

    public static class RuledMethod<T, R> extends AbstractMethod<T, R> {
        AbstractMethod<T, R> method;
        Function<T, Boolean> rule;

        public RuledMethod(AbstractMethod<T, R> method, Function<T, Boolean> rule) {
            this.method = method;
            this.rule = rule;
        }

        @Override
        public R apply(T t) {
            if (rule.apply(t))
                return method.apply(t);
            else
                throw new RuntimeException();
        }
    }

    public static void main(String[] args) {

        AbstractMethod<Integer, Boolean> submit = new AbstractMethod<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer o) {
                return true;
            }
        };

        AbstractMethod<Integer, Boolean> guardedSubmit = new RuledMethod<>(submit, i -> i < 1_000_000);

        guardedSubmit.apply(1);
        guardedSubmit.apply(2_000_000);
    }
}
