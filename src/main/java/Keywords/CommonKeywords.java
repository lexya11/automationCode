package Keywords;

import Locators.*;
import Support.*;
import io.appium.java_client.*;
import org.apache.log4j.Logger;
import org.testng.Assert;

import javax.swing.text.View;

public class CommonKeywords{
    SetupServer driver;
    ObjectRepository OR;


    static Logger Log = Logger.getLogger(CommonKeywords.class);

    public CommonKeywords(SetupServer driver){
        this.driver = driver;
    }

    /**
     * Click on XCUIElementTypeButton Button
     * @param button
     */
    public void clickElementButton(String button){
        Log.info("Started clickElementButton keyword > "+ button);
        MobileElement btnButton = driver.findElement(OR.common.getLocatorDynamic("btnElementButton", new String[]{button}));
        btnButton.click();
    }

    /**
     * Click on XCUIElementTypeStaticText Button
     * @param button
     */
    public void clickStaticTextButton(String button){
        Log.info("Started clickStaticTextButton keyword > "+ button);
        MobileElement btnButton = driver.findElement(OR.common.getLocatorDynamic("btnStaticButton", new String[]{button}));
        btnButton.click();
    }
    /**
     * Click on XCUIElementTypeTextField Button
     * @param button
     */
    public void clickElementTextField(String button){
        Log.info("Started clickElementButton keyword > "+ button);
        MobileElement btnButton = driver.findElement(OR.common.getLocatorDynamic("btnTextField", new String[]{button}));
        btnButton.click();
    }
    /**
     * Click on XCUIElementTypeMenuItem in context menu when long press on element
     * @param button
     */
    public void clickMenuItemTextButton(String button){
        Log.info("Started clickElementButton keyword > "+ button);
        MobileElement btnButton = driver.findElement(OR.common.getLocatorDynamic("btnMenuItemButton", new String[]{button}));
        btnButton.click();
    }

    /**
     * Swipe center screen Left to Right
     */
    public void swipeCenterLeftToRight(){
        Log.info("Started swipeCenterLeftToRight keyword");
        //Swipe left to right in Bottom bar
        driver.touchActionSwipe(OR.common.getSwipeCoordinate("swipeCenterLeftRight"));
    }

    /**
     * Swipe center screen Right to Left
     */
    public void swipeCenterRightToLeft(){
        Log.info("Started swipeCenterRightToLeft keyword");
        //Swipe left to right in Bottom bar
        driver.touchActionSwipe(OR.common.getSwipeCoordinate("swipeCenterRightLeft"));
    }

    /**
     * Swipe bottom screen Left to Right
     */
    public void swipeBottomLeftToRight(){
        Log.info("Started swipeBottomLeftToRight keyword");
        driver.Wait(1);
        //Swipe left to right in Bottom bar
        driver.touchActionSwipe(OR.common.getSwipeCoordinate("swipeBottomLeftRight"));
    }

    /**
     * Swipe bottom screen Right to Left
     */
    public void swipeBottomRightToLeft(){
        Log.info("Started swipeBottomRightToLeft keyword");
        driver.Wait(1);
        //Swipe left to right in Bottom bar
        driver.touchActionSwipe(OR.common.getSwipeCoordinate("swipeBottomRightLeft"));
    }


    /**
     * Select 1 or many collection to local filter of view
     * @param collections All Collections, All Events, All To.do, All Contacts, All Notes or  MUST have ‘comma’ for each item. Ex: General,Play,Home
     */
    public void selectLocalFilter(String collections){
        Log.info("Started selectLocalFilter keyword");

        // Click on local filter
        driver.findElement(OR.common.getLocator("localFilter")).click();
        // All items
//        if (collections.equalsIgnoreCase("All Events")||collections.equalsIgnoreCase("All Todo")||
//                collections.equalsIgnoreCase("All Contacts")||collections.equalsIgnoreCase("All Notes")){
//
//            if(collections.equalsIgnoreCase("All Todo"))
//                collections = "All ToDo's";
//
//            try {
//                driver.isElementExist(OR.common.getLocatorDynamic("itmCollectionTicked",new String[]{collections}));
//                Log.info(collections +" was selected");
//                // No action
//            }catch (Exception e){
//                Log.info("Select collection > "+ collections);
//
//                // Case "All Contacts" is not visible
//                if(collections.equalsIgnoreCase("All Contacts")){
//                    driver.findElement(OR.common.getLocator("itmAllContacts")).click();
//                }
//                else
//                    clickStaticTextButton(collections);
//            }
//        }
        // All Collections
        if (collections.equalsIgnoreCase("All Collections")){
            try {
                driver.isElementExist(OR.common.getLocatorDynamic("itmCollectionTicked",new String[]{collections}));
                Log.info(collections+" was selected");
                // No action
            }catch (Exception e){
                Log.info("Select collection > "+collections);
                driver.Wait(3);
                driver.findElement(5,OR.common.getLocatorDynamic("itmCollection",new String[]{collections})).click();
            }
        }

        // Select collections
        else{
            Log.info("Select collection > "+collections);

            int length = collections.split(",").length;
            try {
                driver.isElementExist(OR.common.getLocatorDynamic("itmCollectionTicked",new String[]{"All Collections"}));
                Log.info("Click All Collections");
                driver.findElement(5,OR.common.getLocatorDynamic("itmCollection",new String[]{"All Collections"})).click();
                for(int i = 0; i < length; i++){
                    String splitCollections = collections.split(",")[i];
                    Log.info("Select collection > "+splitCollections);
                    driver.Wait(3);
                    driver.findElement(5,OR.common.getLocatorDynamic("itmCollection",new String[]{splitCollections})).click();
                }
            }catch (Exception e){
                // Select many Collections in list
                Log.info("Remove all Collections");
                driver.findElement(5,OR.common.getLocatorDynamic("itmCollection",new String[]{"All Collections"})).click();
                driver.Wait(1);
                // Re-findElement b/c screen reload
                driver.findElement(5,OR.common.getLocatorDynamic("itmCollection",new String[]{"All Collections"})).click();
                for(int i = 0; i < length; i++){
                    String splitCollections = collections.split(",")[i];
                    Log.info("Select collection > "+splitCollections);
                    driver.Wait(2);
                    driver.findElement(5,OR.common.getLocatorDynamic("itmCollection",new String[]{splitCollections})).click();
                }
            }
        }
        // Close section local filter
        MobileElement iconPlus = driver.findElement(OR.common.getLocator("icoPlus"));
        int X = iconPlus.getLocation().getX();
        int Y = iconPlus.getLocation().getY();
        driver.tapCoordinate(X,Y);
        Log.info("Ended selectLocalFilter keyword");
    }

    /**
     * Check current local filter of any view
     * @param collections: input name of Collection to check. MUST have ‘comma’ for each item. Ex: General,Play,Home
     * @param testcase
     * @return
     */
    public boolean checkLocalFilter(String collections, String testcase){
        Log.info("Started checkLocalFilter keyword");
        String localfilter;
        boolean flag = true;

        // Check multi collections
        if(collections.contains(",")){
            // Generate string local filter
            localfilter = "     Flo - Multiple Collections";
            String txtLocalFilter = driver.findElement(OR.common.getLocator("localFilter")).getAttribute("value");
            try{
                Assert.assertEquals(txtLocalFilter,localfilter);
                Log.info("Passed - Collection in List view is > Multi collections");
            }catch (AssertionError aE){
                Log.info("Failed - Collection in List view isn't > Multi collections "+ aE.getMessage());
                flag = false;
            }

            // Open local filter view
            MobileElement localFilter = driver.findElement(OR.common.getLocator("localFilter"));
            localFilter.click();
            driver.Wait(2);

            // Check Collections
            int length = collections.split(",").length;
            for(int i = 0; i < length; i++){
                String collection = collections.split(",")[i];
                try {
                    driver.isElementExist(OR.common.getLocatorDynamic("itmCollectionTicked",new String[]{collection}));
                    Log.info("Passed - Collection in local is selected > "+ collection);
                }
                catch (Exception e){
                    Log.info("Failed - Collection in local isn't selected > "+ collection + e.getMessage());
                    flag = false;
                }
            }
            // Screenshot in local filter view when failed
            if(!flag) {
                driver.captureScreenShot(testcase);
            }
            // Tap to escape filter view
            driver.tapCoordinate(333,425);

        }

        // Check 1 collection
        else {
            // Generate string local filter
            localfilter = "     Flo - "+ collections;
            String txtLocalFilter = driver.findElement(OR.common.getLocator("localFilter")).getAttribute("value");
            try {
                Assert.assertEquals(txtLocalFilter, localfilter);
                Log.info("Passed - Collection in List View is > "+ collections);
            } catch (AssertionError aE) {
                Log.info("Failed - Collection in List View isn't > "+ aE.getMessage());
                flag = false;
                driver.captureScreenShot(testcase);
            }

            // Open local filter view
            MobileElement localFilter = driver.findElement(OR.common.getLocator("localFilter"));
            localFilter.click();

            try {
                driver.isElementExist(OR.common.getLocatorDynamic("itmCollectionTicked", new String[]{collections}));
                Log.info("Passed - Collection in local is selected > " + collections);
            } catch (Exception e) {
                Log.info("Failed - Collection in local isn't selected > " + collections);
                flag = false;

                // Screenshot in local filter view
                driver.captureScreenShot(testcase);
            }
            // Tap to escape filter view
            driver.tapCoordinate(333,425);
        }
        Log.info("Ended checkLocalFilter keyword");
        return flag;
    }

    /**
     * Check popup message when trashing an item
     * @param message
     * @param clickButton OK - trash item/ OK Not Show - trash and not show notification again/ Cancel - not trash
     * @param testcase
     * @return
     */
    public boolean checkTrashMessage(String message, String clickButton, String testcase){
        Log.info("Started checkDeleteMessage keyword");
        boolean flag = true;
        // Check message on popup
        try {
            String actualText1 = driver.findElement(OR.common.getLocator("lblMessage1")).getAttribute("value");
            Assert.assertEquals(actualText1, message);
            Log.info("Passed - Message shows: "+ actualText1);
        }catch (AssertionError aE){
            Log.info("Failed - Assert Error: " + aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }

        // Click button
        if(clickButton.equalsIgnoreCase("OK"))
            clickElementButton("OK");
        else if(clickButton.equalsIgnoreCase("OK Not Show"))
            clickElementButton("Ok. Don't show again.");
        else if(clickButton.equalsIgnoreCase("Cancel"))
            clickElementButton("Cancel");

        Log.info("Ended checkDeleteMessage keyword");
        return flag;
    }

    /**
     * Check static button exist and its location
     * @param button button to check Ex: Home, Plus, Compose
     * @param location = "" is skip check. Ex: Left, Right
     * @return
     */
    public boolean checkStaticButton(String button, String location, String testcase){
        Log.info("Started checkStaticButton keyword");
        boolean flag = true;
        // Coordinate of middle view
        int locationMiddle = 180;

        // Generate UTF string for button
        String icon = "";
        switch (button.toLowerCase()){
            case "home icon":
                icon = "\uE052";
                break;
            case "plus icon":
                icon = "\uE066";
                break;
            case "compose icon":
                icon = "\uE04E";
                break;
            case "settings icon":
                icon = "\uE01A";
                break;
        }
        // Check button exist
        try{
            driver.isElementExist(OR.common.getLocatorDynamic("btnStaticButton",new String[]{icon}));
            Log.info("Passed - Button "+ button + " is exist");
        }catch(Exception e){
            flag = false;
            Log.info("Failed - Button "+ button + " is not exist");
        }

        // Check location button
        if(!location.equalsIgnoreCase("")) {
            MobileElement btnElement = driver.findElement(OR.common.getLocatorDynamic("btnStaticButton", new String[]{icon}));
            // Get X coordinate of button
            int locationElement = btnElement.getLocation().getX();
            if (location.equals("Left")) {
                if (locationElement < locationMiddle) {
                    Log.info("Passed - " + button + " button is in left");
                } else {
                    flag = false;
                    Log.info("Failed - " + button + " button is NOT in left");
                }
            } else if (location.equals("Right")) {
                if (locationElement > locationMiddle) {
                    Log.info("Passed - " + button + " button is in right");
                } else {
                    Log.info("Failed - " + button + " button is NOT in right");
                    flag = false;
                }
            } else
                Log.info("Parameter input failed, please check again!!!");
        }
        if(!flag)
            driver.captureScreenShot(testcase);
        Log.info("Ended checkStaticButton keyword");
        return flag;
    }

    /**
     * Open add/move collection view from item details and add/move collection for an item
     * @param isOpen true - open view/ false - view is opened
     * @param option Add - add collection/ Move - Move collection
     * @param collections MUST have ‘comma’ for each items when add multi collection. Ex: General,Play,Home
     */
    public void addMoveToCollection(boolean isOpen, String option, String collections){
        Log.info("Started addMoveToCollection keyword");
        if(isOpen == true){
            // Close keyboard if it shows
            if(driver.isElementPresent(OR.common.getLocator("secKeyboard"))) {
                Log.info("Close keyboard");
                driver.tapCoordinate(8, 64);
            }
            else
                Log.info("Keyboard don't show");

            Log.info("Open Add/Move Collection view");
            driver.findElement(OR.common.getLocator("icoCollectionDetails")).click();
        }

        // Add collection
        if(option.equalsIgnoreCase("Add")){
            Log.info("Click tab > Add");
            driver.Wait(10, OR.common.getLocator("tabAdd"));
            driver.findElement(OR.common.getLocator("tabAdd")).click();
            driver.Wait(3);
            Log.info("Add to Collections > "+collections);
            //Count length collections
            int length = collections.split(",").length;
            for(int i = 0; i < length; i++){
                String splitCollections = collections.split(",")[i];
                try {
                    driver.isElementExist(OR.common.getLocatorDynamic("itmCollectionAddMoveTicked",new String[]{splitCollections}));
                    Log.info("No action - "+ splitCollections +" is added");
                }catch (Exception e){
                    Log.info("Add Collection > "+ splitCollections);
                    driver.Wait(3);
                    driver.findElement(5,OR.common.getLocatorDynamic("itmCollectionAddMove",new String[]{splitCollections})).click();

                    while(!driver.isElementPresent(OR.common.getLocatorDynamic("itmCollectionAddMoveTicked",new String[]{splitCollections}))){
                        driver.findElement(5,OR.common.getLocatorDynamic("itmCollectionAddMove",new String[]{splitCollections})).click();
                        Log.info("click Again, click first time failed");
                        driver.Wait(2);
                    }
                }
            }
            Log.info("Click Done on add/move view");
            driver.Wait(2);
            MobileElement btnButton = driver.findElement(OR.common.getLocatorDynamic("btnElementButton", new String[]{"Done"}));
            if(btnButton.isEnabled()){
                clickElementButton("Done");
            }else {
                clickElementButton("Cancel");
            }
        }
        // Move collection
        else if(option.equalsIgnoreCase("Move")){
            Log.info("Click tab > Move");
            driver.Wait(10, OR.common.getLocator("tabMove"));
            driver.findElement(OR.common.getLocator("tabMove")).click();
            // Move to collection
            Log.info("Move collection > "+collections);
            driver.findElement(5,OR.common.getLocatorDynamic("itmCollectionAddMove",new String[]{collections})).click();
        }
        Log.info("Ended addMoveToCollection keyword");
    }

    /**
     * Unselect default selected collection when add new items
     * @param isOpen
     * @param clickButton click Done button
     */
    public void unselectCollection(boolean isOpen, boolean clickButton) {
        Log.info("Started unselectCollection keyword");

        if(isOpen == true){
            // Check show keyboard in detail
            if(driver.isElementPresent(OR.common.getLocator("secKeyboard")))
                driver.tapCoordinate(8,64);

            Log.info("Open Add/Move Collection view");
            driver.findElement(OR.common.getLocator("icoCollectionDetails")).click();
        }

        // Click on tab Add
        driver.findElement(OR.common.getLocator("tabAdd")).click();

        Log.info("Unselect collection");
        if(driver.isElementPresent(OR.common.getLocator("icoSelected")))
            driver.findElement(OR.common.getLocator("icoSelected")).click();

        // Click "Done" button
        if(clickButton == true)
            clickElementButton("Done");
        Log.info("Ended unselectCollection keyword");
    }


    /**
     * Open search section and search
     * @param keyword
     * @param view (Email, To.Do, Contact, Note, Collection)
     */
    public void searchItem(String view, String keyword){
        Log.info("Started searchItem keyword");
        //Swipe up to down to show Search field
        driver.Wait(2);
        driver.touchActionSwipe(OR.common.getSwipeCoordinate("swipeUpDownItemView"));

        // Click to search textbox
        Log.info("Open search "+view+" field");
        switch (view){
            case "Email":
                view = "Search";
                break;
            case "ToDo":
                view = "Search ToDo's";
                break;
            case "Contact":
                view = "Search Contacts";
                break;
            case "Note":
                view = "Search Notes";
                break;
            case "Collection":
                view = "Search";
                break;
        }
        driver.findElement(OR.common.getLocatorDynamic("btnSearch",new String[]{view})).click();

        // Input keyword to search + "\n" = Enter
        Log.info("Search by keyword > " +keyword);
        driver.tapCoordinate(70,40);
        driver.findElement(OR.common.getLocatorDynamic("txtSearch",new String[]{view})).setValue(keyword + "\n");
        Log.info("Ended searchItem keyword");
    }

    /**
     * Trash an item from item details view and click on OK icon
     * @param isOK true - click OK to delete/ false - no action
     */
    public void trashItemByDetails(boolean isOK){
        Log.info("Started trashItemByDetails keyword");
        // Find Trash email icon to click (if not click Trash icon)
        try{
            driver.isElementExist(OR.common.getLocator("icoTrashInDetailEmail"));
            Log.info("Click Trash email icon");
            driver.findElement(OR.common.getLocator("icoTrashInDetailEmail")).click();
        }catch (Exception e){
            Log.info("Click Trash icon");
            driver.findElement(OR.common.getLocator("icoTrashInDetail")).click();
        }
        // Click Ok button
        if(isOK == true) {
            Log.info("Click OK button");
            clickElementButton("OK");
        }

        // Wait for delete
        driver.Wait(2);
        Log.info("Ended trashItemByDetails keyword");
    }

    /**
     * Open an item from Links section in detail of Object or from Search Result list
     * @param type: Email, Event, To.Do, DoneTodo, Contact, Note
     * @param title
     */
    public void openItem(String type, String title){
        Log.info("Started openItem keyword");
        String Type = type;

        switch (type.toLowerCase()){
            case "email":
                type = "\uE037";
                break;
            case "event":
                type = "\uE017";
                break;
            case "todo":
                type = "\uE094";
                break;
            case "done todo":
                type = "\uE084";
                break;
            case "note":
                type = "\uE081";
                break;
        }
        String [] item ={type,title};
        Log.info("Open item "+ Type +" > "+ title);

        // Open Contact item
        if (type.equals("Contact")){
            driver.Wait(5, OR.common.getLocatorDynamic("openContact",new String[]{title}));
            Log.info("Open Contact > "+ title);
            driver.findElement(5,OR.common.getLocatorDynamic("openContact",new String[]{title})).click();
        }
        // Open Email item
        else if(Type.equals("Email")){
            driver.Wait(5, OR.common.getLocatorDynamic("openEmail",new String[]{title}));

            // In Collection View
            if(driver.isElementPresent(OR.common.getLocatorDynamic("openEmail",new String[]{title}))){
                Log.info("Open Email > "+ title+" in Collection View");
                driver.findElement(5,OR.common.getLocatorDynamic("openEmail",new String[]{title})).click();
            }
            // In other view
            else {
                Log.info("Open Email > "+ title);
                driver.Wait(3);
                driver.findElement(5,OR.common.getLocatorDynamic("openItem",item)).click();
            }
        }
        // Open Other item
        else {
            driver.Wait(5, OR.common.getLocatorDynamic("openItem",item));
            Log.info("Open "+ Type +" > "+ title);
            driver.findElement(5,OR.common.getLocatorDynamic("openItem",item)).click();
        }
        Log.info("Ended openItem keyword");
    }

    /**
     * Check item exist or not exist in view (item is XCUIElementTypeStaticText)
     * @param name
     * @param isExist: Exist - check item exist / Not Exist - check item not exist
     * @param testcase
     */
    public boolean checkItemExist(String name, String isExist, String testcase){
        Log.info("Started checkItemExist keyword > "+ name);
        boolean flag = true;

        // Wait for app to search items
        driver.Wait(1);

        // If item exist
        try {
            driver.isElementExist(OR.common.getLocatorDynamic("itmTitleItem", new String[]{name}));
            if(isExist.equals("Not Exist")){
                Log.info("Failed - Item '"+name+"' is existed");
                flag = false;
                driver.captureScreenShot(testcase);
            }else if(isExist.equals("Exist")){
                Log.info("Passed - Item '"+name+"' is existed");
            }
        }
        // If item not exists
        catch(Exception e)
        {
            if(isExist.equals("Exist")){
                Log.info("Failed - Item '"+name+"' is not existed");
                flag = false;
                driver.captureScreenShot(testcase);
            }else if(isExist.equals("Not Exist")){
                Log.info("Passed - Item '"+name+"' is not existed");
            }
        }
        Log.info("Ended checkItemExist keyword");
        return flag;
    }

    /**
     * Check button exist or not exist (button is XCUIElementTypeStaticText)
     * @param name
     * @param isExist: Exist - check button exist / Not Exist - check button not exist
     * @param testcase
     */
    public boolean checkButtonExist(String name, String isExist, String testcase){
        Log.info("Started checkButtonExist keyword > "+ name);
        boolean flag = true;
        // Check item exist
        try {
            driver.isElementExist(OR.common.getLocatorDynamic("btnElementButton", new String[]{name}));
            if(isExist.equals("Not Exist")){
                Log.info("Failed - Button '"+name+"' is existed");
                flag = false;
                driver.captureScreenShot(testcase);
            }else if(isExist.equals("Exist")){
                Log.info("Passed - Button '"+name+"' is existed");
            }
        }
        catch(Exception e)
        {
            if(isExist.equals("Exist")){
                Log.info("Failed - Button '"+name+"' is not existed");
                flag = false;
                driver.captureScreenShot(testcase);
            }else if(isExist.equals("Not Exist")){
                Log.info("Passed - Button '"+name+"' is not existed");
            }
        }
        Log.info("Ended checkButtonExist keyword");
        return flag;
    }

    /**
     * Search and link item by type and title in details item view
     * @param type
     * @param title
     */
    public void addLinkedItemInDetail(String type, String title){
        Log.info("Started addLinkedItemInDetail keyword");
        // Open Link View from detail
        clickStaticTextButton("\uE05D");
        Log.info("Search things to link has title: "+title);
        if(title.contains(" ")){
            String splitCollections = title.split(" ")[0];
            driver.findElement(OR.common.getLocator("searchItemLinked")).setValue(splitCollections+"\n");
        }else {
            driver.findElement(OR.common.getLocator("searchItemLinked")).setValue(title+"\n");
        }
        driver.Wait(5);
        Log.info("Select "+type+" has name: "+title+" to link");
        // Convert type
        String Type = type;
        switch (type){
            case "Email":
                type = "\uE037";
                break;
            case "Event":
                type = "\uE017";
                break;
            case "Note":
                type = "\uE081";
                break;
            case "ToDo":
                type = "\uE094";
                break;
            case "DoneTodo":
                type = "\uE084";
                break;
        }
        String[]item = {type,title};
        // Select Contact
        if (type.equals("Contact")){
            try {
                driver.isElementExist(OR.common.getLocatorDynamic("contactInLinkView",new String[]{title}));
                driver.findElement(OR.common.getLocatorDynamic("contactInLinkView",new String[]{title})).click();
            }catch (Exception e){
                Log.info("Not found Contact has name "+title+" to link");
            }
        }
        // select Email
        else if(Type.equals("Email")){
            try {
                driver.isElementExist(OR.common.getLocatorDynamic("emailInLinkView",new String[]{title}));
                driver.findElement(OR.common.getLocatorDynamic("emailInLinkView",new String[]{title})).click();
            }catch (Exception e){
                Log.info("Not found Email has Subject "+title+" to link");
            }
        }
        else {
            try {
                driver.isElementExist(OR.common.getLocatorDynamic("itemInLinkView",item));
                MobileElement Item = driver.findElement(OR.common.getLocatorDynamic("itemInLinkView",item));
                Item.click();
            }catch (Exception e){
                Log.info("Not found "+Type+" has title "+title+" to link");
            }
        }
        clickElementButton("Done");
        Log.info("Ended addLinkedItemInDetail keyword");
    }

    /**
     * Close Search Item Screen
     */
    public void closeSearchItemScreen(){
        Log.info("Started closeSearchItemScreen keyword");
        driver.isElementExist(OR.common.getLocatorDynamic("btnElementButton",new String[]{"Cancel"}));
        MobileElement Item = driver.findElement(OR.common.getLocatorDynamic("btnElementButton",new String[]{"Cancel"}));
        Item.click();
        Log.info("Ended closeSearchItemScreen keyword");
    }

    /**
     * Expand collection to show child collection
     * @param item
     */
    public void expandItem(String item){
        Log.info("Started expandItem keyword");
//        driver.Wait(1);
        if(item.contains(",")){
            // Count length of String Collection
            int length = item.split(",").length;
            for(int i = 0; i < length; i++){
                String splitItem = item.split(",")[i];
                try {
                    if(!driver.isElementPresent(OR.common.getLocatorDynamic("btnLowerItem", new String[]{splitItem})))
                    driver.findElement(OR.common.getLocatorDynamic("btnExpandItem", new String[]{splitItem})).click();
                }catch (AssertionError aE){
                    Log.info("Failed - Can not find"+ splitItem);
                }
            }
        }
        // Check one  Item
        else {
            if(!driver.isElementPresent(OR.common.getLocatorDynamic("btnLowerItem", new String[]{item})))
                driver.findElement(OR.common.getLocatorDynamic("btnExpandItem", new String[]{item})).click();
        }
//        driver.Wait(2);
        Log.info("Ended expandCollection keyword");
    }

    /**
     * Check Enable status of button btnExistingUsers & btnNewUser_SignUp
     * @param Element
     * @param testcase
     * @return true/false
     */
    public boolean checkButtonEnable(String Element, String testcase){
        Log.info("Started Check Button Enable keyword");
        boolean flag = true;
        try{
            MobileElement element = driver.findElement(OR.signin.getLocator(Element));
            if (element.isEnabled())
                Log.info("Passed - " + Element + " is enable");
        }catch (Exception e) {
            Log.error("Failed - Check Error: " + e);
            flag = false;
            driver.captureScreenShot(testcase);
        }
        return flag;
    }

    /**
     * Check searched item in the search result list
     * @param type To.Do, Done To.do, Note
     * @param name
     * @param option  Exist - check item exist / Not Exist - check item not exist
     * @param clickCancel
     * @param testcase
     * @return
     */
    public boolean checkSearchResults(String type, String name, String option, boolean clickCancel, String testcase){
        Log.info("Started checkSearchResults keyword");
        boolean flag = true;
        if(driver.isElementPresent(OR.common.getLocator("itmNoResult"))){
            if(option.equals("Not Exist"))
                Log.info("Passed "+type+" item '"+name+"' were not found");
            else if(option.equals("Exist")) {
                flag = false;
                driver.captureScreenShot(testcase);
                Log.info("Failed - No results were found");
            }
        }else {
            String Type = type;
            switch (type) {
                case "Note":
                    type = "\uE081";
                    break;
                case "ToDo":
                    type = "\uE094";
                    break;
                case "Done Todo":
                    type = "\uE084";
                    break;
            }
            String[] itmResult = {type, name};
            if(driver.isElementPresent(OR.common.getLocatorDynamic("itmSearchResult", itmResult))){
                if(option.equals("Exist"))
                    Log.info("Passed - "+Type+" item '"+name+"' were found");
                else if(option.equals("Not Exist")) {
                    flag = false;
                    driver.captureScreenShot(testcase);
                    Log.info("Failed - "+Type+" item '"+name+"' were found");
                }
            }
        }
        // Click Cancel button
        if(clickCancel == true)
            clickElementButton("Cancel");
        Log.info("Ended checkSearchResults keyword");
        return flag;
    }

    /**
     * Click Star Icon Of Item (Only to.do and note)
     * @param title
     * @param status (Staxr/UnStar)
     */
    public void clickStarItem(String title, String status){
        Log.info("Started clickStarItem keyword");
        Log.info("Mark "+ status +" Item > "+ title);
        if(status.equalsIgnoreCase("UnStar"))
        {
            // Check to click star icon in other view
            MobileElement iconStar = driver.findElement(OR.common.getLocatorDynamic("icoStar",new String[]{title}));

            if(iconStar.isDisplayed())
                driver.findElement(OR.common.getLocatorDynamic("icoStar",new String[]{title})).click();
            else
                driver.findElement(OR.common.getLocatorDynamic("icoStarNote",new String[]{title})).click();
        }
        else if(status.equalsIgnoreCase("Star")){
            // Check to click Unstar icon in other view
            MobileElement iconUnStar = driver.findElement(OR.common.getLocatorDynamic("icoUnStar",new String[]{title}));

            if(iconUnStar.isDisplayed()){
                driver.findElement(OR.common.getLocatorDynamic("icoUnStar",new String[]{title})).click();
            } else
                driver.findElement(OR.common.getLocatorDynamic("icoUnStarNote",new String[]{title})).click();
        }
        Log.info("Ended clickStarItem keyword");
    }


    /**
     * Click Star Icon Is Visible in Item List (Using for Setting "Show Stars in List View" )
     * @param name item name
     * @param status true/false (true: visible, false: invisible)
     * @param testCase
     */
    public boolean checkStarIsVisible(String name, boolean status, String testCase) {
        Log.info("Started checkStarIsVisible keyword");
//        driver.Wait(4);
        boolean flag ;
        String check = driver.findElement(OR.common.getLocatorDynamic("icoUnStar", new String[]{name})).getAttribute("visible");
        if (status == true) {
            if (check.equals("true")) {
                Log.info("Passed - Star is visible in List View");
                flag = true;
            } else {
                Log.info("Failed - Star is not visible in List View");
                flag = false;
                driver.captureScreenShot(testCase);
            }
        } else {
            if (check.equals("true")) {
                Log.info("Failed - Star is visible in List View");
                flag = false;
                driver.captureScreenShot(testCase);
            } else {
                Log.info("Passed - Star is not visible in List View");
                flag = true;
            }
        }
        Log.info("Ended checkStarIsVisible keyword");
        return flag;
    }

    /**
     * Tap and hold on item to open multi-select mode
     * @param items: 1 or multi items, have ‘comma’ for each item. Ex: "abc,xyz,..."
     * @param isSelectAll: true - select all items / false - select some items in lists
     * @param options: Collect - Open Add/Move collection view / Trash - Delete all selected items / Cancel - Cancel action
     */
    public void multiSelectMode(String items, boolean isSelectAll, String options){
        Log.info("Started multiSelectMode keyword");
        // Split string items to click
        String selectedItem;
        if(items.contains(","))
            // Get 1st item
            selectedItem = items.split(",")[0];
        else
            selectedItem = items;

        // Long press to go multi mode
        Log.info("Long press on '"+ selectedItem +"' to Multi Mode");
        if(!driver.isElementPresent(OR.common.getLocatorDynamic("itmTitleItem", new String[]{selectedItem}))) {
            // Wait for view load all items
            driver.Wait(10);
            // long press or throw exception
            driver.longPressElement(OR.common.getLocatorDynamic("itmTitleItem", new String[]{selectedItem}));
        }
        else
            driver.longPressElement(OR.common.getLocatorDynamic("itmTitleItem", new String[]{selectedItem}));

        // Select all items
        if(isSelectAll == true){
            Log.info("Click Select all button");
            //Click 2 to "Select All" button coordinate to take view select longer
            driver.tapCoordinate(325,40);
            driver.tapCoordinate(325,40);
            if(driver.isElementPresent(OR.common.getLocator("btnSelectAll"))){
                driver.findElement(OR.common.getLocator("btnSelectAll")).click();
//                driver.tapCoordinate(325,40);
            }
//            driver.tapCoordinate(325,25);
        }

        // Select some items in lists
        else {
            int length = items.split(",").length;
            // i=1 - not select again selected item in longPress action
            for (int i = 1; i < length; i++) {
                String splitItems = items.split(",")[i];
                Log.info("Select items: " + splitItems);
                driver.findElement(OR.common.getLocatorDynamic("itmItemNotSelected", new String[]{splitItems})).click();
            }
        }

        // Click option icons to do action
        if(options.equalsIgnoreCase("Cancel")){
            Log.info("Cancel action");
            clickElementButton("Cancel");
        }
        else {
            Log.info("Select " + options + " option");
            driver.findElement(OR.common.getLocatorDynamic("txtOptions", new String[]{options})).click();

            if(options.equalsIgnoreCase("Trash")) {
                clickElementButton("OK");
                // Wait for delete all items
                driver.Wait(3);
            }
        }
        Log.info("Ended multiSelectMode keyword");
    }

    /**
     * unlink Item From Detail View
     * @param type (Email, Note, Event, Contact, To.do )
     * @param title
     */
    public void unlinkItem(String type, String title){
        Log.info("Started unlinkItem keyword");
        MobileElement item;
        String Type = type;
//        driver.Wait(4);
        switch (type){
            case "Email":
                type = "\uE037";
                break;
            case "Event":
                type = "\uE017";
                break;
            case "Note":
                type = "\uE081";
                break;
            case "ToDo":
                type = "\uE094";
                break;
            case "DoneTodo":
                type = "\uE084";
                break;
        }
        String[] itmResult = {type, title};
        if(Type.contains("Contact")){
            if(driver.isElementPresent(OR.common.getLocatorDynamic("txbLinkContact", new String[]{title}))){
                item = driver.findElement(OR.common.getLocatorDynamic("txbLinkContact", new String[]{title}));
                Log.info(Type+" item '"+title+"' were found");
            }else {
                Log.info(Type+" item '"+title+"' were not found");
                return;
            }
        }
        else {
            if(driver.isElementPresent(OR.common.getLocatorDynamic("txbLink", itmResult))){
                Log.info(Type+" item '"+title+"' were found");
                item = driver.findElement(OR.common.getLocatorDynamic("txbLink", itmResult));
            }else {
                Log.info(Type+" item '"+title+"' were not found");
                return;
            }
        }
        int X = item.getLocation().getX(); //0
        int Y = item.getLocation().getY();
        X = X + 300;
        Y = Y + 15;
        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        // Action swipe right to left on Item
        Log.info("Swipe right to left on Item: "+title);
        driver.touchActionSwipe(swipeRightToLeftToDo);
        Log.info("Clicking Unlink Button");
        MobileElement btnButton = driver.findElement(OR.common.getLocatorDynamic("btnElementButton", new String[]{"Unlink"}));
        btnButton.click();
        Log.info("Ended unlinkItem keyword");
    }

    /**
     * Check View Screen is displayed by get name of Navigation Bar
     * @param viewScreen Ex: Email,Event,To.Do,Contact,Note,Collection, New Email, New To.Do..
     * @param testCase
     * @return
     */
    public boolean checkViewScreenDisplay(String viewScreen, String testCase){
        Log.info("Started checkViewScreenDisplay keyword > "+ viewScreen);
        boolean flag = true;
        MobileElement NavigationBars = driver.findElement(OR.common.getLocator("secNavigationBar"));
        String ViewScreen = NavigationBars.getAttribute("name");
        String viewActual = "";
        if(viewScreen.contains("ToDo") || viewScreen.equals("Contact")){
            viewActual = viewScreen+"View";
        }else if(viewScreen.equals("Note") || viewScreen.equals("Collection")){
            viewActual = "More";
        }else if(viewScreen.equals("Email")){
            viewActual = "Emails";
        }else{
            viewActual = ViewScreen;
        }
        try{
            // Keep New Email Screen
            if(viewActual.contains("Email")){
                driver.Wait(3);
                driver.tapCoordinate(50,330);
            }
            Assert.assertEquals(ViewScreen,viewActual);
            Log.info("Passed - " + viewScreen + " View is displayed");
        } catch (AssertionError e){
            Log.info("Failed - " + viewScreen + " View is not displayed - " +e.getMessage());
            flag = false;
            driver.captureScreenShot(testCase);
        } return flag;
    }

    /**
     * Close Collection Introduce screen if it displays
     */
    public void closeCollectionIntro(){
        Log.info("Started closeCollectionIntro keyword");
        try{
            driver.Wait(2);
            driver.isElementExist(OR.common.getLocator("imgCollectionIntro"));
            Log.info("Collection Introduce shows");
            clickElementButton("Close");
        }catch (Exception e){
            Log.info("Collection Introduce doesn't show");
        }
    }

    /**
     * Ignore Connect Your Account popup
     */
    public void ignoreConnectAcountPopup(){
        Log.info("Started ignoreConnectAcountPopup keyword");
        try{
            driver.Wait(5);
            driver.isElementExist(OR.common.getLocator("connectAccount"));
            Log.info("Connect your account popup shows");
            driver.tapCoordinate(185,532);
        }catch (Exception e){
            Log.info("Connect your account popup doesn't show");
        }
        Log.info("Ended ignoreConnectAcountPopup keyword");
    }

    /**
     * This case only happens with 3rd party account which has been already added needs to be verified
     * Close Account Notification
     * @param isGoAccount (true: Go to Account / false: Don’t go to Account)
     */
    public void closeAccountNotification(boolean isGoAccount){
        driver.Wait(2);
        Log.info("Started closeAccountNotification keyword");
        try {
            driver.isElementExist(OR.common.getLocator("popupAccNotification"));
            Log.info("Account Notification popup shows");
            if(isGoAccount == true)
                clickElementButton("Accounts");
            else {
                clickElementButton("Later");
            }
        }
        catch (Exception e) {
            Log.info("Account Notification popup doesn't show");
        }
    }

    /**
     * Close Connect your account popup if it displays
     */
    public void closeConnectAccount(){
        Log.info("Started closeConnectAccount keyword");
        try{
            driver.Wait(2);
            driver.isElementExist(OR.common.getLocator("lblHeader"));
            Log.info("Connect your account popup shows");
            MobileElement addLater = driver.findElement(OR.common.getLocator("btnAddLater"));
            addLater.click();
        }catch (Exception e){
            Log.info("Connect your account popup doesn't show");
        }
    }

    /**
     * Close Send Notification if it shows
     * @param isAllow (true: Allow / false: Don’t Allow)
     */
    public void closeSendNotification(boolean isAllow){
        Log.info("Started closeSendNotification keyword");
        try{
            driver.Wait(5);
            driver.isElementExist(OR.common.getLocator("popupNotification"));
            Log.info("Send Notification popup shows");

            // Click allow / not allow button
            if(isAllow == true)
                clickElementButton("Allow");
            else {
                clickElementButton("Don’t Allow");
            }
            // Wait for close popup
            driver.Wait(1);
        }catch (Exception e){
            Log.info("Send Notification popup doesn't show");
        }
    }

    /**
     * Click view icon to open View in Bottom bar
     * @param View (Email, Event, To.Do, Contact, Note, Collection)
     * @param
     */
    public void clickViewBottomBar(String View){
        Log.info("Started clickViewBottomBar keyword > "+ View);
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
            // Generate string view icon when Light Header and Footer active
            String splitView = View.split(" ")[1];
            View = "icon white "+ splitView;
            Log.info("Light bottom bar active");
        }

        // Click view icon
        MobileElement btnViewBottom = driver.findElement(OR.common.getLocatorDynamic("btnViewBottom",new String[]{View}));
        btnViewBottom.click();

        // Close collection intro for collection view
        if(View.contains("6")){
            closeCollectionIntro();
        }
        // Close account popup for email/event view
        if(View.contains("1") || View.contains("2")){
            ignoreConnectAcountPopup();
        }
        // Close account popup for to.do/event view
        if(View.contains("2") || View.contains("3")){
            closeSendNotification(true);
        }
        Log.info("Ended clickViewBottomBar keyword");
    }

    /**
     * Check Icon In Bottom Bar
     * @param testcase
     * @return boolean (true/false)
     */
    public boolean checkIconInBottomBar(boolean isLight, String testcase){
        Log.info("Started checkIconInBottomBar keyword ");
        driver.Wait(1);
        boolean flag = true;
        String[] a = new String[]{"Email","Event","ToDo","Contact","Note","Collection"};
        if(isLight == false){
            for(int i = 0 ; i < 6; i++){
                try {
                    driver.isElementExist(OR.common.getLocatorDynamic("btnViewBottom", new String[]{"icon "+(i+1)}));
                    Log.info("Passed - Icon "+a[i]+" is available");
                }catch (Exception e){
                    Log.info("Failed - Icon "+a[i]+  " is not available");
                    driver.captureScreenShot(testcase);
                    flag = false;
                }
            }
        }else {
            for(int i = 0 ; i < 6; i++){
                try {
                    driver.isElementExist(OR.common.getLocatorDynamic("btnViewBottom", new String[]{"icon white "+(i+1)}));
                    Log.info("Passed - Icon "+a[i]+" is available");
                }catch (Exception e){
                    Log.info("Failed - Icon "+a[i]+  " is not available");
                    driver.captureScreenShot(testcase);
                    flag = false;
                }
            }
        }
        Log.info("Ended checkIconInBottomBar keyword");
        return flag;
    }

    /**
     * Close Start Flo popup
     * @return
     */
    public void closeStartFlo(){
        Log.info("Started closeStartFlo keyword");
        // Wait for app login
        driver.Wait(10, OR.common.getLocator("btnOkGotIt"));

        // Click button Start Flo
        MobileElement btnOkGotIt = driver.findElement(OR.common.getLocator("btnOkGotIt"));
        Log.info("Close Start Flo popup");
        btnOkGotIt.click();
        Log.info("Ended closeStartFlo keyword");
    }

    /**
     * Check Start Flo popup - Logo, Tips guide, button Start Flo
     * @param sloganExpect (Slogan of Start Flo popup)
     * @param tipExpect1 (Tip guide to use Flo app - 1)
     * @param tipExpect2 (Tip guide to use Flo app - 2)
     * @param testcase
     * @return
     */
    public boolean checkStartFlo (String sloganExpect, String tipExpect1, String tipExpect2, String testcase){
        Log.info("Started Check Start Flo popup keyword");
        driver.Wait(4);
        boolean flag = true;
        // Check logo exist
        try {
            driver.isElementExist(OR.common.getLocator("lgoFlo"));
            Log.info("Passed - Show Logo Flo correctly");
        }catch (Exception e){
            Log.info("Failed - Not show Logo Flo");
            flag = false;
            driver.captureScreenShot(testcase);
        }
        // Check correct text on popup
        String slogan = driver.findElement(OR.common.getLocator("lblSlogan")).getAttribute("value");
        Log.info("Show slogan : " + slogan);
        String lblTips1 = driver.findElement(OR.common.getLocator("lblTips1")).getAttribute("value");
        Log.info("Show tip 1: " + lblTips1);
        String lblTips2 = driver.findElement(OR.common.getLocator("lblTips2")).getAttribute("value");
        Log.info("Show tip 2: " + lblTips2);
        try {
            Assert.assertEquals(sloganExpect, slogan);
            Assert.assertEquals(tipExpect1, lblTips1);
            Assert.assertEquals(tipExpect2, lblTips2);

        } catch (AssertionError aE) {
            Log.info("Failed - Assert Error: " + aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        // Check button Start Flo exist
        try {
            driver.isElementExist(OR.common.getLocator("btnStartFlo"));
            MobileElement btnStartFlo = driver.findElement(OR.common.getLocator("btnStartFlo"));
            Log.info("Passed - Show button Start Flo correctly");
            // Close Start Flo popup
            btnStartFlo.click();
        }catch (Exception e){
            Log.info("Failed - Not show button Start Flo");
            flag = false;
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended Check Start Flo popup keyword");
        return flag;
    }

    /**
     * Open settings View
     */
    public void openSettingsView(){
        Log.info("Started openSettingsView keyword");
        driver.findElement(OR.common.getLocator("btnSettings")).click();
        Log.info("Ended openSettingsView keyword");
    }
    /**
     * Add new Item by click Plus Button
     */
    public void openNewItemView(){
        Log.info("Started openNewItemView keyword");
        driver.findElement(OR.common.getLocator("icoPlus")).click();
        closeSendNotification(true);
        Log.info("Ended openNewItemView keyword");
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
     * Clear all items in View
     */
    public void clearView(){
        Log.info("Started clearView keyword");
        if(driver.isElementPresent(OR.common.getLocator("secItemInList"))){
            driver.longPressElement(OR.common.getLocator("secItemInList"));
        }
        // Take view select longer
        if(driver.isElementPresent(OR.common.getLocator("btnSelectCancel"))){
            // Coordinate of Trash button
            driver.tapCoordinate(335,640);
            driver.findElement(OR.common.getLocatorDynamic("btnButtonPopup", new String[]{"Cancel"})).click();
        }
        // Check Select All is showing in View
        if(driver.isElementPresent(OR.common.getLocator("btnSelectAll"))){
            Log.info("Click 'Select all' button");
            driver.findElement(OR.common.getLocator("btnSelectAll")).click();
            // Click Trash option
            driver.findElement(OR.common.getLocatorDynamic("txtOptions", new String[]{"Trash"})).click();
            Log.info("Trash all items selected");
            driver.findElement(OR.common.getLocatorDynamic("btnButtonPopup", new String[]{"OK"})).click();
        // 1 item selected
        }else {
            if(driver.isElementPresent(OR.common.getLocatorDynamic("txtOptions", new String[]{"Trash"}))){
                driver.findElement(OR.common.getLocatorDynamic("txtOptions", new String[]{"Trash"})).click();
                driver.findElement(OR.common.getLocatorDynamic("btnButtonPopup", new String[]{"OK"})).click();
            }
        }
        Log.info("Ended clearView keyword");

    }

}