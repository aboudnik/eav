package org.boudnik.acl.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alexandre_Boudnik
 * @since 06/22/17 14:15
 */
public class Account {
    private Lazy<List<Transaction>> transactions = new Lazy<>(ArrayList::new);

    public static class Transaction {
        Account debit;
        Account credit;
        double amount;
        Date date;

        public Transaction(Account debit, Account credit, double amount, Date date) {
            this.debit = debit;
            this.credit = credit;
            this.amount = amount;
            this.date = date;
        }
    }

    public List<Transaction> getTransactions() {
        return transactions.get();
    }

    public static void main(String[] args) {
        Account account = new Account();
        List<Transaction> transactions = account.getTransactions();
        ACL acl = new ACL();
        System.out.println("transactions = " + transactions);
        System.out.println("execute = " + acl.execute(null, Account::getTransactions, account));
    }
}
