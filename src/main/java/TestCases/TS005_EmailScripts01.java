package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;


public class TS005_EmailScripts01 extends SetupServer {
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

    String floEmailAddress  = "khuong001@123flo.com";
    String floEmailAddress1 = "khuong002@123flo.com";
    String floEmailAddress2 = "khuong003@123flo.com";


    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);
        note = new NoteKeywords(this);
        todo = new TodoKeywords(this);
        signin = new SignInKeywords(this);
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
                common.ignoreConnectAcountPopup();
                common.clickViewBottomBar("Contact");
                common.clearView();
                statusLogin = true;
            }catch(Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }
    }

    // TC: Verify Email View
    String testCase = "TS005_EmailScripts01_Email0_verifyEmailView";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email0_verifyEmailView(){
        Log.startTestCase(testCase);
        // 1. Open Email view
        common.clickViewBottomBar("Email");
        // 2. Check "Settings" button in right
        flag = common.checkStaticButton("Settings Icon","Right",testCase);
        // 3. Check current IMAP folder
        boolean flag1 = email.checkImapFolderScreen("All Inboxes",testCase);
        Log.endTestCase(testCase);
        if (!(flag & flag1))
            Assert.fail("Test case is failed!");
    }

    // TC: Compose new Email, Input all info then delete draft email
    String testCase1 = "TS005_EmailScripts01_Email1_composeThenDeleteDraftEmail";
    String subject1 = "TS005_EmailScripts01_Subject 1";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email1_composeThenDeleteDraftEmail(){
        Log.startTestCase(testCase1);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Enter text on all fields, then tap on "Cancel" button
        email.sendEmail("",floEmailAddress,floEmailAddress1,floEmailAddress2,subject1,"content","","Add","Home",false);
        // 3. Tap on "Delete Draft"
        email.draftEmail("Delete");
        common.ignoreConnectAcountPopup();
        // 4. Check email not save in Drafts folder
        flag = email.checkEmailExist("Drafts",floEmailAddress,subject1,"Not Exist",testCase1);
        // 5. Back to 'All Inboxes' folder
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase1);
        if(flag == false){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, Input all info then save draft email
    String testCase2 = "TS005_EmailScripts01_Email2_composeThenSaveDraftEmail";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email2_composeThenSaveDraftEmail(){
        Log.startTestCase(testCase2);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Enter text on all fields, then tap on "Cancel" button
        email.sendEmail("",floEmailAddress,floEmailAddress1,floEmailAddress2,subject1,"content","","Add","Home",false);
        // 3. Tap on "Save Draft"
        email.draftEmail("Save");
        // 4. Check email save in Drafts folder
        flag = email.checkEmailExist("Drafts",floEmailAddress,subject1,"Exist",testCase2);
        // 5. Delete email save as draft in Drafts folder by swipe action
        email.deleteEmailBySwipe(floEmailAddress,subject1,true);
        // 6. Back to 'All Inboxes' folder
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase2);
        if(flag == false){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, Input all info then cancel save draft email
    String testCase3 = "TS005_EmailScripts01_Email3_composeThenCancelSaveDraftEmail";
    String subject = "TS005_EmailScripts01";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email3_composeThenCancelSaveDraftEmail(){
        Log.startTestCase(testCase3);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Enter text on all fields, then tap on "Cancel" button
        email.sendEmail("",floEmailAddress,floEmailAddress1,floEmailAddress2,subject,"content","","Add","Home",false);
        // 3. Tap on "Cancel not save draft Email"
        email.draftEmail("Cancel");
        // 4. Check Compose Email Screen is display
        flag = common.checkViewScreenDisplay(subject,testCase3);
        // 5. Tap on "Cancel" button on top left
        common.clickElementButton("Cancel");
        // 6. Delete draft email
        email.draftEmail("Delete");
        Log.endTestCase(testCase3);
        if(flag == false){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, Input only To field then send email
    String testCase4 = "TS005_EmailScripts01_Email4_inputToFieldThenSendEmail";
    String subject2 = "TS005_EmailScripts01_Subject 2";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email4_inputToFieldThenSendEmail(){
        Log.startTestCase(testCase4);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Input the recipient in To field, then tap on "Send" button
        email.sendEmail("",floEmailAddress,"","",subject2,"","","Add","Home",true);
        // 3. Filter IMAP by "Sent", Check email has been sent
        flag = email.checkEmailExist("Sent",floEmailAddress,subject2,"Exist",testCase4);
        // 4. Open detail of Email
        email.openEmailItem(floEmailAddress,subject2);
        // 5. Check email's detail
        boolean flag1 = email.checkEmailItemDetail(subject2,"",floEmailAddress,"","",false,"","","None",testCase4);
        // 6. Delete email by detail
        common.trashItemByDetails(true);
        // 7. Back to Email list and filter by All Inboxes
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase4);
        if(!(flag1 & flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, Input only Cc field then send email
    String testCase5 = "TS005_EmailScripts01_Email5_inputCcFieldThenSendEmail";
    String subject3 = "TS005_EmailScripts01_Subject 3";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email5_inputCcFieldThenSendEmail(){
        Log.startTestCase(testCase5);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Input the recipient in Cc field, then tap on "Send" button
        email.sendEmail("","",floEmailAddress1,"",subject3,"","","Add","Home",true);
        // 3. Filter IMAP by "Sent", Check email has been sent
        flag = email.checkEmailExist("Sent","No Recipients",subject3,"Exist",testCase5);
        // 4. Open detail of Email
        email.openEmailItem("No Recipients",subject3);
        // 5. Check email's detail
        boolean flag1 = email.checkEmailItemDetail(subject3,"","",floEmailAddress1,"",false,"","","None",testCase5);
        // 6. Delete email by detail
        common.trashItemByDetails(true);
        // 7. Back to Email list and filter by All Inboxes
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase5);
        if(!(flag1 & flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, Input only Bcc field then send email
    String testCase6 = "TS005_EmailScripts01_Email6_inputBccFieldThenSendEmail";
    String subject4 = "TS005_EmailScripts01_Subject 4";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email6_inputBccFieldThenSendEmail(){
        Log.startTestCase(testCase6);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Input the recipient in Bcc field, then tap on "Send" button
        email.sendEmail("","","",floEmailAddress2,subject4,"","","Add","Home",true);
        // 3. Filter IMAP by "Sent", Check email has been sent
        flag = email.checkEmailExist("Sent","No Recipients",subject4,"Exist",testCase6);
        // 4. Open detail of Email
        email.openEmailItem("No Recipients",subject4);
        // 5. Check email's detail
        boolean flag1 = email.checkEmailItemDetail(subject4,"","","",floEmailAddress2,false,"","","None",testCase6);
        // 6. Delete email by detail
        common.trashItemByDetails(true);
        // 7. Back to Email list and filter by All Inboxes
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase6);
        if(!(flag1  & flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, send email without Subject
    String testCase7 = "TS005_EmailScripts01_Email7_sendEmailwithoutSubject";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email7_sendEmailwithoutSubject(){
        Log.startTestCase(testCase7);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Input the recipient in To field, Not input Subject then tap on "Send" button
        email.sendEmail("",floEmailAddress,"","","","Content","","Add","Home",true);
        // 3. Check Empty Subject popup, Tap on "Send"
        flag = email.checkEmailSubject("This message has no subject. Do you want to send it anyway?",true,"",testCase7);
        // 4. Add email to Collection after sent successfully
        common.addMoveToCollection(false,"Add","Home");
        // 5. Check email has been sent
        boolean flag1 = email.checkEmailExist("Sent",floEmailAddress,"(No Subject)","Exist",testCase7);
        // 6. Open detail of Email
        email.openEmailItem(floEmailAddress,"(No Subject)");
        // 7. Check email's detail
        boolean flag2 = email.checkEmailItemDetail("(No Subject)","",floEmailAddress,"","",false,"","","None",testCase7);
        // 8. Delete email by detail
        common.trashItemByDetails(true);
        // 9. Back to Email list and filter by All Inboxes
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase7);
        if(!(flag & flag1 & flag2)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, send email to invalid email address
    String testCase8 = "TS005_EmailScripts01_Email8_sendEmailToInvalidAddress";
    String message = "This message has no subject. Do you want to send it anyway?";
    String message1 = "seems to be an invalid email address. Please fix it or remove it from the field.";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email8_sendEmailToInvalidAddress(){
        Log.startTestCase(testCase8);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Input the invalid email address in To field, Not input Subject then tap on "Send" button
        email.sendEmail("","1234","","","","Content","","Add","Home",true);
        // 3. Show Empty Subject popup, Tap on "Send"
        flag = email.checkEmailSubject(message,true,"",testCase8);
        // 4. Check popup Invalid Email Address, tap on "OK"
        boolean flag1 = email.checkInvalidAddress("1234", message1,testCase8);
        // 5. Tap on "Cancel" button on top left
        common.clickElementButton("Cancel");
        // 6. Delete draft Email
        email.draftEmail("Delete");
        Log.endTestCase(testCase8);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Compose new Email, send email has image in content
    String testCase9 = "TS005_EmailScripts01_Email9_sendEmailHasImageAttached";
    String subject5 = "TS005_EmailScripts01_Subject 5";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts01_Email9_sendEmailHasImageAttached(){
        Log.startTestCase(testCase9);
        // 1. Open Compose new email view
        common.openNewItemView();
        // 2. Input all info, add an image in content
        email.sendEmail("",floEmailAddress,"","",subject5,"Email has image in content","1","Add","Home",true);
        // 3. Filter IMAP by "Sent"
        email.navigateToImapFolder("Sent");
        // 4. Open detail of Email
        email.openEmailItem(floEmailAddress,subject5);
        // 5. Check email's detail
        flag = email.checkEmailItemDetail(subject5,"",floEmailAddress,"","",false,"","","None",testCase9);
        // 6. Delete email by detail
        common.trashItemByDetails(true);
        // 7. Back to Email list and filter by All Inboxes
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase9);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        common.clickViewBottomBar("Contact");
        common.clearView();

        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();

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
