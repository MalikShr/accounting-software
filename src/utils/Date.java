package utils;

public class Date {
    private static final java.util.Date date = new java.util.Date();
    public static int BookingNr = 1;
    private static String today = formatDate(date.getDate()) + (".") + formatDate(date.getMonth() + 1) + (".") + (date.getYear() + 1900);

    private static String formatDate(int format) {
        if (format <= 9) {
            return "0" + format;
        }
        return format + "";
    }

    public static String getTodayDate() {
        return today;
    }

    public static String getBeginYearDate() {
        return "01.01." + (date.getYear() + 1900);
    }

    public static String getEndYearDate() {
        return "31.12." + (date.getYear() + 1900);
    }
}
