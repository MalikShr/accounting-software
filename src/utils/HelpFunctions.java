package utils;

import main.ProgramFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class HelpFunctions {
    public static void UpdateAllPanels(ProgramFrame frame) {
        //Update AccountScreen
        frame.getAllAccounts().setBalance(frame);
        frame.getAllAccountsPanel().revalidate();
        frame.getAllAccountsPanel().repaint();

        //Update Bilanz
        frame.getBilanz().setBalance();
        frame.getBilanzPanel().revalidate();
        frame.getBilanzPanel().repaint();

        //Update GUV
        frame.getGuv().setBalance();
        frame.getGuvPanel().revalidate();
        frame.getGuvPanel().repaint();
    }

    public static String Format(double number) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        DecimalFormat df = (DecimalFormat) nf;

        if (number < 1 && number > -1) {
            df.applyPattern("0.00");
        } else {
            df.applyPattern("###,###.00");
        }
        return df.format(number);
    }

    public static double unFormat(String formattedString) {
        String unFormattedAmount = formattedString.replace(".", "");
        return Double.parseDouble(unFormattedAmount.replaceAll(",", "."));
    }

    public static void Navigate(ProgramFrame frame, JPanel panel) {
        frame.setContentPane(panel);
        frame.validate();
    }

    public static void ClearTable(DefaultTableModel tableModel) {
        tableModel.getDataVector().removeAllElements();
        tableModel.setNumRows(0);
    }
}
