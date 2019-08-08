package TestCases;

import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;


public class TS005_EmailScripts05 extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    HomeKeywords home;
    AgendaKeywords agenda;
    SignInKeywords signin;
    NoteKeywords note;
    TodoKeywords todo;
    EmailKeywords email;
    CommonKeywords common;
    ContactKeywords contact;
    SettingsKeywords settings;

    boolean flag = true;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    String flomailAddress = username + Constants.MAIL_SERVER;
    String flomailAddress1 = "khuong001@123flo.com";
    String flomailAddress2 = "khuong002@123flo.com";
    String flomailAddress3 = "khuong003@123flo.com";
    String flomailAddress4 = "khuong004@123flo.com";
    String flomailAddress5 = "khuong005@123flo.com";
    String eventTime;

    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        settings = new SettingsKeywords(this);
        home = new HomeKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);
        todo = new TodoKeywords(this);
        signin = new SignInKeywords(this);
        contact = new ContactKeywords(this);
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
                common.clickViewBottomBar("Contact");
                common.clearView();
                // 1. Open Email view
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

    // TC: Set Star For Email From Email List
    String testCase1 = "TS005_EmailScripts05_Email01_setStarForEmailFromEmailList";
    String subject1 = "TS005_EmailScripts05_Email 1";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email01_setStarForEmailFromEmailList(){
        Log.startTestCase(testCase1);
        //1. Setting > Customization > Set Show Star In List View
        common.openSettingsView();
        settings.clickSettingOptionList("Customization");
        settings.setShowStarInListView(true);

        //2. Create new email
        common.clickViewBottomBar("Email");
        common.openNewItemView();
        email.sendEmail("",flomailAddress1,"","",subject1,"content","","Add","Home",true);
        //3. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress1,subject1,"Exist",testCase1);

        //4. Mark Star email
        email.clickStarInListView(flomailAddress1,subject1);
        //5. Check email in detail screen
        email.openEmailItem(flomailAddress1,subject1);
        flag = email.checkEmailItemDetail(subject1,"", flomailAddress1,"","",true,"","","",testCase1);
        //6. Back to Email List View
        common.clickElementButton("Emails");
        Log.endTestCase(testCase1);
        if(!(flag&flag1))
            Assert.fail("Test case is failed.");
    }

    // TC: Set UnStar For Email From Email List
    String testCase2 = "TS005_EmailScripts05_Email02_setUnStarForEmailFromEmailList";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email02_setUnStarForEmailFromEmailList(){
        Log.startTestCase(testCase2);
        //1. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress1,subject1,"Exist",testCase2);
        //2. Mark unstar email
        email.clickStarInListView(flomailAddress1,subject1);
        //3. Check email in view list screen
        email.openEmailItem(flomailAddress1,subject1);
        flag = email.checkEmailItemDetail(subject1,"", flomailAddress1,"","",false,"","","",testCase2);
        //4. Back to Email List View
        common.clickElementButton("Emails");
        Log.endTestCase(testCase2);
        if(!(flag&flag1))
            Assert.fail("Test case is failed.");
    }

    // TC: Move Email to Default Collection after creating To.Do or Event
    String testCase3 = "TS005_EmailScripts05_Email03_moveEmailToDefaultCollectionAfterCreatingTodoOrEvent";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email03_moveEmailToDefaultCollectionAfterCreatingTodoOrEvent(){
        Log.startTestCase(testCase3);
        // Setting > Email > Set moveEmailToDefaultCollection true
        common.openSettingsView();
        settings.clickSettingOptionList("Email");
        common.closeSendNotification(true);
        settings.setMoveEmail(true);
        common.clickElementButton("Settings");

        // Setting > Email > Collection > Set Default Collection " General"
        settings.clickSettingOptionList("Collections");
        settings.setDefaultCollection("General");

        // 1. Go to Email > All Sent
        common.clickViewBottomBar("Email");
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress1, subject1,"Exist",testCase3);

        // 2. Add new event by swipe
        email.addNewEventBySwipe(flomailAddress1,subject1);
        eventTime = agenda.addSimpleEvent(subject1,false,"Done");

        // 3. Check Email is moved to default collection (General)
        flag = email.checkEmailExist("Flo Collections>General",flomailAddress,subject1,"Exist",testCase3);

        // 4. Open Event's detail from links section
        email.openEmailItem(flomailAddress,subject1);
        boolean flag2 = email.checkEmailItemDetail(subject1,flomailAddress,"","","",false,"","",subject1, testCase3);
        common.openItem("Event",subject1);

        // 5. Check Event's detail
        flag = agenda.checkEventItemDetails(subject1, false,"","General",subject1,testCase3);

        // 6. Clean up Delete Event
        common.trashItemByDetails(true);
        // 7. Delete Email
        common.trashItemByDetails(true);
        Log.endTestCase(testCase3);
        if(!(flag&flag1&flag2))
            Assert.fail("Test case is failed.");
    }

    // TC: Search Email by All
    String testCase4 = "TS005_EmailScripts05_Email04_searchEmailByAll";
    String subject2 = "TS005_EmailScripts05 Email 2";
    String subject3 = "TS005_EmailScripts05 Email 3";
    String subject4 = "TS005_EmailScripts05 Automation 4";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email04_searchEmailByAll(){
        Log.startTestCase(testCase4);
        //1. Create some new emails
        common.openNewItemView();
        email.sendEmail("",flomailAddress2,"","",subject2,"content 2","","Add","Home",true);
        common.openNewItemView();
        email.sendEmail("",flomailAddress3,"","",subject3,"content 3","","Add","Home",true);
        common.openNewItemView();
        email.sendEmail("",flomailAddress4,"","",subject4,"qa 4","","Add","Home",true);
        //2. Check emails are existed
        boolean flag1 = email.checkEmailExist("Sent",flomailAddress2,subject2,"Exist",testCase4);
        boolean flag2 = email.checkEmailExist("",flomailAddress3,subject3,"Exist",testCase4);
        boolean flag3 = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase4);
        //3. Search by "All"
        email.searchEmailItem("All","Email");
        //4.  Check emails are existed
        boolean flag4 = email.checkEmailExist("",flomailAddress2,subject2,"Exist",testCase4);
        boolean flag5 = email.checkEmailExist("",flomailAddress3,subject3,"Exist",testCase4);
        flag = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase4);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase4);
        if(!(flag&flag1&flag2&flag3&flag4&flag5))
            Assert.fail("Test case is failed.");

    }

    // TC: Search Email by subject
    String testCase5 = "TS005_EmailScripts05_Email05_searchEmailBySubject";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email05_searchEmailBySubject(){
        Log.startTestCase(testCase5);
        //1. Go to All Sent
        email.navigateToImapFolder("Sent");
        //2. Search by Subject with keyword "Email"
        email.searchEmailItem("Subject","Email");
        //3. Check item exist
        boolean flag1 = email.checkEmailExist("",flomailAddress2,subject2,"Exist",testCase5);
        boolean flag2 = email.checkEmailExist("",flomailAddress3,subject3,"Exist",testCase5);
        boolean flag3 = email.checkEmailExist("",flomailAddress4,subject4,"Not Exist",testCase5);
        common.clickElementButton("Cancel");
        //4. Search by Subject with keyword "automation"
        email.searchEmailItem("Subject","Automation");
        //5. Check item exist
        boolean flag4 = email.checkEmailExist("",flomailAddress2,subject2,"Not Exist",testCase5);
        boolean flag5 = email.checkEmailExist("",flomailAddress3,subject3,"Not Exist",testCase5);
        flag = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase5);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase5);
        if(!(flag&flag1&flag2&flag3&flag4&flag5))
            Assert.fail("Test case is failed.");

    }

    // TC: Search Email by Body
    String testCase6 = "TS005_EmailScripts05_Email06_searchEmailByBody";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email06_searchEmailByBody(){
        Log.startTestCase(testCase6);
        //1. Go to All Sent
        email.navigateToImapFolder("Sent");
        //2. Search by Body with keyword "content"
        email.searchEmailItem("Body","content");
        //3. Check item existed
        boolean flag1 = email.checkEmailExist("",flomailAddress2,subject2,"Exist",testCase6);
        boolean flag2 = email.checkEmailExist("",flomailAddress3,subject3,"Exist",testCase6);
        boolean flag3 = email.checkEmailExist("",flomailAddress4,subject4,"Not Exist",testCase6);
        common.clickElementButton("Cancel");
        //4. Search by Body with keyword "qa"
        email.searchEmailItem("Body","qa");
        //5. Check item existed
        boolean flag4 = email.checkEmailExist("",flomailAddress2,subject2,"Not Exist",testCase6);
        boolean flag5 = email.checkEmailExist("",flomailAddress3,subject3,"Not Exist",testCase6);
        flag = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase6);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase6);
        if(!(flag&flag1&flag2&flag3&flag4&flag5))
            Assert.fail("Test case is failed.");

    }

    // TC: Search Email by To
    String testCase7 = "TS005_EmailScripts05_Email07_searchEmailByTo";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email07_searchEmailByTo(){
        Log.startTestCase(testCase7);
        //1. Go to All Sent
        email.navigateToImapFolder("Sent");
        //2. Search by To with keyword "khuong002"
        email.searchEmailItem("To","khuong002");
        //3. Check item existed
        boolean flag1 = email.checkEmailExist("",flomailAddress2,subject2,"Exist",testCase7);
        boolean flag2 = email.checkEmailExist("",flomailAddress3,subject3,"Not Exist",testCase7);
        boolean flag3 = email.checkEmailExist("",flomailAddress4,subject4,"Not Exist",testCase7);
        common.clickElementButton("Cancel");
        //4. Search by To with keyword "khuong003"
        email.searchEmailItem("To","khuong003");
        //5. Check item existed
        boolean flag4 = email.checkEmailExist("",flomailAddress2,subject2,"Not Exist",testCase7);
        boolean flag5 = email.checkEmailExist("",flomailAddress3,subject3,"Exist",testCase7);
        boolean flag6 = email.checkEmailExist("",flomailAddress4,subject4,"Not Exist",testCase7);
        common.clickElementButton("Cancel");
        //6. Search by To with keyword "khuong004"
        email.searchEmailItem("To","khuong004");
        //7. Check item existed
        boolean flag7 = email.checkEmailExist("",flomailAddress2,subject2,"Not Exist",testCase7);
        boolean flag8 = email.checkEmailExist("",flomailAddress3,subject3,"Not Exist",testCase7);
        flag = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase7);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase7);
        if(!(flag&flag1&flag2&flag3&flag4&flag5&flag6&flag7&flag8))
            Assert.fail("Test case is failed.");

    }

    // TC: Search Email by From
    String testCase8 = "TS005_EmailScripts05_Email08_searchEmailByFrom";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email08_searchEmailByFrom(){
        Log.startTestCase(testCase8);
        //1. Go to All Sent
        email.navigateToImapFolder("Sent");
        //2. Search by From with keyword username
        email.searchEmailItem("From",username);
        //3. Check item
        boolean flag1 = email.checkEmailExist("",flomailAddress2,subject2,"Exist",testCase8);
        boolean flag2 = email.checkEmailExist("",flomailAddress3,subject3,"Exist",testCase8);
        boolean flag3 = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase8);
        common.clickElementButton("Cancel");
        //2. Search by From with keyword "content"
        email.searchEmailItem("From","content");
        //4. Check item
        boolean flag4 = email.checkEmailExist("",flomailAddress2,subject2,"Not Exist",testCase8);
        boolean flag5 = email.checkEmailExist("",flomailAddress3,subject3,"Not Exist",testCase8);
        flag = common.checkItemExist("Sorry, No results were found","Exist",testCase8);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase8);
        if(!(flag&flag1&flag2&flag3&flag4&flag5))
            Assert.fail("Test case is failed.");
    }

    // TC: Search Email by Subject , Body
    String testCase9 = "TS005_EmailScripts05_Email09_searchEmailBySubjectBody";
    String subject5 = "TS005_EmailScripts05 Content 5";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email09_searchEmailBySubjectBody(){
        Log.startTestCase(testCase9);
        //1. Compose new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress5,"","",subject5,"Automation 5","","Add","Home",true);
        //2. Go to All Sent
        email.navigateToImapFolder("Sent");
        //3. Search by  "Subject,Body" , keyword "content"
        email.searchEmailItem("Subject,Body","content");
        //4. Check item in view
        boolean flag1 = email.checkEmailExist("",flomailAddress2,subject2,"Exist",testCase9);
        boolean flag2 = email.checkEmailExist("",flomailAddress3,subject3,"Exist",testCase9);
        boolean flag3 = email.checkEmailExist("",flomailAddress4,subject4,"Not Exist",testCase9);
        boolean flag4 = email.checkEmailExist("",flomailAddress5,subject5,"Exist",testCase9);
        common.clickElementButton("Cancel");
        //5. Search by  "Subject,Body" , keyword "automation"
        email.searchEmailItem("Subject,Body","automation");
        //6. Check item in view
        boolean flag5 = email.checkEmailExist("",flomailAddress2,subject2,"Not Exist",testCase9);
        boolean flag6 = email.checkEmailExist("",flomailAddress3,subject3,"Not Exist",testCase9);
        boolean flag7 = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase9);
        flag = email.checkEmailExist("",flomailAddress5,subject5,"Exist",testCase9);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase9);
        if(!(flag&flag1&flag2&flag3&flag4&flag5&flag6&flag7))
            Assert.fail("Test case is failed.");
    }

    // TC: Search Email by Subject , Body, To
    String testCase10 = "TS005_EmailScripts05_Email10_searchEmailBySubjectBodyTo";
    String subject6 = "TS005_EmailScripts05 Automation 6";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email10_searchEmailBySubjectBodyTo(){
        Log.startTestCase(testCase10);
        //1. Compose new email
        common.openNewItemView();
        email.sendEmail("",flomailAddress,"","",subject6,"ABC 6","","Add","Home",true);
        //2. goto All Sent
        email.navigateToImapFolder("Sent");
        //3. Search by  "Subject,Body,To" , keyword "content"
        email.searchEmailItem("Subject,Body,To","content");
        //4. Check item in view
        boolean flag1 = email.checkEmailExist("",flomailAddress2,subject2,"Exist",testCase10);
        boolean flag2 = email.checkEmailExist("",flomailAddress4,subject4,"Not Exist",testCase10);
        boolean flag3 = email.checkEmailExist("",flomailAddress5,subject5,"Exist",testCase10);
        boolean flag4 = email.checkEmailExist("",flomailAddress,subject6,"Not Exist",testCase10);
        common.clickElementButton("Cancel");
        //5. Search by  "Subject,Body,To" , keyword "automation"
        email.searchEmailItem("Subject,Body,To","automation");
        //6. Check item in view
        boolean flag5 = email.checkEmailExist("",flomailAddress2,subject2,"Not Exist",testCase10);
        boolean flag6 = email.checkEmailExist("",flomailAddress4,subject4,"Exist",testCase10);
        boolean flag7 = email.checkEmailExist("",flomailAddress5,subject5,"Exist",testCase10);
        flag = email.checkEmailExist("",flomailAddress,subject6,"Exist",testCase10);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase10);
        if(!(flag&flag1&flag2&flag3&flag4&flag5&flag6&flag7))
            Assert.fail("Test case is failed.");
    }

    // TC: Search Email by All with keyword not related
    String testCase11 = "TS005_EmailScripts05_Email11_searchEmailByAllWithKeywordNotRelated";
    String message = "Sorry, No results were found";
    @Test(retryAnalyzer = RetryFailedTestCases.class)
    public void TS005_EmailScripts05_Email11_searchEmailByAllWithKeywordNotRelated(){
        Log.startTestCase(testCase11);
        //1. Goto All Sent
        email.navigateToImapFolder("Sent");
        //2. Search by "All" with keyword "Relate"
        email.searchEmailItem("All","Relate");
        //3. Check item in view
        boolean flag1 = email.checkEmailExist("",flomailAddress2,subject2,"NotExist",testCase10);
        boolean flag2 = email.checkEmailExist("",flomailAddress4,subject4,"Not Exist",testCase10);
        boolean flag3 = email.checkEmailExist("",flomailAddress,subject6,"Not Exist",testCase10);
        flag = common.checkItemExist(message,"Exist",testCase8);
        common.clickElementButton("Cancel");
        Log.endTestCase(testCase11);
        if(!(flag&flag1&flag2&flag3))
            Assert.fail("Test case is failed.");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());

        email.navigateToImapFolder("Sent");
        email.multiSelectEmail(flomailAddress,true,"Trash");

        email.navigateToImapFolder("All Inboxes");
        email.deleteEmailBySwipe(flomailAddress,subject6,true);

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