import java.time.LocalDateTime;

public class DriveDataSnapshot {

    public final String from;
    public final String to;

    public final LocalDateTime creationDateTime;
    public final LocalDateTime driveDateTime;

    public final String availableSeatsAsString;
    public final String cost;


    DriveDataSnapshot(String from, String to, LocalDateTime creationDateTime, LocalDateTime driveDateTime, String availableSeatsAsString, String cost){
        this.from = from;
        this.to = to;

        this.creationDateTime = creationDateTime;
        this.driveDateTime = driveDateTime;


        this.availableSeatsAsString = availableSeatsAsString;
        this.cost = cost;
    }
}
