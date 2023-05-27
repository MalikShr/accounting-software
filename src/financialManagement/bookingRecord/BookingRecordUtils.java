package financialManagement.bookingRecord;

import utils.Date;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class BookingRecordUtils {
    private static ArrayList<BookingRecord> bookingRecords = new ArrayList<BookingRecord>();
    private static ArrayList<BookingRecord> bookingRecordsWithAbschluss = new ArrayList<BookingRecord>();

    public static void CreateBookingRecord(DefaultTableModel tableModel, BookingRecord bookingRecord) {
        bookingRecordsWithAbschluss.add(bookingRecord);
        String bookingNumber = Date.BookingNr + "";
        String date = bookingRecord.getFormattedDate();
        String text = bookingRecord.getBookingText();
        String amount = bookingRecord.getFormattedBookingAmount();

        Object[] data = {bookingNumber, date, text, amount};
        tableModel.addRow(data);

        ++Date.BookingNr;
    }

    public static ArrayList<BookingRecord> GetBookingRecords() {
        return bookingRecords;
    }

    public static ArrayList<BookingRecord> GetBookingRecordsWithAbschluss() {
        return bookingRecordsWithAbschluss;
    }

    public static void SetBookingRecords(ArrayList<BookingRecord> newBookingRecords) {
        bookingRecords = newBookingRecords;
    }

}
