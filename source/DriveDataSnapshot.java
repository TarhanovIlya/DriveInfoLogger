import org.jsoup.nodes.Element;

import java.time.LocalDateTime;

public class DriveDataSnapshot {

    public final String from;
    public final String to;

    public final LocalDateTime creationTime;
    public final LocalDateTime driveDateTime;

    public final Integer availableSeats;


    DriveDataSnapshot(String from, String to, LocalDateTime creationTime, LocalDateTime driveDateTime, Integer availableSeats){
        this.from = from;
        this.to = to;

        this.creationTime = creationTime;
        this.driveDateTime = driveDateTime;


        this.availableSeats = availableSeats;
    }
}
