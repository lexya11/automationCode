package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.*;
import org.testng.annotations.*;

public class TS008_ContactScripts03 extends SetupServer {
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

    String username = Constants.USERNAME05;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String floEmailAddress1 = "khuong001@123flo.com";
    String floEmailAddress2 = "khuong002@123flo.com";
    String floEmailAddress3 = "khuong003@123flo.com";
    String floEmailAddress4 = "khuong004@123flo.com";

    String firstName = "Contact";
    String lastName1 = "Scripts03_A";
    String lastName2 = "Scripts03_B";
    String lastName3 = "Scripts03_C";
    String lastName4 = "Scripts03_D";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        agenda = new AgendaKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        email = new EmailKeywords(this);
        home = new HomeKeywords(    this);
        settings = new SettingsKeywords(this);
        collection = new CollectionKeywords(this);

        capabilities = new MobileCapabilities(device, false);
        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try {
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
                // Set Display In Order 'First Last'
                settings.setDisplayOrder("First Last");
                // Set Display In List 'Name and Email'
                settings.setDisplayInList("Name_Email");
                // Open Contact View
                common.clickViewBottomBar("Contact");
                common.clearView();
                statusLogin=true;
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    // TC: Change Local filter to a Collection, add new Normal Contact then check Contact detail
    String testCase1 = "TS008_ContactScripts03_Contact1_FilterByACollectionAddNewContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact1_FilterByACollectionAddNewContact(){
        Log.startTestCase(testCase1);
        // 1. Select a Collection in Local filter
        common.selectLocalFilter("Home");
        // 2. Check Local filter of Contact View
        flag = common.checkLocalFilter("Home",testCase1);
        // 3. Add New Contact (Contact Script03_A)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName1,"Company1","123",floEmailAddress1,false,true);
        // 4. Check about Contact created (Contact Script03_A)
        boolean flag1 = contact.checkContactItemView(firstName,lastName1,floEmailAddress1,false,testCase1);
        // 5. Open detail of Contact (Contact Script03_A)
        contact.openContactItem(firstName,lastName1);
        // 6. Check detail of Contact (Contact Script03_A)
        boolean flag2 = contact.checkContactItemDetails(firstName,lastName1,"Company1","123",floEmailAddress1,false,"","Home",testCase1);
        // 7. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase1);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Change Local filter to a Collection, add new VIP Contact then check Contact detail
    String testCase2 = "TS008_ContactScripts03_Contact2_FilterByACollectionAddNewVIPContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact2_FilterByACollectionAddNewVIPContact(){
        Log.startTestCase(testCase2);
        // Pre-Test: Filter By VIP
        contact.filterByType("Vip");
        // 1. Check Local filter of Contact View
        flag = common.checkLocalFilter("Home",testCase2);
        // 2. Add New Contact (Contact Script03_B)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName2,"Company2","1234",floEmailAddress2,true,true);
        // 3. Check about Contact created (Contact Script03_B)
        boolean flag1 = contact.checkContactItemView(firstName,lastName2,floEmailAddress2,true,testCase2);
        // 4. Open detail of Contact (Contact Script03_B)
        contact.openContactItem(firstName,lastName2);
        // 5. Check detail of Contact (Contact Script03_B)
        boolean flag2 = contact.checkContactItemDetails(firstName,lastName2,"Company2","1234",floEmailAddress2,true,"","Home",testCase2);
        // 6. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase2);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }
    // TC: Change Local filter to multi Collections, add new Normal Contact then check Contact detail
    String testCase3 = "TS008_ContactScripts03_Contact3_FilterByMultiCollectionAddNewContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact3_FilterByMultiCollectionAddNewContact(){
        Log.startTestCase(testCase3);
        // Pre-Test: Filter By All
        contact.filterByType("All");

        // 1. Check Number and Local filter of Contact View
        flag = contact.checkContactLocalFilter("Home",2,testCase3);
        // 2. Select a Collection in Local filter
        common.selectLocalFilter("Home,Play");
        // 3. Check Local filter
//        boolean flag1 = common.checkLocalFilter("Home,Play",testCase3);
        // 4. Add New Contact (Contact Script03_C)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName3,"Company3","12345",floEmailAddress3,false,true);
        // 5. Check about Contact created (Contact Script03_C)
        boolean flag2 = contact.checkContactItemView(firstName,lastName3,floEmailAddress3,false,testCase3);
        // 6. Open detail of Contact (Contact Script03_C)
        contact.openContactItem(firstName,lastName3);
        // 7. Check detail of Contact (Contact Script03_C)
        boolean flag3 = contact.checkContactItemDetails(firstName,lastName3,"Company3","12345",floEmailAddress3,false,"","General",testCase3);
        // 8. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase3);
        if(!(flag & flag2 & flag3))
            Assert.fail("Test case is failed");
    }
    // TC: Add new VIP Contact when Local filter to multi Collections
    String testCase4 = "TS008_ContactScripts03_Contact4_FilterByMultiCollectionAddNewVIPContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact4_FilterByMultiCollectionAddNewVIPContact(){
        Log.startTestCase(testCase4);
        // Pre-Test: Filter By VIP
        contact.filterByType("Vip");

        // 1. Check Local filter
//        flag = common.checkLocalFilter("Home,Play",testCase3);
        // 2. Add New Contact (Contact Script03_D)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName4,"Company4","123456",floEmailAddress4,true,true);
        // 3. Check about Contact created (Contact Script03_D)
        boolean flag1 = contact.checkContactItemView(firstName,lastName4,floEmailAddress4,true,testCase4);
        // 4. Open detail of Contact (Contact Script03_D)
        contact.openContactItem(firstName,lastName4);
        // 5. Check detail of Contact (Contact Script03_D)
        boolean flag2 = contact.checkContactItemDetails(firstName,lastName4,"Company4","123456",floEmailAddress4,true,"","General",testCase4);
        // 6. Back to Contact View
        common.clickElementButton("Contacts");

        Log.endTestCase(testCase3);
        if(!( flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Change Local filter then Check number of Contact
    String testCase5 = "TS008_ContactScripts03_Contact5_checkNumberContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact5_checkNumberContact(){
        Log.startTestCase(testCase5);
        // 1. Select All Collections in Local Filter
        common.selectLocalFilter("All Collections");
        // 2. Filter By All from bottom
        contact.filterByType("All");
        // 3. Check Number and Local filter of Contact View
        flag = contact.checkContactLocalFilter("All Collections",4,testCase5);
        // 4. Filter By VIP from bottom
        contact.filterByType("Vip");
        // 5. Check Number and Local filter of Contact View
        boolean flag1 = contact.checkContactLocalFilter("All Collections",2,testCase5);
        Log.endTestCase(testCase5);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Delete Contacts
    String testCase6 = "TS008_ContactScripts03_Contact6_deleteContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact6_deleteContact(){
        Log.startTestCase(testCase6);
        // 1. Filter By VIP from Top
        contact.filterByType("Vip");
        // 2. Delete Contact (Contact Script03_D)
        contact.deleteContactBySwipe(firstName,lastName4,true);
        flag = common.checkItemExist(firstName+" "+lastName4,"Not Exist",testCase6);
        // 3. Delete Contact (Contact Script03_B)
        contact.deleteContactBySwipe(firstName,lastName2,true);
        boolean flag1 = common.checkItemExist(firstName+" "+lastName2,"Not Exist",testCase6);
        // 4. Filter By All from Top
        contact.filterByType("All");
        // 5. Delete Contact (Contact Script03_C) by swipe
        contact.deleteContactBySwipe(firstName,lastName3,true);
        boolean flag2 = common.checkItemExist(firstName+" "+lastName3,"Not Exist",testCase6);
        Log.endTestCase(testCase6);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC. Send an Email in detail of Contact
    String testCase7 = "TS008_ContactScripts03_Contact7_sendEmailFromDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact7_sendEmailFromDetail(){
        Log.startTestCase(testCase7);
        // 1. Open detail of Contact (Contact Script03_A)
        contact.openContactItem(firstName,lastName1);
        // 2. Send an Email from detail
        contact.sendEmailByDetails("","","","","Contact Scripts03_Email",
                "Content of Email","","Add","General",true);
        // 3. Check Email is not existing in Links section
        common.clickElementButton("Contacts");
        contact.openContactItem(firstName,lastName1);
        flag = contact.checkContactItemDetails(firstName,lastName1,"Company1","123",
                floEmailAddress1,false,"None","Home",testCase7);
        // 4. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase7);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }
    // TC. TC. Add an Event in detail of Contact
    String testCase8 = "TS008_ContactScripts03_Contact8_addEventFromDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts03_Contact8_addEventFromDetail(){
        Log.startTestCase(testCase8);
        // 1. Open detail of Contact (Contact Script03_A)
        contact.openContactItem(firstName,lastName1);
        // 2. Create new Event from Detail then send invite
        contact.addEventByDetails("Contact Scripts03_Event A",false,"Send",true,"Done");
        // 3. Check Event linked in Links section
        common.clickElementButton("Contacts");
        contact.openContactItem(firstName,lastName1);
        flag = contact.checkContactItemDetails(firstName,lastName1,"Company1","123",
                floEmailAddress1,false,"Contact Scripts03_Event A","Home",testCase8);
        // 4. Open Event from Links section in detail
        common.openItem("Event","Contact Scripts03_Event A");
        // 5. Check Event's detail has Contact (Contact Script03_A) in Links section
        boolean flag1 = agenda.checkEventItemDetails("Contact Scripts03_Event A",false,firstName+" "+lastName1,
                "General",firstName+" "+lastName1,testCase8);
        // 6. Delete Event from detail
        common.trashItemByDetails(true);
        // 7. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase8);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }
    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete Contact from detail
        common.clearView();

        // Delete sent emails
        common.clickViewBottomBar("Email");
        email.navigateToImapFolder("Sent");
        common.clearView();

        // Empty trash collection
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();

        // close app
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
