package TestCases;

import Environments.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import Keywords.*;

public class TS001_SignInScripts extends SetupServer{
    // Test case variables
    MobileCapabilities capabilities;
    SignInKeywords signin;
    CommonKeywords common;

    String username_Valid = Constants.USERNAME;
    String password_Valid = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    boolean flag = true;

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device, false);
        SetUp(capabilities);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
    }

    // Check Introduction pages
    String textTitlePage1 = "LESS WORK, MORE FLO";
    String textTitlePage2 = "COLLECTIONS";
    String textTitlePage3 = "LINKING";

    String textDescPage1 = "Mail, Calendar, ToDo's, Contacts and Notes. Perfectly Integrated!";
    String textDescPage2 = "Related Emails, Notes, Events, ToDo's, Contacts, Website, and Files\n" +
            "Find things instantly in one place!";
    String textDescPage3 = "Email to a ToDo. Note to an Event.\n" +
            "Anything to Anything!";

    String Testcase0 = "TS001_SignInScripts_SignIn1_checkIntroductionPage";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS001_SignInScripts_SignIn1_checkIntroductionPage() {
        Log.startTestCase(Testcase0);
        // Swipe and check each page
        boolean flag1 = signin.checkPageIndicator(1, textTitlePage1, textDescPage1, Testcase0);
        common.swipeCenterRightToLeft();
        boolean flag2 = signin.checkPageIndicator(2, textTitlePage2, textDescPage2, Testcase0);
        common.swipeCenterRightToLeft();
        boolean flag3 = signin.checkPageIndicator(3, textTitlePage3, textDescPage3, Testcase0);

        Log.endTestCase(Testcase0);
        if (!(flag1 & flag2 & flag3))
            Assert.fail("Test case is failed");
    }

    // Check button btnExistingUsers & btnNewUser_SignUp enable
    String Testcase1 = "TS001_SignInScripts_SignIn2_CheckEnableButton";
    @Test
    public void TS001_SignInScripts_SignIn2_CheckEnableButton() {
        Log.startTestCase(Testcase1);
        signin.skipIntroduce();
        // Check button Existing User
        boolean flag1 = common.checkButtonEnable("btnExistingUsers",Testcase1);
        // Check button New User - Sign Up
        flag = common.checkButtonEnable("btnNewUser_SignUp",Testcase1);
        Log.endTestCase(Testcase1);
        if (!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // Check Sign In Screen is displayed
    String Testcase2 = "TS001_SignInScripts_SignIn3_CheckSignInScreenDisplay";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS001_SignInScripts_SignIn3_CheckSignInScreenDisplay() {
        Log.startTestCase(Testcase2);
        signin.openLoginScreen();
        flag = signin.checkSignInScreenDisplay(Testcase2);
        Log.endTestCase(Testcase2);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    // Sign In With Null Username
    String Testcase3 = "TS001_SignInScripts_SignIn4_NullUsername";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS001_SignInScripts_SignIn4_NullUsername(){
        Log.startTestCase(Testcase3);
        signin.login("","123123", false);
        flag = signin.checkSignInNotEnable(Testcase3);
        Log.endTestCase(Testcase3);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    // Sign In With Null Password
    String Testcase4 = "TS001_SignInScripts_SignIn5_NullPassword";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS001_SignInScripts_SignIn5_NullPassword(){
        Log.startTestCase(Testcase4);
        signin.login("bi_test02","", false);
        flag = signin.checkSignInNotEnable(Testcase4);
        Log.endTestCase(Testcase4);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * TC1: Login wrong username
     * TC2: Login password < 6 characters
     * TC3: Login password > 32 characters
     * TC4: Login invalid username
     * TC5: Login invalid username
     * TC6: Login wrong password
     */
    String Testcase5 = "TS001_SignInScripts_SignIn6_CheckErrorMessage";
    @Test(dataProvider = "DataExcel")
    public void TS001_SignInScripts_SignIn6_CheckErrorMessage(String Testcase, String Username, String Password, String ErrorMessage) {
        Log.startTestCase(Testcase5);
        // Input Username & Password
        signin.login(Username, Password,true);
        // Check error message
        flag = signin.checkErrorMessage(ErrorMessage, Testcase);
        Log.endTestCase(Testcase5);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] DataExcel() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.SIGNIN_UP_DATA,"SignInFail");
        return (testObjArray);
    }

    // Sign In Success
    String Testcase6 = "TS001_SignInScripts_SignIn7_SignInSuccess";
    @Test
    public void TS001_SignInScripts_SignIn7_SignInSuccess()  {
        Log.startTestCase(Testcase6);
        signin.login(username_Valid,password_Valid, true);
        flag = signin.checkSignInSuccess(Testcase6);
        Log.endTestCase(Testcase6);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void cleanUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        driver.closeApp();
    }

    @AfterSuite
    public void sendMail(ITestContext context){
        if(flag && !Constants.RUN_XML) {
            Log.startTestCase("Send Email Report");
            sendEmail.getResults(context);
            sendEmail.sendGmail(this.getClass().getSimpleName(), device, buildVersion, Constants.EMAIL_TO, Constants.EMAIL_CC);
        }
    }
}