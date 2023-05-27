package screens;

import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import main.ProgramFrame;
import main.ProgramPanel;
import utils.Date;
import utils.HelpFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Map;

public class AccountsScreen extends ProgramPanel {
    private JPanel panel;
    private JTable table;
    private JScrollPane pane;
    private JLabel header;
    private DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Konto", "Bezeichnung", "Art", "Konto Steuer", "AB (in €)", "Saldo (in €)"}, 0);

    public AccountsScreen(ProgramFrame frame) {
        setBalance(frame);
        header.setText("Kontenübersicht zum " + Date.getTodayDate());
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pane.getViewport().setBackground(new Color(235, 248, 255));
    }

    public void setBalance(ProgramFrame frame) {
        HelpFunctions.ClearTable(tableModel);
        createTable();
    }

    public void createTable() {
        Font boldFont = new Font(table.getTableHeader().getFont().getName(), Font.BOLD, table.getTableHeader().getFont().getSize());
        DefaultTableCellRenderer textCenterRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer textRightRenderer = new DefaultTableCellRenderer();
        TableColumnModel columns = table.getColumnModel();

        textCenterRenderer.setHorizontalAlignment(JLabel.CENTER);
        textRightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        table.setModel(tableModel);

        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetAccounts().entrySet()) {
            Object[] data = AccountUtils.setTextFieldAccountValues(accountEntry.getKey(), accountEntry.getValue());
            tableModel.addRow(data);
        }

        columns.getColumn(1).setPreferredWidth(250);
        columns.getColumn(2).setCellRenderer(textCenterRenderer);
        columns.getColumn(3).setCellRenderer(textCenterRenderer);
        columns.getColumn(4).setCellRenderer(textRightRenderer);
        columns.getColumn(5).setCellRenderer(textRightRenderer);

        table.setRowHeight((table.getRowHeight()) + 10);
        table.setShowHorizontalLines(true);

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(boldFont);
        table.getTableHeader().setBackground(new Color(174, 238, 238));
    }

    public JPanel getPanel() {
        return panel;
    }

}
