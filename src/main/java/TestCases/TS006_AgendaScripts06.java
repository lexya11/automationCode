package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TS006_AgendaScripts06 extends SetupServer {
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

    String titleEvent1 = "AgendaScripts06_Event 1";

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
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Calendars");
        settings.setNormalEventAlert("None");
        // Open Agenda View
        common.clickViewBottomBar("Event");
    }

    //  TC: Check Week Start Agenda
    String testCase5 ="TS006_AgendaScripts06_Agenda1_checkWeekStartAgenda";
    @Test
    public void TS006_AgendaScripts06_Agenda1_checkWeekStartAgenda(){
        Log.startTestCase(testCase5);
        //  1. Go to Home
        //  2. Go to Setting > Calendars
        home.clickTopLeftbutton("Home");
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Calendars");
        common.closeSendNotification(true);
        //  3. Set week day to Monday
        settings.setWeekStart("Monday");
        //  4. Go to Agenda
        common.clickViewBottomBar("Event");
        //  5. Check weekday
        flag = agenda.checkWeekStart("Monday", testCase5);

        //  6. Go to Setting > Calendars
        common.swipeBottomLeftToRight();
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Calendars");
        //  7. Set week day to Sunday
        settings.setWeekStart("Sunday");
        //  8. Go to Agenda
        common.clickViewBottomBar("Event");
        //  9. Check weekday
        boolean flag1 = agenda.checkWeekStart("Sunday", testCase5);
        Log.endTestCase(testCase5);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    //  TC: Add Event plus icon
    String testCase2 = "TS006_AgendaScripts06_Agenda2_addEventPlusIcon";
    @Test
    public void TS006_AgendaScripts06_Agenda2_addEventPlusIcon(){
        Log.startTestCase(testCase2);

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

        //  1. Click plus icon
        //  2. Add simple event
        eventTime = agenda.addSimpleEvent(titleEvent1,false,"Done");
        //  3. Check event in Agenda view
        flag = agenda.checkEventItemView(today, eventTime,titleEvent1, "None",false,false,testCase2);
        Log.endTestCase(testCase2);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    //  TC: Check event details
    String testCase1 = "TS006_AgendaScripts06_Agenda3_checkEventDetails";
    String deleteMessage = "This Event will be moved to your Trash Collection.";
    @Test
    public void TS006_AgendaScripts06_Agenda3_checkEventDetails(){
        Log.startTestCase(testCase1);
        //  1. Open event details
        agenda.openEventItem(today, eventTime,titleEvent1);
        //  2. Check event in Details view
        flag = agenda.checkEventItemDetails(titleEvent1,false, "None","General","None", testCase1);
        //  3. Check delete message
        //  4. Delete event
        common.trashItemByDetails(false);
        boolean flag1 = common.checkTrashMessage(deleteMessage,"OK", testCase1);
        Log.endTestCase(testCase1);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Empty Trash
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();
        // Close App
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
