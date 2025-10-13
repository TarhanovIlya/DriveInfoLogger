import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileOperator implements AutoCloseable {

    private FileOutputStream csvOutputStream;
    private FileOutputStream errorLogOutputStream;

    private String dateTimeFormat = "yyyy-MM-dd HH:mm";
    private DateTimeFormatter dateTimeFormatter;

    private String headerLine = "Departure,Arrival,Seats,SnapshotCreationTime,DriveDateTime,Cost\n";


    FileOperator(String csvLogFileURL, String errorLogFileURL){
        try {
            errorLogOutputStream = new FileOutputStream(errorLogFileURL, true);

            csvOutputStream = new FileOutputStream(csvLogFileURL, true);
            tryInitializingLogFile(csvLogFileURL);



            dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void tryInitializingLogFile(String logFileURL) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(logFileURL);
        if (new String(fileInputStream.readAllBytes()).isEmpty()){
            csvOutputStream.write(headerLine.getBytes(StandardCharsets.UTF_8));
        }
        fileInputStream.close();
    }

    public void writeDriveLine_CSV(DriveDataSnapshot snapshot) throws IOException {


        StringBuilder line = new StringBuilder("");

        line.append(snapshot.from).append(",");
        line.append(snapshot.to).append(",");
        line.append(snapshot.availableSeatsAsString).append(",");
        line.append(snapshot.creationDateTime.format(dateTimeFormatter)).append(",");
        line.append(snapshot.driveDateTime.format(dateTimeFormatter)).append(",");
        line.append(snapshot.cost).append("\n");

        csvOutputStream.write(line.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void writeErrorLog(String errorMessage) throws IOException {

        StringBuilder logMessage = new StringBuilder();

        logMessage.append("Date, time = ").append(LocalDateTime.now().format(dateTimeFormatter)).append("\n");
        logMessage.append("error description:").append("\n");
        logMessage.append(errorMessage);
        logMessage.append("\n");
        logMessage.append("\n");
        logMessage.append("\n");
        logMessage.append("\n");
        logMessage.append("\n");

        errorLogOutputStream.write(logMessage.toString().getBytes(StandardCharsets.UTF_8));

    }


    @Override
    public void close() throws Exception {
        csvOutputStream.close();
        errorLogOutputStream.close();
    }
}
