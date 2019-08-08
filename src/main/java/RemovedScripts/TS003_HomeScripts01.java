package RemovedScripts;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS003_HomeScripts01 extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;

    boolean flag = true;
    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;

    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device,false);
        SetUp(capabilities);
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);

        // Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);
//        home.closeStartFlo();
    }

    /**
     * Check Start Flo popup on Home Screen
     * Check slogen on popup
     * Check all tips on popup
     * Check Start Flo button
     */
    String testCase = "TS003_HomeScripts01_Home1_checkStartFlo";
    String slogan = "Flo is simple and easy to learn.";
    String fristTip = "These tips will get you started.";
    String secondTip = "Tap the icons below to see emails, meetings, notes, contacts, etc.";

//    @Test
//    public void TS003_HomeScripts01_Home1_checkStartFlo(){
//        Log.startTestCase(testCase);
//        flag = common.checkStartFlo(slogan, fristTip, secondTip, testCase);
//        Log.endTestCase(testCase);
//        if(flag == false)
//            Assert.fail("Test case is failed");
//    }

    String testCase1 = "TS003_HomeScripts01_Home2_checkRecentCollectionSectionOnHome";
    /**
     * Settings > Customization > Turn ON "Recent Collections on Home"
     * Check Recent Collection is shown on Home Screen
     * Settings > Customization > turn OFF "Recent Collections on Home"
     * NOT show Recent Collections on Home
     */
    @Test
    public void TS003_HomeScripts01_Home2_checkRecentCollectionSectionOnHome() {
        Log.startTestCase(testCase1);
        // Open Settings Screen
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Customization");

        //Turn OFF Recent Collections on Home toggle in Settings
//        settings.setCollectionOnHome(false);
//        settings.backToHomeScreen();
        //Check Recent Collection not show on Home Screen
        boolean flag1 = home.checkRecentCollection(false, testCase1);

        // Go to setting again
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Customization");

        //Turn ON Recent Collections on Home toggle in Settings
//        settings.setCollectionOnHome(true);
//        settings.backToHomeScreen();
        //Check Recent Collection show on Home Screen
        boolean flag2 = home.checkRecentCollection(true,testCase1);
        Log.endTestCase(testCase1);

        if ((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Open new item screen from Bottom Bar
     * Tap on Email, Event, To.Do, Contact, Note icon in Bottom bar > Check View is opened
     * Tap again on Email, Event, To.Do, Contact, Note icon in Bottom bar > Check New item view
     */
    String testCase2a = "TS003_HomeScripts01_Home3_OpenNewItemViewFromBottomBar";
    @Test (dataProvider = "BottomBarData")
    public void TS003_HomeScripts01_Home3_OpenNewItemViewFromBottomBar(String TestCase, String ViewButton, String ViewScreen, String NewItemScreen){
        Log.startTestCase(testCase2a);
        // Click icon in bottom bar
        common.clickViewBottomBar(ViewButton);
        // Check each opened view
        boolean flag1 = common.checkViewScreenDisplay(ViewScreen, TestCase);
        // click icon in bottom bar again to open New Create Screen
        common.clickViewBottomBar(ViewButton);
        // Check New Create Screen is opening
        boolean flag2 = common.checkViewScreenDisplay(NewItemScreen, TestCase);
        // Back to Home screen
        common.clickElementButton("Cancel");
        home.clickTopLeftbutton("Home");
        Log.endTestCase(testCase2a);
        if ((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] BottomBarData() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.GENERAL_DATA,"BottomBar");
        return (testObjArray);
    }

    /**
     * Open Collection view from Bottom Bar
     * Tap on Collection icon in Bottom bar > Check Colelction View
     */
    String testCase2 = "TS003_HomeScripts01_Home4_OpenCollectionViewFromBottomBar";
    @Test
    public void TS003_HomeScripts01_Home4_OpenCollectionViewFromBottomBar(){
        Log.startTestCase(testCase2);
        // Click icon in bottom bar
        common.clickViewBottomBar("Collection");
        // Close Introduce Collection View
        common.closeCollectionIntro();
        // Check the opened view
        flag = common.checkViewScreenDisplay("More", testCase2);
        // Back to Home screen
        home.clickTopLeftbutton("Home");
        Log.endTestCase(testCase2);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Open Collection screen from Recent Collections list
     * Check Collection screen is opening
     * Return to Home Screen then check others Collection.
     */
    String testCase3 = "TS003_HomeScripts01_Home5_openRecentCollectionsScreen";
    @Test (dataProvider = "RecentCollections")
    public void TS003_HomeScripts01_Home5_openRecentCollectionsScreen(String testCase, String nameOfCollection, String recentCollectionScreen){
        Log.startTestCase(testCase3);
        // Open Collection screen in Recent Collection list
        home.openRecentCollection(nameOfCollection);
        boolean flag1 = home.checkRecentCollectionsScreen(recentCollectionScreen,testCase);
        // Tap on arrow icon to return to Home Screen from Collection screen
        home.clickTopLeftbutton("Arrow");
        // Check Home screen
        boolean flag2 = home.checkHomeScreenDisplay(testCase);
        Log.endTestCase(testCase3);
        if ((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] RecentCollections () throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.GENERAL_DATA,"RecentCollections");
        return (testObjArray);
    }

    /**
     * Open new item screen from Plus icon
     * Tap on "+" icon, tap on "Email" icon, Open compose Email Screen
     * Tap on "+" icon, tap on "Event" icon, Open Create New Event Screen
     * Tap on "+" icon, tap on "To.do" icon, Open Create New To.Do Screen
     * Tap on "+" icon, tap on "Contact" icon, Open Create New Contact Screen
     * Tap on "+" icon, tap on "Note" icon, Open Create New Note Screen
     * Tap on "+" icon, tap on "Collection" icon, Open Create New Collection Screen
     */
    String testCase6 = "TS003_HomeScripts01_Home6_OpenNewItemViewFromPlusIcon";
    @Test ( dataProvider = "PlusIconData")
    public void TS003_HomeScripts01_Home6_OpenNewItemViewFromPlusIcon (String testCase, String viewButton, String viewScreen){
        Log.startTestCase(testCase6);

        home.openNewScreenPlusIcon(viewButton);
        // Check New Create item screen is opening.
        flag = common.checkViewScreenDisplay(viewScreen, testCase);
        common.clickElementButton("Cancel");

        Log.endTestCase(testCase6);
        if (flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] PlusIconData() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.GENERAL_DATA,"PlusIcon");
        return (testObjArray);
    }

    /**
     * Swipe left to right on each view to back Home
     * Open Object Detail view from Bottom Bar
     * Swipe Left to Right on Bottom bar to back to Home Screen
     */
    String testCase7 = "TS003_HomeScripts01_Home7_SwipeLefttoRightBackHome";
    @Test (dataProvider = "SwipeActionData")
    public void TS003_HomeScripts01_Home7_SwipeLefttoRightBackHome(String TestCase, String ViewButton, String ViewScreen){
        Log.startTestCase(testCase7);

        // Click icon in bottom bar
        common.clickViewBottomBar(ViewButton);
        // Check each opened view
        boolean flag1 = common.checkViewScreenDisplay(ViewScreen, TestCase);
        // Action Swipe Left to Right - back to Home Screen
        home.swipeBottomViewToBackHome();
        boolean flag2 = home.checkHomeScreenDisplay(TestCase);

        Log.endTestCase(testCase7);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] SwipeActionData() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.GENERAL_DATA,"SwipeAction");
        return (testObjArray);
    }

    /**
     * Check clock section on Home Screen
     * Tap on Space Clock
     * Check Create New Event Screen
     */
    String testCase4= "TS003_HomeScripts01_Home8_CheckClockSection";
    @Test
    public void TS003_HomeScripts01_Home8_CheckClockSection(){
        Log.startTestCase(testCase4);
       flag = home.checkClockonHome(testCase4);
        // Create new Event from section Clock
        home.openEventFromClock();
        // Check Create New Event Screen.
        boolean flag1 = common.checkViewScreenDisplay("New Event",testCase4);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase4);
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
