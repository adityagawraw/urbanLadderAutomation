package io.urban;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Base {

    public static void main(String[] args) throws InterruptedException, IOException {
        testMethod();
    }
    public static void testMethod() throws InterruptedException, IOException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver= new ChromeDriver();

        driver.navigate().to("https://www.urbanladder.com/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Actions actions  = new Actions(driver);
//        WebElement living = driver.findElement(By.cssSelector(".topnav_itemname"));

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

//        WebElement scrollSegment = driver.findElement(By.cssSelector(".noUi-base"));
//        int scrollSegementLength  = scrollSegment.getSize().getWidth();

        WebElement leftScrolls = driver.findElement(By.className("noUi-handle-lower"));
        System.out.println(leftScrolls.getLocation());
        actions.dragAndDropBy(leftScrolls,60, 0).perform();

        Thread.sleep(4000);

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
