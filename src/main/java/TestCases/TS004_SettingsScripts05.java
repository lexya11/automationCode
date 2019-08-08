package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.junit.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class TS004_SettingsScripts05 extends SetupServer {
    //     Pre-condition: Flo acc added@ Gmail before run script
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    CommonKeywords common;
    SignInKeywords signin;
    CollectionKeywords collection;
    TodoKeywords todo;
    NoteKeywords note;
    AgendaKeywords agenda;
    EmailKeywords email;
    ContactKeywords contact;

    // Test case variables
    boolean flag = true;
    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String floEmail = username+Constants.MAIL_SERVER;

    @BeforeClass
    public void setUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        capabilities = new MobileCapabilities(device,false);
        SetUp(capabilities);
        signin = new SignInKeywords(this);
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        todo = new TodoKeywords(this);
        note = new NoteKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);
        contact = new ContactKeywords(this);

        //Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();
        common.openSettingsView();
        settings.clickSettingOptionList("Email");
        common.closeSendNotification(true);
    }

    /**************************
     * Setting > Email > Set Signature Flomail
     */
    String testCase1 = "TS004_SettingsScripts05_SettingEmail1_setSignatureForFlomail";
    String signature = "Automation test";
    @Test
    public void TS004_SettingsScripts05_SettingEmail1_setSignatureForFlomail(){
        Log.startTestCase(testCase1);
        //1. Set Signature for FloEmail
        settings.addEmailSignature(floEmail,signature);
        common.clickViewBottomBar("Email");
        common.openNewItemView();
        //2. Check Signature is available in content of new email view
        flag = email.checkNewEmailView("",signature,testCase1);
        //3. Back to Setting > Email
        common.openSettingsView();
        settings.clickSettingOptionList("Email");
        Log.endTestCase(testCase1);
        if(!flag)
            Assert.fail("Test case is failed");

    }

    /**************************
     * Setting > Email > Set Bear Track Notify if NO reply
     */
    String testCase2 = "TS004_SettingsScripts05_SettingEmail2_setBearTrackNotifyifNoReply";
    String toggle1 = "Notify if NO reply";
    @Test
    public void TS004_SettingsScripts05_SettingEmail2_setBearTrackNotifyifNoReply(){
        Log.startTestCase(testCase2);
        //1. Set Bear Track No Reply toggle : true
        settings.setBearTrackNoReply(true);
        //2. Check Toggle
        boolean flag1 = settings.checkSwitchToggle(toggle1,"ON",testCase2);
        //3. Set Bear Track No Reply toggle : false
        settings.setBearTrackNoReply(false);
        //4. Check Toggle
        flag = settings.checkSwitchToggle(toggle1,"OFF",testCase2);
        Log.endTestCase(testCase2);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > Email > Set Bear Track Notify upon reply
     */
    String testCase3 = "TS004_SettingsScripts05_SettingEmail3_setBearTrackNotifyUponReply";
    String toggle2 = "Notify upon reply";
    @Test
    public void TS004_SettingsScripts05_SettingEmail3_setBearTrackNotifyUponReply(){
        Log.startTestCase(testCase3);
        //1. Set Notify upon reply toggle : true
        settings.setBearTrackUponReply(true);
        //2. Check Toggle
        boolean flag1 = settings.checkSwitchToggle(toggle2,"ON",testCase3);
        //3. Set Notify upon reply toggle : false
        settings.setBearTrackUponReply(false);
        //4. Check Toggle
        flag = settings.checkSwitchToggle(toggle2,"OFF",testCase3);
        Log.endTestCase(testCase3);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > Email > Check status notify of Collection Email when Sending
     */
    String testCase4 = "TS004_SettingsScripts05_SettingEmail4_checkStatusNotifyCollectionEmailWhenSending";
    String content = "Selecting this option will cause Flo to suggest Collections for storing your emails when you send them. Incredibly powerful. No more wasted time trying to find emails that you've sent.";
    @Test
    public void TS004_SettingsScripts05_SettingEmail4_checkStatusNotifyCollectionEmailWhenSending(){
        Log.startTestCase(testCase4);
        // check notify of collection Email when Sending from Settings > Email view
        flag = settings.checkCollectEmailsWhenSendingNotify(content,testCase4);
        Log.endTestCase(testCase4);
        if(!flag)
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > Email > Set Collect Emails when Sending
     */
    String testCase5 = "TS004_SettingsScripts05_SettingEmail5_setCollectEmailsWhenSending";
    @Test
    public void TS004_SettingsScripts05_SettingEmail5_setCollectEmailsWhenSending(){
        Log.startTestCase(testCase5);
        //1. Set Collect Emails When Sending : true
        settings.setCollectEmailsWhenSending(true);
        //2. Open new Email, check collection is available when sending
        common.clickViewBottomBar("Email");
        common.openNewItemView();
        boolean flag1 = email.checkCollectionIsAvailableWhenSending();
        //3. Back Settings > Email
        common.openSettingsView();
        settings.clickSettingOptionList("Email");
        //4. Set Collect Emails When Sending : false
        settings.setCollectEmailsWhenSending(false);
        //5. Open new Email, check collection is not available when sending
        common.clickViewBottomBar("Email");
        common.openNewItemView();
        flag = email.checkCollectionIsAvailableWhenSending();
        //6. Back Settings > Email
        common.openSettingsView();
        settings.clickSettingOptionList("Email");
        Log.endTestCase(testCase5);
        if(!((!flag)&flag1))
            Assert.fail("Test case is failed");
    }

    /**************************
     * Setting > Email > Set Email Description Lines
     */
    String testCase6 = "TS004_SettingsScripts05_SettingEmail6_setPreviewDescriptionLines";
    @Test(dataProvider = "EmailDescriptionLine")
    public void TS004_SettingsScripts05_SettingEmail6_setPreviewDescriptionLines(String testCase,String lineNumber){
        Log.startTestCase(testCase6);
        //1. Set Email Description Line
        settings.setEmailDescriptionLine(lineNumber);
        //2. Check Email Description Line
        flag = settings.checkEmailDescriptionLine(lineNumber,testCase);
        Log.endTestCase(testCase6);
        if(!flag)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] EmailDescriptionLine() throws Exception {
        Object[][] testObjectArray = ExcelUtils.getTableArray(Constants.SETTINGS_DATA,"EmailDescriptionLine");
        return testObjectArray;
    }

    /**************************
     * Setting > Email > Set Move Email To Default Collection afer creating To do Event
     */
    String testCase7 = "TS004_SettingsScripts05_SettingEmail7_setMoveEmailToDefaultCollection";
    String toggle3 = "Move Email to Default Collection after creating ToDo or Event";
    @Test
    public void TS004_SettingsScripts05_SettingEmail7_setMoveEmailToDefaultCollection(){
        Log.startTestCase(testCase7);
        //1. Set Move Email to Default Collection after creating To.Do or Event toggle
        settings.setMoveEmail(true);
        //2. Check toggle
        boolean flag1 = settings.checkSwitchToggle(toggle3,"ON",testCase7);
        //3. Set Move Email to Default Collection after creating To.Do or Event toggle
        settings.setMoveEmail(false);
        //4. Check toggle
        flag = settings.checkSwitchToggle(toggle3,"OFF",testCase7);
        Log.endTestCase(testCase7);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Clean Signature
        settings.addEmailSignature(floEmail,"");

        //Clean Email in All Inboxes
        common.clickViewBottomBar("Email");
        email.navigateToImapFolder("All Inboxes");
        email.multiSelectEmail("checkCollectionIsAvailableWhenSending",true,"Trash");

        //Clean Email in All Inboxes
        email.navigateToImapFolder("Sent");
        email.multiSelectEmail("checkCollectionIsAvailableWhenSending",true,"Trash");

        //Delete Contact was created by send email in checkCollectionIsAvailableWhenSending KW
//        common.clickViewBottomBar("Contact");
//        common.openItem("Contact",Constants.USERNAME+Constants.MAIL_SERVER);
//        common.trashItemByDetails(true);
        common.clickViewBottomBar("Collection");

        //Empty trash
        collection.emptyTrashCollection();
        Wait(5);
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

