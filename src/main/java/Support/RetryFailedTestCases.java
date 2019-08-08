package Support;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class RetryFailedTestCases implements IRetryAnalyzer {
    //Counter to keep track of retry attempts
    private int retryCount = 0;
    private int maxRetryCount = 3;
    public boolean retry(ITestResult result) {
        if(retryCount < maxRetryCount)
        {
            retryCount++;
            return true;
        }
        return false;
    }

}
