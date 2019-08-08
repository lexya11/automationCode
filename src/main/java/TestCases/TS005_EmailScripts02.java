package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.RetryFailedTestCases;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TS005_EmailScripts02 extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    AgendaKeywords agenda;
    SignInKeywords signin;
    NoteKeywords note;
    TodoKeywords todo;
    EmailKeywords email;
    CommonKeywords common;
    ContactKeywords contact;
    SettingsKeywords settings;

    String today = "";
    String tomorrow = "";
    String nextWeekDay= "";
    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String flomailAddress = username + Constants.MAIL_SERVER;
    String flomailAddess2 = "khuong001@123flo.com";
    String flomailAddress1 = "khuong002@123flo.com";


    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);
        todo = new TodoKeywords(this);
        signin = new SignInKeywords(this);
        contact = new ContactKeywords(this);
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
                signin.login(username, password, true);
                // Close Start Flo popup
                common.closeStartFlo();
                common.openSettingsView();
                settings.clickSettingOptionList("Accounts");
                settings.updateFloAccount(flomailAddress,"","",true);
                // Settings Normal Event  = None
                common.clickElementButton("Settings");
                settings.clickSettingOptionList("Calendars");
                settings.setNormalEventAlert("None");
                common.clickViewBottomBar("Contact");
                common.clearView();
                common.clickViewBottomBar("Email");
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

    // TC: Reply Email By Swipe
    String testCase1 = "TS005_EmailScripts02_Email1_replyEmailBySwipe";
    String subject1 = "TS005_EmailScripts02_Email 1";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email1_replyEmailBySwipe(){
        Log.startTestCase(testCase1);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress,"","",subject1,"content","","Add","Home",true);
        // 2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress,subject1,"Exist",testCase1);
        // 3. Reply email by swipe
        email.navigateToImapFolder("All Inboxes");
        email.replyEmailBySwipe(flomailAddress, subject1,"");
        // Delete email
        email.deleteEmailBySwipe(flomailAddress,subject1,true);
        // 4. Check new reply email in "All Sent"
        boolean flag2 = email.checkEmailExist("Sent",flomailAddress,"Re: "+subject1,"Exist",testCase1);
        // 5. Delete Email
        email.deleteEmailBySwipe(flomailAddress,"Re: "+subject1,true);
        // 6. Check reply email is existed in "All Inboxes"
        flag = email.checkEmailExist("All Inboxes",flomailAddress,"Re: "+subject1,"Exist",testCase1);
        // 7. Delete Email
        email.deleteEmailBySwipe(flomailAddress,"Re: "+subject1,true);
        Log.startTestCase(testCase1);
        if(!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Reply All Email By Swipe
    String testCase2 = "TS005_EmailScripts02_Email2_replyAllEmailBySwipe";
    String subject2 = "TS005_EmailScripts02_Email 2";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email2_replyAllEmailBySwipe(){
        Log.startTestCase(testCase2);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress,flomailAddess2,"",subject2,"content","","Add","Home",true);
        // 2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress,subject2,"Exist",testCase2);
        // 3. Reply All email by swipe
        email.navigateToImapFolder("All Inboxes");
        Wait(3);
        email.replyAllEmailBySwipe(flomailAddress,subject2,"reply all email");
        // Delete email
        email.deleteEmailBySwipe(flomailAddress,subject2,true);
        // 4. Check reply All email is existed in "All Sent"
        boolean flag2 = email.checkEmailExist("Sent",flomailAddress,"Re: "+subject2,"Exist",testCase2);
        // 5. Delete Email
        email.deleteEmailBySwipe(flomailAddress,"Re: "+subject2,true);
        // 6. Check reply All email is existed in "All Inboxes"
        flag = email.checkEmailExist("All Inboxes",flomailAddress,"Re: "+subject2,"Exist",testCase2);
        // 7. Delete Email
        email.deleteEmailBySwipe(flomailAddress,"Re: "+subject2,true);
        Log.endTestCase(testCase2);
        if(!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Forward  Email By Swipe
    String testCase3 = "TS005_EmailScripts02_Email3_forwardEmailBySwipe";
    String subject3 = "TS005_EmailScripts02_Email 3";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email3_forwardEmailBySwipe(){
        Log.startTestCase(testCase3);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress,flomailAddess2,"",subject3,"content","","Add","Home",true);
        // 2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress,subject3,"Exist",testCase3);
        // 3. Forward email by swipe
        email.navigateToImapFolder("All Inboxes");
        Wait(5);
        email.forwardEmailBySwipe(flomailAddress,subject3,flomailAddress,"forward email");
        // Delete email
        email.deleteEmailBySwipe(flomailAddress,subject3,true);
        // 4. Check reply All email is existed in "All Sent"
        boolean flag2 = email.checkEmailExist("Sent",flomailAddress,"Fwd: "+subject3,"Exist",testCase3);
        // 5. Delete Email
        email.deleteEmailBySwipe(flomailAddress,"Fwd: "+subject3,true);
        // 6. Check reply All email is existed in "All Inboxes"
        flag = email.checkEmailExist("All Inboxes",flomailAddress,"Fwd: "+subject3,"Exist",testCase3);
        Log.endTestCase(testCase3);
        if(!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    // TC: Delete  Email By Swipe
    String testCase4 = "TS005_EmailScripts02_Email4_deleteEmailBySwipe";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email4_deleteEmailBySwipe(){
        Log.startTestCase(testCase4);
        // 1. Delete Emails by Swipe
        email.deleteEmailBySwipe(flomailAddress,"Fwd: "+subject3,true);
        // 2. Check Emails is not Existed
        boolean flag = email.checkEmailExist("",flomailAddress,"Fwd: "+subject3,"Not Exist",testCase4);
        Log.endTestCase(testCase4);
        if(!flag)
            Assert.fail("Test case is failed");
    }

    // TC: Add Event By Swipe
    String testCase5 = "TS005_EmailScripts02_Email5_addEventBySwipe";
    String subject4 = "TS005_EmailScripts02_Email 4";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email5_addEventBySwipe(){
        Log.startTestCase(testCase5);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress1,"","",subject4,"content","","Add","Home",true);
        // 2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent", flomailAddress1, subject4,"Exist", testCase5);
        // 3. Add new event by swipe
        email.addNewEventBySwipe(flomailAddress1,subject4);
        agenda.addSimpleEvent("Event 1", false,"Done");
        // 4. Check new event is existed in detail of Email
        Wait(4);
        email.openEmailItem(flomailAddress1,subject4);
        boolean flag2 = email.checkEmailItemDetail(subject4,"",flomailAddress1,"","",false,"","","Event 1",testCase5);
        // 5. Open Event's detail from links section
        common.openItem("Event","Event 1");
        // 6. Check Event's detail
        flag = agenda.checkEventItemDetails("Event 1", false,"","General",subject4,testCase5);
        // 7. Clean up Delete Event
        common.trashItemByDetails(true);
        // 8. Delete Email
        common.trashItemByDetails(true);
        Log.endTestCase(testCase5);
        if(!(flag&flag1&flag2))
            Assert.fail("Test case is failed");

    }

    // TC: Add To.do By Swipe --> Due Date: Today
    String testCase61 = "TS005_EmailScripts02_Email6_addTodoBySwipe1";
    String subject5 = "TS005_EmailScripts02_Email 5";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email6_addTodoBySwipe1(){
        Log.startTestCase(testCase61);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress1,"","",subject5,"content","","Add","Home",true);
        // 2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress1,subject5,"Exist",testCase61);
        // 3. Add new To.do by swipe
        email.addNewTodoBySwipe(flomailAddress1,subject5,"Today", "Play");
        // Get date of Today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        // 4. Check To.do is existed
        common.clickViewBottomBar("ToDo");
        flag = todo.checkTodoItemView(subject5, "Due",today,false,false,false,true,testCase61);
        // 5. Clean: Delete To.do by swipe
        todo.deleteTodoBySwipe(subject5,true);
        // 6. Back to Emails
        common.clickViewBottomBar("Email");
        Log.endTestCase(testCase61);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");

    }

    // TC: Add To.do By Swipe --> Due Date: Tomorrow
    String testCase62 = "TS005_EmailScripts02_Email6_addTodoBySwipe2";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email6_addTodoBySwipe2(){
        Log.startTestCase(testCase62);
        // 1. Add new To.do by swipe
        email.addNewTodoBySwipe(flomailAddress1,subject5,"Tomorrow", "Play");
        // Get date of Tomorrow
        Calendar calTomorrow = Calendar.getInstance();
        calTomorrow.add(Calendar.DATE, 1);
        tomorrow = new SimpleDateFormat("EEE, dd MMM").format(calTomorrow.getTime());
        // 2. Check To.do is existed
        common.clickViewBottomBar("ToDo");
        flag = todo.checkTodoItemView(subject5, "Due",tomorrow,false,false,false,true,testCase62);
        // 3. Clean: Delete To.do by swipe
        todo.deleteTodoBySwipe(subject5,true);
        // 4. Back to Emails
        common.clickViewBottomBar("Email");
        Log.endTestCase(testCase62);
        if(!(flag))
            Assert.fail("Test case is failed");

    }

    // TC: Add To.do By Swipe --> Due Date: Next Week
    String testCase63 = "TS005_EmailScripts02_Email6_addTodoBySwipe3";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email6_addTodoBySwipe3(){
        Log.startTestCase(testCase63);
        // 1. Add new To.do by swipe
        email.addNewTodoBySwipe(flomailAddress1,subject5,"Next Week", "Play");
        // Next Week
        Calendar calNextWeek = Calendar.getInstance();
        calNextWeek.add(Calendar.DATE, 7);
        nextWeekDay = new SimpleDateFormat("EEE, dd MMM").format(calNextWeek.getTime());
        Log.info(nextWeekDay);
        // 2. Check To.do is existed
        common.clickViewBottomBar("ToDo");
        flag = todo.checkTodoItemView(subject5, "Due",nextWeekDay,false,false,false,true,testCase63);
        // 3. Clean: Delete To.do by swipe
        todo.deleteTodoBySwipe(subject5,true);
        // 4. Back to Emails
        common.clickViewBottomBar("Email");
        Log.endTestCase(testCase63);
        if(!(flag))
            Assert.fail("Test case is failed");

    }

    // TC: Add To.do By Swipe --> Due Date: None
    String testCase64 = "TS005_EmailScripts02_Email6_addTodoBySwipe4";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email6_addTodoBySwipe4(){
        Log.startTestCase(testCase64);
        // 1. Add new To.do by swipe
        email.addNewTodoBySwipe(flomailAddress1,subject5,"None", "Play");
        // 2. Check To.do is existed
        common.clickViewBottomBar("ToDo");
        flag = todo.checkTodoItemView(subject5, "Normal","",false,false,false,true,testCase64);
        // 3. Clean: Delete To.do by swipe
        todo.deleteTodoBySwipe(subject5,true);
        // 4. Back to Emails
        common.clickViewBottomBar("Email");
        // 5. Delete Email
        email.deleteEmailBySwipe(flomailAddress1,subject5,true);
        Log.endTestCase(testCase64);
        if(!(flag))
            Assert.fail("Test case is failed");

    }

    // TC: Move Email To Other Imap Folder By Swipe
    String testCase7 = "TS005_EmailScripts02_Email7_moveEmailToOtherImapBySwipe";
    String subject6 = "TS005_EmailScripts02_Email 6";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email7_moveEmailToOtherImapBySwipe(){
        Log.startTestCase(testCase7);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddess2,"","",subject6,"content","","Add","Home",true);
        //2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddess2,subject6,"Exist",testCase4);
        //3. Move Email to Other Imap Folder By swipe
        email.moveEmailToOtherImapFolderBySwipe(flomailAddess2,subject6,"Flo", "Inbox",true);
        //4. Check Email is Existed in Imap Folder
        flag = email.checkEmailExist("Flo",flomailAddress,subject6,"Exist",testCase4);
        //5. Delete email
        email.deleteEmailBySwipe(flomailAddress,subject6,true);
        Log.endTestCase(testCase7);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Add Email To Collection By Swipe
    String testCase8 = "TS005_EmailScripts02_Email8_addEmailToCollectionBySwipe";
    String subject7 = "TS005_EmailScripts02_Email 7";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts02_Email8_addEmailToCollectionBySwipe(){
        Log.startTestCase(testCase8);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress1,"","",subject7,"content","","Add","Home",true);
        //2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress1,subject7,"Exist",testCase8);
        //3. Open Collection By swipe
        email.openCollectionBySwipe(flomailAddress1,subject7);
        //4. Add Collection To Email
        common.addMoveToCollection(false,"Add","Play");
        //5. Check Email
        email.openEmailItem(flomailAddress1,subject7);
        flag = email.checkEmailItemDetail("","","","","",false,"","Play","",testCase8);
        // 6. Delete email
        common.trashItemByDetails(true);
        Log.endTestCase(testCase8);
        if(!(flag&flag1))
            Assert.fail("Test case is failed");
    }

    // TC: Move Email To Other Collection By Swipe
    String testCase9 = "TS005_EmailScripts02_Email9_moveEmailToOtherCollectionBySwipe";
    String subject8 = "TS005_EmailScripts02_Email 8";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void     TS005_EmailScripts02_Email9_moveEmailToOtherCollectionBySwipe(){
        Log.startTestCase(testCase9);
        // 1. Open create new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress1,"","",subject8,"content","","Add","Home",true);
        //2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress1,subject8,"Exist",testCase9);
        //3. Open Collection By swipe
        email.openCollectionBySwipe(flomailAddress1,subject8);
        //4. Move Email to "Play "Collection
        common.addMoveToCollection(false,"Move","Play");
        //5. Check Email is Existed in "Play "Collection
        boolean flag2 = email.checkEmailExist("Flo Collections>Play",flomailAddress,subject8,"Exist",testCase9);
        //6. Check Detail Email
        email.openEmailItem(flomailAddress,subject8);
        flag = email.checkEmailItemDetail("","","","","",false,"","","",testCase9);
        //7. Delete email
        common.trashItemByDetails(true);
        Log.endTestCase(testCase9);
        if(!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        email.navigateToImapFolder("Sent");
        email.multiSelectEmail(flomailAddress,true,"Trash");

        common.clickViewBottomBar("Contact");
        common.clearView();

        common.clickViewBottomBar("Collection");
        collection.emptyTrashCollection();

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
