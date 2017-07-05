package org.boudnik.acl;

import java.util.Date;

/**
 * @author Alexandre_Boudnik
 * @since 06/30/17 16:20
 */
public class Transaction {
    public long debit;
    private long credit;
    double amount;
    private Date date;

    public Transaction(long debit, long credit, double amount, Date date) {
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "debit=" + debit +
                ", credit=" + credit +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
