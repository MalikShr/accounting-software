package utils;

import File.Directory;
import File.FileExplorer;
import File.ReadFromFile;
import financialManagement.account.AccountUtils;
import financialManagement.bookingRecord.BookingRecord;
import financialManagement.bookingRecord.BookingRecordUtils;
import main.ProgramFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectUtils {
    public static void NavigateNewProject(ProgramFrame frame) {
        AccountUtils.LoadAccountList();
        frame.getNewProject().resetNames();
        frame.getNewProject().setOpeningBalance();
        HelpFunctions.UpdateAllPanels(frame);
        frame.setContentPane(frame.navigateNewProject());
    }

    public static void CreateNewProject(ProgramFrame frame, String path) throws IOException, ClassNotFoundException {
        Directory.CreateFile(path);
        Directory.SetPath(path);

        JOptionPane.showMessageDialog(null, "Dein neues Projekt wurde erfolgreich erstellt!");

        frame.getSideBar().setContent(0);
        frame.setFrameTitle("Buchführungsprogramm " + " [" + Directory.GetPathFile().getName() + "]");
        frame.getBooking().createList();
        HelpFunctions.UpdateAllPanels(frame);
        HelpFunctions.Navigate(frame, frame.navigateBookingPanel());
    }

    public static void OpenProject(ProgramFrame frame) {
        try {
            int selection = JFileChooser.FILES_AND_DIRECTORIES;
            FileNameExtensionFilter filter = null;

            Directory.SetPath(FileExplorer.getFileExplorerPath("Projekt öffnen", selection, filter).getAbsolutePath());
            ReadFromFile.GetDataFromFile();

            ArrayList<BookingRecord> bookingRecords = new ArrayList<BookingRecord>(BookingRecordUtils.GetBookingRecords());

            Date.BookingNr = 1;
            HelpFunctions.ClearTable(frame.getBookingRecordScreen().getTableModel());

            for (BookingRecord bookingRecord : bookingRecords) {
                BookingRecordUtils.CreateBookingRecord(frame.getBookingRecordScreen().getTableModel(), bookingRecord);
            }

            File accounts = new File(Directory.GetPath() + "/data/accounts");
            File bookings = new File(Directory.GetPath() + "/data/bookingRecords");

            if (accounts.exists() && bookings.exists()) {
                JOptionPane.showMessageDialog(null, "Erfolgreich importiert");
                frame.getSideBar().setContent(0);
                frame.setFrameTitle("Buchführungsprogramm " + " [" + Directory.GetPathFile().getName() + "]");
                HelpFunctions.Navigate(frame, frame.navigateBookingPanel());
            } else {
                JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HelpFunctions.UpdateAllPanels(frame);
    }
}
