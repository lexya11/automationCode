package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.annotations.*;
import org.testng.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TS008_ContactScripts05 extends SetupServer{
    MobileCapabilities capabilities;
    SignInKeywords signin;
    HomeKeywords home;
    SettingsKeywords settings;
    ContactKeywords contact;
    CommonKeywords common;
    EmailKeywords email;
    TodoKeywords todo;
    CollectionKeywords collection;
    NoteKeywords note;
    Date currentDate = new Date();
    String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
    boolean flag = true;

    String username = Constants.USERNAME05;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String firstName1 = "Mary";
    String firstName2 = "Peter";
    String lastName1 = "Jane";
    String lastName2 = "Parker";
    String titleTodo = "ContactScripts05_Todo 1";
    String titleNote = "ContactScripts05_Note 1";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        home = new HomeKeywords(    this);
        email = new EmailKeywords(this);
        settings = new SettingsKeywords(this);
        todo = new TodoKeywords(this);
        note = new NoteKeywords(this);
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
                statusLogin = true;
            }catch (Exception e){
                count = count+1;
                if(count == 2){
                    throw e;
                }
            }
        }

    }

    // TC: Add Linked To.Do for Contact in Detail
    String testCase1 = "TS008_ContactScripts05_Contact1_addLinkedTodoInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact1_addLinkedTodoInDetail(){
        Log.startTestCase(testCase1);
        // pre-test add new To.Do
        common.clickViewBottomBar("ToDo");
        todo.addQuickTodo(titleTodo,"Today");

        // 1. Open Contact View
        common.clickViewBottomBar("Contact");
        common.clearView();
        // 2. Filter type by "All"
        contact.filterByType("All");
        // 3. Create new Contact (Contact A)
        common.openNewItemView();
        contact.addNewContact(firstName1,lastName1,"Company1","123","abc@gmail.com",false,true);
        // 4. Open detail that Contact
        contact.openContactItem(firstName1,lastName1);
        // 5. Add To.Do created into Links section
        common.addLinkedItemInDetail("ToDo",titleTodo);
        // 6. Check detail of Contact has To.Do linked
        flag = contact.checkContactItemDetails(firstName1,lastName1,"Company1","123","abc@gmail.com",false,titleTodo,"General",testCase1);
        // 7. Back to Contact list
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase1);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Add Linked Note for Contact in Detail
    String testCase2 = "TS008_ContactScripts05_Contact2_addLinkedNoteInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact2_addLinkedNoteInDetail(){
        Log.startTestCase(testCase2);
        // pre-test add new Note
        common.clickViewBottomBar("Note");
        note.addQuickNote(titleNote);

        // 1. Open Contact View
        common.clickViewBottomBar("Contact");
        // 2. Filter type by "All"
        contact.filterByType("All");
        // 3. Create new Contact (Contact B)
        common.openNewItemView();
        contact.addNewContact(firstName2, lastName2,"Company2","1234","abcd@gmail.com",true,true);
        // 4. Open detail that Contact
        contact.openContactItem(firstName2,lastName2);
        // 5. Add Note created into Links section
        common.addLinkedItemInDetail("Note",titleNote);
        // Generate String today for clean up
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        // 6. Check detail of Contact has Note linked
        flag = contact.checkContactItemDetails(firstName2,lastName2,"Company2","1234","abcd@gmail.com",true,titleNote,"General",testCase2);
        // 7. Back to Contact list
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase2);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Add Linked Contact for Contact in Detail
    String testCase3 = "TS008_ContactScripts05_Contact3_addLinkedContactInDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact3_addLinkedContactInDetail(){
        Log.startTestCase(testCase3);
        // 1. Filter type by "All"
        contact.filterByType("All");
        // 2. Open detail that Contact
        contact.openContactItem(firstName1,lastName1);
        // 3. Add Contact A created into Links section
        common.addLinkedItemInDetail("Contact",firstName2+" "+lastName2);
        // 4. Check detail of Contact A has Contact B linked
        flag = contact.checkContactItemDetails(firstName1,lastName1,"Company1","123",
                "abc@gmail.com",false,firstName2+" "+lastName2,"General",testCase3);
        // 5. Back to Contact list
        common.clickElementButton("Contacts");
        Log.endTestCase(testCase3);
        if(flag == false){
            Assert.fail("Test case is failed");
        }
    }

    // TC: Search Contact By filter All Collections
    String testCase4 = "TS008_ContactScripts05_Contact4_searchContactByAllCollections";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact4_searchContactByAllCollections(){
        Log.startTestCase(testCase4);
        // 1. Filter by All Collections, and type "All"
        contact.filterByType("All");
        // 2. Input to Search a Contact is existing in list
        common.searchItem("Contact",lastName1);
        // 3. Check contact exist in Search Result
        flag = common.checkItemExist(firstName1+" "+lastName1,"Exist",testCase4);
        // 4. Back to Contact list
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase4);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Search contact not depends to Filter by a collections
    String testCase5 = "TS008_ContactScripts05_Contact5_searchContactByACollection";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact5_searchContactByACollection(){
        Log.startTestCase(testCase5);
        // 1. Filter by a Collection (Home), and type "All"
        common.selectLocalFilter("Home");
        contact.filterByType("All");
        // 2. Input to Search a Contact
        common.searchItem("Contact",firstName1);
        // 3. Check contact exist in Search Result
        flag = common.checkItemExist(firstName1+" "+lastName1,"Exist",testCase5);
        // 4. Back to Contact list
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase5);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Search contact not depends to all collection Local Filter and filter by type
    String testCase6 = "TS008_ContactScripts05_Contact6_searchContactNotDependAllCollections";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact6_searchContactNotDependAllCollections(){
        Log.startTestCase(testCase6);
        // 1. Filter by All Collections, and type "Vip"
        common.selectLocalFilter("All Collections");
        contact.filterByType("Vip");
        // 2. Input to Search a Contact is existing in list
        common.searchItem("Contact",firstName2);
        // 3. Check contact exist in Search Result
        flag = common.checkItemExist(firstName2+" "+lastName2,"Exist",testCase6);
        // 4. Back to Contact list
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase6);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Search contact not depends to a collection Local Filter and filter by type
    String testCase7 = "TS008_ContactScripts05_Contact7_searchContactNotDependACollection";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact7_searchContactNotDependACollection(){
        Log.startTestCase(testCase7);
        // 1. Filter by a Collection (Home), and type "Vip"
        common.selectLocalFilter("Home");
        contact.filterByType("Vip");
        // 2. Input to Search a Contact
        common.searchItem("Contact",lastName2);
        // 3. Check contact exist in Search Result
        flag = common.checkItemExist(firstName2+" "+lastName2,"Exist",testCase6);
        // 4. Back to Contact list
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase7);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Search contact not depends to VIP filter
    String testCase8 = "TS008_ContactScripts05_Contact8_searchContactNotDependVipFilter";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact8_searchContactNotDependVipFilter(){
        Log.startTestCase(testCase8);
        // 1. Filter by a Collection (Home), and type "Vip"
        contact.filterByType("Vip");
        // 2. Input to Search a Contact
        common.searchItem("Contact",firstName1);
        // 3. Check contact exist in Search Result
        flag = common.checkItemExist(firstName1+" "+lastName1,"Exist",testCase8);
        // 4. Back to Contact list
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase8);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Search contact not depends to VIP filter
    String testCase9 = "TS008_ContactScripts05_Contact9_searchContactNotExist";
    String keyword = "Contact A";
    String noResult = "Sorry, No results were found";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS008_ContactScripts05_Contact9_searchContactNotExist(){
        Log.startTestCase(testCase9);
        // 1. Filter by a Collection (Home), and type "All"
        contact.filterByType("All");
        // 2. Input to Search a Contact
        common.searchItem("Contact",keyword);
        // 3. Check not found contact in Search Result
        flag = common.checkItemExist(noResult,"Exist",testCase9);
        // 4. Back to Contact list
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase9);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete all items
        common.selectLocalFilter("All Collections");
        contact.filterByType("All");
        common.clearView();

        common.clickViewBottomBar("ToDo");
        common.clearView();
        common.clickViewBottomBar("Note");
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
