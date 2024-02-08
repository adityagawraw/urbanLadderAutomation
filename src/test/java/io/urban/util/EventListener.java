package io.urban.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.urban.Test2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EventListener implements ITestListener {
    ExtentReports extentReports = new ExtentReports() ;
    ExtentTest test;
    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(new Date()+".html");

        sparkReporter.config().setDocumentTitle("my Report");
        extentReports.attachReporter(sparkReporter);
    }
    @Override
    public void onTestStart(ITestResult result) {
        test = extentReports.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass(result.getMethod().getMethodName()+" passed");
    }
    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(result.getThrowable().getMessage()+" failed");
        test.addScreenCaptureFromPath(captureScreenshot());
    }
    @Override
    public void onFinish(ITestContext context){
        extentReports.flush();
    }

    private String captureScreenshot() {
        WebDriver driver = Test2.getDriver();
        String screenShotPath= new Date().toString()+".jpg";

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File screenShot = takesScreenshot.getScreenshotAs(OutputType.FILE);
        System.out.println("screen "+screenShot.getName());

        try{
            FileHandler.copy(screenShot, new File(screenShotPath));
        }
        catch (IOException er){
            System.out.println(er);
        }

        return  screenShotPath;
    }

}
