package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.junit.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS004_SettingsScripts03 extends SetupServer {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    CommonKeywords common;
    SignInKeywords signin;
    CollectionKeywords collection;
    TodoKeywords todo;
    NoteKeywords note;
    AgendaKeywords agenda;
    EmailKeywords email;

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
        todo = new TodoKeywords(this);
        note = new NoteKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);

        //Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();
        common.openSettingsView();
        settings.clickSettingOptionList("Customization");
    }


    /**************************
     * Setting > Customization > Change BackGround Image
     */

    String testCase1 = "TS004_SettingsScripts03_SettingCustomization1_changeBackGround";
    String image = "Image 4";
    @Test
    public void TS004_SettingsScripts03_SettingCustomization1_changeBackGround(){
        Log.startTestCase(testCase1);
        settings.changeBackgroundImage(image);
        flag = settings.checkBackgroundImage(image,testCase1);
        Log.endTestCase(testCase1);
        if(!flag)
            Assert.fail("Test case is failed");
    }

    /**
     * Set Header and Footer
     */
    String testCase2 = "TS004_SettingsScripts03_SettingCustomization2_setLightHeaderAndFooter";
    String toggle1 = "Light Header and Footer";
    @Test
    public void TS004_SettingsScripts03_SettingCustomization2_setLightHeaderAndFooter(){
        Log.startTestCase(testCase2);
        settings.setLightHeaderFooter(true);
        boolean flag1 = settings.checkSwitchToggle(toggle1,"ON",testCase2);
        settings.setLightHeaderFooter(false);
        flag = settings.checkSwitchToggle(toggle1,"OFF",testCase2);
        Log.endTestCase(testCase2);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }


    /**
     * Set Show Star in List View
     */
    String testCase5 = "TS004_SettingsScripts03_SettingCustomization5_setShowStarInListView";
    String toggle3 = "Show Stars in List View";
    String title1 = "SettingsScripts03_Todo 1";
    @Test
    public void TS004_SettingsScripts03_SettingCustomization5_setShowStarInListView(){
        Log.startTestCase(testCase5);
        settings.setShowStarInListView(true);
        boolean flag1 = settings.checkSwitchToggle(toggle3,"ON",testCase5);
        common.clickViewBottomBar("ToDo");
        todo.addQuickTodo(title1,"None");
        boolean flag2 = common.checkStarIsVisible(title1,true,testCase5);
        // set Show Star in List View false
        common.openSettingsView();
        settings.clickSettingOptionList("Customization");
        settings.setShowStarInListView(false);
        boolean flag3 = settings.checkSwitchToggle(toggle3,"OFF",testCase5);
        common.clickViewBottomBar("ToDo");
        flag = common.checkStarIsVisible(title1,false,testCase5);
        common.openSettingsView();
        settings.clickSettingOptionList("Customization");
        Log.endTestCase(testCase5);
        if(!(flag&flag1&flag2&flag3))
            Assert.fail("Test case is failed");
    }

    /**
     * After Set Show Star in List View, Set Star For Item
     */
    String testCase6 = "TS004_SettingsScripts03_SettingCustomization6_setStarForItem";
    String title2 = "SettingsScripts03_Todo 2";
    @Test
    public void TS004_SettingsScripts03_SettingCustomization6_setStarForItem() {
        Log.startTestCase(testCase6);
        settings.setShowStarInListView(true);
        boolean flag1 = settings.checkSwitchToggle(toggle3, "ON", testCase6);
        common.clickViewBottomBar("ToDo");
        todo.filterByType("Top","All");
        todo.addQuickTodo(title2, "None");
        boolean flag2 = common.checkStarIsVisible(title2,true,testCase6);
        common.clickStarItem(title2,"Star");
        flag = todo.checkTodoItemView(title2,"Normal","None",false,false,true,false,testCase6);
        common.openSettingsView();
        settings.clickSettingOptionList("Customization");
        Log.endTestCase(testCase6);
        if (!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    /**
     * After Set Show Star in List View, Set UnStar For Item
     */
    String testCase7 = "TS004_SettingsScripts03_SettingCustomization7_setUnStarForItem";
    String title3 = "SettingsScripts03_Todo 3";
    @Test
    public void TS004_SettingsScripts03_SettingCustomization7_setUnStarForItem() {
        Log.startTestCase(testCase7);
        settings.setShowStarInListView(true);
        boolean flag1 = settings.checkSwitchToggle(toggle3, "ON", testCase7);
        common.clickViewBottomBar("ToDo");
        todo.filterByType("Top","Star");
        todo.addQuickTodo(title3, "None");
        todo.filterByType("Top","All");
        common.clickStarItem(title3,"UnStar");
        flag = todo.checkTodoItemView(title3,"Normal","None",false,false,false,false,testCase7);
        common.openSettingsView();
        settings.clickSettingOptionList("Customization");
        Log.endTestCase(testCase7);
        if (!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * set Show Navigation Bar When Idle
     */
    String testCase8 = "TS004_SettingsScripts03_SettingCustomization8_setShowNavigationBarWhenIdle";
    @Test(dataProvider = "ShowNavigationBar")
    public void TS004_SettingsScripts03_SettingCustomization8_setShowNavigationBarWhenIdle(String testCase, String value){
        Log.startTestCase(testCase8);
        settings.setShowNavigationBarWhenIdle(value);
        flag = common.checkItemExist(value,"Exist",testCase);
        Log.endTestCase(testCase8);
        if(!flag)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] ShowNavigationBar() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"ShowNavigationBar");
        return testObjectArray;
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        common.clickViewBottomBar("ToDo");
        todo.selectMultiTodo(title1,true,"Trash");
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();
        Wait(5);
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
