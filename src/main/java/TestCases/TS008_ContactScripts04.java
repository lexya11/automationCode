package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.annotations.*;
import org.testng.*;

public class TS008_ContactScripts04 extends SetupServer{
    MobileCapabilities capabilities;
    SignInKeywords signin;
    HomeKeywords home;
    SettingsKeywords settings;
    ContactKeywords contact;
    CommonKeywords common;
    EmailKeywords email;
    CollectionKeywords collection;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String floEmailAddress1 = "khuong001@123flo.com";
    String floEmailAddress2 = "khuong002@123flo.com";
    String floEmailAddress3 = "khuong003@123flo.com";
    String floEmailAddress4 = "khuong004@123flo.com";

    String firstName = "Contact";
    String lastName1 = "Scripts04_A";
    String lastName2 = "Scripts04_B";
    String lastName3 = "Scripts04_C";
    String lastName4 = "Scripts04_D";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        email = new EmailKeywords(this);
        collection = new CollectionKeywords(this);
        home = new HomeKeywords(    this);
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
                // Open Contact View
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

    // TC: Check Contact marked as Vip in List
    String testCase1 = "TS008_ContactScripts04_Contact1_markVIPContactInList";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact1_markVIPContactInList(){
        Log.startTestCase(testCase1);
        // 1. Filter by All Collections, and type "All"
        contact.filterByType("All");
        // 2. Create normal Contact (Bottom)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName1,"Company1","123",
                floEmailAddress1,false,true);
        // 3. Mark VIP a normal contact in Contact list
        contact.setVipContact(false,firstName,lastName1,true);
        // 4. filter by Vip
        contact.filterByType("Vip");
        // 5. Check about Contact
        flag = contact.checkContactItemView(firstName,lastName1,floEmailAddress1,true,testCase1);
        Log.endTestCase(testCase1);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Check Contact marked as Vip in Detail
    String testCase2 = "TS008_ContactScripts04_Contact2_markVIPContactInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact2_markVIPContactInDetail(){
        Log.startTestCase(testCase2);
        // 1. Filter by All Collections, and type "All"
        contact.filterByType("All");
        // 2. Create normal Contact (Bottom)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName2,"Company2","1234",
                floEmailAddress2,false,true);
        // 3. Open detail Contact
        contact.openContactItem(firstName,lastName2);
        // 4. Mark VIP a normal contact in detail
        contact.setVipContact(true,firstName,lastName2,true);
        // 5. Back to Contact list
        common.clickElementButton("Done");
        // 6. Check about Contact
        flag = contact.checkContactItemView(firstName,lastName2,floEmailAddress2,true,testCase2);
        Log.endTestCase(testCase2);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Check Contact Un-Vip as Vip in List
    String testCase3 = "TS008_ContactScripts04_Contact3_UnVIPContactInList";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact3_UnVIPContactInList(){
        Log.startTestCase(testCase3);
        // 1. Filter by All Collections, and type "Vip"
        contact.filterByType("Vip");
        // 2. Create Vip Contact (Bottom)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName3,"Company3","12345",
                floEmailAddress3,true,true);
        // 3. Mark Un-VIP a Vip contact in Contact list
        contact.setVipContact(false,firstName,lastName3,false);
        // 4. Filter by All
        contact.filterByType("All");
        // 5. Check about Contact
        flag = contact.checkContactItemView(firstName,lastName3,floEmailAddress3,false,testCase3);
        Log.endTestCase(testCase3);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Check Contact Un-Vip as Vip in detail
    String testCase4 = "TS008_ContactScripts04_Contact4_UnVIPContactInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact4_UnVIPContactInDetail(){
        Log.startTestCase(testCase4);
        // 1. Filter by All Collections, and type "Vip"
        contact.filterByType("Vip");
        // 2. Create VIP Contact (Bottom)
        common.openNewItemView();
        contact.addNewContact(firstName,lastName4,"Company4","123456",
                floEmailAddress4,true,true);
        // 3. Open detail a Vip contact
        contact.openContactItem("Contact","Scripts04_D");
        // 4. Mark UnVip that Contact
        contact.setVipContact(true,firstName,lastName4,false);
        // 5. Return to Contact list filter by Vip
        common.clickElementButton("Done");
        // 6. Filter by All
        contact.filterByType("All");
        // 7. Check about Contact
        flag = contact.checkContactItemView(firstName,lastName4,floEmailAddress4,false,testCase4);
        Log.endTestCase(testCase4);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Add Contact to Collection in Detail
    String testCase5 = "TS008_ContactScripts04_Contact5_addCollectionToContactInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact5_addCollectionToContactInDetail(){
        Log.startTestCase(testCase5);
        // 1. Open detail that Contact (Contact Scripts04_C)
        contact.openContactItem(firstName,lastName3);
        // 2. Add Contact to Collection (Play,Work)
        common.addMoveToCollection(true,"Add","Play,Work");
        // 3. Check detail of Contact
        flag = contact.checkContactItemDetails(firstName,lastName3,"Company3","12345",
                floEmailAddress3,false,"","General,Play,Work",testCase5);
        // 4. Back to Contact list
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase5);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Add Collection to Contact VIP in Detail
    String testCase6 = "TS008_ContactScripts04_Contact6_addCollectionToVipContactInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact6_addCollectionToVipContactInDetail(){
        Log.startTestCase(testCase6);
        // 1. Filter by VIP
        contact.filterByType("Vip");
        // 2. Open detail that Contact (Contact Scripts04_A)
        contact.openContactItem(firstName,lastName1);
        // 3. Add Contact to Collection (Play,Work)
        common.addMoveToCollection(true,"Add","Play,Work");
        // 4. Check detail of Contact
        flag = contact.checkContactItemDetails(firstName,lastName1,"Company1","123",
                floEmailAddress1,true,"","General,Play,Work",testCase6);
        // 5. Back to Contact list
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase6);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Move Contact to a Collection in Detail
    String testCase7 = "TS008_ContactScripts04_Contact7_moveCollectionToContactInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact7_moveCollectionToContactInDetail(){
        Log.startTestCase(testCase7);
        // 1. Filter type by "All"
        contact.filterByType("All");
        // 2. Open detail that Contact (Contact Scripts04_D)
        contact.openContactItem(firstName,lastName4);
        // 3. Move Contact to Collection (Play)
        common.addMoveToCollection(true,"Move","Play");
        // 4. Check detail of Contact
        flag = contact.checkContactItemDetails(firstName,lastName4,"Company4","123456",
                floEmailAddress4,false,"","Play",testCase7);
        // 5. Back to Contact list
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase7);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Move Contact VIP to a Collection in Detail
    String testCase8 = "TS008_ContactScripts04_Contact8_moveCollectionToVipContactInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts04_Contact8_moveCollectionToVipContactInDetail(){
        Log.startTestCase(testCase8);
        // 1. Filter type by "Vip"
        contact.filterByType("Vip");
        // 2. Open detail that Contact (Contact Scripts04_B)
        contact.openContactItem(firstName,lastName2);
        // 3. Move Contact to Collection (Play)
        common.addMoveToCollection(true,"Move","Work");
        // 4. Check detail of Contact
        flag = contact.checkContactItemDetails(firstName,lastName2,"Company2","1234",
                floEmailAddress2,true,"","Work",testCase8);
        // 5. Back to Contact list
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase8);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete all Contacts
        contact.filterByType("All");
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
