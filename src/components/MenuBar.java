package components;

import main.ProgramFrame;
import utils.Constants;
import utils.HelpFunctions;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JPanel {

    JButton booking = new JButton(" Neuer Buchungssatz");
    JButton bookingRecords = new JButton(" Buchungssätze");
    JButton accounts = new JButton(" Kontenübersicht");
    JButton bilanz = new JButton(" Bilanz");
    JButton guv = new JButton(" GUV");
    ;
    int content = 0;

    JButton[] buttons = {booking, bookingRecords, accounts, bilanz, guv};


    public MenuBar(ProgramFrame frame) {
        handleButtonBackground();
        setBackground(new Color(174, 238, 238));

        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel header = new JLabel("Menü");

        header.setFont(new Font("Dialog", Font.BOLD, 18));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        booking.setIcon(new ImageIcon(Constants.boIcon));
        bookingRecords.setIcon(new ImageIcon(Constants.brIcon));
        accounts.setIcon(new ImageIcon(Constants.aIcon));
        bilanz.setIcon(new ImageIcon(Constants.bIcon));
        guv.setIcon(new ImageIcon(Constants.gIcon));

        for (JButton b : buttons) {
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, b.getMinimumSize().height));
        }

        booking.addActionListener(e -> {
            content = 0;
            handleButtonBackground();
            HelpFunctions.Navigate(frame, frame.navigateBookingPanel());
        });
        bookingRecords.addActionListener(e -> {
            content = 1;
            handleButtonBackground();
            HelpFunctions.Navigate(frame, frame.navigateBookingRecordPanel());
        });
        accounts.addActionListener(e -> {
            content = 2;
            handleButtonBackground();
            HelpFunctions.Navigate(frame, frame.navigateAllAccountsPanel());
        });
        bilanz.addActionListener(e -> {
            content = 3;
            handleButtonBackground();
            HelpFunctions.Navigate(frame, frame.navigateBilanzPanel());
        });
        guv.addActionListener(e -> {
            content = 4;
            handleButtonBackground();
            HelpFunctions.Navigate(frame, frame.navigateGuvPanel());
        });

        add(Box.createRigidArea(new Dimension(0, 30)));
        add(header);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(booking);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(bookingRecords);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(accounts);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(bilanz);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(guv);
    }

    public void handleButtonBackground() {
        int[] contents = {0, 1, 2, 3, 4};

        for (int i = 0; i < contents.length; ++i) {
            if (contents[i] == content) {
                buttons[i].setContentAreaFilled(true);
                buttons[i].setBackground(new Color(231, 255, 255));
            } else {
                buttons[i].setContentAreaFilled(false);
            }
        }
    }

    public void setContent(int content) {
        this.content = content;
        handleButtonBackground();
    }
}
