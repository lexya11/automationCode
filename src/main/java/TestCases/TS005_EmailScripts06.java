package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TS005_EmailScripts06 extends  SetupServer{
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    HomeKeywords home;
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

    String floEmailAddress   = "khuong001@123flo.com";
    String floEmailAddress1  = "khuong002@123flo.com";
    String floAccount = username + Constants.MAIL_SERVER;

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        home = new HomeKeywords(this);
        agenda = new AgendaKeywords(this);
        note = new NoteKeywords(this);
        signin = new SignInKeywords(this);
        todo = new TodoKeywords(this);
        email = new EmailKeywords(this);
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
                // Open Settings View
                common.openSettingsView();
                Wait(2);
                settings.clickSettingOptionList("Accounts");
                settings.updateFloAccount(floAccount,"","",true);
                common.clickViewBottomBar("Contact");
                common.clearView();
                statusLogin = true;
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    // TC: Move email to other folder in detail
    String testCase = "EmailScripts06_Email0_moveEmailToOtherFolderInDetail";
    String subject1 = "EmailScripts06_Move Email";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts06_Email0_moveEmailToOtherFolderInDetail(){
        Log.startTestCase(testCase);
        // 1. Open Email view
        common.clickViewBottomBar("Email");
        common.clearView();
        // 2. Tap on compose icon on top right
        common.openNewItemView();
        // 3. Send an Email to gmail address and Cc: Flo address
        email.sendEmail("", floEmailAddress1, floEmailAddress,"",subject1,"","","Add","Play",true);
        // 4. Filter IMAP folder by "All Sent"
        email.navigateToImapFolder("Sent");
        // 5. Open detail, tap on 'Folder' icon then move to Flo>Inbox
        email.moveEmailToOtherImapFolderByDetail(floEmailAddress1,subject1,"Flo","Inbox",true);
        // 6. Check email in "Flo>Inbox" folder
        flag = email.checkEmailExist("Flo>Inbox",floAccount,subject1,"Exist",testCase);
        Log.endTestCase(testCase);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Reply email in detail
    String testCase1 = "EmailScripts06_Email1_replyEmailInDetail";
    String subject2 = "EmailScripts06_Reply Email";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts06_Email1_replyEmailInDetail(){
        Log.startTestCase(testCase1);
        // 1. Open detail an Email
        Wait(5);
        email.openEmailItem(floAccount,subject1);
        // 2. Tap on "Reply" icon in detail, then send email
        email.replyEmailByDetail("","","","",subject2,"","","Add","Home",true);
        // 3. Check email has been sent
        flag = email.checkEmailExist("Sent",floAccount,subject2,"Exist",testCase1);
        // 4. open detail that email
        email.openEmailItem(floAccount,subject2);
        // 5. Check email detail
        boolean flag1 = email.checkEmailItemDetail(subject2,"",floAccount,"","",false,"","","",testCase1);
        // 6. delete email in detail
        common.trashItemByDetails(true);
        // 7. Filter IMAP folder to "Flo>Inbox"
        email.navigateToImapFolder("Flo");
        Log.endTestCase(testCase1);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Reply all email in detail
    String testCase2 = "EmailScripts06_Email2_replyAllEmailInDetail";
    String subject3 = "EmailScripts06_Reply All Email";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts06_Email2_replyAllEmailInDetail(){
        Log.startTestCase(testCase2);
        // 1. Open detail an Email
        email.navigateToImapFolder("All Inboxes");
        Wait(5);
        email.openEmailItem(floAccount,subject1);
        // 2. Tap on "Reply all" icon in detail, then send email
        email.replyAllEmailByDetail("","","","",subject3,"","","Add","Home",true);
        // 3. Check email has been sent
        flag = email.checkEmailExist("Sent", floEmailAddress1,subject3,"Exist",testCase2);
        // 4. open detail that email
        email.openEmailItem(floEmailAddress1,subject3);
        // 5. Check email detail
        boolean flag1 = email.checkEmailItemDetail(subject3,floAccount, floEmailAddress1, floEmailAddress,"",false,"","","",testCase2);
        // 6. delete email in detail
        common.trashItemByDetails(true);
        // 7. Filter IMAP folder to "Flo>Inbox"
        email.navigateToImapFolder("Flo");
        Log.endTestCase(testCase2);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Forward email in detail
    String testCase3 = "EmailScripts06_Email3_forwardEmailInDetail";
    String subject4 = "EmailScripts06_Forward Email";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts06_Email3_forwardEmailInDetail(){
        Log.startTestCase(testCase3);
        // 1. Open detail an Email
        email.openEmailItem(floAccount,subject1);
        // 2. Tap on "Reply all" icon in detail, then send email
        email.forwardEmailByDetail("", floEmailAddress,"","",subject4,"","","Add","Home",true);
        // 3. Check email has been sent
        flag = email.checkEmailExist("Sent", floEmailAddress,subject4,"Exist",testCase3);
        // 4. open detail that email
        email.openEmailItem(floEmailAddress,subject4);
        // 5. Check email detail
        boolean flag1 = email.checkEmailItemDetail(subject4,floAccount, floEmailAddress,"","",false,"","","",testCase3);
        // 6. delete email in detail
        common.trashItemByDetails(true);
        // 7. Filter IMAP folder to "Flo>Inbox"
        email.navigateToImapFolder("Flo");
        Log.endTestCase(testCase3);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Create new Event in detail
    String testCase4 = "EmailScripts06_Email4_createNewEventInDetail";
    String subjectEvent = "EmailScripts06_Event A";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts06_Email4_createNewEventInDetail(){
        Log.startTestCase(testCase4);
        // 1. Open detail an Email
        email.openEmailItem(floAccount,subject1);
        // 2. Tap on "Event" icon in detail, then create new Event
        email.addNewEventByDetail(subjectEvent,false,true);
        // 3. Check email detail has Event linked
        flag = email.checkEmailItemDetail(subject1,"", "","","",false,"","Play",subjectEvent,testCase4);
        // 4. Open detail of Event
        common.openItem("Event",subjectEvent);
        // 5. Check detail of Event
        boolean flag1 =  agenda.checkEventItemDetails(subjectEvent,false,"","",subject1,testCase4);
        // 6. delete Event by detail
        common.trashItemByDetails(true);
        // 7. Back to email list
        common.clickElementButton("Emails");
        Log.endTestCase(testCase4);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Create new To.Do in detail
    String testCase5 = "EmailScripts06_Email5_createNewTodoInDetail";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts06_Email5_createNewTodoInDetail(){
        Log.startTestCase(testCase5);
        // 1. Open detail an Email
        email.openEmailItem(floAccount,subject1);
        // 2. Tap on "To.Do" icon in detail, then create new To.Do
        email.addNewTodoByDetail("None","Home");
        // 3. Check email detail has To.Do linked
        flag = email.checkEmailItemDetail(subject1,"", "","","",false,"","Play",subject1,testCase5);
        // 4. Open detail of To.Do
        common.openItem("ToDo",subject1);
        // 5. Check detail of To.Do
        boolean flag1 =  todo.checkTodoItemDetails(subject1,"None",false,"","","Home",subject1,testCase5);
        // 6. delete To.Do by detail
        common.trashItemByDetails(true);
        // 7. delete email by detail
        common.trashItemByDetails(true);
        Log.endTestCase(testCase5);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        email.navigateToImapFolder("All Inboxes");
        email.deleteEmailBySwipe(floAccount, subject2,true);

        common.clickViewBottomBar("Contact");
        common.clearView();

        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();

        // Close App
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
