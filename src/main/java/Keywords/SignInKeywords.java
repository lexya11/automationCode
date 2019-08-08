package Keywords;

import Locators.*;
import Support.SetupServer;
import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

public class SignInKeywords {
    // SignInKeywords variables
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;

    // Logger variable
    static Logger Log = Logger.getLogger(SignInKeywords.class);

    public SignInKeywords(SetupServer driver) {
        this.driver = driver;
        this.common = new CommonKeywords(driver);
    }

    /**
     * Skip Introduce Flo App screen
     */
    public void skipIntroduce(){
        Log.info("Started Skip Introduce keywords");
        // Check SendNotification popup to close
        common.closeSendNotification(true);

        driver.Wait(3, OR.signin.getLocator("btnSkipIntroduce"));
        MobileElement btnSkip = driver.findElement(OR.signin.getLocator("btnSkipIntroduce"));
        btnSkip.click();

        // Action to smoke test
//        btnSkip.tap(1,1);this.home = new HomeKeywords(driver);
//        driver.tapCoordinate(340, 640);
//        driver.tapElement(OR.signin.getLocator("btnSkipIntroduce"));
    }

    /**
     * Open Login screen
     */
    public void openLoginScreen(){
        Log.info("Started Open Login screen keywords");
        MobileElement btnSkip = driver.findElement(OR.signin.getLocator("btnExistingUsers"));
        btnSkip.click();
    }

    /**
     * Check Sign In success
     * @param testcase
     * @return true/false
     */
    public boolean checkSignInSuccess(String testcase){
        Log.info("Started Check SignIn Success");
        boolean flag = true;
        try {
            driver.Wait(10, OR.signin.getLocator("btnStartFlo"));
            driver.isElementExist(OR.signin.getLocator("btnStartFlo"));
            Log.info("Passed - Sign In Successfully");
        }catch(Exception e){
            Log.info("Failed - Sign In failed "+ e);
            flag = false;
            driver.captureScreenShot(testcase);
        }
        return flag;
    }

    /**
     * Check Sign In Screen is displayed
     * @param testcase
     * @return true/false
     */
    public boolean checkSignInScreenDisplay(String testcase){
        Log.info("Started Check SignIn Screen Display keyword");
        boolean flag = true;
        try {
            driver.Wait(10, OR.signin.getLocator("btnSignIn"));
            driver.isElementExist(OR.signin.getLocator("btnSignIn"));
            Log.info("Passed - Sign In Screen is displayed ");
        }catch(Exception e){
            Log.info("Failed - Sign In Screen is not displayed "+ e);
            flag = false;
            driver.captureScreenShot(testcase);
        }
        return flag;
    }

    /**
     * Check Sign In button enable (true - User can Tap on it / false - Show Log)
     * @return true/false
     */
    public boolean checkSignInNotEnable(String testcase){
        Log.info("Started Check SignIn button enable keyword");
        driver.Wait(2);
        boolean flag = true;
        MobileElement SignInButton = driver.findElement(OR.signin.getLocator("btnSignIn"));
        if (!SignInButton.isEnabled())
            Log.info("Passed - SignIn button is not ENABLED");
        else{
            Log.info("Failed - SignIn button is ENABLED ");
            flag = false;
            driver.captureScreenShot(testcase);
        }
        return flag;
    }

    /**
     * Input Username and Password then click Sign In Button
     * @param username
     * @param password
     * @param isSignin
     */
    public void login(String username, String password, boolean isSignin) {
        Log.info("Started Sign in keywords");
        Log.info("Input Username > " + username);
        MobileElement tbUserName = driver.findElement(OR.signin.getLocator("tbUserName"));
        MobileElement tbPassWord = driver.findElement(OR.signin.getLocator("tbPassWord"));
        if(!username.equals("")){
            tbUserName.clear();
            tbUserName.setValue(username);
        }else {
            tbUserName.clear();
        }
        Log.info("Input Password > " + password);
        if(!password.equals("")) {
            tbPassWord.clear();
            tbPassWord.setValue(password);
        }else {
            tbPassWord.clear();
        }
        if (isSignin == true){
            Log.info("Click Sign in button");
            MobileElement SignInButton = driver.findElement(OR.signin.getLocator("btnSignIn"));
            SignInButton.click();
        }
        Log.info("Ended Sign in keywords");
    }

    /**
     * Close Error Message popup
     */
    public void closeErrorMessage(){
        Log.info("Started Error Message popup keywords");
        MobileElement btnOK = driver.findElement(OR.signin.getLocator("btnOK"));
        btnOK.click();
    }

    /**
     * Check Error Message popup
     * @param Content
     * @return true/false
     */
    public boolean checkErrorMessage(String Content, String testcase) {
        Log.info("Started Check Error Messages keyword");
        boolean flag = true;
        try{
            // Wait to show Error Message popup
            driver.Wait(10, OR.signin.getLocator("ErrorMessage"));
            MobileElement ErrorMessage = driver.findElement(OR.signin.getLocator("ErrorMessage"));
            String text = ErrorMessage.getAttribute("value");
            Log.info("Passed - Error message shows: "+ text);
            Assert.assertEquals(text,Content);
        }catch (AssertionError aE) {
            Log.info("Failed - Assert Error: " + aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        closeErrorMessage();
        return flag;
    }

    /**
     * Check page current is expected page
     * @param numPage (1, 2, 3,)
     */
    public boolean checkPageIndicator (int numPage, String title, String description, String testCase){
        Log.info("Started checkPageIndicator keyword");
        boolean flag = true;
        try{
            // Check exist before find element
            driver.isElementExist(OR.signin.getLocator("pageIndicator"));

            // Get current page value from pageIndicator
            MobileElement pageIndicator = driver.findElement(OR.signin.getLocator("pageIndicator"));
            String page = pageIndicator.getAttribute("value");
            //Log.info(page);
            int pageNum = Integer.parseInt(page.split(" ")[1]);

            // Get text of page
            String intro_page = String.valueOf(pageNum-1);
            MobileElement titlePage = driver.findElement(OR.signin.getLocatorDynamic("txtTitle", new String[]{intro_page}));
            String txtTitle = titlePage.getAttribute("value");

            // Get text of page
            MobileElement descriptionPage = driver.findElement(OR.signin.getLocatorDynamic("txtDescription", new String[]{intro_page}));
            String txtDescription = descriptionPage.getAttribute("value");

            // Compare when page 1 - 3
            if(numPage == pageNum){
                Assert.assertEquals(title,txtTitle);
                Assert.assertEquals(description,txtDescription);
                Log.info("Passed - Current page: "+pageNum+"\nTitle: "+txtTitle+"\nDescription: "+txtDescription);
            }else {
                Log.info("Failed - Current page incorrect - "+pageNum);
                driver.captureScreenShot(testCase);
                flag = false;
            }
        }catch (Exception e){
            Log.info("Failed - Assert Error: " + e.getMessage());
        }
        Log.info("Ended checkPageIndicator keyword");
        return flag;
    }
}