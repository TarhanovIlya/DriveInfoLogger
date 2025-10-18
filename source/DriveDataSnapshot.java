import java.time.LocalDateTime;

public class DriveDataSnapshot {

    public final String from;
    public final String to;

    public final LocalDateTime creationDateTime;
    public final LocalDateTime departureTime;
    public final LocalDateTime arrivalTime;

    public final String availableSeatsAsString;
    public final String cost;
    public final String carType;


    DriveDataSnapshot(String from, String to, LocalDateTime creationDateTime, LocalDateTime departureTime, LocalDateTime arrivalTime, String availableSeatsAsString, String cost, String carType){
        this.from = from;
        this.to = to;

        this.creationDateTime = creationDateTime;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;


        this.availableSeatsAsString = availableSeatsAsString;
        this.cost = cost;
        this.carType = carType;
    }
}
