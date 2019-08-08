package Support;

import Environments.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Log {

    /**
     * Initialize Log4j logs
     */
    private static Logger Log = Logger.getLogger(Log.class);
    private static String strSpliter = StringUtils.repeat('*', Constants.NUM_CHARS_LOG);

    /**
     * This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite
     * @param sTestCaseName
     */
    public static void startTestCase(String sTestCaseName){
        Log.info(strSpliter);
        String strJoin;
        // Count number chars to add to String test case name
        if(sTestCaseName.length() >= Constants.NUM_CHARS_LOG - 12)
            strJoin = "#";
        else{
            int numChar = Constants.NUM_CHARS_LOG - sTestCaseName.length();
            strJoin = StringUtils.repeat('$', numChar/2 - 5);
        }
        // Print log test case
        Log.info(strJoin +"     "+ sTestCaseName +"     " + strJoin);
        Log.info(strSpliter);
    }

    /**
     * This is to print log for the ending of the test case
     * @param sTestCaseName
     */
    public static void endTestCase(String sTestCaseName){
        // Count number chars to add to String test case name
        String strJoin;
        if(sTestCaseName.length() >= Constants.NUM_CHARS_LOG - 20)
            strJoin = "#";
        else {
            int numChar = Constants.NUM_CHARS_LOG - sTestCaseName.length();
            strJoin = StringUtils.repeat('#', numChar/2 - 9);
        }
        Log.info(strJoin +"     - End - "+ sTestCaseName +"     " + strJoin);
        Log.info(strSpliter);
    }

    // Need to create these methods, so that they can be called
    public static void info(String message) {
        Log.info(message);
    }

    public static void warn(String message) {
        Log.warn(message);
    }

    public static void error(String message) {
        Log.error(message);
    }

    public static void fatal(String message) {
        Log.fatal(message);
    }

    public static void debug(String message) {
        Log.debug(message);
    }
}
