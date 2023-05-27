package components;

import main.ProgramFrame;

import javax.swing.*;

public class Info {
    public Info(ProgramFrame frame) {
        JDialog d = new JDialog(frame, "components.Info");

        JLabel l = new JLabel(utils.Constants.Version);
        l.setHorizontalTextPosition(SwingConstants.CENTER);
        l.setHorizontalAlignment(SwingConstants.CENTER);

        d.add(l);
        d.setSize(200, 100);
        d.setLocation(500, 500);

        d.setVisible(true);
    }
}
