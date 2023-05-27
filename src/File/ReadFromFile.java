package File;

import financialManagement.account.Account;
import financialManagement.account.AccountUtils;
import financialManagement.bookingRecord.BookingRecord;
import financialManagement.bookingRecord.BookingRecordUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.TreeMap;

public class ReadFromFile {
    public static void GetDataFromFile() throws IOException, ClassNotFoundException {
        if (Directory.GetAccountsFile().isFile() && Directory.GetAccountsFile().length() > 0) {
            HandleAccounts(new FileInputStream(Directory.GetAccountsFile()));
        }
        if (Directory.GetBookingRecordsFile().isFile() && Directory.GetBookingRecordsFile().length() > 0) {
            HandleBookingRecords(new FileInputStream(Directory.GetBookingRecordsFile()));
        }
    }

    private static void HandleAccounts(InputStream isn) throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(isn);
        AccountUtils.SetAccounts((TreeMap<String, Account>) is.readObject());
        is.close();
    }

    private static void HandleBookingRecords(InputStream isn) throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(isn);
        BookingRecordUtils.SetBookingRecords((ArrayList<BookingRecord>) is.readObject());
        is.close();
    }

}
