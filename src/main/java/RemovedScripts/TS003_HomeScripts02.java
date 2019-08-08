package RemovedScripts;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.ExcelUtils;
import Support.Log;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TS003_HomeScripts02 extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;
    EmailKeywords email;
    CollectionKeywords collection;
    TodoKeywords todo;
    ContactKeywords contact;
    NoteKeywords note;
    AgendaKeywords agenda;
    boolean flag = true;
    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;

    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String validEmail = "khuong001@123flo.com";

    Date currentDate;
    String today;

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device,false);
        SetUp(capabilities);
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        email = new EmailKeywords(this);
        collection = new CollectionKeywords(this);
        todo = new TodoKeywords(this);
        contact = new ContactKeywords(this);
        agenda = new AgendaKeywords(this);
        note = new NoteKeywords(this);

        // Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);
        common.closeStartFlo();
    }

    /**
     * Check All Icon in Bottom Bar
     */
    String testCase1 = "TS003_HomeScripts02_Home1_checkAllIconInBottomBar";
    @Test
    public void TS003_HomeScripts02_Home1_checkAllIconInBottomBar(){
        Log.startTestCase(testCase1);
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Customization");
        common.closeSendNotification(true);
        settings.setLightHeaderFooter(false);
        common.swipeBottomLeftToRight();
        boolean flag1 = common.checkIconInBottomBar(false,testCase1);
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Customization");
        common.closeSendNotification(true);
        settings.setLightHeaderFooter(true);
        common.swipeBottomLeftToRight();
        flag = common.checkIconInBottomBar(true,testCase1);
        Log.endTestCase(testCase1);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Check Item In Popup Global
     */
    String testCase2 = "TS003_HomeScripts02_Home2_checkItemInPopupGlobal";
    @Test
    public void TS003_HomeScripts02_Home2_checkItemInPopupGlobal() {
        Log.startTestCase(testCase2);
        // Check All Item after click to Plus Button on Top
        flag = home.checkItemInPopupGlobal(testCase2);
        Log.endTestCase(testCase2);
        if (!flag)
            Assert.fail("Test case is failed");
    }

    /**
     * Check Collection on Top after Filter In other View
     */
    String testCase3a = "TS003_HomeScripts02_Home3_checkCollectionTopAfterFilterView";
    @Test (dataProvider = "LocalFilterCollection")
    public void TS003_HomeScripts02_Home3_checkCollectionTopAfterFilterView(String testCase, String view, String collect ) {
        Log.startTestCase(testCase3a);
        //1. Go to View
        common.clickViewBottomBar(view);
        //2. Filter Collection
        if(view.equals("Email")){
            email.navigateToImapFolder(collect);
        }
        else if(view.equals("Collection")){
            collection.filterByType(collect,"Events");
        }
        else common.selectLocalFilter(collect);
        //3. Check Collection on Top Recent Collection
        common.swipeBottomLeftToRight();
        flag = home.checkCollectionOnTop(collect, testCase);
        //4. Go to View
        common.clickViewBottomBar(view);
        //5. Filter  General Collection
        if(view.equals("Email")){
            email.navigateToImapFolder("General");
        }else if(view.equals("Collection")){
            collection.filterByType("General","Events");
        }else common.selectLocalFilter("General");
        common.swipeBottomLeftToRight();
        //6. Check General Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("General", testCase);
        Log.endTestCase(testCase3a);
        if (!(flag&flag1))
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] LocalFilterCollection() throws Exception{
        Object[][] testObjArray = ExcelUtils.getTableArray(Constants.GENERAL_DATA,"LocalFilterCollection");
        return (testObjArray);
    }

    /**
     * Check Collection on Top Recent Collection after add new To.do
     */
    String testCase3 = "TS003_HomeScripts02_Home4_checkCollectionTopAfterAddTodo";
    String subject1 = "HomeScripts02_Home4";
    @Test
    public void TS003_HomeScripts02_Home4_checkCollectionTopAfterAddTodo() {
        Log.startTestCase(testCase3);
        //1. Go to To.Do View
        common.clickViewBottomBar("ToDo");
        //2. Add To.do with collection Work
        todo.addFullTodo(subject1,false,false,"None","","","Work",true);
        home.clickTopLeftbutton("Home");
        //3. Check Work Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("Work",testCase3);
        //4. Go to To.do view
        common.clickViewBottomBar("ToDo");
        //5. Delete Contact
        todo.deleteTodoBySwipe(subject1,true);
        //6. Filter to "General" Collection
        common.selectLocalFilter("General");
        home.clickTopLeftbutton("Home");
        //7. Check General Collection on Top Recent Collection
        flag = home.checkCollectionOnTop("General",testCase3);
        Log.endTestCase(testCase3);
        if (!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Check Collection on Top Recent Collection after add new Contact
     */
    String testCase4 = "TS003_HomeScripts02_Home5_checkCollectionTopAfterAddContact";
    String fName = "Home";
    String lName = "HomeScripts02_Home5";
    @Test
    public void TS003_HomeScripts02_Home5_checkCollectionTopAfterAddContact() {
        Log.startTestCase(testCase4);
        //1. Go to Contact View
        common.clickViewBottomBar("Contact");
        //2. Add To.do with collection Work
        contact.addNewContact(fName, lName,"None","","",true,false);
        common.addMoveToCollection(true,"Move","Work");
        common.clickElementButton("Done");
        home.clickTopLeftbutton("Home");
        //3. Check Work Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("Work",testCase4);
        //4. Go to To.do view
        common.clickViewBottomBar("Contact");
        //5. Delete Contact
        contact.deleteContactBySwipe(fName, lName,true);
        //6. Filter to "General" Collection
        common.selectLocalFilter("General");
        home.clickTopLeftbutton("Home");
        //7. Check General Collection on Top Recent Collection
        flag = home.checkCollectionOnTop("General",testCase4);
        Log.endTestCase(testCase4);
        if (!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Check Collection on Top Recent Collection after add new Note
     */
    String testCase5 = "TS003_HomeScripts02_Home6_checkCollectionTopAfterAddNote";
    String subject2 = "HomeScripts02_Home6";
    @Test
    public void TS003_HomeScripts02_Home6_checkCollectionTopAfterAddNote() {
        Log.startTestCase(testCase5);
        //1. Go to Note View
        common.clickViewBottomBar("Note");
        //2. Add new Note with collection Work
        note.addNewNote(subject2,"",false,"None");
        common.addMoveToCollection(true,"Move","Work");
        common.clickElementButton("Done");

        // Generate String today
        currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

        home.clickTopLeftbutton("Home");
        //3. Check Work Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("Work",testCase5);
        //4. Go to To.do view
        common.clickViewBottomBar("Note");
        //5. Delete Contact
        note.deleteNoteBySwipe(today, subject2,"OK");
        //6. Filter to "General" Collection
        common.selectLocalFilter("General");
        home.clickTopLeftbutton("Home");
        //7. Check General Collection on Top Recent Collection
        flag = home.checkCollectionOnTop("General",testCase5);
        Log.endTestCase(testCase5);
        if (!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**
     * Check Collection on Top Recent Collection after add new Email
     */
    String testCase6 = "TS003_HomeScripts02_Home7_checkCollectionTopAfterAddEmail";
    String subject3 = "HomeScripts02_Home7";
    @Test
    public void TS003_HomeScripts02_Home7_checkCollectionTopAfterAddEmail() {
        Log.startTestCase(testCase6);
        //1. Go to Email View
        common.clickViewBottomBar("Email");
        //2. Send with collection Sample
        common.openNewItemView();
        email.sendEmail("",validEmail,"","",subject3,"","","Move","Sample",true);
        home.clickTopLeftbutton("Home");
        //3. Check Sample Collection on Top Recent Collection
        Wait(10);
        boolean flag1 = home.checkCollectionOnTop("Sample",testCase6);
        //4. Go to To.do view
        common.clickViewBottomBar("Email");
        email.navigateToImapFolder("All Sent");
        email.deleteEmailBySwipe(validEmail,subject3,true);
//        email.navigateToImapFolder("All Inboxes");
//        email.deleteEmailBySwipe(validEmail,subject3,true);
        //5.Clean Up Filter to "General" collection
        email.navigateToImapFolder("General");
        //6. Clean Up Back to Home, check "General" is Top Recent Collection
        home.clickTopLeftbutton("Home");
        flag = home.checkCollectionOnTop("General",testCase6);
        Log.endTestCase(testCase6);
        if (!(flag&flag1))
            Assert.fail("Test case is failed");
    }

//    /**
//     * Check Collection on Top Recent Collection after add new Event
//     */
//    String testCase7 = "TS003_HomeScripts02_Home8_checkCollectionTopAfterAddEvent";
//    String subject4 = "HomeScripts02_Home8";
//    @Test
//    public void TS003_HomeScripts02_Home8_checkCollectionTopAfterAddEvent() {
//        Log.startTestCase(testCase7);
//        //1. Go to Email View
//        home.clickViewBottomBar("Event");
//        //2. Add New Event with collection Sample
//        agenda.addSimpleEvent("Plus Icon",subject4,true,"None");
//        common.addMoveToCollection(true,"Move","Sample");
//        common.clickElementButton("Done");
//        common.swipeBottomLeftToRight();
//        //3. Check Sample Collection on Top Recent Collection
//        boolean flag1 = home.checkCollectionOnTop("Sample",testCase7);
//        //4. Go to To.do view
//        home.clickViewBottomBar("Event");
//        agenda.deleteEventBySwipe(today,"All Day",subject4,true);
//        //5.Clean Up Filter to "General" collection
//        common.selectLocalFilter("General");
//        //6. Clean Up Back to Home, check "General" is Top Recent Collection
//        common.swipeBottomLeftToRight();
//        flag = home.checkCollectionOnTop("General",testCase7);
//        Log.endTestCase(testCase4);
//        if (!(flag&flag1))
//            Assert.fail("Test case is failed");
//    }



    @AfterClass
    public void cleanUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete contact email
//        home.clickViewBottomBar("Contact");
//        contact.filterByType("Top","All");
//        common.openItem("Contact", validEmail);
//        common.trashItemByDetails(true);

        // Clean trash
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();

        // Close app
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
