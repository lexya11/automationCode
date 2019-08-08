package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.*;
import org.testng.annotations.*;

public class TS008_ContactScripts02 extends SetupServer {
    MobileCapabilities capabilities;
    SignInKeywords signin;
    HomeKeywords home;
    SettingsKeywords settings;
    ContactKeywords contact;
    AgendaKeywords agenda;
    CommonKeywords common;
    EmailKeywords email;
    CollectionKeywords collection;
    boolean flag = true;

    String username = Constants.USERNAME03;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String floEmailAddress = "khuong001@123flo.com";

    String firstName = "Contact";
    String lastName1 = "Scripts02_A";
    String lastName2 = "Scripts02_B";
    String lastName3 = "Scripts02_C";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        email = new EmailKeywords(this);
        home = new HomeKeywords(    this);
        settings = new SettingsKeywords(this);
        agenda = new AgendaKeywords(this);
        collection = new CollectionKeywords(this);

        capabilities = new MobileCapabilities(device,false);
        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);
                // Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username,password,true);
                // Close Start Flo popup
                common.closeStartFlo();
                // Open Settings View
                common.openSettingsView();
                // Open Settings > Contacts View
                settings.clickSettingOptionList("Contacts");
                // Set Display In List 'Name and Email'
                settings.setDisplayInList("Name_Email");
                // Set Display Order 'First Last'
                settings.setDisplayOrder("First Last");
                // Opne Contact View
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

    // TC: Swipe left to right Contact and send Email
    String testCase1 = "TS008_ContactScripts02_Contact1_sendAnEmailBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts02_Contact1_sendAnEmailBySwiping(){
        Log.startTestCase(testCase1);
        // 1. Add New Contact (Contact Script02_A1)
        common.openNewItemView();
        contact.addNewContact( firstName, lastName1,"Company","123",
                floEmailAddress,false,true);
        // 2. Send an Email by Swipe action on Contact (Contact Script02_A1)
        contact.sendEmailBySwipe(firstName, lastName1,"","","",
                "","Contact Scripts_02 Email","Content of mail","","Add","General",true);
        // 3. Open detail of Contact (Contact Script02_A1)
        contact.openContactItem(firstName, lastName1);
        // 4. Check Email is not exist in Links section
        boolean flag1 = contact.checkContactItemDetails(firstName,lastName1,"Company","123",
                floEmailAddress,false,"None","General",testCase1);
        flag = common.checkItemExist("Contact Scripts_02 Email","Exist",testCase1);
        // 5. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase1);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe left to right Contact and create other linked Event
    String testCase2 = "TS008_ContactScripts02_Contact2_createLinkedEventBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts02_Contact2_createLinkedEventBySwiping(){
        Log.startTestCase(testCase2);
        // 1. Create New Event by swipe action on Contact (Contact Script02_A1) not send invite
        contact.addEventBySwipe(firstName,lastName1,"Contact Scripts_02 Event A",false,"Not Send",false,"Done");
        // 2. Open detail of Contact (Contact Script02_A1)
        contact.openContactItem(firstName,lastName1);
        // 3. Check Event linked in Links section
        flag = contact.checkContactItemDetails(firstName,lastName1,"Company","123",floEmailAddress,
                false,"Contact Scripts_02 Event A","General",testCase2);
        // 4. Open detail Event from Links section
        common.openItem("Event","Contact Scripts_02 Event A");
        // 5. Check detail of Event
        boolean flag1 = agenda.checkEventItemDetails("Contact Scripts_02 Event A",false,firstName+" "+lastName1,"General",
                firstName+" "+lastName1,testCase2);
        // 6. Delete Event
        common.trashItemByDetails(true);
        // 7. Back to Contacts View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase2);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Contact and move Contact to Collection
    String testCase3 = "TS008_ContactScripts02_Contact3_moveToCollectionBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts02_Contact3_moveToCollectionBySwiping(){
        Log.endTestCase(testCase3);
        // 1. Move Contact (Contact Script02_A1) to Collection by swipe action
        contact.openCollectionBySwipe(firstName,lastName1);
        // 2. Select Collection Sample to move
        common.addMoveToCollection(false,"Move","Sample");
        // 3. Open detail of Contact (Contact Script02_A1)
        contact.openContactItem(firstName,lastName1);
        // 4. Check detail of Contact (Contact Script02_A1)
        flag = contact.checkContactItemDetails(firstName,lastName1,"Company","123",floEmailAddress,
                false,"","Sample",testCase3);
        // 5. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase3);
        if(flag == false )
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Contact and add Contact to Collection
    String testCase4 = "TS008_ContactScripts02_Contact4_addToCollectionBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts02_Contact4_addToCollectionBySwiping(){
        Log.endTestCase(testCase4);
        // 1. Add Contact (Contact Script02_A1) to Collection by swipe action
        contact.openCollectionBySwipe(firstName,lastName1);
        // 2. Select Collection to add
        common.addMoveToCollection(false,"Add","Home,Play");
        // 3. Open detail of Contact (Contact Script02_A1)
        contact.openContactItem(firstName,lastName1);
        // 4. Check detail of Contact (Contact Script02_A1)
        flag = contact.checkContactItemDetails(firstName,lastName1,"Company","123",floEmailAddress,
                false,"","Sample,Home,Play",testCase3);
        // 5. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase4);
        if(flag == false )
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left Contact, then trash Contact
    String testCase5 = "TS008_ContactScripts02_Contact5_deleteContactBySwiping";
    String deleteMessage = "This Contact will be moved to your Trash Collection.";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts02_Contact5_deleteContactBySwiping(){
        Log.startTestCase(testCase5);
        // 1. Delete Contact (Contact Script02_A1) by swipe
        contact.deleteContactBySwipe(firstName,lastName1,false);
        // 2. Check popup Message Delete
        flag = common.checkTrashMessage(deleteMessage,"OK", testCase5);
        Log.endTestCase(testCase5);
        if(flag == false )
            Assert.fail("Test case is failed");
    }

    // TC: Check email icon not exist by swiping on contact
    String testCase6 = "TS008_ContactScripts02_Contact6_checkEmailIconNotExistBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts02_Contact6_checkEmailIconNotExistBySwipe(){
        Log.startTestCase(testCase6);
        // 1. Add contact without email
        common.openNewItemView();
        contact.addNewContact(firstName,lastName2,"Company","123",
                "",false,true);
        // 2. Swipe left to right on contact, check email icon not exist
        flag = contact.checkIconNotExistBySwipe(firstName,lastName2,"email",testCase6);
        Log.endTestCase(testCase6);
        if(flag == false )
            Assert.fail("Test case is failed");
    }

    // TC: Check phone, chat icon not exist by swiping on contact
    String testCase7 = "TS008_ContactScripts02_Contact7_checkPhoneAndChatIconNotExist";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts02_Contact7_checkPhoneAndChatIconNotExist(){
        Log.startTestCase(testCase7);
        // 1. Add contact without Phone number
        common.openNewItemView();
        contact.addNewContact(firstName,lastName3,"Company","",
                floEmailAddress,false,true);
        // 2. Swipe left to right on contact, check phone icon not exist
        flag = contact.checkIconNotExistBySwipe(firstName,lastName3,"phone",testCase7);
        contact.openContactItem(firstName,lastName3);
        common.clickElementButton("Contacts");
        // 3. Swipe left to right on contact, check chat icon not exist
        boolean flag1 = contact.checkIconNotExistBySwipe(firstName,lastName3,"chat",testCase7);
        // 4. Delete all Contacts in multi-select mode
        common.multiSelectMode(firstName+" "+lastName2,true,"Trash");
        Log.endTestCase(testCase7);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }
    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete all Emails and Contacts
        common.clickViewBottomBar("Email");
        email.navigateToImapFolder("Sent");
        common.clearView();

        // Empty trash collection
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
