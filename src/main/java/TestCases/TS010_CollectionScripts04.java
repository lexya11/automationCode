package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

public class TS010_CollectionScripts04 extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    SignInKeywords signin;
    ContactKeywords contact;
    AgendaKeywords agenda;
    CommonKeywords common;
    TodoKeywords todo;
    EmailKeywords email;
    NoteKeywords note;
    SettingsKeywords settings;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleEvent1 = "CollectionScripts04_Event A";
    String firstName = "Contact";
    String lastName1 = "CollectionScripts04_A";
    String subjectEmail = "CollectionScripts04_Email A";



    @BeforeClass
    public void Setup(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        agenda = new AgendaKeywords(this);
        signin = new SignInKeywords(this);
        note = new NoteKeywords(this);
        todo = new TodoKeywords(this);
        common = new CommonKeywords(this);
        settings = new SettingsKeywords(this);
        collection = new CollectionKeywords(this);
        email = new EmailKeywords(this);

        capabilities = new MobileCapabilities(device,false);
        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);
                // Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username, password,true);

                // Close Start Flo popup
                common.closeStartFlo();
                // GoTo Settings > Calendar > Set None Alert Event(Alert show up, will fail TC)
                common.openSettingsView();
                settings.clickSettingOptionList("Calendars");
                settings.setNormalEventAlert("None");
                common.clickElementButton("Settings");
                // Settings Display Order Contact as First Last

                settings.clickSettingOptionList("Contacts");
                settings.setDisplayOrder("First Last");

                // Pre-test: create new Contact (Contact A)
                common.clickViewBottomBar("Contact");
                common.selectLocalFilter("Home");
                common.openNewItemView();
                contact.addNewContact(firstName,lastName1,"Floware","0123",
                        "khuong001@123flo.com",true,true);

                // Open Collection view
                common.clickViewBottomBar("Collection");
                statusLogin = true;
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    // TC: Swipe left to right Contact and send an Email
    String testCase1 = "TS010_CollectionScripts04_Collection1_sendAnEmailBySwipeOnContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts04_Collection1_sendAnEmailBySwipeOnContact(){
        Log.startTestCase(testCase1);
        // 1. Filter by a Collection (Home) and Contacts
        collection.filterByType("Home","Contact");
        // 2. Swipe left to right Contact (Contact A) and click Email icon
        collection.swipeLeftToRightItem("Contact",firstName+" "+lastName1,"Email");
        // 3. Input info send an Email
        email.sendEmail("","","","",subjectEmail,"Content","","Move",
                "Home",true);
        // 6. Filter type by Emails
        collection.filterByType("","Email");
        // 7. Check Email belong Collection (Home)
        Wait(3);
        flag = collection.checkItemBelongCollection("Email",subjectEmail,testCase1);
        // 8. Open detail of Email
        Wait(3);
        common.openItem("Email",subjectEmail);
        // 9. Trash email in detail
        common.trashItemByDetails(true);
        Log.endTestCase(testCase1);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Swipe left to right Contact and create Event
    String testCase2 = "TS010_CollectionScripts04_Collection2_createNewEventBySwipeOnContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts04_Collection2_createNewEventBySwipeOnContact(){
        Log.startTestCase(testCase2);
        // 1. Filter by a Collection (Home) and Contacts
        collection.filterByType("Home","Contact");
        // 2. Swipe left to right Contact (Contact A) and click Event icon
        collection.swipeLeftToRightItem("Contact",firstName+" "+lastName1,"Event");
        // 3. Input to add new event (Event A)
        agenda.addSimpleEvent(titleEvent1,false,"Done");
        // 4. Do not send invite
        agenda.sendEmailInvite("Not Send",false);
        // 5. Filter type by Events
        collection.filterByType("General","Event");
        // 6. Check new Event A is added belong Collection (Home)
        flag = collection.checkItemBelongCollection("Event",titleEvent1,testCase2);
        // 7. Open detail of event (Event A)
        common.openItem("Event",titleEvent1);
        // 8. Check link has new Contact (Contact A)
        boolean flag1 = agenda.checkEventItemDetails(titleEvent1,false,firstName+" "+lastName1,"General",
                firstName+" "+lastName1,testCase2);
        // 9. Delete event (Event A) in detail
        common.trashItemByDetails(true);
        // 10. Filter by Contacts
        collection.filterByType("Home","Contact");
        Log.endTestCase(testCase2);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Contact and move collection
    String testCase3 = "TS010_CollectionScripts04_Collection3_moveContactToCollectionBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts04_Collection3_moveContactToCollectionBySwipe(){
        Log.startTestCase(testCase3);
        // 1. Swipe right to left on Contact (Contact A) and click Collection icon
        collection.swipeRightToLeftItem("Contact",firstName+" "+lastName1,"Collection");
        // 2. Move collection to others (Play)
        common.addMoveToCollection(false,"Move","Play");
        // 3. Check Contact not existing (Contact A)
        flag = common.checkItemExist(firstName+" "+lastName1,"Not Exist",testCase3);
        // 4. Filter by Collection moved (Play)
        collection.filterByType("Play","Contact");
        // 5. Open detail of Contact (Contact A)
        Wait(3);
        common.openItem("Contact",firstName+" "+lastName1);
        // 6. Check collection is moved
        boolean flag1 = contact.checkContactItemDetails(firstName,lastName1,"Floware","0123",
                "khuong001@123flo.com",true,"","Play",testCase3);
        // 7. Back to Collection view
        common.clickElementButton("Play");
        Log.endTestCase(testCase3);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Contact and add collections
    String testCase4 = "TS010_CollectionScripts04_Collection4_addContactToCollectionBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts04_Collection4_addContactToCollectionBySwipe(){
        Log.startTestCase(testCase4);
        // 1. Swipe right to left on (Contact A) and click Collection icon
        collection.swipeRightToLeftItem("Contact",firstName+" "+lastName1,"Collection");
        // 2. Add more collection Ex: General, Work
        common.addMoveToCollection(false,"Add","General,Work");
        // 3. Open detail of Contact (Contact A)
        common.openItem("Contact",firstName+" "+lastName1);
        // 4. Check collection is added
        flag = contact.checkContactItemDetails(firstName,lastName1,"Floware","0123",
                "khuong001@123flo.com",true,"","Play,General,Work",testCase4);
        // 5. Back to Collection view
        common.clickElementButton("Play");
        Log.endTestCase(testCase4);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Contact and delete
    String testCase5 = "TS010_CollectionScripts04_Collection5_deleteContactBySwipe";
    String deleteMessage = "This Contact will be moved to your Trash Collection.";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts04_Collection5_deleteContactBySwipe(){
        Log.startTestCase(testCase5);
        // 1. Swipe right to left on (Contact A) and click Trash icon
        collection.swipeRightToLeftItem("Contact",firstName+" "+lastName1,"Trash");
        // 2. Check message delete
        flag = common.checkTrashMessage(deleteMessage,"OK",testCase5);
        Log.endTestCase(testCase5);
        if(!(flag))
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Empty trash collection
        collection.emptyTrashCollection();
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
