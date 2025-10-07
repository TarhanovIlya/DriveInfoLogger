import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SiteSnapshotExtractor implements AutoCloseable {

    public final String url;
    private JavascriptExecutor js;
    private ChromeDriver chromeDriver;
    private Route route;


    public SiteSnapshotExtractor(Route route) throws InterruptedException {

        this.chromeDriver = new ChromeDriver();
        this.js = (JavascriptExecutor) chromeDriver;

        this.route = route;

        getToDesiredRouteWebPage(route);

        url = chromeDriver.getCurrentUrl();
    }

    @Override
    public void close() throws Exception {
        chromeDriver.quit();
    }




    public void getToDesiredRouteWebPage(Route route) throws InterruptedException {
        final String baseUrl = "https://atlasbus.by/";

        chromeDriver.get(baseUrl);
        Thread.sleep(5000);


        WebElement fromBox = chromeDriver.findElement(By.cssSelector("input[data-testid='from-suggest']"));
        inputIntoWebElement(fromBox, route.from);

        WebElement toBox = chromeDriver.findElement(By.cssSelector("input[data-testid='to-suggest']"));
        inputIntoWebElement(toBox, route.to);

        WebElement searchButton = chromeDriver.findElement(By.cssSelector("button[data-testid='search-button']"));
        searchButton.sendKeys(Keys.ENTER);

        changeUrlDate(route.date);
    }

    private void inputIntoWebElement(WebElement element, String data) throws InterruptedException {


        new Actions(chromeDriver).moveToElement(element).click().perform();

        element.sendKeys(data);

        element.click();
        Thread.sleep(3000);
        element.sendKeys(Keys.ENTER);

    }

    private void changeUrlDate(String date) throws InterruptedException {

        Thread.sleep(1000);
        StringBuilder url = new StringBuilder(chromeDriver.getCurrentUrl());

        int start = url.indexOf("date=") + 5;
        int end = start + 10;

        url.replace(start, end, date);

        chromeDriver.get(url.toString());

    }




    public List<Element> extractDriveElements(String url) throws IOException {

        List<Element> elements = new ArrayList<>();


        Document doc = Jsoup.connect(url).get();

        elements = doc.select(".jss10");

        return elements;
    }



}
