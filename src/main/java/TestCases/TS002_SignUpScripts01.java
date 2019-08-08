package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import java.util.Random;

public class TS002_SignUpScripts01 extends SetupServer{
    // Test case variables
    MobileCapabilities capabilities;
    SignUpKeywords signup;
    SignInKeywords signin;
    boolean flag = true;
    Random rd = new Random();
    String username = "automation_" + RandomStringUtils.randomAlphabetic(5) + rd.nextInt(10000);
    String password = Constants.PASSWORD;

    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device, false);
        super.SetUp(capabilities);
        signup = new SignUpKeywords(this);
        signin = new SignInKeywords(this);
        signin.skipIntroduce();
    }

    /**
     * Check Sign Up Screen is displayed
     */
    String Testcase1 = "TS002_SignUpScripts01_SignUp1_CheckSignUpScreenDisplay";
    @Test
    public void TS002_SignUpScripts01_SignUp1_CheckSignUpScreenDisplay() {
        Log.startTestCase(Testcase1);
        signup.openSignUpScreen();
        flag = signup.checkSignUpDisplay(Testcase1);
        Log.endTestCase(Testcase1);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Check Sign Up Screen is displayed
     */
//    String Testcase1 = "TS002_SignUpScripts01_SignUp1_CheckSignUpScreenDisplay";
//    @Test
//    public void TS002_SignUpScripts01_SignUp1_CheckSignUpScreenDisplay() {
//        Log.startTestCase(Testcase1);
//        signup.openSignUpScreen();
//        flag = signup.checkSignUpDisplay(Testcase1);
//        Log.endTestCase(Testcase1);
//        if (flag == false)
//            Assert.fail("Test case is failed");
//    }

    /**
     * Sign up with username contains the dot
     */
    String Testcase2 = "TS002_SignUpScripts01_SignUp2_CheckErrorMessage";
    @Test
    public void TS002_SignUpScripts01_SignUp2_CheckErrorMessage(){
        Log.startTestCase(Testcase2);
        String Username = "flo.automation";

        signup.signUp(Username, password,true);
        flag = signup.checkSignUpSuccess(Testcase2);

        // Close message b/c bug 0025571
        signup.closeErrorMessage();

        Log.endTestCase(Testcase2);
        if (flag == false)
            Assert.fail("Test case is failed - bug 0025571: Unable to sign up account with username contains the dot");
    }

    /**
     * Check error message is displayed correctly
     * TC2: Sign Up password < 6 characters
     * TC3: Sign Up password > 32 characters
     * TC4: Input Invalid Username
     * TC5: Not input Username
     * TC6: Input Username Existing
     */
    String Testcase3 = "TS002_SignUpScripts01_SignUp3_CheckErrorMessage";
    @Test(dataProvider = "DataExcel")
    public void TS002_SignUpScripts01_SignUp3_CheckErrorMessage(String TestCase, String Username, String Password, String ErrorMessage){
        Log.startTestCase(Testcase3);
        signup.signUp(Username, Password,true);
        flag = signup.checkErrorMessage(ErrorMessage,TestCase);
        Log.endTestCase(Testcase3);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] DataExcel() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SIGNIN_UP_DATA,"SignUpFail");
        return testObjectArray;
    }

    /**
     * Test Sign Up Successfully
     */
    String Testcase4 = "TS002_SignUpScripts01_SignUp4_SignUpSuccess";
    @Test
    public void TS002_SignUpScripts01_SignUp4_SignUpSuccess () {
        Log.startTestCase(Testcase4);
        signup.signUp(username, password,true);
        System.out.println(username);
        flag = signup.checkSignUpSuccess(Testcase4);
        Log.endTestCase(Testcase4);
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
