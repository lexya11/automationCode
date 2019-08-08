package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS010_CollectionScripts06 extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    AgendaKeywords agenda;
    SignInKeywords signin;
    NoteKeywords note;
    TodoKeywords todo;
    EmailKeywords email;
    CommonKeywords common;
    ContactKeywords contact;
    SettingsKeywords settings;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "CollectionScripts06_Event A";
    String titleTodo1 = "CollectionScripts06_Todo 1";;
    String firstName = "Contact";
    String lastName1 = "CollectionScripts06_A";
    String titleNote1 = "CollectionScripts06_Note A";


    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);
        todo = new TodoKeywords(this);
        note = new NoteKeywords(this);
        signin = new SignInKeywords(this);
        contact = new ContactKeywords(this);
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
                // GoTo Settings > Calendar > Set None Alert Event(Alert show up, will fail TC)
                common.openSettingsView();
                settings.clickSettingOptionList("Calendars");
                settings.setNormalEventAlert("None");
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

    // TC: Add New Event from Suggestion List
    String testCase1 = "TS010_CollectionScripts06_Collection1_addNewEventFromSuggestionList";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection1_addNewEventFromSuggestionList(){
        Log.startTestCase(testCase1);
        // 1. Filter by Collection Play
        collection.filterByType("Play","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Open Create New Event View
        collection.openAddItemView("Event");
        // 4. Add New Event A
        agenda.addSimpleEvent(titleEvent1, false, "Done");
        // 5. Filter by Collection Home
        collection.filterByType("Home","");
        // 6. Click on Plus icon
        common.openNewItemView();
        // 7. Add New Event from Suggestion list
        collection.addItemSuggestion("Event",titleEvent1);
        // 8. Check Event added belong Collection (Play)
        collection.filterByType("","Event");
        flag = collection.checkItemBelongCollection("Event",titleEvent1,testCase1);
        Log.endTestCase(testCase1);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add New To.do from Suggestion List
    String testCase2 = "TS010_CollectionScripts06_Collection2_addNewTodoFromSuggestionList";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection2_addNewTodoFromSuggestionList(){
        Log.startTestCase(testCase2);
        // 1. Filter by Collection Play
        collection.filterByType("Play","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Open Create New To.Do View
        collection.openAddItemView("Todo");
        todo.addNewTodo(titleTodo1,true,true);
        // 4. Filter by Collection (Home)
        collection.filterByType("Home","");
        // 5. Click on Plus icon
        common.openNewItemView();
        // 6. Add New To.Do from Suggestion list
        collection.addItemSuggestion("ToDo",titleTodo1);
        // 7. Check To.Do added belong Collection (Home)
        collection.filterByType("","Todo");
        flag = collection.checkItemBelongCollection("ToDo",titleTodo1,testCase2);
        Log.endTestCase(testCase2);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add New Contact from Suggestion List
    String testCase3 = "TS010_CollectionScripts06_Collection3_addNewContactFromSuggestionList";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection3_addNewContactFromSuggestionList(){
        Log.startTestCase(testCase3);
        // 1. Filter by Collection Play
        collection.filterByType("Play","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Open Create New Contact View
        collection.openAddItemView("Contact");
        // 4. Create New Contact A
        contact.addNewContact(firstName,lastName1,"","","abc@gmail.com",false,true);
        // 5. Filter by Collection (Home)
        collection.filterByType("Home","");
        // 6. Click on Plus icon
        common.openNewItemView();
        // 7. Add New Contact from Suggestion list
        collection.addItemSuggestion("Contact",firstName+" "+lastName1);
        // 8. Check Contact added belong Collection (Home)
        collection.filterByType("","Contact");
        flag = collection.checkItemBelongCollection("Contact",firstName+" "+lastName1,testCase3);
        Log.endTestCase(testCase3);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add New Note from Suggestion List
    String testCase4 = "TS010_CollectionScripts06_Collection4_addNewNoteFromSuggestionList";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection4_addNewNoteFromSuggestionList(){
        Log.startTestCase(testCase4);
        // 1. Filter by Collection Play
        collection.filterByType("Play","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Open Create New Note View
        collection.openAddItemView("Note");
        // 4. Add new Note
        note.addNewNote(titleNote1,"",true,"Done");
        // 5. Filter by Collection (Home)
        collection.filterByType("Home","");
        // 6. Click on Plus icon
        common.openNewItemView();
        // 7. Add New Note from Suggestion list
        collection.addItemSuggestion("Note",titleNote1);
        // 6. Check Note added belong Collection (Home)
        collection.filterByType("","Note");
        flag = collection.checkItemBelongCollection("Note",titleNote1,testCase4);
        Log.endTestCase(testCase4);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Search and add Event to Collection
    String testCase5 = "TS010_CollectionScripts06_Collection5_searchAndAddEvent";
    String keywordEvent = titleEvent1;
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection5_searchAndAddEvent(){
        Log.startTestCase(testCase5);
        // 1. Filter by Collection (Sample)
        collection.filterByType("Sample","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Select Event in Search Result list then add to Collection
        collection.searchItemToAdd(keywordEvent,"Event",titleEvent1);
        // 4. Check Event added belong Collection (Sample)
        collection.filterByType("","Event");
        flag = collection.checkItemBelongCollection("Event",titleEvent1,testCase5);
        Log.endTestCase(testCase5);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Search and add To.do to Collection
    String testCase6 = "TS010_CollectionScripts06_Collection6_searchAndAddTodo";
    String keywordTodo = titleTodo1;
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection6_searchAndAddTodo(){
        Log.startTestCase(testCase6);
        // 1. Filter by Collection (Sample)
        collection.filterByType("Sample","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Select To.do in Search Result list then add to Collection
        collection.searchItemToAdd(keywordTodo,"ToDo",titleTodo1);
        // 4. Check Event added belong Collection (Sample)
        collection.filterByType("","Todo");
        flag = collection.checkItemBelongCollection("ToDo",titleTodo1,testCase6);
        Log.endTestCase(testCase6);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Search and add Note to Collection
    String testCase7 = "TS010_CollectionScripts06_Collection7_searchAndAddNote";
    String keywordNote = titleNote1;
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection7_searchAndAddNote(){
        Log.startTestCase(testCase7);
        // 1. Filter by Collection (Sample)
        collection.filterByType("Sample","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Select Note in Search Result list then add to Collection
        collection.searchItemToAdd(keywordNote,"Note",titleNote1);
        // 4. Check Note added belong Collection (Sample)
        collection.filterByType("","Note");
        flag = collection.checkItemBelongCollection("Note",titleNote1,testCase7);
        Log.endTestCase(testCase7);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Search and add Contact to Collection
    String testCase8 = "TS010_CollectionScripts06_Collection8_searchAndAddContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts06_Collection8_searchAndAddContact(){
        Log.startTestCase(testCase8);
        // 1. Filter by Collection (Sample)
        collection.filterByType("Sample","");
        // 2. Click on Plus icon
        common.openNewItemView();
        // 3. Select Contact in Search Result list then add to Collection
        collection.searchItemToAdd(firstName,"Contact",firstName+" "+lastName1);
        // 4. Check Contact added belong Collection (Sample)
        collection.filterByType("","Contact");
        flag = collection.checkItemBelongCollection("Contact",firstName+" "+lastName1,testCase8);
        Log.endTestCase(testCase8);
        if(!flag){
            Assert.fail("Test case is failed!");
        }
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Trash all items
        collection.filterByType("Home","Event");
        collection.swipeRightToLeftItem("Event",titleEvent1,"Trash");
        common.clickElementButton("OK");
        collection.filterByType("","ToDo");
        collection.swipeRightToLeftItem("ToDo",titleTodo1,"Trash");
        common.clickElementButton("OK");
        collection.filterByType("","Contact");
        collection.swipeRightToLeftItem("Contact",firstName+" "+lastName1,"Trash");
        common.clickElementButton("OK");
        collection.filterByType("","Note");
        collection.swipeRightToLeftItem("Note",titleNote1,"Trash");
        common.clickElementButton("OK");

        // Trash Collection in Collection View
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