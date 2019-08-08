package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import java.lang.Double;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TS007_TodoScripts01 extends SetupServer {
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;
    AgendaKeywords agenda;
    TodoKeywords todo;
    CollectionKeywords collection;
    boolean flag = false;

    String username = Constants.USERNAME04;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleTodo1 = "TodoScripts01_Todo 1";
    String titleTodo2 = "TodoScripts01_Todo 2";
    String titleTodo3 = "TodoScripts01_Todo 3";
    String titleTodo4 = "TodoScripts01_Todo 4";

    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        settings = new SettingsKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        agenda = new AgendaKeywords(this);
        todo = new TodoKeywords(this);
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
                common.ignoreConnectAcountPopup();
                // Open Settings > To.Do's > Default Due Date > None
                common.openSettingsView();
                settings.clickSettingOptionList("ToDo's");
                // close Send Notification popup
                common.closeSendNotification(true);
                settings.setDefaultDueDate("None");

                //  Open To.Do view
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

    // TC: Verify To.Do view
    String testCase1 = "TS007_TodoScripts01_ToDo1_verifyToDoView";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts01_ToDo1_verifyToDoView(){
        Log.startTestCase(testCase1);
        // 1. Check Plus button in right
        boolean flag1 = common.checkStaticButton("settings icon","Right",testCase1);
        // 2. Check view header
        boolean flag2 = common.checkLocalFilter("All Collections",testCase1);
        flag = todo.checkFilterByType("Due",testCase1);
        // 3. Filter To.Do by All
        todo.filterByType("Bottom","All");
        Log.endTestCase(testCase1);
        if(!(flag & flag1 & flag2)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add quick To.Do, done, undone To.Do
    String testCase2 = "TS007_TodoScripts01_ToDo2_addQuickToDo";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts01_ToDo2_addQuickToDo(){
        String message1 = "This will mark your ToDo as 'Done'. Hint: You can undo this by selecting the ‘Done’ filter then unchecking the ‘Done’ box.";
        String message2 = "This will mark your ToDo as 'Undone'.";

        Log.startTestCase(testCase2);
        //  1. Quick add New To.Do
        todo.addQuickTodo(titleTodo2,"None");
        //  2. Mark Done To.Do
        todo.markTodo(titleTodo2,"done",false);
        //  3. Check message popup when mark Done
        flag = todo.checkMessage(message1,"OK",testCase2);

        //  4. Filter by Done
        todo.filterByType("Top","Done");
        //  5. Check about To.Do
        boolean flag1 = todo.checkTodoItemView(titleTodo2,"Normal","None",true,false,false,false,testCase2);
        //  6. Mark Undone To.Do
        todo.markTodo(titleTodo2,"undone",false);
        //  7. Check message popup when mark Undone
        boolean flag2 = todo.checkMessage(message2,"OK",testCase2);
        //  8. Filter by All
        todo.filterByType("Top","All");
        //  9. Check To.Do item
        boolean flag3 = todo.checkTodoItemView(titleTodo2,"Normal","None",false,false,false,false,testCase2);
        Log.endTestCase(testCase2);
        if(!(flag & flag1 & flag2 & flag3)){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Add To.Do from plus button
    String testCase3 = "TS007_TodoScripts01_ToDo3_addToDoFromPlusButton";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts01_ToDo3_addToDoFromPlusButton(){
        Log.startTestCase(testCase3);
        // 1. Add New To.Do from plus icon
        common.openNewItemView();
        todo.addNewTodo( titleTodo3,true,true);
        // 2. Check about To.Do
        flag = todo.checkTodoItemView(titleTodo3,"Normal","None",false,false,true,false,testCase3);
        Log.endTestCase(testCase3);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    // TC: Set due date by swiping
    String testCase4 = "TS007_TodoScripts01_ToDo4_setDueDateBySwiping";
    @Test(dataProvider = "setDueDate", retryAnalyzer = RetryFailedTestCases.class)
    public void TS007_TodoScripts01_ToDo4_setDueDateBySwiping(String TestCase, String dueDate, String checkDueDate){
        Log.startTestCase(testCase4);

        //  1. Set Due date a To.Do by swipe action
        todo.setDueDateBySwipe(titleTodo3, dueDate);

        // Convert String checkDueDate to Int then get date value
        int value = (int)Double.parseDouble(checkDueDate.toString());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, value);
        String DueDate = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        //  2. Check about To.Do
        flag = todo.checkTodoItemView(titleTodo3,"Due", DueDate,false,false,true, false,TestCase);
        Log.endTestCase(testCase4);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }
    @DataProvider
    public Object[][] setDueDate() throws Exception{
        Object[][] setDueDate = ExcelUtils.getTableArray(Constants.TODO_DATA,"SetDueDate");
        return (setDueDate);
    }

    String testCase5 = "TS007_TodoScripts01_ToDo5_deleteTodoBySwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts01_ToDo5_deleteTodoBySwiping(){
        String deleteMessage = "This ToDo will be moved to your Trash Collection.";

        Log.startTestCase(testCase5);
        // Check Message delete To.Do
        todo.deleteTodoBySwipe(titleTodo2,false);
        flag = common.checkTrashMessage(deleteMessage,"OK", testCase5);
        Log.endTestCase(testCase5);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

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

