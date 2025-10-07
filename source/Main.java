import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        SiteSnapshotExtractor snapshotExtractor = null;
        try {
            Route route = new Route("Молодечно", "Минск", "2025-10-08");
            snapshotExtractor = new SiteSnapshotExtractor(route);

            System.out.println(snapshotExtractor.url);

            List<Element> drives = snapshotExtractor.extractDriveElements(snapshotExtractor.url);

            DriveInfoBoxDataExtractor extractor = new DriveInfoBoxDataExtractor();

            for (Element e: drives) {
                System.out.println(extractor.fetchTime(e));
            }

        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }

        finally {
            snapshotExtractor.close();
        }




    }
}
