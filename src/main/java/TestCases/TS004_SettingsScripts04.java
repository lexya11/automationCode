package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.junit.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS004_SettingsScripts04 extends SetupServer {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    CommonKeywords common;
    SignInKeywords signin;
    CollectionKeywords collection;
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
        collection = new CollectionKeywords(this);
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
     * Setting > Calendars > Default Event Duration
     */
//    String testCase1 = "TS004_SettingsScripts04_SettingCalendars1_setDefaultEventDuration";
//    @Test(dataProvider = "DefaultEventDuration")
//    public void TS004_SettingsScripts04_SettingCalendars1_setDefaultEventDuration(String testCase,String value){
//        Log.startTestCase(testCase1);
//        //1.Set Default Event Duration
//        settings.setDefaultEventDuration(value);
//        //2. Open New Event View, check Default Event Duration
//        common.clickViewBottomBar("Event");
//        common.openNewItemView();
//        flag = agenda.checkNewEventView(value, "","","",true,testCase);
//        //3. Back to Setting > Calendars
//        common.swipeBottomRightToLeft();
//        settings.clickSettingOptionList("Calendars");
//        Log.endTestCase(testCase1);
//        if(!flag)
//            Assert.fail("Test case is failed");
//    }
//    @DataProvider
//    public Object[][] DefaultEventDuration() throws Exception {
//        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"DefaultEventDuration");
//        return testObjectArray;
//    }

    /**************************
     * Setting > Calendars > Show Past Events in Collection View
     */
    String testCase2 = "TS004_SettingsScripts04_SettingCalendars2_setShowPastEventsInCollectionView";
    String toggle = "Show Past Events in Collection View";
    @Test
    public void TS004_SettingsScripts04_SettingCalendars2_setShowPastEventsInCollectionView(){
        Log.startTestCase(testCase2);
        //1. Set toggle Show Past Events in Collection View : true
        settings.setShowPastEventInCollectionView(true);
        //2. Check toggle
        boolean flag1 = settings.checkSwitchToggle(toggle,"ON",testCase2);
        //3. Set toggle Show Past Events in Collection View : false
        settings.setShowPastEventInCollectionView(false);
        //4. Check toggle
        flag = settings.checkSwitchToggle(toggle,"OFF",testCase2);
        Log.endTestCase(testCase2);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > Calendars > Working Hours
     */
    String testCase3 = "TS004_SettingsScripts04_SettingCalendars3_setWorkingHours";
    String date = "Tuesday";
    String startTime = "08:15 AM";
    String endTime = "08:30 PM";
    @Test
    public void TS004_SettingsScripts04_SettingCalendars3_setWorkingHours(){
        Log.startTestCase(testCase3);
        //1. Set Working Hours
        settings.setWorkingHours("Tuesday",startTime,endTime);
        //2. Check Working Hours
        flag = settings.checkWorkingHours("Tuesday",startTime,endTime, testCase3);
        Log.endTestCase(testCase3);
        if(!flag)
            Assert.fail("Test case is failed");
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
