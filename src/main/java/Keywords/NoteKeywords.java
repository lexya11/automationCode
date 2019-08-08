package Keywords;

import Locators.ObjectRepository;
import Support.SetupServer;
import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    HomeKeywords home;
    EmailKeywords email;

    static Logger Log = Logger.getLogger(NoteKeywords.class);

    public NoteKeywords(SetupServer driver){
        this.driver = driver;
        this.common = new CommonKeywords(driver);
        this.home = new HomeKeywords(driver);
        this.email = new EmailKeywords(driver);
    }

    /**
     * Open a Note from list on Note view
     * @param date Ex: "Thu, 02 Apr"
     * @param title Ex: "New Note"
     */
    public void openNoteItem(String date, String title){
        Log.info("Started openNoteItem keyword");
        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Compare date to add text
        if (date.equals(today))
            date = "Today";
        else if(date.equals(yesterday))
            date = "Yesterday";

        // Find note item in list by Xpath
        Log.info("Open Note > "+ date + " - " + title);
        String[] item = {date, title};
        for (int i = 0; i<3; i++){
            if(driver.isElementPresent(OR.note.getLocatorDynamic("itmNote", item))){
                driver.Wait(2);
                driver.findElement(OR.note.getLocatorDynamic("itmNote", item)).click();
                break;
            }else {
                driver.Wait(2);
                Log.info(i);
            }
            if(i == 2){
                driver.captureScreenShot("Not found Note has title '"+title+"' in list");
            }
        }
//        MobileElement noteItem = driver.findElement(OR.note.getLocatorDynamic("itmNote", item));
//        // Click on note
//        noteItem.click();
        Log.info("Ended openNoteItem keyword");
    }

    /**
     * Check Note item in Note view
     * @param title Title of Note
     * @param date  Ex: "Thu, 02 Apr"
     * @param updatedTime   Ex: "11:54 AM". None is not check
     * @param isStar
     * @param isLinked
     * @param testcase
     */
    public boolean checkNoteItemView(String date, String title, String updatedTime, boolean isLinked, boolean isStar, String testcase){
        Log.info("Started checkNoteItemView keyword > " + title);
        boolean flag = true;

        // Check exist
        common.checkItemExist(title, "Exist", testcase);

        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        Log.info("Today - "+today);
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        Log.info("Yesterday - "+yesterday);

        // Compare date to add text
        if (date.equals(today))
            date = "Today";
        else if (date.equals(yesterday))
            date = "Yesterday";
        String[] item = {date, title};

        // Check updatedTime
        if(!updatedTime.equals("")) {
            // Find updated time of Note
            String strUpdateTime = driver.findElement(OR.note.getLocatorDynamic("itmUpdatedTime", item)).getAttribute("value");
            if (strUpdateTime.equals(updatedTime)) {
                Log.info("Passed - Updated time is > " + updatedTime);
            } else {
                Log.info("Failed - Updated time is not > " + updatedTime);
                flag = false;
            }
        }

        // Check Note is Linked or Unlinked
        if(isLinked == true){
            try{
                // Found icon Linked
                driver.isElementExist(OR.note.getLocatorDynamic("itmLinkedIcon", item));
                Log.info("Passed - Note is > Linked");
            } catch (Exception e){
                flag = false;
                Log.info("Failed - Note is > Unlinked");
            }
        } else {
            try{
                // Found icon Starred
                driver.isElementExist(OR.note.getLocatorDynamic("itmLinkedIcon", item));
                Log.info("Failed - Note is > Linked");
                flag = false;
            } catch (Exception e){
                Log.info("Passed - Note is > Unlinked");
            }
        }

        // Check Note is Star or UnStar
        if(isStar == true){
            try{
                // Found icon Starred
                driver.isElementExist(OR.note.getLocatorDynamic("itmStarIcon", item));
                Log.info("Passed - Note is > Star");
            } catch (Exception e){
                flag = false;
                Log.info("Failed - Note is > Unstar");
            }
        } else {
            try{
                // Found icon Starred
                driver.isElementExist(OR.note.getLocatorDynamic("itmStarIcon", item));
                Log.info("Failed - Note is > Star");
                flag = false;
            } catch (Exception e){
                Log.info("Passed - Note is > Unstar");
            }
        }

        // Take screen shoot 1 time
        if(flag == false)
            driver.captureScreenShot(testcase);
        Log.info("Ended checkNoteItemView keyword");
        return flag;
    }

    /**
     * Filter Note By Type
     * @param from Top - filter by type from Navigation Bar / Bottom - filter by type from Bottom Bar
     * @param type Top: All, Star
     */
    public void filterByType (String from , String type){
        Log.info("Started filterByType keywords");
        // Filter by top
        if (from.equals("Top")){
            Log.info("Filter from Navigation Bar");
            driver.findElement(OR.common.getLocator("secNavigationBar")).click();
        }
        // Filter by bottom
        else if(from.equals("Bottom")){
            Log.info("Filter from Bottom Bar");
            //Long press on Note icon in bottom bar
            common.longPressBottomBar("Note");
        }
        Log.info("Select filter > " + type );
        driver.findElement(OR.note.getLocatorDynamic("optFilter",new String[]{type})).click();
        // Check Button 'Done' is enabled
        MobileElement btnDone = driver.findElement(OR.todo.getLocator("btnDoneInLocal"));
        if(btnDone.isEnabled()){
            btnDone.click();
        }else {
            // Close local filter
            MobileElement iconSettings = driver.findElement(OR.common.getLocator("btnSettings"));
            int X = iconSettings.getLocation().getX();
            int Y = iconSettings.getLocation().getY();
            driver.tapCoordinate(X,Y);
        }
        Log.info("Ended filterByType keywords");
    }

    /**
     * Check current filter by type
     * @param type: All, Star
     * @return
     */
    public boolean checkFilterByType(String type, String testcase){
        Log.info("Started checkFilterByType Keyword" );
        boolean flag = true;
        driver.findElement(OR.common.getLocator("secNavigationBar")).click();
        driver.findElement(OR.note.getLocatorDynamic("optFilter",new String[]{type})).click();
        // Check Button 'Done' is enabled
        MobileElement btnDone = driver.findElement(OR.todo.getLocator("btnDoneInLocal"));
        if(btnDone.isEnabled()){
            Log.info("Failed - Filter type is not "+ type);
        }else {
            Log.info("Passed - Filter type is "+ type);
        }
        // Close local filter
        MobileElement iconSettings = driver.findElement(OR.common.getLocator("btnSettings"));
        int X = iconSettings.getLocation().getX();
        int Y = iconSettings.getLocation().getY();
        driver.tapCoordinate(X,Y);

        Log.info("Ended checkFilterByType Keyword" );
        return flag;
    }

    /**
     * Sort note list by options
     * @param option Title - sort by title / Date - sort by date
     */
    public void sortNoteBy(String option){
        Log.info("Started sortNoteBy keywords");
        driver.findElement(OR.common.getLocator("secNavigationBar")).click();
        MobileElement optionFilter = driver.findElement(OR.note.getLocatorDynamic("optSort",new String[]{option}));
        optionFilter.click();
        Log.info("Sort by " + option);
        // Check Button 'Done' is enabled
        MobileElement btnDone = driver.findElement(OR.todo.getLocator("btnDoneInLocal"));
        if(btnDone.isEnabled()){
            btnDone.click();
        }else {
            // Close local filter
            MobileElement iconSettings = driver.findElement(OR.common.getLocator("btnSettings"));
            int X = iconSettings.getLocation().getX();
            int Y = iconSettings.getLocation().getY();
            driver.tapCoordinate(X,Y);
        }
        Log.info("Ended sortNoteBy keywords");
    }

    /**
     * Check sort order of 2 note item in list (default note1.Y < note2.Y)
     * @param date
     * @param title1
     * @param title2
     * @param option
     * @param testcase
     * @return
     */
    public boolean checkSortOrderNoteList(String date, String title1, String title2, String option, String testcase){
        Log.info("Started checkSortOrderNoteList keywords");
        boolean flag = true;

        // Check exist
        if(!common.checkItemExist(title1, "Exist", testcase) & common.checkItemExist(title2, "Exist", testcase)){
            Log.info("Note item is not found");
            driver.captureScreenShot(testcase);
            return false;
        }

        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Compare date1 to add text
        if (date.equals(today))
            date = "Today";
        else if (date.equals(yesterday))
            date = "Yesterday";

        // Check sort order 2 notes
        int Y1 = 0;
        int Y2 = 0;
        if(option.equals("Date")) {
            String[] item1 = {date, title1};
            String[] item2 = {date, title2};

            Y1 = driver.findElement(OR.note.getLocatorDynamic("itmNote", item1)).getLocation().getY();
            Y2 = driver.findElement(OR.note.getLocatorDynamic("itmNote", item2)).getLocation().getY();
        }else if(option.equals("Title")){
            Y1 = driver.findElement(OR.note.getLocatorDynamic("itmNoteTitle", new String[]{title1})).getLocation().getY();
            Y2 = driver.findElement(OR.note.getLocatorDynamic("itmNoteTitle", new String[]{title2})).getLocation().getY();
        }
        Log.info("Y coordinate of Note '"+ title1 + "' is " + Y1);
        Log.info("Y coordinate of Note '"+ title2 + "' is " + Y2);
        // Compare Y to check sort order
        if(Y1 < Y2){
            Log.info("Passed - List note is sorted by " + option);
        }
        else{
            Log.info("Failed - List note is not sorted by " + option);
            driver.captureScreenShot(testcase);
            flag = false;
        }
        Log.info("Ended checkSortOrderNoteList keywords");
        return flag;
    }

    /**
     * Add new Note by quick add section
     * @param title
     */
    public void addQuickNote(String title){
        Log.info("Started addQuickNote keyword");
        Log.info("Quick add Note > " + title);
        driver.findElement(OR.note.getLocator("txbQuickAdd")).sendKeys(title, Keys.ENTER);
        // Wait for create Note and load view
        driver.Wait(2);
        Log.info("Ended addQuickNote keyword");
    }

    /**
     * Add new note from many locations
     * @param title
     * @param content
     * @param isStar
     * @param isDone Done: click Done / Cancel: click Cancel / None: No Action
     * @return String
     */
    public String addNewNote(String title, String content, boolean isStar, String isDone){
        Log.info("Started addNewNote keyword");
        // Add new Note when new Note view was opened
        driver.Wait(2);
        driver.findElement(OR.note.getLocator("viewNote")).click();

        // Input info to add new note
        if(!title.equals("")){
            Log.info("Input title > "+title);
            MobileElement txbTitle = driver.findElement(OR.note.getLocator("txbTitle"));
            txbTitle.click();
            String strTitle = txbTitle.getText();

            if(!strTitle.equalsIgnoreCase("New note")) {
                if(!driver.isElementPresent(OR.common.getLocatorDynamic("btnMenuItemButton", new String[]{"Select All"}))) {
                    // long press on title of Note
                    txbTitle.click();
                    driver.longPressElement(OR.note.getLocator("txbTitle"));
                    driver.Wait(2);
                    common.clickMenuItemTextButton("Select All");
                    driver.Wait(1);
                    common.clickMenuItemTextButton("Cut");
                }
            }
            // Input note title
            txbTitle.setValue(title + "\n");
        }
        if(!content.equals("")) {
            Log.info("Input content > "+content);
            MobileElement txbContent = driver.findElement(OR.note.getLocator("txaContent"));
            txbContent.click();
            txbContent.setValue(content);
        }

        // Click star icon if any
        if(isStar == true){
            Log.info("Set Star for note");
            String Star = "\uE074";
            common.clickStaticTextButton(Star);
        }

        // Get and return dateTime
        String strDateTime = driver.findElement(OR.note.getLocator("lblDateTime")).getAttribute("value");
        // Cut time string
        String strTime = strDateTime.split(" at ", 2)[1];
        String strTime1 = (strTime.length() == 6) ? "0" + strTime : strTime;
        // Get value AM or PM
        String AMPM = strTime1.substring(5);
        // Convert format string Ex: 12:00PM -> 12:00 PM
        String noteTime = strTime.replace(AMPM," " +AMPM);
        Log.info("DateTime is " + strDateTime);
        Log.info("NoteTime is " + noteTime);

        // Click on Done button or Cancel (isDone = None , No action)
        if (isDone.equals("Done")||isDone.equals("Cancel"))
            common.clickElementButton(isDone);
        Log.info("Ended addNewNote Keyword");
        return noteTime;
    }

    /**
     * Send an email by swiping on Note and click on Email icon
     * @param title
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param option Add/Move
     * @param collections
     * @param isSent
     */
    public void sendEmailBySwipe(String date, String title, String from, String to, String cc, String bcc, String subject, String content,String numImage, String option, String collections, boolean isSent){
        Log.info("Started sendEmailBySwipe keyword");

        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Compare date to add text
        if (date.equals(today))
            date = "Today";
        else if(date.equals(yesterday))
            date = "Yesterday";

        String[] item = {date, title};
        // Find Note
        MobileElement itmNote = driver.findElement(OR.note.getLocatorDynamic("itmNote", item));

        int X = itmNote.getLocation().getX(); //0
        int Y = itmNote.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X + 250;
        int Y1 = Y;
        int[] swipeAction = {X,Y,X1,Y1};
        // Swipe left to right on Note
        Log.info("Swipe left to right on Note > " + date + " - " + title);
        driver.touchActionSwipe(swipeAction);

        Log.info("Clicking Email icon");
        driver.tapElement(OR.note.getLocator("icoEmail"));
        driver.Wait(5);
        // Swipe top to bottom to open email header
        driver.touchActionSwipe(190,115,190,315);
        // Compose email and send
        email.sendEmail(from,to,cc,bcc,subject,content,numImage,option,collections,isSent);
        driver.Wait(10);
        Log.info("Ended sendEmailBySwipe keyword");
    }

    /**
     * Add new simple event by swiping on a Note and click on Event icon
     * @param date
     * @param title
     * @param newTitle - Input new title
     * @param isAllDay - set ADE if True
     * @param isDone - True: Click Done button / False: Click Cancel button
     */
    public void addEventBySwipe(String date, String title, String newTitle, boolean isAllDay, boolean isDone){
        Log.info("Started addNewEventBySwipe keyword");

        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Compare date to add text
        if (date.equals(today))
            date = "Today";
        else if(date.equals(yesterday))
            date = "Yesterday";

        // Find Coordinates Note item in list
        String[] item = {date, title};
        MobileElement itmNote = driver.findElement(OR.note.getLocatorDynamic("itmNote", item));

        int X = itmNote.getLocation().getX(); //0
        int Y = itmNote.getLocation().getY();
        // Point in left X, Y = Y + 20
        X = X + 150;
        Y = Y + 20;
        // Swipe right 200 pixel (X = 256, Y = 0)
        int X1 = X+256;
        int Y1 = Y;
        int[] swipeAction = {X,Y,X1,Y1};

        // Swipe left to right on Note
        Log.info("Swipe left to right on Note > " + date + " - " + title);
        driver.touchActionSwipe(swipeAction);
        // Tap on Event icon
        Log.info("Clicking Event icon");
        driver.tapElement(OR.note.getLocator("icoEvent"));

        // Create new Event item
        Log.info("Clean & input title");
        common.closeSendNotification(true);
        driver.Wait(3);
        MobileElement TitleEvent = driver.findElement(OR.agenda.getLocator("txbTitle"));
        // Clear all text in Title field
        int maxChars = TitleEvent.getAttribute("value").length();
        for (int i = 0; i < maxChars; i++)
            TitleEvent.sendKeys(Keys.DELETE);
        // Input event title
        TitleEvent.setValue(newTitle);
        // Tap on "All Day" toggle
        if (isAllDay == true) {
            Log.info("Tap on All Day toggle");
            driver.tapCoordinate(OR.agenda.getCoordinate("coordinatesAllDay"));
        }
        // Click on button
        if (isDone == true)
            common.clickElementButton("Done");
        else
            common.clickElementButton("Cancel");
        Log.info("Ended addNewEventBySwipe keyword");
    }

    /**
     * Add new simple to.do by swiping on a note and click on To.do icon
     * @param date
     * @param title
     */
    public void addTodoBySwipe(String date, String title){
        Log.info("Started addNewTodoBySwipe keyword");

        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Compare date to add text
        if (date.equals(today))
            date = "Today";
        else if(date.equals(yesterday))
            date = "Yesterday";

        // Find Coordinates Event item in list
        String[] item = {date, title};
        MobileElement itmNote = driver.findElement(OR.note.getLocatorDynamic("itmNote", item));
        int X = itmNote.getLocation().getX(); //0
        int Y = itmNote.getLocation().getY();
        // Point in left X, Y = Y + 20
        X = X + 150;
        Y = Y + 20;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+256;
        int Y1 = Y;
        int[] swipeAction = {X,Y,X1,Y1};

        // Swipe left to right on Note
        Log.info("Swipe left to right on Note > " + date + " - " + title);
        driver.touchActionSwipe(swipeAction);
        // Tap on To.Do icon
        Log.info("Clicking ToDo icon");
        driver.tapElement(OR.note.getLocator("icoToDo"));
        Log.info("Ended addNewTodoBySwipe keyword");
    }

    /**
     * Delete Note by swiping on a note and click on Delete icon
     * @param date
     * @param title
     * @param clickButton OK - click Ok to delete/ OK Not Show - delete and not show notification again/ Cancel - click Cancel button
     */
    public void deleteNoteBySwipe(String date, String title, String clickButton){
        Log.info("Started deleteNoteBySwipe keyword");

        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Compare date to add text
        if (date.equals(today))
            date = "Today";
        else if(date.equals(yesterday))
            date = "Yesterday";

        // Find Coordinates Event item in list
        String[] item = {date, title};
        MobileElement itmNote = driver.findElement(OR.note.getLocatorDynamic("itmNote", item));
        int X = itmNote.getLocation().getX(); //0
        int Y = itmNote.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;

        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeAction = {X,Y,X1,Y1};
        // Swipe right to left on To.Do
        Log.info("Swipe right to left on Note > " + date + " - " + title);
        driver.touchActionSwipe(swipeAction);

        driver.Wait(2);
        Log.info("Clicking Trash icon");
        driver.tapElement(OR.note.getLocator("icoTrash"));
        if (clickButton.equalsIgnoreCase("OK"))
            common.clickElementButton("OK");
        else if(clickButton.equalsIgnoreCase("OK Not Show"))
            common.clickElementButton("Ok. Don't show again.");
        else if(clickButton.equalsIgnoreCase("Cancel"))
            common.clickElementButton("Cancel");
        else if(clickButton.equalsIgnoreCase("None"))
            // Do no action
        Log.info("Ended deleteNoteBySwipe keyword");
    }

    /**
     * Open move/add to collection by swiping on a note and click on Collection icon
     * @param date
     * @param title
     */
    public void openCollectionBySwipe(String date, String title){
        Log.info("Started openCollectionBySwipe keyword");

        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Compare date to add text
        if (date.equals(today))
            date = "Today";
        else if(date.equals(yesterday))
            date = "Yesterday";

        // Find Coordinates Event item in list
        String[] item = {date, title};
        MobileElement itmNote = driver.findElement(OR.note.getLocatorDynamic("itmNote", item));
        int X = itmNote.getLocation().getX(); //0
        int Y = itmNote.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeAction = {X,Y,X1,Y1};
        // Swipe right to left on Note
        Log.info("Swipe right to left on Note > " + date + " - " + title);
        driver.touchActionSwipe(swipeAction);

        Log.info("Clicking Collection icon");
        driver.tapElement(OR.note.getLocator("icoCollection"));
        Log.info("Ended openCollectionBySwipe keyword");
    }

    /**
     * Check Note item in note details view
     * @param title  Note Title
     * @param dateTime= "" is no check. Ex: "Thu, 03 May at 08:02AM"
     * @param isStar    True is Star - False is UnStar
     * @param content   Text in content
     * @param collections    1 or many collections, MUST have ‘comma’ for each item if many collections. Ex: None or General or General,Play,Home
     * @param isBack    back to Note view
     * @param testcase
     * @return
     */
    public boolean checkNoteItemDetails(String title, String dateTime, boolean isStar, String content, String collections, String linkedItems, boolean isBack, String testcase){
        Log.info("Started checkNoteItemDetails keyword");
        boolean flag = true;
        driver.Wait(5);
        // Check title
        if(!title.equals("")){
            try{
                String strTitle = driver.findElement(OR.note.getLocator("txbTitle")).getAttribute("value");
                Assert.assertEquals(strTitle, title);
                Log.info("Passed - Title is "+ strTitle);
            } catch (AssertionError aE){
                flag = false;
                Log.info("Failed - Assert Title Error: "+aE.getMessage());
            }
        }

        // Check dateTime
        if(!dateTime.equals("")) {
            // Today
            Calendar cal = Calendar.getInstance();
            String today = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
            // Yesterday
            cal.add(Calendar.DATE, -1);
            String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

            // Format string expect as app display
            String expectDate = dateTime.split(" at ", 2)[0];
            String expectTime = dateTime.split(" at ", 2)[1];
            if (expectDate.equals(today))
                dateTime = "Today at " + expectTime;
            else if (expectDate.equals(yesterday))
                dateTime = "Yesterday at " + expectTime;

            try {
                String strDateTime = driver.findElement(OR.note.getLocator("lblDateTime")).getAttribute("value");
                Assert.assertEquals(strDateTime, dateTime);
                Log.info("Passed - DateTime is " + strDateTime);
            } catch (AssertionError aE) {
                flag = false;
                Log.info("Failed - Assert DateTime Error: " + aE.getMessage());
            }
        }

        // Check Star icon
        String strStar = driver.findElement(OR.note.getLocator("icoStar")).getAttribute("value");

        if(isStar == true){
            try {
                Assert.assertEquals(strStar, "\ue075");
                Log.info("Passed - Note is Star");
            }catch (AssertionError e){
                Log.info("Failed - Note is Unstar " + e.getMessage());
                flag = false;
            }
        }else {
            try {
                Assert.assertEquals(strStar, "\ue074");
                Log.info("Passed - Note is Unstar");
            }catch (AssertionError e){
                Log.info("Failed - Note is Star " + e.getMessage());
                flag = false;
            }
        }

        // Check Content
        if(!content.equals("")) {
            if (content.equals("None")) {
                content = "";
            }
            try {
                String strContent = driver.findElement(OR.note.getLocator("txaContent")).getAttribute("value");
                Assert.assertTrue(strContent.contains(content));
                Log.info("Passed - Content is " + strContent);
            } catch (AssertionError aE) {
                flag = false;
                Log.info("Failed - Assert Content Error: " + aE.getMessage());
            }
        }

        // Check Collection(s) in Collection View
        if(!collections.equals("")){
            // None Collection
            if(collections.equals("None")){
                try {
                    driver.isElementExist(OR.note.getLocator("itmNoneCollection"));
                    Log.info("Passed - Collection is > None");
                } catch (Exception e){
                    Log.info("Failed - Collection is not > None");
                    flag = false;
                }

            }
            // Check Multiple Collections in Collection field
            else if(collections.contains(",")){
                // Count length of String Collection
                int length = collections.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitCollection = collections.split(",")[i];
                    try {
                        driver.isElementExist(OR.note.getLocatorDynamic("itmCollection", new String[]{splitCollection}));
                        Log.info("Passed - Collection is > "+splitCollection);
                    }catch (Exception e){
                        Log.info("Failed - Collection is not > "+splitCollection);
                        flag = false;
                    }
                }
            }
            // Check a Collection in Collection field
            else {
                // Convert String collections, get space in text
                String convertCollection = collections;
                try {
                    driver.isElementExist(OR.note.getLocatorDynamic("itmCollection", new String[]{convertCollection}));
                    Log.info("Passed - Collection is > " + collections);
                } catch (Exception e) {
                    Log.info("Failed - Collection is not > " + collections);
                    flag = false;
                }
            }
        }

        // Check Items(s)in Links section
        if(!linkedItems.equals("")){
            if (linkedItems.equals("None")) {
                try {
                    driver.isElementExist(OR.note.getLocator("txbItemLinked"));
                    Log.info("Failed - Linked is > None");
                    flag = false;
                } catch (Exception e) {
                    Log.info("Passed - Linked is not > None");
                }
            } else if (linkedItems.contains(",")) {
                // Count length of String Linked Items
                int length = linkedItems.split(",").length;
                for (int i = 0; i < length; i++) {
                    String splitLinkedItems = linkedItems.split(",")[i];
                    try {
                        String actualText = driver.findElement(OR.note.getLocatorDynamic("txbLink", new String[]{splitLinkedItems})).getAttribute("name");
                        Assert.assertEquals(actualText, splitLinkedItems);
                        Log.info("Passed - Note is linked with > " + splitLinkedItems);
                    } catch (AssertionError aE) {
                        Log.info("Failed - Note is not linked with > " + splitLinkedItems + " - " + aE.getMessage());
                        flag = false;
                    }
                }
            } else {
                try {
                    String actualText = driver.findElement(OR.note.getLocatorDynamic("txbLink", new String[]{linkedItems})).getAttribute("name");
                    Assert.assertEquals(actualText, linkedItems);
                    Log.info("Passed - Note is linked with > " + linkedItems);
                } catch (AssertionError aE) {
                    Log.info("Failed - Note is not linked with > " + linkedItems + " - " + aE.getMessage());
                    flag = false;
                }
            }
        }

        // Take screen shoot 1 time
        if(flag == false)
            driver.captureScreenShot(testcase);

        // Back to note view
        if(isBack == true)
            common.clickElementButton("Notes");
        Log.info("Ended checkNoteItemDetails keyword");
        return flag;
    }

    /**
     * Send an email from note details and click on Email icon
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param option (Add/Move)
     * @param collections
     * @param isSent
     */
    public void sendEmailByDetails(String from, String to, String cc, String bcc, String subject, String content, String numImage,String option,String collections, boolean isSent){
        Log.info("Started sendEmailByDetails keyword");
        Log.info("Clicking Email icon");
        driver.findElement(OR.note.getLocator("toolBarEmail")).click();
        driver.Wait(5);
        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,315);
        driver.Wait(2);
        email.sendEmail(from,to,cc,bcc,subject,content,numImage, option,collections,isSent);
        Log.info("Ended sendEmailByDetails keyword");
    }

    /**
     * Add new event from Note details and click on Event icon
     * @param newTitle Input New Event's title
     * @param newIsAllDay True: set All Day for Event
     * @param isDone True - Tap on "Done" button/ False: click Cancel
     */
    public void addEventByDetails(String newTitle, boolean newIsAllDay, boolean isDone){
        Log.info("Started addEventByDetails keyword");
        Log.info("Tap on Event icon");
        driver.findElement(OR.note.getLocator("toolBarEvent")).click();

        // Edit New Event's title
        MobileElement TitleEvent  =  driver.findElement(OR.agenda.getLocator("txbTitle"));

        Log.info("Delete old title");
        int maxChars = TitleEvent.getAttribute("value").length();
        for (int i = 0; i < maxChars; i++)
        {
            TitleEvent.sendKeys(Keys.DELETE);
        }
        Log.info("Input new title > "+ newTitle);
        TitleEvent.setValue(newTitle);

        if (newIsAllDay == true) {
            // Tap on "All Day" toggle
            Log.info("Tap on All Day toggle");
            driver.tapCoordinate(OR.agenda.getCoordinate("coordinatesAllDay"));
        }
        if (isDone == true){
            // Tap on "Done" button
            common.clickElementButton("Done");
        }else {
            // Tap on "Cancel" button
            common.clickElementButton("Cancel");
        }
        Log.info("Ended addEventByDetails keyword");
    }

    /**
     * Add new to.do from event details and click on To.do icon
     */
    public void addTodoByDetails(){
        Log.info("Started addTodoByDetails keyword");
        Log.info("Tap on ToDo icon");
        driver.findElement(OR.note.getLocator("toolBarTodo")).click();
        Log.info("Ended addTodoByDetails keyword");
    }

    /**
     * Update note item.
     * All params = "" will skip
     * @param title
     * @param content
     * @param isStar
     * @param collections
     * @param isDone
     * @return
     */
    public String updateNoteItem(String title, String content, boolean isStar, String option, String collections, boolean isDone ){
        Log.info("Started updateNoteItem keyword");
        boolean isUpdated = false;
        String updateDateTime;
        driver.Wait(2);

        // Update title
        if(!title.equals("")){
            isUpdated = true;
            Log.info("Input title > "+ title);
            MobileElement txbTitle = driver.findElement(OR.note.getLocator("txbTitle"));
            txbTitle.clear();
            // Delete all text in Title field
            int maxChars = txbTitle.getText().length();
            for (int i = 0; i < maxChars; i++)
            {
                txbTitle.sendKeys(Keys.DELETE);
            }
            txbTitle.setValue(title + "\n");
        }

        // Update content
        if(!content.equals("")) {
            isUpdated = true;
            MobileElement txbContent = driver.findElement(OR.note.getLocator("txaContent"));
//            txbContent.click();
            String text = txbContent.getText();
            if(text != null && !text.isEmpty()) {
                Log.info("Delete old title");
                for (int i = 0; i < text.length(); i++) {
                    txbContent.sendKeys(Keys.DELETE);
                }
            }
            Log.info("Input content > "+ content);
            txbContent.setValue(content);
            // Close keyboard
            if(driver.isElementPresent(OR.note.getLocator("btnCloseKeyboard")))
                driver.findElement(OR.note.getLocator("btnCloseKeyboard")).click();
        }

        // Update star icon
        String strStar = driver.findElement(OR.note.getLocator("icoStar")).getAttribute("value");
        if(isStar == true){
            Log.info("Set star note");
            String Unstar = "\uE074";
            if(strStar.equals(Unstar)) {
                isUpdated = true;
                common.clickStaticTextButton(Unstar);
            }
        }else{
            Log.info("Set unstar note");
            String Star = "\ue075";
            if(strStar.equals(Star)) {
                isUpdated = true;
                common.clickStaticTextButton(Star);
            }
        }

        // Update collection
        common.addMoveToCollection(true, option, collections);

        // Wait for load Edit note view
        driver.Wait(1);
        if(isUpdated == true) {
            // Get updateTime
            Date currentDate = new Date();
            String date = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
            String time = new SimpleDateFormat("h:mma").format(currentDate);
            updateDateTime = date + " at "+ time;

            // Click on Done button or Cancel
            if (isDone == true)
                common.clickElementButton("Done");
            else
                common.clickElementButton("Cancel");
        }else {
            updateDateTime = driver.findElement(OR.note.getLocator("lblDateTime")).getAttribute("value");
            common.clickElementButton("Notes");
        }
        Log.info("Started updateNoteItem keyword");
        return updateDateTime;
    }
}
