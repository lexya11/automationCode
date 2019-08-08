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
import Support.RetryFailedTestCases;



public class TS007_TodoScripts02 extends SetupServer{
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    SignInKeywords signin;
    CommonKeywords common;
    AgendaKeywords agenda;
    TodoKeywords todo;
    CollectionKeywords collection;
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String titleTodo1 = "TodoScripts02_Todo 1";

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

    // TC: Check new To.do view show default duration as options in setting
    String testCase1 = "TS007_TodoScripts02_ToDo1_checkDefaultDurationSetting";
    @Test(dataProvider = "defaultDuration", retryAnalyzer = RetryFailedTestCases.class)
    public void TS007_TodoScripts02_ToDo1_checkDefaultDurationSetting(String testcase, String durationTimeOptions){
        Log.startTestCase(testCase1);
        // 1. Go to Setting > to.do
        common.openSettingsView();
        settings.clickSettingOptionList("ToDo's");
        // 2. Select "Default Due Date" > select Today
        settings.clickSettingOptionList("Default Duration");
        Wait(2);
        settings.setDefaultDuration(durationTimeOptions);
        // 3. Back to to.do view
        common.clickViewBottomBar("ToDo");
        // 4. Check new To.Do View as Setting
        common.openNewItemView();
        flag = todo.checkNewTodoView("","","",durationTimeOptions,"","",true, testcase);
        Log.endTestCase(testCase1);
        if(flag == false){
            Assert.fail("Test case is failed!");
        }
    }
    @DataProvider
    public Object[][] defaultDuration() throws Exception{
        Object[][] defaultDuration = ExcelUtils.getTableArray(Constants.TODO_DATA,"defaultDuration");
        return (defaultDuration);
    }

    // TC: Check Show Due To.do's in Agenda option in Setting
    String testCase2 = "TS007_TodoScripts02_ToDo2_checkShowDueToDoInAgenda";
//    @Test
//    public void TS007_TodoScripts02_ToDo2_checkShowDueToDoInAgenda(){
//        Log.startTestCase(testCase2);
//        // 1. Open To.Do View
//        common.clickViewBottomBar("ToDo");
//        todo.filterByType("Top","Due");
//        // 2. Add new due to.do with due today
//        todo.addQuickTodo(titleTodo1,"Today");
//
//        // 3. Go to Setting > to.do
//        common.openSettingsView();
//        settings.clickSettingOptionList("ToDo's");
//
//        // 4. Set ShowDueTodoAgenda > Off
//        settings.showDueTodoAgenda(false);
//        // 5. Go to Agenda, check about due to.do > Not show to.do
////        home.clickViewBottomBar("Event");
////        boolean flag1 = common.checkItemExist(titleTodo1,"Not Exist",testCase2);
//
//        // 6. Go to Setting > to.do
//        home.swipeBottomViewToBackHome();
//        home.clickTopLeftbutton("Settings");
//        settings.clickSettingOptionList("ToDo's");
//        // 7. Set ShowDueTodoAgenda > On
//        settings.showDueTodoAgenda(true);
//        // 8. Go to Agenda, check about due to.do > Show to.do
////        home.clickViewBottomBar("Event");
////        boolean flag2 = common.checkItemExist(titleTodo1,"Exist",testCase2);
//
//        // 9. Delete new to.do
//        common.clickViewBottomBar("ToDo");
//        todo.deleteTodoBySwipe(titleTodo1,true);
//        Log.endTestCase(testCase2);
////        if((flag1 & flag2) == false){
////            Assert.fail("Test case is failed!");
////        }
//    }

    // TC: Check new To.do view show default Star, Done as filter
    String testCase3 = "TS007_TodoScripts02_ToDo3_checkNewToDoDefaultStarDone";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts02_ToDo3_checkNewToDoDefaultStarDone(){
        Log.startTestCase(testCase3);
        // 1. Open To.Do View
        // 2. Filter To.Do List by Star
        todo.filterByType("Top","Star");
        // 3. Add New To.Do by clicking Plus icon
        common.openNewItemView();
        // 4. Check new To.Do View show default Star, Undone
        boolean flag1 = todo.checkNewTodoView("UnDone","Star","","","",
                "",true,testCase3);
        // 5. Filter To.Do List by Done
        todo.filterByType("Top","Done");
        // 6. Add New To.Do by clicking Plus icon
        common.openNewItemView();
        // 7. Check new To.Do View show default UnStar, Done
        boolean flag2 = todo.checkNewTodoView("Done","UnStar","","","",
                "",true,testCase3);
        // 8. Filter by Due
        todo.filterByType("Top","Due");
        Log.endTestCase(testCase3);
        if((flag1 & flag2) == false){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Check new To.do view show default Collection as local filter
    String testCase4 = "TS007_TodoScripts02_ToDo4_checkNewToDoDefaultCollection";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS007_TodoScripts02_ToDo4_checkNewToDoDefaultCollection(){
        Log.startTestCase(testCase4);
        // 1. Open To.Do View
        common.clickViewBottomBar("ToDo");
        // 2. Check local filter is All Collections
        flag = common.checkLocalFilter("All Collections",testCase4);
        // 6. Open add new To.Do View
        common.openNewItemView();
        // 7. Check new To.Do view show Collection as default Collection
        boolean flag1 = todo.checkNewTodoView("","","","","","General",true,testCase4);
        // 8. Select local filter to a Collection Home
        common.selectLocalFilter("Home");
        // 9. Open add new To.Do View
        common.openNewItemView();
        // 10. Check new To.Do view show Collection as local filter
        boolean flag2 = todo.checkNewTodoView("","","","","","Home",true,testCase4);
        // 11. Select local filter to Collection Home and Collection Sample
        common.selectLocalFilter("Home,Play");
        // 12. Open add new To.Do View
        common.openNewItemView();
        // 13. Check new To.Do view show Collection as default Collection
        boolean flag3 = todo.checkNewTodoView("","","","","","General",true,testCase4);
        // 14. Select local filter is All To.Do's
        common.selectLocalFilter("All Collections");
        Log.endTestCase(testCase4);
        if((flag1 & flag2 & flag3) == false){
            Assert.fail("Test case is failed!");
        }
    }

    // TC: Check new To.do view show default due date as options in setting
    String testCase5 = "TS007_TodoScripts02_ToDo5_checkDefaultDueDateSetting";
    @Test(dataProvider = "defaultDueDate", retryAnalyzer = RetryFailedTestCases.class)
    public void TS007_TodoScripts02_ToDo5_checkDefaultDueDateSetting(String testcase, String dueDateOptions){
        Log.startTestCase(testCase5);
        // 1. Go to Setting > to.do
        common.openSettingsView();
        settings.clickSettingOptionList("ToDo's");
        // 2. Select "Default Due Date " > select Options
        settings.setDefaultDueDate(dueDateOptions);
        // 3. Open To.Do View
        common.clickViewBottomBar("ToDo");
        // 4. Check new To.Do View as Setting
        common.openNewItemView();
        flag = todo.checkNewTodoView("","", dueDateOptions,"","","",true, testcase);
        Log.endTestCase(testCase5);
        if(flag == false){
            Assert.fail("Test case is failed!");
        }
    }
    @DataProvider
    public Object[][] defaultDueDate() throws Exception{
        Object[][] defaultDueDate = ExcelUtils.getTableArray(Constants.TODO_DATA,"defaultDueDate");
        return (defaultDueDate);
    }

    // TC: Check new To.do view show default due date alert as options in setting
    String testCase6 = "TS007_TodoScripts02_ToDo6_checkDefaultDueDateAlertSetting";
    @Test(dataProvider = "defaultDueDateAlert", retryAnalyzer = RetryFailedTestCases.class)
    public void TS007_TodoScripts02_ToDo6_checkDefaultDueDateAlertSetting(String testcase, String dueAlertOptions){
        Log.startTestCase(testCase6);
        // 1. Go to Setting > to.do
        common.openSettingsView();
        settings.clickSettingOptionList("ToDo's");
        // 2. Select "Default Due Date Alert" > select options
        settings.setDefaultDueDateAlert(dueAlertOptions);
        // 3. Back to to.do view
        common.clickViewBottomBar("ToDo");
        // 4. Check new To.Do View as Setting
        common.openNewItemView();
        flag = todo.checkNewTodoView("","","","", dueAlertOptions,"",true,testcase);
        Log.endTestCase(testCase6);
        if(flag == false){
            Assert.fail("Test case is failed!");
        }
    }
    @DataProvider
    public Object[][] defaultDueDateAlert() throws Exception{
        Object[][] defaultDueDateAlert = ExcelUtils.getTableArray(Constants.TODO_DATA,"defaultDueDateAlert");
        return (defaultDueDateAlert);
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // 1. Go to Setting > to.do
        common.openSettingsView();
        settings.clickSettingOptionList("ToDo's");
        // 2. Select "Default Due Date Alert" > select None
        settings.setDefaultDueDate("None");

        // Delete in trash
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
