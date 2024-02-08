package io.urban;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Base {
    WebDriver driver = null;
    public static void main(String[] args) throws InterruptedException, IOException {
       new Base().testMethod(5000, 20000, "chrome");
    }

    public  void testMethod(int lowerRange, int upperRange, String browser)
            throws InterruptedException, IOException {

        if(browser.equalsIgnoreCase("chrome"))
        {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }

        driver.navigate().to("https://www.urbanladder.com/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try{
            Actions actions = new Actions(driver);

            Action goToLiving = actions
                    .moveToElement(driver.findElement(By.cssSelector(".topnav_item.livingunit")))
                    .build();
            goToLiving.perform();

            WebElement coffeTable = driver
                    .findElement(By.cssSelector("a[href^='/coffee-table?src=g_topnav_living_tables_coffee-tables']"));
            wait.until(ExpectedConditions.elementToBeClickable(coffeTable));

            coffeTable.click();

            Action goToPrice = actions
                    .moveToElement(driver.findElement(By.cssSelector("li[data-group='price']"))).
                    build();

            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".close-reveal-modal.hide-mobile"))));
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

            WebElement leftScrolls = driver.findElement(By.cssSelector(".noUi-handle.noUi-handle-lower"));
            WebElement rightScrolls = driver.findElement(By.cssSelector(".noUi-handle.noUi-handle-upper"));

            actions.dragAndDropBy(leftScrolls, leftScrollDistance, 0).perform();
            actions.dragAndDropBy(rightScrolls, -rightScrollDistance, 0).perform();

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

            driver.close();
        }
        catch (Exception e){
           String screenShotPath= "/home/aditya/Documents/"+new Date().toString()+".jpg";
            TakesScreenshot takesScreenshot =(TakesScreenshot) driver;
            File screenShot = takesScreenshot.getScreenshotAs(OutputType.FILE);
            System.out.println("screen "+screenShot.getTotalSpace());
            try{
                FileHandler.copy(screenShot, new File(screenShotPath));
            }
            catch (IOException er){
                System.out.println(er);
            }
            driver.close();
        }
    }


}
