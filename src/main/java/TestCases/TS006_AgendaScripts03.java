package TestCases;

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
import java.util.Calendar;
import java.util.Date;

public class TS006_AgendaScripts03 extends SetupServer {
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
        todo.addQuickTodo("AgendaScripts03_Todo 1", "Today");
        // Back to Home
        home.clickTopLeftbutton("Home");
    }

    // TC: Check due To.Do show in Agenda
    String testCase1 = "TS006_AgendaScripts03_Agenda1_checkDueToDoInView";
    @Test
    public void TS006_AgendaScripts03_Agenda1_checkDueToDoInView(){
        Log.startTestCase(testCase1);

        // 1. Open Settings View
        home.clickTopLeftbutton("Settings");
        // 2. Open Settings > To.Do's View
        settings.clickSettingOptionList("ToDo's");
        // 3. Close Send Notification popup
        common.closeSendNotification(true);
        // 4. Turn OFF 'Show Due To.Do's in Agenda' toogle
        settings.showDueTodoAgenda(false);

        // 5. Open Agenda View
        common.clickViewBottomBar("Event");
        // 6. Check Due To.Do not existing in Agenda
        boolean flag1 = common.checkItemExist("AgendaScripts03_Todo 1","Not Exist",testCase1);
        // 7. Back to Home View
        common.swipeBottomLeftToRight();
        // 8. Open Settings View
        home.clickTopLeftbutton("Settings");
        // 9.  Open Settings > To.Do's View
        settings.clickSettingOptionList("ToDo's");
        // 10. Turn ON 'Show Due To.Do's in Agenda' toogle
        settings.showDueTodoAgenda(true);

        // 11. Open Agenda View
        common.clickViewBottomBar("Event");
        // 12. Check Due To.Do in Agenda
        flag = agenda.checkTodoItemView("AgendaScripts03_Todo 1","Due",today,false,false,false, testCase1 );
        Log.endTestCase(testCase1);
        if(!(flag & flag1))
            Assert.fail("Test case is failed!");
    }

    // TC: Set due date by swiping
    String testCase2 = "TS006_AgendaScripts03_Agenda2_setDueDateBySwiping";
    @Test(dataProvider = "setDueDateBySwipe")
    public void TS006_AgendaScripts03_Agenda2_setDueDateBySwiping(String testCase, String title, String status, String selectDue, String dueDate){
        Log.startTestCase(testCase2);

        // 1. Swipe on Due To.Do (Test 1) and set due date To.Do for each option
        agenda.setDueDateTodoBySwipe(title, selectDue);
        // Convert String dueDate to Int then get date value
        int numDate = (int)Double.parseDouble(dueDate.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, numDate);
        String DueDate = new SimpleDateFormat("EEE, dd MMM").format(calendar.getTime());

        // 2. Check Due To.Do (Test 1) with due in view for each option
        flag = agenda.checkTodoItemView(title, status, DueDate,false,false,false, testCase);
        Log.endTestCase(testCase2);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }
    @DataProvider
    public Object[][] setDueDateBySwipe() throws Exception{
        Object[][] setDueDateBySwipe = ExcelUtils.getTableArray(Constants.AGENDA_DATA,"addDueBySwipe");
        return (setDueDateBySwipe);
    }


    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete Contact
        agenda.deleteTodoBySwipe("AgendaScripts03_Todo 1", true);

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