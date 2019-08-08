package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS003_GeneralScripts extends SetupServer {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;

    boolean flag = true;
    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;

    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    @BeforeClass
    public void setUp() {
        Support.Log.startTestCase("Set Up " + this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device, false);
        SetUp(capabilities);
        settings = new SettingsKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);

        // Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);
    }

    /**
     * Check Start Flo popup on Home Screen
     * Check slogen on popup
     * Check all tips on popup
     * Check Start Flo button
     */
    String testCase1 = "TS003_GeneralScripts01_General1_checkStartFlo";
    String slogan = "Flo is simple and easy to learn.";
    String fristTip = "These tips will get you started.";
    String secondTip = "Tap the icons below to see emails, meetings, notes, contacts, etc.";

//    @Test
//    public void TS003_GeneralScripts_General1_checkStartFlo(){
//        Log.startTestCase(testCase1);
//        flag = common.checkStartFlo(slogan, fristTip, secondTip, testCase1);
//        Log.endTestCase(testCase1);
//        if(flag == false)
//            Assert.fail("Test case is failed");
//    }

    /**
     * Open View from Bottom Bar
     * Tap on Email, Event, To.Do, Contact, Note Collection icon in Bottom bar > Check View is opened
     */
    String testCase2 = "TS003_GeneralScripts_General2_OpenViewFromBottomBar";
    @Test (dataProvider = "BottomBarData")
    public void TS003_GeneralScripts_General2_OpenViewFromBottomBar(String TestCase, String ViewButton, String ViewScreen){
        Log.startTestCase(testCase2);
        // Click icon in bottom bar
        common.clickViewBottomBar(ViewButton);
        // Check each opened view
        boolean flag = common.checkViewScreenDisplay(ViewScreen, TestCase);
        Log.endTestCase(testCase2);
        if (!flag)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] BottomBarData() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.GENERAL_DATA,"BottomBar");
        return (testObjArray);
    }

    /**
     * Check All Icon in Bottom Bar
     */
    String testCase3 = "TS003_GeneralScripts01_General3_checkAllIconInBottomBar";
    @Test
    public void TS003_GeneralScripts_General3_checkAllIconInBottomBar(){
        Log.startTestCase(testCase3);
        // Open Settings View
        common.openSettingsView();
        // Open Settings > Customization
        settings.clickSettingOptionList("Customization");
        common.closeSendNotification(true);
        // Turn OFF Light Header and Footer
        settings.setLightHeaderFooter(false);
        // Check icon in Bottom Bar
        boolean flag1 = common.checkIconInBottomBar(false,testCase3);
        // Turn ON Light Header and Footer
        settings.setLightHeaderFooter(true);
        // Check icon in Bottom Bar
        flag = common.checkIconInBottomBar(true,testCase3);
        Log.endTestCase(testCase3);
        if(!(flag&flag1))
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
