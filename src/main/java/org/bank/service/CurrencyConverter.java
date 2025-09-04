package org.bank.service;

import org.bank.model.Currency;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyConverter {

    //rates: base USD for simplicity (1 USD = rate target?  ) we will store as target per 1 USD

    private final Map<Currency, BigDecimal> rates = new ConcurrentHashMap<>();
    private final MathContext mathContext;


    public CurrencyConverter(MathContext mathContext) {
        this.mathContext = mathContext;
        //default rates
        rates.put(Currency.USD,BigDecimal.ONE);
        rates.put(Currency.EUR,new BigDecimal(".92",mathContext));
        rates.put(Currency.IRR,new BigDecimal("42000",mathContext));
        rates.put(Currency.JPY,new BigDecimal("154.30",mathContext));

    }

    public void setRate(Currency currency,BigDecimal rate){
        rates.put(currency,rate);
    }

    public BigDecimal convert(BigDecimal amount,Currency from,Currency to){
        Objects.requireNonNull(amount,"amount");
        Objects.requireNonNull(from,"from");
        Objects.requireNonNull(to,"to");

        if (from == to){
            return amount.setScale(8,RoundingMode.HALF_EVEN);
        }

        BigDecimal rateFrom = rates.get(from);
        BigDecimal rateTo = rates.get(to);

        if (rateFrom == null || rateTo == null){
            throw new IllegalArgumentException("Missing exchange rate for from/to");
        }
        if (rateFrom.compareTo(BigDecimal.ZERO) == 0){
            throw  new ArithmeticException("Invalid exchange rate: division by zero");
        }

      //  BigDecimal usd = amount.divide(rates.get(from),mathContext);
    //    BigDecimal target = usd.multiply(rates.get(to),mathContext);


      BigDecimal target = amount.multiply(rateTo,mathContext)
              .divide(rateFrom,mathContext);




        //scale money to 8 decimal places for internal; persentation can use 2
        return target.setScale(8, RoundingMode.HALF_EVEN);
    }


}
