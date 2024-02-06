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
//        driver.manage().window().maximize();

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


        WebElement scroll = driver.findElement(By.cssSelector("div[class = 'noUi-origin']"));
        JavascriptExecutor je = (JavascriptExecutor) driver;
//        je.executeScript("arguments[0].setAttribute('value', '5000')", lowerValue);
        je.executeScript("arguments[0].style.left = '10%'", scroll);

//        je.executeScript("document.getElementByClass('noUi-origin')[0].style.left = '20%'");
//        je.executeScript("document.getElementById('filters_price_max').value = '20000'");

//        je.executeScript()


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
//        driver.close();
    }



}
