package main;

import components.MenuBar;
import components.ToolBar;
import screens.*;
import utils.Constants;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;

public class ProgramFrame extends JFrame {
    private DashboardScreen dashboard;
    private BookingScreen booking;
    private AccountsScreen allAccounts;
    private BookingRecordsScreen bookingRecordScreen;
    private Bilanz bilanz;
    private Guv guv;
    private JPanel mainPanel = new JPanel();
    private ToolBar toolBar;
    private MenuBar menuBar;
    private NewProjectScreen newProject;

    public ProgramFrame() throws IOException, ClassNotFoundException {
        dashboard = new DashboardScreen(this);
        mainPanel.setLayout(new BorderLayout());

        toolBar = new ToolBar(this);
        menuBar = new MenuBar(this);
        bookingRecordScreen = new BookingRecordsScreen(this);
        booking = new BookingScreen(this);
        allAccounts = new AccountsScreen(this);
        bilanz = new Bilanz(this);
        guv = new Guv(this);
        newProject = new NewProjectScreen(this);

        try {
            String plaf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(plaf);
            SwingUtilities.updateComponentTreeUI(this);

        } catch (UnsupportedLookAndFeelException ue) {
            System.err.println(ue.toString());
        } catch (ClassNotFoundException ce) {
            System.err.println(ce.toString());
        } catch (InstantiationException ie) {
            System.err.println(ie.toString());
        } catch (IllegalAccessException iae) {
            System.err.println(iae.toString());
        }

        setTitle("Buchführungsprogramm");
        setContentPane(navigateDashboardPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setIconImage(Constants.logo);
        pack();
    }

    public JPanel getBilanzPanel() {
        return bilanz.getPanel();
    }

    public JPanel getGuvPanel() {
        return guv.getPanel();
    }

    public JPanel getAllAccountsPanel() {
        return allAccounts.getPanel();
    }


    public JPanel navigateDashboardPanel() {
        mainPanel.removeAll();
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(dashboard.getPanel());
        return mainPanel;
    }

    public JPanel navigateNewProject() {
        mainPanel.removeAll();
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(newProject.getPanel());
        return mainPanel;
    }

    public JPanel navigateBookingPanel() {
        return handleNavigation(booking);
    }

    public JPanel navigateBilanzPanel() {
        return handleNavigation(bilanz);
    }

    public JPanel navigateGuvPanel() {
        return handleNavigation(guv);
    }

    public JPanel navigateAllAccountsPanel() {
        return handleNavigation(allAccounts);
    }

    public JPanel navigateBookingRecordPanel() {
        return handleNavigation(bookingRecordScreen);
    }

    public JPanel handleNavigation(ProgramPanel screen) {
        mainPanel.removeAll();
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(menuBar, BorderLayout.WEST);
        mainPanel.add(screen.getPanel());
        return mainPanel;
    }

    public MenuBar getSideBar() {
        return menuBar;
    }

    public AccountsScreen getAllAccounts() {
        return allAccounts;
    }

    public BookingRecordsScreen getBookingRecordScreen() {
        return bookingRecordScreen;
    }

    public Bilanz getBilanz() {
        return bilanz;
    }

    public Guv getGuv() {
        return guv;
    }

    public NewProjectScreen getNewProject() {
        return newProject;
    }

    public BookingScreen getBooking() {
        return booking;
    }

    public void setFrameTitle(String title) {
        setTitle(title);
    }
}