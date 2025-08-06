package org.example.listeners;

import org.apache.logging.log4j.Logger;
import org.example.utils.LoggerUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyser implements IRetryAnalyzer {
    public final Logger logger = LoggerUtil.getLogger(getClass());
    private int attempt = 0;
    private final int maxRetry = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (attempt < maxRetry) {
            logger.warn("Retrying test: {} | Retry #{}", result.getName(), (attempt +1));
            attempt++;
            return true;
        }
        return false;
    }
}
