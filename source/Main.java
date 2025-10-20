import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        // Schedule the task to run periodically
        Timer timer = new Timer(true);
        long delay = 0;
        long period = 1000 * 60 * 10;

        timer.scheduleAtFixedRate(new SnapshotTask(), delay, period);

        System.out.println("Automation started. Press Ctrl+C to stop.");

        // Keep main thread alive so Timer can run
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class SnapshotTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("Starting snapshot task at: " + new java.util.Date());

            ChromeDriver chromeDriver = null;
            FileOperator writer = null;
            SiteSnapshotExtractor snapshotExtractor = null;

            try {
                chromeDriver = new ChromeDriver();
                writer = new FileOperator(
                        GlobalVariables.csvLogURL,
                        GlobalVariables.errorLogURL,
                        GlobalVariables.sessionLogURL,
                        GlobalVariables.departureArrivalURL
                );

                snapshotExtractor = new SiteSnapshotExtractor(chromeDriver);
                MyScripts scripts = new MyScripts();

                scripts.setSnapshotExtractor(snapshotExtractor);
                scripts.setWriter(writer);

                List<Route> routes = scripts.initializeRoutes();

                for (Route route : routes) {
                    try {
                        scripts.downloadSnapshots_Iterable(route);
                    } catch (RuntimeException ignored) {
                    }
                }

                System.out.println("Snapshot task completed successfully.");

            } catch (Exception e) {
                e.printStackTrace();
                if (writer != null) {
                    try {
                        writer.writeErrorLog(e.getMessage());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } finally {
                try {
                    if (writer != null) {
                        writer.writeSessionLog();
                        writer.close();
                    }
                    if (snapshotExtractor != null) snapshotExtractor.close();
                    if (chromeDriver != null) chromeDriver.quit();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
