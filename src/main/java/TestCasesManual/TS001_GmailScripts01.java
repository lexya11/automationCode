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

public class TS001_GmailScripts01 extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    HomeKeywords home;
    AgendaKeywords agenda;
    TodoKeywords todo;
    EmailKeywords email;
    NoteKeywords note;
    CommonKeywords common;
    ContactKeywords contact;
    SettingsKeywords settings;
    boolean flag = true;
    String username = "bi_test08";
    String gmailAccount = "flo.automationtest@gmail.com";
    String floAccount = username+Constants.MAIL_SERVER;

    @BeforeClass
    public void SetUp() {
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        home = new HomeKeywords(this);
        agenda = new AgendaKeywords(this);
        todo = new TodoKeywords(this);
        email = new EmailKeywords(this);
        contact = new ContactKeywords(this);
        settings = new SettingsKeywords(this);
        note = new NoteKeywords(this);
        capabilities = new MobileCapabilities("iPhone 6s.1", true);
        SetUp(capabilities);
        // Open Settings View
        home.clickTopLeftbutton("Settings");
        // Open Settings > Accounts
        settings.clickSettingOptionList("Accounts");
        // Update Name of Gmail account
        settings.updateFloAccount(gmailAccount,"","",true);
        // Back to Settings View
        common.clickElementButton("Settings");
        // Open Settings > Contacts
        settings.clickSettingOptionList("Contacts");
        // Set Display Order is 'First Last'
        settings.setDisplayOrder("First Last");
        // Back to Home View
//        settings.backToHomeScreen();
    }

    // TC: Send email from 3rd party account
    String testCase = "Send email from 3rd party account";
    @Test
    public void Email0_sendEmailFrom3rdPartyAccount(){
        Log.startTestCase(testCase);
        // Pre-Test: Add Gmail account to Flo app
        // 1. Open Email view
        common.clickViewBottomBar("Email");
        // 2. Open compose Email screen
        common.openNewItemView();
        // 3. Send an Email from Gmail account to Flo account
        email.sendEmail(gmailAccount,floAccount,"","","Send From 3rd Party Account","Content","","Add","Home",true);
        // 4. Check email in 'Flo>Inbox'
        flag = email.checkEmailExist("Flo>Inbox",gmailAccount,"Send From 3rd Party Account","Exist",testCase);
        // 5. Open detail email
        email.openEmailItem(gmailAccount,"Send From 3rd Party Account");
        // 6. Check detail of email
        boolean flag1 = email.checkEmailItemDetail("Send From 3rd Party Account",gmailAccount,floAccount,"","",false,"","None","",testCase);
        // 7. Delete Email in detail
        common.trashItemByDetails(true);
        // 8. Filter IMAP folder to "All Inboxes"
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Send email by beartrack from 3rd party account
    String testCase1 = "Send email by beartrack from 3rd party account";
    @Test
    public void Email1_sendEmailByBearTrackFrom3rdPartyAccount(){
        Log.startTestCase(testCase1);
        // 2. Open compose Email screen
        common.openNewItemView();
        // 3. Send an Email  by Beartrack  from Gmail account to Flo account
        email.sendEmailByBearTrack(gmailAccount,floAccount,"","","Send by Bear Track From 3rd Party Account","Content","",true,"30 days","Add","Home");
        // 4. Check email in 'Flo>Inbox'
        flag = email.checkEmailExist("Flo>Inbox",gmailAccount,"Send by Bear Track From 3rd Party Account","Exist",testCase1);
        // 5. Open detail email
        email.openEmailItem(gmailAccount,"Send by Bear Track From 3rd Party Account");
        // 6. Check detail of email
        boolean flag1 = email.checkEmailItemDetail("Send by Bear Track From 3rd Party Account",gmailAccount,floAccount,"","",false,"","None","",testCase1);
        // 7. Delete Email in detail
        common.trashItemByDetails(true);
        // 8. Filter IMAP folder to "All Trash"
        email.navigateToImapFolder("All Trash");
        // 9. Delete all emails in Trash folder
        email.multiSelectEmail("Send by Bear Track From 3rd Party Account",true,"Trash");
        // 10. Filter IMAP folder to "All Inboxes"
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase1);
        if(!(flag & flag1)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Mark Star email of 3rd party account in detail
    String testCase2 = "Mark Star email of 3rd party account in detail";
    @Test
    public void Email2_markStarEmail3rdInDetail() {
        Log.startTestCase(testCase2);
        // 1. Filter IMAP folder to "Gmail>Inbox"
        email.navigateToImapFolder("Gmail>Inbox");
        // 2. Mark star that Email in detail
        email.markStarEmail(gmailAccount, "Subject A", true);
        // 3. Open email detail
        email.openEmailItem(gmailAccount, "Subject A");
        // 4. Check email detail
        flag = email.checkEmailItemDetail("Subject A", "", gmailAccount, "", "", true, "Content", "", "", testCase2);
        // 5. Back to email list
        common.clickElementButton("Email");
        Log.endTestCase(testCase2);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Un-Star email of 3rd party account in detail
    String testCase3 = "Un-Star email of 3rd party account in detail";
    @Test
    public void Email3_UnStarEmail3rdInDetail(){
        Log.startTestCase(testCase3);
        // 1. Mark star that Email in detail
        email.markStarEmail(gmailAccount, "Subject A", false);
        // 2. Open email detail
        email.openEmailItem(gmailAccount, "Subject A");
        // 3. Check email detail
        flag = email.checkEmailItemDetail("Subject A", "", gmailAccount, "", "", false, "Content", "", "", testCase3);
        // 4. Back to email list
        common.clickElementButton("Email");
        Log.endTestCase(testCase3);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add item linked in Email detail
    String testCase4 = "Add item linked in Email detail";
    @Test
    public void Email4_addItemsLinkedInDetail(){
        Log.startTestCase(testCase4);
        // 1. Back to Home View
        home.clickTopLeftbutton("Home");
        // 2. Create new Event/To.Do/Contact/Note from plus icon
        home.openNewScreenPlusIcon("Event");
        agenda.addSimpleEvent("Event A",false,"Done");
        home.openNewScreenPlusIcon("ToDo");
        todo.addNewTodo("ToDo A",false,true);
        home.openNewScreenPlusIcon("Contact");
        contact.addNewContact("Contact","A","","","",false,true);
        home.openNewScreenPlusIcon("Note");
        note.addNewNote("Note 1","note",false,"Done");
        // 3. Open Email view
        common.clickViewBottomBar("Email");
        // 4. Open detail an Email
        email.openEmailItem(gmailAccount, "Subject A");
        // 5. Add Event/To.Do/Contact/Note into Links section
        common.addLinkedItemInDetail("Event","Event A");
        common.addLinkedItemInDetail("ToDo","ToDo A");
        common.addLinkedItemInDetail("Contact","Contact A");
        common.addLinkedItemInDetail("Note","Note 1");
        // 6. Check detail of Email
        flag = email.checkEmailItemDetail("Subject A", gmailAccount, gmailAccount, "", "", false, "", "", "Event A,ToDo A,Contact A,Note 1", testCase4);
        // 7. Back to Email list
        common.clickElementButton("Email");
        Log.endTestCase(testCase4);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add Collection in Email detail
    String testCase5 = "Add Collection in Email detail";
    @Test
    public void Email5_addCollectionInDetail(){
        Log.startTestCase(testCase5);
        // 1. Open detail an Email
        email.openEmailItem(gmailAccount, "Subject A");
        // 2. Add more Collections into Collection field
        common.addMoveToCollection(true,"Add","Home,Play,Sample");
        // 3. Open detail email
        email.openEmailItem(gmailAccount,"Subject A");
        // 4. Check detail of Email
        flag = email.checkEmailItemDetail("Subject A", gmailAccount, gmailAccount, "", "", false, "", "Home,Play,Sample", "Event A,ToDo A,Contact A,Note 1", testCase5);
        // 5. Back to Email list
        common.clickElementButton("Email");
        Log.endTestCase(testCase5);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }
    // TC: Move Collection in Email detail
    String testCase6 = "Move Collection in Email detail";
    @Test
    public void Email6_moveCollectionInDetail(){
        Log.startTestCase(testCase6);
        // 1. Open detail an Email
        email.openEmailItem(gmailAccount, "Subject A");
        // 2. Move email to other Collections from Collection field
        common.addMoveToCollection(true,"Move","Work");
        // 3. Filter IMAP folder by 'Collection Work'
        email.navigateToImapFolder("Work");
        // 4. Check email in List
        flag = email.checkEmailItemView(gmailAccount,"Subject A","Content",true,testCase6);
        // 5. Move email to Gmail>Inbox in detail
        email.moveEmailToOtherImapFolderByDetail(gmailAccount,"Subject A","Gmail","Inbox",true);
        // 6. Back to Home View
        home.clickTopLeftbutton("Home");
        Log.endTestCase(testCase6);
        if(!(flag)){
            Assert.fail("Test case is failed!");
        }
    }
    @AfterClass
    public void clearUp(){
        common.clickViewBottomBar("Collection");
        collection.filterByType("General","Event");
        collection.swipeRightToLeftItem("Event","Event A","Trash");
        common.clickElementButton("OK");
        collection.filterByType("","ToDo");
        collection.swipeRightToLeftItem("ToDo","ToDo A","Trash");
        common.clickElementButton("OK");
        collection.filterByType("","Contact");
        collection.swipeRightToLeftItem("Contact","Contact A","Trash");
        common.clickElementButton("OK");
        collection.filterByType("","Note");
        collection.swipeRightToLeftItem("Note","Note 1","Trash");
        common.clickElementButton("OK");
        // driver.closeApp();
    }
}
