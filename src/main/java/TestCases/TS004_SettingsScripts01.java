package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.CommonKeywords;
import Keywords.HomeKeywords;
import Keywords.SettingsKeywords;
import Keywords.SignInKeywords;
import Support.ExcelUtils;
import Support.Log;
import Support.SetupServer;
import org.junit.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS004_SettingsScripts01 extends SetupServer  {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    CommonKeywords common;
    SignInKeywords signin;

    // Test case variables
    boolean flag = true;
    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device,false);
        SetUp(capabilities);
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        common = new CommonKeywords(this);
        signin = new SignInKeywords(this);

        //Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();
        common.openSettingsView();
    }
    /**************************
        Check Setting Details Screen Display
    */
    String testCase1 = "TS004_SettingsScripts01_Settings1_checkSettingDetailsScreenDisplay";
    // Using data driven for all Setting details
    @Test(dataProvider = "CheckSettingScreen")
    public void TS004_SettingsScripts01_Settings1_checkSettingDetailsScreenDisplay(String testcase, String option, String settingsView){
        Log.startTestCase(testCase1);
        // 1. Open Settings > Option in settings
        settings.clickSettingOptionList(option);
        // 2 Check Screen option is opening
        flag = settings.checkSettingScreen(option, testcase);
        // 3. Back to Settings View
        common.clickElementButton(settingsView);
        Log.endTestCase(testCase1);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] CheckSettingScreen() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"CheckSettingScreen");
        return (testObjArray);
    }

    /**************************
     * Setting > General > Open About Screen
     */
    String testcase2 = "TS004_SettingsScripts01_Settings2_checkAboutScreen";
    @Test
    public void TS004_SettingsScripts01_Settings2_checkAboutScreen(){
        String slogan = "Less Work, More Flo.";
        String url = "floware.com";

        Log.startTestCase(testcase2);
        // 1. Open Settings > General View
        settings.clickSettingOptionList("General");
        // 2. Open 'About' View
        settings.clickSettingOptionList("About");
        // 3. Check 'About' View
        flag = settings.checkGeneralAbout(slogan, url, buildVersion, testcase2);
        // 4. Back to Settings > General View
        common.clickElementButton("General");
        Log.endTestCase(testcase2);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Check Settings 'Show All Warning' toggle
     */
    String testcase3 = "TS004_SettingsScripts01_Settings3_showAllWarnings";
    String toggle = "Show All Warnings";
    @Test
    public void TS004_SettingsScripts01_Settings3_showAllWarnings(){
        Log.startTestCase(testcase3);
        // 1. Switch ON toggle 'Show All Warnings'
        settings.setShowAllWarnings(true);
        // 2. Check status toggle 'Show All Warning' is ON
        boolean flag1 = settings.checkSwitchToggle(toggle,"ON",testcase3);
        // 3. Switch OFF toggle 'Show All Warnings'
        settings.setShowAllWarnings(false);
        // 4. Check status toggle 'Show All Warning' is OFF
        flag = settings.checkSwitchToggle(toggle,"OFF",testcase3);
        Log.endTestCase(testcase3);
        if (!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    /**
     *  Check Settings 'General Sync' toggle
     */
    String testcase4 = "TS004_SettingsScripts01_Settings4_GeneralSync";
    @Test (dataProvider = "GeneralSyncData")
    public void TS004_SettingsScripts01_Settings4_GeneralSync(String TestCase, String Options, String Value){
        Log.startTestCase(testcase4);
        // 1. Open General > Sync View
        settings.clickSettingOptionList(Options);
        // 2. Set value for option 'Sync'
        settings.clickSettingOptionList(Value);
        // 3. Back to 'General' View
        common.clickElementButton("General");
        // 4. Check value setup for option 'Sync'
        flag = settings.checkSettingValue(Options, Value, TestCase);
        Log.endTestCase(testcase4);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] GeneralSyncData() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"GeneralSyncData");
        return (testObjArray);
    }

    /**
     *  Check Settings 'General Alert Snooze' toggle
     */
    String testcase5 = "TS004_SettingsScripts01_Settings5_GeneralAlertSnooze";
    @Test (dataProvider = "GeneralAlertSnoozeData")
    public void TS004_SettingsScripts01_Settings5_GeneralAlertSnooze(String TestCase, String Options, String Value){
        Log.startTestCase(testcase5);
        // 1. Open General > Alert Snooze View
        settings.clickSettingOptionList(Options);
        // 2. Set value for option 'Alert Snooze'
        settings.clickSettingOptionList(Value);
        // 3. Back to 'General' View
        common.clickElementButton("General");
        // 4. Check value setup for option 'Alert Snooze'
        flag = settings.checkSettingValue(Options, Value, TestCase);
        Log.endTestCase(testcase5);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] GeneralAlertSnoozeData() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"GeneralAlertSnoozeData");
        return (testObjArray);
    }

    @AfterClass
    public void cleanUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        driver.closeApp();
    }

    @AfterSuite
    public void sendMail(ITestContext context){
        if(flag && !Constants.RUN_XML) {
            sendEmail.getResults(context);
            sendEmail.sendGmail(this.getClass().getSimpleName(), device, buildVersion, Constants.EMAIL_TO, Constants.EMAIL_CC);
        }
    }
}
