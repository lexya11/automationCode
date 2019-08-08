package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;


public class TS010_CollectionScripts01 extends SetupServer {
    MobileCapabilities capabilities;
    SignInKeywords signin;
    ContactKeywords contact;
    AgendaKeywords agenda;
    CommonKeywords common;
    TodoKeywords todo;
    NoteKeywords note;
    CollectionKeywords collection;
    SettingsKeywords settings;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "CollectionScripts01_Event A";
    String titleTodo1 = "CollectionScripts01_Todo 1";
    String firstName = "Contact";
    String lastName1 = "CollectionScripts01_A";
    String titleNote1 = "CollectionScripts01_Note A";



    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        agenda = new AgendaKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        todo = new TodoKeywords(this);
        note = new NoteKeywords(this);
        collection = new CollectionKeywords(this);
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
                signin.login(username,password,true);
                // Close Start Flo popup
                common.closeStartFlo();

                // GoTo Settings > Calendar > Set None Alert Event(Alert show up, will fail TC)
                common.openSettingsView();
                settings.clickSettingOptionList("Calendars");
                settings.setNormalEventAlert("None");
                common.clickElementButton("Settings");
                // Settings Display Order Contact as First Last

                settings.clickSettingOptionList("Contacts");
                settings.setDisplayOrder("First Last");
                // Open Collection View
                common.clickViewBottomBar("Collection");
                statusLogin = true;
            }catch (Exception e ){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    // TC: Select Local filter by a Collection then verify view
    String testCase1 = "TS010_CollectionScripts01_Collection1_verifyCollectionView";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts01_Collection1_verifyCollectionView(){
        Log.startTestCase(testCase1);
        // 1. Check settings icon on the top right
        boolean flag1 = common.checkStaticButton("settings icon","Right",testCase1);
        // 2. Check Local filter
        boolean flag2 = collection.checkFilterByCollection("General",testCase1);
        Log.endTestCase(testCase1);
        if(flag1 == false || flag2 == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Filter by a Collection, create new Event from Plus Icon
    String testCase2 = "TS010_CollectionScripts01_Collection2_addNewEventFromPlusIcon";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts01_Collection2_addNewEventFromPlusIcon(){
        Log.startTestCase(testCase2);
        // 1. Filter event by Collection Home
        collection.filterByType("Home","");
        // 2. Add new Event from plus icon
        common.openNewItemView();
        collection.openAddItemView("Event");
        // 3. Input info of Event (Event A)
        agenda.addSimpleEvent(titleEvent1,false,"Done");
        // 4. Check about Event (Event A)
        collection.filterByType("","Event");
        flag = collection.checkItemBelongCollection("Event",titleEvent1,testCase2);
        // 5. Open detail of Event (Event A)
        common.openItem("Event",titleEvent1);
        // 6. Check detail of Event (Event A)
        boolean flag1 = agenda.checkEventItemDetails(titleEvent1,false,"","Home","",testCase2);
        // 7. Delete Event created
        common.trashItemByDetails(true);
        Log.endTestCase(testCase2);
        if(flag == false || flag1 == false){
            Assert.fail("Test case is failed");
        }
    }



    // TC: Filter by a Collection, create new To.do from Plus Icon
    String testCase3 = "TS010_CollectionScripts01_Collection3_addNewTodoFromPlusIcon";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts01_Collection3_addNewTodoFromPlusIcon(){
        Log.startTestCase(testCase3);
        // 1. Filter by a Collection (Home) and To.Do
        collection.filterByType("Home","");
        // 2. Add new To.Do from Plus icon
        common.openNewItemView();
        collection.openAddItemView("ToDo");
        // 3. Input info of To.Do (To.do 1)
        todo.addNewTodo(titleTodo1,false,true);
        // 4. Check about To.do (To.do 1)
        collection.filterByType("","ToDo");
        flag = collection.checkItemBelongCollection("ToDo",titleTodo1,testCase3);
        // 5. Open detail of To.do (To.do 1)
        common.openItem("ToDo",titleTodo1);
        // 6. Check detail of To.do (To.do 1)
        boolean flag1 = todo.checkTodoItemDetails(titleTodo1,"",false,"","","Home","", testCase3);
        // 7. Delete To.Do created
        common.trashItemByDetails(true);
        Log.endTestCase(testCase3);
        if(flag == false || flag1 == false){
            Assert.fail("Test case is failed");
        }
    }


    // TC: Filter by a Collection, create new Contact from Plus Icon
    String testCase4 = "TS010_CollectionScripts01_Collection4_addNewContactFromPlusIcon";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts01_Collection4_addNewContactFromPlusIcon(){
        Log.startTestCase(testCase4);
        // 1. Filter by a Collection (Home) and Contacts
        collection.filterByType("Home","");
        // 2. Add new Contact from Plus icon
        common.openNewItemView();
        collection.openAddItemView("Contact");
        // 3. Input info of Contact (Contact A)
        contact.addNewContact(firstName,lastName1,"Company","1234",
                "abcd@gmail.com",true,true);
        // 4. Check about Contact (Contact A)
        collection.filterByType("","Contact");
        flag = collection.checkItemBelongCollection("Contact",firstName+" "+lastName1,testCase4);
        // 5. Open detail of Contact (Contact A)
        common.openItem("Contact",firstName+" "+lastName1);
        // 6. Check detail of Contact (Contact A)
        boolean flag1 = contact.checkContactItemDetails(firstName,lastName1,"Company","1234",
                "",true,"","Home",testCase4);
        // 7. Delete Contact created
        common.trashItemByDetails(true);
        Log.endTestCase(testCase4);
        if(flag == false || flag1 == false){
            Assert.fail("Test case is failed");
        }
    }



    // TC: Filter by a Collection, create new Note from Plus Icon
    String testCase5 = "TS010_CollectionScripts01_Collection5_addNewNoteFromPlusIcon";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts01_Collection5_addNewNoteFromPlusIcon(){
        Log.startTestCase(testCase5);
        // 1. Filter by a Collection (Home) and Notes
        collection.filterByType("Home","");
        // 2. Add new Note from Plus icon
        common.openNewItemView();
        collection.openAddItemView("Note");
        // 3. Input info of Note (Note A)
        note.addNewNote(titleNote1,"",false,"Done");
        // 4. Check about Note (Note A)
        collection.filterByType("","Note");
        flag = collection.checkItemBelongCollection("Note",titleNote1,testCase5);
        // 5. Open detail of Note (Note A)
        common.openItem("Note",titleNote1);
        // 6. Check detail of Note (Note A)
        boolean flag1 = note.checkNoteItemDetails(titleNote1,"",false,"","Home","",false,testCase5);
        // 7. Delete Note created
        common.trashItemByDetails(true);
        Log.endTestCase(testCase5);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed");
        }
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
