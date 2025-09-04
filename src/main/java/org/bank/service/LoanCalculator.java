package org.bank.service;

import org.bank.model.LoanScheduleEntity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanCalculator {

    private final MathContext mathContext;

    public LoanCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    //monthly payment using standard amortization formula
//principal : سرمایه اصلی یا مایه
    public BigDecimal monthlyPayment(BigDecimal principal, BigDecimal annualRate, int months) {

        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(months), mathContext).setScale(2, RoundingMode.HALF_EVEN);
        }

        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), mathContext);
        //payment = P*r/(1-(1+r)^-n)
        BigDecimal onePlusRPowerN = (BigDecimal.ONE.add(monthlyRate)).pow(months, mathContext);
        BigDecimal numerator = principal.multiply(monthlyRate, mathContext).multiply(onePlusRPowerN, mathContext);
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE, mathContext);
        BigDecimal payment = numerator.divide(denominator, mathContext);
        return payment.setScale(2, RoundingMode.HALF_EVEN);
    }


    /**
     * <p>
     * Installment repayment method and loan planning
     * </p>
     */

    public List<LoanScheduleEntity> amortizationSchedule(BigDecimal principal, BigDecimal annualRate, int months, LocalDate startDate) {

        List<LoanScheduleEntity> scheduleEntities = new ArrayList<>();

        BigDecimal balance = principal.setScale(8, RoundingMode.HALF_EVEN);
        BigDecimal payment = monthlyPayment(principal, annualRate, months);
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12", mathContext));

        for (int i = 0; i < months; i++) {

            //interest for this period (rounded to cents)
            //interest = نرخ بهره
            BigDecimal interest = balance.multiply(monthlyRate, mathContext).setScale(2, RoundingMode.HALF_EVEN);

            // principal portion = payment - interest
            BigDecimal principalPaid = payment.subtract(interest).setScale(2, RoundingMode.HALF_EVEN);

            // if principalPaid would exceed remaining balance (due to rounding ), adjust

            if (principalPaid.compareTo(balance.setScale(2, RoundingMode.HALF_EVEN)) > 0) {
                principalPaid = balance.setScale(2, RoundingMode.HALF_EVEN);
                payment = principalPaid.add(interest).setScale(2, RoundingMode.HALF_EVEN);
            }

            // last payment : adjust to fully clear the balance (avoid tiny negative due to rounding)

            if (i == months - 1) {
                //recompute interest one last time using current balance
                interest = balance.multiply(monthlyRate, mathContext).setScale(2, RoundingMode.HALF_EVEN);
                principalPaid = balance.setScale(2, RoundingMode.HALF_EVEN);
            }

            //subtract principal paid from balance
            balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_EVEN);
            scheduleEntities.add(new LoanScheduleEntity(startDate.plusMonths(i), payment, principalPaid, interest, balance));
        }
        return scheduleEntities;
    }


}
