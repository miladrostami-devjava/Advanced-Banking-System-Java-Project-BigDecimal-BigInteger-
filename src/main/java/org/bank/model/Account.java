package org.bank.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account implements Serializable {

   private static final long serialVersionUID = 1L;

    private final BigInteger accountNumber;
    private final String ownerName;
    private  BigDecimal balance;
    private final Currency currency;// ارز
    private final List<Transaction> history = Collections.synchronizedList(new ArrayList<>());

    public Account(BigInteger accountNumber, String ownerName, BigDecimal balance, Currency currency) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.currency = currency;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }


    public synchronized void deposit(BigDecimal amount , String note){
        balance = balance.add(amount);
        history.add(new Transaction(accountNumber,null,amount,currency, Transaction.Type.DEPOSIT, Instant.now(),note));

    }

    public synchronized void withdraw(BigDecimal amount, String note){
        if (balance.compareTo(amount) < 0 ){
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
        history.add(new Transaction(accountNumber,null,amount,currency, Transaction.Type.WITHDRAW,Instant.now(),note));
    }

    //internal used by transfer to keep single transaction record create by service banking
   public synchronized void applyAmount(BigDecimal delta){
        balance = balance.add(delta);
    }


    public List<Transaction> getHistory(){
        return new ArrayList<>(history);
    }


    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                '}';
    }

    public void addTransaction(Transaction transaction) {
        history.add(transaction);

    }
}
