package screens;

import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import financialManagement.kennzahl.BilanzKennzahlen;
import financialManagement.statements.BilanzSum;
import main.ProgramFrame;
import main.ProgramPanel;
import utils.HelpFunctions;

import javax.swing.*;
import java.util.Map;

public class Bilanz extends ProgramPanel {
    private JPanel panel;
    private JTextField anlageText;
    private JTextField account084Text;
    private JTextField umlaufText;
    private JTextField account240Text;
    private JTextField account280Text;
    private JTextField account288Text;
    private JTextField ekText;
    private JTabbedPane pane;
    private JTextField account300Text;
    private JTextField fkText;
    private JTextField account425Text;
    private JTextField account420Text;
    private JTextField account44Text;
    private JTextField account07Text;
    private JTextField aktivaText;
    private JTextField passivaText;
    private JTextField account3001Text;
    private JTextField account20Text;
    private JTextField account27Text;
    private JTextField account05Text;
    private JTextField ekQuoteText;
    private JTextField fkQuoteText;
    private JTextField vgText;
    private JTextField uiText;
    private JTextField aiTextField;
    private JTextField dg1Text;
    private JTextField dg2Text;
    private JTextField lg1Text;
    private JTextField lg2Text;
    private JTextField lg3Text;
    private JTextField workingCapitalText;

    public Bilanz(ProgramFrame frame) {
        setBalance();
    }

    public void setBalance() {
        JTextField[] accountTextFields = {account05Text, account07Text, account084Text, account20Text, account240Text, account27Text, account280Text, account288Text, account300Text, account3001Text, account420Text, account425Text, account44Text};
        JTextField[] kennzahlenTextFields = {ekQuoteText, fkQuoteText, vgText, uiText, aiTextField, dg1Text, dg2Text, lg1Text, lg2Text, lg3Text, workingCapitalText};

        int i = 0;
        for (Map.Entry<String, Account> accountEntry : AccountUtils.GetBestandAccounts().entrySet()) {
            accountTextFields[i].setText(accountEntry.getValue().getFormattedAccountBalance());
            ++i;
        }

        for (int j = 0; j < kennzahlenTextFields.length; j++) {
            kennzahlenTextFields[j].setText(BilanzKennzahlen.GetBilanzKennzahlen().get(j).getFormattedAmount());
        }

        anlageText.setText(HelpFunctions.Format(BilanzSum.getAnlage()));
        umlaufText.setText(HelpFunctions.Format(BilanzSum.getUmlauf()));
        fkText.setText(HelpFunctions.Format(BilanzSum.getFk()));
        ekText.setText(HelpFunctions.Format(BilanzSum.getEk()));
        account300Text.setText(HelpFunctions.Format(BilanzSum.getEk()));

        aktivaText.setText(HelpFunctions.Format(BilanzSum.getAktivSumme()));
        passivaText.setText(HelpFunctions.Format(BilanzSum.getPassivSumme()));
    }

    public JPanel getPanel() {
        return panel;
    }

}
