package org.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanScheduleEntity {

    private final LocalDate date;
    private final BigDecimal payment;
    private final BigDecimal principal;
    private final BigDecimal interest;
    private final BigDecimal balanceRemaining;

    public LoanScheduleEntity(LocalDate date, BigDecimal payment, BigDecimal principal, BigDecimal interest, BigDecimal balanceRemaining) {
        this.date = date;
        this.payment = payment;
        this.principal = principal;
        this.interest = interest;
        this.balanceRemaining = balanceRemaining;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getBalanceRemaining() {
        return balanceRemaining;
    }

    @Override
    public String toString() {
        return "LoanScheduleEntity{" +
                "date=" + date +
                ", payment=" + payment +
                ", principal=" + principal +
                ", interest=" + interest +
                ", balanceRemaining=" + balanceRemaining +
                '}';
    }
}
