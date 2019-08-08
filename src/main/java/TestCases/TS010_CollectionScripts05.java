package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

public class TS010_CollectionScripts05 extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    AgendaKeywords agenda;
    SignInKeywords signin;
    NoteKeywords note;
    TodoKeywords todo;
    EmailKeywords email;
    CommonKeywords common;
    SettingsKeywords settings;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "CollectionScripts05_Event A";
    String titleNote1 = "CollectionScripts05_Note A";
    String titleTodo1 = titleNote1;
    String subjectEmail = "CollectionScripts05_Email A";

    String floAddress = "khuong001@123flo.com";



    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        agenda = new AgendaKeywords(this);
        todo = new TodoKeywords(this);
        email = new EmailKeywords(this);
        note = new NoteKeywords(this);
        signin = new SignInKeywords(this);
        settings = new SettingsKeywords(this);
        capabilities = new MobileCapabilities(device, false);
        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);
                // Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username, password, true);
                // Close Start Flo popup
                common.closeStartFlo();
                common.openSettingsView();
                settings.clickSettingOptionList("Notes");
                settings.setOpenTodoCreateFromNote(false);
                // Pre-test: create new Note (Note A)
                common.clickViewBottomBar("Note");
                common.selectLocalFilter("Work");
                common.openNewItemView();
                note.addNewNote( titleNote1, "Content", true, "Done");
                // Open Collection view
                common.clickViewBottomBar("Collection");
                statusLogin = true;
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    // TC: Swipe left to right Note and create linked Email
    String testCase1 = "TS010_CollectionScripts05_Collection1_sendEmailBySwipeOnNote";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts05_Collection1_sendEmailBySwipeOnNote() {
        Log.startTestCase(testCase1);
        // 1. Filter by a Collection (Work) and Notes
        collection.filterByType("Work", "Note");
        // 2. Swipe left to right Note (Note A) and click Email icon
        collection.swipeLeftToRightItem("Note", titleNote1, "Email");
        // 3. Input info send an Email
        email.sendEmail("",floAddress,"","",subjectEmail,"Content","",
                "Move","Work",true);
        // 4. Filter type by Emails
        collection.filterByType("", "Email");
        // 5. Check Email belong Collection (Work)
        flag = collection.checkItemBelongCollection("Email", subjectEmail, testCase1);
        // 6. Open detail of Email
        common.openItem("Email", subjectEmail);
        // 7. Check Note A existing in detail
        boolean flag1 = common.checkItemExist(titleNote1, "Exist", testCase1);
        // 8. Delete email in detail (x2)
        common.trashItemByDetails(true);
        Log.endTestCase(testCase1);
        if (!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe left to right Note and create linked Event
    String testCase2 = "TS010_CollectionScripts05_Collection2_createNewEventBySwipeOnNote";

    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts05_Collection2_createNewEventBySwipeOnNote() {
        Log.startTestCase(testCase2);
        // 1. Swipe left to right note (Note A) and click Event icon
        collection.filterByType("Work", "Note");
        collection.swipeLeftToRightItem("Note", titleNote1, "Event");
        // 2. Input to add new Event (Event A)
        agenda.addSimpleEvent( titleEvent1, false, "Done");
        // 3. Filter type by Events
        collection.filterByType("General", "Event");
        // 4. Check new Event A is added belong Collection
        flag = collection.checkItemBelongCollection("Event", titleEvent1, testCase2);
        // 5. Open detail of event (Event A)
        common.openItem("Event", titleEvent1);
        // 6. Check link has new note (Note A)
        boolean flag1 = agenda.checkEventItemDetails(titleEvent1, false, "", "General",
                titleNote1, testCase2);
        // 7. Delete event (Event A) in detail
        common.trashItemByDetails(true);
        // 8. Filter by Notes
        collection.filterByType("Work", "Note");
        Log.endTestCase(testCase2);
        if (!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe left to right Note and create linked To.Do
    String testCase3 = "TS010_CollectionScripts05_Collection3_createNewTodoBySwipeOnNote";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts05_Collection3_createNewTodoBySwipeOnNote() {
        Log.startTestCase(testCase3);
        // 1. Swipe left to right note (Note A) and click To.do icon
        collection.swipeLeftToRightItem("Note", titleNote1, "ToDo");
        // 2. Filter by To.Do's
        collection.filterByType("General", "ToDo");
        // 3. Check new To.Do add belong Collection
        flag = collection.checkItemBelongCollection("ToDo", titleTodo1, testCase3);
        // 4. Open detail of new To.do
        common.openItem("ToDo", titleTodo1);
        // 5. Check detail of new To.do has Note A in Link section
        boolean flag1 = todo.checkTodoItemDetails(titleTodo1, "None", false, "Short",
                "None", "General", titleNote1, testCase3);
        // 6. Delete new To.do in detail
        common.trashItemByDetails(true);
        // 7. Back to Collection view and filter by 'Notes'
        collection.filterByType("Work", "Note");
        Log.endTestCase(testCase3);
        if (!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Note and move collection
    String testCase4 = "TS010_CollectionScripts05_Collection4_moveNoteToCollectionBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts05_Collection4_moveNoteToCollectionBySwipe() {
        Log.startTestCase(testCase4);
        // 1. Swipe left to right Note (Note A) and click Collection icon
        collection.swipeRightToLeftItem("Note", titleNote1, "Collection");
        // 2. Move collection to others (Play)
        common.addMoveToCollection(false, "Move", "Play");
        // 3. Check Note not existing (Note A)
        flag = common.checkItemExist(titleNote1, "Not Exist", testCase4);
        // 4. Filter by Collection moved (Play)
        collection.filterByType("Play", "Note");
        // 5. Open detail of Note (Note A)
        common.openItem("Note", titleNote1);
        // 6. Check collection is moved
        boolean flag1 = note.checkNoteItemDetails(titleNote1, "",true, "","Play",
                "", false, testCase4);
        // 7. Back to Collection view
        common.clickElementButton("Play");
        Log.endTestCase(testCase4);
        if (!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Note and add collections
    String testCase5 = "TS010_CollectionScripts05_Collection5_addNoteToCollectionsBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts05_Collection5_addNoteToCollectionsBySwipe(){
        Log.startTestCase(testCase5);
        // 1. Swipe left to right Note (Note A) and click Collection icon
        collection.swipeRightToLeftItem("Note",titleNote1,"Collection");
        // 2. Add more collection Ex: General, Work
        common.addMoveToCollection(false,"Add","General,Sample");
        // 3. Open detail of Note (Note A)
        common.openItem("Note",titleNote1);
        // 4. Check collection is added
        flag = note.checkNoteItemDetails(titleNote1,"",true,"","Play,General,Sample","",false,testCase5);
        // 5. Back to Collection view
        common.clickElementButton("Play");
        Log.endTestCase(testCase5);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Note and delete
    String testCase6 = "TS010_CollectionScripts05_Collection6_deleteNoteBySwipe";
    String deleteMessage = "This Note will be moved to your Trash Collection.";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts05_Collection6_deleteNoteBySwipe(){
        Log.startTestCase(testCase6);
        // 1. Swipe right to left on (Contact A) and click Trash icon
        collection.swipeRightToLeftItem("Note",titleNote1,"Trash");
        // 2. Check message delete
        flag = common.checkTrashMessage(deleteMessage,"OK",testCase5);
        Log.endTestCase(testCase6);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Empty trash collection
        collection.emptyTrashCollection();
        // close app
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

