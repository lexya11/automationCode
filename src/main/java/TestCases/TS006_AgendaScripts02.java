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
import java.util.Calendar;

public class TS006_AgendaScripts02 extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;
    AgendaKeywords agenda;
    CollectionKeywords collection;
    String today = "";
    String eventTime = "";
    String tomorrow = "";
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "AgendaScripts02_Agenda01_Event 1";
    String titleEvent2 = "AgendaScripts02_Agenda02_Event 2";

    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        common = new CommonKeywords(this);
        signin = new SignInKeywords(this);
        agenda = new AgendaKeywords(this);
        collection = new CollectionKeywords(this);

        capabilities = new MobileCapabilities(device,false);
        SetUp(capabilities);

        // Sign In to Flo
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username,password,true);

        // Close Start Flo popup
        common.closeStartFlo();

        // GoTo Settings > Calendar > Set None Alert Event(Alert show up, will fail TC)
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Calendars");
        settings.setNormalEventAlert("None");

        // Open Agenda View
        common.clickViewBottomBar("Event");

    }

    // TC : Swipe left to right event and create other linked event
    String testCase1 = "TS006_AgendaScripts02_Agenda01_createOtherLinkedEventBySwipe";
    @Test
    public void TS006_AgendaScripts02_Agenda01_createOtherLinkedEventBySwipe(){
        Log.startTestCase(testCase1);

        // 1. Create new Simple Event from Plus icon
        eventTime = agenda.addSimpleEvent(titleEvent1,false,"Done");

        // Generate String today
        Calendar cal = Calendar.getInstance();
        today = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // 2. Add New Event by Swipe
        agenda.addEventBySwipe(today, eventTime, titleEvent1, titleEvent2,false,"",false,"None");
        // 3. Settings Start Time for new Event
        agenda.setEventDateTime(true, "Tomorrow","",9,30,true);
        // 4. Tap on "Done" button
        common.clickElementButton("Done");
        // 5. Check new Event is added
        boolean flag1 = agenda.checkEventItemView(tomorrow,"09:30 AM", titleEvent2,"",
                false,true,testCase1);
        // 6. Check pre-Event
        boolean flag2 = agenda.checkEventItemView(today, eventTime, titleEvent1,"",false,true, testCase1);
        // 7. Open pre-Event
        agenda.openEventItem(today, eventTime, titleEvent1);
        // 8. Check detail of pre-Event
        boolean flag3 = agenda.checkEventItemDetails(titleEvent1,false,"None","General",
                titleEvent2, testCase1);
        // 9. Back to Agenda view
        common.clickElementButton("Events");
        // 10. Open detail of Event is added
        agenda.openEventItem(today,"09:30 AM", titleEvent2);
        // 11. Check detail of Event is added
        boolean flag4 = agenda.checkEventItemDetails(titleEvent2,false,"None","General",
                titleEvent1, testCase1);
        // 12. Delete Event is added
        common.trashItemByDetails(true);
        Log.endTestCase(testCase1);
        if ((flag1 & flag2 & flag3 & flag4) == false)
            Assert.fail("Test case is failed");
    }

    // TC : Swipe left to right event and create other linked to.do
    String testCase2 = "TS006_AgendaScripts02_Agenda02_createOtherLinkedToDoBySwipe";
    @Test
    public void TS006_AgendaScripts02_Agenda02_createOtherLinkedToDoBySwipe(){
        Log.startTestCase(testCase2);

        // 1. Add New To.Do by Swipe
        agenda.addTodoBySwipe(today, eventTime, titleEvent1);
        // 2. Check Event show link icon
        boolean flag1 = agenda.checkEventItemView(today, eventTime, titleEvent1,"",false,true, testCase2);
        // 3. Open Event's detail
        agenda.openEventItem(today, eventTime, titleEvent1);
        // 4. Check Event's detail
        boolean flag2 = agenda.checkEventItemDetails(titleEvent1,false,"None","General", titleEvent1, testCase2);
        // 5. Open To.Do from Links section
        common.openItem("ToDo", titleEvent1);
        // 6. Delete To.Do
        common.trashItemByDetails(true);
        // 7. Back to Agenda view
        common.clickElementButton("Events");
        Log.endTestCase(testCase2);
        if ((flag1 & flag2) == false)
            Assert.fail();
    }

    // TC: Check linked icon is removed after emptying trash collection
    String testCase3 =  "TS006_AgendaScripts02_Agenda03_checkLinkedIconIsRemoved";
    @Test
    public void TS006_AgendaScripts02_Agenda03_checkLinkedIconIsRemoved(){
        Log.startTestCase(testCase3);
        // 1. Open Collection View
        common.clickViewBottomBar("Collection");
        // 2. Delete all items in Trash Collection
        collection.emptyTrashCollection();
        // 3. Open Agenda View
        common.clickViewBottomBar("Event");
        // 4. Check Event is not linked with other items
        flag = agenda.checkEventItemView(today,eventTime,titleEvent1,"",false,false,testCase3);
        Log.endTestCase(testCase3);
        if ((flag) == false)
            Assert.fail();
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        // Trash Event
        agenda.openEventItem(today, eventTime,titleEvent1);
        common.trashItemByDetails(true);

        // Empty trash collection
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
