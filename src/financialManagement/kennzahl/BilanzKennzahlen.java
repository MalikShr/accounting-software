package financialManagement.kennzahl;

import financialManagement.account.AccountUtils;
import financialManagement.statements.BilanzSum;
import utils.Enums;

import java.util.ArrayList;

public class BilanzKennzahlen {
    private static ArrayList<Kennzahl> bilanzKennzahlen = new ArrayList<>();

    public static double getEkQuote() {
        double divider = BilanzSum.getEk() + BilanzSum.getFk();
        if (divider == 0) return 0;
        return (BilanzSum.getEk() / divider) * 100;
    }

    public static double getFkQuote() {
        double divider = BilanzSum.getEk() + BilanzSum.getFk();
        if (divider == 0) return 0;
        return (BilanzSum.getFk() / divider) * 100;
    }

    public static double getVg() {
        double divider = BilanzSum.getEk();
        if (divider == 0) return 0;
        return (BilanzSum.getFk() / divider) * 100;
    }

    public static double getGesamtVermoegen() {
        return BilanzSum.getAnlage() + BilanzSum.getUmlauf();
    }

    public static double getAi() {
        double divider = getGesamtVermoegen();
        if (divider == 0) return 0;
        return (BilanzSum.getAnlage() / divider) * 100;
    }

    public static double getUi() {
        double divider = getGesamtVermoegen();
        if (divider == 0) return 0;
        return (BilanzSum.getUmlauf() / divider) * 100;
    }

    public static double getDg1() {
        double divider = BilanzSum.getAnlage();
        if (divider == 0) return 0;
        return BilanzSum.getEk() / divider;
    }

    public static double getDg2() {
        double divider = BilanzSum.getAnlage();
        if (divider == 0) return 0;
        return (BilanzSum.getEk() + AccountUtils.GetAccounts().get("425").getAccountBalanceValue()) / divider;
    }

    public static double getLg1() {
        double divider = AccountUtils.GetAccounts().get("420").getAccountBalanceValue();
        if (divider == 0) return 0;
        return (AccountUtils.GetAccounts().get("280").getAccountBalanceValue() + AccountUtils.GetAccounts().get("288").getAccountBalanceValue() / divider) * 100;
    }

    public static double getLg2() {
        double divider = AccountUtils.GetAccounts().get("420").getAccountBalanceValue();
        if (divider == 0) return 0;
        return ((
                AccountUtils.GetAccounts().get("240").getAccountBalanceValue()
                        + AccountUtils.GetAccounts().get("280").getAccountBalanceValue()
                        + AccountUtils.GetAccounts().get("288").getAccountBalanceValue()
                        + AccountUtils.GetAccounts().get("27").getAccountBalanceValue())
                /
                divider) * 100;
    }

    public static double getLg3() {
        double divider = AccountUtils.GetAccounts().get("420").getAccountBalanceValue();
        if (divider == 0) return 0;
        return (BilanzSum.getUmlauf() / divider) * 100;
    }

    public static double getWorkingCapital() {
        return BilanzSum.getUmlauf() - AccountUtils.GetAccounts().get("420").getAccountBalanceValue();
    }

    public static ArrayList<Kennzahl> GetBilanzKennzahlen() {
        bilanzKennzahlen.clear();
        bilanzKennzahlen.add(new Kennzahl("Eigenkapitalquote", getEkQuote(), " %", Enums.KennzahlType.QUOTE));
        bilanzKennzahlen.add(new Kennzahl("Fremdkapitalquote", getFkQuote(), " %", Enums.KennzahlType.QUOTE));
        bilanzKennzahlen.add(new Kennzahl("Verschuldungsgrad", getVg(), " %", Enums.KennzahlType.QUOTE));

        bilanzKennzahlen.add(new Kennzahl("Umlaufintensität", getUi(), " %", Enums.KennzahlType.INTENSITY));
        bilanzKennzahlen.add(new Kennzahl("Anlageintensität", getAi(), " %", Enums.KennzahlType.INTENSITY));

        bilanzKennzahlen.add(new Kennzahl("Deckungsgrad I", getDg1(), "", Enums.KennzahlType.DG));
        bilanzKennzahlen.add(new Kennzahl("Deckungsgrad II", getDg2(), "", Enums.KennzahlType.DG));

        bilanzKennzahlen.add(new Kennzahl("Liquiditätsgrad I", getLg1(), " %", Enums.KennzahlType.LG));
        bilanzKennzahlen.add(new Kennzahl("Liquiditätsgrad II", getLg2(), " %", Enums.KennzahlType.LG));
        bilanzKennzahlen.add(new Kennzahl("Liquiditätsgrad III", getLg3(), " %", Enums.KennzahlType.LG));

        bilanzKennzahlen.add(new Kennzahl("Working Capital(Betriebskapital)", getWorkingCapital(), " €", Enums.KennzahlType.WC));

        return bilanzKennzahlen;
    }
}
