package screens;

import financialManagement.account.AccountUtils;
import main.ProgramFrame;
import main.ProgramPanel;
import utils.ProjectUtils;

import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends ProgramPanel {
    private JPanel panel;
    private JButton newProjectButton;
    private JButton openProjectButton;

    public DashboardScreen(ProgramFrame frame) {
        AccountUtils.LoadAccountList();
        newProjectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newProjectButton.setFocusPainted(false);
        newProjectButton.setBorderPainted(false);
        newProjectButton.setContentAreaFilled(false);

        openProjectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openProjectButton.setFocusPainted(false);
        openProjectButton.setBorderPainted(false);
        openProjectButton.setContentAreaFilled(false);

        newProjectButton.addActionListener(e -> {
            ProjectUtils.NavigateNewProject(frame);
        });
        openProjectButton.addActionListener(e -> ProjectUtils.OpenProject(frame));
    }

    public JPanel getPanel() {
        return panel;
    }
}
