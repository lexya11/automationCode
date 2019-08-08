package TestCases;

import Environments.MobileCapabilities;
import Environments.Constants;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TS009_NoteScripts03 extends SetupServer {
    MobileCapabilities capabilities;

    SignInKeywords signin;
    CommonKeywords common;
    NoteKeywords note;
    AgendaKeywords agenda;
    TodoKeywords todo;
    ContactKeywords contact;
    CollectionKeywords collection;
    SettingsKeywords settings;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;

    String fromEmail = username + Constants.MAIL_SERVER;
    String toEmail = "khuong001@123flo.com";

    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    boolean flag = true;
    String noteTime;
    String noteDateTime;
    String today;

    String titleNote1 = "NoteScripts03_Note01";

    String titleEmail = "NoteScripts03_Note01_Email";
    String titleEvent = "NoteScripts03_Note02_Event";

    String titleEmail2 = "NoteScripts03_Note06_Email";
    String titleEvent2 = "NoteScripts03_Note07_Event";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        contact = new ContactKeywords(this);
        note = new NoteKeywords(this);
        agenda = new AgendaKeywords(this);
        todo = new TodoKeywords(this);
        collection = new CollectionKeywords(this);
        settings = new SettingsKeywords(this);

        capabilities = new MobileCapabilities(device, false);
        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);
                // Generate String today
                Date currentDate = new Date();
                today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
                Log.info("String today from system - "+today);

                //Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username, password, true);

                // Close start Flo popup
                common.closeStartFlo();

                // Pre-test: set OpenTodoCreateFromNote setting - OFF
                common.openSettingsView();
                settings.clickSettingOptionList("Notes");
                settings.setOpenTodoCreateFromNote(false);

                // Add note (Test 1)
                common.clickViewBottomBar("Note");
                common.clearView();
                common.openNewItemView();
                noteTime = note.addNewNote(titleNote1,"Normal",false, "Done");

                // Generate dateTime of Note
                String NoteTime = (noteTime.length() == 7) ? "0" + noteTime : noteTime;
                int hour = Integer.parseInt(NoteTime.substring(0,2));
                String strTime = (hour > 9) ? NoteTime.substring(0,5) : NoteTime.substring(1,5);
                String AMPM = NoteTime.substring(6);
                noteDateTime = today + " at " + strTime + AMPM;
                statusLogin = true;
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }


    }

    /**
     * Swipe left to right note and create other linked email
     */
    String testCase1 ="TS009_NoteScripts03_Note01_createOtherLinkedEmailBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note01_createOtherLinkedEmailBySwipe(){
        Support.Log.startTestCase(testCase1);
        // 1. Swipe left to right note (Test 1) and click email icon
        // 2. Input info and send email
        note.sendEmailBySwipe(today, titleNote1, fromEmail, toEmail,"","", titleEmail,"MailLinked",
                "","Add","General",true);
        // 3. Check note show link icon
        boolean flag1 = note.checkNoteItemView(today, titleNote1, noteTime,true,false, testCase1);
        // 4. Open note (Test 1)
        note.openNoteItem(today, titleNote1);
        // 5. Check link has new email
        boolean flag2 = note.checkNoteItemDetails(titleNote1, noteDateTime,false,"Normal",
                "General", titleEmail,false, testCase1);
        // 6. Open email
        common.openItem("Email", titleEmail);
        // 7. Check link has note
        boolean flag3 = common.checkItemExist(titleNote1,"Exist", testCase1);
        // 8. Trash new email
        common.trashItemByDetails(true);
        // 9. Back to note view
        common.clickElementButton("Notes");
        // 10. Check note still show link icon (trash collection)
        boolean flag4 = note.checkNoteItemView(today, titleNote1,"",true,false, testCase1);
        Log.endTestCase(testCase1);
        if((flag1 & flag2 & flag3 & flag4) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Swipe left to right note and create other linked event
     */
    String testCase2 = "TS009_NoteScripts03_Note02_createOtherLinkedEventBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note02_createOtherLinkedEventBySwipe(){
        Log.startTestCase(testCase2);
        // 1. Swipe left to right note (Test 1) and click event icon
        // 2. Input to add new event
        note.addEventBySwipe(today, titleNote1, titleEvent,false,true);
        // 3. Check note show link icon
        boolean flag1 = note.checkNoteItemView(today, titleNote1, noteTime,true,false,testCase2);
        // 4. Open note (Test 1)
        note.openNoteItem(today, titleNote1);
        // 5. Check link has new event
        boolean flag2 = note.checkNoteItemDetails(titleNote1, noteDateTime, false, "", "", titleEvent,
                false, testCase2);
        // 6. Open new event
        common.openItem("Event", titleEvent);
        // 7. Check link has note (Test 1)
        boolean flag3 = agenda.checkEventItemDetails(titleEvent,false,"","", titleNote1, testCase2);
        // 8. Delete new event
        common.trashItemByDetails(true);
        // 9. Back to note view
        common.clickElementButton("Notes");
        // 10. Check note still show link icon(Trash collection)
        boolean flag4 = note.checkNoteItemView(today, titleNote1, noteTime,true,false, testCase2);
        Log.endTestCase(testCase2);
        if ((flag1 & flag2 & flag3 & flag4) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Swipe left to right note and create other linked to.do
     */
    String testCase3 = "TS009_NoteScripts03_Note03_createOtherLinkedTodoBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note03_createOtherLinkedTodoBySwipe(){
        Log.startTestCase(testCase3);
        // 1. Swipe left to right note (Test 1) and click to.do icon
        note.addTodoBySwipe(today, titleNote1);
        // 2. Check note show link icon
        boolean flag1 = note.checkNoteItemView(today, titleNote1, noteTime,true,false,testCase3);
        // 3. Open note (Test 1)
        note.openNoteItem(today, titleNote1);
        // 4. Check link has new to.do
        boolean flag2 = note.checkNoteItemDetails(titleNote1, noteDateTime, false, "", "",
                titleNote1,false, testCase2);
        // 5. Open new to.do
        common.openItem("Todo", titleNote1);
        // 6. Check link has note (Test 1)
        boolean flag3 = todo.checkTodoItemDetails(titleNote1,"",false,"","",
                "", titleNote1, testCase3);
        // 7. Delete new to.do
        common.trashItemByDetails(true);
        // 8. Back to note view
        common.clickElementButton("Notes");
        // 9. Check note still show link icon (Trash Collection)
        boolean flag4 = note.checkNoteItemView(today, titleNote1, noteTime,true,false, testCase3);
        Log.endTestCase(testCase3);
        if ((flag1 & flag2 & flag3 & flag4) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Swipe right to left note and move collection
     */
    String testCase4 = "TS009_NoteScripts03_Note04_moveNoteToCollectioBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note04_moveNoteToCollectioBySwipe(){
        Log.startTestCase(testCase4);
        // 1. Swipe right to left note (Test 1) and click collection icon
        note.openCollectionBySwipe(today, titleNote1);
        // 2. Move collection to others
        common.addMoveToCollection(false,"Move","Home");
        // 3. Open note (Test 1)
        note.openNoteItem(today, titleNote1);
        // 4. Check collection is moved
        // 5. Back to note view
        flag = note.checkNoteItemDetails(titleNote1,"",false,"","Home","",true, testCase4);
        Log.endTestCase(testCase4);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Swipe right to left note and move collection
     */
    String testCase5 = "TS009_NoteScripts03_Note05_addNoteToCollectioBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note05_addNoteToCollectioBySwipe(){
        Log.startTestCase(testCase5);
        // 1. Swipe right to left note (Test 1) and click collection icon
        note.openCollectionBySwipe(today, titleNote1);
        // 2. Add more collection Ex: Home, Play
        common.addMoveToCollection(false,"Add","General,Home");
        // 3. Open note (Test 1)
        note.openNoteItem(today, titleNote1);
        // 4. Check collection is added
        // 5. Back to note view
        flag = note.checkNoteItemDetails(titleNote1,"",false,"","General,Home","",true, testCase5);
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();
        common.clickViewBottomBar("Note");
        Log.endTestCase(testCase5);
        if (flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Add linked email from details view
     */
    String testCase6 = "TS009_NoteScripts03_Note06_addLinkedEmailFromDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note06_addLinkedEmailFromDetail(){
        Log.startTestCase(testCase6);
        // 1. Open new note
        note.openNoteItem(today, titleNote1);
        // 2. Click email icon
        // 3. Input info and send email
        note.sendEmailByDetails(fromEmail, toEmail,"","", titleEmail2,"Linked Email","",
                "Add","General",true);
        // 4. Check link section of note show new email
        boolean flag1 = note.checkNoteItemDetails(titleNote1,"",false,"","", titleEmail2,false, testCase6);
        // 5. Open email
        common.openItem("Email", titleEmail2);
        // 6. Check link section of email show note
        boolean flag2 = common.checkItemExist(titleNote1,"Exist", testCase6);

        // 7. Trash new email (trash and again to delete)
        common.trashItemByDetails(true);
        // 8. Back to note List
        common.clickElementButton("Notes");
        // 9. Check note still show link icon (Trash Collection)
        boolean flag3 = note.checkNoteItemView(today, titleNote1,"",true,false, testCase6);
        Log.startTestCase(testCase6);
        if((flag1 & flag2 & flag3) == false){
            Assert.fail("Test case is failed");
        }
    }

    /**
     * Add linked event from details view
     */
    String testCase7 = "TS009_NoteScripts03_Note07_addLinkedEventFromDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note07_addLinkedEventFromDetail() {
        Log.startTestCase(testCase7);
        // 1. Open new note
        note.openNoteItem(today, titleNote1);
        // 2. Click event icon
        // 3. Input info to add new event
        note.addEventByDetails(titleEvent2,false,true);
        // 4. Check link section of note show new email
        boolean flag1 = note.checkNoteItemDetails(titleNote1,"",false,"","",
                titleEvent2,false, testCase6);
        // 5. Open event
        common.openItem("Event", titleEvent2);
        // 6. Check link section of event show note
        boolean flag2 = common.checkItemExist(titleNote1,"Exist", testCase6);
        // 7. Trash new event
        common.trashItemByDetails(true);
        // 8. Back to note List
        common.clickElementButton("Notes");
        // 9. Check note still show link icon (Trash collection)
        boolean flag3 = note.checkNoteItemView(today, titleNote1,"",true,false, testCase7);
        Log.startTestCase(testCase7);
        if ((flag1 & flag2 & flag3) == false) {
            Assert.fail("Test case is failed");
        }
    }

    /**
     * Add linked to.do from details view
     */
    String testCase8 = "TS009_NoteScripts03_Note08_addLinkedTodoFromDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note08_addLinkedTodoFromDetail(){
        Log.startTestCase(testCase8);
        // 1. Open new note
        note.openNoteItem(today, titleNote1);

        // 2. Click to.do icon
        // 3. Input info to add new to.do
        note.addTodoByDetails();

        // Re-open note to check
        common.clickElementButton("Notes");
        note.openNoteItem(today, titleNote1);

        // 4. Check link section of note show new email
        boolean flag1 = note.checkNoteItemDetails(titleNote1,"",false,"","", titleNote1,
                false, testCase6);
        // 5. Open to.do
        common.openItem("Todo", titleNote1);
        // 6. Check link section of to.do show note
        boolean flag2 = todo.checkTodoItemDetails(titleNote1,"",false,"","",
                "", titleNote1, testCase8);
        // 7. Delete new to.do
        common.trashItemByDetails(true);
        // 8. Back to note List
        common.clickElementButton("Notes");
        // 9. Check note still show link icon
        boolean flag3 = note.checkNoteItemView(today, titleNote1,"",true,false, testCase8);
        Log.startTestCase(testCase8);
        if((flag1 & flag2 & flag3) == false){
            Assert.fail("Test case is failed");
        }
    }

    /**
     * Check linked icon is removed after emptying trash collection
     */
    String testCase9 = "TS009_NoteScripts03_Note09_checkLinkItemAfterEmptyTrash";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts03_Note09_checkLinkItemAfterEmptyTrash(){
        Log.startTestCase(testCase9);
        // 1. Goto Collection > Empty Trash
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();

        //2. Check not not show link icon
        common.clickViewBottomBar("Note");
        flag = note.checkNoteItemView(today, titleNote1,"",false,false, testCase9);
        Log.startTestCase(testCase9);
        if(!flag){
            Assert.fail("Test case is failed");
        }
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete note
        common.clearView();

        // Delete contact
        common.clickViewBottomBar("Contact");
        common.clearView();

        // Empty trash collection
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();

        // Clean app
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
