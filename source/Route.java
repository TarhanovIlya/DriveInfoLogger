import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Route {
    public final String from;
    public final String to;
    public final String date;


    public Route(String from, String to, String date) {
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Route(String[] departureArrival, LocalDateTime date){

        this.from = departureArrival[0];
        this.to = departureArrival[1];
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
