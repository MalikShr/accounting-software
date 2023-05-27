package components;

import File.ExportPDF;
import File.FileExplorer;
import main.ProgramFrame;
import utils.ProjectUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ToolBar extends JMenuBar {
    public ToolBar(ProgramFrame frame) {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(new Color(121, 205, 205));

        JMenu file = new JMenu("Datei");
        JMenu program = new JMenu("Programm");

        JMenuItem newProject = new JMenuItem("Neues Projekt");
        JMenuItem importProject = new JMenuItem("Projekt öffnen");
        JMenuItem exportProject = new JMenuItem("Als .pdf exportieren");

        JMenuItem info = new JMenuItem("Info");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem end = new JMenuItem("Beenden");

        add(file);
        add(program);

        file.add(newProject);
        file.add(importProject);
        file.add(exportProject);

        program.add(info);
        program.add(refresh);
        program.add(end);


        newProject.addActionListener(e -> {
            ProjectUtils.NavigateNewProject(frame);
        });

        exportProject.addActionListener(e -> {
            try {
                int selection = JFileChooser.FILES_ONLY;
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");

                new ExportPDF(FileExplorer.getFileExplorerPath("Projekt als .pdf exportieren", selection, filter).getAbsolutePath() + ".pdf");
                JOptionPane.showMessageDialog(null, "Dein Projekt wurde erfolgreich exportiert!");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        importProject.addActionListener(e -> {
            ProjectUtils.OpenProject(frame);
        });

        info.addActionListener(e -> new Info(frame));

        refresh.addActionListener(e -> {
            frame.revalidate();
            frame.validate();
        });

        end.addActionListener(e -> System.exit(0));
    }
}
