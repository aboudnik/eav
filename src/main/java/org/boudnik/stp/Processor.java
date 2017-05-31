package org.boudnik.stp;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 05/30/2017
 */
public class Processor {
    @SuppressWarnings("WeakerAccess")
    static List<Set<RRR>> executionPlan =
            sequential(
                    parallel(
                            new RRR("withdrawal", document -> StringUtils.isNumeric(document.getString("src")), "wrong withdrawal account"),
                            new RRR("beneficiary", document -> StringUtils.isNumeric(document.getString("dst")), "wrong beneficiary account")
                    ),
                    parallel(
                            new RRR("amount", document -> document.getNumber("amount") > 0, "negative or zero ammout")
                    ),
                    async(
                            new RRR("fraud", document -> "MMM".equals(document.getString("dst_name")), "fraud detected")
                    )
            );

    @SafeVarargs
    private static List<Set<RRR>> sequential(Set<RRR>... sequential) {
        List<Set<RRR>> executionPlan = new ArrayList<>();
        executionPlan.addAll(Arrays.asList(sequential));
        return executionPlan;
    }

    private static Set<RRR> parallel(final RRR... parallel) {
        Set<RRR> checks = new HashSet<>();
        Collections.addAll(checks, parallel);
        return checks;
    }

    private static Set<RRR> async(final RRR async) {
        Set<RRR> checks = new HashSet<>();
        checks.add(new RRR(async.name, document -> async.check.apply(document), async.message));
        return checks;
    }

    public static void main(String[] args) {
        System.out.println("executionPlan = " + executionPlan);
    }
}