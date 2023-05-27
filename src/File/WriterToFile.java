package File;

import financialManagement.account.AccountUtils;
import financialManagement.bookingRecord.BookingRecordUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriterToFile {

    public static void InsertDataToFile() {
        InsertAccountDataToFile();
        InsertBookingRecordsToFile();
    }

    private static void InsertAccountDataToFile() {
        Directory.GetAccountsFile().delete();

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(Directory.GetAccountsFile().getPath()));

            os.writeObject(AccountUtils.GetAccounts());
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void InsertBookingRecordsToFile() {
        Directory.GetBookingRecordsFile().delete();

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(Directory.GetBookingRecordsFile().getPath()));
            os.writeObject(BookingRecordUtils.GetBookingRecords());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

