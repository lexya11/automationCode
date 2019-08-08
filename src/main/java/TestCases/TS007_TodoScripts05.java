package TestCases;

    import Environments.*;
    import Keywords.*;
    import Support.*;
    import org.testng.Assert;
    import org.testng.ITestContext;
    import org.testng.annotations.*;

public class TS007_TodoScripts05 extends SetupServer{
    MobileCapabilities capabilities;
    SignInKeywords signin;
    TodoKeywords todo;
    CommonKeywords common;

    SettingsKeywords settings;
    CollectionKeywords collection;
    boolean flag = true;

    String username = Constants.USERNAME07;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleTodo1 = "TodoScripts05_Todo 1";
    String titleTodo2 = "TodoScripts05_Todo 2";
    String titleTodo3 = "TodoScripts05_Todo 3";
    String titleTodo4 = "TodoScripts05_Todo 4";
    String titleTodo5 = "TodoScripts05_Todo 5";

    @BeforeClass
    public void Setup(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        signin = new SignInKeywords(this);
        todo = new TodoKeywords(this);
        common = new CommonKeywords(this);

        collection = new CollectionKeywords(this);
        settings = new SettingsKeywords(this);
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

                // Open Settings > To.Do
                common.openSettingsView();
                settings.clickSettingOptionList("ToDo's");
                // Set Default Due Date: None
                settings.setDefaultDueDate("None");
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

    /**
     * TC: Set UnStar To.do from To.do List View
     */
    String testCase1 = "TS007_TodoScripts05_ToDo1_setUnStarTodoFromTodoListView";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts05_ToDo1_setUnStarTodoFromTodoListView(){
        Log.startTestCase(testCase1);

        // 1. Add new To.Do (Test 1)
        common.openNewItemView();
        todo.addFullTodo(titleTodo1,true,false,"","","","",true);
        flag = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,true,false,testCase1);
        // 2. Un Star To.do From To.o View
        common.clickStarItem(titleTodo1,"UnStar");
        // 3. Check To.do
        boolean flag1 = todo.checkTodoItemView(titleTodo1,"Normal","",false,false,false,false,testCase1);
        Log.endTestCase(testCase1);
        if((flag & flag1) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * TC: Delete To.do from To.do View Detail
     */
    String testCase2 = "TS007_TodoScripts05_Todo2_deleteToDoFromTodoViewDetail";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts05_Todo2_deleteToDoFromTodoViewDetail(){
        Log.startTestCase(testCase2);
        // 1. Add new To.do
        common.openNewItemView();
        todo.addFullTodo(titleTodo2,true,false,"","","","",true);
        // 2. Check To.do created in List
        flag = todo.checkTodoItemView(titleTodo2,"Normal","",false,false,true,false,testCase2);
        // 3. Open To.Do View Detail
        todo.openTodoItem(titleTodo2);
        common.trashItemByDetails(true);
        // 4. Check To.do is not existed
        todo.filterByType("Top", "All");
        boolean flag1 = common.checkItemExist(titleTodo2,"Not Exist",testCase2);
        Log.endTestCase(testCase2);
        if((flag & flag1) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * TC: Search To.do by title
     */
    String testCase3 = "TS007_TodoScripts05_Todo3_searchTodoByTitleFromTodoList";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts05_Todo3_searchTodoByTitleFromTodoList(){
        Log.startTestCase(testCase3);
        // 1. Add new To.do
        common.openNewItemView();
        todo.addFullTodo(titleTodo3,true,false,"","","","",true);
        // 2. Check To.do created in List
        flag = todo.checkTodoItemView(titleTodo3,"Normal","",false,false,true,false,testCase3);
        // 3. search To.Do
        todo.filterByType("Top", "All");
        common.searchItem("ToDo",titleTodo3);
        boolean flag1 = common.checkItemExist(titleTodo1,"Not Exist",testCase3);
        boolean flag2 = common.checkItemExist(titleTodo3,"Exist",testCase3);
        common.closeSearchItemScreen();
        Log.endTestCase(testCase3);
        if((flag & flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Search To.do By content From To.do List
     */
    String testCase4 = "TS007_TodoScripts05_Todo4_searchTodoByContentFromTodoList";
    String keyword1= "home stay";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts05_Todo4_searchTodoByContentFromTodoList(){
        Support.Log.startTestCase(testCase4);
        // 1. Add new To.do
        common.openNewItemView();
        todo.addFullTodo(titleTodo4,true,false,"","",keyword1,"",true);
        // 2. Check To.do created in List
        flag = todo.checkTodoItemView(titleTodo4,"Normal","",false,false,true,false,testCase4);
        // 3. search To.Do
        todo.filterByType("Top", "All");
        Wait(4);
        common.searchItem("ToDo",keyword1);
        boolean flag1 = common.checkItemExist(titleTodo1,"Not Exist",testCase4);
        boolean flag2 = common.checkItemExist(titleTodo3,"Not Exist",testCase4);
        boolean flag3 = common.checkItemExist(titleTodo4,"Exist",testCase4);
        common.closeSearchItemScreen();
        Log.endTestCase(testCase4);
        if((flag & flag1 & flag2 & flag3) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Search To.do From To.do List With no related Keyword. Verify result screen"
     */
    String  testCase5 = "TS007_TodoScripts05_Todo5_searchTodoFromTodoListWithWrongKeyword";
    String  status = "Sorry, No results were found";
    String  keyword2 = "automation";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts05_Todo5_searchTodoFromTodoListWithWrongKeyword(){
        Support.Log.startTestCase(testCase5);
        // 1. Add new To.do
        common.openNewItemView();
        todo.addFullTodo(titleTodo5,true,false,"","","home stay","",true);
        // 2. Check To.do created in List
        flag = todo.checkTodoItemView(titleTodo5,"Normal","",false,false,true,false,testCase5);
        // 3. search To.Do
        todo.filterByType("Top", "All");
        common.searchItem("ToDo",keyword2);
        boolean flag1 = common.checkItemExist(titleTodo1,"Not Exist",testCase5);
        boolean flag2 = common.checkItemExist(titleTodo3,"Not Exist",testCase5);
        boolean flag3 = common.checkItemExist(titleTodo4,"Not Exist",testCase5);
        boolean flag4 = common.checkItemExist(titleTodo5,"Not Exist",testCase5);
        boolean flag5 = common.checkItemExist(status,"Exist",testCase5);
        common.closeSearchItemScreen();
        Log.endTestCase(testCase5);
        if((flag & flag1 & flag2 & flag3 & flag4 & flag5) == false)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete all To.Do
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