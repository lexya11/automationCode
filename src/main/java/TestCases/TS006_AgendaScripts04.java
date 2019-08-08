package TestCases;
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

public class TS006_AgendaScripts04 extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;
    AgendaKeywords agenda;
    ContactKeywords contact;
    CollectionKeywords collection;
    TodoKeywords todo;
    String today = "";
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleTodo1 = "AgendaScripts04_Todo 1";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        agenda = new AgendaKeywords(this);
        todo = new TodoKeywords(this);
        contact = new ContactKeywords(this);
        collection = new CollectionKeywords(this);

        capabilities = new MobileCapabilities(device, false);
        SetUp(capabilities);

        //Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();

        // GoTo Settings > Calendar > Set None Alert Event(Alert show up, will fail TC)
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Calendars");
        settings.setNormalEventAlert("None");

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

        // Add quick to.do
        common.clickViewBottomBar("ToDo");
        todo.addQuickTodo(titleTodo1, "Today");

        // Back to Home
        common.clickViewBottomBar("Event");
    }

    // TC: Swipe left to right Due To.Do and create other linked Note
    String testCase1 ="TS006_AgendaScripts04_Agenda1_createLinkedNoteBySwiping";
    @Test
    public void TS006_AgendaScripts04_Agenda1_createLinkedNoteBySwiping(){
        Log.startTestCase(testCase1);
        // 1. Swipe left to right Due To.Do (To.Do 1) and click Note icon
        agenda.addNoteTodoBySwipe(titleTodo1,"AgendaScripts04_New Note","Test Linked Note",true);
        // 2. Check Due To.Do (To.Do 1) has Linked icon in Agenda
        boolean flag1 = agenda.checkTodoItemView(titleTodo1,"Due",today,false,false,true, testCase1);
        // 3. Open detail of Due To.Do (To.Do 1)
        agenda.openDueTodoItem(today,titleTodo1);
        // 4. Check Note created in Links section
        boolean flag2 = common.checkItemExist("AgendaScripts04_New Note","Exist",testCase1);
        // 5. Open detail of Note
        common.openItem("Note","AgendaScripts04_New Note");
        // 6. Check Due To.Do (To.Do 1) in Links section
        boolean flag3 = common.checkItemExist(titleTodo1,"Exist",testCase1);
        // 7. Delete Note from detail
        common.trashItemByDetails(true);
        // 8. Back to Agenda View
        common.clickElementButton("Events");
        Log.endTestCase(testCase1);
        if(!(flag & flag1 & flag2 & flag3)){
            Assert.fail("Test case is failed!");
        }
    }
    // TC: Swipe right to left Due To.Do and Add collection
    String testCase2 = "TS006_AgendaScripts04_Agenda2_addCollectionBySwiping";
    @Test
    public void TS006_AgendaScripts04_Agenda2_addCollectionBySwiping(){
        Log.startTestCase(testCase2);
        // 1. Swipe right to left Due To.Do (To.Do 1) and click Collection icon
        agenda.openCollectionTodoBySwipe(titleTodo1);
        // 2. Add Due To.Do (To.Do 1) to Collection (Play, Home)
        common.addMoveToCollection(false,"Add","Play");
        // 3. Open detail of Due To.Do (To.Do 1)
        agenda.openDueTodoItem(today,titleTodo1);
        // 4. Check detail of Due To.Do (To.Do 1) (check Collection has been added)
        flag = todo.checkTodoItemDetails(titleTodo1,today,false,"","","Play","", testCase2);
        // 5. Back to Agenda View
        common.clickElementButton("Events");
        Log.endTestCase(testCase2);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    // TC: Swipe right to left Due To.Do and move collection
    String testCase3 = "TS006_AgendaScripts04_Agenda3_moveCollectionBySwiping";
    @Test
    public void TS006_AgendaScripts04_Agenda3_moveCollectionBySwiping(){
        Log.startTestCase(testCase3);
        // 1. Swipe right to left Due To.Do (To.Do 1) and click Collection icon
        agenda.openCollectionTodoBySwipe(titleTodo1);
        // 2. Move Due To.Do (To.Do 1) to Collection (Sample)
        common.addMoveToCollection(false,"Move","Sample");
        // 3. Open detail of Due To.Do (To.Do 1)
        agenda.openDueTodoItem(today,titleTodo1);
        // 4. Check detail of Due To.Do (To.Do 1) (check Collection has been moved)
        flag = todo.checkTodoItemDetails(titleTodo1,today,false,"","","Sample","", testCase3);
        // 5. Back to Agenda View
        common.clickElementButton("Events");
        Log.endTestCase(testCase3);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    // TC: Swipe left to right Due To.Do and create other linked Email
    String testCase4 ="TS006_AgendaScripts04_Agenda4_createLinkedEmailBySwiping";
    @Test
    public void TS006_AgendaScripts04_Agenda4_createLinkedEmailBySwiping(){
        Log.startTestCase(testCase4);
        // 1. Swipe left to right Due To.Do (To.Do 1) and click Email icon
        agenda.sendEmailTodoBySwipe(titleTodo1,"","bi_test01@flomail.net","","","AgendaScripts04_New Email",
                "Test Linked Email","","Add","General",true);
        // 2. Check Due To.Do has Linked icon in Agenda
        boolean flag1 = agenda.checkTodoItemView(titleTodo1,"Due",today,false,false,true, testCase4);
        // 3. Open detail of Due To.Do (To.Do 1)
        agenda.openDueTodoItem(today,titleTodo1);
        // 4. Check Email has been sent in Links section
        boolean flag2 = common.checkItemExist("AgendaScripts04_New Email","Exist",testCase4);
        // 5. Open detail of Email
        common.openItem("Email","AgendaScripts04_New Email");
        // 6. Check Due To.Do (To.Do 1) in Links section
        boolean flag3 = common.checkItemExist(titleTodo1,"Exist",testCase4);
        // 7. Delete Email from detail
        common.trashItemByDetails(true);
        // 8. Back to Agenda View
        common.clickElementButton("Events");
        Log.endTestCase(testCase4);
        if(!(flag & flag1 & flag2 & flag3)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Check linked icon is removed after emptying trash collection
    String testCase5 =  "TS006_AgendaScripts04_Agenda5_checkLinkedIconIsRemoved";
    @Test
    public void TS006_AgendaScripts04_Agenda5_checkLinkedIconIsRemoved(){
        Log.startTestCase(testCase5);
        // 1. Open Collection View
        common.clickViewBottomBar("Collection");
        // 2. Delete all items in Trash Collection
        collection.emptyTrashCollection();
        // 3. Open Agenda View
        common.clickViewBottomBar("Event");
        // 4. Check To.do has no Link icon
        flag = agenda.checkTodoItemView(titleTodo1,"Due", today,false,false,false, testCase5);
        Log.endTestCase(testCase5);
        if(!(flag))
            Assert.fail("Test case is failed!");
    }

    // TC: Delete Due To.Do by Swiping
    String testCase6 = "TS006_AgendaScripts04_Agenda6_deleteDueTodoBySwiping";
    String deleteMessage = "This ToDo will be moved to your Trash Collection.";
    @Test
    public void TS006_AgendaScripts04_Agenda6_deleteDueTodoBySwiping(){
        Log.startTestCase(testCase6);
        // 1. Swipe right to left Due To.Do (To.Do 1) and click Trash icon
        agenda.deleteTodoBySwipe(titleTodo1, false);
        // 2. Check trash message and click OK
        flag = common.checkTrashMessage(deleteMessage,"OK",testCase6);
        Log.endTestCase(testCase6);
        if(!flag)
            Assert.fail("Test case is failed!");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete Contact
        common.clickViewBottomBar("Contact");
        common.openItem("Contact","bi_test01@flomail.net");
        common.trashItemByDetails(true);

        // Empty Trash
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