package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS002_SignUpScripts02 extends SetupServer{
    // Test case variables
    MobileCapabilities capabilities;
    SignUpKeywords signup;
    SignInKeywords signin;
    boolean flag = true;

    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String errorMessage = "This email name is not allowed.";

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device, false);
        super.SetUp(capabilities);
        signup = new SignUpKeywords(this);
        signin = new SignInKeywords(this);
        signin.skipIntroduce();
        signup.openSignUpScreen();
    }

    /**
     * Restriction of Sensitive Flo Account name
     */
    String Testcase = "TS002_SignUpScripts02_SignUp1_SensitiveFloAccountName";
    @Test(dataProvider = "DataExcel")
    public void TS002_SignUpScripts02_SignUp1_SensitiveFloAccountName(String AccountName, String password){
        Log.startTestCase(Testcase);
        signup.signUp(AccountName, password,true);
        flag = signup.checkErrorMessage(errorMessage, Testcase);
        Log.endTestCase(Testcase);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] DataExcel() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SIGNIN_UP_DATA,"SensitiveWords");
        return testObjectArray;
    }

    /**
     * Restriction of Bad Words Flo Account name
     */
    String Testcase1 = "TS002_SignUpScripts02_SignUp2_BadWordsFloAccountName";
    @Test(dataProvider = "DataExcel1")
    public void TS002_SignUpScripts02_SignUp2_BadWordsFloAccountName(String AccountName, String password){
        Log.startTestCase(Testcase1);

        AccountName = AccountName + RandomStringUtils.randomAlphabetic(10);

        signup.signUp(AccountName, password,true);
        flag = signup.checkErrorMessage(errorMessage, Testcase1);
        Log.endTestCase(Testcase1);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] DataExcel1() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SIGNIN_UP_DATA,"BadWords");
        return testObjectArray;
    }

    /**
     * Restriction of Invalid Flo Account name
     */
    String Testcase3 = "TS002_SignUpScripts02_SignUp3_BadWordsFloAccountName";
    String errorMessage1 = "Only letters, numbers, underscores, and one period are allowed";
    @Test(dataProvider = "DataExcel2")
    public void TS002_SignUpScripts02_SignUp3_BadWordsFloAccountName(String AccountName){
        Log.startTestCase(Testcase3);

        String generateAccount = RandomStringUtils.randomAlphabetic(7) + AccountName;

        signup.signUp(generateAccount,"a12345",true);
        flag = signup.checkErrorMessage(errorMessage1, Testcase3);
        Log.endTestCase(Testcase3);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] DataExcel2() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SIGNIN_UP_DATA,"InvalidWords");
        return testObjectArray;
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
