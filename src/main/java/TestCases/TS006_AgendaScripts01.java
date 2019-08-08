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

public class TS006_AgendaScripts01 extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;
    CollectionKeywords collection;
    AgendaKeywords agenda;
    String today = "";
    String eventTime = "";
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "AgendaScripts01_Event 1";
    String titleEvent2 = "AgendaScripts01_Event 2";

    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());

        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        agenda = new AgendaKeywords(this);
        collection = new CollectionKeywords(this);

        capabilities = new MobileCapabilities(device,false);
        SetUp(capabilities);

        //Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();
        // GoTo Settings > Calendar > Set None Alert Event(Alert show up, will fail TC)
        common.openSettingsView();
        settings.clickSettingOptionList("Calendars");
        settings.setNormalEventAlert("None");
    }

    //  TC: Verify agenda view
    String testCase = "TS006_AgendaScripts01_Agenda1_verifyAgendaView";
    @Test
    public void TS006_AgendaScripts01_Agenda1_verifyAgendaView(){
        Log.startTestCase(testCase);
        //  1. Open Agenda view
        common.clickViewBottomBar("Event");
        //  2. Check today section in list
        flag = agenda.checkDateSection("Today", testCase);
        //  3. Check others next day
        boolean flag1 = agenda.checkDateSection("Tomorrow", testCase);
        //  4. Check Home button in left
        boolean flag2 = common.checkStaticButton("Home Icon","Left",testCase);
        //  5. Check Plus button in right
        boolean flag3 = common.checkStaticButton("Plus Icon","Right",testCase);
        //  6. Check view header
        boolean flag4 = common.checkLocalFilter("All Collections",testCase);
        //  7. Check today button
        //  8. Check Month/year from header
        boolean flag5 = agenda.checkTodayAgenda(testCase);
        Log.endTestCase(testCase);
        if(!(flag & flag1 & flag2 & flag3 & flag4 & flag5)){
            Assert.fail("Test case is failed!");
        }
    }

    //  TC: Add Event from Home
    String testCase1 = "TS006_AgendaScripts01_Agenda2_addEventFromHome";
    @Test
    public void TS006_AgendaScripts01_Agenda2_addEventFromHome(){
        Log.startTestCase(testCase1);

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

        //  1. Click Home icon to back Homepage
        home.clickTopLeftbutton("Home");
        //  2. Click plus icon => click Event
        home.openNewScreenPlusIcon("Event");
        //  3. Add simple event
        eventTime = agenda.addSimpleEvent(titleEvent1,false,"Done");
        //  4. Go to Agenda view
        common.clickViewBottomBar("Event");
        //  5. Check event in Agenda view
        flag = agenda.checkEventItemView(today, eventTime,titleEvent1,"None",false,false,testCase1);
        //  6. Open detail of Event
        agenda.openEventItem(today, eventTime,titleEvent1);
        //  7. Delete Event in detail
        common.trashItemByDetails(true);
        Log.endTestCase(testCase1);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    //  TC: Add Event bottom bar
    String testCase2 = "TS006_AgendaScripts01_Agenda3_addEventBottomBar";
    @Test
    public void TS006_AgendaScripts01_Agenda3_addEventBottomBar(){
        Log.startTestCase(testCase2);

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

        //  1. Click event icon on bottom bar
        //  2. Add simple event
        eventTime = agenda.addSimpleEvent(titleEvent2,false,"Done");
        //  3. Check event in Agenda view
        flag = agenda.checkEventItemView(today, eventTime,titleEvent2, "None",false,false,testCase2);
        //  5. Open detail of Event
        agenda.openEventItem(today, eventTime,titleEvent2);
        //  6. Delete Event in detail
        common.trashItemByDetails(true);
        Log.endTestCase(testCase2);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Empty trash
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
