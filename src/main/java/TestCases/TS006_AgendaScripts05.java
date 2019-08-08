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
import java.util.Calendar;

public class TS006_AgendaScripts05 extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;
    AgendaKeywords agenda;
    CollectionKeywords collection;
    String today = "";
    String eventTime = "";
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "AgendaScripts05_Event 1";

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

        // Generate String today
        Calendar cal = Calendar.getInstance();
        today = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Open Agenda View
        common.clickViewBottomBar("Event");
        eventTime = agenda.addSimpleEvent(titleEvent1,false,"Done");
    }

    //TC: Swipe left to right event and create other linked note
    String testCase1 = "TS006_AgendaScripts05_Agenda01_createOtherLinkedNoteBySwipe";
    @Test
    public void TS006_AgendaScripts05_Agenda01_createOtherLinkedNoteBySwipe(){
        Log.startTestCase(testCase1);
        // 1. Add New Note by Swipe
        agenda.addNoteBySwipe(today, eventTime,titleEvent1,"AgendaScripts05_Note 1","Note",true);
        // 2. Check Event show link icon
        boolean flag1 = agenda.checkEventItemView(today, eventTime,titleEvent1,"",false,true,testCase1);
        // 3. Open Event's detail
        agenda.openEventItem(today,eventTime,titleEvent1);
        // 4. Check Event's detail
        flag = agenda.checkEventItemDetails(titleEvent1,false,"None","General","AgendaScripts05_Note 1",testCase1);
        // 5. Open Note from Links section
        common.openItem("Note","AgendaScripts05_Note 1");
        // 6. Delete Note
        common.trashItemByDetails(true);
        // 7. Back to Agenda view
        common.clickElementButton("Events");
        Log.endTestCase(testCase1);
        if (!(flag1&flag))
            Assert.fail();
    }

    //TC: Check linked icon is removed after emptying trash collection
    String testCase2 =  "TS006_AgendaScripts05_Agenda02_checkLinkedIconIsRemoved";
    @Test
    public void TS006_AgendaScripts05_Agenda02_checkLinkedIconIsRemoved(){
        Log.startTestCase(testCase2);
        // 1. Open Collection View
        common.clickViewBottomBar("Collection");
        // 2. Delete all items in Trash Collection
        collection.emptyTrashCollection();
        // 3. Open Agenda View
        common.clickViewBottomBar("Event");
        // 4. Check Event is not linked with other items
        flag = agenda.checkEventItemView(today, eventTime,titleEvent1,"",false,false,testCase2);
        Log.endTestCase(testCase2);
        if ((flag) == false)
            Assert.fail();
    }

    //TC: Swipe right to left event and move collection
    String testCase3 = "TS006_AgendaScripts05_Agenda03_moveEventToCollectionBySwipe";
    @Test
    public void TS006_AgendaScripts05_Agenda03_moveEventToCollectionBySwipe(){
        Log.startTestCase(testCase3);
        // 1. Open Move/Add to Collection view by Swipe
        agenda.openCollectionBySwipe(today,eventTime,titleEvent1);
        // 2. Move to a Collection
        common.addMoveToCollection(false,"Move","Home");
        // 3. Open Event's detail
        agenda.openEventItem(today,eventTime,titleEvent1);
        // 4. Check Event's detail
        flag = agenda.checkEventItemDetails(titleEvent1,false,"None","Home","",testCase3);
        // 5. Back to Agenda view
        common.clickElementButton("Events");
        Log.endTestCase(testCase3);
        if (flag == false)
            Assert.fail();
    }

    //TC: Swipe right to left event and add collection
    String testCase4 = "TS006_AgendaScripts05_Agenda04_addEventToCollectionBySwipe";
    @Test
    public void TS006_AgendaScripts05_Agenda04_addEventToCollectionBySwipe(){
        Log.startTestCase(testCase4);
        // 1. Open Move/Add to Collection view by Swipe
        agenda.openCollectionBySwipe(today, eventTime,titleEvent1);
        // 2. Move to a Collection
        common.addMoveToCollection(false,"Add","Play,Sample");
        // 3. Open Event's detail
        agenda.openEventItem(today, eventTime,titleEvent1);
        // 4. Check Event's detail
        flag = agenda.checkEventItemDetails(titleEvent1,false,"None","Home,Play,Sample","",testCase4);
        // 5. Delete Event in detail
        common.trashItemByDetails(true);
        Log.endTestCase(testCase4);
        if (flag == false)
            Assert.fail();
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
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
