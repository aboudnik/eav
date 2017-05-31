package org.boudnik.stp;

/**
 * @author Alexandre_Boudnik
 * @since 05/30/2017
 */
public class Result {
    private boolean ok;
    private String message;
    private String check;

    public Result(String name, boolean ok, String message) {
        this.ok = ok;
        this.message = ok ? "OKAY" : message;
        this.check = name;
    }

    @Override
    public String toString() {
        return "Result{" +
                "ok=" + ok +
                ", message='" + message + '\'' +
                ", check='" + check + '\'' +
                '}';
    }
}
