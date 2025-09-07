package org.bank;


import org.bank.model.Account;
import org.bank.model.Currency;
import org.bank.model.LoanScheduleEntity;
import org.bank.service.BankingService;
import org.bank.service.CurrencyConverter;
import org.bank.service.LoanCalculator;
import org.bank.service.PersistenceService;
import org.bank.util.HelperPrint;
import org.bank.util.RSAUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainApp {


    public static void main(String[] args) {
        BigDecimal transferFeePercent = new BigDecimal("0.005");
        HelperPrint help = new HelperPrint();
        MathContext context = new MathContext(34);
        CurrencyConverter converter = new CurrencyConverter(context);
        PersistenceService persistenceService = new PersistenceService(new File("bank_data.json"));
        RSAUtils rsa = new RSAUtils();

        LoanCalculator loanCalculator = new LoanCalculator(context);
        BankingService service = new BankingService(context, converter, loanCalculator, transferFeePercent, persistenceService, rsa);
        Scanner scanner = new Scanner(System.in);

        Account account = null;


        while (true) {
            help.showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice) {
                case 1://create Account
                   /* if (account == null) {
                        help.print("Account already exists:" + account);
                        break;
                    }*/
                    help.print("Enter owner name:");
                    String ownerName = scanner.nextLine();
                    help.print("Enter your Balance");
                    BigDecimal balance = scanner.nextBigDecimal();
                    scanner.nextLine();
                    help.print("Enter your currency(USD,EUR,IRR,JPY):");
                    String currencyInput = scanner.nextLine().toUpperCase();
                    Currency currency = Currency.valueOf(currencyInput);
                    account = service.createAccount(ownerName, balance, currency);
                    help.print("Account created:" + "  " + account);
                    break;

                case 2://Deposit
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }
                    help.print("Enter your deposit amount:");
                    BigDecimal depAmount = scanner.nextBigDecimal();
                    help.print("Enter Description:");
                    String desc = scanner.nextLine();
                    account.deposit(depAmount, desc);
                    help.print("Deposit successful. New Balance :" + " " + account.getBalance());
                    break;


                case 3://withdraw
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }

                    help.print("Enter Withdraw Amount:");
                    BigDecimal withDrawAmount = scanner.nextBigDecimal();
                    help.print("Enter Description:");
                    String descWithDraw = scanner.nextLine();
                    account.withdraw(withDrawAmount, descWithDraw);
                    help.print("WithDraw successful. New Balance :" + " " + account.getBalance());
                    break;
                case 4://Transfer
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }
                    help.print("From Account ID:");
                    BigInteger fromAccountID = scanner.nextBigInteger();
                    help.print("To Account ID:");
                    BigInteger toAccountID = scanner.nextBigInteger();
                    help.print("Enter  Amount To Transfer:");
                    BigDecimal amountForTransfer = scanner.nextBigDecimal();
                    scanner.nextLine();
                    help.print("Enter Description:");
                    String descTransfer = scanner.nextLine();
                    service.transfer(fromAccountID, toAccountID, amountForTransfer, descTransfer);
                    help.print("Transfer successfully completed.");
                    help.print("Transferred" + " " + amountForTransfer + " " + "from Account Id :" + " " + fromAccountID +
                            " " + " to  Account Id :" + " " + toAccountID);
                    BigDecimal newBalanceFrom = service.getBalance(fromAccountID);
                    BigDecimal newBalanceTo = service.getBalance(toAccountID);

                    help.print("New balance - From Account :" + " " + newBalanceFrom
                            + " " + ", To Account :" + " " + newBalanceTo);
                    break;

                case 5://Show Accounts
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }
                    help.print("Show Accounts:");
                    for (Account acc : service.listAccounts()){
                        System.out.println(acc);
                    }
                  //  service.listAccounts();
                    break;
                case 6:// Currency Conversion
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }
                    //     public BigDecimal convert(BigDecimal amount,Currency from,Currency to){
                    help.print("Enter  Amount To Currency Conversion:");
                    BigDecimal amountForCurrency = scanner.nextBigDecimal();
                    scanner.nextLine();
                    help.print("Enter your From currency(USD,EUR,IRR,JPY):");
                    String fromCurrencyInput = scanner.nextLine().toUpperCase();
                    Currency fromCurrency = Currency.valueOf(fromCurrencyInput);

                    help.print("Enter your To currency(USD,EUR,IRR,JPY):");
                    String toCurrencyInput = scanner.nextLine().toUpperCase();
                    Currency toCurrency = Currency.valueOf(toCurrencyInput);
                    BigDecimal result = converter.convert(amountForCurrency, fromCurrency, toCurrency);
                    help.print("Converted successful :" + " " + fromCurrencyInput + " " + toCurrencyInput);
                    break;

                case 7://Loan Amortization Schedule
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }
                    help.print("Principal:");
                    BigDecimal principal = scanner.nextBigDecimal();

                    help.print("Annual Rate (e.g. .012):");
                    BigDecimal annualRate = scanner.nextBigDecimal();
                    scanner.nextLine();
                    help.print("Enter Months:");
                    int months = scanner.nextInt();
                    scanner.nextLine();
                    help.print("Enter start Date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine();
                    LocalDate startDate = LocalDate.parse(dateInput);
                    List<LoanScheduleEntity> scheduleEntityList = loanCalculator.amortizationSchedule(principal, annualRate, months, startDate);
                    help.print("Loan Schedule:");
                    scheduleEntityList.forEach(System.out::println);
                    break;
                case 8:// Encrypt Demo
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }

                    help.print("Enter number to encrypt:");
                    BigInteger msg = scanner.nextBigInteger();
                    scanner.nextLine();
              //      if (rsa.getKeyPair() == null){
                    RSAUtils.KeyPair keyPair =    RSAUtils.generateKeys(1024);
                //    }

                   // RSAUtils.KeyPair keyPair = rsa.getKeyPair();
                    BigInteger encrypted = rsa.encrypt(msg, keyPair.pubExp, keyPair.modules);
                    help.print("Encrypted :" + " " + encrypted);
                    break;

                case 9: //Save Account To File
                    if (account == null) {
                        help.print("No account exists! Please create and account first.");
                        break;
                    }

                    try {
                        persistenceService.save(service.saveAll());
                        help.print("Accounts saved successfully.");
                    } catch (Exception e) {
                        help.print("Error saving accounts :" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                    break;
                case 0://Exit From Program
                    help.print("Exit of Program");
                    return;

                default:
                    help.print("Invalid choice");


            }


        }


    }


}
