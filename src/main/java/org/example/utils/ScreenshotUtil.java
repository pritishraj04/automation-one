package org.example.utils;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String methodName) {
        final Logger logger = LoggerUtil.getLogger(ScreenshotUtil.class);

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = methodName + "_" + timestamp + ".png";
        String destPath = System.getProperty("user.dir") + "/reports/screenshots/" + fileName;
        File dest = new File(destPath);

        dest.getParentFile().mkdirs();
        try {
            logger.info("Saving screenshot to {}", destPath);
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Failed to Copy to destination file", e);
        }

        return destPath;
    }

    public static WebDriver extractDriver(ITestResult result) {
        Object instance = result.getInstance();
        try {
            return (WebDriver) instance.getClass().getDeclaredField("driver").get(instance);
        } catch (Exception e) {
            return null;
        }
    }
}
