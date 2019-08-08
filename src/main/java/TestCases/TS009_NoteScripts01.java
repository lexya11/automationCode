package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TS009_NoteScripts01 extends SetupServer{
    MobileCapabilities capabilities;
    SignInKeywords signin;
    CommonKeywords common;
    NoteKeywords note;
    CollectionKeywords collection;

    String username = Constants.USERNAME06;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    boolean flag = true;
    String today;

    String noteTitle1 = "NoteScripts01_Note01";
    String noteTitle2 = "NoteScripts01_Note02";
    String noteTitle3 = "NoteScripts01_Note03";
    String noteTitle4 = "NoteScripts01_Note04";

    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        note = new NoteKeywords(this);
        collection = new CollectionKeywords(this);
        // Generate Today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        capabilities = new MobileCapabilities(device,false);

        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);
                //Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username, password, true);

                // Close start Flo popup
                common.closeStartFlo();
                //Note view
                common.clickViewBottomBar("Note");
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
     * Verify note view
     */
    String testCase1 ="TS009_NoteScripts01_Note01_VerifyNoteView";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note01_VerifyNoteView(){
        Log.startTestCase(testCase1);
        // 1. Check Settings icon in left
        boolean flag2 = common.checkStaticButton("settings icon","Right", testCase1);
        // 2. Check view header
        boolean flag3 = common.checkLocalFilter("All Collections", testCase1);
        Log.endTestCase(testCase1);
        if(( flag2 & flag3) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Check cancel delete notification by swiping
     */
    String testCase2 ="TS009_NoteScripts01_Note02_checkCancelNotificationSwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note02_checkCancelNotificationSwiping(){
        Log.startTestCase(testCase2);
        // TC variables
        String deleteMessage = "This Note will be moved to your Trash Collection.";
        // 1. In Note view, add new Note
        note.addQuickNote(noteTitle1);
        // 2. Swipe right to left on note
        // 3. Select delete icon
        note.deleteNoteBySwipe(today, noteTitle1,"None");
        // 4. Check delete message and click OK
        boolean flag1 = common.checkTrashMessage(deleteMessage,"Cancel", testCase2);
        // 5. Check note is existed
        boolean flag2 = common.checkItemExist(noteTitle1,"Exist",testCase2);
        Log.endTestCase(testCase2);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Check delete notification and delete note by swiping
     */
    String testCase3 ="TS009_NoteScripts01_Note03_checkNotificationAndDeleteSwiping";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note03_checkNotificationAndDeleteSwiping(){
        Log.startTestCase(testCase3);
        // TC variables
        String deleteMessage = "This Note will be moved to your Trash Collection.";

        // 2. Swipe right to left on note
        // 3. Select delete icon
        // Generate Today
        note.deleteNoteBySwipe(today, noteTitle1,"None");
        // 4. Check delete message and click OK
        boolean flag1 = common.checkTrashMessage(deleteMessage,"OK", testCase3);
        // 5. Check note is not existed
        boolean flag2 = common.checkItemExist(noteTitle1,"Not Exist",testCase3);
        Log.endTestCase(testCase3);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }


    /**
     * Add note from plus button
     */
    String testCase4 ="TS009_NoteScripts01_Note04_AddNoteFromPlusButton";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note04_AddNoteFromPlusButton(){
        Log.startTestCase(testCase4);
        // 1. Click plus icon
        common.openNewItemView();
        // 2. Add new note
        String noteTime = note.addNewNote( noteTitle2,"Content",false, "Done");

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Log.info("String today from system - "+today);

        // 3. Check note in Note view
        flag = note.checkNoteItemView(today, noteTitle2, noteTime,false,false, testCase4);
        Log.endTestCase(testCase4);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Check cancel delete notification in details view
     */
    String testCase5 ="TS009_NoteScripts01_Note05_checkCancelNotificationDetails";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note05_checkCancelNotificationDetails(){
        Log.startTestCase(testCase5);
        // TC variables
        String deleteMessage = "This Note will be moved to your Trash Collection.";

        // 1. In Note view
        // 2. Open a note
        note.openNoteItem(today, noteTitle2);
        // 3. Select delete icon
        common.trashItemByDetails(false);
        // 4. Check delete message and click Cancel
        boolean flag1 = common.checkTrashMessage(deleteMessage,"Cancel",testCase5);
        // 5. Check Notes is existed (Notes button is existed)
        boolean flag2 = common.checkButtonExist("Notes","Exist",testCase5);
        Log.endTestCase(testCase5);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Check delete notification and delete in detail view
     */
    String testCase6 ="TS009_NoteScripts01_Note06_checkNotificationAndDeleteDetails";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note06_checkNotificationAndDeleteDetails(){
        Log.startTestCase(testCase6);
        // TC variables
        String deleteMessage = "This Note will be moved to your Trash Collection.";

        // 1. In Note view
        // 2. Open a note
        // 3. Select delete icon
        common.trashItemByDetails(false);
        // 4. Check delete message and click OK
        boolean flag1 = common.checkTrashMessage(deleteMessage,"OK", testCase6);
        // 5. Check note is not existed
        boolean flag2 = common.checkItemExist( noteTitle2,"Not Exist", testCase6);
        Log.endTestCase(testCase6);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

//    /**
//     * Add note from bottom bar
//     */
//    String testCase8 ="TS009_NoteScripts01_Note08_AddNoteFromBottomBar";
//    @Test
//    public void TS009_NoteScripts01_Note08_AddNoteFromBottomBar(){
//        Log.startTestCase(testCase8);
//
//        // 1. Click note icon on bottom bar
//        // 2. Add new note
//        common.openNewItemView();
//        String noteTime = note.addNewNote( noteTitle3, "Content", false, "Done");
//
//        // Generate String today
//        Date currentDate = new Date();
//        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
//        Log.info("String today from system - "+today);
//
//        // 3. Check note in Note view
//        flag = note.checkNoteItemView(today, noteTitle3, noteTime, false, false, testCase8);
//        // 4. Delete note
//        note.deleteNoteBySwipe(today, noteTitle3,"OK");
//        Log.endTestCase(testCase8);
//        if(flag == false)
//            Assert.fail("Test case is failed");
//    }

    /**
     * Add quick note
     */
    String testCase7 ="TS009_NoteScripts01_Note07_AddQuickNote";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note07_AddQuickNote(){
        Log.startTestCase(testCase7);

        // 1. In note view
        // 2. Add new note by quick section => Normal
        note.addQuickNote(noteTitle3);

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Log.info("String today from system - "+today);

        // 3. Check new note is added with UnStar, UnLink
        flag = note.checkNoteItemView(today, noteTitle3, "", false, false, testCase7);
        Log.endTestCase(testCase7);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Add quick note with star
     */
    String testCase8 ="TS009_NoteScripts01_Note08_AddQuickNoteWithStar";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note08_AddQuickNoteWithStar(){
        Log.startTestCase(testCase8);

        // 1. Go to Note view
        common.clickViewBottomBar("Note");
        // 2. Select filter to Star
        note.filterByType("Top", "Star");
        // 3. Check filter to Star
        boolean flag1 = note.checkFilterByType("Star", testCase8);
        // 4. Add new note by quick section => Star
        note.addQuickNote(noteTitle4);

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Log.info("String today from system - "+today);

        // 5. Check star note (Star) is shown
        boolean flag2 = note.checkNoteItemView(today, noteTitle4, "", false, true, testCase8);
        // 6. Check note (Normal) is not shown
        boolean flag3 = common.checkItemExist(noteTitle3, "Not Exist", testCase8);
        // 7. Clean up - Delete star note (Star)
        note.deleteNoteBySwipe(today, noteTitle4,"OK");

        Log.endTestCase(testCase8);
        if((flag1 & flag2 & flag3) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Search note by keyword
     */
    String testCase9 ="TS009_NoteScripts01_Note09_SearchNoteByKeyword";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note09_SearchNoteByKeyword(){
        Log.startTestCase(testCase9);

        //1. Go to Note view
        //2. Input keyword not in list into search section
        note.filterByType("Top","All");
        common.searchItem("Note","No exist");
        //3. Check search result show no result
        boolean flag1 = common.checkSearchResults("Note","No exist","Not Exist",true, testCase9);

        //4. Input keyword exist in list into search section
        common.searchItem("Note", noteTitle3);
        //5. Check search result show note which title contain keyword
        boolean flag2 = common.checkSearchResults("Note", noteTitle3,"Exist",true, testCase9);

        Log.endTestCase(testCase9);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Update note item
     */
    String newTitle;
    String newContent;
    String testCase10 ="TS009_NoteScripts10_Note10_UpdateNoteItem";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts01_Note10_UpdateNoteItem(){
        Log.startTestCase(testCase10);

        //1. Go to Note view
        //2. Select note in the list and open (Normal)
        note.openNoteItem(today, noteTitle3);

        //3. Update note item and Done
        newTitle = "NoteScripts01_Note10";
        newContent = "Updated";
        String updateDateTime = note.updateNoteItem(newTitle, newContent,true,"Add","Home",true);

        // Make String updateTime as Note view (hh:mm AMPM)
        String strTime = updateDateTime.split(" at ",2)[1];
        strTime = (strTime.length() == 6) ? "0" + strTime : strTime;
        String AMPM = strTime.substring(5);
        String updateTime = strTime.replace(AMPM," " + AMPM);
        Log.info("NoteTime is " + updateTime);

        //4. Check note in note view
        boolean flag1 = note.checkNoteItemView(today, newTitle,"", false,true, testCase10);
        //5. Open note again to check
        note.openNoteItem(today, newTitle);
        //6. Check note is updated in details
        boolean flag2 = note.checkNoteItemDetails(newTitle,"",true, newContent,"General,Home",
                "",true, testCase10);

        Log.endTestCase(testCase10);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete all notes
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
