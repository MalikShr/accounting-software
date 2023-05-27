package screens;

import File.FileExplorer;
import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import financialManagement.bookingRecord.BookingRecord;
import financialManagement.bookingRecord.BookingRecordUtils;
import main.ProgramFrame;
import main.ProgramPanel;
import utils.Date;
import utils.Enums;
import utils.HelpFunctions;
import utils.ProjectUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class NewProjectScreen extends ProgramPanel {
    JPanel panel = new JPanel();
    JPanel headerPanel = new JPanel();
    JPanel warningPanel = new JPanel();
    JPanel directoryPanel = new JPanel();
    JPanel accountsPanel = new JPanel();
    JPanel footerPanel = new JPanel();
    Border errorBorder = new LineBorder(Color.RED, 2, true);
    GridBagConstraints gbc = new GridBagConstraints();

    TreeMap<String, JTextField> accountNames = new TreeMap<>();
    TreeMap<String, JTextField> openingBalances = new TreeMap<>();
    String path;
    String a = "aawt";

    public NewProjectScreen(ProgramFrame frame) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JLabel header = new JLabel("Neues Projekt einrichten");
        header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, 20));

        JLabel warning = new JLabel("Achtung! Diese Einstellungen können später nicht mehr geändert werden!");

        JLabel newDirectory = new JLabel("Speicherort");
        JTextField directoryText = new JTextField();
        directoryText.setColumns(20);
        JButton directoryButton = new JButton("...");

        JScrollPane accountsPane = new JScrollPane(accountsPanel);
        accountsPanel.setLayout(new GridBagLayout());

        JLabel konto = new JLabel("Konto");
        JLabel bezeichnung = new JLabel("Bezeichnung (empfohlen)");
        JLabel ab = new JLabel("Anfangsbestand zum 01.01.");

        JButton discard = new JButton("Abbrechen");
        JButton create = new JButton("Neues Projekt erstellen");

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        accountsPanel.setBackground(new Color(235, 248, 255));

        addGb(konto, 0, 0, 1, 1);
        addGb(bezeichnung, 1, 0, 1, 1);
        addGb(ab, 4, 0, 1, 1);

        setTextFieldValues();

        headerPanel.add(header);
        headerPanel.setBackground(new Color(174, 238, 238));

        warningPanel.add(warning);
        warningPanel.setBackground(new Color(174, 238, 238));

        directoryPanel.add(newDirectory);
        directoryPanel.add(directoryText);
        directoryPanel.add(directoryButton);
        directoryPanel.setBackground(new Color(174, 238, 238));

        directoryText.setEditable(false);

        footerPanel.add(discard);
        footerPanel.add(create);
        footerPanel.setBackground(new Color(174, 238, 238));

        panel.add(headerPanel);
        panel.add(warning);
        panel.add(directoryPanel);
        panel.add(accountsPane);
        panel.add(footerPanel);
        panel.setBackground(new Color(174, 238, 238));

        directoryButton.addActionListener(e -> {
            int selection = JFileChooser.FILES_ONLY;

            path = FileExplorer.getFileExplorerPath("Neues Projekt", selection, null).getAbsolutePath();
            if (path != null) {
                directoryText.setText(path);
                directoryText.setBorder(null);
            }
        });

        create.addActionListener(e -> {
            if (directoryText.getText().equals("")) {
                directoryText.setBorder(errorBorder);
                JOptionPane.showMessageDialog(null, "Du musst einen gültigen Speicherort für dein Projekt auswählen!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (containsDuplicateValues()) {
                JOptionPane.showMessageDialog(null, "Accountnamen dürfen nicht doppelt vergeben werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AccountUtils.ResetAccounts();
            BookingRecordUtils.GetBookingRecords().clear();
            Date.BookingNr = 1;
            HelpFunctions.ClearTable(frame.getBookingRecordScreen().getTableModel());

            try {
                setAccountNames(frame);
                ProjectUtils.CreateNewProject(frame, path);
                setOpeningBalance();
                resetDirectory(directoryText);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Du kannst nur numerische Werte zu den Anfangsbeständen hinzufügen", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        discard.addActionListener(e -> {
            frame.setContentPane(frame.navigateDashboardPanel());
            resetDirectory(directoryText);
            resetNames();
            setOpeningBalance();
            AccountUtils.LoadAccountList();
        });
    }


    private void setAccountNames(ProgramFrame frame) {
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetAccounts().entrySet()) {
            String textFieldValueName = accountNames.get(accountEntry.getKey()).getText();
            accountEntry.getValue().setAccountName(textFieldValueName);

            double textFieldValue;
            if (!openingBalances.get(accountEntry.getKey()).getText().equals("nicht begehbar")) {
                textFieldValue = HelpFunctions.unFormat(openingBalances.get(accountEntry.getKey()).getText());
            } else {
                textFieldValue = 0;
            }
            accountEntry.getValue().setOpeningBalance(textFieldValue);
            accountEntry.getValue().modifyBalance(textFieldValue);
            bookOpening(frame, accountEntry.getValue(), textFieldValue);
        }
    }

    public void setTextFieldValues() {
        int i = 0;
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetAccounts().entrySet()) {
            ++i;
            Account account = accountEntry.getValue();
            String key = accountEntry.getKey();

            JTextField accountNrTextfield = new JTextField(key);
            accountNrTextfield.setHorizontalAlignment(SwingConstants.CENTER);
            accountNrTextfield.setEditable(false);
            accountNrTextfield.setFocusable(false);

            JTextField accountBezTextfield = new JTextField(account.getAccountName());
            accountBezTextfield.setHorizontalAlignment(SwingConstants.LEFT);

            JTextField accountAbTextfield = new JTextField();
            accountAbTextfield.setHorizontalAlignment(SwingConstants.RIGHT);

            accountNames.put(key, accountBezTextfield);
            openingBalances.put(key, accountAbTextfield);

            setOpeningBalance();

            addGb(accountNrTextfield, 0, i + 1, 1, 1);
            addGb(accountBezTextfield, 1, i + 1, 1, 1);
            addGb(accountAbTextfield, 4, i + 1, 1, 1);
        }
    }

    public void resetNames() {
        for (Map.Entry<String, JTextField> entry : accountNames.entrySet()) {
            entry.getValue().setText(AccountUtils.GetAccounts().get(entry.getKey()).getAccountName());
        }
    }

    public void setOpeningBalance() {
        for (Map.Entry<String, JTextField> openingBalanceEntry : openingBalances.entrySet()) {
            String key = openingBalanceEntry.getKey();
            JTextField accountObTextField = openingBalanceEntry.getValue();

            if (AccountUtils.GetBestandAccounts().containsKey(key)) {
                accountObTextField.setText("0,00");
            } else {
                accountObTextField.setText("nicht begehbar");
                accountObTextField.setFocusable(false);
                accountObTextField.setEditable(false);
            }
        }
    }

    public boolean containsDuplicateValues() {
        Set<String> uniqueValues = new HashSet<>();

        for (JTextField text : accountNames.values()) {
            if (!uniqueValues.add(text.getText())) {
                return true;
            }
        }
        return false;
    }

    public void resetDirectory(JTextField textField) {
        textField.setBorder(null);
        textField.setText("");
    }

    private void addGb(Component component, int x, int y, int gridwidth, int gridheight) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        accountsPanel.add(component, gbc);
    }

    private void bookOpening(ProgramFrame frame, Account account, double amount) {
        if (account.getAccountBalanceValue() == 0) return;
        if (account.getAccountType().equals(Enums.AccountType.AKTIV)) {
            BookingRecord bookingRecord = new BookingRecord(Date.getBeginYearDate(), "Aktiv Eröffnung", AccountUtils.GetAccounts().get("800"), account, amount, amount, 0);
            BookingRecordUtils.CreateBookingRecord(frame.getBookingRecordScreen().getTableModel(), bookingRecord);
            BookingRecordUtils.GetBookingRecords().add(bookingRecord);
        }
        if (account.getAccountType().equals(Enums.AccountType.PASSIV)) {
            BookingRecord bookingRecord = new BookingRecord(Date.getBeginYearDate(), "Aktiv Eröffnung", account, AccountUtils.GetAccounts().get("800"), amount, amount, 0);
            BookingRecordUtils.CreateBookingRecord(frame.getBookingRecordScreen().getTableModel(), bookingRecord);
            BookingRecordUtils.GetBookingRecords().add(bookingRecord);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

}
