package financialManagement.statements;

import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import utils.Enums;

import java.util.Map;

public class GuvSum {
    public static double getIncomeSum() {
        double totalSum = 0.0;
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetErfolgAccounts().entrySet()) {
            if (accountEntry.getValue().getAccountType().equals(Enums.AccountType.ERTRAG)) {
                totalSum += accountEntry.getValue().getAccountBalanceValue();
            }
        }
        return totalSum;
    }

    public static double getExpensesSum() {
        double totalSum = 0.0;
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetErfolgAccounts().entrySet()) {
            if (accountEntry.getValue().getAccountType().equals(Enums.AccountType.AUFWAND)) {
                totalSum += accountEntry.getValue().getAccountBalanceValue();
            }
        }
        return totalSum;
    }

    public static double calculateWin() {
        if (getIncomeSum() - getExpensesSum() > 0) {
            return getIncomeSum() - getExpensesSum();
        }
        return 0;
    }

    public static double calculateLoose() {
        if (getExpensesSum() - getIncomeSum() > 0) {
            return getExpensesSum() - getIncomeSum();
        }
        return 0;
    }

    public static double getCosts() {
        return AccountUtils.GetAccounts().get("60").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("62").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("64").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("68").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("670").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("75").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("677").getAccountBalanceValue();
    }
}
