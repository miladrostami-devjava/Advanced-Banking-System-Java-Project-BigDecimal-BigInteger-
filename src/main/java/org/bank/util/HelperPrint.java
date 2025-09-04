package org.bank.util;

public class HelperPrint {


    public void print(String text){
        System.out.println(text);
    }

    public void showMenu(){
        print("\n === Banking System Menu === ");
        print("1. Create Account");
        print("2. Deposit");
        print("3. Withdraw");
        print("4. Transfer");
        print("5. Show Accounts");
        print("6. Currency Conversion");
        print("7. Loan Amortization Schedule ");
        print("8. Encrypt Demo");
        print("9. Save Account To File");
        print("0. Exit From Program");
    }

}
