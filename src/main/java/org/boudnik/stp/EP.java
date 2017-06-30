package org.boudnik.stp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Alexandre_Boudnik
 * @since 06/28/17 20:01
 */
public class EP {

    Predicate<Object> predicate;
    List<EP> list;
    Boolean result;
    EP previous;
    String name;

    public EP(String name, Predicate<Object> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    protected boolean isReady() {
        return previous == null || previous.result != null;
    }

    @NotNull
    private static EP check(final String name, Predicate<Object> predicate) {
        return new EP(name, predicate);
    }

    @NotNull
    public EP sequential(EP first, EP... rest) {
        List<EP> n = concatenate(new EP[]{first}, rest);
        EP e = this;
        for (EP aList : n) {
            aList.previous = e;
            e = aList;
        }
        return n.get(n.size() - 1);
    }

    @NotNull
    private static List<EP> concatenate(EP[] f, EP[] s) {
        return new ArrayList<EP>() {{
            addAll(Arrays.asList(f));
            addAll(Arrays.asList(s));
        }};
    }

    @NotNull
    private EP parallel(String name, EP first, EP second, EP... rest) {
        List<EP> concatenated = concatenate(new EP[]{first, second}, rest);
        EP leave = new EP(name + ".leave", o -> {
            if (isReady()) {
                for (EP n : concatenated) {
                    if (!n.predicate.test(o))
                        return false;
                }
                return true;
            } else
                return false;
        }) {
            {
                list = concatenated;
            }

            @Override
            protected boolean isReady() {
                for (EP ep : list) {
                    if (!ep.isReady())
                        return false;
                }
                return true;
            }
        };
        return leave;
    }

    public static void main(String[] args) {
        EP ep = check("0", o -> true);
        EP e = ep.sequential(
                check("1", o -> true)).
                parallel("2",
                        check("3", o -> true),
                        check("4", o -> true),
                        check("5", o -> true)
                );
        System.out.println("ep = " + ep);
    }
}
