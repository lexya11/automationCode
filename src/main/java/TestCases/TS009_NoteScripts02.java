package TestCases;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TS009_NoteScripts02 extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SignInKeywords signin;
    CommonKeywords common;
    NoteKeywords note;
    CollectionKeywords collection;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    boolean flag = true;
    String today;

    String titleNote1 = "NoteScripts02_Note01";
    String titleNote2 = "NoteScripts02_Note02_Star1";
    String titleNote3 = "NoteScripts02_Note02_Star2";
    String titleNote4 = "NoteScripts02_Note08";

    @BeforeClass
    public void SetUp() {
        Log.startTestCase("Set Up "+ this.getClass().getSimpleName());
        home = new HomeKeywords(this);
        signin = new SignInKeywords(this);
        common = new CommonKeywords(this);
        note = new NoteKeywords(this);
        collection = new CollectionKeywords(this);

        capabilities = new MobileCapabilities(device,false);

        boolean statusLogin = false;
        int count =0;
        while(statusLogin == false){
            try{
                SetUp(capabilities);

                //Sign in Flo iPhone
                signin.skipIntroduce();
                signin.openLoginScreen();
                signin.login(username, password,true);

                // Close start Flo popup
                common.closeStartFlo();

                // Add note Normal & Star
                common.clickViewBottomBar("Note");
                common.clearView();
                note.filterByType("Top", "Star");

                note.addQuickNote(titleNote1);
                note.addQuickNote(titleNote2);
                note.addQuickNote(titleNote3);

                // Generate String today
                Date currentDate = new Date();
                today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
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
     * Unstar note from note view
     */
    String testCase1 ="TS009_NoteScripts02_Note01_unStarNoteFromNoteView";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note01_unStarNoteFromNoteView(){
        Log.startTestCase(testCase1);
        // 1. In Note view
        // 2. Click on Star icon of note "Star"
        Wait(2);
        common.clickStarItem(titleNote1,"UnStar");
        // 3. Check note is un_star in view
        boolean flag1 = common.checkItemExist(titleNote1,"Not Exist",testCase1);
        note.filterByType("Top", "All");
        boolean flag2 = common.checkItemExist(titleNote1,"Exist",testCase1);
        // 4. Open note
        note.openNoteItem(today, titleNote1);
        // 5. Check note is unstar in detail view
        boolean flag3 = note.checkNoteItemDetails("","",false,"","","",true,testCase1);
        Log.endTestCase(testCase1);
        if((flag1 & flag2 & flag3) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Filter All shows all notes
     */
    String testCase2 ="TS009_NoteScripts02_Note02_filterAllShowsAllNotes";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note02_filterAllShowsAllNotes(){
        Log.startTestCase(testCase2);
        // 1. Go to Note view
        // 2. Select filter to All
        note.filterByType("Top", "All");
        // 3. Check filter to All
        boolean flag1 = note.checkFilterByType("All", testCase2);

        // 4. Check star note (Star) is shown
        boolean flag2 = common.checkItemExist(titleNote2, "Exist", testCase2);
        boolean flag3 = common.checkItemExist(titleNote3, "Exist", testCase2);

        // 5. Check note (Normal) is shown
        boolean flag4 = common.checkItemExist(titleNote1, "Exist", testCase2);

        Log.endTestCase(testCase2);
        if((flag1 & flag2 & flag3 & flag4) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Filter All & sort by title
     */
    String testCase3 ="TS009_NoteScripts02_Note03_filterAllSortByTitle";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note03_filterAllSortByTitle(){
        Log.startTestCase(testCase3);
        // 1. Go to Note view
        // 2. Select filter to All & Sort by title
        note.sortNoteBy("Title");
        // 3. Check note is sorted by title
        flag = note.checkSortOrderNoteList(today, titleNote1, titleNote2, "Title", testCase3);
        Log.endTestCase(testCase3);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Filter All & sort by date
     */
    String testCase4 ="TS009_NoteScripts02_Note04_filterAllSortByDate";
    @Test
    public void TS009_NoteScripts02_Note04_filterAllSortByDate(){
        Log.startTestCase(testCase4);
        // 1. Go to Note view
        // 2. Select filter to All & Sort by date
        note.sortNoteBy("Date");
        // 3. Check note is sorted by date
        flag = note.checkSortOrderNoteList(today, titleNote2, titleNote1, "Date", testCase4);
        Log.endTestCase(testCase4);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Filter Star shows only star note
     */
    String testCase5 ="TS009_NoteScripts02_Note05_filterStarShowsOnlyStarNote";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note05_filterStarShowsOnlyStarNote(){
        Log.startTestCase(testCase5);
        // 1. Go to Note view
        // 2. Select filter to Star
        note.filterByType("Top", "Star");
        // 3. Check filter to Star
        boolean flag1 = note.checkFilterByType("Star", testCase5);

        // 4. Check star note (Star) is shown
        boolean flag2 = common.checkItemExist(titleNote2, "Exist", testCase5);
        boolean flag3 = common.checkItemExist(titleNote3, "Exist", testCase5);

        // 5. Check note (Normal) is not shown
        boolean flag4 = common.checkItemExist(titleNote1, "Not Exist", testCase5);
        Log.endTestCase(testCase5);
        if((flag1 & flag2 & flag3 & flag4) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Filter All & sort by title
     */
    String testCase6 ="TS009_NoteScripts02_Note06_filterAllSortByTitle";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note06_filterAllSortByTitle(){
        Log.startTestCase(testCase6);
        // 1. Go to Note view
        // 2. Select filter to Star & Sort by title
        note.sortNoteBy("Title");
        // 3. Check note is sorted by title
        flag = note.checkSortOrderNoteList(today, titleNote2, titleNote3,"Title", testCase6);
        Log.endTestCase(testCase6);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Filter All & sort by date
     */
    String testCase7 ="TS009_NoteScripts02_Note07_filterAllSortByDate";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note07_filterAllSortByDate(){
        Log.startTestCase(testCase7);
        // 1. Go to Note view
        // 2. Select filter to Star & Sort by date
        note.sortNoteBy("Date");
        // 3. Check note is sorted by date
        flag = note.checkSortOrderNoteList(today, titleNote3, titleNote2, "Date", testCase7);
        Log.endTestCase(testCase7);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Move note item to other collection
     */
    String testCase8 ="TS009_NoteScripts02_Note08_moveNoteItemToOtherCollection";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note08_moveNoteItemToOtherCollection(){
        Log.startTestCase(testCase8);
        // 1. Go to Note view
        note.filterByType("Bottom","All");
        // 2. Select note in the list and open
        Wait(1);
        note.openNoteItem(today, titleNote1);
        // 3. Check note item details
        boolean flag1 = note.checkNoteItemDetails(titleNote1,"",false,"","General",
                "",false, testCase8);
        // 4. Move note item to other collection
        note.updateNoteItem(titleNote4,"",true,"Move","Home",true);
        // 5. Re-open note item
        note.openNoteItem(today, titleNote4);
        // 6. Check Collection section show new collection
        boolean flag2 = note.checkNoteItemDetails(titleNote4, "",true,"","Home",
                "",true, testCase8);
        Log.endTestCase(testCase8);
        if(!(flag1 & flag2))
            Assert.fail("Test case is failed");
    }

    /**
     * Local filter by a collection
     */
    String testCase9 ="TS009_NoteScripts02_Note09_localFilterByACollection";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note09_localFilterByACollection(){
        Log.startTestCase(testCase9);
        // 1. Go to Note view
        // 2. Select local filter to a collection
        common.selectLocalFilter("Home");
        // 3. Check local filter is Home
        boolean flag1 = common.checkLocalFilter("Home", testCase9);
        // 4. Check note (Normal) is shown
        boolean flag2 = common.checkItemExist(titleNote4, "Exist", testCase9);
        Log.endTestCase(testCase9);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Add Note Item To Many Collection
     */
    String testCase10 ="TS009_NoteScripts02_Note10_addNoteItemToManyCollection";
    @Test(retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note10_addNoteItemToManyCollection(){
        Log.startTestCase(testCase10);
        // 1. Go to Note view
        // 2. Open a note in the list Ex: Normal
        note.openNoteItem(today, titleNote4);

        // 3. Add note item to many collection Ex: General,Home
        note.updateNoteItem(titleNote1,"",false,"Add","General,Home",true);

        // 4. Re-open note item
        note.openNoteItem(today,titleNote1);

        // 5. Check Collection section show new collections
        flag = note.checkNoteItemDetails(titleNote1,"",false,"","General,Home","",true, testCase10);
        Log.endTestCase(testCase10);
        if(flag == false)
            Assert.fail("Test case is failed");
    }

    /**
     * Local filter by multi collection
     */
    String testCase11 ="TS009_NoteScripts02_Note11_localFilterByMultiCollection";
    @Test(dataProvider = "MultiCollection",retryAnalyzer=RetryFailedTestCases.class)
    public void TS009_NoteScripts02_Note11_localFilterByMultiCollection(String testcase, String collection){
        Log.startTestCase(testCase11);
        // 1. Go to Note view
        // 2. Select local filter to multi collection Ex: General,Home
        common.selectLocalFilter(collection);
        // 3. Check local filter is multi select
        boolean flag1 = common.checkLocalFilter(collection, testcase);
        // 4. Check note (Normal) is shown
        // 5. Repeat with local filter is General, Home
        boolean flag2 = common.checkItemExist(titleNote1, "Exist", testcase);
        Log.endTestCase(testCase11);
        if((flag1 & flag2) == false)
            Assert.fail("Test case is failed");
    }
    @DataProvider
    public Object[][] MultiCollection() throws Exception{
        Object[][] MultiCollection = ExcelUtils.getTableArray(Constants.NOTE_DATA,"MultiCollection");
        return (MultiCollection);
    }

    @AfterClass
    public void clearUp(){
        Log.startTestCase("Clean Up "+ this.getClass().getSimpleName());
        // Delete Notes
        common.selectLocalFilter("All Collections");
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