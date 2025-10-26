import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyScripts {

    public SiteSnapshotExtractor snapshotExtractor = null;
    private FileOperator writer = null;



//    public void downloadSnapshot(Route route) throws InterruptedException, IOException {
//        snapshotExtractor.getToDesiredRouteWebPage(route);
//
//        System.out.println(snapshotExtractor.getCurrentURL());
//        List<Element> drives = new ArrayList<>();
//        try {
//             drives = snapshotExtractor.extractDriveElements(snapshotExtractor.changeUrlDate(route.date));
//        } catch (RuntimeException exception){
//            GlobalVariables.extractionErrors += 1;
//        }
//
//
//            for (Element el: drives
//                 ) {
//                writer.writeDriveLine_CSV(DriveInfoBoxDataExtractor.getDriveDataSnapshot(snapshotExtractor.changeUrlDate(route.date),el));
//            }
//
//    }

    public void downloadSnapshots_Iterable(Route firstDateRoute) throws InterruptedException, IOException {
        snapshotExtractor.getToDesiredRouteWebPage(firstDateRoute);
        String currentWorkingUrl = snapshotExtractor.getCurrentURL();

        List<Element> driveDataElements = new ArrayList<>();

//        if(!snapshotExtractor.noTicketsPage(currentWorkingUrl)){
//            driveDataElements.addAll(snapshotExtractor.extractDriveElements(currentWorkingUrl));
//            currentWorkingUrl = snapshotExtractor.getNextDateUrl(currentWorkingUrl);
//        }
//        else {
//            currentWorkingUrl = snapshotExtractor.getNextDateUrl(currentWorkingUrl);
//        }
//
//        while (!snapshotExtractor.noTicketsPage(currentWorkingUrl)){
//            driveDataElements.addAll(snapshotExtractor.extractDriveElements(currentWorkingUrl));
//            currentWorkingUrl = snapshotExtractor.getNextDateUrl(currentWorkingUrl);
//        }

        for(int day=0; day<GlobalVariables.maxDaysDifference; day+=1){
            if(!snapshotExtractor.noTicketsPage(currentWorkingUrl)){
                driveDataElements.addAll(snapshotExtractor.extractDriveElements(currentWorkingUrl));
            }
            currentWorkingUrl = snapshotExtractor.getNextDateUrl(currentWorkingUrl);
        }

        for (Element driveData: driveDataElements
             ) {
            writer.writeDriveLine_CSV(DriveInfoBoxDataExtractor.getDriveDataSnapshot(driveData));

        }


    }

    List<Route> initializeRoutes() throws IOException {
        List<String[]> pairs = writer.readDepartureArrival();

        List<Route> routes = new ArrayList<>();

        for (String[] pair:pairs
             ) {
                routes.add(new Route(pair, LocalDateTime.now()));
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
