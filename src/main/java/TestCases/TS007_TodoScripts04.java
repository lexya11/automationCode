package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TS007_TodoScripts04 extends SetupServer{
    MobileCapabilities capabilities;
    SignInKeywords signin;
    TodoKeywords todo;
    CommonKeywords common;

    EmailKeywords email;
    SettingsKeywords settings;
    NoteKeywords note;
    ContactKeywords contact;
    CollectionKeywords collection;
    String today = "";
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleTodo1 = "TodoScripts04_Todo1";
    String titleTodo2 = "TodoScripts04_CheckList";
    String titleTodo3 = "TodoScripts04_Full Todo";

    String titleEmail1 = "TodoScripts04_Email1";
    String titleEmail2 = "TodoScripts04_Email2";

    String titleNote1 = "TodoScripts04_Note1";
    String titleNote2 = "TodoScripts04_Note2";

    @BeforeClass
    public void Setup(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        signin = new SignInKeywords(this);
        todo = new TodoKeywords(this);
        common = new CommonKeywords(this);

        settings = new SettingsKeywords(this);
        email = new EmailKeywords(this);
        contact = new ContactKeywords(this);
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
                signin.login(username, password, true);

                // Close start Flo popup
                common.closeStartFlo();

                // Go to Setting > To.do > set DefaultDueDate None
                common.openSettingsView();
                settings.clickSettingOptionList("ToDo's");
                settings.setDefaultDueDate("None");
                // Open To.Do View
                common.clickViewBottomBar("ToDo");
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

    /**
     * Swipe left to right To.Do and create other linked email
     */
    String testCase1 = "TS007_TodoScripts04_ToDo01_createEmailBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo01_createEmailBySwiping(){
        Log.startTestCase(testCase1);

        // 1. Add new To.Do (Test 1)
        common.openNewItemView();
        todo.addFullTodo(titleTodo1,false,false,"None","","","",true);
        // 2. Swipe left to right To.Do (Test 1) and click Email icon
        todo.sendEmailBySwipe(titleTodo1,"","khuong001@123flo.com","","", titleEmail1,"Send Email from ToDo","","Add","General",true);
        // 3. Check To.Do show Link icon
        flag = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,false,true,testCase1);
        // 4. Open To.Do (Test 1)
        todo.openTodoItem(titleTodo1);
        // 5. Check Link has new Email
        boolean flag1 = common.checkItemExist(titleEmail1,"Exist",testCase1);
        // 6. Open Email's detail from Links section
        common.openItem("Email", titleEmail1);
        // 7. Check Link has To.Do (Test 1)
        boolean flag2 = common.checkItemExist(titleTodo1,"Exist",testCase1);
        // 8. Trash Email from detail (1)
        common.trashItemByDetails(true);
        // 9. Back to To.Do List
        common.clickElementButton("ToDo's");
        // 10. Check To.Do (Test 1) still shows Link icon
        boolean flag3 = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,false,true,testCase1);
        Log.endTestCase(testCase1);
        if(!(flag & flag1 & flag2 & flag3))
            Assert.fail("Test case is failed");
    }

    /**
     * Swipe left to right To.Do and create other linked Note
     */
    String testCase2 = "TS007_TodoScripts04_ToDo02_createNoteBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo02_createNoteBySwiping(){
        Log.startTestCase(testCase2);
        // 1. Swipe left to right To.Do (Test 1) and click Note icon
        todo.addNoteBySwipe(titleTodo1, titleNote1,"Create Note from ToDo",true);
        // 2. Check To.Do show Link icon
        flag = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,false,true,testCase2);
        // 3. Open detail of To.Do (Test 1)
        todo.openTodoItem(titleTodo1);
        // 4. Check Note (Note 1) in Link section
        boolean flag1 = common.checkItemExist(titleNote1,"Exist",testCase2);
        // 5. Open Note's detail from Links section
        common.openItem("Note", titleNote1);
        // 6. Check To.Do (Test 1) in Links section
        boolean flag2 = common.checkItemExist(titleTodo1,"Exist",testCase2);
        // 7. Trash Note from detail
        common.trashItemByDetails(true);
        // 8. Back to To.Do List
        common.clickElementButton("ToDo's");
        // 9. Check To.Do (Test 1) still shows Link icon
        boolean flag3 = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,false,true,testCase2);
        Log.endTestCase(testCase2);
        if(!(flag & flag1 & flag2 & flag3))
            Assert.fail("Test case is failed");
    }

    /**
     * TC: Swipe right to left To.Do and move Collection
     */
    String testCase4 = "TS007_TodoScripts04_ToDo03_moveCollectionBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo03_moveCollectionBySwiping(){
        Log.startTestCase(testCase4);
        // 1. Swipe right to left To.Do (Test 1) and click Collection icon
        todo.openCollectionBySwipe(titleTodo1);
        // 2. Move to Collection Sample
        common.addMoveToCollection(false,"Move","Sample");
        // 3. Open detail of To.Do (Test 1)
        todo.openTodoItem(titleTodo1);
        // 4. Check Collection moved
        flag = todo.checkTodoItemDetails(titleTodo1,"",false,"","","Sample","",testCase4);
        // 5. Back to To.Do List
        common.clickElementButton("ToDo's");
        Log.endTestCase(testCase4);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * TC: Swipe right to left To.Do and add Collection
     */
    String testCase5 = "TS007_TodoScripts04_ToDo04_addCollectionBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo04_addCollectionBySwiping(){
        Log.startTestCase(testCase5);
        // 1. Swipe right to left To.Do (Test 1) and click Collection icon
        todo.openCollectionBySwipe(titleTodo1);
        // 2. Add Collection (Home, Play)
        common.addMoveToCollection(false,"Add","Home,Play");
        // 3. Open Detail of To.Do (Test 1)
        todo.openTodoItem(titleTodo1);
        // 4. Check Collection added
        flag = todo.checkTodoItemDetails(titleTodo1,"",false,"","","Home,Play","", testCase5);
        // 5. Back to To.Do List
        common.clickElementButton("ToDo's");
        Log.endTestCase(testCase5);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Add To.Do with Checklist
     */
    String testCase6 = "TS007_TodoScripts04_ToDo05_addTodoWithChecklist";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo05_addTodoWithChecklist(){
        Log.startTestCase(testCase6);
        // 1. Add new To.Do (Checklist)
        common.openNewItemView();
        todo.addNewTodo(titleTodo2,false,false);
        // 2. Add Checklist (Item 1) and Done
        todo.addCheckItem("Item 1",true);
        // 3. Add Checklist (Item 2) and Done
        todo.addCheckItem("Item 2",true);
        // 4. Add Checklist (Item 3) and Done
        todo.addCheckItem("Item 3",true);
        // 5. Tap on Done button
        common.clickElementButton("Done");
        // 6. Check To.Do added (Checklist) with checklist icon
        flag = todo.checkTodoItemView(titleTodo2,"Normal","",false,true,false,false,testCase6);
        Log.endTestCase(testCase6);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * TC: Remove checklist of To.Do
     */
    String testCase7 ="TS007_TodoScripts04_ToDo06_removeChecklistOfToDo";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo06_removeChecklistOfToDo(){
        Log.startTestCase(testCase7);
        // 1. Open detail of To.Do added (Checklist)
        todo.openTodoItem(titleTodo2);
        // 2. Delete checklist (Item 1)
        todo.deleteCheckItem("Item 1");
        // 3. Delete checklist (Item 2)
        todo.deleteCheckItem("Item 2");
        // 4. Delete checklist (Item 3)
        todo.deleteCheckItem("Item 3");
        // 5. Tap on Done button
        common.clickElementButton("Done");
        // 6. Check To.Do added (Checklist) with no checklist icon
        flag = todo.checkTodoItemView(titleTodo2,"Normal","",false,false,false,false,testCase7);
        // 7. Trash To.Do (Checklist)
        todo.deleteTodoBySwipe(titleTodo2,true);
        Log.endTestCase(testCase7);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Create new To.Do, input all fields
     */
    String testCase8 = "TS007_TodoScripts04_ToDo07_addFullToDo";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo07_addFullToDo(){
        Log.startTestCase(testCase8);
        // 1. Add full new To.Do (Full To.do)
        common.openNewItemView();
        todo.addFullTodo(titleTodo3,true,true,"Today","Medium","Test Full ToDo","Home,Play",true);
        // 2. Filter by 'Done'
        todo.filterByType("Top","Done");
        // 3. Open detail of new To.Do added (Full To.do)
        todo.openTodoItem(titleTodo3);
        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        // 4. Check detail of To.Do (Full To.do)
        flag = todo.checkTodoItemDetails(titleTodo3,today,true,"Medium","Test Full ToDo","Home,Play","", testCase8);
        // 5. Back to To.Do View
        common.clickElementButton("ToDo's");
        Log.endTestCase(testCase8);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * TC: Send an Email from details view
     */
    String testCase9 = "TS007_TodoScripts04_ToDo08_sendEmailFromDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo08_sendEmailFromDetail(){
        Log.startTestCase(testCase9);
        // 1. Open detail of new To.Do added (Full To.do)
        todo.openTodoItem(titleTodo3);
        // 2. Send Email from detail
        todo.sendEmailByDetails("","khuong001@123flo.com","","", titleEmail2,"Send Email from Detail","","Add","General",true);
        // 3. Check Email in Links section
        flag = common.checkItemExist(titleEmail2,"Exist",testCase9);
        // 4. Back to To.Do View
        common.clickElementButton("ToDo's");
        // 5. Check To.Do (Full To.do) in List has Linked icon
        boolean flag1 = todo.checkTodoItemView(titleTodo3,"Due",today,true,false,true,true,testCase9);
        // 6. Open detail of new To.Do added (Full To.do)
        todo.openTodoItem(titleTodo3);
        // 7. Open detail Email in Links section
        common.openItem("Email", titleEmail2);
        // 8. Trash Email
        common.trashItemByDetails(true);
        // 9. Back to To.Do View
        common.clickElementButton("ToDo's");
        // 10. Check To.Do (Full To.do) in List has Linked icon
        boolean flag2 = todo.checkTodoItemView(titleTodo3,"Due",today,true,false,true,true,testCase9);
        Log.startTestCase(testCase9);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    /**
     * TC: Create new Note from details view
     */
    String testCase10 = "TS007_TodoScripts04_ToDo09_createNoteFromDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo09_createNoteFromDetail(){
        Log.startTestCase(testCase10);
        // 1. Open detail of new To.Do added (Full To.do)
        todo.openTodoItem(titleTodo3);
        // 2. Add new Note from detail (TodoScripts04_Note 2)
        todo.addNoteByDetails(titleNote2,"New Note from detail",true);
        // 3. Check Note added in Links section
        flag = common.checkItemExist(titleNote2,"Exist",testCase10);
        // 4. Back to To.Do View
        common.clickElementButton("ToDo's");
        // 5. Check To.Do (Full To.do) in List has Linked icon
        boolean flag1 = todo.checkTodoItemView(titleTodo3,"Due",today,true,false,true,true,testCase10);
        // 6. Open detail of new To.Do added (Full To.do)
        todo.openTodoItem(titleTodo3);
        // 7. Open detail of Note in Links section (Note 1)
        common.openItem("Note", titleNote2);
        // 8. Trash Note (Note 1)
        common.trashItemByDetails(true);
        // 9. Back to To.Do View
        common.clickElementButton("ToDo's");
        // 10. Check To.Do (Full To.do) still has Linked icon
        boolean flag2 = todo.checkTodoItemView(titleTodo3,"Due",today,true,false,true,true,testCase10);
        Log.startTestCase(testCase10);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    /**
     * Check linked icon is removed after empty trash collection
     */
    String testCase11 = "TS007_TodoScripts04_ToDo10_checkLinkedIconIsRemoved";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts04_ToDo10_checkLinkedIconIsRemoved(){
        Log.startTestCase(testCase11);
        // 1. To go Collection > Trash
        common.clickViewBottomBar("Collection");
        // 2. Empty trash
        collection.emptyTrashCollection();
        // 3. Go to to.do view
        common.clickViewBottomBar("Todo");
        // 4. Check to.do item not show Link icon (Full To.do) and then trash it
        boolean flag1 = todo.checkTodoItemView(titleTodo3,"Due",today,true,false,true,false,testCase11);
        todo.deleteTodoBySwipe(titleTodo3,true);
        // 5. Filter to.do by all
        todo.filterByType("Top","All");
        // 6. Check To.Do item not show Link icon (Test 1) and then trash it
        flag = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,false,false,testCase11);
        todo.deleteTodoBySwipe(titleTodo1,true);
        Log.endTestCase(testCase11);
        if(!flag & flag1)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Trash Contacts
        common.clickViewBottomBar("Contact");
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
            Log.startTestCase("Send Email Report");
            sendEmail.getResults(context);
            sendEmail.sendGmail(this.getClass().getSimpleName(), device, buildVersion, Constants.EMAIL_TO, Constants.EMAIL_CC);
        }
    }
}
