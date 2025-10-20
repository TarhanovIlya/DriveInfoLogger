import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class DriveInfoBoxDataExtractor {

    private DriveInfoBoxDataExtractor() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static LocalDateTime fetchDepartureTime(Element element) {
        element = getInfoContainer(element);

        Element timeElement = element
                .child(0)
                .child(0)
                .child(0)
                .child(0);

        Element dateElement = null;
        try {
            dateElement = element
                    .child(0)
                    .child(0)
                    .child(0)
                    .child(1);
        } catch (Exception ignored) {
        }

        String timeText = timeElement.text().trim();
        LocalTime time = LocalTime.parse(timeText);

        LocalDate date;
        if (dateElement != null && !dateElement.text().isEmpty()) {
            date = parseDateWithoutYear(dateElement.text().trim());
        } else {
            date = LocalDate.now();
        }

        return LocalDateTime.of(date, time);
    }

    public static LocalDateTime fetchArrivalTime(Element element, LocalDateTime departureDateTime) {
        element = getInfoContainer(element);

        Element timeElement = element
                .child(1)
                .child(0)
                .child(0)
                .child(0);

        LocalTime time = LocalTime.parse(timeElement.text().trim());

        Element dateElement = null;
        try {
            dateElement = element
                    .child(1)
                    .child(0)
                    .child(0)
                    .child(1);
        } catch (Exception ignored) { }

        LocalDate date;

        if (dateElement != null && !dateElement.text().isEmpty()) {
            date = parseDateWithoutYear(dateElement.text().trim());
        } else {
            date = departureDateTime.toLocalDate();

            if (time.isBefore(departureDateTime.toLocalTime())) {
                date = date.plusDays(1);
            }
        }

        return LocalDateTime.of(date, time);
    }

    //функция parseDateWithoutYear может дать неверные данные в конце текущего года / начале следующего
    private static LocalDate parseDateWithoutYear(String text) {
        String[] parts = text.split(",");
        String datePart = parts[0].trim();

        int year = LocalDate.now().getYear();

        DateTimeFormatter ruFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("ru"));
        DateTimeFormatter enFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

        try {
            return LocalDate.parse(datePart + " " + year, ruFormatter);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(datePart + " " + year, enFormatter);
            } catch (DateTimeParseException e2) {
                throw new RuntimeException("Не удалось распарсить дату: " + text);
            }
        }
    }


    public static LocalDateTime getDateTime(LocalDate date, LocalTime time){
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        return dateTime;
    }

    public static String fetchDeparturePlace(Element element){
       element = getInfoContainer(element);

        element = element
                    .child(0)
                    .child(0)
                    .child(1);



        if (element.text().isEmpty()) throw new RuntimeException("Empty departure place");

        return element.text();
    }

    public static String fetchArrivalPlace(Element element){
        element = getInfoContainer(element);

        element = element
                .child(1)
                .child(0)
                .child(1);


        if (element.text().isEmpty()) throw new RuntimeException("Empty arrival place");

        return element.text();
    }

    public static String fetchAvailableSeats(Element element){

        element = getInfoContainer(element);

        Element seatsElement;
        try {
            seatsElement = element
                    .child(2)
                    .child(0)
                    .child(3);
        }
        catch (Exception e){
            try {
                seatsElement = element
                        .child(3)
                        .child(0);
            }
            catch (Exception e1){
                throw new RuntimeException("seats not found in element");
            }
        }


        String seatsText = seatsElement.text();

        if(seatsText.toLowerCase().equals("нет мест") ) return "0";
        if(seatsText.toLowerCase().equals("последнее место")) return "1";
        if(seatsText.toLowerCase().equals("свободно 5+ мест")) return "5+";
        if(seatsText.toLowerCase().contains("свободно")){
            return seatsText.substring(8, 10).trim();
        }
        else throw new RuntimeException("Unknown seats number string");
    }

    public static String fetchCost(Element element){
        element = getInfoContainer(element);

        //TODO добавить нормальную обработку отсутствия цены
        try {
            element = element
                    .child(2)
                    .child(0)
                    .child(0)
                    .child(0);
        }catch (Exception e){
            return "";
        }

        return element.text();
    }

    public static LocalDate fetchDate(String url){
        int index = url.indexOf("date");
        String date1 = url.substring(index+5,index+15);
        String[] dateContent = new String[3];
        dateContent = date1.split("-");




        LocalDate date = LocalDate.of(Integer.parseInt(dateContent[0]), Integer.parseInt(dateContent[1]), Integer.parseInt(dateContent[2]));
        return  date;
    }

    public static boolean isBigBus(Element element) {
        Elements busTitleElements = element.select("> div > div > span:contains(Большой автобус)");
        if (!busTitleElements.isEmpty()) {
            return true;
        }

        return false;
    }

    private static Element getInfoContainer(Element element){
        Element container = element.selectFirst("div.MuiGrid-root.MuiGrid-container");
        return container;
    }

    public static DriveDataSnapshot getDriveDataSnapshot(String pageURL, Element driveElement){
        DriveDataSnapshot snapshot = new DriveDataSnapshot(
                fetchDeparturePlace(driveElement),
                fetchArrivalPlace(driveElement),
                LocalDateTime.now(),
                fetchDepartureTime(driveElement),
                fetchArrivalTime(driveElement, fetchDepartureTime(driveElement)),
                fetchAvailableSeats(driveElement),
                fetchCost(driveElement),
                new String(isBigBus(driveElement)? "not r.t.":"route taxi")
        );

        return snapshot;
    }

}
