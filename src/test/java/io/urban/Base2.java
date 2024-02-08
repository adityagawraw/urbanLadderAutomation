package io.urban;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Base2 {
    static WebDriver driver = null;
    static Actions actions;
    static WebDriverWait wait;

    ExtentReports extentReports  = new ExtentReports();
    ExtentSparkReporter sparkReporter =
            new ExtentSparkReporter("/home/aditya/Documents/testReports"+new Date()+".html");

    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeTest
    public void setWebDriver(){
        driver = new ChromeDriver();
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        sparkReporter.config().setDocumentTitle("my Report");
        extentReports.attachReporter(sparkReporter);
    }

    @AfterTest
    public void closeDriver(){
        extentReports.flush();
        driver.close();
    }


    @Test(priority = 0)
    public void navigateToWebsite(){
        driver.navigate().to("https://www.urbanladder.com/");
        driver.manage().window().maximize();

        ExtentTest test = extentReports.createTest("Navigate to website").assignAuthor("aditya");
        test.info("captring title");

        if(driver.getTitle().equalsIgnoreCase("urbanladder")){
            test.pass(" correct title");
        }
        else {
            test.fail("incoorect title");
        }
    }

    @Test(priority = 1)
    public void navigateToCoffeeTableProducts(){
        ExtentTest test = extentReports.createTest("Navigate to website").assignAuthor("aditya");
        Action goToLiving = actions
                    .moveToElement(driver.findElement(By.cssSelector(".topnav_item.livingunit")))
                    .build();
            goToLiving.perform();

            WebElement coffeTable = driver
                    .findElement(By.cssSelector("a[href^='/coffee-table?src=g_topnav_living_tables_coffee-tables']"));

            wait.until(ExpectedConditions.elementToBeClickable(coffeTable));

            test.info(" clicking on coffee table option");
             try {
                 coffeTable.click();
                 test.pass("Successfully clicked on coffee table option");
             }
             catch (Exception e){
                 test.fail("Was unable to successfully clicked on coffee table option with error : "
                         +e.getMessage());
                 test.addScreenCaptureFromPath(captureScreenShot(driver), "Unable to clicking coffee button");
             }
        }
    @Test(priority = 2)
    public void setProductPriceRange(){
        int lowerRange = 5000;
        int upperRange = 20000;

        ExtentTest test = extentReports.createTest("Set price range").assignAuthor("aditya");

        Action goToPrice = actions
                .moveToElement(driver.findElement(By.cssSelector("li[data-group='price']"))).
                build();

        wait.until(ExpectedConditions.
                visibilityOf(driver.findElement(By.cssSelector(".close-reveal-modal.hide-mobile"))));
        WebElement closePopup = driver.findElement(By.cssSelector(".close-reveal-modal.hide-mobile"));
        closePopup.click();

        goToPrice.perform();

        WebElement priceRange = driver
                .findElement(By
                        .cssSelector(".range-slider.noUi-target.noUi-ltr.noUi-horizontal.noUi-background"));

        int maxPriceRange = Integer.parseInt(priceRange.getAttribute("data-max"));
        int minPriceRange = Integer.parseInt(priceRange.getAttribute("data-min"));
        int range = maxPriceRange - minPriceRange;


        WebElement scrollSegment = driver.findElement(By.cssSelector(".noUi-base"));
        wait.until(ExpectedConditions.visibilityOf(scrollSegment));

        int scrollSegementLength = scrollSegment.getSize().getWidth();
        int leftScrollDistance = (lowerRange * scrollSegementLength) / range;
        int rightScrollDistance = scrollSegementLength - (upperRange * scrollSegementLength) / range;



        test.info("Check if price range selected via scroll ");
        try {
            WebElement leftScrolls = driver.findElement(By.cssSelector(".noUi-handle.noUi-handle-lower"));
            WebElement rightScrolls = driver.findElement(By.cssSelector(".noUi-handle.noUi-handle-upper"));
            actions.dragAndDropBy(leftScrolls, leftScrollDistance, 0).perform();
            actions.dragAndDropBy(rightScrolls, -rightScrollDistance, 0).perform();
            test.pass("price rnage selected");
        }
        catch (Exception e){
            test.fail("could not set price range");
            test.addScreenCaptureFromPath(captureScreenShot(driver));
        }

    }

    @Test(priority = 3)
    public void setTheProductList(){
        WebElement listOfProduct = driver.
                findElement(By.
                        cssSelector(".productlist.withdivider.clearfix.coffee_tables.productgrid"));

        wait.until(ExpectedConditions.stalenessOf(listOfProduct));

        List<WebElement> products = driver.findElements(By.cssSelector("a[class='product-title-block']"));
        List<Product> productData = new ArrayList<>();

        for (WebElement element : products) {
            Product product = new Product();
            product.setProductName(element.findElement(By.cssSelector("span[class = 'name']")).getText());
            product.setFinalPrice(element.findElement(By.cssSelector("div[class = 'price-number'] span")).getText());

            productData.add(product);
        }

        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.enterProductData(productData);
    }

    public static String  captureScreenShot(WebDriver driver){
        String screenShotPath= "/home/aditya/Documents/testScreenShots"+new Date().toString()+".jpg";

        TakesScreenshot takesScreenshot =(TakesScreenshot) driver;
        File screenShot = takesScreenshot.getScreenshotAs(OutputType.FILE);
        System.out.println("screen "+screenShot.getTotalSpace());

        try{
            FileHandler.copy(screenShot, new File(screenShotPath));
        }
        catch (IOException er){
            System.out.println(er);
        }

        return  screenShotPath;
    }


}
