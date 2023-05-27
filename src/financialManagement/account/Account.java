package financialManagement.account;

import utils.Enums;
import utils.HelpFunctions;

import java.io.Serializable;

public class Account implements Serializable {
    private String accountName;

    private double accountBalance;
    private double openingBalance;
    private Enums.TaxAccount taxAccount;
    private Enums.AccountType accountType;

    public Account(String accountName, Enums.AccountType accountType, Enums.TaxAccount taxAccount) {
        this.accountName = accountName;
        this.accountType = accountType;
        this.taxAccount = taxAccount;
        this.accountBalance = 0;
        this.openingBalance = 0;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Enums.AccountType getAccountType() {
        return accountType;
    }

    public void setAccountBalance(double number) {
        accountBalance = number;
    }

    public String getFormattedAccountBalance() {
        return HelpFunctions.Format(accountBalance);
    }

    public double getAccountBalanceValue() {
        return accountBalance;
    }

    public void modifyBalance(double balance) {
        this.accountBalance += balance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getFormattedOpeningBalance() {
        return HelpFunctions.Format(openingBalance);
    }

    public Enums.TaxAccount getTaxAccount() {
        return taxAccount;
    }

}
