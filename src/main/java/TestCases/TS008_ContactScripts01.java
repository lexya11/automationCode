package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.*;
import org.testng.annotations.*;

public class TS008_ContactScripts01 extends SetupServer {
    MobileCapabilities capabilities;
    SignInKeywords signin;
    HomeKeywords home;
    SettingsKeywords settings;
    ContactKeywords contact;
    CommonKeywords common;
    EmailKeywords email;
    CollectionKeywords collection;
    boolean flag = true;

    String username = Constants.USERNAME02;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

//    String floEmailAddress1 = "bi_test01@flomail.net";
    String floEmailAddress2 = "khuong001@123flo.com";
//    String floEmailAddress3 = "bi_test03@flomail.net";
    String floEmailAddress4 = "khuong002@123flo.com";
    String floEmailAddress5 = "khuong003@123flo.com";

    String firstName = "Contact";
//    String lastName1 = "Scripts01_A";
    String lastName2 = "Scripts01_B";
//    String lastName3 = "Scripts01_C";
    String lastName4 = "Scripts01_D";
    String lastName5 = "Scripts01_E";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        email = new EmailKeywords(this);
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        collection = new CollectionKeywords(this);

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
                // One Contact View
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

    // TC: Verify Contact View
    String testCase1 = "TS008_ContactScripts01_Contact1_verifyContactView";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts01_Contact1_verifyContactView(){
        Log.startTestCase(testCase1);
        // 1. Check Home button in left
        flag = common.checkStaticButton("settings icon","Right",testCase1);
        // 3. Check view header
        boolean flag1 = common.checkLocalFilter("All Collections",testCase1);
        // 4. Check current filter by type (All)
        boolean flag2 = contact.checkFilterByType("All",testCase1);
        Log.endTestCase(testCase1);
        if(flag == false || flag1 == false || flag2 == false ){
            Assert.fail("Test case is failed");
        }
    }

//    // TC: Add New Contact from Home View
//    String testCase2 = "TS008_ContactScripts01_Contact2_addContactFromHomeView";
//    @Test
//    public void TS008_ContactScripts01_Contact2_addContactFromHomeView(){
//        Log.startTestCase(testCase2);
//        // 1. Back to Home View
//        home.clickTopLeftbutton("Home");
//        // 2. Tap on "Contact" icon to open create New Contact View
//        home.openNewScreenPlusIcon("Contact");
//        // 3. Add new Contact (Contact Scripts01_A), input all info NOT mark VIP
//        contact.addNewContact("None",firstName,lastName1,"Company1","123",
//                floEmailAddress1,false,true);
//        // 4. Open Contact View
//        common.clickViewBottomBar("Contact");
//        // 5. Check new Contact created in Contact View (Contact Scripts01_A)
//        flag = contact.checkContactItemView(firstName,lastName1,floEmailAddress1,false,testCase2);
//        Log.endTestCase(testCase2);
//        if(flag == false){
//            Assert.fail("Test case is failed");
//        }
//    }

    // TC: Add New Contact from Plus icon
    String testCase3 = "TS008_ContactScripts01_Contact3_addContactFromPlusIcon";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts01_Contact3_addContactFromPlusIcon(){
        Log.startTestCase(testCase3);
        // 1. Add new Contact (Contact Scripts01_B), input all info NOT mark VIP
        common.openNewItemView();
        contact.addNewContact(firstName,lastName2,"Company2","456",
                floEmailAddress2,false,true);
        // 2. Check new Contact created in Contact View (Contact Scripts01_B)
        flag = contact.checkContactItemView(firstName,lastName2,floEmailAddress2,false,testCase3);
        Log.endTestCase(testCase3);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

//    // TC: Add New Contact from Bottom Bar
//    String testCase4 = "TS008_ContactScripts01_Contact4_addContactFromBottomBar";
//    @Test
//    public void TS008_ContactScripts01_Contact4_addContactFromBottomBar(){
//        Log.startTestCase(testCase4);
//        // 1. Add new Contact (Contact Scripts01_C), input all info mark as VIP
//        contact.addNewContact("Bottom Bar",firstName,lastName3,"Company3","789",
//                floEmailAddress3,true,true);
//        // 2. Check new Contact created in Contact View (Contact Scripts01_C)
//        flag = contact.checkContactItemView(firstName,lastName3,floEmailAddress3,true,testCase4);
//        Log.endTestCase(testCase4);
//        if(flag == false){
//            Assert.fail("Test case is failed");
//        }
//    }

    // TC: Filter Contact by "Recent", create new Contact then check Local filter and number of Contact View
    String testCase5 = "TS008_ContactScripts01_Contact5_filterByRecentAddNewContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts01_Contact5_filterByRecentAddNewContact(){
        Log.startTestCase(testCase5);
        // 1. Filter by 'Recent' from Navigation Bar
        contact.filterByType("Recent");
        // 2. Check filter by type (Recent)
        flag = contact.checkFilterByType("Recent",testCase5);
        // 3. Add new Contact (Contact Scripts01_D), input all info NOT mark as VIP
        common.openNewItemView();
        contact.addNewContact(firstName,lastName4,"Company4","91011",
                floEmailAddress4,false,true);
        // 4. Check Local Filter and number of Contact
        boolean flag1 = contact.checkContactLocalFilter("All Collections",2,testCase5);
        // 5. Check new Contact created in Contact View (Contact Scripts01_D)
        boolean flag2 = contact.checkContactItemView(firstName,lastName4,floEmailAddress4,false,testCase5);
        // 6. Open Contact created (Contact Scripts01_D)
        contact.openContactItem(firstName,lastName4);
        // 7. Check detail of Contact (Contact Scripts01_D)
        boolean flag3 = contact.checkContactItemDetails(firstName,lastName4,"Company4","91011",
                floEmailAddress4,false,"None","General",testCase5);
        // 8. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase5);
        if(flag == false || flag1 == false || flag2 == false || flag3 == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Filter Contact by "VIP", create new VIP Contact then check Local filter and number of Contact View
    String testCase6 = "TS008_ContactScripts01_Contact6_filterByVIPAddNewContact";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts01_Contact6_filterByVIPAddNewContact(){
        Log.startTestCase(testCase6);
        // 1. Filter by 'VIP' from Navigation Bar
        contact.filterByType("VIP");
        // 2. Check filter by type (VIP)
        flag = contact.checkFilterByType("VIP",testCase6);
        // 3. Add new Contact (Contact Scripts01_E), input all info mark as VIP
        common.openNewItemView();
        contact.addNewContact(firstName,lastName5,"Company5","101112",
                floEmailAddress5,true,true);
        // 4. Check Local Filter and number of Contact
        boolean flag1 = contact.checkContactLocalFilter("All Collections",1,testCase6);
        // 5. Check new Contact created in Contact View (Contact Scripts01_E)
        boolean flag2 = contact.checkContactItemView(firstName,lastName5,floEmailAddress5,
                true,testCase6);
        // 6. Open Contact created (Contact Scripts01_E)
        contact.openContactItem(firstName,lastName5);
        // 7. Check detail of Contact (Contact Scripts01_E)
        boolean flag3 = contact.checkContactItemDetails(firstName,lastName5,"Company5","101112",
                floEmailAddress5,true,"None","General",testCase6);
        // 8. Back to Contact View
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase6);
        if(flag == false || flag1 == false || flag2 == false || flag3 == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Filter Contact by "All", check Local filter and number of Contact View
    String testCase7 = "TS008_ContactScripts01_Contact7_filterByAllCheckAndDeleteContact";
    @Test(dataProvider = "checkAndDeleteContact", retryAnalyzer = RetryFailedTestCases.class)
    public void TS008_ContactScripts01_Contact7_filterByAllCheckAndDeleteContact(String testCase, String localFilter, String number,
                                          String firstName, String lastName, String company, String phone, String email, String vip){
        Log.startTestCase(testCase7);
        int Number = (int)Double.parseDouble(number.toString());
        String Phone = phone.split("\\.")[0];
        boolean isVIP = Boolean.valueOf(vip);

        // 1. Filter by 'All' from Navigation Bar
        contact.filterByType("All");
        // 2. Check Local Filter and number of Contact
        flag = contact.checkContactLocalFilter(localFilter, Number, testCase);
        // 3. Open detail of Contact created
        contact.openContactItem(firstName, lastName);
        // 4. Check detail of Contact
        boolean flag2 = contact.checkContactItemDetails(firstName, lastName, company, Phone, email, isVIP,
                "None","General", testCase);
        common.trashItemByDetails(true);
        Log.endTestCase(testCase7);
        if(flag == false || flag2 == false){
            Assert.fail("Test case is failed");
        }
    }
    @DataProvider
    public Object[][] checkAndDeleteContact() throws Exception{
        Object[][] checkAndDeleteContact = ExcelUtils.getTableArray(Constants.CONTACT_DATA,"checkAndDeleteContact");
        return (checkAndDeleteContact);
    }

    // TC: Change "Display order" and "Display in list" in Settings > Contacts then check Contact's List
    String testCase8 = "TS008_ContactScripts01_Contact8_changeDisplayInSettings";
    @Test(dataProvider = "changeDisplayInSettings", retryAnalyzer =  RetryFailedTestCases.class)
    public void TS008_ContactScripts01_Contact8_changeDisplayInSettings(String testCase, String displayOrder, String displayInList, String firstName, String lastName, String more, String vip){
        Log.startTestCase(testCase8);
        String More = more;
        if(displayInList.contains("Phone")){
          More = more.split("\\.")[0];
        }
        boolean isVIP = Boolean.valueOf(vip);
        // 1. Open Settings View
        common.openSettingsView();
        // 2. Open Settings > Contacts View
        settings.clickSettingOptionList("Contacts");
        // 3. Set Display Order
        settings.setDisplayOrder(displayOrder);
        // 4. Set Display in list
        settings.setDisplayInList(displayInList);
        // 5. Open Contact View
        common.clickViewBottomBar("Contact");
        // 6. Check Display of Contact in Contact View
        flag = contact.checkContactItemView(firstName, lastName, More, isVIP, testCase);
        Log.endTestCase(testCase8);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }
    @DataProvider
    public Object[][] changeDisplayInSettings() throws Exception{
        Object[][] changeDisplayInSettings = ExcelUtils.getTableArray(Constants.CONTACT_DATA,"changeDisplayInSettings");
        return (changeDisplayInSettings);
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete Contact (Contact A) by swipe action
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
            sendEmail.getResults(context);
            sendEmail.sendGmail(this.getClass().getSimpleName(), device, buildVersion, Constants.EMAIL_TO, Constants.EMAIL_CC);
        }
    }
}
