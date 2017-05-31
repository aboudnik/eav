package org.boudnik.stp;

import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 05/30/2017
 */
public class ExecutionPlan {
    private String name;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Function<Document, Boolean> check;
    private ExecutionPlan[] planList;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean isSequential;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean isSync;

    private ExecutionPlan(String name, Function<Document, Boolean> check, ExecutionPlan[] planList, boolean isSequential, boolean isSync) {
        this.name = name;
        this.check = check;
        this.planList = planList;
        this.isSequential = isSequential;
        this.isSync = isSync;
    }

    private ExecutionPlan(String name, ExecutionPlan[] planList, boolean isSequential, boolean isSync) {
        this(name, null, planList, isSequential, isSync);
    }

    @SuppressWarnings("WeakerAccess")
    public static ExecutionPlan check(String name, Function<Document, Boolean> check) {
        return new ExecutionPlan(name, check, new ExecutionPlan[0], true, true);
    }

    @SuppressWarnings("WeakerAccess")
    public static ExecutionPlan parallel(ExecutionPlan... parallel) {
        return new ExecutionPlan("parallel", parallel, false, true);
    }

    @SuppressWarnings("WeakerAccess")
    public static ExecutionPlan sequential(ExecutionPlan... sequential) {
        return new ExecutionPlan("sequential", sequential, true, true);
    }

    @SuppressWarnings("WeakerAccess")
    public static ExecutionPlan async(String name, Function<Document, Boolean> check) {
        return new ExecutionPlan("async: " + name, check, new ExecutionPlan[0], true, false);
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    private static Boolean submit(String ABS, Document doc) {
        return true;
    }

    @Override
    public String toString() {
        return getString(0);
    }

    private String getString(int indent) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            s.append(" ");
        }
        StringBuilder sb = new StringBuilder();
        if (planList.length > 0) {
            for (ExecutionPlan plan : planList) {
                sb.append(s).append("\n").append(plan.getString(indent + 2)).append(",");
            }
        }
        return s + "{" + name + " " + sb.toString() + "}";
    }

    public static void main(String[] args) {
        ExecutionPlan plan =
                sequential(
                        parallel(
                                check("1", doc -> doc.getNumber("1") > 1),
                                check("2", doc -> doc.getNumber("2") > 1),
                                check("3", doc -> doc.getNumber("3") > 1),
                                check("4", doc -> doc.getNumber("4") > 1),
                                check("5", doc -> doc.getNumber("5") > 1),
                                sequential(
                                        check("a", doc -> doc.getNumber("a") > 1),
                                        check("b", doc -> doc.getNumber("b") > 1),
                                        check("c", doc -> doc.getNumber("c") > 1)
                                )
                        ),
                        async("fraud", doc -> "MMM".equals(doc.getString("dstName"))),
                        async("submit", doc -> submit("ЦАБС", doc))
                );
        System.out.println(plan);
    }
}
