package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;


public class TS005_EmailScripts03 extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    AgendaKeywords agenda;
    SignInKeywords signin;
    NoteKeywords note;
    TodoKeywords todo;
    EmailKeywords email;
    CommonKeywords common;
    ContactKeywords contact;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String validEmail = "khuong001@123flo.com";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);
        note = new NoteKeywords(this);
        signin = new SignInKeywords(this);
        todo = new TodoKeywords(this);
        contact = new ContactKeywords(this);
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
                common.clickViewBottomBar("Email");
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

    // TC: Compose new Email, Input only To field then send email by BearTrack
    String testCase = "TS005_EmailScripts03_Email0_inputToFieldThenSendEmailBearTrack";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts03_Email0_inputToFieldThenSendEmailBearTrack(){
        Log.startTestCase(testCase);

        String subject = "TS005_EmailScripts03_Bear Track Email";
        String content = "Send Email with tracking time 30 days";

        // 1. Compose email Bear Track (30 days), input only To field
        common.openNewItemView();
        email.sendEmailByBearTrack("", validEmail,"","", subject, content,"",true,
                "30 days","Add","Play");
        common.ignoreConnectAcountPopup();
        // 2. Go to IMAP folder "All Sent"
        Wait(3);
        email.navigateToImapFolder("Sent");
        // 3. Open detail of Email has been sent
        email.openEmailItem(validEmail, subject);
        // 4. Check detail of email
        flag = email.checkEmailItemDetail(subject,"",validEmail,"","",false, content,"",
                "None",testCase);
        // 5. Delete the sent email
        common.trashItemByDetails(true);
        // 7. Back to All Inboxes folder
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase);
        if(!flag)
            Assert.fail("Test case is failed!");
    }

    // TC: Compose new Email, Input only Cc field then send email by BearTrack
    String testCase1 = "TS005_EmailScripts03_Email1_inputCcFieldThenSendEmailBearTrack";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void     TS005_EmailScripts03_Email1_inputCcFieldThenSendEmailBearTrack(){
        Log.startTestCase(testCase1);

        String subject = "Cc Bear Track Email";
        String content = "Send Email with tracking time 30 days";
        String noRecipient = "No Recipients";

        // 1. Compose email Bear Track (30 days), input only CC field
        common.openNewItemView();
        email.sendEmailByBearTrack("","", validEmail,"", subject, content,"",true,"30 days",
                "Add","Play");
        // 2. Go to IMAP folder "All Sent"
        email.navigateToImapFolder("Sent");
        // 3. Open detail of Email has been sent
        email.openEmailItem(noRecipient, subject);
        // 4. Check detail of email
        flag = email.checkEmailItemDetail(subject,"","", validEmail,"",false, "","",
                "None", testCase1);
        // 5. Delete the sent email
        common.trashItemByDetails(true);
        // 6. Back to All Inboxes folder
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase1);
        if(!flag)
            Assert.fail("Test case is failed!");
    }

    // TC: Compose new Email, Input only Bcc field then send email by BearTrack
    String testCase2 = "TS005_EmailScripts03_Email2_inputBccFieldThenSendEmailBearTrack";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts03_Email2_inputBccFieldThenSendEmailBearTrack(){
        Log.startTestCase(testCase2);

        String subject = "Bcc Bear Track Email";
        String content = "Send Email with tracking time 30 days";
        String noRecipient = "No Recipients";

        // 1. Compose email Bear Track (30 days), input only BCC field
        common.openNewItemView();
        email.sendEmailByBearTrack("","","", validEmail, subject, content,"",true, "30 days",
                "Add","Play");
        // 2. Go to IMAP folder "All Sent"
        email.navigateToImapFolder("Sent");
        // 3. Open detail of Email has been sent
        email.openEmailItem(noRecipient, subject);
        // 4. Check detail of email
        flag = email.checkEmailItemDetail(subject,"","","",validEmail,false, "","",
                "None", testCase2);
        // 5. Delete the sent email
        common.trashItemByDetails(true);
        // 6. Back to All Inboxes folder
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase2);
        if(!flag)
            Assert.fail("Test case is failed!");
    }

    // TC: Compose new Email, send email by BearTrack without Subject
    String testCase3 = "TS005_EmailScripts03_Email3_sendEmailByBearTrackWithoutSubject";
    String message = "This message has no subject. Do you want to send it anyway?";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts03_Email3_sendEmailByBearTrackWithoutSubject(){
        Log.startTestCase(testCase3);

        String content = "Send Email with tracking time 30 days";
        String noSubject = "(No Subject)";

        // 1. Compose email Bear Track (30 days) without Subject
        common.openNewItemView();
        email.sendEmailByBearTrack("", validEmail,"","","", content,"",true,
                "30 days","Add","Play");
        // 2. Check error message popup, sent email with bear Track (30 days)
        flag = email.checkEmailSubject(message,true,"30 days", testCase3);
        common.addMoveToCollection(false,"Add","Play");
        // 3. Go to IMAP folder "All Sent"
        email.navigateToImapFolder("Sent");
        // 4. Open sent Email
        email.openEmailItem(validEmail, noSubject);
        // 5. Check detail of email
        boolean flag1 = email.checkEmailItemDetail(noSubject,"", validEmail,"","",false, "",
                "","None", testCase3);
        // 6. Delete email by detail
        common.trashItemByDetails(true);
        // 7. Back to All Inboxes folder
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase3);
        if(!(flag & flag1))
            Assert.fail("Test case is failed!");
    }

    // TC: Compose new Email, send email by BearTrack to invalid email address
    String testCase4 = "TS005_EmailScripts03_Email4_sendEmailByBearTrackToIvalidAddress";
    String message1 = "seems to be an invalid email address. Please fix it or remove it from the field.";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts03_Email4_sendEmailByBearTrackToIvalidAddress(){
        Log.startTestCase(testCase4);

        String invalidEmail = "abcde";
        String subject = "Invalid Email Address";
        String content = "Send Email with tracking time 30 days";

        // 1. Compose email Bear Track (30 days) with invalid email address in To field
        common.openNewItemView();
        email.sendEmailByBearTrack("", invalidEmail,"","", subject, content,"",true,"30 days","Add","Play");
        // 2. Check error message popup, tap on OK
        flag = email.checkInvalidAddress(invalidEmail, message1, testCase4);
        // 4. Tap on "Cancel"
        common.clickElementButton("Cancel");
        // 5. Delete draft email
        email.draftEmail("Delete");
        Log.endTestCase(testCase4);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    // TC: Compose new Email, send email by BearTrack has image in content
    String testCase5 = "TS005_EmailScripts03_Email5_sendEmailByBearTrackHasImage";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts03_Email5_sendEmailByBearTrackHasImage(){
        Log.startTestCase(testCase5);

        String subject = "Image Attached";
        String content = "Send Email with tracking time 30 days";

        // 1. Tap on compose icon on top right
        common.openNewItemView();
        // 2. Input all info, add an image in content, then send email by Bear Track (30 days)
        email.sendEmailByBearTrack("", validEmail,"","", subject, content,"2",true,
                "30 days","Add","Play");
        // 3. Filter IMAP by "Sent"
        email.navigateToImapFolder("Sent");
        // 4. Open detail of Email
        Wait(15);
        email.openEmailItem(validEmail, subject);
        // 5. Check email's detail
        flag = email.checkEmailItemDetail(subject,"", validEmail,"","",false, "","",
                "None",testCase5);
        // 6. Delete email by detail
        common.trashItemByDetails(true);
        // 7. Back to Email list and filter by All Inboxes
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase5);
        if (flag == false)
            Assert.fail("Test case is failed!");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        // Trash email in Inboxes
//        email.multiSelectEmail(validEmail,true,"Trash");
        common.clickViewBottomBar("Contact");
        common.clearView();

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
