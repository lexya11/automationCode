package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.ExcelUtils;
import Support.Log;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import Support.RetryFailedTestCases;

public class TS007_TodoScripts03 extends SetupServer {
    MobileCapabilities capabilities;
    SignInKeywords signin;
    TodoKeywords todo;
    CommonKeywords common;
    CollectionKeywords collection;
    SettingsKeywords settings;
    String today = "";
    String currentMonth = "";
    String lastMonth = "";
    String yesterday = "";
    String dateOverDue = "";
    int currentDay;
    int Year;
    int Yesterday;
    int Overdue;
    int n = 10;
    boolean flag = true;

    String username = Constants.USERNAME01;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleTodo1 = "TodoScripts03_Star";
    String titleTodo2 = "TodoScripts03_Done";
    String titleTodo31 = "TodoScripts03_Due 1";
    String titleTodo32 = "TodoScripts03_Due 2";
    String titleTodo33 = "TodoScripts03_Due 3";
    String titleTodo34 = "TodoScripts03_Due 4";
    String titleTodo4 = "TodoScripts03_Normal";
    String titleTodo51 = "TodoScripts03_OverDue 1";
    String titleTodo52 = "TodoScripts03_OverDue 2";

    @BeforeClass
    public void Setup(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        signin = new SignInKeywords(this);
        todo = new TodoKeywords(this);
        common = new CommonKeywords(this);
        settings = new SettingsKeywords(this);
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
                //Open To.Do View
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

    // TC: Quick add To.Do with Star
    String testCase1 = "TS007_TodoScripts03_ToDo1_quickAddStarToDo";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo1_quickAddStarToDo(){
        Log.startTestCase(testCase1);

        // 1. Filter To.Do by Star
        todo.filterByType("Top","Star");
        // 2. Quick add new To.Do
        todo.addQuickTodo(titleTodo1,"");
        // 3. Check new To.Do created in List
        flag = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,true,false,testCase1);
        Log.endTestCase(testCase1);
        if (flag == false)
            Assert.fail("Test case is failed!");
    }

    // TC: Quick add To.Do with Done
    String testCase2 = "TS007_TodoScripts03_ToDo2_quickAddDoneToDo";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo2_quickAddDoneToDo(){
        Log.startTestCase(testCase2);
        // 1. Filter To.Do by Done
        todo.filterByType("Top","Done");
        // 2. Quick add new To.Do
        todo.addQuickTodo(titleTodo2,"");
        // 3. Check new To.Do created
        flag = todo.checkTodoItemView(titleTodo2,"Normal","",true,false,false,false,testCase2);
        Log.endTestCase(testCase2);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }

    // TC: Quick add To.Do with Due date
    String testCase3 = "TS007_TodoScripts03_ToDo3_quickAddDueToDo";
    @Test(dataProvider = "quickAddDueToDo", retryAnalyzer = RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo3_quickAddDueToDo(String testcase, String name, String status, String selectDue, String dueDate){
        Log.startTestCase(testCase3);

        // Generate dueDate from string from data provider
        int numDate = (int)Double.parseDouble(dueDate.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,numDate);
        String DueDate = new SimpleDateFormat("EEE, dd MMM").format(calendar.getTime());

        // 1. Filter To.Do by Due
        todo.filterByType("Top", status);
        // 2. Quick add new To.Do
        todo.addQuickTodo(name, selectDue);
        // 3. Check new To.Do created
        flag = todo.checkTodoItemView(name, status, DueDate,false,false,false,false,testcase);
        Log.endTestCase(testCase3);
        if(flag == false)
            Assert.fail("Test case is failed!");
    }
    @DataProvider
    public Object[][] quickAddDueToDo() throws Exception{
        Object[][] quickAddDueToDo = ExcelUtils.getTableArray(Constants.TODO_DATA,"quickAddDueToDo");
        return (quickAddDueToDo);
    }

    // TC: Filter All shows all To.Do excepts Done To.Do
    String testCase4 = "TS007_TodoScripts03_ToDo4_showAllTodoWithFilterByAll";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo4_showAllTodoWithFilterByAll(){
        Log.startTestCase(testCase4);
        // 1. Filter To.Do by All
        todo.filterByType("Top","All");
        // 2. Quick add new To.Do
        todo.addQuickTodo(titleTodo4,"");
        // 3. Check new To.Do created
        flag = todo.checkTodoItemView(titleTodo4,"Normal","",false,false,false,false,testCase4);
        // 4. Check Star To.Do in List
        boolean flag1 = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,true,false,testCase4);
        // 5. Check Done To.Do is not shown
        boolean flag2 = common.checkItemExist(titleTodo2,"Not Exist",testCase4);
        // Generate Today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        // 6. Check Due To.Do in List
        boolean flag3 = todo.checkTodoItemView(titleTodo31,"Due",today,false,false,false,false,testCase4);
        Log.endTestCase(testCase4);
        if(!(flag & flag1 & flag2 & flag3))
            Assert.fail("Test case is failed!");
    }

    // TC: Filter by Star only show Star To.Do
    String testCase5 = "TS007_TodoScripts03_ToDo5_showStarTodoWithFilterByStar";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo5_showStarTodoWithFilterByStar(){
        Log.startTestCase(testCase5);
        // 1. Filter To.Do by All
        todo.filterByType("Bottom","Star");
        // 2. Check Star To.Do is show
        flag = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,true,false,testCase5);
        // 3. Check Done To.Do is not shown
        boolean flag1 = common.checkItemExist(titleTodo2,"Not Exist",testCase5);
        // 4. Check Normal To.Do is not showns
        boolean flag2 = common.checkItemExist(titleTodo4,"Not Exist",testCase5);
        Log.endTestCase(testCase5);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed!");
    }

    // TC: Filter by Done only show Done To.Do
    String testCase6 = "TS007_TodoScripts03_ToDo6_showDoneTodoWithFilterByDone";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo6_showDoneTodoWithFilterByDone(){
        Log.startTestCase(testCase6);
        // 1. Filter To.Do by Done
        todo.filterByType("Bottom","Done");
        // 2. Check Done To.Do is show
        flag = todo.checkTodoItemView(titleTodo2,"Normal","",true,false,false,false,testCase6);
        // 3. Check Star To.Do is not shown
        boolean flag1 = common.checkItemExist(titleTodo1,"Not Exist",testCase6);
        // 4. Check Normal To.Do is not shown
        boolean flag2 = common.checkItemExist(titleTodo4,"Not Exist",testCase6);
        Log.endTestCase(testCase6);
        if(!(flag & flag1 & flag2))
            Assert.fail("Test case is failed!");
    }

    // TC: Add due yesterday To.Do and due past To.Do
    String testCase7 = "TS007_TodoScripts03_ToDo7_addOverDueToDo";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo7_addOverDueToDo(){
        Log.startTestCase(testCase7);
        common.clickViewBottomBar("Todo");
        // 1. Filter by All
        todo.filterByType("Bottom","All");
        // 2. Add New To.Do
        common.openNewItemView();
        todo.addNewTodo(titleTodo51,false,true);
        // 3. Set new To.Do has Overdue Yesterday
        todo.openTodoItem(titleTodo51);

        Calendar calendar = Calendar.getInstance();
        //Get current Date
        calendar.get(Calendar.DATE);
        currentDay = Integer.parseInt(new SimpleDateFormat("dd").format(calendar.getTime()));
        // Get current Month
        calendar.get(Calendar.MONTH);
        currentMonth = new SimpleDateFormat("MMMM").format(calendar.getTime());
        // Get current Year
        calendar.get(Calendar.YEAR);
        Year = Integer.parseInt(new SimpleDateFormat("YYYY").format(calendar.getTime()));
        //Get Last Month
        calendar.add(Calendar.MONTH, -1);
        lastMonth = new SimpleDateFormat("MMMM").format(calendar.getTime());

        // Due date = Yesterday
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        // int day (yesterday)
        Yesterday = Integer.parseInt(new SimpleDateFormat("dd").format(calendar.getTime()));
        // String format Yesterday
        yesterday = new SimpleDateFormat("EEE, dd MMM").format(calendar.getTime());

        // Due date = many days < current date
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-n);
        Overdue = Integer.parseInt(new SimpleDateFormat("dd").format(calendar.getTime()));
        dateOverDue = new SimpleDateFormat("EEE, dd MMM").format(calendar.getTime());

        if(currentDay>=2){
            todo.setDueDate("", currentMonth, Yesterday, Year,true);
        } else todo.setDueDate("", lastMonth, Yesterday, Year,true);
        // 4. Check To.Do added
        flag = todo.checkTodoItemView(titleTodo51,"Overdue",yesterday,false,false,false,false,testCase7);
        // 5. Add New To.Do
        common.openNewItemView();
        todo.addNewTodo(titleTodo52,false,false);
        // 6. Set new To.Do has Overdue < current date
        if(currentDay>n){
            todo.setDueDate("", currentMonth, Overdue, Year,true);
        }else todo.setDueDate("", lastMonth, Overdue, Year,true);
        // 7. Check To.Do added
        boolean flag1 = todo.checkTodoItemView(titleTodo52,"Overdue",dateOverDue,false,false,false,false,testCase7);
        // 8. Delete To.Do
        todo.deleteTodoBySwipe(titleTodo52,true);
        // 9. Delete To.Do
        todo.deleteTodoBySwipe(titleTodo4,true);
        Log.endTestCase(testCase7);
        if(!(flag & flag1))
            Assert.fail("Test case is failed!");
    }

    // TC: Filter due will show normal, due, star To.Do by group
    String testCase8 = "TS007_TodoScripts03_ToDo8_showDueTodoWithFilterByDue";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts03_ToDo8_showDueTodoWithFilterByDue(){
        Log.startTestCase(testCase8);
        // 1. Filter by All
        todo.filterByType("Bottom","Due");
        // 2. Check Done To.Do is not shown
        flag = common.checkItemExist(titleTodo2,"Not Exist",testCase8);
        // 3. Check Due To.Do is shown in 'Today' Group
        boolean flag1 = todo.checkGroupDueTodo("Today",titleTodo31,testCase8);
        // 4. Check Due To.Do is shown in 'Next 3 days' Group
        boolean flag2 = todo.checkGroupDueTodo("Next 3 days",titleTodo32,testCase8);
        // 5. Check Due To.Do is shown in 'Next 10 days 'Group
        boolean flag3 = todo.checkGroupDueTodo("Next 10 days",titleTodo33,testCase8);
        // 6. Check Due To.Do is shown in 'More than 10 days' Group
        boolean flag4 = todo.checkGroupDueTodo("More than 10 days",titleTodo34,testCase8);
        // 7. Check Over Due To.Do is shown in 'Overdue' Group
        boolean flag5 = todo.checkGroupDueTodo("Overdue",titleTodo51,testCase8);
        // 8. Check Star To.Do is shown in 'No Due Date' Group
        boolean flag6 = todo.checkGroupDueTodo("No due date",titleTodo1,testCase8);
        Log.endTestCase(testCase8);
        if(!(flag & flag1& flag2 & flag3 & flag4 & flag5 & flag6))
            Assert.fail("Test case is failed!");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete all to.do
        todo.filterByType("Top","Collection");
        todo.selectMultiTodo(titleTodo1,true,"Trash");
        todo.filterByType("Top","Due");
        // Delete in trash
        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();
        // close app
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
