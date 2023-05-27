package financialManagement.statements;

import financialManagement.account.AccountUtils;

public class BilanzSum {
    public static double getAnlage() {
        return AccountUtils.GetAccounts().get("05").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("07").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("084").getAccountBalanceValue();
    }

    public static double getUmlauf() {
        return AccountUtils.GetAccounts().get("20").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("240").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("27").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("280").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("288").getAccountBalanceValue();
    }

    public static double getFk() {
        return AccountUtils.GetAccounts().get("420").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("425").getAccountBalanceValue()
                + AccountUtils.GetAccounts().get("44").getAccountBalanceValue();
    }

    public static double getEk() {
        return (getAnlage() + getUmlauf()) - getFk();
    }

    public static double getAktivSumme() {
        return getAnlage() + getUmlauf();
    }

    public static double getPassivSumme() {
        return getEk() + getFk();
    }
}
