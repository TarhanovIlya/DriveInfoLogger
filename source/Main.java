import org.jsoup.nodes.Element;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {




        String csvLogURL = "C:\\Users\\r1r2\\Desktop\\java_code\\FreeBusSeatsLogger\\data\\data.txt";
        String errorLogURL = "C:\\Users\\r1r2\\Desktop\\java_code\\FreeBusSeatsLogger\\data\\errorLog.txt";

        Route route = new Route("Минск", "Молодечно", "2025-10-18");




        FileOperator writer = new FileOperator(csvLogURL, errorLogURL);
        SiteSnapshotExtractor snapshotExtractor = null;



        try {


            snapshotExtractor = new SiteSnapshotExtractor(route);

            System.out.println(snapshotExtractor.url);

            String pageURL = snapshotExtractor.url;

            List<Element> drives = snapshotExtractor.extractDriveElements(snapshotExtractor.url);


            for (Element el: drives
                 ) {
                writer.writeDriveLine_CSV(DriveInfoBoxDataExtractor.getDriveDataSnapshot(pageURL,el));
            }

        }

        catch (Exception e){
            e.printStackTrace();
            writer.writeErrorLog(e.getMessage());
        }

        finally {
            writer.close();
            snapshotExtractor.close();
        }

    }

}
