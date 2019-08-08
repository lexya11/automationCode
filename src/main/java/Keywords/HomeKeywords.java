package Keywords;

import Locators.*;
import Support.*;
import io.appium.java_client.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class HomeKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;

    static Logger Log = Logger.getLogger(HomeKeywords.class);

    public HomeKeywords(SetupServer driver){
        this.driver = driver;
        this.common = new CommonKeywords(driver);
    }

    /**
     * Check Recent Collections section displays on Home Screen
     * @param isDisplay (true: Collection display / false: Collection not display)
     * @param testcase
     * @return true/false
     */
    public boolean checkRecentCollection(boolean isDisplay, String testcase){
        Log.info("Started Check Recent Collection keyword" );
        boolean flag = true;
        try {
            driver.Wait(5);
            driver.isElementExist(OR.home.getLocator("secRecentCollection"));
            Log.info("Show Recent Collections on Home Screen" );
            if (isDisplay == false) {
                driver.captureScreenShot(testcase);
                flag = false;
            }
        } catch (Exception e) {
            Log.info("Not show Recent Collections on Home Screen");
            if (isDisplay == true) {
                driver.captureScreenShot(testcase);
                flag = false;
            }
        }return flag;
    }

    /**
     * Click on collection in Recent Collections list to open Collection view
     * @param collection
     */
    public void openRecentCollection(String collection){
        Log.info("Started openRecentCollection keyword > "+ collection);
        MobileElement recentCollection = driver.findElement(OR.home.getLocatorDynamic("lblNameOfCollection",new String[]{collection}));
        recentCollection.click();
    }

    /**
     * Check Recent Collections Screen is displayed
     * @param expectView (Detail of Collection screen is opening)
     * @param testCase
     * @return
     */
    public boolean checkRecentCollectionsScreen(String expectView, String testCase){
        Log.info("Started checkRecentCollectionsScreen keywords");
        boolean flag = true;
        MobileElement secNameofCollection = driver.findElement(OR.home.getLocatorDynamic("secNameofCollection",new String[]{expectView}));
        String NameofCollection = secNameofCollection.getAttribute("name");
        try {
            Assert.assertEquals(NameofCollection,expectView+"\ue009");
            Log.info("Recent Collections Screen - " +NameofCollection+ " is displayed");
        } catch (AssertionError aE){
            aE.getMessage();
            driver.captureScreenShot(testCase);
            flag = false;
        }
        return flag;
    }

    /**
     * Tap on PLUS icon to open GLOBAL popup
     * Click on an item in GLOBAL popup to open new item screen
     * @param item (Email,Event,To.Do,Contact,Note,Collection)
     */
    public void openNewScreenPlusIcon(String item) {
        Log.info("Started openNewScreenPlusIcon keyword > "+ item);
        Log.info("Click on Plus icon");
        driver.Wait(1);
        driver.tapCoordinate(OR.home.getCoordinate("icoPlus"));
        Log.info("Click on "+ item);

        MobileElement items = driver.findElement(OR.home.getLocatorDynamic("btnNewItem", new String[]{item}));
        items.click();
        // Keep New Email Screen
        if(item.contains("Email")){
            driver.Wait(3);
            driver.tapCoordinate(50,330);
        }
    }

    /**
     * Click top left button on each Screens
     * @param button (Home, Arrow, Settings)
     */
    public void clickTopLeftbutton (String button){
        Log.info("Started clickTopLeftbutton keyword > " + button);
        String btnTopLeft = "";
        switch (button){
            case "Home":
                btnTopLeft = "\uE052";
                break;
            case "Arrow":
                btnTopLeft = "\uE099";
                break;
            case "Settings":
                btnTopLeft = "\uE01A";
                break;
        }
        driver.Wait(2);
        if(button.equalsIgnoreCase("Settings")){
            Log.info("Click on Settings");
            driver.tapCoordinate(OR.home.getCoordinate("icoSettings"));
        }
        else {
            Log.info("Click top left icon");
            MobileElement topLeftbutton = driver.findElement(OR.home.getLocatorDynamic("btnTopLeft", new String[]{btnTopLeft}));
            topLeftbutton.click();
        }

    }


    /**
     * Long press view icon to open context menu
     * @param View (Email, Event, To.Do, Contact, Note, Collection)
     * @param
     */
    public void longPressBottomBar(String View){
        Log.info("Started longPressBottomBar keyword > "+ View);
        switch (View.toLowerCase()){
            case "email":
                View = "icon 1";
                break;
            case "event":
                View = "icon 2";
                break;
            case "todo":
                View = "icon 3";
                break;
            case "contact":
                View = "icon 4";
                break;
            case "note":
                View = "icon 5";
                break;
            case "collection":
                View = "icon 6";
                break;
        }
        // When dark bottom bar active
        try {
            driver.isElementExist(OR.common.getLocatorDynamic("btnViewBottom", new String[]{View}));
            Log.info("Dark bottom bar active");
        }
        // When light bottom bar active
        catch (Exception e){
            // Generate string view icon
            String splitView = View.split(" ")[1];
            View = "icon white "+ splitView;
            Log.info("Light bottom bar active");
        }
        // Click on view icon
        driver.longPressElement(OR.common.getLocatorDynamic("btnViewBottom",new String[]{View}));
        Log.info("Ended longPressBottomBar keyword");
    }

    /**
     * Swipe Left to Right to back to Home Screen
     */
    public void swipeBottomViewToBackHome(){
        Log.info("Started swipeBottomViewToBackHome keyword");
        driver.Wait(2);
        //Swipe left to right in Bottom bar
        driver.touchActionSwipe(OR.home.getSwipeCoordinate("swipeBottomLeftRight"));
    }

    /**
     * Check Home Screen
     * @param testCase
     * @return
     */
    public boolean checkHomeScreenDisplay(String testCase){
        Log.info("Started Check Home Screen keyword");
        boolean flag = true;
        try {
            driver.isElementExist(OR.home.getLocator("secClock"));
            Log.info("Show Home Screen");
        } catch (Exception e){
            e.getMessage();
            flag = false;
            Log.info("Not show Home Screen");
            driver.captureScreenShot(testCase);
        } return flag;
    }


    /**
     * Tap on clock section to open Create New Event Screen
     */
    public void openEventFromClock(){
        Log.info("Start openEventFromClock keyword");
        MobileElement secClock = driver.findElement(OR.home.getLocator("secClock"));
        secClock.click();
    }

    /**
     * Check Day on month, Short Weekday and Month on Clock in Home screen
     * @param testcase
     * @return boolean
     */
    public boolean checkClockonHome(String testcase){
        Log.info("Start checkClockonHome keyword");
        boolean flag = true;
        // Get value date on Clock
        MobileElement lblDay = driver.findElement(OR.home.getLocator("lblDay"));
        int dayValue = Integer.parseInt(lblDay.getAttribute("value"));

        MobileElement lblWeekday = driver.findElement(OR.home.getLocator("lblWeekDay"));
        String weekdayValue = lblWeekday.getAttribute("value");

        MobileElement lblMonth = driver.findElement(OR.home.getLocator("lblMonth"));
        String monthValue = lblMonth.getAttribute("value");

        MobileElement lblTop = driver.findElement(OR.home.getLocator("lblTop"));
        String timeAtTop= lblTop.getAttribute("value");

        MobileElement lblBottom = driver.findElement(OR.home.getLocator("lblBottom"));
        String timeAtBottom = lblBottom.getAttribute("value");

        // Get current date
        Date currentDate = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        // Convert to correct format
        String expectMonth = new SimpleDateFormat("MMM").format(currentDate);
        //String expectWeekday = driver.getShortWeekDay(date);
        String expectWeekday = new SimpleDateFormat("EEE").format(currentDate);
        int expectDay = LocalDateTime.now().getDayOfMonth();
        String time = driver.findElement(OR.home.getLocator("lblLocalHour")).getAttribute("name");
        String expectTimeTop ;
        String expectTimeBottom ;
        if(time.contains("AM")){
            expectTimeTop = "Noon";
            expectTimeBottom = "6 AM";
        }else {
            expectTimeTop = "Midnight";
            expectTimeBottom = "6 PM";
        }

        // Compare observe and expect
        try{
            Log.info("Current Day on Clock is: " + dayValue);
            Assert.assertEquals(dayValue, expectDay);
            Log.info("Week Day on Clock is: " + weekdayValue);
            Assert.assertEquals(weekdayValue, expectWeekday);
            Log.info("Month on Clock is: " + monthValue);
            Assert.assertEquals(monthValue, expectMonth);
            Log.info("Time on Top Clock is: " + timeAtTop);
            Assert.assertEquals(timeAtTop, expectTimeTop);
            Log.info("Time on Bottom Clock is: " + timeAtBottom);
            Assert.assertEquals(timeAtBottom, expectTimeBottom);
        } catch (AssertionError aE){
            Log.info("Failed - Assert Error: " + aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        return flag;
    }

    /**
     * Close Siri Notification
     * @param isAllow (true: Allow / false: Don’t Allow)
     */
    public void closeSiriNotification(boolean isAllow){
        //Todo
    }

    /**
     * Click top plus button on view to add new item
     */
    public void clickTopPlusButton(){
        Log.info("Started clickTopPlusButton keyword");
        // Clicking on "Plus" icon
        driver.Wait(5);
        String plusicon = "\uE066";
        common.clickStaticTextButton(plusicon);
        driver.Wait(3);
        Log.info("Ended clickTopPlusButton keyword");
    }

    /**
     * Click icon item is shown in screen after click plus button on top .Start in home view
     * @param testcase
     * @return true/false
     */
    public boolean checkItemInPopupGlobal(String testcase){
        Log.info("Started checkPopUpGlobal keyword");
        boolean flag = true;
        String[] iconName = new String[]{"Email", "Event", "ToDo", "Contact", "Note", "Collection"};
        Log.info("Click on Plus icon");
        driver.Wait(1);
        driver.tapCoordinate(OR.home.getCoordinate("icoPlus"));

        // loop to check all icons in popup
        for(int i=0; i<6; i++){
            try{
                driver.isElementExist(OR.home.getLocatorDynamic("btnNewItem",new String[]{iconName[i]}));
                Log.info("Passed - Icon "+ iconName[i] +" is available");
            }catch (Exception e){
                driver.captureScreenShot(testcase);
                Log.info("Failed - Icon "+ iconName[i] +" is not available");
                flag = false;
            }
        }

        // Touch out popup to close
        driver.tapCoordinate(277,44);
        Log.info("Ended checkPopUpGlobal keyword");
        return flag;
    }

    /**
     * Check recent collection is top in list
     * @param collection
     * @param testcase
     * @return boolean (true/false)
     */
    public boolean checkCollectionOnTop(String collection, String testcase){
        Log.info("Started checkCollectionOnTop keyword");
        driver.Wait(2);
        boolean flag = true;
        String collect = driver.findElement(OR.home.getLocator("txtCollectionOnTop")).getAttribute("value");
        try {
            Assert.assertEquals(collect, collection);
            Log.info("Passed - Collection '"+ collection +"' is on top in list");
        } catch (AssertionError aE) {
            Log.info("Failed - Assert Error: " + aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkCollectionOnTop keyword");
        return flag;
    }


}
