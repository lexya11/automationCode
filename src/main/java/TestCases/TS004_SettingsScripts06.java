package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.ExcelUtils;
import Support.Log;
import Support.SetupServer;
import org.junit.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS004_SettingsScripts06 extends SetupServer {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    CommonKeywords common;
    SignInKeywords signin;
    AgendaKeywords agenda;
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
        signin = new SignInKeywords(this);
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        common = new CommonKeywords(this);
        agenda = new AgendaKeywords(this);

        //Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();
        common.openSettingsView();
        settings.clickSettingOptionList("Calendars");
        common.closeSendNotification(true);
    }

    /**************************
     * Setting > Calendars > Normal Event Alert
     */
    String testCase1 = "TS004_SettingsScripts06_SettingCalendars1_setNormalEventAlert";
    @Test(dataProvider = "NormalEventAlert")
    public void TS004_SettingsScripts06_SettingCalendars1_setNormalEventAlert(String testCase,String value){
        Log.startTestCase(testCase1);
        //1.Set Normal Event Alert
        settings.setNormalEventAlert(value);
        //2. Open New Event View, check Normal Event Alert
        common.clickViewBottomBar("Event");
        common.openNewItemView();
        flag = agenda.checkNewEventView("", "",value,"",true,testCase);
        //3. Back to Setting > Calendars
        Wait(1);
        common.openSettingsView();
        settings.clickSettingOptionList("Calendars");
        Log.endTestCase(testCase1);
        if(!flag)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] NormalEventAlert() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"NormalEventAlert");
        return testObjectArray;
    }

    /**************************
     * Setting > Calendars > All Day Event Alert
     */
    String testCase2 = "TS004_SettingsScripts06_SettingCalendars2_setAllDayEventAlert";
    @Test(dataProvider = "AllDayEventAlert")
    public void TS004_SettingsScripts06_SettingCalendars2_setAllDayEventAlert(String testCase,String value){
        Log.startTestCase(testCase2);
        //1.Set All Day Event Alert
        settings.setAllDayEventAlert(value);
        //2. Open New Event View, check All Day Event Alert
        common.clickViewBottomBar("Event");
        common.openNewItemView();
        flag = agenda.checkNewEventView("", value,"","",true,testCase);
        //3. Back to Setting > Calendars
        Wait(1);
        common.openSettingsView();
        settings.clickSettingOptionList("Calendars");
        Log.endTestCase(testCase2);
        if(!flag)
            Assert.fail("Test case is failed");
    }

    @DataProvider
    public Object[][] AllDayEventAlert() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"AllDayEventAlert");
        return testObjectArray;
    }

    @AfterClass
    public void clearUp(){
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
