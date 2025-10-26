import java.time.LocalDateTime;

public class GlobalVariables {
    static LocalDateTime sessionStart = LocalDateTime.now();
    static int driveElementsRead = 0;
    static int driveDataSnapshotsWritten = 0;
    static int extractionErrors = 0;
    static int fetchErrors = 0;

    static int maxDaysDifference = 30; // максимальная разница между сегодняшней датой и датой сбора информации и поездках


    static String csvLogURL = "C:\\Users\\r1r2\\Desktop\\java_code\\FreeBusSeatsLogger\\data\\data.txt";
    static String errorLogURL = "C:\\Users\\r1r2\\Desktop\\java_code\\FreeBusSeatsLogger\\data\\errorLog.txt";
    static String sessionLogURL = "C:\\Users\\r1r2\\Desktop\\java_code\\FreeBusSeatsLogger\\data\\sessionLog.txt";
    static String departureArrivalURL = "C:\\Users\\r1r2\\Desktop\\java_code\\FreeBusSeatsLogger\\data\\departure-arrival places.txt";
}
