package screens;

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
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class BookingRecordsScreen extends ProgramPanel {
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Nr.", "Datum", "Buchungssatz", "Betrag (in €)"}, 0);
    private JPanel panel;
    private JTable table;
    private JScrollPane pane;
    private JButton aBestands;
    private JButton aErfolgs;
    private JComboBox sort;
    private JTextField filterTextField;
    private JComboBox filter;
    private JButton filterButton;
    private ArrayList<Account> alreadyAdded = new ArrayList<>();

    public BookingRecordsScreen(ProgramFrame frame) {
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pane.getViewport().setBackground(new Color(235, 248, 255));
        createTable();

        aBestands.addActionListener(e -> {
            handleSteuerAbschluss(frame);
            bookAbschluss(Enums.AccountType.AKTIV, Enums.AccountType.PASSIV);
        });

        aErfolgs.addActionListener(e -> bookAbschluss(Enums.AccountType.AUFWAND, Enums.AccountType.ERTRAG));

        sort.addActionListener(e -> handleSorting());
        filterButton.addActionListener(e -> handleFilter());
    }


    private void sortTable(int columnIndex, boolean ascending) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        sorter.setComparator(columnIndex, (Comparator<String>) (s1, s2) -> {
            double n1 = HelpFunctions.unFormat(s1);
            double n2 = HelpFunctions.unFormat(s2);
            return Double.compare(n1, n2);
        });
        table.setRowSorter(sorter);
        ArrayList sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(columnIndex, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    private void filterTable(int columnIndex, boolean showAll) {
        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
            @Override
            public boolean include(Entry<?, ?> entry) {
                if (showAll) return true;
                String value = entry.getStringValue(columnIndex);
                return value.toLowerCase().contains(filterTextField.getText().toLowerCase().trim());
            }
        };

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        sorter.setRowFilter(filter);
        table.setRowSorter(sorter);
    }

    private void handleSorting() {
        int index = sort.getSelectedIndex();

        switch (index) {
            case 0 -> sortTable(0, true);
            case 1 -> sortTable(0, false);
            case 2 -> sortTable(3, true);
            case 3 -> sortTable(3, false);
        }
    }

    private void handleFilter() {
        int index = filter.getSelectedIndex();

        switch (index) {
            case 9 -> filterTable(0, true);
            case 1 -> filterTable(1, false);
            case 2 -> filterTable(3, false);
            case 3 -> filterTable(2, false);
        }
    }

    public void bookAbschluss(Enums.AccountType type1, Enums.AccountType type2) {
        double amount;
        BookingRecord bookingRecord;

        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetAccounts().entrySet()) {
            Account account = accountEntry.getValue();

            if (!AccountUtils.CheckAdded(account, alreadyAdded) && account.getAccountBalanceValue() != 0) {
                amount = account.getAccountBalanceValue();
                if (type1.equals(Enums.AccountType.AKTIV) && type2.equals(Enums.AccountType.PASSIV)) {
                    if (AccountUtils.CheckAccountType(account, Enums.AccountType.AKTIV)) {
                        bookingRecord = new BookingRecord(Date.getEndYearDate(), "Abschluss Aktivkonto", AccountUtils.GetAccounts().get("801"), account, amount, amount, 0);
                        alreadyAdded.add(account);
                        BookingRecordUtils.CreateBookingRecord(tableModel, bookingRecord);
                    } else if (AccountUtils.CheckAccountType(account, Enums.AccountType.PASSIV)) {
                        bookingRecord = new BookingRecord(Date.getEndYearDate(), "Abschluss Passivkonto", account, AccountUtils.GetAccounts().get("801"), amount, amount, 0);
                        alreadyAdded.add(account);
                        BookingRecordUtils.CreateBookingRecord(tableModel, bookingRecord);
                    }
                } else {
                    if (AccountUtils.CheckAccountType(account, Enums.AccountType.AUFWAND)) {
                        bookingRecord = new BookingRecord(Date.getTodayDate(), "Abschluss Aufwandskonto", AccountUtils.GetAccounts().get("802"), account, amount, amount, 0);
                        alreadyAdded.add(account);
                        BookingRecordUtils.CreateBookingRecord(tableModel, bookingRecord);
                    } else if (AccountUtils.CheckAccountType(account, Enums.AccountType.ERTRAG)) {
                        bookingRecord = new BookingRecord(Date.getEndYearDate(), "Abschluss Ertragskonto", account, AccountUtils.GetAccounts().get("802"), amount, amount, 0);
                        alreadyAdded.add(account);
                        BookingRecordUtils.CreateBookingRecord(tableModel, bookingRecord);
                    }
                }
            }
        }
    }

    private void handleSteuerAbschluss(ProgramFrame frame) {
        Account accountUst = AccountUtils.GetAccounts().get("480");
        Account accountVst = AccountUtils.GetAccounts().get("260");

        double accountUstValue = accountUst.getAccountBalanceValue();
        double accountVstValue = accountVst.getAccountBalanceValue();

        if (accountUstValue == 0 && accountVstValue == 0) return;
        BookingRecord bookingRecordTax = new BookingRecord(Date.getEndYearDate(), "Abschluss Vorsteuer", accountVst, accountUst, accountVst.getAccountBalanceValue(), accountVst.getAccountBalanceValue(), 0);
        BookingRecordUtils.CreateBookingRecord(frame.getBookingRecordScreen().getTableModel(), bookingRecordTax);

        if (accountVst.getAccountBalanceValue() > accountUst.getAccountBalanceValue()) {
            accountVstValue -= accountUstValue;
            BookingRecord bookingRecordAbschluss = new BookingRecord(Date.getEndYearDate(), "Aktivierung der Vst Zahllast", AccountUtils.GetAccounts().get("801"), accountVst, accountVstValue, accountVstValue, 0);
            BookingRecordUtils.CreateBookingRecord(frame.getBookingRecordScreen().getTableModel(), bookingRecordAbschluss);
            return;
        }

        accountUstValue -= accountVstValue;
        BookingRecord bookingRecordAbschluss = new BookingRecord(Date.getEndYearDate(), "Passivierung der Ust Zahllast", accountUst, AccountUtils.GetAccounts().get("801"), accountUstValue, accountUstValue, 0);
        BookingRecordUtils.CreateBookingRecord(frame.getBookingRecordScreen().getTableModel(), bookingRecordAbschluss);
    }

    public void createTable() {
        Font boldFont = new Font(table.getTableHeader().getFont().getName(), Font.BOLD, table.getTableHeader().getFont().getSize());
        DefaultTableCellRenderer textCenterRenderer = new DefaultTableCellRenderer();
        TableColumnModel columns = table.getColumnModel();

        textCenterRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setModel(tableModel);

        for (BookingRecord bookingRecord : BookingRecordUtils.GetBookingRecords()) {
            BookingRecordUtils.CreateBookingRecord(tableModel, bookingRecord);
        }

        columns.getColumn(0).setCellRenderer(textCenterRenderer);
        columns.getColumn(1).setCellRenderer(textCenterRenderer);
        columns.getColumn(2).setPreferredWidth(250);
        columns.getColumn(3).setCellRenderer(textCenterRenderer);

        table.setRowHeight((table.getRowHeight() * 3) + 20);

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(boldFont);
        table.getTableHeader().setBackground(new Color(174, 238, 238));
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JPanel getPanel() {
        return panel;
    }

}
