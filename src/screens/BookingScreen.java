package screens;

import File.Directory;
import File.WriterToFile;
import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import financialManagement.bookingRecord.BookingRecord;
import financialManagement.bookingRecord.BookingRecordUtils;
import main.ProgramFrame;
import main.ProgramPanel;
import utils.Date;
import utils.Enums;
import utils.HelpFunctions;

import javax.swing.*;
import java.util.Map;

public class BookingScreen extends ProgramPanel {
    double sollAmount;
    double habenAmount;
    double taxes;
    private JTextField textFieldDate;
    private JTextField textFieldBookingText;
    private Account sollAccount;
    private Account habenAccount;
    private double bookingAmount;
    private JPanel panel;
    private JButton buchenButton;
    private JComboBox habenAccountCombo;
    private JComboBox sollAccountCombo;
    private JTextField bookingAmountText;
    private JButton reverseButton;
    private BookingRecord bookingRecord;

    public BookingScreen(ProgramFrame frame) {
        createList();

        textFieldDate.setText(Date.getTodayDate());

        buchenButton.addActionListener(e -> {
            if (bookingAmountText.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Gebe einen Betrag ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] keysArray = AccountUtils.GetBuchungAccounts().keySet().toArray(new String[AccountUtils.GetBuchungAccounts().keySet().size()]);

            sollAccount = AccountUtils.GetAccounts().get(keysArray[sollAccountCombo.getSelectedIndex()]);
            habenAccount = AccountUtils.GetAccounts().get(keysArray[habenAccountCombo.getSelectedIndex()]);

            if (sollAccount.equals(habenAccount)) {
                JOptionPane.showMessageDialog(null, "Das Soll und Haben Konto dürfen nicht dasselbe sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                bookingAmount = HelpFunctions.unFormat(bookingAmountText.getText());
                handleTaxes();

                if (AccountUtils.CheckAccountType(sollAccount, Enums.AccountType.AKTIV)) {
                    sollAccount.modifyBalance(sollAmount);
                } else if (AccountUtils.CheckAccountType(sollAccount, Enums.AccountType.PASSIV)) {
                    sollAccount.modifyBalance(-sollAmount);
                }

                if (AccountUtils.CheckAccountType(habenAccount, Enums.AccountType.AKTIV)) {
                    habenAccount.modifyBalance(-habenAmount);
                } else if (AccountUtils.CheckAccountType(habenAccount, Enums.AccountType.PASSIV)) {
                    habenAccount.modifyBalance(habenAmount);
                }

                if (AccountUtils.CheckAccountType(sollAccount, Enums.AccountType.AUFWAND))
                    sollAccount.modifyBalance(sollAmount);
                else if (AccountUtils.CheckAccountType(sollAccount, Enums.AccountType.ERTRAG)) {
                    sollAccount.modifyBalance(-sollAmount);
                }

                if (AccountUtils.CheckAccountType(habenAccount, Enums.AccountType.AUFWAND))
                    habenAccount.modifyBalance(-habenAmount);
                else if (AccountUtils.CheckAccountType(habenAccount, Enums.AccountType.ERTRAG)) {
                    habenAccount.modifyBalance(habenAmount);
                }

                handleBookingRecord(frame);

                JOptionPane.showMessageDialog(null, "Buchung erfolgreich!");
                clearInput();
            } catch (NumberFormatException error) {
                error.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ungültige Eingabe!");
            }
        });

        reverseButton.addActionListener(e -> {
            Object habenAccountSelectedItem = habenAccountCombo.getSelectedItem();
            Object sollAccountSelectedItem = sollAccountCombo.getSelectedItem();

            sollAccountCombo.setSelectedItem(habenAccountSelectedItem);
            habenAccountCombo.setSelectedItem(sollAccountSelectedItem);
        });

    }

    public void createList() {
        sollAccountCombo.removeAllItems();
        habenAccountCombo.removeAllItems();

        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetBuchungAccounts().entrySet()) {
            String listItem = accountEntry.getKey() + " " + accountEntry.getValue().getAccountName();
            sollAccountCombo.addItem(listItem);
            habenAccountCombo.addItem(listItem);
        }
    }

    private void clearInput() {
        bookingAmountText.setText("");
        textFieldBookingText.setText("");
    }

    private void handleTaxes() {
        if (AccountUtils.IsVst(sollAccount, habenAccount)) {
            sollAmount = bookingAmount / 1.19;
            taxes = bookingAmount - sollAmount;
            habenAmount = bookingAmount;
            AccountUtils.GetAccounts().get("260").modifyBalance(taxes);
        } else if (AccountUtils.IsUst(habenAccount, sollAccount)) {
            sollAmount = bookingAmount;
            habenAmount = bookingAmount / 1.19;
            taxes = bookingAmount - habenAmount;
            AccountUtils.GetAccounts().get("480").modifyBalance(taxes);
        } else {
            sollAmount = bookingAmount;
            habenAmount = bookingAmount;
            taxes = 0;
        }
    }

    private void handleBookingRecord(ProgramFrame frame) {
        bookingRecord = new BookingRecord(Date.getTodayDate(), textFieldBookingText.getText(), sollAccount, habenAccount, sollAmount, habenAmount, taxes);
        BookingRecordUtils.GetBookingRecords().add(bookingRecord);
        BookingRecordUtils.CreateBookingRecord(frame.getBookingRecordScreen().getTableModel(), bookingRecord);

        Directory.GetAccountsFile().delete();
        Directory.GetBookingRecordsFile().delete();

        WriterToFile.InsertDataToFile();
        HelpFunctions.UpdateAllPanels(frame);
    }

    public JPanel getPanel() {
        return panel;
    }

}
