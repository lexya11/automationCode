package RemovedScripts;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TS003_HomeScripts03 extends SetupServer {
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

    // Generate String today
    Date currentDate = new Date();
    String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
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
        note = new NoteKeywords(this);
        agenda = new AgendaKeywords(this);

        // Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);
        common.closeStartFlo();
    }

    /**
     * Check Collection on Top after edit Collection
     */
    String testCase1 = "TS003_HomeScripts03_Home1_checkCollectionOnTopAfterEditCollection";
    @Test
    public void TS003_HomeScripts03_Home1_checkCollectionOnTopAfterEditCollection() {
        Log.startTestCase(testCase1);
        //1. Go to Settings > Collections
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Collections");
        //2. Edit Work Collection
        settings.editCollection("Work","","Violet","","","",false,true);
//        settings.backToHomeScreen();
        //3. Check Work Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("Work",testCase1);
        //4. Go to Settings > Collections
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Collections");
        //5. Edit General Collection
        settings.editCollection("General","","Violet","","","",false,true);
//        settings.backToHomeScreen();
        //6. Check Work Collection on Top Recent Collection
        flag = home.checkCollectionOnTop("General",testCase1);
        Log.endTestCase(testCase1);
        if (!(flag&flag1))
            Assert.fail("Test case is failed - Bug 26325 ");
    }

    /**
     * Check Collection on Top after add new Collection
     */
    String testCase2 = "TS003_HomeScripts03_Home2_checkCollectionOnTopAfterAddCollection";
    String subject1 = "HomeScripts03_Home2";
    @Test
    public void TS003_HomeScripts03_Home2_checkCollectionOnTopAfterAddCollection() {
        Log.startTestCase(testCase2);
        //1. Go to Settings > Collections
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Collections");
        //2. Edit Work Collection
        collection.addNewCollection("setting",subject1,"Violet","None","",false,true);
//        settings.backToHomeScreen();
        //3. Check Work Collection on Top Recent Collection
        flag = home.checkCollectionOnTop(subject1, testCase2);
        //4. Go to Settings > Collections
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Collections");
        //5. delete Collection
        settings.deleteCollectionBySwipe(subject1,"",true);
//        settings.backToHomeScreen();
        //6. Check Work Collection on Top Recent Collection
        Log.endTestCase(testCase2);
        if (!flag)
            Assert.fail("Test case is failed - Bug 26325 ");
    }

    /**
     * Check Collection on Top Recent Collection after move To.do to collection
     */
    String testCase3 = "TS003_HomeScripts03_Home3_checkCollectionOnTopAfterMoveTodo";
    String subject2 = "HomeScripts03_Home3";
    @Test
    public void TS003_HomeScripts03_Home3_checkCollectionOnTopAfterMoveTodo(){
        Log.startTestCase(testCase3);
        //1. Go to To.do View
        common.clickViewBottomBar("ToDo");
        //2. Add new To.Do with "Home" Collection
        todo.addFullTodo(subject2,false,false,"","","","Home",true);
        common.swipeBottomLeftToRight();
        //3. Check Home Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("Home",testCase3);
        common.clickViewBottomBar("ToDo");
        //4. Move To.Do to "General" Collection
        todo.openCollectionBySwipe(subject2);
        common.addMoveToCollection(false,"Move","General");
        common.swipeBottomLeftToRight();
        //5. Check General on Top Recent Collection
        flag = home.checkCollectionOnTop("General",testCase3);
        common.clickViewBottomBar("ToDo");
        //6. Clean Up: Delete To.Do
        todo.deleteTodoBySwipe(subject2,true);
        common.swipeBottomLeftToRight();
        Log.endTestCase(testCase3);
        if(!(flag&flag1))
            Assert.fail("Test case is failed.");
    }

//    /**
//     * Check Collection On Top Recent Collection after move Event to Collection
//     */
//    String testCase4 = "TS003_HomeScripts03_Home4_checkCollectionOnTopAfterMoveEvent";
//    String subject3 = "HomeScripts03_Home4";
//    @Test
//    public void TS003_HomeScripts03_Home4_checkCollectionOnTopAfterMoveEvent(){
//        Log.startTestCase(testCase4);
//        //1. Go to Event View
//        home.clickViewBottomBar("Event");
//        //2. Add new Event with "Home" Collection
//        agenda.addSimpleEvent("Plus Icon",subject3,true,"None");
//        common.addMoveToCollection(true,"Move","Home");
//        common.clickElementButton("Done");
//        common.swipeBottomLeftToRight();
//        //3. Check Home Collection on Top Recent Collection
//        boolean flag1 = home.checkCollectionOnTop("Home",testCase4);
//        home.clickViewBottomBar("Event");
//        //4. Move To.Do to "General" Collection
//        agenda.openCollectionBySwipe(today,"All Day",subject3);
//        common.addMoveToCollection(false,"Move","General");
//        common.swipeBottomLeftToRight();
//        //5. Check General on Top Recent Collection
//        flag = home.checkCollectionOnTop("General",testCase4);
//        home.clickViewBottomBar("Event");
//        //6. Clean Up: Delete Event
//        agenda.deleteEventBySwipe(today,"All Day", subject3,true);
//        common.swipeBottomLeftToRight();
//        Log.endTestCase(testCase4);
//        if(!(flag&flag1))
//            Assert.fail("Test case is failed.");
//    }

    /**
     * Check Collection On Top Recent Collection after move Contact to Collection
     */
    String testCase5 = "TS003_HomeScripts03_Home5_checkCollectionOnTopAfterMoveContact";
    String fName = "Home";
    String lName = "HomeScripts03_Home5";
    @Test
    public void TS003_HomeScripts03_Home5_checkCollectionOnTopAfterMoveContact(){
        Log.startTestCase(testCase5);
        //1. Go to Contact View
        common.clickViewBottomBar("Contact");
        //2. Add Contact with collection Home
        contact.addNewContact(fName, lName,"None","","",true,false);
        common.addMoveToCollection(true,"Move","Home");
        common.clickElementButton("Done");
        common.swipeBottomLeftToRight();
        //3. Check Home Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("Home",testCase5);
        common.clickViewBottomBar("Contact");
        //4. Move To.Do to "General" Collection
        contact.openCollectionBySwipe(fName, lName);
        common.addMoveToCollection(false,"Move","General");
        common.swipeBottomLeftToRight();
        //5. Check General on Top Recent Collection
        flag = home.checkCollectionOnTop("General",testCase5);
        common.clickViewBottomBar("Contact");
        //6. Clean Up: Delete Contact
        contact.deleteContactBySwipe(fName, lName,true);
        common.swipeBottomLeftToRight();
        Log.endTestCase(testCase5);
        if(!(flag&flag1))
            Assert.fail("Test case is failed.");
    }

    /**
     * Check Collection On Top after move Note to Collection
     */
    String testCase6 = "TS003_HomeScripts03_Home6_checkCollectionOnTopAfterMoveNote";
    String subject4 = "HomeScripts03_Home6";
    @Test
    public void TS003_HomeScripts03_Home6_checkCollectionOnTopAfterMoveNote(){
        Log.startTestCase(testCase6);
        //1. Go to Contact View
        common.clickViewBottomBar("Note");
        //2. Add Contact with collection Home
        note.addNewNote(subject4,"",false,"None");
        common.addMoveToCollection(true,"Move","Work");
        common.clickElementButton("Done");
        common.swipeBottomLeftToRight();
        //3. Check Home Collection on Top Recent Collection
        boolean flag1 = home.checkCollectionOnTop("Work",testCase6);
        common.clickViewBottomBar("Note");
        //4. Move To.Do to "General" Collection
        Wait(5);
        note.openCollectionBySwipe(today, subject4);
        common.addMoveToCollection(false,"Move","General");
        common.swipeBottomLeftToRight();
        //5. Check General on Top Recent Collection
        flag = home.checkCollectionOnTop("General",testCase6);
        common.clickViewBottomBar("Note");
        //6. Clean Up: Delete Contact
        note.deleteNoteBySwipe(today, subject4,"OK");
        common.swipeBottomLeftToRight();
        Log.endTestCase(testCase6);
        if(!(flag&flag1))
            Assert.fail("Test case is failed.");
    }

    /**
     * Check Collection On Top Recent Collection after move Email to Collection
     */
    String testCase7 = "TS003_HomeScripts03_Home7_checkCollectionOnTopAfterMoveEmail";
    String subject5 = "HomeScripts03_Home7";
    @Test
    public void TS003_HomeScripts03_Home7_checkCollectionOnTopAfterMoveEmail(){
        Log.startTestCase(testCase7);
        //1. Go to Contact View
        common.clickViewBottomBar("Email");
        //2. Add Contact with collection Home
        common.openNewItemView();
        email.sendEmail("",validEmail,"","",subject5,"","","Move","Sample",true);
        Wait(10);
        common.swipeBottomLeftToRight();
        //3. Check Home Collection on Top Recent Collection
        Wait(10);
        boolean flag1 = home.checkCollectionOnTop("Sample",testCase6);
        common.clickViewBottomBar("Email");
        //4. Move To.Do to "General" Collection
        email.navigateToImapFolder("All Sent");
        email.openCollectionBySwipe(validEmail, subject5);
        common.addMoveToCollection(false,"Move","General");
        common.swipeBottomLeftToRight();
        //5. Check General on Top Recent Collection
        Wait(10);
        flag = home.checkCollectionOnTop("General",testCase6);
        common.clickViewBottomBar("Email");
        //6. Clean Up: Delete Contact
//        email.navigateToImapFolder("General");
//        email.deleteEmailBySwipe(validEmail, subject5,true);
//        email.navigateToImapFolder("All Inboxes");
//        email.deleteEmailBySwipe(validEmail, subject5,true);
        common.swipeBottomLeftToRight();
        Log.endTestCase(testCase6);
        if(!(flag&flag1))
            Assert.fail("Test case is failed.");
    }

    @AfterClass
    public void cleanUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Trash all contact
//        home.clickViewBottomBar("Contact");
//        contact.filterByType("Top","All");
//        common.openItem("Contact", validEmail);
//        common.trashItemByDetails(true);

        // Empty collection trash
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
