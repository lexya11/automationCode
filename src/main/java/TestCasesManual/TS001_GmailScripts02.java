package TestCasesManual;

//     Pre-condition:
//     Login User, Flo acc added@ Gmail before run script
//     User :flo.automationtest@gmail.com
//     PWD : lcl12345678@9

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class TS001_GmailScripts02 extends SetupServer{
    MobileCapabilities capabilities;
    HomeKeywords home;
    EmailKeywords email;
    CommonKeywords common;
    SettingsKeywords settings;
    boolean flag = true;
    String gmailAccount = "flo.automationtest@gmail.com";

    @BeforeClass
    public void SetUp() {
        common = new CommonKeywords(this);
        home = new HomeKeywords(this);
        email = new EmailKeywords(this);
        settings = new SettingsKeywords(this);
        capabilities = new MobileCapabilities("iPhone 6s.1", true);
        SetUp(capabilities);
        // Open Settings View
        home.clickTopLeftbutton("Settings");
        // Open Settings > Accounts
        settings.clickSettingOptionList("Accounts");
        // Update Name of Gmail account
        settings.updateFloAccount(gmailAccount,"","",true);
        common.clickViewBottomBar("Email");
    }

    // TC: Mark Email as unread in multi-select mode
    String testCase = "Mark Email as unread in multi-select mode";
    @Test
    public void Email0_markEmailAsUnreadInSelectModeView(){
        Log.startTestCase(testCase);
        // 1. Filter IMAP folder by 'Gmail>Inbox'
        email.navigateToImapFolder("Gmail>Inbox");
        // 2. Access to multi-select mode > Select an email in list > Mark email email as unread
        email.multiSelectEmail("Subject B",false,"Mark as unread");
        // 3. filter type by "Unread'
        email.filterByType("Unread");
        // 4. Check email in Email view
        flag = email.checkEmailItemView(gmailAccount,"Subject B","Content B",false,testCase);
        Log.endTestCase(testCase);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }
    // TC: Mark Email as read in multi-select mode
    String testCase1 = "Mark Email as read in multi-select mode";
    @Test
    public void Email1_markEmailAsReadInSelectModeView(){
        Log.startTestCase(testCase1);
        // 1. Filter IMAP folder by 'Gmail>Inbox'
        email.navigateToImapFolder("Gmail>Inbox");
        // 2. filter type by "Unread'
        email.filterByType("Unread");
        // 3. Access to multi-select mode > Select an email in list > Mark email email as read
        email.multiSelectEmail("Subject B",false,"Mark as read");
        // 5. Check email not exist
        flag = email.checkEmailExist("Gmail>Inbox",gmailAccount,"Subject B","Not Exist",testCase1);
        Log.endTestCase(testCase1);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Move an Email from 3rd party to inbox of Flo in Multi-select mode
    String testCase2 = "Move an Email from 3rd party to inbox of Flo in Multi-select mode";
    @Test
    public void Email2_moveEmailIntoFloInboxInSelectModeView(){
        Log.startTestCase(testCase2);
        // 1. Filter IMAP folder by 'Gmail>Inbox'
        email.navigateToImapFolder("Gmail>Inbox");
        // 2. filter type by "All'
        email.filterByType("All");
        // 3. Access to multi-select mode > Select an Email of Gmail > Move to Flo>Inbox
        email.multiSelectEmail("Subject B",false,"File");
        email.moveToImapFolder("Flo","Inbox",true);
        // 4. Check email exist in Inbox folder of Flo
        flag = email.checkEmailExist("Flo>Inbox",gmailAccount,"Subject B","Exist",testCase2);
        Log.endTestCase(testCase2);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }
    // TC: Move an Email from Flo account to inbox of Gmail in Multi-select mode
    String testCase3 = "Move an Email from Flo account to inbox of Gmail in Multi-select mode";
    @Test
    public void Email3_moveEmailIntoGmailInboxInSelectModeView(){
        Log.startTestCase(testCase3);
        // 1. Filter IMAP folder by 'Flo>Inbox'
        email.navigateToImapFolder("Flo>Inbox");
        // 2. filter type by "All'
        email.filterByType("All");
        // 3. Access to multi-select mode > Select an Email of Flo > Move to Gmail>Inbox
        email.multiSelectEmail("Subject B",false,"File");
        email.moveToImapFolder("Gmail","Inbox",true);
        // 4. Check email exist in Inbox folder of Gmail
        flag = email.checkEmailExist("Gmail>Inbox",gmailAccount,"Subject B","Exist",testCase3);
        Log.endTestCase(testCase3);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add an Email to Collection(s) in Multi-select mode
    String testCase4 = "Add an Email to Collection(s) in Multi-select mode";
    @Test
    public void Email4_addEmailToCollectionInSelectModeView(){
        Log.startTestCase(testCase4);
        // 1. Filter IMAP folder by 'Gmail>Inbox'
        email.navigateToImapFolder("Gmail>Inbox");
        // 2. Access to multi-select mode > Select an Email of Gmail > Select option 'Collect'
        email.multiSelectEmail("Subject A,Subject B",false,"Collect");
        // 3. Add email to Collection
        common.addMoveToCollection(false,"Add","Home,Work,Play");
        // 4. Open detail of Email
        email.openEmailItem(gmailAccount,"Subject B");
        // 5. Check email in detail
        flag = email.checkEmailItemDetail("Subject B","",gmailAccount,"","",false,"Content B","Home,Work,Play","",testCase4);
        // 6. Back to Email view
        common.clickElementButton("Email");
        Log.endTestCase(testCase4);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }
    // TC: Move an Email to Collection in Multi-select mode
    String testCase5 = "Move an Email to Collection in Multi-select mode";
    @Test
    public void Email5_moveEmailToCollectionInSelectModeView(){
        Log.startTestCase(testCase5);
        // 1. Filter IMAP folder by 'Gmail>Inbox'
        email.navigateToImapFolder("Gmail>Inbox");
        // 2. Access to multi-select mode > Select an Email of Gmail > Select option 'Collect'
        email.multiSelectEmail("Subject B",false,"Collect");
        // 3. Move email to Collection
        common.addMoveToCollection(false,"Move","Sample");
        // 4. Check email exist in IMAP folder by 'Collection Sample'
        flag = email.checkEmailExist("Sample",gmailAccount,"Subject B","Exist",testCase5);
        // 5. Open detail of Email
        email.openEmailItem(gmailAccount,"Subject B");
        // 6. Check email in detail
        boolean flag1 = email.checkEmailItemDetail("Subject B","",gmailAccount,"","",false,"Content B","Sample","",testCase5);
        // 7. Back to Email view
        common.clickElementButton("Email");
        // 8. Move email to Gmail>Inbox in detail
        email.moveEmailToOtherImapFolderByDetail(gmailAccount,"Subject B","Gmail","Inbox",true);
        Log.endTestCase(testCase5);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Trash all Emails in Multi-select mode
    String testCase6 = "Trash all Emails in Multi-select mode";
    @Test
    public void Email6_trashAllEmailsInSelectModeView(){
        Log.startTestCase(testCase6);
        // 1. Filter IMAP folder by 'Gmail>Inbox'
        email.navigateToImapFolder("Gmail>Inbox");
        // 2. Select all Emails in Multi-Select mode > Trash all emails
        email.multiSelectEmail("Subject C",true,"Trash");
        // 3. Filter IMAP folder by 'All Trash'
        email.navigateToImapFolder("All Trash");
        // 4. Check all emails trashed existing
        flag = email.checkEmailExist("",gmailAccount,"Subject A","Exist",testCase6);
        boolean flag1 = email.checkEmailExist("",gmailAccount,"Subject B","Exist",testCase6);
        boolean flag2 = email.checkEmailExist("",gmailAccount,"Subject C","Exist",testCase6);
        // 5.  Select all Emails in Multi-Select mode > Move to Gmail>Inbox
        email.multiSelectEmail("Subject C",true,"File");
        email.moveToImapFolder("Gmail","Inbox",true);
        // 6. Filter IMAP folder by 'All Inboxes'
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase6);
        if(!(flag & flag1 & flag2)){
            Assert.fail("Test case is failed!");
        }
    }
    @AfterClass
    public void clearUp(){
        driver.closeApp();
    }
}
