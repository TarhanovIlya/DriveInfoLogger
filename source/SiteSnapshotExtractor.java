import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SiteSnapshotExtractor implements AutoCloseable {

    private JavascriptExecutor js;
    private ChromeDriver chromeDriver;
    private WebDriverWait wait;

    public SiteSnapshotExtractor(ChromeDriver chromeDriver) throws InterruptedException {

        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(30));

        this.js = (JavascriptExecutor) chromeDriver;
    }

    @Override
    public void close() throws Exception {
        chromeDriver.quit();
    }

    public String getCurrentURL(){
        return chromeDriver.getCurrentUrl();
    }





    public void getToDesiredRouteWebPage(Route route) throws InterruptedException {
        final String baseUrl = "https://atlasbus.by/";

        chromeDriver.get(baseUrl);

        WebElement fromBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[data-testid='from-suggest']")));;
        inputIntoWebElement(fromBox, route.from);

        WebElement toBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[data-testid='to-suggest']")));
        inputIntoWebElement(toBox, route.to);

        String previousUrl = chromeDriver.getCurrentUrl();

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-testid='search-button']")));
        searchButton.sendKeys(Keys.ENTER);


        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(previousUrl)));
        chromeDriver.get(changeUrlDate(route.date, getCurrentURL()));
    }

    private void inputIntoWebElement(WebElement element, String data) throws InterruptedException {




        new Actions(chromeDriver).moveToElement(element).click().perform();

        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);

        Thread.sleep(3000);
        element.sendKeys(data);
        Thread.sleep(3000);
        element.sendKeys(Keys.ENTER);

    }

    public String getNextDateUrl(String url) throws InterruptedException {
        LocalDate currentUrlDate = extractUrlDate(url);
        currentUrlDate = currentUrlDate.plusDays(1);
        return changeUrlDate(currentUrlDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), url);
    }

    private LocalDate extractUrlDate(String url) {
        int start = url.indexOf("date=") + 5;
        int end = start + 10;
        return LocalDate.parse(url.substring(start, end));
    }

    public String changeUrlDate(String date, String previousUrl) throws InterruptedException {

        StringBuilder url = new StringBuilder(chromeDriver.getCurrentUrl());

        int start = url.indexOf("date=") + 5;
        int end = start + 10;

        url.replace(start, end, date);

        return url.toString();

    }


    public boolean noTicketsPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).timeout(60000).get();

        Element e = doc.selectFirst(".MuiContainer-root.jss7.MuiContainer-maxWidthLg").child(0).child(0).child(0);
        e = e.selectFirst("h3");


        if (e == null) return false;

        String text = e.text();

        return text.equalsIgnoreCase("билеты не найдены");
    }


    public List<Element> extractDriveElements(String url) throws IOException {


        List<Element> elements = new ArrayList<>();

        Document doc = Jsoup.connect(url).timeout(60000).get();

        elements = doc.select(".jss10");

        GlobalVariables.driveElementsRead += elements.size();

        if (elements.isEmpty()) throw new RuntimeException("Tried to fetch driveElements, but extracted 0");
        return elements;
    }



}
