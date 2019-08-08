package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

public class TS010_CollectionScripts02 extends SetupServer {
    MobileCapabilities capabilities;
    SignInKeywords signin;
    ContactKeywords contact;
    AgendaKeywords agenda;
    CommonKeywords common;
    SettingsKeywords settings;
    TodoKeywords todo;
    NoteKeywords note;
    CollectionKeywords collection;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "CollectionScripts02_Event A";
    String titleEvent2 = "CollectionScripts02_Event B";
    String titleTodo1 = titleEvent1;
    String titleNote1 = "CollectionScripts02_Note A";



    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        agenda = new AgendaKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        note = new NoteKeywords(this);
        todo = new TodoKeywords(this);
        settings = new SettingsKeywords(this);
        collection = new CollectionKeywords(this);
        capabilities = new MobileCapabilities(device, false);

        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);
                // Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username,password,true);
                // Close Start Flo popup
                common.closeStartFlo();
                // GoTo Settings > Calendar > Set None Alert Event(Alert show up, will fail TC)
                common.openSettingsView();
                Wait(3);
                settings.clickSettingOptionList("Calendars");
                settings.setNormalEventAlert("None");
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

    // TC: Swipe left to right event and create linked event
    String testCase1 = "TS010_CollectionScripts02_Collection1_addEventLinkedToEventBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts02_Collection1_addEventLinkedToEventBySwipe(){
        Log.startTestCase(testCase1);
        // 1. Filter by a Collection (Play) and Events, create new Event A
        collection.filterByType("Play","");
        common.openNewItemView();
        collection.openAddItemView("Event");
        agenda.addSimpleEvent(titleEvent1,false,"Done");
        // 2. Swipe left to right event (Event A) and click event icon
        collection.filterByType("Play","Event");
        collection.swipeLeftToRightItem("Event",titleEvent1,"Event");
        // 3. Input to add new event (Event B) in next day (Tomorrow)
        agenda.addSimpleEvent(titleEvent2,false,"None");
        agenda.setEventDateTime(true,"Tomorrow","",0,0,true);
        common.clickElementButton("Done");
        // 4. Check new Event B is added
        collection.filterByType("General","Event");
        flag = collection.checkItemBelongCollection("Event",titleEvent2,testCase1);
        // 5. Open detail of event (Event A)
        collection.filterByType("Play","Event");
        common.openItem("Event",titleEvent1);
        // 6. Check link has new event (Event B)
        boolean flag1 = agenda.checkEventItemDetails(titleEvent1,false,"","Play",titleEvent2,testCase1);
        // 7. Open new event (Event B) from Link section
        common.openItem("Event",titleEvent2);
        // 8. Check detail of event (Event B)
        boolean flag2 = agenda.checkEventItemDetails(titleEvent2,false,"","General",titleEvent1,testCase1);
        // 9. Delete event (Event B) in detail
        common.trashItemByDetails(true);
        // 10. Back to Collection View
        common.clickElementButton("Play");
        Log.endTestCase(testCase1);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe left to right Event and create linked To.do
    String testCase2 = "TS010_CollectionScripts02_Collection2_addTodoLinkedToEventBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts02_Collection2_addTodoLinkedToEventBySwipe(){
        Log.endTestCase(testCase2);
        // 1. Swipe left to right event (Event A) and click To.do icon
        collection.filterByType("","Event");
        collection.swipeLeftToRightItem("Event",titleEvent1,"ToDo");
        // 2. Open detail of event (Event A)
        common.openItem("Event",titleEvent1);
        // 3. Check link has new To.do (title of To.do: Event A)
        flag = agenda.checkEventItemDetails(titleEvent1,false,"","Play",titleTodo1,testCase2);
        common.clickElementButton("Play");
        // 4. Back to Collection View then filter by To.Do's)
        collection.filterByType("General","ToDo");
        // 5. Check new To.Do existing
        boolean flag1 = collection.checkItemBelongCollection("ToDo",titleTodo1,testCase2);
        // 6. Open detail of new To.do
        common.openItem("ToDo",titleTodo1);
        // 7. Check detail of new To.do
        boolean flag2 = todo.checkTodoItemDetails(titleTodo1,"None",false,"Short","None","General",titleEvent1,testCase2);
        // 8. Delete new To.do in detail
        common.trashItemByDetails(true);
        // 9. Back to Collection view and filter by 'Events'
        collection.filterByType("Play","Event");
        Log.endTestCase(testCase2);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe left to right event and create linked note
    String testCase3 = "TS010_CollectionScripts02_Collection3_addNoteLinkedToEventBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts02_Collection3_addNoteLinkedToEventBySwipe(){
        Log.endTestCase(testCase3);
        // 1. Swipe left to right event (Event A) and click Note icon
        collection.swipeLeftToRightItem("Event",titleEvent1,"Note");
        // 2. Input info new Note (Note A)
        note.addNewNote(titleNote1,"",false,"Done");
        // 3. Open detail of event (Event A)
        common.openItem("Event",titleEvent1);
        // 4. Check link has new Note (Note A)
        flag = agenda.checkEventItemDetails(titleEvent1,false,"","Play",titleNote1,testCase3);
        // 5. Back to Collection View then filter by 'Notes')
        common.clickElementButton("Play");
        collection.filterByType("General","Note");
        // 6. Check new Note existing (Note A)
        boolean flag1 = collection.checkItemBelongCollection("Note",titleNote1,testCase3);
        // 7. Open detail of new Note (Note A)
        common.openItem("Note",titleNote1);
        // 8. Check detail of new Note (Note A)
        boolean flag2 = note.checkNoteItemDetails(titleNote1,"",false,"","General",titleEvent1,false,testCase3);
        // 9. Delete new Note in detail (Note A)
        common.trashItemByDetails(true);
        // 10. Back to Collection view and filter by 'Events'
        collection.filterByType("Play","Event");
        Log.endTestCase(testCase3);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Event and move Collection
    String testCase4 = "TS010_CollectionScripts02_Collection4_moveEventToCollectionBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts02_Collection4_moveEventToCollectionBySwipe(){
        Log.startTestCase(testCase4);
        // 1. Swipe left to right event (Event A) and click Collection icon
        collection.swipeRightToLeftItem("Event",titleEvent1,"Collection");
        // 2. Move collection to others (Work)
        common.addMoveToCollection(false,"Move","Work");
        // 3. Check event not existing (Event A)
        flag = common.checkItemExist(titleEvent1,"Not Exist",testCase4);
        // 4. Filter by Collection moved (Work)
        collection.filterByType("Work","Event");
        // 5. Open detail of event (Event A)
        common.openItem("Event",titleEvent1);
        // 6. Check collection is moved
        boolean flag1 = agenda.checkEventItemDetails(titleEvent1,false,"","Work","",testCase4);
        // 7. Back to Collection view
        common.clickElementButton("Work");
        Log.endTestCase(testCase4);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Event and add Collection
    String testCase5 = "TS010_CollectionScripts02_Collection5_addEventToCollectionBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts02_Collection5_addEventToCollectionBySwipe(){
        Log.startTestCase(testCase5);
        // 1. Swipe left to right event (Event A) and click Collection icon
        collection.swipeRightToLeftItem("Event",titleEvent1,"Collection");
        // 2. Add more collection Ex: Sample, Home
        common.addMoveToCollection(false,"Add","Sample,Home");
        // 3. Open detail of event (Event A)
        common.openItem("Event",titleEvent1);
        // 4. Check collection is added
        flag = agenda.checkEventItemDetails(titleEvent1,false,"","Work,Sample,Home","",testCase5);
        // 5. Back to Collection view
        common.clickElementButton("Work");
        Log.endTestCase(testCase5);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left event and delete
    String testCase6 = "TS010_CollectionScripts02_Collection6_deleteEventBySwipe";
    String deleteMessage = "This Event will be moved to your Trash Collection.";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts02_Collection6_deleteEventBySwipe(){
        Log.startTestCase(testCase6);
        // 1. Swipe left to right event (Event A) and click Trash icon
        collection.swipeRightToLeftItem("Event",titleEvent1,"Trash");
        // 2. Check message delete then tap on OK button
        flag = common.checkTrashMessage(deleteMessage,"OK",testCase6);
        Log.endTestCase(testCase6);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Empty trash collection
        collection.emptyTrashCollection();
        // Close app
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
