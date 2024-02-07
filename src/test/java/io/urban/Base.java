package io.urban;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Base {

    public static void main(String[] args) throws InterruptedException, IOException {
        testMethod(5000, 20000, "chrome");
    }
    public static void testMethod(int lowerRange, int upperRange, String browser)
            throws InterruptedException, IOException {
        WebDriver driver = null;

        if(browser.equalsIgnoreCase("chrome"))
        {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }

        driver.navigate().to("https://www.urbanladder.com/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        Actions actions  = new Actions(driver);

        Action goToLiving = actions
                .moveToElement(driver.findElement(By.cssSelector(".topnav_item.livingunit")))
                .build();
                goToLiving.perform();

        wait.until(ExpectedConditions
                .elementToBeClickable(driver
                        .findElement(By
                                .cssSelector("a[href^='/coffee-table?src=g_topnav_living_tables_coffee-tables']"))));
        WebElement coffeTable = driver
                .findElement(By.cssSelector("a[href^='/coffee-table?src=g_topnav_living_tables_coffee-tables']"));

        coffeTable.click();

        Action goToPrice = actions
                .moveToElement(driver.findElement(By.cssSelector("li[data-group='price']"))).
                build();
        goToPrice.perform();
        Thread.sleep(2000);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".close-reveal-modal.hide-mobile"))));
        WebElement closePopup = driver.findElement(By.cssSelector(".close-reveal-modal.hide-mobile"));
        closePopup.click();
        Thread.sleep(1000);

        goToPrice.perform();

        WebElement priceRange = driver
                .findElement(By
                        .cssSelector(".range-slider.noUi-target.noUi-ltr.noUi-horizontal.noUi-background"));

        int maxPriceRange = Integer.parseInt(priceRange.getAttribute("data-max"));
        int minPriceRange = Integer.parseInt(priceRange.getAttribute("data-min"));
        int range = maxPriceRange - minPriceRange;


        WebElement scrollSegment = driver.findElement(By.cssSelector(".noUi-base"));
        int scrollSegementLength  = scrollSegment.getSize().getWidth();

        int leftScrollDistance = (lowerRange*scrollSegementLength)/range;
        int rightScrollDistance =scrollSegementLength - (upperRange*scrollSegementLength)/range;

        WebElement leftScrolls = driver.findElement(By.cssSelector(".noUi-handle.noUi-handle-lower"));
        WebElement rightScrolls = driver.findElement(By.cssSelector(".noUi-handle.noUi-handle-upper"));

        actions.clickAndHold(leftScrolls).moveByOffset(leftScrollDistance, 0).release().perform();
        actions.clickAndHold(rightScrolls).moveByOffset(rightScrollDistance, 0).release().perform();

        List<WebElement> products =  driver.findElements(By.cssSelector("a[class='product-title-block']"));
        List<Product> productData = new ArrayList<>();

        for(WebElement element: products){
            Product product = new Product();
            product.setProductName(element.findElement(By.cssSelector("span[class = 'name']")).getText());
            product.setFinalPrice(element.findElement(By.cssSelector("div[class = 'price-number'] span")).getText());

            productData.add(product);
        }

        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.enterProductData(productData);
        driver.close();
    }



}
