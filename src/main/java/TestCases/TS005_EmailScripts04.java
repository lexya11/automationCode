package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;


public class TS005_EmailScripts04 extends  SetupServer{
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

    String floAddress  = "khuong001@123flo.com";
    String floAddress1 = "khuong002@123flo.com";


    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        home = new HomeKeywords(this);
        agenda = new AgendaKeywords(this);
        note = new NoteKeywords(this);
        email = new EmailKeywords(this);
        signin = new SignInKeywords(this);
        todo = new TodoKeywords(this);
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
                // Access to Settings > Contacts
                settings.clickSettingOptionList("Contacts");
                // Set Display in list: Name and Email
                settings.setDisplayInList("Name_Email");
                // Set Display order: First Name
                settings.setDisplayOrder("First Last");
                statusLogin = true;
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    // TC: Check email in VIP filter
    String testCase = "TS005_EmailScripts04_Email0_checkEmailInVipFilter";
    String firstName = "A_EmailScripts04_Contact";
    String lastName = "Email 1";
    String fullName = firstName+" "+lastName;
    String subject1 = "TS005_EmailScripts04_VIP Contact";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts04_Email0_checkEmailInVipFilter(){
        Log.startTestCase(testCase);
        // Pre-Test: Create a Vip Contact Email 1
        common.clickViewBottomBar("Contact");
        common.clearView();
        common.openNewItemView();
        contact.addNewContact(firstName,lastName,"","", floAddress,true,true);

        // 1. Open Email View
        common.clickViewBottomBar("Email");
        // 2. Send email to email address belong Contact Test 1, then tap on "Send" button
//        email.openNewEmailView("Compose Icon");
        common.openNewItemView();
        email.sendEmail("", floAddress,"","",subject1,"Send Email to Vip Contact","","Add","Home",true);

        // 4. Filter type by "VIP"
        email.filterByType("VIP");

        // 5. Check email has been sent to Contact Email 1
        flag = email.checkEmailExist("Sent",fullName,subject1,"Exist",testCase);
        // 6. Delete email by swipe
        email.deleteEmailBySwipe(fullName,subject1,true);
        // 7. Filter IMAP folder to "All Inboxes"
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase);
        if(!flag)
            Assert.fail("Test case is failed.");

    }

    // TC: Check email in Star filter
    String testCase1 =  "TS005_EmailScripts04_Email1_checkEmailInStarFilter";
    String subject2 = "TS005_EmailScripts04_Star Email";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts04_Email1_checkEmailInStarFilter(){
        Log.startTestCase(testCase1);
        // 1. Tap on compose icon on top right & Send email to Contact A
//        email.openNewEmailView("Compose Icon");
        common.openNewItemView();
        email.sendEmail("", floAddress,"","",subject2,"Mark Star Email","","Add","Home",true);

        // 3. Filter IMAP folder to "All Sent"
        email.navigateToImapFolder("Sent");
        // 4. Mark star in detail of Email
        email.markStarEmail(fullName, subject2,true);

        // 5. Filter type by Star
        email.filterByType("Star");
        // 6. Check email existing
        flag = email.checkEmailExist("",fullName,subject2,"Exist",testCase1);

        // 7. Un-Star in Email List
        email.clickStarInListView(fullName, subject2);

        // 8. Check email not existing in List
        boolean flag1 = email.checkEmailExist("",fullName,subject2,"Not Exist",testCase1);

        // 9. Filter type by All
        email.filterByType("All");
        // 10. Filter IMAP folder to "All Inboxes"
        email.navigateToImapFolder("All Inboxes");
        Log.endTestCase(testCase1);
        if(!(flag & flag1))
            Assert.fail("Test case is failed.");
    }

    // TC: Update in email list after delete contact
    String testCase2 =  "TS005_EmailScripts04_Email2_updateEmailListAfterDeleteContact";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts04_Email2_updateEmailListAfterDeleteContact(){
        Log.startTestCase(testCase2);
        // 1. Open Contact View
        common.clickViewBottomBar("Contact");
        // 2. Delete Contact A by swipe action
        contact.deleteContactBySwipe(firstName,lastName,true);
        // 3. Open Collection View
        common.clickViewBottomBar("Collection");
        // 4. Empty Trash Collection
        collection.emptyTrashCollection();
        // 5. Open Email View
        common.clickViewBottomBar("Email");
        // 6. Filter by All Sent
        email.navigateToImapFolder("Sent");
        // 7. Check update email in list after delete Contact
        flag = email.checkEmailItemView(floAddress,subject2,"Mark Star Email",false,testCase2);
        Log.endTestCase(testCase2);
        if(!(flag))
            Assert.fail("Test case is failed. - BUG 0024829");
    }

    // TC: Create new Contact from email detail
    String testCase3 =  "TS005_EmailScripts04_Email3_createNewContactFromDetail";
    String subject3 = "TS005_EmailScripts04_New Email 1";
    String lastName2 = "Email 2";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts04_Email3_createNewContactFromDetail(){
        Log.startTestCase(testCase3);
        // 1. Open Compose New Email > Send an email
//        email.openNewEmailView("Compose Icon");
        common.openNewItemView();
        email.sendEmail("",floAddress1,"","",subject3,"Test 1","","Add","General",true);
        // Delete emailaddress added in Contact
        common.clickViewBottomBar("Contact");
        common.multiSelectMode(floAddress1,false,"Trash");
        // 2. Open detail an Email
        common.clickViewBottomBar("Email");
        email.navigateToImapFolder("Sent");
        email.openEmailItem(floAddress1,subject3);
        // 3. Add New contact (Contact Email 2) from email address
        email.addNewContactFromDetail(floAddress1,"New Contact",firstName,lastName2,"","",false,true);
        // 4. Back to Email List
        common.clickElementButton("Emails");
        // 5. Open Contact View
        common.clickViewBottomBar("Contact");
        // 6. Check contact created
        flag = contact.checkContactItemView(firstName,lastName2, floAddress1,false,testCase3);
        // 7. Delete that contact by swipe
        contact.deleteContactBySwipe(firstName,lastName2,true);
//        // 8. Back to Home view
//        home.clickTopLeftbutton("Home");
        Log.endTestCase(testCase3);
        if(!(flag))
            Assert.fail("Test case is failed.");
    }

    // TC: Create new VIP Contact from email detail
    String testCase4 = "TS005_EmailScripts04_Email4_createNewVIPContactFromDetail";
    String lastName3 = "Email 3";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts04_Email4_createNewVIPContactFromDetail(){
        Log.startTestCase(testCase4);
        // 1. Open Email View
        common.clickViewBottomBar("Email");
        // 2. Open detail an Email
        email.openEmailItem(floAddress1,subject3);
        // 3. Add New Vip contact (Contact Email 3) from email address
        email.addNewContactFromDetail(floAddress1,"Vip Contact",firstName,lastName3,"","",true,true);
        // 4. Back to Email List
        common.clickElementButton("Emails");
        // 5. Open Contact View
        common.clickViewBottomBar("Contact");
        // 6. Check contact created
        flag = contact.checkContactItemView(firstName,lastName3, floAddress1,true,testCase4);
        // 7. Delete that contact by swipe
        contact.deleteContactBySwipe(firstName,lastName3,true);
//        // 8. Back to Home view
//        home.clickTopLeftbutton("Home");
        Log.endTestCase(testCase4);
        if(!(flag))
            Assert.fail("Test case is failed.");
    }

    // TC: TC: Add new email address to existing Contact
    String testCase5 = "TS005_EmailScripts04_Email5_addToExistingContactFromDetail";
    String lastName4 = "Email 4";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts04_Email5_addToExistingContactFromDetail(){
        Log.startTestCase(testCase5);
        // Pre-test: create new Contact Email 4
        // 1. Open Contact View
        common.clickViewBottomBar("Contact");
        // 2. Create new Contact A (none email address)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName4,"","", "",false,true);
        // 3. Open Email View
        common.clickViewBottomBar("Email");
        // 4. Open detail an Email
        email.openEmailItem(floAddress1,subject3);
        // 5. Add email address to existing Contact (Contact Email 4)
        email.addNewContactFromDetail(floAddress1,"Existing Contact",firstName,lastName4,"","",false,true);
        // 6. Back to Email list
        common.clickElementButton("Emails");
        // 7. Open Contact View
        common.clickViewBottomBar("Contact");
        // 8. Open detail Contact A
        contact.openContactItem(firstName,lastName4);
        // 9. Check detail Contact A
        flag = contact.checkContactItemDetails(firstName,lastName4,"","", floAddress1,false,"","",testCase5);
        // 10. Delete Contact A in detail
        common.trashItemByDetails(true);
//        // 11. Back to Home View
//        home.clickTopLeftbutton("Home");
        Log.endTestCase(testCase5);
        if(!(flag))
            Assert.fail("Test case is failed.");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        common.clickViewBottomBar("Email");
        email.multiSelectEmail(floAddress1,true,"Trash");

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
