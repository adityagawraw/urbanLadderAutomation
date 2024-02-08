package io.urban;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        captureScreenshot();
    }

    private void captureScreenshot() {
        WebDriver driver = Base2.getDriver();
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
