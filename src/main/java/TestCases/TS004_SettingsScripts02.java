package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.SetupServer;
import org.junit.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS004_SettingsScripts02 extends SetupServer {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    CommonKeywords common;
    SignInKeywords signin;
    CollectionKeywords collection;
    AgendaKeywords agenda;
    TodoKeywords todo;

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
        todo = new TodoKeywords(this);
        //Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();
        common.openSettingsView();
        settings.clickSettingOptionList("Collections");
    }

    /**************************
     * Setting > Collection > Add New Collection
     */

    String testCase01 = "TS004_SettingsScripts02_SettingCollection01_addNewCollection";
    @Test
    public void TS004_SettingsScripts02_SettingCollection01_addNewCollection(){
        Log.startTestCase(testCase01);
        //1.Add new collection
        collection.addNewCollection("setting","Collection1","Red","None","",false,true);
        //2. Check Collection
        flag = settings.checkCollectionItemDetails("Collection1","Red","None",false,testCase01);
        Log.endTestCase(testCase01);
        if(!flag)
          Assert.fail("Test case is failed");
    }

    /**
     * Add New Collection With Existed Name
     */
    String testCase02 = "TS004_SettingsScripts02_SettingCollection02_addExistedCollection";
    String errorStatus = " is existing, please use another name";
    @Test
    public void TS004_SettingsScripts02_SettingCollection02_addExistedCollection(){
        Log.startTestCase(testCase02);
        //1.Add new collection
        collection.addNewCollection("setting","Collection1","Blue","None","",false,true);
        String error = "\"Collection1\"" + errorStatus;
        //2. Check Error Pop up
        boolean flag = collection.checkExistCollectionErrorMessage(error,testCase02);
        Log.endTestCase(testCase02);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    /**
     * Add New Child Collection
     */
    String testCase03 = "TS004_SettingsScripts02_SettingCollection03_addNewChildCollection";
    @Test void TS004_SettingsScripts02_SettingCollection03_addNewChildCollection(){
        Log.startTestCase(testCase03);
        //1.Add new collection
        collection.addNewCollection("setting","Collection2","Orange","Collection1","",false,true);
        //2. Check Collection
        flag = settings.checkCollectionItemDetails("Collection2","Orange","Collection1",false,testCase03);
        Log.endTestCase(testCase03);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    /**
     * Add New Child Collection Of Child Collection
     */
    String testCase04 = "TS004_SettingsScripts02_SettingCollection04_addNewChildCollectionOfChildCollection";
    @Test void TS004_SettingsScripts02_SettingCollection04_addNewChildCollectionOfChildCollection(){
        Log.startTestCase(testCase04);
        //1.Add new collection
        collection.addNewCollection("setting","Collection3","Blue","Collection2","Collection1",false,true);
        //2. Check Collection
        flag = settings.checkCollectionItemDetails("Collection3","Blue","Collection2",false,testCase04);
        Log.endTestCase(testCase04);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    /**
     * Edit Title of Collection
     */
    String testCase05 = "TS004_SettingsScripts02_SettingCollection05_editTitleOfCollection";
    @Test void TS004_SettingsScripts02_SettingCollection05_editTitleOfCollection(){
        Log.startTestCase(testCase05);
        //1.Edit title of collection
        settings.editCollection("Collection3","Collection4","","Collection1,Collection2","","",false,true);
        //2. Check collection
        flag = settings.checkCollectionItemDetails("Collection4","Blue","Collection2",false,testCase05);
        Log.endTestCase(testCase05);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    /**
     * Edit Color of Collection
     */
    String testCase06 = "TS004_SettingsScripts02_SettingCollection06_editColorOfCollection";
    @Test void TS004_SettingsScripts02_SettingCollection06_editColorOfCollection(){
        Log.startTestCase(testCase06);
        //1.Edit color of collection
        settings.editCollection("Collection4","","Green","Collection1,Collection2","","",false,true);
        //2. Check collection
        flag = settings.checkCollectionItemDetails("Collection4","Green","Collection2",false,testCase06);
        Log.endTestCase(testCase06);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    /**
     * Edit Parent of Collection
     */
    String testCase07 = "TS004_SettingsScripts02_SettingCollection07_editParentOfCollection";
    @Test void TS004_SettingsScripts02_SettingCollection07_editParentOfCollection(){
        Log.startTestCase(testCase07);
        //1.Edit parent of collection
        settings.editCollection("Collection4","","","Collection1,Collection2","None","",false,true);
        //2. Check collection
        flag = settings.checkCollectionItemDetails("Collection4","Green","None",false,testCase07);
        Log.endTestCase(testCase07);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    /**
     * Make Default Collection
     */
    String testCase08 = "TS004_SettingsScripts02_SettingCollection08_editMakeDefaultCollection";
    @Test void TS004_SettingsScripts02_SettingCollection08_editMakeDefaultCollection(){
        Log.startTestCase(testCase08);
        //1.Edit collection is default collection
        settings.editCollection("Collection4","","","","","",true,true);
        //2. Check Collection is default from view list screen
        Boolean flag1 = settings.checkIsDefaultCollection("Collection4",testCase08);
        //3. Check detail Collection
        flag = settings.checkCollectionItemDetails("Collection4","Green","None",true,testCase08);
        Log.endTestCase(testCase08);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Delete Collection From Details View
     */
    String testCase09 = "TS004_SettingsScripts02_SettingCollection09_deleteCollectionFromDetailsView";
    @Test void TS004_SettingsScripts02_SettingCollection09_deleteCollectionFromDetailsView(){
        Log.startTestCase(testCase09);
        //1. Add new collection
        collection.addNewCollection("setting","Collection5","Blue","None","",false,true);
        //2. Check detail collection
        flag = settings.checkCollectionItemDetails("Collection5","Blue","None",false,testCase09);
        //3. Delete collection from detail screen
        settings.deleteCollectionFromDetailsView("Collection5","",true);
        //4.Check collection is not existed from view list screen
        boolean flag1 = settings.checkCollectionExist("Collection5","Not Exist",testCase09);
        Log.endTestCase(testCase09);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Delete Collection By Swipe
     */
    String testCase10 = "TS004_SettingsScripts02_SettingCollection10_deleteCollectionBySwipe";
    @Test void TS004_SettingsScripts02_SettingCollection10_deleteCollectionBySwipe(){
        Log.startTestCase(testCase10);
        //1. Add new collection
        collection.addNewCollection("setting","Collection6","Orange","None","",false,true);
        //2. Check detail collection
        flag = settings.checkCollectionItemDetails("Collection6","Orange","None",false,testCase10);
        //3. Delete collection by swipe
        settings.deleteCollectionBySwipe("Collection6","",true);
        //4.Check collection is not existed from view list screen
        boolean flag1 = settings.checkCollectionExist("Collection6","Not Exist",testCase10);
        Log.endTestCase(testCase10);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Delete Collection, Check Child Collection Will Be Deleted
     */
    String testCase11 = "TS004_SettingsScripts02_SettingCollection11_deleteCollectionChildCollectionBeDeleted";
    @Test void TS004_SettingsScripts02_SettingCollection11_deleteCollectionChildCollectionBeDeleted(){
        Log.startTestCase(testCase11);
        //1. Delete parent collection
        settings.deleteCollectionBySwipe("Collection1","",true);
        //2. Check collection is not existed
        boolean flag1 = settings.checkCollectionExist("Collection1","Not Exist",testCase11);
        //3. Check child collection is not existed too
        flag = settings.checkCollectionExist("Collection2","Not Exist", testCase11);
        Log.endTestCase(testCase11);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Check Default Collection Can Not Be Deleted
     */
    String testCase12 = "TS004_SettingsScripts02_SettingCollection12_checkDefaultCollectionCanNotBeDeleted";
    @Test void TS004_SettingsScripts02_SettingCollection12_checkDefaultCollectionCanNotBeDeleted(){
        Log.startTestCase(testCase12);
        //1. Check default collection can not be deleted
        flag = settings.checkDefaultCollectionNotBeDelete("Collection4", testCase12);
        //2. Set General is default collection
        settings.editCollection("General","","","","","",true,true);
        //3. Delete Collection 4 by swipe
        settings.deleteCollectionBySwipe("Collection4","",true);
        //4. Collection 4 is not existed
        boolean flag1 = settings.checkCollectionExist("Collection4","Not Exist",testCase12);
        Log.endTestCase(testCase12);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Set Default Collection ,Check Default Collection Is Set For New Item
     */
    String testCase13 = "TS004_SettingsScripts02_SettingCollection13_checkDefaultCollectionIsAvailableInNewItemView" ;
    @Test void TS004_SettingsScripts02_SettingCollection13_checkDefaultCollectionIsAvailableInNewItemView(){
        Log.startTestCase(testCase13);
        //1.Add new collection: Collection 7 is default collection
        collection.addNewCollection("setting","Collection7","Blue","None","",true,true);
        //2. Check new Event View, new Event is belong to "Collection 7"
//        home.clickViewBottomBar("Event");
//        boolean flag1 = agenda.checkNewEventView("", "","","Collection7",true,testCase13);
        //3. Check new To.do View, new To.do is belong to "Collection 7"
        common.clickViewBottomBar("ToDo");
        common.openNewItemView();
        boolean flag2 = todo.checkNewTodoView("","","","","","Collection7", true, testCase13);
        //4. Back to Settings-->Collections
        common.openSettingsView();
        settings.clickSettingOptionList("Collections");
        Log.endTestCase(testCase13);
        if(!flag2)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // delete collection
        settings.setDefaultCollection("General");
        settings.deleteCollectionBySwipe("Collection7","",true);
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
