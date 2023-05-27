package screens;

import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import financialManagement.kennzahl.GuvKennzahlen;
import financialManagement.statements.GuvSum;
import main.ProgramFrame;
import main.ProgramPanel;
import utils.HelpFunctions;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Guv extends ProgramPanel {
    private JTabbedPane pane;
    private JTextField account50Text;
    private JTextField account616Text;
    private JTextField account540Text;
    private JTextField account64Text;
    private JTextField account56Text;
    private JTextField account670Text;
    private JTextField account571Text;
    private JTextField account65Text;
    private JTextField account70Text;
    private JTextField account75Text;
    private JTextField account542Text;
    private JTextField account677Text;
    private JTextField account68Text;
    private JTextField habenText;
    private JTextField sollText;
    private JTextField account60Text;
    private JTextField account62Text;
    private JPanel panel;
    private JTextField verlustText;
    private JTextField gewinnText;
    private JLabel l50;
    private JLabel l540;
    private JLabel l542;
    private JLabel l56;
    private JLabel l571;
    private JLabel lVerlust;
    private JLabel l60;
    private JLabel l616;
    private JLabel l62;
    private JLabel l64;
    private JLabel l65;
    private JLabel l670;
    private JLabel l677;
    private JLabel l68;
    private JLabel l70;
    private JLabel l75;
    private JLabel lGewinn;
    private JTextField annualProfitText;
    private JLabel ckText;
    private JTextField skText;
    private JTextField pkText;
    private JTextField kkText;
    private JTextField uEkText;
    private JTextField uGkText;
    private JTextField uFordText;
    private JTextField annualKreditText;
    private JTextField mkText;

    public Guv(ProgramFrame frame) {
        setBalance();
        verlustText.setForeground(Color.RED);
    }

    public void setBalance() {
        JTextField[] accountTextFields = {account50Text, account540Text, account542Text, account56Text, account571Text,
                account60Text, account616Text, account62Text, account64Text, account65Text, account670Text,
                account677Text, account68Text, account70Text, account75Text};
        JTextField[] kennzahlenTextFields = {annualProfitText, pkText, kkText, skText, mkText, uEkText, uGkText, uFordText, annualKreditText};
        JLabel[] labels = {l50, l540, l542, l56, l571, l60, l616, l62, l64, l65, l670, l677, l68, l70, l75};

        int i = 0;
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetErfolgAccounts().entrySet()) {
            accountTextFields[i].setText(accountEntry.getValue().getFormattedAccountBalance());
            accountTextFields[i].setText(accountEntry.getValue().getFormattedAccountBalance());
            manageVisible(accountTextFields[i], labels[i]);
            ++i;
        }

        for (int j = 0; j < kennzahlenTextFields.length; ++j) {
            kennzahlenTextFields[j].setText(GuvKennzahlen.getGuvKennzahlen().get(j).getFormattedAmount());
        }

        sollText.setText(HelpFunctions.Format(GuvSum.getIncomeSum() + GuvSum.calculateLoose()));
        habenText.setText(HelpFunctions.Format(GuvSum.getExpensesSum() + GuvSum.calculateWin()));
        gewinnText.setText(HelpFunctions.Format(GuvSum.calculateWin()));
        verlustText.setText(HelpFunctions.Format(GuvSum.calculateLoose()));
        manageVisible(gewinnText, lGewinn);
        manageVisible(verlustText, lVerlust);

        annualProfitText.setForeground(GuvKennzahlen.getAnnualProfit() < 0 ? Color.RED : Color.BLACK);
    }

    public void manageVisible(JTextField textField, JLabel label) {
        try {
            double amount = HelpFunctions.unFormat(textField.getText());

            if (amount == 0) {
                label.setVisible(false);
                textField.setVisible(false);
            } else {
                label.setVisible(true);
                textField.setVisible(true);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public JPanel getPanel() {
        return panel;
    }

}
