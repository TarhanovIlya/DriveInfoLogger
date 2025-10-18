import org.jsoup.nodes.Element;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MyScripts {

    public SiteSnapshotExtractor snapshotExtractor = null;
    private FileOperator writer = null;



    public void downloadSnapshot(Route route) throws InterruptedException, IOException {
        snapshotExtractor.getToDesiredRouteWebPage(route);

        System.out.println(snapshotExtractor.getCurrentURL());

            List<Element> drives = snapshotExtractor.extractDriveElements(snapshotExtractor.changeUrlDate(route.date));


            for (Element el: drives
                 ) {
                writer.writeDriveLine_CSV(DriveInfoBoxDataExtractor.getDriveDataSnapshot(snapshotExtractor.changeUrlDate(route.date),el));
            }

    }

    List<Route> createRoutes() throws IOException, InterruptedException {
        List<String[]> pairs = writer.readDepartureArrival();

        List<Route> routes = new ArrayList<>();

        for (String[] pair:pairs
             ) {
            LocalDateTime date = LocalDateTime.now();
            snapshotExtractor.getToDesiredRouteWebPage(new Route(pair, date));

            // if current day does not have any tickets left
            if(snapshotExtractor.noTicketsPage(snapshotExtractor.changeUrlDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))){
                date = date.plusDays(1);
            }

            while (!snapshotExtractor.noTicketsPage(snapshotExtractor.changeUrlDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))){
                routes.add(new Route(pair, date));
                date = date.plusDays(1);
            }
        }


        return routes;
    }





    public void setSnapshotExtractor(SiteSnapshotExtractor snapshotExtractor) {
        this.snapshotExtractor = snapshotExtractor;
    }

    public void setWriter(FileOperator writer) {
        this.writer = writer;
    }
}
