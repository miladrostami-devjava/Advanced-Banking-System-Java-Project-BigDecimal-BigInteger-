package org.bank.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    public enum Type {
        TRANSFER,DEPOSIT,WITHDRAW,FEE
    }

    private final BigInteger fromAccount;// nullable for deposit
    private final BigInteger toAccount;// nullable for withdraw
    private final BigDecimal amount ;
    private final Currency currency;
    private final Type type;
    private final Instant timestamp;
    private final String note;


    public Transaction(BigInteger fromAccount, BigInteger toAccount, BigDecimal amount, Currency currency, Type type, Instant timestamp, String note) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.timestamp = timestamp;
        this.note = note;
    }


    public BigInteger getFromAccount() {
        return fromAccount;
    }

    public BigInteger getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Type getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", currency=" + currency +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", note='" + note + '\'' +
                '}';
    }
}
