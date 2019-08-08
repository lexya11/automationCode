package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import net.bytebuddy.implementation.bytecode.Throw;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TS010_CollectionScripts03 extends SetupServer {
    MobileCapabilities capabilities;
    SignInKeywords signin;
    ContactKeywords contact;
    AgendaKeywords agenda;
    CommonKeywords common;
    TodoKeywords todo;
    EmailKeywords email;
    NoteKeywords note;
    CollectionKeywords collection;
    SettingsKeywords setting;
    String today = "";
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleTodo1 = "CollectionScripts03_Todo 1";
    String subjectEmail = "CollScripts03_Send Email";
    String titleNote = "CollectionScripts03_Note A";


    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        contact = new ContactKeywords(this);
        agenda = new AgendaKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        note = new NoteKeywords(this);
        todo = new TodoKeywords(this);
        collection = new CollectionKeywords(this);
        email = new EmailKeywords(this);
        setting = new SettingsKeywords(this);

        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{capabilities = new MobileCapabilities(device, false);
                SetUp(capabilities);

                // Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username,password,true);

                // Close Start Flo popup
                common.closeStartFlo();
                // Open Collection view
                common.openSettingsView();
                setting.clickSettingOptionList("ToDo's");
                setting.setDefaultDueDate("None");
                // Clean all To.do
                common.clickViewBottomBar("ToDo");
                common.clearView();
                // Go to collection View
                common.clickViewBottomBar("Collection");
                statusLogin = true;
            }catch(Exception e){
                count = count+1;
                if(count == 2){
                   throw e;
                }
            }
        }

    }

    // TC: Swipe left to right To.do and create other linked Email
    String testCase1 = "TS010_CollectionScripts03_Collection1_addEmailLinkedToDoBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts03_Collection1_addEmailLinkedToDoBySwipe(){
        Log.startTestCase(testCase1);
        // 1. Filter by a Collection (Sample) and To.Do's, create new To.do 1
        collection.filterByType("Sample","ToDo");
        common.openNewItemView();
        collection.openAddItemView("ToDo");
        todo.addNewTodo(titleTodo1,true,true);
        collection.filterByType("Sample","ToDo");
        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        collection.filterByType("","ToDo");
        // 2. Swipe left to right To.do (To.do 1) and click Email icon
        collection.swipeLeftToRightItem("ToDo",titleTodo1,"Email");
        // 3. Input info send an Email
        email.sendEmail("","khuong001@123flo.com","","",subjectEmail," Content",
                "","Add","Sample",true);
        // 4. Open detail of To.do (To.do 1)
        common.openItem("ToDo",titleTodo1);
        // 5. Check link has Email
        flag = todo.checkTodoItemDetails(titleTodo1,"None",true,"Short","None",
                "Sample",subjectEmail,testCase1);
        // 6. Back to Collection View then filter by 'Notes')
        common.clickElementButton("Sample");
        collection.filterByType("","Email");
        // 7. Check Email belong Collection
        boolean flag1 = collection.checkItemBelongCollection("Email",subjectEmail,testCase1);
        // 8. Open detail of Email
        common.openItem("Email",subjectEmail);
        // 9. Check To.do 1 existing in detail
        boolean flag2 = common.checkItemExist(titleTodo1,"Exist",testCase1);
        // 10. Trash email in detail
        common.trashItemByDetails(true);
        // 11. Back to Collection view and filter by 'To.Do's'
        collection.filterByType("","ToDo");
        Log.endTestCase(testCase1);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe left to right To.do and create other linked Note
    String testCase2 = "TS010_CollectionScripts03_Collection2_addNoteLinkedToDoBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts03_Collection2_addNoteLinkedToDoBySwipe(){
        Log.startTestCase(testCase2);
        // 1. Swipe left to right To.do (To.do 1) and click Note icon
        collection.filterByType("","ToDo");
        collection.swipeLeftToRightItem("ToDo",titleTodo1,"Note");
        // 2. Input info new Note (Note A)
        note.addNewNote(titleNote,"",true,"Done");
        // 3. Open detail of To.do (To.do 1)
        common.openItem("ToDo",titleTodo1);
        // 4. Check link has new Note (Note A)
        flag = todo.checkTodoItemDetails(titleTodo1,"None",true,"Short","None",
                "Sample",titleNote,testCase2);
        // 5. Back to Collection View then filter by 'Notes')
        common.clickElementButton("Sample");
        collection.filterByType("General","Note");
        // 6. Check new Note existing (Note A)
        boolean flag1 = collection.checkItemBelongCollection("Note",titleNote,testCase2);
        // 7. Open detail of new Note (Note A)
        common.openItem("Note",titleNote);
        // 8. Check detail of new Note (Note A)
        boolean flag2 = note.checkNoteItemDetails(titleNote,"",true,"","General",
                titleTodo1,false,testCase2);
        // 9. Delete new Note in detail (Note A)
        common.trashItemByDetails(true);
        // 10. Back to Collection view and filter by 'To.Do's'
        collection.filterByType("Sample","ToDo");
        Log.endTestCase(testCase2);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left To.do add Due date
    String testCase3 = "TS010_CollectionScripts03_Collection3_addDueForToDoBySwipe";
    @Test(dataProvider = "setDueDate", retryAnalyzer = RetryFailedTestCases.class)
    public void TS010_CollectionScripts03_Collection3_addDueForToDoBySwipe(String testCase, String title,String status, String dueDate, String checkDueDate){
        Log.startTestCase(testCase3);
        switch (dueDate){
            case "3 days":
                dueDate = "In 3 Days";
                break;
            case "10 days":
                dueDate = "In 10 Days";
                break;
            case "30 days":
                dueDate = "In 30 Days";
                break;
        }
        collection.filterByType("","ToDo");
        // 1. Swipe left to right To.do (To.do 1) and click Due date icon
        collection.swipeLeftToRightItem("ToDo",title,status);
        // 2. Select due date (In 3 days, In 10 days, In 30 days, Today)
        common.clickStaticTextButton(dueDate);
        // Convert String checkDueDate to Int then get date value
        int value = (int)Double.parseDouble(checkDueDate.toString());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, value);
        String CheckDueDate = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        // 3. Check Due To.do (To.do 1)
        flag = todo.checkTodoItemView(title,status,CheckDueDate,false,false,true,true,testCase);
        Log.endTestCase(testCase3);
        if(flag == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] setDueDate() throws Exception{
        Object[][] setDueDate = ExcelUtils.getTableArray(Constants.COLLECTION_DATA,"SetDueDateTodo");
        return (setDueDate);
    }

    // TC: Swipe right to left To.do and move collection
    String testCase4 = "TS010_CollectionScripts03_Collection4_MoveToDoToCollectionBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts03_Collection4_MoveToDoToCollectionBySwipe(){
        Log.startTestCase(testCase4);
        collection.filterByType("","ToDo");
        // 1. Swipe left to right To.do (To.do 1) and click Collection icon
        collection.swipeRightToLeftItem("ToDo",titleTodo1,"Collection");
        // 2. Move collection to others (Play)
        common.addMoveToCollection(false,"Move","Play");
        // 3. Check To.do not existing (To.do 1)
        flag = common.checkItemExist(titleTodo1,"Not Exist",testCase4);
        // 4. Filter by Collection moved (Play)
        collection.filterByType("Play","ToDo");
        // 5. Open detail of To.do (To.do 1)
        common.openItem("ToDo",titleTodo1);
        // 6. Check collection is moved
        boolean flag1 = todo.checkTodoItemDetails(titleTodo1,today,true,"Short","None","Play","",testCase4);
        // 7. Back to Collection view
        common.clickElementButton("Play");
        Log.endTestCase(testCase4);
        if(!(flag & flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left To.do and add collection
    String testCase5 = "TS010_CollectionScripts03_Collection5_addToDoToCollectionBySwipe";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts03_Collection5_addToDoToCollectionBySwipe(){
        Log.startTestCase(testCase5);
        collection.filterByType("","ToDo");
        // 1. Swipe left to right To.do (To.do 1) and click Collection icon
        collection.swipeRightToLeftItem("ToDo",titleTodo1,"Collection");
        // 2. Add more collection Ex: General, Work
        common.addMoveToCollection(false,"Add","General,Work");
        // 3. Open detail of To.do (To.do 1)
        common.openItem("ToDo",titleTodo1);
        // 4. Check collection is added
        flag = todo.checkTodoItemDetails(titleTodo1,today,true,"Short","None",
                "Play,General,Work","",testCase5);
        // 5. Back to Collection view
        common.clickElementButton("Play");
        Log.endTestCase(testCase5);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    // TC: Swipe right to left To.do and add delete
    String testCase6 = "TS010_CollectionScripts03_Collection6_deleteToDoBySwipe";
    String deleteMessage = "This ToDo will be moved to your Trash Collection.";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS010_CollectionScripts03_Collection6_deleteToDoBySwipe(){
        Log.startTestCase(testCase6);
        collection.filterByType("","ToDo");
        // 1. Swipe left to right To.do (To.do 1) and click Trash icon
        collection.swipeRightToLeftItem("ToDo",titleTodo1,"Trash");
        // 2. Check message delete then tap on OK button
        flag = common.checkTrashMessage(deleteMessage,"OK",testCase6);
        Log.endTestCase(testCase6);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        // Empty trash collection
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
