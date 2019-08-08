package Keywords;

import Locators.ObjectRepository;
import Support.*;
import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

public class SignUpKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    static Logger Log = Logger.getLogger(SignInKeywords.class);

    public SignUpKeywords(SetupServer driver){
        this.driver = driver;
        this.common = new CommonKeywords(driver);
    }

    /**
     * Open Sign Up screen
     */
    public void openSignUpScreen(){
        Log.info("Started openSignUpScreen keywords");
        MobileElement NewUSer = driver.findElement(OR.signup.getLocator("btnNewUser_SignUp"));
        NewUSer.click();
    }

    /**
     * Check Sign Up Screen and all elements are displayed
     * @return true/false
     */
    public boolean checkSignUpDisplay(String testcase){
        Log.info("Started checkSignUpDisplay keyword");
        boolean flag = true;
        driver.Wait(2);
        try {
            driver.isElementExist(OR.signup.getLocator("tbUserName"));
            Log.info("Passed - UserName is shown");
        }catch(Exception e){
            Log.info("Failed - UserName is not shown");
            flag = false;
        }

        try {
            driver.isElementExist(OR.signup.getLocator("tbPassWord"));
            Log.info("Passed - Password is shown");
        }catch(Exception e){
            Log.info("Failed - Password is not shown");
            flag = false;
        }

        try {
            driver.isElementExist(OR.signup.getLocator("btnSignUp"));
            Log.info("Passed - SignUp Button is shown");
        }catch(Exception e){
            Log.info("Failed - SignUp Button is not shown");
            flag = false;
        }

        String status = driver.findElement(OR.signup.getLocator("btnSignUp")).getAttribute("enabled");
        if (status.equals("false")){
            Log.info("Passed - SignUp Button is disable");
        }else{
            Log.info("Failed - SignUp Button is not disable");
            flag = false;
        }

        common.clickElementButton("Terms of Service");
        try {
            driver.Wait(4,OR.signup.getLocator("webView"));
            driver.isElementExist(OR.signup.getLocator("webView"));
            Log.info("Passed - Can click the link Terms of Service");
            // Return to FloDEV
            driver.findElement(OR.signup.getLocator("returnToFlo")).click();
        }catch(Exception e){
            Log.info("Failed - Can not click the link Terms of Service");
            flag = false;
        }

        common.clickElementButton("Privacy Policy.");
        try {
            driver.Wait(4,OR.signup.getLocator("webView"));
            driver.isElementExist(OR.signup.getLocator("webView"));
            Log.info("Passed - Can click the link Privacy Policy.");
            // Return to FloDEV
            driver.findElement(OR.signup.getLocator("returnToFlo")).click();
        }catch(Exception e){
            Log.info("Failed - Can not click the link Privacy Policy.");
            flag = false;
        }

        if(flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkSignUpDisplay keyword");
        return flag;
    }

    /**
     * Input Username and Password, then tap on "Sign Up" button
     * @param Username
     * @param Password
     * @param IsClickSignUp
     */
    public void signUp(String Username, String Password, boolean IsClickSignUp) {
        Log.info("Started Sign Up keywords");
        Log.info("Input Username > "+ Username);
        MobileElement tbUserName = driver.findElement(OR.signup.getLocator("tbUserName"));
        MobileElement tbPassWord = driver.findElement(OR.signup.getLocator("tbPassWord"));
        // input username
        tbUserName.clear();
        tbUserName.setValue(Username);

        // Check error show
        if(driver.isElementPresent(OR.signup.getLocator("btnOK"))== true){
            Log.info("Error message displayed - Please check username again");
            return;
        }

        // input password
        if(Password.equals("")) {
            // No action
        }else if (Username.length() <= 2 || Username.equals(""))
                tbPassWord.click();
            else {
                Log.info("Input Password > " + Password);
                tbPassWord.clear();
                tbPassWord.setValue(Password);
                // driver.Wait(5);
            }

        if(IsClickSignUp == true) {
            driver.Wait(2);
            Log.info("Click Sign in button");
            MobileElement SignInButton = driver.findElement(OR.signup.getLocator("btnSignUp"));
            SignInButton.click();
        }
        Log.info("Ended Sign Up keywords");
    }

    /**
     * Check Sign Up success
     * @param testcase
     * @return true/false
     */
    public boolean checkSignUpSuccess(String testcase){
        Log.info("Started Check Sign Up Success");
        boolean flag = true;
        try {
            // Wait to show Start Flo popup
            driver.Wait(5, OR.signup.getLocator("btnStartFlo"));
            driver.isElementExist(OR.signup.getLocator("btnStartFlo"));
            Log.info("Passed - Sign Up Successfully");
        }catch(Exception e){
            Log.info("Failed - Sign Up failed ");
            flag = false;
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended Check Sign Up Success");
        return flag;
    }

    /**
     * Check content of Error Message popup
     * @param content
     * @param testcase
     * @return
     */
    public boolean checkErrorMessage(String content, String testcase){
        Log.info("Started checkErrorMessages keywords");
        boolean flag = true;
        // Wait to show Error Message popup
        driver.Wait(10, OR.signup.getLocatorDynamic("ErrorMessage",new String[]{content}));
        if(driver.isElementPresent(OR.signup.getLocatorDynamic("ErrorMessage",new String[]{content}))) {
            Log.info("Passed - Error shows: " + content);
            closeErrorMessage();
        }
        else{
            Log.info("Failed - Error do not show: " + content);
            flag = false;
            driver.captureScreenShot(testcase);

            // Check and close other popup
            if(driver.isElementPresent(OR.signup.getLocatorDynamic("ErrorMessage",new String[]{"Sign Up"})))
                closeErrorMessage();
        }
        Log.info("Ended checkErrorMessages keywords");
        return flag;
    }

    /**
     * Close Error Message popup
     */
    public void closeErrorMessage(){
        Log.info("Started closeErrorMessage keywords");
        MobileElement btnOK = driver.findElement(OR.signup.getLocator("btnOK"));
        btnOK.click();
    }
}

