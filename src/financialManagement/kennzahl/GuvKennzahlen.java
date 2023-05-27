package financialManagement.kennzahl;

import financialManagement.account.AccountUtils;
import financialManagement.statements.BilanzSum;
import financialManagement.statements.GuvSum;
import utils.Enums;

import java.util.ArrayList;

public class GuvKennzahlen {
    private static ArrayList<Kennzahl> guvKennzahlen = new ArrayList<>();

    public static double getAnnualProfit() {
        return GuvSum.getIncomeSum() - GuvSum.getExpensesSum();
    }

    public static double getPersonalCostQuote() {
        double divider = GuvSum.getCosts();
        if (divider == 0) return 0;
        return ((AccountUtils.GetAccounts().get("62").getAccountBalanceValue() + AccountUtils.GetAccounts().get("64").getAccountBalanceValue()) / divider) * 100;
    }

    public static double getCapitalCostQuote() {
        double divider = GuvSum.getCosts();
        if (divider == 0) return 0;
        return (AccountUtils.GetAccounts().get("75").getAccountBalanceValue() / divider) * 100;
    }

    public static double getThingCostQuote() {
        double divider = GuvSum.getCosts();
        if (divider == 0) return 0;
        return ((
                AccountUtils.GetAccounts().get("670").getAccountBalanceValue()
                        + AccountUtils.GetAccounts().get("677").getAccountBalanceValue()
                        + AccountUtils.GetAccounts().get("68").getAccountBalanceValue()
        )
                / divider)
                * 100;
    }

    public static double getMaterialCostQuote() {
        double divider = GuvSum.getCosts();
        if (divider == 0) return 0;
        return (AccountUtils.GetAccounts().get("60").getAccountBalanceValue() / divider) * 100;
    }

    public static double getUmschlagEK() {
        double divider = AccountUtils.GetAccounts().get("300").getAccountBalanceValue();
        if (divider == 0) return 0;
        return AccountUtils.GetAccounts().get("50").getAccountBalanceValue() / divider;
    }

    public static double getUmschlagGesamt() {
        double divider = BilanzSum.getEk() + BilanzSum.getFk();
        if (divider == 0) return 0;
        return AccountUtils.GetAccounts().get("50").getAccountBalanceValue() / divider;
    }

    public static double getUmschlagForderung() {
        double divider = AccountUtils.GetAccounts().get("240").getAccountBalanceValue();
        if (divider == 0) return 0;
        return AccountUtils.GetAccounts().get("50").getAccountBalanceValue() / divider;
    }

    public static double getAverageKreditDauer() {
        double divider = getUmschlagGesamt();
        if (divider == 0) return 0;
        return 360 / divider;
    }


    public static ArrayList<Kennzahl> getGuvKennzahlen() {
        guvKennzahlen.clear();
        guvKennzahlen.add(new Kennzahl("Jahresüberschuss", getAnnualProfit(), " €", Enums.KennzahlType.ANNUAL));

        guvKennzahlen.add(new Kennzahl("Personalkostenanteil", getPersonalCostQuote(), " %", Enums.KennzahlType.COSTPORTION));
        guvKennzahlen.add(new Kennzahl("Kapitalkostenanteil", getCapitalCostQuote(), " %", Enums.KennzahlType.COSTPORTION));
        guvKennzahlen.add(new Kennzahl("Sachkostenanteil", getThingCostQuote(), " %", Enums.KennzahlType.COSTPORTION));
        guvKennzahlen.add(new Kennzahl("Materialkostenanteil", getMaterialCostQuote(), " %", Enums.KennzahlType.COSTPORTION));

        guvKennzahlen.add(new Kennzahl("Umschlagshäufigkeit Eigenkapital", getUmschlagEK(), "", Enums.KennzahlType.UMSCHLAG));
        guvKennzahlen.add(new Kennzahl("Umschlagshäufigkeit Gesamtkapital", getUmschlagGesamt(), "", Enums.KennzahlType.UMSCHLAG));
        guvKennzahlen.add(new Kennzahl("Umschlagshäufigkeit Forderungen", getUmschlagForderung(), "", Enums.KennzahlType.UMSCHLAG));
        guvKennzahlen.add(new Kennzahl("Durchschnittliche Kreditdauer", getAverageKreditDauer(), " Tage", Enums.KennzahlType.UMSCHLAG));

        return guvKennzahlen;
    }
}
