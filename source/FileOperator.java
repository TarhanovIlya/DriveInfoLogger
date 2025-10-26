import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class FileOperator implements AutoCloseable {

    private FileOutputStream csvOutputStream;
    private FileOutputStream errorLogOutputStream;
    private FileOutputStream sessionLogStream;
    private FileInputStream departureArrivalStream;

    private String dateTimeFormat = "yyyy-MM-dd HH:mm";
    private DateTimeFormatter dateTimeFormatter;

    private String headerLine = "departure_place,arrival_place,seats,snapshot_creation_time,departure_time,arrival_time,cost,car_type\n";


    FileOperator(String csvLogFileURL, String errorLogFileURL, String sessionLogFileURL, String departureArrivalFileURL){
        try {
            errorLogOutputStream = new FileOutputStream(errorLogFileURL, true);
            sessionLogStream = new FileOutputStream(sessionLogFileURL, true);


            csvOutputStream = new FileOutputStream(csvLogFileURL, true);
            tryInitializingLogFile(csvLogFileURL);



            departureArrivalStream = new FileInputStream(departureArrivalFileURL);

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
        line.append(snapshot.departureTime.format(dateTimeFormatter)).append(",");
        line.append(snapshot.arrivalTime.format(dateTimeFormatter)).append(",");
        line.append(snapshot.cost).append(",");
        line.append(snapshot.carType).append("\n");

        csvOutputStream.write(line.toString().getBytes(StandardCharsets.UTF_8));
        GlobalVariables.driveDataSnapshotsWritten += 1;
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

    public void writeSessionLog() throws IOException {
        StringBuilder sessionLog = new StringBuilder();
        sessionLog.append("Session started: ").append(GlobalVariables.sessionStart.format(dateTimeFormatter)).append("\n");
        sessionLog.append("Session ended:   ").append(LocalDateTime.now().format(dateTimeFormatter)).append("\n");
        sessionLog.append("Session time:    ").append(ChronoUnit.MINUTES.between(GlobalVariables.sessionStart, LocalDateTime.now())).append(" minutes").append("\n");
        sessionLog.append("    driveElements read:         ").append(GlobalVariables.driveElementsRead).append("\n");
        sessionLog.append("    driveDataSnapshots written: ").append(GlobalVariables.driveDataSnapshotsWritten).append("\n");
        sessionLog.append("    drive elements extraction errors: ").append(GlobalVariables.extractionErrors).append("\n");
        sessionLog.append("    drive elements data fetch errors: ").append(GlobalVariables.fetchErrors).append("\n");
        sessionLog.append("\n");
        sessionLog.append("\n");
        sessionLog.append("\n");
        sessionLog.append("\n");
        sessionLog.append("\n");



        sessionLogStream.write(sessionLog.toString().getBytes(StandardCharsets.UTF_8));
    }

    public List<String[]> readDepartureArrival() throws IOException {
        String[] lines = new String(departureArrivalStream.readAllBytes()).split("\n");

        List<String[]> pairs = new ArrayList<>();

        for (String line : lines
             ) {
            String[] pair = line.trim().split("->");
            pairs.add(pair);
        }

        return  pairs;
    }

    @Override
    public void close() throws Exception {
        csvOutputStream.close();
        errorLogOutputStream.close();
        sessionLogStream.close();
        departureArrivalStream.close();
    }
}
