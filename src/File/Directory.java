package File;

import java.io.File;

public class Directory {
    private static String path;
    private static File pathFile = new File("");

    public static String GetPath() {
        return path;
    }

    public static void SetPath(String newPath) {
        path = newPath;
        pathFile = new File(newPath);
    }

    public static File GetPathFile() {
        return pathFile;
    }

    public static File GetAccountsFile() {
        return new File(path + "/data/bookingRecords/BookingRecordUtilz.dat");
    }

    public static File GetBookingRecordsFile() {
        return new File(path + "/data/accounts/accounts.dat");
    }

    public static void CreateFile(String path) {
        new File(path + "/data/accounts").mkdirs();
        new File(path + "/data/bookingRecords").mkdir();
    }
}
