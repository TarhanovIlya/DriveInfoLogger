import org.jsoup.nodes.Element;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {




    public static void main(String[] args) throws Exception {

        ChromeDriver chromeDriver = new ChromeDriver();

        FileOperator writer = new FileOperator(GlobalVariables.csvLogURL, GlobalVariables.errorLogURL, GlobalVariables.sessionLogURL, GlobalVariables.departureArrivalURL);

        SiteSnapshotExtractor snapshotExtractor = new SiteSnapshotExtractor(chromeDriver);
        MyScripts scripts = new MyScripts();

        scripts.setSnapshotExtractor(snapshotExtractor);
        scripts.setWriter(writer);

        try {

//            List<Route> routes = scripts.createRoutes();
//
//            for (Route route:routes
//                 ) {
//                scripts.downloadSnapshot(route);
//            }

            scripts.downloadSnapshot(new Route("Минск", "Гродно", "2025-10-20"));


        }

        catch (Exception e){
            e.printStackTrace();
            writer.writeErrorLog(e.getMessage());
        }

        finally {
            writer.writeSessionLog();

            writer.close();
            snapshotExtractor.close();
        }

    }

}
