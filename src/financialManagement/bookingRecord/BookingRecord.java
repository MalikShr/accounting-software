package financialManagement.bookingRecord;

import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import utils.HelpFunctions;

import java.io.Serializable;

public class BookingRecord implements Serializable {
    private String bookingDescription, bookingText, date;
    private Account sollAccount, habenAccount;
    private double sollAmount, habenAmount;
    private double taxes;
    private String sollAccountNumber;
    private String habenAccountNumber;

    public BookingRecord(String date, String bookingDescription, Account sollAccount, Account habenAccount, double sollAmount, double habenAmount, double taxes) {
        this.date = date;
        this.bookingDescription = bookingDescription;
        this.sollAccount = sollAccount;
        this.habenAccount = habenAccount;
        this.sollAmount = sollAmount;
        this.habenAmount = habenAmount;
        this.taxes = taxes;
        this.bookingText = getFormattedBookingText();

        this.sollAccountNumber = AccountUtils.GetAccountNumber(sollAccount);
        this.habenAccountNumber = AccountUtils.GetAccountNumber(habenAccount);
    }

    public String getBookingText() {
        return bookingText;
    }

    public String getFormattedDate() {
        return date;
    }

    public String getFormattedBookingAmount() {
        if (AccountUtils.IsVst(sollAccount, habenAccount)) {
            return String.format("<html>%s<br>%s</br><br>%s</br></html>", HelpFunctions.Format(sollAmount), HelpFunctions.Format(taxes), HelpFunctions.Format(habenAmount));
        }
        if (AccountUtils.IsUst(habenAccount, sollAccount)) {
            return String.format("<html>%s<br>%s</br><br>%s</br></html>", HelpFunctions.Format(sollAmount), HelpFunctions.Format(habenAmount), HelpFunctions.Format(taxes));
        }
        return HelpFunctions.Format(sollAmount);
    }

    public String getFormattedBookingText() {
        if (AccountUtils.IsVst(sollAccount, habenAccount)) {
            return String.format("<html>%s %s<br>%s %s</br><br>an %s %s</br></html>",
                    sollAccountNumber, sollAccount.getAccountName()
                    , "260", AccountUtils.GetAccounts().get("260").getAccountName()
                    , habenAccountNumber, habenAccount.getAccountName()
            );
        }
        if (AccountUtils.IsUst(habenAccount, sollAccount)) {
            return String.format("<html>%s %s<br>an %s %s</br><br>an %s %s</br></html>",
                    sollAccountNumber, sollAccount.getAccountName()
                    , habenAccountNumber, habenAccount.getAccountName()
                    , "480", AccountUtils.GetAccounts().get("480").getAccountName()
            );
        }
        return String.format("%s %s an %s %s", AccountUtils.GetAccountNumber(sollAccount), sollAccount.getAccountName(), AccountUtils.GetAccountNumber(habenAccount), habenAccount.getAccountName());
    }

    public String[] getFormattedBookingTextPdf() {
        if (AccountUtils.IsVst(sollAccount, habenAccount)) {
            String[] texts = {
                    sollAccountNumber + " " + sollAccount.getAccountName(),
                    "260 " + AccountUtils.GetAccounts().get("260").getAccountName(),
                    "an " + habenAccountNumber + " " + habenAccount.getAccountName()
            };
            return texts;
        }
        if (AccountUtils.IsUst(habenAccount, sollAccount)) {
            String[] texts = {
                    sollAccountNumber + " " + sollAccount.getAccountName(),
                    "an " + habenAccountNumber + " " + habenAccount.getAccountName(),
                    "an " + "480 " + AccountUtils.GetAccounts().get("480").getAccountName()

            };
            return texts;
        }
        String[] texts = {sollAccountNumber + " " + sollAccount.getAccountName() + " an " + habenAccountNumber + " " + habenAccount.getAccountName()};
        return texts;
    }

    public String[] getFormattedBookingAmountPdf() {
        if (AccountUtils.IsVst(sollAccount, habenAccount)) {
            String[] amounts = {HelpFunctions.Format(sollAmount), HelpFunctions.Format(taxes), HelpFunctions.Format(habenAmount)};
            return amounts;
        }
        if (AccountUtils.IsUst(habenAccount, sollAccount)) {
            String[] amounts = {HelpFunctions.Format(sollAmount), HelpFunctions.Format(habenAmount), HelpFunctions.Format(taxes)};
            return amounts;
        }
        String[] amounts = {HelpFunctions.Format(sollAmount)};
        return amounts;
    }


}
