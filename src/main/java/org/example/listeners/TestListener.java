package org.example.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.Logger;
import org.example.reports.ExtentManager;
import org.example.reports.ExtentTestManager;
import org.example.utils.LoggerUtil;
import org.example.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;

public class TestListener implements ITestListener, ISuiteListener {

    ExtentReports extent;

    public final Logger logger = LoggerUtil.getLogger(getClass());

    @Override
    public void onStart(ISuite suite) {
        extent = ExtentManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (extent != null && result.getMethod() != null) {
            logger.info("Test Started: {}", result.getName());
            ExtentTest test = extent.createTest(result.getMethod().getMethodName());
            ExtentTestManager.setTest(test);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        ExtentTestManager.getTest().info("Logs can be found at: logs/test-log.log");
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        ExtentTestManager.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable());

        WebDriver driver = ScreenshotUtil.extractDriver(result);

        if (driver != null) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getMethod().getMethodName());
            logger.info("Screenshot saved at: {}", screenshotPath);
            ExtentTestManager.getTest().addScreenCaptureFromPath(new File(screenshotPath).getAbsolutePath());
        } else {
            logger.warn("WebDriver was null, screenshot not captured.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
    }
}
