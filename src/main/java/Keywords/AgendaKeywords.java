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

public class AgendaKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    HomeKeywords home;
    EmailKeywords email;

    static Logger Log = Logger.getLogger(AgendaKeywords.class);

    public AgendaKeywords(SetupServer driver){
        this.driver = driver;
        this.common = new CommonKeywords(driver);
        this.home = new HomeKeywords(driver);
        this.email = new EmailKeywords(driver);
    }

    /**
     * Check the week start on Calendar
     * @param day (Sunday, Monday)
     * @return
     */
    public boolean checkWeekStart(String day, String testcase){
        Log.info("Started checkWeekStart keyword");
        boolean flag = true;
        try {
            // Sunday or Monday of Week start
            MobileElement Weekstart = driver.findElement(OR.agenda.getLocatorDynamic("secWeekStart", new String[]{day}));
            String dayOfWeek = Weekstart.getAttribute("name");
            Log.info("Week start is: " + dayOfWeek);
            Assert.assertEquals(dayOfWeek, day);
        } catch (AssertionError aE) {
            flag = false;
            Log.info("Assert Error " + aE.getMessage());
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkWeekStart keyword");
        return flag;
    }

    /**
     * Check default date is today when Agenda view is opened
     * check "Today" button if it NOT show in Agenda list -> OK
     * check current Month of Year on header of Calendar
     * @return
     */
    public boolean checkTodayAgenda(String testcase){
        Log.info("Started checkTodayAgenda keyword");
        boolean flag = true;
        Date currentDate = new Date();

        // Check "Today" button not exist in Agenda List.
        try {
            //Find "Today" button
            driver.isElementExist(OR.common.getLocatorDynamic("btnElementButton",new String[]{"Today"}));
            flag = false;
            Log.info("Failed - Today button exist in Agenda view");
        } catch (Exception e) {
            Log.info("Passed - Today button not exist in Agenda view");
        }

        // Check Month of Year on header of Calendar.
        try {
            // Get current Month Year on System
            String monthYear = new SimpleDateFormat("MMMM YYYY").format(currentDate);
            // Get Month Year on Header of Calendar
            String monthOfYear = driver.findElement(OR.agenda.getLocator("secMonthOfYear")).getAttribute("name");
            // Compare Month Year in Calendar with Month Year on System
            Assert.assertEquals(monthOfYear,monthYear);
            Log.info("Passed - Show "+ monthOfYear +" on header of Calendar ");
        } catch (AssertionError aE) {
            flag = false;
            Log.info("Show Month of Year on Header Calendar incorrectly: " + aE.getMessage());
        }
        if (flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkTodayAgenda keyword");
        return  flag;
    }

    /**
     * Check Date section in Agenda list. (Ex: Today Mon, 09 Apr/ Tomorrow Tue, 10 Apr)
     * @param day (Today / Tomorrow)
     * @param testcase
     * @return
     */
    public boolean checkDateSection(String day, String testcase){
        Log.info("Started checkDateSection keyword");
        boolean flag = true;
        String expectDate = "";

        try {
            Calendar cal = Calendar.getInstance();
            // Get expected date (Ex: Mon, 09 Apr)
            if(day.equals("Today"))
                expectDate = "Today " + new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
            else if(day.equals("Tomorrow")) {
                cal.add(Calendar.DATE,1);
                expectDate = "Tomorrow " + new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
            }
            // Check Date section in Agenda list
            driver.isElementExist(OR.agenda.getLocatorDynamic("secDate",new String[]{expectDate}));
            Log.info("Passed - Date section shows: "+ expectDate);
        } catch (Exception e){
            flag = false;
            Log.info("Failed - Not show date " + expectDate + " section in Agenda view");
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkDateSection keyword");
        return flag;
    }

    /**
     * Add a simple event from many locations
     * @param title (Title of Event)
     * @param isAllDay (true: add All Day Event / false: add normal event)
     * @param clickButton: Done: click Done/ Cancel: click Cancel/ None: No action
     */
    public String addSimpleEvent(String title, boolean isAllDay, String clickButton){
        Log.info("Started addSimpleEvent keyword");
        String eventTime = "";
        driver.Wait(2);
        try{
            driver.isElementExist(OR.agenda.getLocator("txbTitle"));
            Log.info("Input Event's title");
            MobileElement TitleEvent  =  driver.findElement(OR.agenda.getLocator("txbTitle"));
            TitleEvent.clear();
            TitleEvent.setValue(title);
        }catch (Exception e){
            // Edit, input Event's title
            Log.info("Edit Event's title");
            MobileElement titleEvent  =  driver.findElement(OR.agenda.getLocator("txtTitleEditView"));
            titleEvent.clear();
            // Clear all text in Title field
            int maxChars = titleEvent.getAttribute("value").length();
            for (int i = 0; i < maxChars; i++){
                titleEvent.sendKeys(Keys.DELETE);
            }
            // Input event title
            titleEvent.setValue(title);
        }
        // Tap on "All Day" toggle
        if (isAllDay == true)
        {
            Log.info("Tap on All Day toggle");
            driver.tapCoordinate(OR.agenda.getCoordinate("coordinatesAllDay"));
        }else {
            // Get current Start time of Event
            String StartTime = driver.findElement(OR.agenda.getLocator("txbStartTime")).getAttribute("value");
            // Get value AM or PM
            String AMPM = StartTime.substring(5);
            // Convert format string Ex: 12:00PM -> 12: 00 PM
            eventTime = StartTime.replace(AMPM," "+AMPM);
        }
        // Tap on "Done" button
        if (clickButton.equalsIgnoreCase("Done")){
            // Tap on "Done" button
            common.clickElementButton("Done");
        }else if(clickButton.equalsIgnoreCase("Cancel")){
            // Tap on "Cancel" button
            common.clickElementButton("Cancel");
        }else if(clickButton.equalsIgnoreCase("None")) {
            // No action
        }
        Log.info("Ended addSimpleEvent keyword");
        return eventTime;
    }

    /**
     * Todo update later
     * Check popup Message will display on Agenda View
     * @param message
     * @param clickButton
     * @param testcase
     * @return
     */
    public boolean checkMessage(String message, String clickButton, String testcase){
        Log.info("Started checkMessage keyword");
        boolean flag = true;
        // Check message on popup
        try {
            String actualText = driver.findElement(OR.agenda.getLocator("pupMessage")).getAttribute("value");
            Assert.assertEquals(actualText, message);
            Log.info("Passed - Message shows: "+ actualText);
        }catch (AssertionError aE){
            Log.info("Failed - Assert Error: " + aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        // Click button on popup
        if(clickButton.equals("OK"))
            common.clickElementButton("OK");
        else if(clickButton.equals("Ok - Not Show"))
            common.clickElementButton("Ok. Don't show again.");
        else if(clickButton.equals("Cancel"))
            common.clickElementButton("Cancel");
        else
            Log.info("Parameter input failed, please check again!!!");
        Log.info("Ended checkMessage keyword");
        return flag;
    }

    /**
     * Open an event from list on Agenda view
     * @param date Ex: "Thu, 12 Apr"
     * @param startTime Ex: "03:00 PM"
     * @param title Ex: "New Event"
     */
    public void openEventItem(String date, String startTime, String title) {
        Log.info("Started openEvent keyword");
        driver.Wait(7);
        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        // Compare date to add text
        if (date.equals(today))
            date = "Today " + date;
        else if(date.equals(tomorrow))
            date = "Tomorrow " + date;
        // Find event item in list by Xpath
        Log.info("Open Event: "+ date + " - " + startTime + " - " + title);
        String[] eventValue = {date, startTime, title};
        MobileElement eventItem = driver.findElement(OR.agenda.getLocatorDynamic("eventitem", eventValue));
        // Click on event
        eventItem.click();
        Log.info("Ended openEvent keyword");
    }

    /**
     * Check event item in Agenda view
     * @param date
     * @param startTime
     * @param title
     * @param location: Ex: input "None", Event does not contain Location.
     * @param isInvitee
     * @param isLinked
     * @param testcase
     */
    public boolean checkEventItemView(String date, String startTime, String title, String location, boolean isInvitee, boolean isLinked, String testcase){
        Log.info("Started checkEventItemView keyword");
        driver.Wait(30);
        boolean flag = true;

        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Fix Screen was scrolled
        if(date.equals(today)){
            driver.touchActionSwipe(300,165,80,166);
            driver.Wait(1);
            driver.touchActionSwipe(300,165,80,166);
            driver.Wait(1);
            driver.tapCoordinate(341,562);
        }
        if (date.equals(today))
            date = "Today " + date;
        else if(date.equals(tomorrow))
            date = "Tomorrow " + date;
        String[] eventValue = {date, startTime, title};
        try{
            // Find Event item in list
            driver.isElementExist(OR.agenda.getLocatorDynamic("eventitem", eventValue));
            Log.info("Found Event: Date "+ date + " - Start time: " + startTime + " - title: " + title);

            // Check Location
            if(!location.equals("")){
                String actualText = driver.findElement(OR.agenda.getLocatorDynamic("eventLocation",eventValue)).getAttribute("name");
                if(location.equals("None")){
                    location = null;
                }
                try{
                    Assert.assertEquals(actualText,location);
                    Log.info("Location of Event is: "+location);
                } catch (AssertionError aE){
                    // Found Location of Event - Failed
                    Log.info("Failed - Location of Event: "+aE.getMessage());
                    flag = false;
                }
            }
            // Fix Screen was scrolled
            if(date.equals("Today "+today)){
                driver.touchActionSwipe(300,165,80,166);
                driver.Wait(1);
                driver.touchActionSwipe(300,165,80,166);
                driver.Wait(1);
                driver.tapCoordinate(341,562);
            }
            // Check Invitees and Linked
            try{
                // Event contains Invitee and Linked icon
                if(isInvitee == true && isLinked == true){
                    String iconInviLink = "\uE07E\uE05D";
                    String[] EventValue = {date, iconInviLink, startTime, title};
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoInviAndLink",EventValue));
                    Log.info("Passed - Event '"+title+"' has Linked icon");
                    Log.info("Passed - Event '"+title+"' has Invitee icon");
                }
                // Event only having Invitee does not contain Linked icon
                else if(isInvitee == true && isLinked == false){
                    String iconInvitee = "\uE07E";
                    String[] EventValue = {date, iconInvitee, startTime, title};
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoInviAndLink",EventValue));
                    Log.info("Passed - Event '"+title+"' has Invitee icon");
                    Log.info("Passed - Event '"+title+"' has no Linked icon");
                }
                // Event only having Linked icon does not contain Invitee
                else if(isInvitee == false && isLinked == true){
                    String iconLinked = "\uE05D";
                    String[] EventValue = {date, iconLinked, startTime, title};
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoInviAndLink",EventValue));
                    Log.info("Passed - Event '"+title+"' has Linked icon");
                    Log.info("Passed - Event '"+title+"' has no Invitee icon");

                }
                // Event does not contain both Linked icon and Invitee
                else if(isInvitee == false && isLinked == false){
                    driver.isElementExist(OR.agenda.getLocatorDynamic("NoneicoInviAndLink",eventValue));
                    Log.info("Passed - Event '"+title+"' has no Invitee icon");
                    Log.info("Passed - Event '"+title+"' has no Linked icon");
                }
            } catch (Exception e){
                // Found Invitee and Linked icon - Failed
                Log.info("Failed - Event '"+title+"' does not contains Invitee icon: "+isInvitee+ " and Linked icon: "+isLinked);
                flag = false;
            }
        } catch (Exception e){
            // Found Event - Failed
            Log.info("Failed - Not found Event: on date "+ date + " - Start time: " + startTime + " - title: " + title);
            flag = false;
        }
        if(flag == false){
            driver.captureScreenShot(testcase);
        }
        // Fix Screen was scrolled
        if(date.equals("Today "+today)){
            driver.touchActionSwipe(300,165,80,166);
            driver.Wait(1);
            driver.touchActionSwipe(300,165,80,166);
            driver.Wait(1);
            driver.tapCoordinate(341,562);
        }
        Log.info("Ended checkEventItemView keyword");
        return flag;
    }

    /**
     * Check Detail of an Event (Simple Event). All params = "" will skip check it
     * @param title
     * @param isAllDay
     * @param invitees: 1 or many invitees, MUST have ‘comma’ for each item. Ex: Test Contact,ABC Contact,New Contact
     * @param collections: 1 or many collections, MUST have ‘comma’ for each item. Ex: None or General,Play,Home
     * @param linkedItems: 1 or many linked items, MUST have ‘comma’ for each item. Ex: None or Event1,Contact,Note
     * @param testcase
     * @return
     */
    public boolean checkEventItemDetails(String title, boolean isAllDay, String invitees, String collections, String linkedItems, String testcase){
        Log.info("Started checkEventItemDetails keyword");
        boolean flag = true;

        // Check Title of Event
        if(!title.equals("")){
            String titleEvent = driver.findElement(OR.agenda.getLocator("txtTitleEditView")).getAttribute("name");
            try{
                Assert.assertEquals(titleEvent, title);
                Log.info("Passed - Title of Event is: " + title);
            }catch (AssertionError aE){
                Log.info("Failed - Show Title of Event Error: " + aE.getMessage());
                flag = false;
            }
        }

        // Check status of All Day toggle
        String ObserveStatus = driver.findElement(OR.agenda.getLocator("tglAllDay")).getAttribute("value");
        boolean AllDayON = ObserveStatus.equals("1") == true;
        try {
            Assert.assertEquals(AllDayON,isAllDay);
            Log.info("Passed - Toggle All Day is: "+isAllDay);
        }catch (AssertionError aE){
            Log.info("Failed - Toggle All Day is: "+aE.getMessage());
            flag = false;
        }
        // Check Collection(s) in Collection View
        if(!collections.equals("")){
            if(collections.equals("None")){
                try {
                    driver.isElementExist(OR.agenda.getLocator("noneCollection"));
                    Log.info("Passed - Event "+title+ " does not contain Collection");
                } catch (Exception e){
                    Log.info("Failed - Event "+title+ " contains Collection");
                    flag = false;
                }
            } else if(collections.contains(",")){
                // Count length of String Collection
                int length = collections.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitCollection = collections.split(",")[i];
                    try {
                        driver.isElementExist(OR.agenda.getLocatorDynamic("itmCollection", new String[]{splitCollection}));
                        Log.info("Passed - Event "+title+ " contains Collection(s): "+splitCollection);
                    }catch (Exception e){
                        Log.info("Failed - Event "+title+ " does not contain Collection(s): "+splitCollection);
                        flag = false;
                    }
                }
            } else {
                try {
                    driver.isElementExist(OR.agenda.getLocatorDynamic("itmCollection", new String[]{collections}));
                    Log.info("Passed - Event "+title+ " contains Collection: "+collections);
                } catch (Exception e){
                    Log.info("Failed - Event "+title+ " does not contain Collection: "+collections);
                    flag = false;
                }
            }
        }
        // Check Invitee(s) in Invitees field
        if(!invitees.equals("")){
            if(invitees.equals("None")){
                invitees = "Invitees";
                try {
                    driver.isElementExist((OR.agenda.getLocatorDynamic("tbInvitee", new String[]{invitees})));
                    Log.info("Passed - Event "+title+ " does not contain Invitee");
                } catch (Exception e){
                    Log.info("Failed - Event "+title+ " contains Invitee");
                    flag = false;
                }
            } else if(invitees.contains(",")){
                // Count length of String invitees
                int length = invitees.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitInvitees = invitees.split(",")[i];
                    try {
                        driver.isElementExist(OR.agenda.getLocatorDynamic("tbInvitee", new String[]{splitInvitees}));
                        Log.info("Passed - Event "+title+ " contains Invitee(s): "+splitInvitees);
                    } catch (Exception e){
                        Log.info("Failed - Event "+title+ " does not contain Invitee(s): "+splitInvitees);
                        flag = false;
                    }
                }
            } else {
                try {
                    driver.isElementExist(OR.agenda.getLocatorDynamic("tbInvitee", new String[]{invitees}));
                    Log.info("Passed - Event "+title+ " contains Invitee: "+invitees);
                } catch (Exception e){
                    Log.info("Failed - Event "+title+ " does not contain Invitee: "+invitees);
                    flag = false;
                }
            }
        }
        // Check Items(s) linked in Links section
        if(!linkedItems.equals("")){
            if(linkedItems.equals("None")){
                try {
                    driver.isElementExist(OR.agenda.getLocator("txbItemLinked"));
                    Log.info("Failed - Event "+title+ " is linked");
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Event "+title+ " is not linked");
                }
            } else if(linkedItems.contains(",")){
                // Count length of String Linked Items
                int length = linkedItems.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitLinkedItems = linkedItems.split(",")[i];
                    try {
                        driver.isElementExist(OR.agenda.getLocatorDynamic("txbLink", new String[]{splitLinkedItems}));
                        Log.info("Passed - Event "+title+ " is linked with: "+splitLinkedItems);
                    }catch (Exception e){
                        Log.info("Failed - Event "+title+ " does not link with: "+splitLinkedItems);
                        flag = false;
                    }
                }
            }else {
                try {
                    driver.isElementExist(OR.agenda.getLocatorDynamic("txbLink", new String[]{linkedItems}));
                    Log.info("Passed - Event "+title+ " is linked with: "+linkedItems);
                } catch (Exception e){
                    Log.info("Failed - Event "+title+ " does not link with: "+linkedItems);
                    flag = false;
                }
            }
        }

        // Take screen shoot 1 time
        if (flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkEventItemDetails keyword");
        return flag;
    }

    /**
     * Add new simple event by swiping on an event and click on Event icon
     * @param date
     * @param startTime
     * @param title
     * @param newTitle - Input new title
     * @param newIsAllDay - set ADE if True
     * @param ClickButton - Done: click Done/ Cancel: click Cancel/ None: No action
     */
    public void addEventBySwipe(String date, String startTime, String title, String newTitle, boolean newIsAllDay, String clickButton, boolean isIcs, String ClickButton){
        Log.info("Started addNewEventBySwipe keyword");
        driver.Wait(3);
        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        if (date.equals(today))
            date = "Today " + date;
        else if(date.equals(tomorrow))
            date = "Tomorrow " + date;

        // Find Coordinates Event item in list
        String[] eventValue = {date, startTime, title};
        MobileElement eventItem = driver.findElement(OR.agenda.getLocatorDynamic("eventitem", eventValue));
        int X = eventItem.getLocation().getX(); //0
        int Y = eventItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        Y = Y + 20;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+256;
        int Y1 = Y+0;
        int[] swipeLeftToRightEvent = {X,Y,X1,Y1};
        // Action swipe left to right on Event
        Log.info("Swipe left to right on Event: " + date + " - " + startTime + " - " + title);
        driver.touchActionSwipe(swipeLeftToRightEvent);
        // Tap on Event icon
        Log.info("Clicking Event icon");
//        driver.findElement(OR.agenda.getLocator("icoEvent")).tap(1,0);
        //click Event icon by coordinate
        driver.tapCoordinate(30,Y);
        common.closeSendNotification(true);
        // Add simple Event
        addSimpleEvent(newTitle,newIsAllDay,ClickButton);
        // Send an email invite
        sendEmailInvite(clickButton,isIcs);
        Log.info("Ended addNewEventBySwipe keyword");
    }

    /**
     * Add new simple note by swiping on an event and click on Note icon
     * @param date
     * @param startTime
     * @param title
     * @param newNoteTitle - Input new title
     * @param content - Input Content
     * @param isDone - Clicking "Done" button
     */
    public void addNoteBySwipe(String date, String startTime, String title, String newNoteTitle, String content, boolean isDone){
        Log.info("Started addNoteBySwipe keyword");
        driver.Wait(5);
        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        if (date.equals(today))
            date = "Today " + date;
        else if(date.equals(tomorrow))
            date = "Tomorrow " + date;
        // Find Coordinates Event item in list
        String[] eventValue = {date, startTime, title};
        MobileElement eventItem = driver.findElement(OR.agenda.getLocatorDynamic("eventitem", eventValue));
        int X = eventItem.getLocation().getX(); //0
        int Y = eventItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        Y = Y + 20;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+256;
        int Y1 = Y+0;
        int[] swipeLeftToRightEvent = {X,Y,X1,Y1};
        // Action swipe left to right on Event
        Log.info("Swipe left to right on Event: " + date + " - " + startTime + " - " + title);
        driver.touchActionSwipe(swipeLeftToRightEvent);
        // Tap on Note icon
        Log.info("Clicking Note icon");
//        driver.findElement(OR.agenda.getLocator("icoNote")).tap(1,0);
        //click Note icon by coordinate
        driver.tapCoordinate(150,Y);
        if(!newNoteTitle.equals("")){
            // Create new Note item
            Log.info("Edit, input New Note's title");
            MobileElement TitleNote  =  driver.findElement(OR.note.getLocator("txbTitle"));
            // Clear all text in Title field
            int maxChars = TitleNote.getText().length();
            for (int i = 0; i < maxChars; i++)
                TitleNote.sendKeys(Keys.DELETE);
            // Input note title
            TitleNote.setValue(newNoteTitle);
        }
        MobileElement tbContent  =  driver.findElement(OR.note.getLocator("txaContent"));
        tbContent.click();
        tbContent.setValue(content);
        // Tap on "Done" button
        if (isDone == true)
            common.clickElementButton("Done");
        Log.info("Ended addNoteBySwipe keyword");
    }

    /**
     * Add new simple to.do by swiping on an event and click on To.do icon
     * @param date
     * @param startTime
     * @param title
     */
    public void addTodoBySwipe(String date, String startTime, String title){
        Log.info("Started addNewTodoBySwipe keyword");
        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        if (date.equals(today))
            date = "Today " + date;
        else if(date.equals(tomorrow))
            date = "Tomorrow " + date;
        // Find Coordinates Event item in list
        String[] eventValue = {date, startTime, title};
        MobileElement eventItem = driver.findElement(OR.agenda.getLocatorDynamic("eventitem", eventValue));
        int X = eventItem.getLocation().getX(); //0
        int Y = eventItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        Y = Y + 20;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+256;
        int Y1 = Y;
        int[] swipeLeftToRightEvent = {X,Y,X1,Y1};
        // Action swipe left to right on Event
        Log.info("Swipe left to right on Event: " + date + " - " + startTime + " - " + title);
        driver.touchActionSwipe(swipeLeftToRightEvent);
        // Tap on To.Do icon
        Log.info("Clicking ToDo icon");
//        driver.findElement(OR.agenda.getLocator("icoToDo")).tap(1,0);
        //click Note icon by coordinate
        driver.tapCoordinate(90,Y);
        Log.info("Ended addNewTodoBySwipe keyword");
    }

    /**
     * Delete event by swiping on an event and click on Trash icon
     * @param date
     * @param startTime
     * @param title
     * @param isOK true - click OK to delete/ false - no action
     */
    public void deleteEventBySwipe(String date, String startTime, String title, boolean isOK){
        Log.info("Started deleteEventBySwipe keyword");
        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        if (date.equals(today))
            date = "Today " + date;
        else if(date.equals(tomorrow))
            date = "Tomorrow " + date;

        // Find Coordinates Event item in list
        String[] eventValue = {date, startTime, title};
        MobileElement eventItem = driver.findElement(OR.agenda.getLocatorDynamic("eventitem", eventValue));
        int X = eventItem.getLocation().getX(); //0
        int Y = eventItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 200 pixel (X = -200, Y = 0)
        int X1 = X-200;
        int Y1 = Y+0;
        int[] swipeRightToLeftEvent = {X,Y,X1,Y1};
        // Action swipe right to left on Event
        Log.info("Swipe right to left on Event: " + date + " - " + startTime + " - " + title);
        driver.touchActionSwipe(swipeRightToLeftEvent);
        Log.info("Clicking Trash icon");
//        driver.findElement(OR.agenda.getLocator("icoTrash")).tap(1,0);
        // Click icon trash by coordinate
        driver.tapCoordinate(365, Y);
        // Click Ok button
        if (isOK == true)
            if(driver.isElementPresent(OR.common.getLocatorDynamic("btnElementButton", new String[]{"OK"})))
                common.clickElementButton("OK");
        Log.info("Ended deleteEventBySwipe keyword");
    }

    /**
     * Open move/add collection by swiping on an event and click on Collection icon
     * @param date
     * @param startTime
     * @param title
     */
    public void openCollectionBySwipe(String date, String startTime, String title){
        Log.info("Started openCollectionBySwipe keyword");
        driver.Wait(4);
        // Generate String of element based on date
        Date currentDate = new Date();
        String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        if (date.equals(today))
            date = "Today " + date;
        else if(date.equals(tomorrow))
            date = "Tomorrow " + date;

        // Find Coordinates Event item in list
        String[] eventValue = {date, startTime, title};
        MobileElement eventItem = driver.findElement(OR.agenda.getLocatorDynamic("eventitem", eventValue));
        int X = eventItem.getLocation().getX(); //0
        int Y = eventItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 160 pixel (X = -160, Y = 0)
        int X1 = X-160;
        int Y1 = Y;
        Log.info("Swipe Coodinates: "+ X + " " + Y + " " + X1 + " 0");
        int[] swipeRightToLeftEvent = {X,Y,X1,Y1};
        // Action swipe right to left on Event
        Log.info("Swipe right to left on Event: " + date + " - " + startTime + " - " + title);
        driver.touchActionSwipe(swipeRightToLeftEvent);
        Log.info("Clicking Collection icon");
//        driver.findElement(OR.agenda.getLocator("icoCollection")).tap(1,0);
        // Click collection icon by coordinate
        driver.tapCoordinate(290, Y);
        Log.info("Ended openCollectionBySwipe keyword");
    }

    /**
     * ToDo - Review later
     * When event is not AllDay - Select date time event by date picker for From & To
     * @param isFrom True - setup Start Time; False - Setup End Time
     * @param date (Ex: Apr 30)
     * @param hours (Input hour(s) for Event)
     * @param minutes (Input minutes for Event)
     * @param isMorning (AM/PM)
     */
    public void setEventDateTime(boolean isFrom, String quickOption, String date, int hours, int minutes, boolean isMorning){
        Log.info("Started setEventDateTime keyword");
        String hrs = Integer.toString(hours);
        String mins = Integer.toString(minutes);
        if (isFrom == true) {
            Log.info("Set Start Time of Event");
            driver.findElement(OR.agenda.getLocator("fromDate")).click();
            if(quickOption.equals("")){
                // Setting date of start
                Log.info("Date of Start: "+date);
                driver.findElement(OR.agenda.getLocator("pickerWheel_1")).setValue(date);
                // Setting Time of Start
                Log.info("Time of Start: "+hours+":"+minutes);
                // Setting hours
                driver.findElement(OR.agenda.getLocator("pickerWheel_2")).setValue(hrs);
                // Setting minutes
                driver.findElement(OR.agenda.getLocator("pickerWheel_3")).setValue(mins);
            }else {
                // Setting Time of Start
                Log.info("Time of Start: "+hours+":"+minutes);
                // Setting hours
                driver.findElement(OR.agenda.getLocator("pickerWheel_2")).setValue(hrs);
                // Setting minutes
                driver.findElement(OR.agenda.getLocator("pickerWheel_3")).setValue(mins);
                //Select quick date (Today, Tomorrow, 1 Week)
                Log.info("Set Date of Start for Event by choosing: "+quickOption);
                common.clickElementButton(quickOption);
            }
        } else {
            Log.info("Set End Time of Event");
            driver.findElement(OR.agenda.getLocator("toDate")).click();
            if(quickOption.equals("")){
                // Setting date of end
                Log.info("Date of End: "+date);
                driver.findElement(OR.agenda.getLocator("pickerWheel_1")).setValue(date);
                // Setting Time of End
                Log.info("Time of End: "+hours+":"+minutes);
                // Setting hours
                driver.findElement(OR.agenda.getLocator("pickerWheel_2")).setValue(hrs);
                // Setting minutes
                driver.findElement(OR.agenda.getLocator("pickerWheel_3")).setValue(mins);
            } else {
                // Setting Time of End
                Log.info("Time of End: "+hours+":"+minutes);
                // Setting hours
                driver.findElement(OR.agenda.getLocator("pickerWheel_2")).setValue(hrs);
                // Setting minutes
                driver.findElement(OR.agenda.getLocator("pickerWheel_3")).setValue(mins);
                //Select quick date (Today, Tomorrow, 1 Week)
                Log.info("Set Date of End for Event by choosing: "+quickOption);
                common.clickElementButton(quickOption);
            }
        }
        // Setting AM (Before midday) and PM (Past midday)
        try{
            // Check AM/PM in Date Picker
            driver.isElementExist(OR.agenda.getLocator("AmPmPicker"));
            if (isMorning == true) {
                Log.info("Setting before midday for Event");
                driver.findElement(OR.agenda.getLocator("AmPmPicker")).setValue("AM");
            }
            else {
                Log.info("Setting past midday for Event");
                driver.findElement(OR.agenda.getLocator("AmPmPicker")).setValue("PM");
            }
        }catch (Exception e){
            Log.info("Display Time of device as 24-Hour Time");
        }
        // Close Set Start Time Or End Time
        driver.findElement(OR.agenda.getLocator("fromDate")).click();
        Log.info("Ended setEventDateTime keyword");

    }

    /**
     * Swipe Down to Up on Calendar to show Full Month calendar
     */
    public void swipeUpDownCalendar(){
        Log.info("Started swipeUpDownCalendar keyword");
        //Swipe Up to Down on Calendar to show month calendar
        driver.touchActionSwipe(OR.agenda.getSwipeCoordinate("swipeUpDownCalendar"));
        Log.info("Ended swipeUpDownCalendar keyword");
    }

    /**
     * Swipe Down to Up on Calendar to show 2 weeks calendar
     */
    public void swipeDownUpCalendar(){
        Log.info("Started swipeDownUpCalendar keyword");
        //Swipe Down to Up on Calendar to show 2 weeks calendar
        driver.touchActionSwipe(OR.agenda.getSwipeCoordinate("swipeDownUpCalendar"));
        Log.info("Ended swipeDownUpCalendar keyword");
    }

    //ToDo skip
    public void selectDayOnCalendar(int numDay) {

    }

    /**
     * ToDo - Review later
     * When event is AllDay - Select date event by date picker for From & To
     * @param isFrom True - setup Start Date; False - Setup End Date
     * @param quickOption (Today, Tomorrow, 1 Week)
     * @param month (Input Month)
     * @param day (Input day)
     * @param year (Input Year)
     */
    public void setEventDate(boolean isFrom, String quickOption, String month, int day, int year){
        Log.info("Start setEventDate keyword");
        String Day = Integer.toString(day);
        String Year = Integer.toString(year);
        if (isFrom == true){
            Log.info("Set Start date of ALL DAY EVENT");
            driver.findElement(OR.agenda.getLocator("fromDate")).click();
            if(quickOption == ""){
                Log.info("Date of Start: " +month+" "+day+", "+year);
                // Setting Month of Start
                driver.findElement(OR.agenda.getLocator("pickerWheel_1")).setValue(month);
                // Setting day of Start
                driver.findElement(OR.agenda.getLocator("pickerWheel_2")).setValue(Day);
                // Setting year of Start
                driver.findElement(OR.agenda.getLocator("pickerWheel_3")).setValue(Year);
            } else {
                //Select quick date (Today, Tomorrow, 1 Week)
                Log.info("Set Start date of ADE by choosing "+quickOption+" option");
                common.clickElementButton(quickOption);
                }
            }
            else {
            Log.info("Set End date of ALL DAY EVENT");
            driver.findElement(OR.agenda.getLocator("toDate")).click();
            if (quickOption == ""){
                Log.info("Date of End: " +month+" "+day+", "+year);
                // Setting Month of Start
                driver.findElement(OR.agenda.getLocator("pickerWheel_1")).setValue(month);
                // Setting day of Start
                driver.findElement(OR.agenda.getLocator("pickerWheel_2")).setValue(Day);
                // Setting year of Start
                driver.findElement(OR.agenda.getLocator("pickerWheel_3")).setValue(Year);
            } else {
                //Select quick date (Today, Tomorrow, 1 Week)
                Log.info("Set End date of ADE by choosing "+quickOption+" option");
                common.clickElementButton(quickOption);
            }
        }
        // Close Set Start Time Or End Time
        driver.findElement(OR.agenda.getLocator("fromDate")).click();
        Log.info("Ended setEventDate keyword");
    }

    /**
     * ToDo - Review Later: updated
     * Input 1 or many invitees
     * @param invitees: list invite to input. It MUST have ‘comma’ for each item. Ex: a@gmail.com,b@gmail.com
     */
    public void inputInvitees(String invitees){
        Log.info("Started inputInvitees keyword");
        MobileElement Invitees = driver.findElement(OR.agenda.getLocator("txbInvitees"));
        if(invitees.contains(",")){
            Log.info("input Invitees: "+invitees);
            // Count length of String invitees
            int length = invitees.split(",").length;
            for(int i = 0; i < length; i++){
                String splitInvitees = invitees.split(",")[i];
                Invitees.setValue(splitInvitees+"\n");
            }
        }else {
            Log.info("input Invitees: "+invitees);
            Invitees.setValue(invitees+"\n");
        }
        Log.info("Ended inputInvitees keyword");
    }

    /**
     * ToDo - Review Later: updated
     * Add new event from event details and click on Event icon
     * @param newTitle Input New Event's title
     * @param newIsAllDay True: set All Day for Event
     * @param ClickButton Done: click Done/ Cancel: click Cancel/ None: No action
     */
    public void addEventByDetails(String newTitle, boolean newIsAllDay, String clickButton,boolean isIcs, String ClickButton){
        Log.info("Started addNewEventByDetails keyword");
        Log.info("Tap on Event icon");
        driver.findElement(OR.agenda.getLocator("icoEventDetails")).click();
        addSimpleEvent(newTitle,newIsAllDay,ClickButton);
        // Send an email invite
        sendEmailInvite(clickButton,isIcs);
        Log.info("Ended addNewEventByDetails keyword");
    }

    /**
     * ToDo - Review Later: Updated
     * Add new note from event details and click on Note icon
     * @param newNoteTitle Input New Note's title
     * @param content Input New Note's content
     * @param isDone True - Tap on "Done" button
     */
    public void addNoteByDetails(String newNoteTitle, String content, boolean isDone){
        Log.info("Started addNewNoteByDetails keyword");
        Log.info("Tap on Note icon");
        driver.findElement(OR.agenda.getLocator("icoNoteDetails")).click();
        // Edit, input New Note's title
        Log.info("Edit, input New Note's title");
        MobileElement TitleNote  =  driver.findElement(OR.note.getLocator("txbTitle"));
        // Delete all text in Title field
        int maxChars = TitleNote.getText().length();
        for (int i = 0; i < maxChars; i++)
        {
            TitleNote.sendKeys(Keys.DELETE);
        }
        TitleNote.setValue(newNoteTitle);
        MobileElement tbContent  =  driver.findElement(OR.note.getLocator("txaContent"));
        tbContent.click();
        tbContent.setValue(content);
        if (isDone == true){
            // Tap on "Done" button
            common.clickElementButton("Done");
        }
        Log.info("Ended addNewNoteByDetails keyword");
    }

    /**
     * ToDo - Review Later: updated
     * Add new to.do from event details and click on To.do icon
     */
    public void addTodoByDetails(){
        Log.info("Started addNewTodoByDetails keyword");
        Log.info("Tap on ToDo icon");
        driver.findElement(OR.agenda.getLocator("icoToDoDetails")).click();
        Log.info("Ended addNewTodoByDetails keyword");
    }

    /**
     * Todo review again
     * Check due To.Do item in agenda view
     * @param name
     * @param status Due, Overdue
     * @param dueDate Ex: Mon, 16 Apr
     * @param isCheckList
     * @param isStar
     * @param testcase
     */
    public boolean checkTodoItemView(String name, String status, String dueDate, boolean isCheckList, boolean isStar, boolean isLinked, String testcase){
        Log.info("Started checkTodoItemView keyword");
        // Current date
        Date currentDate = new Date();
        String dateCurrent = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        // Tomorrow
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        // Yesterday
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal1.getTime());
        boolean flag = true;
        // Due Today
        if(dueDate.equals(dateCurrent)){
            dueDate = "Today "+dateCurrent;
        }
        // Overdue Yesterday
        else if(dueDate.equals(yesterday)){
            dueDate = "Yesterday "+yesterday;
        }
        // Due Tomorrow
        else if(dueDate.equals(tomorrow)){
            dueDate = "Tomorrow "+tomorrow;
        }

        // Check Due To.do in To.Do's List
        String[] valueToDo = {name,status,dueDate};
        try{
            driver.isElementExist(OR.agenda.getLocatorDynamic("DueTodoItem",valueToDo));
            Log.info("Passed - Display Due ToDo: "+name+ " - Status: "+status+" - Due Date: "+dueDate);
        } catch (Exception e){
            Log.info("Failed - Not Found Due ToDo: "+name+ " - Status: "+status+" - Due Date: "+dueDate);
            flag = false;
        }

        // Check Due To.Do marked as Star or Not marked as Star
        if(isStar == true){
            try{
                driver.isElementExist(OR.agenda.getLocatorDynamic("icoStar",new String[]{name}));
                Log.info("Passed - Due ToDo: " +name+ " is marked as Star");
            } catch (Exception e){
                Log.info("Failed - Due ToDo: " +name+ " is not marked as star");
                flag = false;
            }
        } else {
            try{
                driver.isElementExist(OR.agenda.getLocatorDynamic("icoStar",new String[]{name}));
                Log.info("Failed - Due ToDo: " +name+ " is marked as Star");
                flag = false;
            } catch (Exception e){
                Log.info("Passed - Due ToDo: " +name+ " is not marked Star");
            }
        }
        // Fixed for view change
        if(dueDate.equals("Today "+dateCurrent)){
            driver.touchActionSwipe(300,165,80,166);
            driver.Wait(1);
            driver.touchActionSwipe(300,165,80,166);
            driver.Wait(1);
            driver.tapCoordinate(341,562);
        }
        // Check To.Do has checkList and Linked icon
        String linkedAndCheckList = "\uE076 \uE05D";
        String Linked = "\uE05D";
        String CheckList = "\uE076";
        String [] LinkedAndChecklist = {linkedAndCheckList,name};
        String [] Checklist = {CheckList,name};
        String [] linked = {Linked,name};
        if(isCheckList == true && isLinked == true){
            try {
                // Check Linked icon and checkList icon
                driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",LinkedAndChecklist));
                Log.info("Passed - Due ToDo: "+name+" has Checklist icon");
                Log.info("Passed - Due ToDo: "+name+" has Linked icon");
            }catch (Exception e){
                try {
                    // Check checkList icon
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                    Log.info("Passed - Due ToDo: "+name+" has Checklist icon");
                    Log.info("Failed - Due ToDo: "+name+" has no Linked icon");
                }catch (Exception e1){
                    // Check Linked icon
                    try{
                        driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",linked));
                        Log.info("Passed - Due ToDo: "+name+" has Linked icon");
                        Log.info("Failed - Due ToDo: "+name+" has no Checklist icon");
                    }catch (Exception e2){
                        Log.info("Failed - Due ToDo: "+name+" has no Checklist icon");
                        Log.info("Failed - Due ToDo: "+name+" has no Linked icon");
                    }
                }
                flag = false;
            }
            // Check To.Do only having Checklist icon
        }else if(isCheckList == true && isLinked == false){
            try {
                // Check checkList icon
                driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                Log.info("Passed - Due ToDo: "+name+" has Checklist icon");
                Log.info("Passed - Due ToDo: "+name+" has no Linked icon");
            }catch (Exception e) {
                try {
                    // Check Linked icon and checkList icon
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList", LinkedAndChecklist));
                    Log.info("Passed - Due ToDo: "+name+" has Checklist icon");
                    Log.info("Failed - Due ToDo: "+name+" has Linked icon");
                } catch (Exception e1) {
                    // Check Linked icon
                    try{
                        driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList", linked));
                        Log.info("Failed - Due ToDo: "+name+" has no Checklist icon");
                        Log.info("Failed - Due ToDo: "+name+" has Linked icon");
                    }catch (Exception e2){
                        Log.info("Failed - Due ToDo: "+name+" has no Checklist icon");
                        Log.info("Passed - Due ToDo: "+name+" has no Linked icon");
                    }
                }
                flag = false;
            }
            // Check To.Do only having Linked icon
        }else if(isCheckList == false && isLinked == true){
            try {
                // Check Linked icon
                driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",linked));
                Log.info("Passed - Due ToDo: "+name+" has Linked icon");
                Log.info("Passed - Due ToDo: "+name+" has no Checklist icon");
            }catch (Exception e){
                try {
                    // Check Linked icon and checkList icon
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",LinkedAndChecklist));
                    Log.info("Passed - Due ToDo: "+name+" has Linked icon");
                    Log.info("Failed - Due ToDo: "+name+" has Checklist icon");
                }catch (Exception e1){
                    // Check checkList icon
                    try{
                        driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                        Log.info("Failed - Due ToDo: "+name+" has Checklist icon");
                        Log.info("Failed - Due ToDo: "+name+" has no Linked icon");
                    }catch (Exception e2){
                        Log.info("Passed - Due ToDo: "+name+" has no Checklist icon");
                        Log.info("Failed - Due ToDo: "+name+" has no Linked icon");
                    }
                }
                flag = false;
            }
            // Check To.Do has no Linked and CheckList icon
        }else if(isCheckList == false && isLinked == false){
            try {
                driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",LinkedAndChecklist));
                Log.info("Failed - Due ToDo: "+name+" has Linked icon");
                Log.info("Failed - Due ToDo: "+name+" has Checklist icon");
                flag = false;
            }catch (Exception e){
                try {
                    // Check CheckList icon
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                    Log.info("Failed - Due ToDo: "+name+" has Checklist icon");
                    flag = false;
                }catch (Exception e1){
                    Log.info("Passed - Due ToDo: "+name+" has no Linked icon");
                }
                try {
                    // Check Linked icon
                    driver.isElementExist(OR.agenda.getLocatorDynamic("icoLinkAndCheckList",linked));
                    Log.info("Failed - Due ToDo: "+name+" has Linked icon");
                    flag = false;
                }catch (Exception e2){
                    Log.info("Passed - Due ToDo: "+name+" has no Checklist icon");
                }
            }
        }
        if(flag == false){
            driver.captureScreenShot(testcase);
        }

        Log.info("Ended checkTodoItemView keyword");
        return flag;
    }

    /**
     * ToDo reiview again
     * Open a Due to.do from event list on Agenda view
     * @param date (Due date of To.Do)
     * @param title
     */
    public void openDueTodoItem(String date, String title){
        Log.info("Started openDueTodoItem keyword");
        Date currentDate = new Date();
        String dateCurrent = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        // Tomorrow
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
        // Yesterday
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal1.getTime());
        // Check Due date in Today/Tomorrow/Yesterday
        if(date.equals(dateCurrent)){
            date = "Due Today "+dateCurrent;
        } else if(date.equals(tomorrow)){
            date = "Due Tomorrow "+tomorrow;
        } else if(date.equals(yesterday)){
            date = "Overdue Yesterday "+yesterday;
        } else {
            date = "Due "+date;
        }
        String DueToDo []={title,date};
        Log.info("Open ToDo "+title+" has Due date is "+date);
        driver.findElement(OR.agenda.getLocatorDynamic("todoInAgendaView",DueToDo)).click();
        common.closeSendNotification(true);
        Log.info("Ended openDueTodoItem keyword");
    }

    /**
     * Todo review again
     * Set due date for to.do by swiping on to.do and click on Due icon
     * @param title
     * @param dueDate Today, 3 days, 10 days, 30 days
     */
    public void setDueDateTodoBySwipe (String title, String dueDate){
        Log.info("Started setDueDateTodoBySwipe keyword");
        MobileElement todoItem = driver.findElement(OR.agenda.getLocatorDynamic("todoInAgendaView", new String[]{title}));
        int X = todoItem.getLocation().getX(); //0
        int Y = todoItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        Y = Y + 20;
        // Swipe right 260 pixel (X = 260, Y = 0)
        int X1 = X+260;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};
        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on ToDo: " + title);
        driver.touchActionSwipe(swipeLeftToRightToDo);
        // Tap on Due icon
        Log.info("Clicking Due icon");
//        driver.findElement(OR.agenda.getLocator("icoDue")).tap(1,0);
        //click Event icon by coordinate
        driver.tapCoordinate(30,Y);
        Log.info("Set a Due date for this ToDo: "+dueDate);
        if(dueDate.equals("3 days")){
            dueDate = "In 3 Days";
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
        } else if(dueDate.equals("10 days")){
            dueDate = "In 10 Days";
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
            driver.touchActionSwipe(175,600,190,65);
        } else if(dueDate.equals("30 days")){
            dueDate = "In 30 Days";
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
            driver.touchActionSwipe(300,165,80,166);
            driver.Wait(2);
            driver.touchActionSwipe(300,165,80,166);
            driver.touchActionSwipe(175,600,190,300);
        } else if(dueDate.equals("Today")){
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
//            common.clickElementButton("Today");
            // Click Today button by coordinate
            driver.tapCoordinate(341,562);
        } else{
            Log.info("Parameter input failed!!!");
        }
        driver.Wait(3);
        Log.info("Ended setDueDateTodoBySwipe keyword");
    }

    /**
     * Todo review again
     * Send an email by swiping on to.do and click on Email icon
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
    public void sendEmailTodoBySwipe(String title, String from, String to, String cc, String bcc, String subject, String content, String numImage, String option, String collections, boolean isSent){
        Log.info("Started sendEmailTodoBySwipe keyword");

        // Wait for item
        driver.Wait(3, OR.agenda.getLocatorDynamic("todoInAgendaView", new String[]{title}));
        MobileElement todoItem = driver.findElement(OR.agenda.getLocatorDynamic("todoInAgendaView", new String[]{title}));

        // Get location element
        int X = todoItem.getLocation().getX(); //0
        int Y = todoItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        Y = Y + 20;

        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+260;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};

        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on ToDo: " + title);
        driver.touchActionSwipe(swipeLeftToRightToDo);

        // Tap on Due icon
        Log.info("Clicking Email icon");
        //click Event icon by coordinate
        driver.tapCoordinate(90,Y);
        driver.Wait(5);

        // Swipe top to down to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,315);

        // Input info and send mail
        email.sendEmail(from, to, cc,bcc, subject, content, numImage, option, collections, isSent);
        Log.info("Ended sendEmailTodoBySwipe keyword");
    }

    /**
     * Todo review again
     * Add new simple note by swiping on to.do and click on Note icon
     * @param title
     * @param newNoteTitle
     * @param content
     * @param isDone
     */
    public void addNoteTodoBySwipe(String title, String newNoteTitle, String content, boolean isDone){
        Log.info("Started addNoteTodoBySwipe keyword");
        MobileElement todoItem = driver.findElement(OR.agenda.getLocatorDynamic("todoInAgendaView", new String[]{title}));
        int X = todoItem.getLocation().getX(); //0
        int Y = todoItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        Y = Y + 20;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+260;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};
        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on ToDo: " + title);
        driver.touchActionSwipe(swipeLeftToRightToDo);
        // Tap on Due icon
        Log.info("Clicking Note icon");
//        driver.findElement(OR.agenda.getLocator("icoNote")).tap(1,0);
        //click Event icon by coordinate
        driver.tapCoordinate(150,Y);
        // Edit, input New Note's title
        Log.info("Edit, input New Note's title");
        MobileElement TitleNote  =  driver.findElement(OR.note.getLocator("txbTitle"));
        // Delete all text in Title field
        int maxChars = TitleNote.getText().length();
        for (int i = 0; i < maxChars; i++)
        {
            TitleNote.sendKeys(Keys.DELETE);
        }
        TitleNote.setValue(newNoteTitle);
        MobileElement tbContent  =  driver.findElement(OR.note.getLocator("txaContent"));
        tbContent.click();
        tbContent.setValue(content);
        if (isDone == true){
            // Tap on "Done" button
            common.clickElementButton("Done");
        }
        Log.info("Ended addNoteTodoBySwipe keyword");
    }
    /**
     * Todo review again
     * Delete to.do by swiping on an to.do and click on Delete icon
     * @param title
     * @param isOK true - click Ok to delete/ false - no action
     */
    public void deleteTodoBySwipe(String title, boolean isOK){
        Log.info("Started deleteTodoBySwipe keyword");
        MobileElement todoItem = driver.findElement(OR.agenda.getLocatorDynamic("todoInAgendaView", new String[]{title}));
        int X = todoItem.getLocation().getX(); //0
        int Y = todoItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 370;
        Y = Y + 22;
        // Swipe left 160 pixel (X = -160, Y = 0)
        int X1 = X-160;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        // Action swipe right to left on To.Do
        Log.info("Swipe right to left on To.Do: " +title);
        driver.touchActionSwipe(swipeRightToLeftToDo);
        Log.info("Clicking Trash icon");
        //click Event icon by coordinate
        driver.tapCoordinate(365,Y);
        if(isOK == true)
            common.clickElementButton("OK");
        Log.info("Ended deleteTodoBySwipe keyword");
    }

    /**
     * Open move/add collection by swiping on an To.Do and click on Collection icon
     * @param title
     */
    public void openCollectionTodoBySwipe(String title){
        Log.info("Started openCollectionTodoBySwipe keyword");
        // Wait for item
        driver.Wait(3, OR.agenda.getLocatorDynamic("todoInAgendaView", new String[]{title}));
        MobileElement todoItem = driver.findElement(OR.agenda.getLocatorDynamic("todoInAgendaView", new String[]{title}));
        // Get location element
        int X = todoItem.getLocation().getX(); //0
        int Y = todoItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 370;
        Y = Y + 22;
        // Swipe left 160 pixel (X = -160, Y = 0)
        int X1 = X-160;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        // Action swipe right to left on To.Do
        Log.info("Swipe right to left on To.Do: " +title);
        driver.touchActionSwipe(swipeRightToLeftToDo);
        Log.info("Clicking Collection icon");
        //click Event icon by coordinate
        driver.tapCoordinate(290,Y);
        Log.info("Ended openCollectionTodoBySwipe keyword");
    }

    /**
     * Send email invite when creating event which has contact
     * @param clickButton: Send, Not Send, Cancel.
     * @param isIcs True: include ICS; False: Not include ICS
     */
    public void sendEmailInvite(String clickButton, boolean isIcs){
        Log.info("Started sendEmailInvite keyword");
        try{
            driver.Wait(3);
            driver.isElementExist(OR.agenda.getLocator("popupSendInvite"));
            Log.info("Show Send an email invite popup");
            String valueICS =  driver.findElement(OR.agenda.getLocator("tglIcs")).getAttribute("value");
            if(isIcs == valueICS.equals("1")){
                Log.info("Send an email invite include ICS = "+isIcs);
            }else {
                driver.findElement(OR.agenda.getLocator("tglIcs")).click();
                Log.info("Send an email invite include ICS = "+isIcs);
            }
            if(clickButton.equals("Send")){
                clickButton = "Send";
            }else if(clickButton.equals("Not Send")){
                clickButton = "Don't Send";
            }else {
                clickButton = "Cancel";
            }
            Log.info(clickButton+" an email invite");
            driver.findElement(OR.agenda.getLocatorDynamic("optSendInvite",new String[]{clickButton})).click();
        }catch (Exception e){
            Log.info("Send an email invite popup doesn't show");
        }
        Log.info("Ended sendEmailInvite keyword");
    }

    /**
     * ToDo review again
     * Check new Event view show default as setting and click Cancel to back Event view
     * For all params: """" - skip this check
     * @param eventDuration: 15 mins, 30 mins, 45 mins, 1 hr, 1hr 30 mins, 2 hrs, 3 hrs
     * @param allDayEventAlert: None, Date Of Start, 1 Day Before, 2 Days Before, 3 Days Before, 7 Days Before, 1 Month Before
     * @param normalEventAlert: None, Time Of Start, 15 mins , 30 mins, 1 hr
     * @param collection
     * @param testcase
     * @return
     */
    public boolean checkNewEventView(String eventDuration, String allDayEventAlert, String normalEventAlert, String collection, boolean isCancel, String testcase){
        Log.info("Started checkNewEventView keyword");
        boolean flag = true;
        common.closeSendNotification(true);
        driver.Wait(4,OR.agenda.getLocator("durationtimeStart"));
        if(!eventDuration.equals("")){
            try {
                String duringTime;
                // Get TimeStart and TimeEnd of Event
                String timeStart = driver.findElement(OR.agenda.getLocator("durationtimeStart")).getAttribute("value");
                String timeEnd = driver.findElement(OR.agenda.getLocator("durationtimeEnd")).getAttribute("value");
                // Situation : Event During from today to tomorrow
                if(timeEnd.contains("Tomorrow")){
                    timeEnd = driver.findElement(OR.agenda.getLocator("durationtimeEnd2")).getAttribute("value");
                }
                // Calculate to get during time
                String a1 = timeStart.substring(0,5);
                String a2 = timeStart.substring(5);
                timeStart = a1+" "+a2;
                a1 = timeEnd.substring(0,5);
                a2 = timeEnd.substring(5);
                timeEnd = a1+" "+a2;
                SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                Date d1 = null;
                Date d2 = null;
                d1 = format.parse(timeStart);
                d2 = format.parse(timeEnd);
                    //in milliseconds
                long diff = d2.getTime() - d1.getTime();
                if(diff<0){
                    diff = diff + 24*60*60*1000;
                }
                long diffMinutes = diff / (60 * 1000) % 60;
                String minute = String.valueOf(diffMinutes);
                long diffHours = diff / (60 * 60 * 1000) % 24;
                String hours = String.valueOf(diffHours);
                if(diffHours == 0 ){
                    duringTime = minute+" mins";
                } else if(diffHours == 1){
                    if(diffMinutes == 0){
                        duringTime = hours+" hr";
                    }else duringTime = hours+" hr "+minute+" mins";
                }else {
                    if(diffMinutes == 0){
                        duringTime = hours+" hrs";
                    }else duringTime = hours+" hr "+minute+" mins";
                }
               if(eventDuration.equals(duringTime))
                Log.info("Passed - Event Duration Time is: "+duringTime);
                else {
                   Log.info("Failed - Event Duration Time is: "+duringTime);
                   flag = false;
               }
            }catch(Exception e)
            {
                Log.info("Failed - checkNewEventView keyword Error - "+e.getMessage());
                flag = false;
            }
        }

        // Check Normal Event Alert
        if(!normalEventAlert.equals("")){
            if(normalEventAlert.equals("None")){
                try {
                    driver.isElementExist(OR.agenda.getLocator("alertEvent"));
                    String actualText = driver.findElement(OR.agenda.getLocator("alertEvent")).getAttribute("value");
                    Log.info("Failed - Event has Default Normal Event Alert is "+actualText);
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Event has None Default Normal Event Alert");
                }
            } else {
                switch (normalEventAlert){
                    case "Time Of Start":
                        normalEventAlert = "At Start";
                        break;
                    case "1 hr":
                        normalEventAlert = "60 mins";
                        break;
                }
                try {
                    String actualText = driver.findElement(OR.agenda.getLocator("alertEvent")).getAttribute("value");
                    Assert.assertEquals(actualText,normalEventAlert);
                    Log.info("Passed - Default Normal Event Alert is: "+ normalEventAlert);
                }catch (AssertionError aE){
                    Log.info("Failed - Default Normal Event Alert - "+aE.getMessage());
                    flag = false;
                }
            }
        }

        // Check All Event Alert
        if(!allDayEventAlert.equals("")){
            driver.findElement(OR.agenda.getLocator("tglAllDay")).click();
            if(allDayEventAlert.equals("None")){
                try {
                    driver.isElementExist(OR.agenda.getLocator("alertEvent"));
                    String actualText = driver.findElement(OR.agenda.getLocator("alertEvent")).getAttribute("value");
                    Log.info("Failed - Event has All Day Event Alert is "+actualText);
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Event has None All Day Event Alert");
                }
            } else {
                switch (allDayEventAlert){
                    case "Date Of Start":
                        allDayEventAlert = "At Start";
                        break;
                    case "1 Day Before":
                        allDayEventAlert = "1 Day";
                        break;
                    case "2 Days Before":
                        allDayEventAlert = "2 Days";
                        break;
                    case "3 Days Before":
                        allDayEventAlert = "3 Days";
                        break;
                    case "7 Days Before":
                        allDayEventAlert = "7 Days";
                        break;
                    case "1 Month Before":
                        allDayEventAlert = "1 Month";
                        break;
                }
                try {
                    String actualText = driver.findElement(OR.agenda.getLocator("alertEvent")).getAttribute("value");
                    Assert.assertEquals(actualText,allDayEventAlert);
                    Log.info("Passed - Default All Day Event Alert is: "+ allDayEventAlert);
                }catch (AssertionError aE){
                    Log.info("Failed - Default All Day Event Alert - "+aE.getMessage());
                    flag = false;
                }
            }
        }
        // Check Default Collection
        if(!collection.equals("")){
            String Collection = " "+collection+" ";
            try {
                String actualText = driver.findElement(OR.agenda.getLocator("defaultCollection")).getAttribute("value");
                Assert.assertEquals(actualText,Collection);
                Log.info("Passed - Event belong to Collection: "+collection);
            }catch (AssertionError aE){
                Log.info("Failed - Collection Error - "+aE.getMessage());
                flag = false;
            }
        }
        if(flag == false){
            driver.captureScreenShot(testcase);
        }
        if(isCancel == true){
            common.clickElementButton("Cancel");
        }
        return flag;
    }
}
