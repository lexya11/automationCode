package Keywords;

import Locators.ObjectRepository;
import Support.SetupServer;
import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class CollectionKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    HomeKeywords home;
    static Logger Log = Logger.getLogger(CollectionKeywords.class);

    public CollectionKeywords(SetupServer driver){
        this.driver = driver;
        this.home = new HomeKeywords(driver);
        this.common = new CommonKeywords(driver);
    }

    /**
     * Add new collection from Setting > collection, Local filter, Add/move collection view
     * NOTE: MUST open Add/Move to Collection view to do this keyword
     * @param location add from 3 location (setting, local filter, add_move collection)
     * @param title title of collection
     * @param colour Select 7 colors (Red, Orange, Yellow, Green, Blue, Indigo, Violet)
     * @param parentCollection If Parent is None => User can set Collection as Default
     * @param pathOfParentCollection The path to show parentCollection On Screen select parentCollection
     * @param isDefault true: set Collection as Default/ false: no set
     */
    public void addNewCollection(String location, String title, String colour, String parentCollection, String pathOfParentCollection, boolean isDefault, boolean isDone){
        Log.info("Started addNewCollection keyword");

        // Open new collection view from setting
        if(location.equals("setting")) {
            driver.Wait(2);
            Log.info("Open New Collection View from Setting > Collection");
            // Tap plus icon by Coordinate
            driver.tapCoordinate(OR.collection.getCoordinate("icoPlusAddCollection"));
        }
        // Open new collection view from local filter, add/move collection
        else if(location.equals("local filter")) {
            // Click on local filter
            MobileElement localFilter = driver.findElement(OR.collection.getLocator("localFilter"));
            localFilter.click();
            // Click plus icon
            MobileElement icoPlus = driver.findElement(OR.collection.getLocator("icoAddCollection"));
            icoPlus.click();
        }
        else if(location.equals("add_move collection")) {
            // Click plus icon
            MobileElement icoPlus = driver.findElement(OR.collection.getLocator("icoAddCollection"));
            icoPlus.click();
        }
        // Input collection details
        Log.info("Input title of Collection: "+title);
        MobileElement tbTitle = driver.findElement(OR.collection.getLocator("txbTitle"));
        tbTitle.setValue(title);
        MobileElement tbColor = driver.findElement(OR.collection.getLocator("optColor"));
        tbColor.click();
        tbColor.click();

        // Set color for collection
        Log.info("Choose Color: "+colour);
        String color = null;
        switch (colour){
            case "Red":
                color = "3";
                break;
            case "Orange":
                color = "4";
                break;
            case "Yellow":
                color = "11";
                break;
            case "Green":
                color = "8";
                break;
            case "Blue":
                color = "16";
                break;
            case "Indigo":
                color = "17";
                break;
            case "Violet":
                color = "24";
                break;
        }
        driver.findElement(OR.collection.getLocatorDynamic("pupColor",new String[]{color})).click();

        // Set parent for collection
        if(parentCollection.equals("None")){
            Log.info("Set parent collection is "+ parentCollection);
            // Don't do anything, default is None

            // Set collection is default
            if(isDefault == true){
                driver.tapElement(OR.collection.getLocator("tglDefaultCollection"));
                Log.info("Set collection is Default");
            }
        }else{
            // Click parent option
            MobileElement tbParent = driver.findElement(OR.collection.getLocator("optParent"));
            tbParent.click();
            driver.Wait(2);
            Log.info("Set parent collection is "+parentCollection);
            if(!pathOfParentCollection.equals("")){
                common.expandItem(pathOfParentCollection);}
            driver.Wait(1);
            MobileElement itmCollection = driver.findElement(OR.collection.getLocatorDynamic("itmCollectionInParentList",new String[]{parentCollection}));
            itmCollection.click();
        }
        // Click Done button
        if(isDone == true)
        {
            common.clickElementButton("Done");
        } else common.clickElementButton("Cancel");
        driver.Wait(3);
        Log.info("Ended addNewCollection keyword");
    }

    /**
     * Check error message when use create existing Collection
     * @return
     */
    public boolean checkExistCollectionErrorMessage(String message, String testcase){
        Log.info("Started checkExistCollection keyword");
        boolean flag = true;
        try {
            String textWarning =   driver.findElement(OR.collection.getLocator("pupMessage")).getText();
            Log.info("Show popup Message: " + textWarning);
            Assert.assertEquals(textWarning, message);
        }catch(AssertionError Ae){
            Log.info("Assert Error: " + Ae.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        // Close popupmessage
        MobileElement btnOK = driver.findElement(OR.collection.getLocator("btnOK"));
        btnOK.click();
        common.clickElementButton("Cancel");
        Log.info("Ended checkExistCollection keyword");
        return flag;
    }

    /**
     * Check current filter by a Collection
     * @param collection
     * @param testCase
     * @return
     */
    public boolean checkFilterByCollection(String collection,String testCase){
        String Collection = "     "+collection;
        boolean flag = true;
        String txtLocalFilter = driver.findElement(OR.collection.getLocator("txtlocalFilter")).getAttribute("value");
        try {
            Assert.assertEquals(txtLocalFilter,Collection);
            Log.info("Passed - Filter by Collection "+collection);
        }catch (AssertionError aE){
            Log.info("Failed - Not filter by Collection "+collection+ " - Assert Error: "+aE.getMessage());
            flag = false;
            driver.captureScreenShot(testCase);
        }
        return flag;
    }

    /**
     * Select collection for local filter then Click on view tab
     * @param collection
     * @param type: Email, Event, To.Do, Note, Contact, Bookmark, Collection
     */
    public void filterByType(String collection, String type){
        Log.info("Started filterByType keyword");

        // Select local filter
        if(!collection.equals("")){
            MobileElement localFilter = driver.findElement(OR.collection.getLocator("localFilter"));
            localFilter.click();
            Log.info("Select local filter > "+collection);
            MobileElement Collection =  driver.findElement(5,OR.collection.getLocatorDynamic("itmCollection",new String[]{collection}));
            Collection.click();
        }
        driver.Wait(5);
        // Click on view tab
        switch (type.toLowerCase()){
            case "event":
                type = "Events";
                break;
            case "todo":
                type = "ToDo's";
                break;
            case "contact":
                type = "Contacts";
                break;
            case "note":
                type = "Notes";
                break;
            case "bookmark":
                type = "Bookmarks";
                break;
            case "collection":
                type = "Collections";
                break;
        }
        if(!type.equals("")){
            // Check tab view is enabled on Screen
            if(driver.isElementPresent(OR.collection.getLocatorDynamic("txtTabView", new String[]{type}))){
                Log.info("Open tab view > "+ type);
                driver.findElement(OR.collection.getLocatorDynamic("txtTabView", new String[]{type})).click();
            }else {
                Log.info("Tab view > "+type+ " is not displayed on Screen");
            }
        }
        Log.info("Ended filterByType keyword");
    }

    /**
     * Select New item to add new by type
     * @param type: Email,Event,To.Do,Note,Contact
     */
    public void openAddItemView(String type){
        Log.info("Started openAddItemView keyword");
        // Select type of item to add
        Log.info("Select type of item to add > "+ type);
        switch (type.toLowerCase()){
            case "email":
                type = "icon 1 selected";
                break;
            case "event":
                type = "icon 2 selected";
                break;
            case "todo":
                type = "icon 3 selected";
                break;
            case "contact":
                type = "icon 4 selected";
                break;
            case "note":
                type = "icon 5 selected";
                break;
        }
        driver.findElement(OR.collection.getLocatorDynamic("selectType",new String[]{type})).click();
        common.closeSendNotification(true);
        Log.info("Ended openAddItemView keyword");
    }

    /**
     * Open "Create New Things in this Collection" View then search item to add
     * @param keyword: input keyword in Search textbox
     * @param type: input: Email, Event, To.Do, DoneTodo, Contact, Note
     * @param title
     */
    public void searchItemToAdd(String keyword, String type, String title){
        Log.info("Started searchItemToAdd keyword");

        // Access to search view
        MobileElement txbSearch = driver.findElement(OR.collection.getLocator("txtSearch"));
        txbSearch.click();
        txbSearch.setValue(keyword+"\n");
        Log.info("Select "+type+" has name "+title);
        switch (type){
            case "Email":
                type = "\uE037";
                break;
            case "ToDo":
                type = "\uE094";
                break;
            case "DoneTodo":
                type = "\uE084";
                break;
            case "Note":
                type = "\uE081";
                break;
            case "Event":
                type = "\uE016";
                break;
        }
        String [] item ={type,title};
        // Open Contact from Links section
        if (type.equals("Contact")){
            try {
                driver.isElementExist(OR.collection.getLocatorDynamic("itmContactItem",new String[]{title}));
                driver.findElement(OR.collection.getLocatorDynamic("itmContactItem",new String[]{title})).click();
            }catch (Exception e){
                Log.info("Not found Contact to select");
            }
        } else if(type.equals("Email")){
            try {
                driver.isElementExist(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{title}));
                driver.findElement(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{title})).click();
            }catch (Exception e){
                Log.info("Not found Email to select");
            }
        }
        else {
            try {
                // Found Items
                driver.isElementExist(OR.collection.getLocatorDynamic("itmOtherItems",item));
                // Open Items
                driver.findElement(OR.collection.getLocatorDynamic("itmOtherItems",item)).click();
            }catch (Exception e){
                Log.info("Not found item to select");
            }
        }
        common.clickElementButton("Done");
        Log.info("Ended searchItemToAdd keyword");
    }

    /**
     * Open "Create New Things in this Collection" View, then select item from Suggestion list to add.
     * User can select many items.
     * @param type: 1 or many type, MUST have ‘comma’ for each type. EX: Email,Event,To.Do,DoneTodo,Contact,Note
     * @param title: 1 or many items, MUST have ‘comma’ for each item. Ex: Event 1,Contact 1,Note 1
     */
    public void addItemSuggestion(String type, String title){
        Log.info("Started addItemSuggestion keyword");
        if(type.contains(",") && title.contains(",")){
            int lengthType = type.split(",").length;
            for(int i = 0; i < lengthType; i++){
                // Split Type
                String splitType = type.split(",")[i];
                // Split Title
                String splitTitle = title.split(",")[i];
                Log.info("Select "+splitType+" has name "+splitTitle);
                switch (splitType){
                    case "Event":
                        splitType = "\uE016";
                        break;
                    case "Email":
                        splitType = "\uE037";
                        break;
                    case "ToDo":
                        splitType = "\uE094";
                        break;
                    case "DoneTodo":
                        splitType = "\uE084";
                        break;
                    case "Note":
                        splitType = "\uE081";
                        break;
                }
                if(!splitType.equals("Contact")){
                    String[] Item ={splitType,splitTitle};
                    try {
                        // Found Items (To.Do/DoneTodo/Note/Event/Email)
                        driver.isElementExist(OR.collection.getLocatorDynamic("itmOtherItems",Item));
                        driver.findElement(OR.collection.getLocatorDynamic("itmOtherItems",Item)).click();
                    }catch (Exception e){
                        Log.info("Not found item has title: "+splitTitle);
                    }
                }else if(splitType.equals("Email")){
                    try {
                        driver.isElementExist(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{splitTitle}));
                        driver.findElement(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{splitTitle})).click();

                    }catch (Exception e){
                        Log.info("Not found Email has subject: "+splitTitle);

                    }
                } else {
                    // Found Contact
                    try {
                        driver.isElementExist(OR.collection.getLocatorDynamic("itmContactItem",new String[]{splitTitle}));
                        driver.findElement(OR.collection.getLocatorDynamic("itmContactItem",new String[]{splitTitle})).click();
                    }catch (Exception e){
                        Log.info("Not found Contact has name: "+splitTitle);
                    }
                }
            }
        }else {
            Log.info("Select "+type+" has name "+title);
            switch (type){
                case "Event":
                    type = "\uE016";
                    break;
                case "Email":
                    type = "\uE037";
                    break;
                case "DoneTodo":
                    type = "\uE084";
                    break;
                case "ToDo":
                    type = "\uE094";
                    break;
                case "Note":
                    type = "\uE081";
                    break;
            }
            String [] item ={type,title};
            // Open Contact from Links section
            if (type.equals("Contact")){
                try {
                    driver.isElementExist(OR.collection.getLocatorDynamic("itmContactItem",new String[]{title}));
                    driver.findElement(OR.collection.getLocatorDynamic("itmContactItem",new String[]{title})).click();
                }catch (Exception e){
                    Log.info("Not found Contact has name "+title);
                }
            } else if(type.equals("Email")){
                try {
                    driver.isElementExist(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{title}));
                    driver.findElement(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{title})).click();

                }catch (Exception e){
                    Log.info("Not found Email has Subject "+title);

                }
            }else {
                try {
                    // Found Items
                    driver.isElementExist(OR.collection.getLocatorDynamic("itmOtherItems",item));
                    // Open Items
                    driver.findElement(OR.collection.getLocatorDynamic("itmOtherItems",item)).click();
                }catch (Exception e){
                    Log.info("Not found item has title: "+title);
                }
            }
        }
        common.clickElementButton("Done");
        Log.info("Ended addItemSuggestion keyword");
    }

    /**
     * Check item is displayed to the current Collection
     * @param type: Email, Event, To.Do, DoneTodo, Contact, Note
     * @param title
     * @param testCase
     * @return
     */
    public boolean checkItemBelongCollection(String type, String title, String testCase){
        Log.info("Started checkItemBelongCollection keyword");
        boolean flag = true;

        Log.info("Check "+ type +" has name "+ title);
        String Type = type;
        switch (type){
            case "Event":
                type = "\uE017";
                break;
            case "DoneTodo":
                type = "\uE084";
                break;
            case "Email":
                type = "\uE037";
                break;
            case "ToDo":
                type = "\uE094";
                break;
            case "Note":
                type = "\uE081";
                break;
        }
        driver.Wait(3);

        // Get name of Collection in Local filter
        String txtLocalFilter = driver.findElement(OR.collection.getLocator("txtlocalFilter")).getAttribute("value");
        String Collection = txtLocalFilter.split("     ")[1];

        String[] item ={type, title};

        if(type.equals("Contact")){
            for(int i = 0; i<3; i++){
                if(driver.isElementPresent(OR.collection.getLocatorDynamic("itmContactItem",new String[]{title}))){
                    Log.info("Passed - "+Type+" has name "+title+" is belong to Collection "+Collection);
                    break;
                }else {
                    driver.Wait(2);
                }
                if(i==2){
                    Log.info("Failed - "+Type+" has name "+title+" is NOT belong to Collection "+Collection);
                    flag = false;
                }
            }
//            try {
//                driver.isElementExist(OR.collection.getLocatorDynamic("itmContactItem",new String[]{title}));
//
//
//            }catch (Exception e){
//                Log.info("Failed - "+Type+" has name "+title+" is NOT belong to Collection "+Collection);
//                flag = false;
//            }
        }else if(Type.equals("Email")){
            for(int i = 0; i<3; i++){
                if(driver.isElementPresent(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{title}))){
                    Log.info("Passed - "+Type+" has Subject "+title+" is belong to Collection "+Collection);
                    break;
                }else {
                    driver.Wait(2);
                }
                if(i==2){
                    Log.info("Failed - "+Type+" has Subject "+title+" is NOT belong to Collection "+Collection);
                    flag = false;
                }
            }
//            try {
//                driver.isElementExist(OR.collection.getLocatorDynamic("itmEmailItem",new String[]{title}));
//                Log.info("Passed - "+Type+" has Subject "+title+" is belong to Collection "+Collection);
//            }catch (Exception e){
//                Log.info("Failed - "+Type+" has Subject "+title+" is NOT belong to Collection "+Collection);
//                flag = false;
//            }
        } else {
            for(int i = 0; i<3; i++){
                if(driver.isElementPresent(OR.collection.getLocatorDynamic("itmOtherItems",item))){
                    Log.info("Passed - "+Type+" has title "+title+" is belong to Collection "+Collection);
                    break;
                }else {
                    driver.Wait(2);
                }
                if(i==2){
                    Log.info("Failed - "+Type+" has title "+title+" is NOT belong to Collection "+Collection);
                    flag = false;
                }
            }
//            try {
//                driver.isElementExist(OR.collection.getLocatorDynamic("itmOtherItems",item));
//                Log.info("Passed - "+Type+" has title "+title+" is belong to Collection "+Collection);
//
//            }catch (Exception e){
//                Log.info("Failed - "+Type+" has title "+title+" is NOT belong to Collection "+Collection);
//                flag = false;
//            }
        }
        if (flag == false){
            driver.captureScreenShot(testCase);
        }
        Log.info("Ended checkItemBelongCollection keyword");
        return  flag;
    }

    /**
     * Swipe left to right on an item then click on a icon
     * @param type: Email, Event, To.Do, Done To.do, Contact, Note
     * @param title
     * @param icon: Email, Event, To.Do, Due, Note
     */
    public void swipeLeftToRightItem(String type, String title, String icon){
        Log.info("Started swipeLeftToRightItem keyword");
        driver.Wait(2);
        String Type = type;
        switch (type.toLowerCase()){
            case "event":
                type = "\uE017";
                break;
            case "done todo":
                type = "\uE084";
                break;
            case "note":
                type = "\uE081";
                break;
            case "todo":
                type = "\uE094";
                break;
            case "collection":
                type = "\uE01C";
                break;
        }
        MobileElement Item;
        // Contact item - Only find item by title
        if(Type.contains("Contact")){
            driver.Wait(10,OR.collection.getLocatorDynamic("itmContactItem", new String[]{title}));
            Item = driver.findElement(5,OR.collection.getLocatorDynamic("itmContactItem", new String[]{title}));
        }else if(Type.contains("Email")){
            driver.Wait(10,OR.collection.getLocatorDynamic("itmEmailItem", new String[]{title}));
            Item = driver.findElement(5,OR.collection.getLocatorDynamic("itmEmailItem", new String[]{title}));
        }
        // Other item - Find item by type and title
        else{
            String[]item ={type,title};
            driver.Wait(10,OR.collection.getLocatorDynamic("itmOtherItems", item));
            Item = driver.findElement(5,OR.collection.getLocatorDynamic("itmOtherItems", item));

        }
        int X = Item.getLocation().getX(); //0
        int Y = Item.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRight = {X,Y,X1,Y1};
        // Swipe left to right on item
        Log.info("Swipe left to right on "+Type+" '"+title+"'");
        driver.touchActionSwipe(swipeLeftToRight);

        // Click on icon after swiping
        Log.info("Click on "+icon);
        switch (icon.toLowerCase()){
            case "email":
                icon = "\uE037";
                break;
            case "event":
                icon = "\uE004";
                break;
            case "todo":
                icon = "\uE083";
                break;
            case "note":
                icon = "\uE081";
                break;
            // icon Due Date on to.do item
            case "due":
                icon = "\uE021";
                break;
        }
        driver.tapElement(OR.collection.getLocatorDynamic("icoObject",new String[]{icon}));

        // If email need to show header of Compose Email editor
        if(icon.equals("\uE037")){
            driver.Wait(5);
            driver.touchActionSwipe(190,115,190,415);
        }
        common.closeSendNotification(true);
        Log.info("Ended swipeLeftToRightItem keyword");
    }

    /**
     * Swipe right to left on an item then click on a icon
     * @param type: Email, Event, To.Do, DoneTodo, Contact, Note, Collection
     * @param title
     * @param icon: Collection, Trash
     */
    public void swipeRightToLeftItem(String type, String title, String icon){
        Log.info("Started swipeRightToLeftItem keyword");
        driver.Wait(3);
        String Type = type;
        switch (type.toLowerCase()){
            case "event":
                type = "\uE017";
                break;
            case "note":
                type = "\uE081";
                break;
            case "todo":
                type = "\uE094";
                break;
            case "done todo":
                type = "\uE084";
                break;
            case "collection":
                type = "\uE01C";
                break;
        }
        MobileElement Item;
        // Contact item - Only find item by title
        if(Type.contains("Contact")){
            driver.Wait(10,OR.collection.getLocatorDynamic("itmContactItem", new String[]{title}));
            Item = driver.findElement(5,OR.collection.getLocatorDynamic("itmContactItem", new String[]{title}));
        }else if(Type.contains("Email")){
            driver.Wait(10,OR.collection.getLocatorDynamic("itmEmailItem", new String[]{title}));
            Item = driver.findElement(5,OR.collection.getLocatorDynamic("itmEmailItem", new String[]{title}));
        }
        // Other item - Find item by type and title
        else{
            String[]item ={type,title};
            driver.Wait(10,OR.collection.getLocatorDynamic("itmOtherItems", item));
            Item = driver.findElement(5,OR.collection.getLocatorDynamic("itmOtherItems", item));
        }
        int X = Item.getLocation().getX(); //0
        int Y = Item.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        // Action swipe right to left
        Log.info("Swipe right to left on "+Type+" has name "+title);
        driver.touchActionSwipe(swipeRightToLeftToDo);

        // Click on icon after swiping
        Log.info("Click on icon "+icon);
        switch (icon.toLowerCase()){
            case "collection":
                icon = "\uE01C";
                break;
            case "trash":
                icon = "\uE078";
                break;
        }
        driver.tapElement(OR.collection.getLocatorDynamic("icoObject",new String[]{icon}));
        Log.info("Ended swipeRightToLeftItem keyword");
    }

    /**
     * Empty Trash Collection
     */
    public void emptyTrashCollection(){
        Log.info("Started emptyTrashCollection keyword");
        Log.info("Select local filter");
        MobileElement localFilter = driver.findElement(OR.collection.getLocator("localFilter"));
        localFilter.click();
        Log.info("Go to Trash Collection");
        MobileElement Collection =  driver.findElement(OR.collection.getLocatorDynamic("itmCollection",new String[]{"Trash"}));
        Collection.click();

        Log.info("Click empty trash button");
        driver.findElement(OR.collection.getLocator("btnEmptyTrash")).click();
        common.clickElementButton("OK");
        // Wait to delete all items
        driver.Wait(5);
        Log.info("Ended emptyTrashCollection keyword");
    }
}
