package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.SetupServer;
import org.junit.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TS004_SettingsScripts07 extends SetupServer  {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    CommonKeywords common;
    CollectionKeywords collection;
    SignInKeywords signin;
    TodoKeywords todo;
    AgendaKeywords agenda;
    NoteKeywords note;
    ContactKeywords contact;
    EmailKeywords email;

    // Test case variables
    boolean flag = true;
    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;
    String today;

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device,false);
        settings = new SettingsKeywords(this);
        common = new CommonKeywords(this);
        signin = new SignInKeywords(this);
        todo = new TodoKeywords(this);
        collection = new CollectionKeywords(this);
        agenda = new AgendaKeywords(this);
        note = new NoteKeywords(this);
        contact = new ContactKeywords(this);
        email = new EmailKeywords(this);

        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);
                //Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username, password, true);


                // Close start Flo popup
                common.closeStartFlo();
                // Setting not Show All Warning
                common.openSettingsView();
                settings.clickSettingOptionList("General");
                settings.setShowAllWarnings(false);
                statusLogin = true;
                //  Open To.Do view
                common.clickViewBottomBar("ToDo");
                common.clearView();
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    /**************************
     * Setting > General > Set OFF Show All Warning, Check not show Warning when delete To.do
     */
    String testcase1 = "TS004_SettingsScripts07_Settings1_checkNotShowWarningWhenDeleteTodo";
    String subject1 = "SettingsScripts07_Todo 1";
    @Test
    public void TS004_SettingsScripts07_Settings1_checkNotShowWarningWhenDeleteTodo(){
        Log.startTestCase(testcase1);
        // 1. Goto To.do > Add new To.do
//        common.clickViewBottomBar("ToDo");
        todo.addQuickTodo(subject1,"None");
        // 2. Open To.do1 > Click Trash Icon
        todo.openTodoItem(subject1);
        common.closeSendNotification(true);
        common.trashItemByDetails(false);
        // 3. Check pop Up Warning is not shown by checking "OK","Cancel" button is not shown
        flag = common.checkButtonExist("OK","Not Exist", testcase1);
        boolean flag1 = common.checkItemExist("Cancel","Not Exist", testcase1);
        // 4. Check To.do 1 is not existed
        boolean flag2 = common.checkItemExist(subject1,"Not Exist",testcase1);
        Log.endTestCase(testcase1);
        if (!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > General > Set OFF Show All Warning, Check not show Warning when delete Event
     */
//    String testcase2 = "TS004_SettingsScripts07_Settings2_checkNotShowWarningWhenDeleteEvent";
//    String subject2 = "SettingsScripts07_Event 1";
//    @Test
//    public void TS004_SettingsScripts07_Settings2_checkNotShowWarningWhenDeleteEvent(){
//        Log.startTestCase(testcase2);
//        // 1. Goto Event > Add new Event 1
//        common.clickViewBottomBar("Event");
//        agenda.addSimpleEvent(subject2,true,"Done");
//        // 2. Open Event 1 > Click Trash Icon
//        agenda.openEventItem(today, "All Day",subject2);
//        common.closeSendNotification(true);
//        common.trashItemByDetails(false);
//        // 3. Check pop Up Warning is not shown by checking "OK","Cancel" button is not shown
//        flag = common.checkButtonExist("OK","Not Exist", testcase2);
//        boolean flag1 = common.checkItemExist("Cancel","Not Exist", testcase2);
//        // 4. Check Event 1 is not existed
//        boolean flag2 = common.checkItemExist(subject2,"Not Exist",testcase2);
//        Log.endTestCase(testcase2);
//        if (!(flag&flag1&flag2))
//            Assert.fail("Test case is failed");
//    }

    /**************************
     * Setting > General > Set OFF Show All Warning, Check not show Warning when delete Note
     */
    String testcase3 = "TS004_SettingsScripts07_Settings3_notShowWarningWhenDeleteNote";
    String subject3 = "SettingsScripts07_Note 1";
    @Test
    public void TS004_SettingsScripts07_Settings3_notShowWarningWhenDeleteNote(){
        Log.startTestCase(testcase3);
        // 1. Goto Note > Add new Note
        common.clickViewBottomBar("Note");
        note.addQuickNote(subject3);
        // 3. Open Note 1 > Click Trash Icon
        Wait(2);
        note.openNoteItem(today,subject3);
        common.trashItemByDetails(false);
        // 4. Check pop Up Warning is not shown by checking "OK","Cancel" button is not shown
        flag = common.checkButtonExist("OK","Not Exist", testcase3);
        boolean flag1 = common.checkItemExist("Cancel","Not Exist", testcase3);
        //5. Check Note 1 is not existed
        boolean flag2 = common.checkItemExist(subject3,"Not Exist",testcase3);
        Log.endTestCase(testcase3);
        if (!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > General > Set OFF Show All Warning, Check not show Warning when delete Contact
     */
    String testcase4 = "TS004_SettingsScripts07_Settings4_notShowWarningWhenDeleteContact";
    String firstName = "SettingsScripts07_Contact";
    String lastName = "A";
    @Test
    public void TS004_SettingsScripts07_Settings4_notShowWarningWhenDeleteContact(){
        Log.startTestCase(testcase4);
        // 1. Goto Note > Add new Note
        common.clickViewBottomBar("Contact");
        common.openNewItemView();
        contact.addNewContact(firstName,lastName,"","","",true,true);
        // 3. Open Note 1 > Click Trash Icon
        Wait(2);
        contact.openContactItem(firstName,lastName);
        common.trashItemByDetails(false);
        // 4. Check pop Up Warning is not shown by checking "OK","Cancel" button is not shown
        flag = common.checkButtonExist("OK","Not Exist", testcase4);
        boolean flag1 = common.checkItemExist("Cancel","Not Exist", testcase4);
        //5. Check Note 1 is not existed
        boolean flag2 = common.checkItemExist(firstName+" "+lastName,"Not Exist",testcase4);
        Log.endTestCase(testcase4);
        if (!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > General > Set OFF Show All Warning, Check not show Warning when delete Email
     */
    String testcase5 = "TS004_SettingsScripts07_Settings5_notShowWarningWhenDeleteEmail";
    String subject4 = "Email 1";
    @Test
    public void TS004_SettingsScripts07_Settings5_notShowWarningWhenDeleteEmail(){
        Log.startTestCase(testcase5);
        // 1. Goto Email > Send Email
        common.clickViewBottomBar("Email");
        common.openNewItemView();
        email.sendEmail("",Constants.USERNAME+ Constants.MAIL_SERVER,"","",subject4,"content","","Add","Home",true);
        // 3. Open Email > Click Trash Icon
        email.navigateToImapFolder("Sent");
        email.openEmailItem(Constants.USERNAME+Constants.MAIL_SERVER,subject4);
        common.trashItemByDetails(false);
        // 4. Check pop Up Warning is not shown by checking "OK","Cancel" button is not shown
        flag = common.checkButtonExist("OK","Not Exist", testcase5);
//        boolean flag1 = common.checkItemExist("Cancel","Not Exist", testcase5);
        //5. Check Email 1 is not existed
        boolean flag2 = email.checkEmailExist("",Constants.USERNAME+Constants.MAIL_SERVER,subject4,"Not Exist",testcase5);
        Log.endTestCase(testcase5);
        if (!(flag&flag2))
            Assert.fail("Test case is failed");
    }


    @AfterClass
    public void cleanUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        common.clickViewBottomBar("Collection");

        //Empty trash
        collection.emptyTrashCollection();
        Wait(5);
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
