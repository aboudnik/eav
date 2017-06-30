package org.boudnik.stp;

import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Alexandre_Boudnik
 * @since 05/30/2017
 */
public class ExecutionPlan {
    private String name;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Predicate<Document> check;
    private ExecutionPlan[] planList;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean isSequential;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean isSync;

    private ExecutionPlan(String name, Predicate<Document> check, ExecutionPlan[] planList, boolean isSequential, boolean isSync) {
        this.name = name;
        this.check = check;
        this.planList = planList;
        this.isSequential = isSequential;
        this.isSync = isSync;
    }

    private ExecutionPlan(String name, ExecutionPlan[] planList, boolean isSequential, boolean isSync) {
        this(name, null, planList, isSequential, isSync);
    }

    public void save() {
        walker(plan -> {/*todo actual save for node*/
            return true;
        });
    }

    private Boolean walker(Function<ExecutionPlan, Boolean> function) {
        boolean r = function.apply(this);
        for (int i = 0; r && i < planList.length; i++) {
            r &= planList[i].walker(function);
        }
        return r;
    }

    public Boolean execute(Document document, ExecutionPlan plan, Token token) {
        return Ignition.ignite().compute().affinityCall("execute", null, (IgniteCallable<Boolean>) () -> walker(plan1 -> {
    /*todo */
            ExecutorService exec = Ignition.ignite().executorService(); // todo use it
            return plan.check.test(document);
        }));
    }

    @SuppressWarnings("WeakerAccess")
    public static ExecutionPlan check(String name, Predicate<Document> check) {
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
    public static ExecutionPlan async(String name, Predicate<Document> check) {
        return new ExecutionPlan("async: " + name, check, new ExecutionPlan[0], true, false);
    }

/*
    @SuppressWarnings("WeakerAccess")
    public static ExecutionPlan state(String name, Control... controls) {
        return null;
    }
*/

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

//        Ignite ignite = Ignition.ignite();

//        ignite.compute().affinityCall("execution", );

        ExecutionPlan plan =
                sequential(
                        parallel(
                                check("withdrawal", doc -> StringUtils.isNumeric(doc.getString("src"))),
                                check("beneficiary", doc -> StringUtils.isNumeric(doc.getString("dst"))),
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
//                        state("Ready to sign", Control.SIGN, Control.CANCEL),
                        parallel(
                                async("fraud-MMM", doc -> "МММ".equals(doc.getString("dstName"))),
                                async("fraud-Khopyor", doc -> "Хопер Инвест".equals(doc.getString("dstName")))
                        ),
                        async("submit", doc -> submit("ЦАБС", doc))
                );
        boolean r = plan.walker(p -> {
            System.out.println("p.name = " + p.name);
            return true;
        });

        System.out.println(plan);
    }

    public interface Document {
        String getString(final String field);

        Double getNumber(final String field);
    }

    public interface Token {
        Object getString(final String field);
    }

    public static class Control {
        static final Control CANCEL = new Control();
        static final Control CREATE = new Control();
        static final Control SIGN = new Control();
        static final Control SUBMIT = new Control();
    }
}
