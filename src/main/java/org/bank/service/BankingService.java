package org.bank.service;

import org.bank.model.Account;
import org.bank.model.Currency;
import org.bank.model.Transaction;
import org.bank.util.RSAUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BankingService {

    private final  Map<BigInteger, Account> accounts = new ConcurrentHashMap<>();
    private final MathContext mathContext;
    private final CurrencyConverter converter;

    private final LoanCalculator loanCalculator;
    private final BigDecimal transferFeePercent;//e.g. 0.005 = 0.5%
    private final PersistenceService persistenceService;
    private final SecureRandom random = new SecureRandom();
    private final RSAUtils rsa ;


    public BankingService(MathContext mathContext, CurrencyConverter converter, LoanCalculator loanCalculator, BigDecimal transferFeePercent, PersistenceService persistenceService, RSAUtils rsa) {
        this.mathContext = mathContext;
        this.converter = converter;
        this.loanCalculator = loanCalculator;
        this.transferFeePercent = transferFeePercent;
        this.persistenceService = persistenceService;
        this.rsa = rsa;
    }



    public Account createAccount(String ownerName, BigDecimal initialBalance, Currency currency){
        BigInteger accountNumber;
        do {
            accountNumber = new BigInteger(130,random);//130 bits
            if (accountNumber.signum() <= 0){
                accountNumber = accountNumber.negate();//برگرداندن معکوس علامت
            }

        }while (accounts.containsKey(accountNumber) );

        BigDecimal bal = initialBalance.setScale(8, RoundingMode.HALF_EVEN);
        Account account = new Account(accountNumber,ownerName,bal,currency);

        accounts.put(accountNumber,account);

        return account;
    }




    public void transfer(BigInteger from ,BigInteger to,BigDecimal amount,String note   ){

        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(amount);

        if (amount.signum() <= 0 ){
            throw  new IllegalArgumentException("Amount must be positive.");
        }
        Account aFrom , aTo;
        aFrom = accounts.get(from);
        aTo = accounts.get(to);

        if (aFrom == null || aTo == null){
            throw  new IllegalArgumentException("Account not found.");
        }


        //fee computed in source currency
        BigDecimal fee = amount.multiply(transferFeePercent,mathContext).setScale(8,RoundingMode.HALF_EVEN);
        BigDecimal totalDebit = amount.add(fee).setScale(8,RoundingMode.HALF_EVEN);

        // determine lock order to prevent deadlock
        Account first ,second;
        first = from.compareTo(to) <= 0 ? aFrom : aTo;
        second = from.compareTo(to) <= 0 ? aTo : aFrom;

        synchronized (first){
            synchronized (second){

                BigDecimal available = aFrom.getBalance();
                if (available.compareTo(totalDebit)<0) {
                    throw  new IllegalArgumentException("Insufficient funds");
                }

                //debit source  (منبع بدهی )
                aFrom.applyAmount(totalDebit.negate());

                //compute credit in destination currency (convert if needed)
                BigDecimal creditAccount = amount;
                if (aFrom.getCurrency() != aTo.getCurrency()){
                    creditAccount = converter.convert(amount,aFrom.getCurrency(),aTo.getCurrency());
                }
                aTo.applyAmount(creditAccount);

                Instant now =Instant.now();

                //record transactions
                Transaction toOut = new Transaction(aFrom.getAccountNumber(),aTo.getAccountNumber(),amount,aFrom.getCurrency(), Transaction.Type.TRANSFER,now,note);
                Transaction tFee = new Transaction(aFrom.getAccountNumber(),aTo.getAccountNumber(),creditAccount,aTo.getCurrency(), Transaction.Type.FEE,now,"transfer Fee");
                Transaction tIn = new Transaction(aFrom.getAccountNumber(),aTo.getAccountNumber(),creditAccount,aTo.getCurrency(), Transaction.Type.TRANSFER,now,note);

                aFrom.addTransaction(toOut);
                aFrom.addTransaction(tFee);
                aTo.addTransaction(tIn);
            }
        }

    }

    public Account findAccount(BigInteger accountNumber){
        return accounts.get(accountNumber);
    }

    public  BigDecimal getBalance(BigInteger accountNumber){
        Account account = accounts.get(accountNumber);
        return account == null ? null : account.getBalance();
    }

    public CurrencyConverter getConverter(){
        return converter;
    }

    public LoanCalculator getLoanCalculator(){
        return loanCalculator;
    }


    public RSAUtils.KeyPair generateRSAKeys(int bits){
        return RSAUtils.generateKeys(bits);
    }


    //persistence
    public Map<BigInteger, Account> saveAll() throws Exception{
    //    persistenceService.save(accounts);
        return accounts;
    }

/*    public void loadAll() throws Exception{
        persistenceService.load();
    }*/


    public void loadAll() throws Exception{
        Map<BigInteger ,Account> loaded = persistenceService.load();
        accounts.clear();
        accounts.putAll(loaded);
    }



    public List<Account> listAccounts(){
        return new ArrayList<>(accounts.values());
    }


















}
