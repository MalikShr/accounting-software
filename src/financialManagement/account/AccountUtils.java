package financialManagement.account;

import utils.Enums;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AccountUtils {
    private static TreeMap<String, Account> allAccounts = new TreeMap<>();
    private static TreeMap<String, Account> buchungAccounts = new TreeMap<>();
    private static TreeMap<String, Account> bestandAccounts = new TreeMap<>();
    private static TreeMap<String, Account> erfolgAccounts = new TreeMap<>();

    public static String[] setTextFieldAccountValues(String key, Account account) {
        String accountNr = AccountUtils.GetAccountNumber(account);
        String accountBez = account.getAccountName();
        String accountType;
        String taxAccount;
        String ab;
        String saldo;

        if (account.getAccountType().equals(Enums.AccountType.PASSIV) || account.getAccountType().equals(Enums.AccountType.AKTIV)) {
            accountType = "Bilanz";
        } else if (account.getAccountType().equals(Enums.AccountType.ERTRAG) || account.getAccountType().equals(Enums.AccountType.AUFWAND)) {
            accountType = "GuV";
        } else {
            accountType = "Ergebnis";
        }

        if (account.getAccountType().equals(Enums.AccountType.ERGEBNIS)) {
            saldo = "nicht begehbar";
        } else {
            saldo = account.getFormattedAccountBalance();
        }

        if (account.getTaxAccount().equals(Enums.TaxAccount.ACCOUNT260)) {
            taxAccount = "260" + " " + AccountUtils.GetAccounts().get("260").getAccountName();
        } else if (account.getTaxAccount().equals(Enums.TaxAccount.ACCOUNT480)) {
            taxAccount = "480" + " " + AccountUtils.GetAccounts().get("480").getAccountName();
        } else {
            taxAccount = "nicht begehbar";
        }
        if (AccountUtils.GetBestandAccounts().containsKey(key)) {
            ab = account.getFormattedOpeningBalance();
        } else {
            ab = "nicht begehbar";
        }

        String[] values = {accountNr, accountBez, accountType, taxAccount, ab, saldo};

        return values;
    }

    public static void LoadAccountList() {
        AccountUtils.GetAccounts().clear();
        AccountUtils.GetBestandAccounts().clear();
        AccountUtils.GetErfolgAccounts().clear();

        allAccounts.put("05", new Account("Grundstücke und Bauten", Enums.AccountType.AKTIV, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("07", new Account("Technische Anlagen und Maschinen", Enums.AccountType.AKTIV, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("084", new Account("Fuhrpark", Enums.AccountType.AKTIV, Enums.TaxAccount.ACCOUNT260));

        allAccounts.put("20", new Account("Vorräte", Enums.AccountType.AKTIV, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("240", new Account("Forderungen", Enums.AccountType.AKTIV, Enums.TaxAccount.NONE));
        allAccounts.put("260", new Account("Vorsteuer", Enums.AccountType.AKTIV, Enums.TaxAccount.NONE));
        allAccounts.put("27", new Account("Wertpapiere", Enums.AccountType.AKTIV, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("280", new Account("Bank", Enums.AccountType.AKTIV, Enums.TaxAccount.NONE));
        allAccounts.put("288", new Account("Kasse", Enums.AccountType.AKTIV, Enums.TaxAccount.NONE));

        allAccounts.put("300", new Account("Eigenkapital", Enums.AccountType.PASSIV, Enums.TaxAccount.NONE));
        allAccounts.put("3001", new Account("Privatkonto", Enums.AccountType.PASSIV, Enums.TaxAccount.NONE));

        allAccounts.put("420", new Account("Kurzfristige Bankverbindlichkeiten", Enums.AccountType.PASSIV, Enums.TaxAccount.NONE));
        allAccounts.put("425", new Account("Langfristige Bankverbindlichkeiten", Enums.AccountType.PASSIV, Enums.TaxAccount.NONE));
        allAccounts.put("44", new Account("Verbindlichkeiten LL", Enums.AccountType.PASSIV, Enums.TaxAccount.NONE));
        allAccounts.put("480", new Account("Umsatzsteuer", Enums.AccountType.PASSIV, Enums.TaxAccount.NONE));

        allAccounts.put("50", new Account("Umsatzerlöse für eigene Erzeugnisse", Enums.AccountType.ERTRAG, Enums.TaxAccount.ACCOUNT480));
        allAccounts.put("540", new Account("Mieterträge", Enums.AccountType.ERTRAG, Enums.TaxAccount.ACCOUNT480));
        allAccounts.put("542", new Account("EGusL", Enums.AccountType.ERTRAG, Enums.TaxAccount.ACCOUNT480));
        allAccounts.put("56", new Account("Erträge aus Wertpapieren", Enums.AccountType.ERTRAG, Enums.TaxAccount.ACCOUNT480));
        allAccounts.put("571", new Account("Zinserträge", Enums.AccountType.ERTRAG, Enums.TaxAccount.ACCOUNT480));

        allAccounts.put("60", new Account("Aufwendungen für Roh-, Hilfs- und Betriebsstoffe", Enums.AccountType.AUFWAND, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("616", new Account("Fremdinstandhaltung", Enums.AccountType.AUFWAND, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("62", new Account("Löhne und Gehälter", Enums.AccountType.AUFWAND, Enums.TaxAccount.NONE));
        allAccounts.put("64", new Account("Soziale Abgaben", Enums.AccountType.AUFWAND, Enums.TaxAccount.NONE));
        allAccounts.put("65", new Account("Abschreibungen", Enums.AccountType.AUFWAND, Enums.TaxAccount.NONE));
        allAccounts.put("670", new Account("Mieten", Enums.AccountType.AUFWAND, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("677", new Account("Rechts- und Beratungskosten", Enums.AccountType.AUFWAND, Enums.TaxAccount.ACCOUNT260));
        allAccounts.put("68", new Account("Aufwendung für Kommunikation", Enums.AccountType.AUFWAND, Enums.TaxAccount.ACCOUNT260));

        allAccounts.put("70", new Account("Betriebliche Steuern", Enums.AccountType.AUFWAND, Enums.TaxAccount.NONE));
        allAccounts.put("75", new Account("Zinsaufwendungen", Enums.AccountType.AUFWAND, Enums.TaxAccount.NONE));

        allAccounts.put("800", new Account("EBK", Enums.AccountType.ERGEBNIS, Enums.TaxAccount.NONE));
        allAccounts.put("801", new Account("SBK", Enums.AccountType.ERGEBNIS, Enums.TaxAccount.NONE));
        allAccounts.put("802", new Account("GuV", Enums.AccountType.ERGEBNIS, Enums.TaxAccount.NONE));
    }

    public static TreeMap<String, Account> GetBuchungAccounts() {
        buchungAccounts.clear();
        for (Map.Entry<String, Account> entry : allAccounts.entrySet()) {
            if (!entry.getKey().equals("260") && !entry.getKey().equals("480") && !entry.getValue().getAccountType().equals(Enums.AccountType.ERGEBNIS)) {
                buchungAccounts.put(entry.getKey(), entry.getValue());
            }
        }
        return buchungAccounts;
    }

    public static TreeMap<String, Account> GetBestandAccounts() {
        bestandAccounts.clear();
        for (Map.Entry<String, Account> entry : GetBuchungAccounts().entrySet()) {
            if (entry.getValue().getAccountType().equals(Enums.AccountType.AKTIV) || entry.getValue().getAccountType().equals(Enums.AccountType.PASSIV)) {
                bestandAccounts.put(entry.getKey(), entry.getValue());
            }
        }
        return bestandAccounts;
    }

    public static TreeMap<String, Account> GetErfolgAccounts() {
        erfolgAccounts.clear();
        for (Map.Entry<String, Account> entry : GetBuchungAccounts().entrySet()) {
            if (entry.getValue().getAccountType().equals(Enums.AccountType.ERTRAG) || entry.getValue().getAccountType().equals(Enums.AccountType.AUFWAND)) {
                erfolgAccounts.put(entry.getKey(), entry.getValue());
            }
        }
        return erfolgAccounts;
    }

    public static TreeMap<String, Account> GetAccounts() {
        return allAccounts;
    }

    public static void SetAccounts(TreeMap<String, Account> accounts) {
        allAccounts = accounts;
    }

    public static boolean CheckAdded(Account account, ArrayList<Account> alreadyAdded) {
        for (Account value : alreadyAdded) {
            if (account == value) return true;
        }
        return false;
    }

    public static void ResetAccounts() {
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetAccounts().entrySet()) {
            accountEntry.getValue().setAccountBalance(0);
        }
    }

    public static boolean IsVst(Account sollAccount, Account habenAccount) {
        if (sollAccount.getTaxAccount().equals(Enums.TaxAccount.ACCOUNT260) && habenAccount.getTaxAccount().equals(Enums.TaxAccount.NONE)) {
            return true;
        }
        return false;
    }

    public static boolean IsUst(Account habenAccount, Account sollAccount) {
        if (habenAccount.getTaxAccount().equals(Enums.TaxAccount.ACCOUNT480) && sollAccount.getTaxAccount().equals(Enums.TaxAccount.NONE)) {
            return true;
        }
        return false;
    }

    public static String GetAccountNumber(Account account) {
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetAccounts().entrySet()) {
            if (accountEntry.getValue().equals(account)) {
                return accountEntry.getKey();
            }
        }
        return "";
    }

    public static boolean CheckAccountType(Account account, Enums.AccountType type) {
        return account.getAccountType().equals(type);
    }

}
