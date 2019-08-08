package Keywords;
import Locators.*;
import Support.*;
import io.appium.java_client.*;
import org.apache.log4j.Logger;
import org.testng.Assert;

public class SettingsKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    HomeKeywords home;
    static Logger Log = Logger.getLogger(SettingsKeywords.class);

    public SettingsKeywords(SetupServer driver){
        this.driver = driver;
        this.common = new CommonKeywords(driver);
        this.home = new HomeKeywords(driver);
    }

    /**
     * Click an option in setting list to navigate/select this item
     * @param options (Accounts, General, Subscription, Customization, Collections, Emails, Calendars, To.Do's, Contacts)
     * General > Sync, Alert Snooze
     * Email > Desciption lines, default
     */
    public void clickSettingOptionList(String options){
        Log.info("Started clickSettingOptionList keyword");

        String SubsMessage = "Sorry, Flo cannot perform subscription for this Apple ID on this device.";

        Log.info("Click Settings > " + options);
        driver.Wait(2);
        driver.findElement(OR.settings.getLocatorDynamic("optSetting", new String[]{options})).click();

        // Close error for Subscription
        if(options.equalsIgnoreCase("Subscription")){
            driver.Wait(2);
            // Check and close other popup
            if(driver.isElementPresent(OR.settings.getLocatorDynamic("SubsMessage",new String[]{SubsMessage}))) {
                Log.info("Error '"+ SubsMessage +"' shows");
                common.clickElementButton("Close");
            }
        }

        // Swipe Up to Down to show all options
        if(options.equalsIgnoreCase("email")||options.equalsIgnoreCase("calendars")||options.equalsIgnoreCase("todo's")){
            common.closeSendNotification(true);
            Log.info("Swipe down to up > " + options);
            driver.touchActionSwipe(185,122,185,62);
        }
        Log.info("Ended clickSettingOptionList keyword");
    }

    /**
     * Check Setting Details Screen is displayed by Navigation Bar
     * @param settingScreen (Accounts/General/Subscription/Customization/Collections/Emails/Calendars/To.Do's/Contacts)
     * @param testCase
     */
    public boolean checkSettingScreen(String settingScreen, String testCase) {
        Log.info("Started checkSettingScreen keyword");
        boolean flag = true;

        if(settingScreen.equalsIgnoreCase("Email"))
            common.closeSendNotification(true);

        MobileElement NavigationBars = driver.findElement(OR.settings.getLocator("secNavigationBar"));
        String actualScreen = NavigationBars.getAttribute("name");
        try{
            Assert.assertEquals(actualScreen, settingScreen);
            Log.info("Passed - Screen displays > "+ actualScreen);
        } catch (AssertionError e){
            Log.info("Failed - Screen doesn't display > "+ actualScreen);
            flag = false;
            driver.captureScreenShot(testCase);
        }
        Log.info("Ended checkSettingScreen keyword");
        return flag;
    }

    /**
     * Check value is selected of each Functions in Settings
     * @param function (Function selected to setup)
     * @param value (value is selected)
     * @param testCase
     */
    public boolean checkSettingValue(String function, String value, String testCase){
        Log.info("Started checkSettingValue keyword");
        boolean flag = true;
        // Get value to compare
        MobileElement Checkvalue = driver.findElement(OR.settings.getLocatorDynamic("optValueSetting",new String[]{function}));
        String values = Checkvalue.getAttribute("value");
        try{
            Assert.assertEquals(values,value);
            Log.info(function + " is equal " + values);
        } catch (AssertionError e){
            Log.error(function + " is not equal " + value + e);
            driver.captureScreenShot(testCase);
            flag = false;
        }
        Log.info("Ended checkSettingValue keyword");
        return flag;
    }

    /*******************************
     * Settings > Accounts
     */
    // ToDo implement keywords

    /**
     * Update Flo Account
     * @param name (Edit Name field)
     * @param description (Edit Description field)
     * @param isCalendars (Not use)
     */
    public void updateFloAccount (String emailAccount, String name, String description, boolean isCalendars){
        Log.info("Started updateFloAccount keywords");
        // Select email of Flo account
        MobileElement emailinAccount = driver.findElement(OR.settings.getLocatorDynamic("accountEmail",new String[]{emailAccount}));
        emailinAccount.click();
        // Edit Name field
        MobileElement tbName = driver.findElement(OR.settings.getLocator("tbName"));
        tbName.clear();
        tbName.setValue(name);
        // Edit Description field
        if(!description.equals("")){
            MobileElement tbDescription = driver.findElement(OR.settings.getLocator("tbDescription"));
            tbDescription.clear();
            tbDescription.setValue(description);
        }
        // Click on Done buton
        common.clickElementButton("Done");
        Log.info("Ended updateFloAccount keywords");
    }

    public void addGoogleAccount(String name , String description, boolean isCalendars, boolean isMail){
        // ToDo addGoogleAccount
    }

    /**
     * Tap on "+" icon to open "Add Account", Select an account to add
     * @param account (iCloud, Google, Yahoo, Other)
     */
    public void selectAccountsToAdd(String account){
        Log.info("Started selectAccounts keyword ");
        // Click on plus icon
        MobileElement icoAddAccount = driver.findElement(OR.settings.getLocator("icoAddAccount"));
        icoAddAccount.click();
        driver.Wait(5);

        Log.info("Clicked " +account+ " account");
        switch (account){
            case "iCloud":
                driver.findElement(OR.settings.getLocatorDynamic("addAccount", new String[]{"logo_icloud"})).click();
                break;
            case "Google":
                driver.findElement(OR.settings.getLocatorDynamic("addAccount", new String[]{"Google_Logo"})).click();
                break;
            case "Yahoo":
                driver.findElement(OR.settings.getLocatorDynamic("addAccount", new String[]{"Yahoo_Logo"})).click();
                break;
            case "Other":
                // Click Other
                common.clickStaticTextButton("Other");
                // Click Add Mail account
                MobileElement addOtherMail = driver.findElement(OR.settings.getLocator("otherAccount"));
                addOtherMail.click();
                break;
        }
        Log.info("Ended selectAccounts keyword ");
    }

    /**
     * Input Account Info
     * @param name (Name field)
     * @param email (Email field)
     * @param password (Password field)
     * @param description (Description field)
     * @param IsClickAdd (true: Click "Add Account" button / False: Don't click "Add Account" button)
     */
    public void inputAccountInfo(String name, String email, String password, String description, boolean IsClickAdd){
        Log.info("Started inputAccountInfo keywords ");
        // Input Name field
        MobileElement tbName = driver.findElement(OR.settings.getLocator("tbName"));
        tbName.clear();
        tbName.setValue(name);
        // Input Email field
        Log.info("Input Email");
        MobileElement tbEmail = driver.findElement(OR.settings.getLocator("tbEmail"));
        tbEmail.clear();
        tbEmail.setValue(email);
        // Input Password field
        Log.info("Input Password");
        MobileElement tbPassword = driver.findElement(OR.settings.getLocator("tbPassword"));
        tbPassword.clear();
        tbPassword.setValue(password);
        // Input Description field
        MobileElement tbDescription = driver.findElement(OR.settings.getLocator("tbDescription"));
        tbDescription.clear();
        tbDescription.setValue(description);
        // Click on "Add Account" button
        MobileElement btnAddAccount = driver.findElement(OR.settings.getLocator("btnAddAccount"));
        if (IsClickAdd == true){
            Log.info("Click Add Account button");
            btnAddAccount.click();
        }
        Log.info("Ended inputAccountInfo keywords ");
    }

    /**
     * input Other Mail Account Info
     * @param incoming  (Host name field of Incoming Mail Server)
     * @param outgoing (Host name field of Outgoing Mail Server)
     * @param IsClickDone (true: click "Done" button / false: Don't click "Done" button )
     */
    public void inputOtherAccountInfo(String incoming, String outgoing, boolean IsClickDone){
        Log.info("Started inputOtherAccountInfo keyword");
        inputAccountInfo("account","email","pass","des", true);
        // Input Host Name for INCOMING SERVER
        MobileElement tbHostIncoming = driver.findElement(OR.settings.getLocator("tbHostIncoming"));
        tbHostIncoming.clear();
        tbHostIncoming.setValue(incoming);
        // Input Host Name for OUTGOING SERVER
        MobileElement tbHostOutgoing = driver.findElement(OR.settings.getLocator("tbHostOutgoing"));
        tbHostOutgoing.clear();
        tbHostOutgoing.setValue(outgoing);
        // Click Done
        if (IsClickDone == true) {
            common.clickElementButton("Done");
        }
        Log.info("Ended inputOtherAccountInfo keyword");
    }

    /**
     * Check Error Message popup
     * @param Content (the content of message)
     * @param testcase
     * @return
     */
    public boolean checkErrorMessage(String Content, String testcase) {
        Log.info("Started checkErrorMessage keywords");
        boolean flag = true;
        try{
            MobileElement ErrorMessage = driver.findElement(OR.settings.getLocator("ErrorMessage"));
            String text = ErrorMessage.getAttribute("value");
            Log.info("Error message shows: "+ErrorMessage.getAttribute("value"));
            Assert.assertEquals( text,Content);
        }catch (AssertionError aE) {
            Log.info("Assert Error: " + aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        // Click "OK" button to close popup
        common.clickElementButton("OK");
        Log.info("Ended checkErrorMessage keywords");
        return flag;
    }

    public void updateGoogleAccount(String name , String description, boolean isCalendars, boolean isMail){
        //Todo updateGoogleAccount
    }

    public void reAuthenticateGoogle(String email , String password){
        //Todo reAuthenticateGoogle
    }

    /**
     * Remove only 3rd party account which has been already added
     * @param emailAccount (Email address in Account)
     * @param isOK (true: Remove / false: Cancel )
     */
    public void removeAccount(String emailAccount, boolean isOK){
        Log.info("Started removeAccount keyword");
        Log.info("Open detail of acccount has email: "+ emailAccount);
        try {
            // Check Account has email address inputted
            driver.isElementExist(OR.settings.getLocatorDynamic("accountEmail",new String[]{emailAccount}));
            //Select Account has email address expected
            MobileElement emailinAccount = driver.findElement(OR.settings.getLocatorDynamic("accountEmail",new String[]{emailAccount}));
            emailinAccount.click();
            common.clickElementButton("Remove account");
            if (isOK == true){
                Log.info("Remove account selected");
                common.clickElementButton("OK");
            }
            else {
                Log.info("Cancel remove account selected");
                common.clickElementButton("Cancel");
            }
        } catch (Exception e) {
            Log.info("Sorry, Email: "+emailAccount+" in account were not found");
            driver.captureScreenShot("Sorry, Email: "+emailAccount+" in account were not found");
        }
        Log.info("Ended removeAccount keyword");
    }

    public boolean checkSettingAccount(String email){
        //Todo checkSettingAccount
        return true;
    }

    /*******************************
     * Settings > General
     */

    /**
     * Switch ON/OFF toggle "Show All Warnings" in Settings > General
     * Check Status toggle
     * @param isOn (true: On / false: Off)
     */
    public void setShowAllWarnings(boolean isOn){
        Log.info("Started setShowAllWarnings keyword");
        String toggle = "Show All Warnings";
        // toggle Show All Warnings
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");

        if(isOn == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > General > "+toggle+" = "+isOn);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > General > "+toggle+" = "+isOn);
        }
        Log.info("Ended setShowAllWarnings keyword");
    }

    /**
     * Check About view in Setting > General
     * @param slogan (Less Work, More Flo.)
     * @param URL (floware.com)
     * @param version (Version)
     * @param testCase
     * @return
     */
    public boolean checkGeneralAbout(String slogan, String URL, String version, String testCase){
        Log.info("Started checkGeneralAbout keywords");
        boolean flag = true;
        // Check Flo Logo
        try {
            driver.isElementExist(OR.settings.getLocator("logoFlo"));
            Log.info("Passed - Show Logo Flo correctly");
        } catch (Exception e){
            Log.info("Failed - Not show Logo Flo");
            flag = false;
        }

        // Check Slogan of Flo
        String actualSlogan = driver.findElement(OR.settings.getLocator("lblslogan")).getAttribute("value");
        try {
            Assert.assertEquals(actualSlogan, slogan);
            Log.info("Passed - Show slogan: " + actualSlogan);
        } catch (AssertionError aE) {
            Log.info("Failed - Show slogan error: " + aE.getMessage());
            flag = false;
        }

        // Check URL server
        String url = driver.findElement(OR.settings.getLocator("linkURL")).getAttribute("name");
        try {
            Assert.assertEquals(url,URL);
            Log.info("Passed - Show URL: "+url);
        } catch (AssertionError aE){
            Log.info("Failed - Show URL error: " + aE.getMessage());
            flag = false;
        }

        // Check version & build
        String buildVersion = driver.findElement(OR.settings.getLocator("lblVersion")).getAttribute("value");
        try {
            Assert.assertEquals(buildVersion,version);
            Log.info("Passed - Show version & build: "+buildVersion);
        }catch (AssertionError aE){
            Log.info("Failed - Show version & build error: " + aE.getMessage());
            flag = false;
        }

        // capture screen shoot 1 time
        if(flag == false)
            driver.captureScreenShot(testCase);
        Log.info("Ended checkGeneralAbout keywords");
        return flag;
    }

    /*******************************
     * Settings > Customization
     */

    /**
     * Switch ON/OFF toggle "Show Star in List View" in Settings > Customization
     * @param isShow (true: On / false: Off)
     */
    public void setShowStarInListView(boolean isShow){
        Log.info("Started setShowStarInListView keyword");
        String toggle = "Show Stars in List View";
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isShow == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Customization > "+ toggle +" = "+ isShow);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Customization > "+ toggle +" = "+ isShow);
        }
        Log.info("Ended setCollectionOnHome keyword");
    }

    /**
     * Switch ON/OFF toggle "Light Header and Footer" in Settings > Customization
     * @param isOn (true: On / false: Off)
     */
    public void setLightHeaderFooter(boolean isOn){
        Log.info("Started setLightHeaderFooter keyword");
        String toggle = "Light Header and Footer";
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isOn == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Customization > "+ toggle +" = "+ isOn);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Customization > "+ toggle +" = "+ isOn);
        }
        Log.info("Ended setLightHeaderFooter keyword");
    }


    /**
     * Set time for Show Navigation Bar when Idle
     * @param time ("15 s", "30 s", "1 min", "5 mins", "Never" )
     */
    public void setShowNavigationBarWhenIdle(String time){
        Log.info("Started setShowNavigationBarWhenIdle keyword");
        driver.findElement(OR.settings.getLocator("btnShowNavigationBar")).click();
        driver.findElement(OR.settings.getLocatorDynamic("optShowNavigationTime",new String[]{time})).click();

        common.clickElementButton("Customization");
        Log.info("Ended setShowNavigationBarWhenIdle keyword");
    }

    /**
     * Change background image
     * @param imageName ("Image 1", "Image 2", "Image 3, "Image 4", "Image 5", "Image 6")
     */
    public void changeBackgroundImage(String imageName){
        Log.info("Started ChangeBackgroundImage keyword");
        switch (imageName.toLowerCase())
        {
            case "image 1":
                imageName = "Beijing-Sunrise-667h@2x.jpg ";
                break;
            case "image 2":
                imageName = "California-667h@2x.jpg ";
                break;
            case "image 3":
                imageName = "Cephalonia-667h@2x.jpg ";
                break;
            case "image 4":
                imageName = "Muztagata-667h@2x.jpg";
                break;
            case "image 5":
                imageName = "Redwoods-667h@2x.jpg";
                break;
            case "image 6":
                imageName = "New-Orleans-667h@2x.jpg";
                break;
        }
        driver.findElement(OR.settings.getLocator("btnBackGroundImage")).click();
        driver.findElement(OR.settings.getLocatorDynamic("imgBackGround",new String[]{imageName})).click();
        common.clickElementButton("Set");
        common.clickElementButton("Customization");
        Log.info("Ended ChangeBackgroundImage keyword");
    }
    /**
     * Check image is background image
     * @param imageName ("Image 1", "Image 2", "Image 3, "Image 4", "Image 5", "Image 6")
     * @param testCase
     */
    public boolean checkBackgroundImage(String imageName, String testCase){
        Log.info("Started checkBackgroundImage keyword");
        boolean flag = false;
        switch (imageName) {
            case "Image 1":
                imageName = "Beijing-Sunrise-667h@2x.jpg";
                break;
            case "Image 2":
                imageName = "California-667h@2x.jpg";
                break;
            case "Image 3":
                imageName = "Cephalonia-667h@2x.jpg";
                break;
            case "Image 4":
                imageName = "Muztagata-667h@2x.jpg";
                break;
            case "Image 5":
                imageName = "Redwoods-667h@2x.jpg";
                break;
            case "Image 6":
                imageName = "New-Orleans-667h@2x.jpg";
                break;
        }
        driver.findElement(OR.settings.getLocator("btnBackGroundImage")).click();
        if(driver.isElementPresent(OR.settings.getLocatorDynamic("ckbImageBackGround",new String[]{imageName}))){
            flag = true;
            Log.info("Passed - Background image is : " + imageName+ " as expected");
        } else{
            flag = false;
            Log.info("Failed - Background image is : " + imageName);
            driver.captureScreenShot(testCase);
        }
        common.clickElementButton("Customization");
        Log.info("Ended checkBackgroundImage keyword");
        return flag;
    }
    /**
     * Check SwitchToggle Item
     * @param toggle  ("Light Header and Footer", "Collection on Home","Floating Collection Button", ....)
     * @param status ON/OFF
     * @param testCase
     * @return true/false
     */
    public boolean checkSwitchToggle(String toggle, String status, String testCase){
        Log.info("Started checkSwitchToogle keyword");
        boolean flag ;
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(ObserveStatus.equals("1")){
            if(status.equals("ON"))
            {
                flag = true;
                Log.info("Passed - Switch Toogle is ON");
            }
            else{
                flag = false;
                Log.info("Failed - Switch Toogle is ON");
                driver.captureScreenShot(testCase);
            }
        }
        else{
            if(status.equals("ON"))
            {
                flag = false;
                Log.info("Failed - Switch Toogle is OFF");
                driver.captureScreenShot(testCase);
            }
            else{
                flag = true;
                Log.info("Passed - Switch Toogle is OFF");
            }
        }
        Log.info("Ended checkSwitchToogle keyword");
        return flag;
    }


    /*******************************
     * Settings > Subscription
     */


    /*******************************
     * Settings > Collections
     // ToDo implement keywords
     */

    /**
     * set Default Collection
     * @param collection
     */
    public void setDefaultCollection(String collection){
        Log.info("Started setDefaultCollection keyword");
        Log.info("Set Collection "+collection+ " as Default Collection");
        driver.Wait(5);
        // Open Collection
        driver.findElement(OR.settings.getLocatorDynamic("nameCollection", new String[]{collection})).click();
        String status = driver.findElement(OR.collection.getLocator("tglDefaultCollection")).getAttribute("value");
        if (status.equals("0")) {
            //Set Default collection
            Log.info("Started setDefaultCollection keyword");
            driver.tapElement(OR.collection.getLocator("tglDefaultCollection"));
        }
        // Tap on Done button
        common.clickElementButton("Done");
        // Tap on Done button
        Log.info("Ended setDefaultCollection keyword");
    }
    /**
     * Check Collection is Default collection from collection list
     * @param collection
     * @param testCase
     */
    public boolean checkIsDefaultCollection(String collection, String testCase){
        Log.info("Started checkIsDefaultCollection keyword");
        boolean flag;
        try {
            driver.Wait(1);
            driver.isElementExist(OR.settings.getLocatorDynamic("icoIsDefaultCollection", new String[]{collection}));
            flag = true;
            Log.info("Passed - Collection '"+collection+"' is default collection");
        }catch(Exception e){
            Log.info("Failed - Collection '"+collection+"' is not default collection" );
            flag = false;
            driver.captureScreenShot(testCase);
        }
        Log.info("Ended checkIsDefaultCollection keyword");
        return flag;
    }

    /**
     * Check Collection Item in Collection detail view
     * @param collection name of collection
     * @param color collection color  7 colors (Red, Orange, Yellow, Green, Blue, Indigo, Violet)
     * @param parent collection parent
     * @param isDefault collection is Default collection or not (True/False)
     */
    public boolean checkCollectionItemDetails(String collection, String color, String parent,boolean isDefault, String testcase) {
        Log.info("Started checkCollectionItemDetails keyword");
        boolean flag = true;
        driver.Wait(2);
        driver.findElement(OR.settings.getLocatorDynamic("nameCollection", new String[]{collection})).click();
        driver.Wait(2);
        // Check title
        try {
            driver.isElementExist(OR.collection.getLocatorDynamic("txbTitleEditView", new String[]{collection}));
            Log.info("Passed - Title is " + collection);
        } catch (AssertionError aE) {
            flag = false;
            Log.info("Failed - Title is not match as expected");
        }
        //Check Color
        String colour = color;
        switch (color) {
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
        driver.findElement(OR.collection.getLocator("optColor")).click();
        String nameColor = driver.findElement(OR.collection.getLocatorDynamic("pupColor", new String[]{color})).getAttribute("name");
        if (nameColor.equals("\uE08B")){
            Log.info("Passed - Color is "+colour+" as expected");
        }else {
            flag = false;
            Log.info("Failed - Color is Not "+colour+" as expected");
        }
        // Tap out pop up to close
        driver.tapCoordinate(200,560);
        //Check Parent Collection
        try{
            driver.isElementExist(OR.collection.getLocatorDynamic("optNameParent",new String[]{parent}));
            Log.info("Passed - parent is "+parent);
        } catch (Exception e){
            flag = false;
            Log.info("Failed - parent is not "+parent);
        }
        //Check Collection is default or not
        String status = driver.findElement(OR.collection.getLocator("tglDefaultCollection")).getAttribute("value");
        if((isDefault == true)&&(status.equals("1"))){
            Log.info("Passed - Collection is default collection as expected");
        }else if((isDefault == false)&&(status.equals("0"))){
            Log.info("Passed - Collection is not default collection as expected");
        }else {
            flag = false;
            Log.info("Failed - isDefault is not match as expected");
        }
        // Take screen shoot 1 time
        if(flag == false)
            driver.captureScreenShot(testcase);
        Log.info("Ended checkCollectionItemDetails keyword");
        common.clickElementButton("Done");
        return flag;
    }

    /**
     * Edit Collection
     * @param collection
     * @param newTitle new title
     * @param color collection color  7 colors (Red, Orange, Yellow, Green, Blue, Indigo, Violet)
     * @param pathOfOldParentCollection The path of old parent collection
     * @param newParent
     * @param pathOfNewParentCollection The path of new parent collection
     * @param isDefault collection is Default collection or not (True/False)
     * @param isDone (True/False)
     */
    public void editCollection(String collection, String newTitle, String color, String pathOfOldParentCollection, String newParent ,String pathOfNewParentCollection, boolean isDefault, boolean isDone){
        Log.info("Started editCollection keyword");
        boolean flag = true;
        driver.Wait(1);
        if(!pathOfOldParentCollection.equals("")){
            common.expandItem(pathOfOldParentCollection);
        }
        driver.Wait(1);
        driver.findElement(OR.settings.getLocatorDynamic("nameCollection", new String[]{collection})).click();
        driver.Wait(2);
        // Input collection details
        if(!newTitle.equals("")) {
            Log.info("Edit title of Collection: " + newTitle);
            MobileElement tbTitle = driver.findElement(OR.collection.getLocatorDynamic("txbTitleEditView",new String[]{collection}));
            tbTitle.clear();
            tbTitle.setValue(newTitle);
        }
        if(!color.equals("")) {
            Log.info("Change color of Collection: " + color);
            MobileElement tbColor = driver.findElement(OR.collection.getLocator("optColor"));
            tbColor.click();
            tbColor.click();
            // Set color for collection
            Log.info("Choose Color: "+color);
            switch (color){
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
        }
        String status = driver.findElement(OR.collection.getLocator("tglDefaultCollection")).getAttribute("value");
        if(!newParent.equals("")){

                // Click parent option
            MobileElement tbParent = driver.findElement(OR.collection.getLocator("optParent"));
            tbParent.click();
            driver.Wait(1);
            Log.info("Set parent collection is "+newParent);
            if(!pathOfNewParentCollection.equals("")){
            common.expandItem(pathOfNewParentCollection);}
            MobileElement itmCollection = driver.findElement(OR.collection.getLocatorDynamic("itmCollectionInParentList",new String[]{newParent}));
            itmCollection.click();
        }
        Log.info("Change Make Default collection ");
        if (((isDefault == true) && (status.equals("0"))) || (((isDefault == false) && (status.equals("1"))))) {
            driver.tapElement(OR.collection.getLocator("tglDefaultCollection"));
            Log.info("Set collection is Default");
        }
        // Click Done button
        if(isDone == true)
        {
            common.clickElementButton("Done");
        } else common.clickElementButton("Cancel");
        Log.info("Ended editCollection keyword");
    }

    /**
     *Delete Collection From Details View
     * @param collection name of collection
     * @param pathParentCollection for showing Collection to open (ex: General,CollectionName)
     * @param isDone - True/false
     */
    public void deleteCollectionFromDetailsView(String collection, String pathParentCollection, boolean isDone){
        Log.info("Started deleteCollectionFromDetailsView keyword");
        driver.Wait(2);
        if(!pathParentCollection.equals(""))
            common.expandItem(pathParentCollection);
        // Open Collection
        driver.findElement(OR.settings.getLocatorDynamic("nameCollection", new String[]{collection})).click();
        //Tap Delete button
        common.clickStaticTextButton("Delete");
        driver.Wait(1);
        if(isDone){
            //Confirm delete
            common.clickElementButton("OK");
        } else common.clickElementButton("Cancel");
        Log.info("Ended deleteCollectionFromDetailsView keyword");
    }

    /**
     * Delete collection By Swipe
     * @param collection name of collection
     * @param pathParentCollection for showing Collection to open (ex: General,CollectionName)
     * @param isDone - True/false
     */
    public void deleteCollectionBySwipe(String collection,String pathParentCollection, boolean isDone){
        Log.info("Started deleteCollectionBySwipe keyword");
        driver.Wait(2);
        if(!pathParentCollection.equals(""))
            common.expandItem(pathParentCollection);
        Log.info("Start to swipe");
        MobileElement collectionItem = driver.findElement(OR.settings.getLocatorDynamic("collectionInList", new String[]{collection}));
        int X = collectionItem.getLocation().getX(); //0
        int Y = collectionItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        // Action swipe right to left on To.Do
        Log.info("Swipe right to left on Collection: "+collection);
        driver.touchActionSwipe(swipeRightToLeftToDo);
        driver.Wait(2);
        Log.info("Clicking Trash icon");
        driver.tapElement(OR.settings.getLocator("icoTrash"));
        if (isDone == true) {
            //
//            common.clickElementButton("Delete");
            common.clickElementButton("OK");
        }else common.clickElementButton("Cancel");
        Log.info("Ended deleteCollectionBySwipe keyword");
    }
    /**
     * Check Collection exist or not exist in view
     * @param name
     * @param isExist: Exist - check item exist / Not Exist - check item not exist
     * @param testCase
     */
    public boolean checkCollectionExist(String name, String isExist, String testCase){
        Log.info("Started checkCollectionExist keyword");
        boolean flag = true;
        // Check item exist
        driver.Wait(4);
        try {
            driver.isElementExist(OR.settings.getLocatorDynamic("nameCollection", new String[]{name}));
            if(isExist.equals("Not Exist")){
                Log.info("Failed - Item '"+name+"' is existed");
                flag = false;
                driver.captureScreenShot(testCase);
            }else if(isExist.equals("Exist")){
                Log.info("Passed - Item '"+name+"' is existed");
            }
        }
        catch(Exception e)
        {
            if(isExist.equals("Exist")){
                Log.info("Failed - Item '"+name+"' is not existed");
                flag = false;
                driver.captureScreenShot(testCase);
            }else if(isExist.equals("Not Exist")){
                Log.info("Passed - Item '"+name+"' is not existed");
            }
        }
        Log.info("Ended checkCollectionExist keyword");
        return flag;
    }
    /**
     * Check Default Collection Can Not Be Delete
     * @param collection
     * @param testCase
     */
    public boolean checkDefaultCollectionNotBeDelete(String collection, String testCase){
        Log.info("Started checkDefaultCollectionNotBeDelete keyword");
        boolean flag = true;
        // Check item exist
        driver.Wait(2);
        driver.findElement(OR.settings.getLocatorDynamic("nameCollection", new String[]{collection})).click();
        driver.Wait(1);
        common.clickStaticTextButton("Delete");
        driver.Wait(1);
        if(driver.isElementPresent(OR.collection.getLocator("btnConfirmDelete"))){
            flag = false;
            Log.info("Failed - Enable to Delete Default Collection ");
            driver.captureScreenShot(testCase);
        }
        common.clickElementButton("Cancel");
        Log.info("Ended checkDefaultCollectionNotBeDelete keyword");
        return flag;
    }

    /*******************************
     * Settings > Emails
     */

    /**
     * Set default Description lines
     * @param lineNumber (0 line, 1 lines, 2 lines, 3 lines, 4 lines)
     */
    public void setEmailDescriptionLine(String lineNumber) {
        Log.info("Started setEmailDescriptionLine keyword");
        Log.info("Set Email Description " +lineNumber );
        driver.findElement(OR.settings.getLocator("btnPreview")).click();
        driver.Wait(2);
        driver.findElement(OR.settings.getLocatorDynamic("descriptionLines", new String[]{lineNumber})).click();
        common.clickElementButton("Email");
        Log.info("Ended setEmailDescriptionLine keyword");
    }

    /**
     * Check default Description lines
     * @param lineNumber (0 line, 1 lines, 2 lines, 3 lines, 4 lines)
     * @param testCase
     * @return boolean true/false
     */
    public boolean checkEmailDescriptionLine(String lineNumber ,String testCase ) {
        Log.info("Started checkEmailDescriptionLine keyword");
        boolean flag = false;
        driver.Wait(2);
        String value = driver.findElement(OR.settings.getLocator("optPreview")).getAttribute("value");
        if(value.contains(lineNumber)){
            flag = true;
            Log.info("Passed - Email Description Line is: "+ value);
        }else {
            Log.info("Failed - Email Description Line is - " + value);
            driver.captureScreenShot(testCase);
        }
        Log.info("Ended checkEmailDescriptionLine keyword");
        return flag;
    }


    /**
     * Set default from email to send
     * @param emailAccount (Flo, iCloud, Yahoo, Gmail, Other email)
     */
    public void setDefaultEmail(String emailAccount){
        Log.info("Started setDefaultEmail keyword");
        driver.Wait(4);
        try {
            driver.findElement(OR.settings.getLocator("btnDefaultFrom")).click();
            driver.Wait(1);
            // Check account exist
            driver.isElementExist(OR.settings.getLocatorDynamic("accountEmail",new String[]{emailAccount}));

            // Select account to set Default From
            Log.info("Set default email > "+ emailAccount);
            MobileElement DefaultFrom = driver.findElement(OR.settings.getLocatorDynamic("accountEmail",new String[]{emailAccount}));
            DefaultFrom.click();
            common.clickElementButton("Email");
        } catch (Exception e) {
            Log.info("Email account " + emailAccount + " not exist");
            driver.captureScreenShot("Email account " + emailAccount + " not exist");
        }
        Log.info("Ended setDefaultEmail keyword");
    }

    /**
     * Switch ON/OFF toggle "Move email after creating Event or To.Do" in Settings > Emails
     * @param isMove (true: On / false: Off)
     */
    public void setMoveEmail(boolean isMove){
        Log.info("Started setMoveEmail keyword");
        String toggle = "Move Email to Default Collection after creating ToDo or Event";
        // get value of toggle Move email after creating Event or To.Do
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isMove == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Emails > "+toggle+" = "+isMove);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Emails > "+toggle+" = "+isMove);
        }
        Log.info("Ended setMoveEmail keyword");
    }

    /**
     * Add email signature for an email account
     * @param email
     * @param signature (signature text inputted)
     */
    public void addEmailSignature (String email, String signature){
        Log.info("Start addEmailSignature keyword");
        driver.findElement(OR.settings.getLocator("btnSignature")).click();

        // Get and update signature text
        driver.findElement(OR.settings.getLocatorDynamic("optSetting", new String[]{email})).click();
        driver.Wait(5);
        driver.findElement(OR.settings.getLocator("tbSignatures")).click();
        driver.Wait(1);
        driver.longPressElement(OR.settings.getLocator("tbSignatures"));
        driver.Wait(1);
        // Delete old signature
        if (driver.isElementPresent(OR.common.getLocatorDynamic("btnMenuItemButton", new String[]{"Select All"}))) {
            common.clickMenuItemTextButton("Select All");
            driver.Wait(1);
            if (driver.isElementPresent(OR.common.getLocatorDynamic("btnMenuItemButton", new String[]{"Cut"}))){
                common.clickMenuItemTextButton("Cut");
                driver.Wait(1);
            }
        }
        // Input new signature
        driver.findElement(OR.settings.getLocator("tbSignatures")).sendKeys(signature);
        common.clickElementButton("Done");
        common.clickElementButton("Email");
        Log.info("Ended addEmailSignature keyword");
    }

    /**
     * Switch ON/OFF toggle "Notify if NO reply" in Settings > Emails (Bear Track)
     * @param isNotify (true: On / false: Off)
     */
    public void setBearTrackNoReply(boolean isNotify){
        Log.info("Started setBearTrackNoReply keyword");
        String toggle = "Notify if NO reply";
        // Find and check status of toggle
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isNotify == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Emails > "+toggle+" = "+isNotify);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Emails > "+toggle+" = "+isNotify);
        }
        Log.info("Ended setBearTrackNoReply keyword");
    }
    /**
     * Switch ON/OFF toggle "Notify upon reply" in Settings > Emails (Bear Track)
     * @param isNotify (true: On / false: Off)
     */
    public void setBearTrackUponReply(boolean isNotify){
        Log.info("Started setBearTrackUponReply keyword");
        String toggle = "Notify upon reply";
        // Find and check status of toggle
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isNotify == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Emails > "+toggle+" = "+isNotify);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Emails > "+toggle+" = "+isNotify);
        }
        Log.info("Ended setBearTrackUponReply keyword");
    }

    /**
     * Set Collect Emails when Sending
     * @param isShow true/false
     */
    public void setCollectEmailsWhenSending(boolean isShow){
        Log.info("Started setCollectEmailsWhenSending keyword");
        String toggle = "Collect Emails when Sending";
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isShow == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Emails > "+toggle+" = "+isShow);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Emails > "+toggle+" = "+isShow);
        }
        Log.info("Ended setCollectEmailsWhenSending keyword");
    }

    /**
     * Check Notify of Collect Emails when Sending
     * @param content
     * @param testCase
     */
    public boolean checkCollectEmailsWhenSendingNotify(String content, String testCase){
        Log.info("Started checkCollectEmailsWhenSendingNotify keyword");
        boolean flag ;
        String status;
        driver.findElement(OR.settings.getLocator("icoCollectionEmailsWhenSendingNotify")).click();
        driver.Wait(5);
        status = driver.findElement(OR.settings.getLocator("notifyCollectionEmailsWhenSending")).getAttribute("value");
        try {
            Assert.assertEquals(status,content);
            flag = true;
            Log.info("Passed - Collection Emails When Sending Notify: "+ status);
        }catch (AssertionError aE) {
            Log.info("Failed - Collection Emails When Sending Notify - " + aE.getMessage());
            driver.captureScreenShot(testCase);
            flag = false;
            }
        common.clickElementButton("OK, Got it!");
        Log.info("Ended checkCollectEmailsWhenSendingNotify keyword");
        return flag;
    }


    /*******************************
     * Settings > Calendars
     */

    /**
     *
     * Set default Event Duration time in Setting > Calendars
     * @param time: 15 mins, 30 mins, 45 mins, 1hr, 1hr 30 mins, 2 hrs, 3 hrs
     */
    public void setDefaultEventDuration(String time){
        Log.info("Started setEventDuration keyword");
        Log.info("Set Event Duration: " + time);
        driver.findElement(OR.settings.getLocator("btnDefaultEventDuration")).click();
        switch (time) {
            case "1 hr":
                time ="1 hr ";
                break;
            case "2 hrs":
                time ="2 hrs ";
                break;
            case "3 hrs":
                time ="3 hrs ";
                break;
        }
        driver.findElement(OR.settings.getLocatorDynamic("optValue", new String[]{time})).click();
        common.clickElementButton("Calendars");
        Log.info("Ended setEventDuration keyword");
    }

    /**
     * Set Week Start in Setting > Calendars
     * @param day (Sunday, Monday)
     */
    public void setWeekStart(String day){
        Log.info("Started setWeekStart keyword");
        common.clickElementButton(day);
        Log.info("Setting Week start of Calendars is "+day);
        Log.info("Ended setWeekStart keyword");
    }

    /**
     * Set Show Past Events in Collection View
     * @param isShow true/false
     */
    public void setShowPastEventInCollectionView(boolean isShow){
        Log.info("Started setShowPastEventInCollectionView keyword");
        String toggle = "Show Past Events in Collection View";
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isShow == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Calendars > "+toggle+" = "+isShow);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Calendars > "+toggle+" = "+isShow);
        }
        Log.info("Ended setShowPastEventInCollectionView keyword");
    }

    /**
     * Set All Day Event Alert time in Setting > Calendars
     * @param time: None, Date Of Start, 1 Day Before, 2 Days Before, 3 Days Before, 7 Days Before, 1 Month Before
     */
    public void setAllDayEventAlert(String time){
        Log.info("Started setAllDayEventAlert keyword");
        Log.info("Set All Day Event Alert: " + time);
        driver.findElement(OR.settings.getLocator("btnAllDayEventAlert")).click();
        driver.findElement(OR.settings.getLocatorDynamic("optValue", new String[]{time})).click();
        common.clickElementButton("Calendars");
        Log.info("Ended setAllDayEventAlert keyword");
    }

    /**
     * Set Normal Event Alert time in Setting > Calendars
     * @param time: None, Time Of Start, 15 mins, 30 mins, 1 hr
     */
    public void setNormalEventAlert(String time){
        Log.info("Started setNormalEventAlert keyword");
        Log.info("Set Normal Event Alert: " + time);
        driver.findElement(OR.settings.getLocator("btnNormalEventAlert")).click();
        driver.findElement(OR.settings.getLocatorDynamic("optValue", new String[]{time})).click();
        common.clickElementButton("Calendars");
        Log.info("Ended setNormalEventAlert keyword");
    }

    /**
     * Set set Working Hours in Setting > Calendars
     * @param date: Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
     * @param startTime: (ex: 07:00 AM) (format : hh:mm aa)
     * @param endTime: (ex: 09:00 PM) (format : hh:mm aa)
     */
    public void setWorkingHours(String date, String startTime, String endTime){
        Log.info("Started setWorkingHours keyword");

        driver.findElement(OR.settings.getLocator("btnWorkingHour")).click();
        driver.findElement(OR.settings.getLocatorDynamic("optValue",new String[]{date})).click();
        String a1 = startTime.substring(0,1);
        if(a1.equals("0")){
            a1 = startTime.substring(1,2);
        } else a1 = startTime.substring(0,2);
        String a2 = endTime.substring(0,1);
        if(a2.equals("0")){
            a2 = endTime.substring(1,2);
        } else a2 = endTime.substring(0,2);
        //Set start time
        driver.findElement(OR.settings.getLocator("optTimeHour")).setValue(a1);
        driver.findElement(OR.settings.getLocator("optTimeMinute")).setValue(startTime.substring(3,5));
        driver.findElement(OR.settings.getLocator("optTimePeriod")).setValue(startTime.substring(6));
        driver.findElement(OR.settings.getLocatorDynamic("optValue",new String[]{"Ends"})).click();
        //Set end time
        driver.findElement(OR.settings.getLocator("optTimeHour")).setValue(a2);
        driver.findElement(OR.settings.getLocator("optTimeMinute")).setValue(endTime.substring(3,5));
        driver.findElement(OR.settings.getLocator("optTimePeriod")).setValue(endTime.substring(6));
        //Click Done
        common.clickElementButton("Done");
        Log.info("Ended setWorkingHours keyword");
    }

    /**
     * check Working Hours
     * @param date: Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
     * @param startTime: (ex: 07:00 PM) (format : hh:mm aa)
     * @param endTime: (ex: 09:00 PM) (format : hh:mm aa)
     */
    public boolean checkWorkingHours(String date, String startTime, String endTime, String testCase){
        Log.info("Started checkWorkingHours keyword");
        boolean flag = false;
        String index = null;
        String workingHours = startTime+" ~ " + endTime;
        driver.Wait(4);
        driver.findElement(OR.settings.getLocator("btnWorkingHour")).click();
        switch (date){
            case "Sunday":
                index ="1";
                break;
            case "Monday":
                index ="2";
                break;
            case "Tuesday":
                index ="3";
                break;
            case "Wednesday":
                index ="4";
                break;
            case "Thursday":
                index ="5";
                break;
            case "Friday":
                index ="6";
                break;
            case "Saturday":
                index ="7";
                break;
        }
        String value = driver.findElement(OR.settings.getLocatorDynamic("timeWorkingHour",new String[]{index})).getAttribute("value");
        try {
            Assert.assertEquals(value,workingHours);
            flag = true;
            Log.info("Passed - Working hours is: "+ value);
        }catch (AssertionError aE) {
            Log.info("Failed - Working hours is - " + aE.getMessage());
            driver.captureScreenShot(testCase);
        }
        common.clickElementButton("Calendars");
        Log.info("Ended setNormalEventAlert keyword");
        return flag;
    }

    /*******************************
     * Settings > To.do
     */

    /**
     *
     * Set default duration of to.do
     * @param duration: None, Short, Medium, Long
     */
    public void setDefaultDuration(String duration){
        Log.info("Started setDefaultDuration keyword");
        if(duration.equals("None")){
            Log.info("Select Default Duration: None");
            driver.tapCoordinate(OR.settings.getCoordinate("durationNone"));
        }else if(duration.equals("Short")){
            Log.info("Select Default Duration: Short (30 mins)");
            driver.tapCoordinate(OR.settings.getCoordinate("durationShort"));
        }else if(duration.equals("Medium")){
            Log.info("Select Default Duration: Medium (2 hrs)");
            driver.tapCoordinate(OR.settings.getCoordinate("durationMedium"));
        }else if(duration.equals("Long")){
            Log.info("Select Default Duration: Long (5 hrs)");
            driver.tapCoordinate(OR.settings.getCoordinate("durationLong"));
        }
        Log.info("Back to Settings > ToDo's");
        driver.tapCoordinate(OR.settings.getCoordinate("icoBackTodo"));
        Log.info("Ended setDefaultDuration keyword");
    }

    /**
     * Set Default Due Date in Setting > To.do
     * @param value: None, Today, Tomorrow, End of Work Week , End of Month
     */
    public void setDefaultDueDate(String value){
        Log.info("Started setDefaultDueDate keyword");
        Log.info("Set Default Due Date: " + value);
        driver.findElement(OR.settings.getLocator("btnDefaultDueDate")).click();
        driver.findElement(OR.settings.getLocatorDynamic("optValue", new String[]{value})).click();
        common.clickElementButton("ToDo's");
        Log.info("Ended setDefaultDueDate keyword");
    }

    /**
     * Set Default Due Date Alert in Setting > To.do
     * @param value: None, On Due Date, 1 Day Before, 2 Days Before, 3 Days Before, 7 Days Before, 1 Month Before
     */
    public void setDefaultDueDateAlert(String value){
        Log.info("Started setDefaultDueDateAlert keyword");
        Log.info("Set option > " + value);
        driver.findElement(OR.settings.getLocator("btnDefaultDueDateAlert")).click();
        driver.findElement(OR.settings.getLocatorDynamic("optValue", new String[]{value})).click();

        // Back to list Setting > To.do's
        common.clickElementButton("ToDo's");
        Log.info("Ended setDefaultDueDateAlert keyword");
    }

    /**
     *
     * Set show Done To.do in Collection view
     * @param isShow
     */
    public void showDoneTodoCollection(boolean isShow){
        Log.info("Started showDoneTodoCollection keyword");
        String toggle = "Show Done ToDo's in Collection View";
        // Find and check status of toggle
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isShow == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > ToDo's > "+toggle+" = "+isShow);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > ToDo's > "+toggle+" = "+isShow);
        }
        Log.info("Ended showDoneTodoCollection keyword");
    }

    /**
     *
     * Set show Due To.do in Agenda view
     * @param isShow
     */
    public void showDueTodoAgenda(boolean isShow){
        Log.info("Started showDueTodoAgenda keyword");
        String toggle = "Show Due ToDo's in Agenda";
        // Find and check status of toggle
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isShow == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > ToDo's > "+toggle+" = "+isShow);
        }
        else{
            // Switch toggle
//            driver.tapCoordinate(OR.settings.getCoordinate("toggleDueToDoAgenda"));
            SwitchToggle.click();
            Log.info("Settings > ToDo's > "+toggle+" = "+isShow);
        }
        Log.info("Ended showDueTodoAgenda keyword");
    }



    /*******************************
     * Settings > Contact
     */

    /**
     *
     * Set Reimport Iphone Contact
     * @param options: Import Anyway / Import & Merge / Cancel
     */
    public void setReimportIphoneContact(String options){
        Log.info("Started setReimportIphoneContact keyword");
        driver.Wait(1);
        common.clickElementButton("Reimport");
        common.clickElementButton(options);
        Log.info("Ended setReimportIphoneContact keyword");
    }
    /**
     *
     * Set Merge Duplicate Flo Contacts
     */
    public void setMergeDuplicateFloContacts(){
        Log.info("Started setMergeDuplicateFloContacts keyword");
        driver.Wait(1);
        common.clickElementButton("Merge");
        Log.info("Ended setMergeDuplicateFloContacts keyword");
    }

    /**
     *
     * Set display order of contact list
     * @param options: Last First - show last name + first name / First Last - show first name + last name
     */
    public void setDisplayOrder(String options){
        Log.info("Started setDisplayOrder keyword");
        if(options.equals("Last First")){
            options = "Last, First";
        }
        common.clickStaticTextButton(options);
        Log.info("Setting option > "+options);
        Log.info("Ended setDisplayOrder keyword");
    }

    /**
     * Set fields will display in contact list
     * @param options: Name_Company, Name_Email, Name_Phone
     */
    public void setDisplayInList(String options){
        Log.info("Started setDisplayInList keyword");
        if(options.equalsIgnoreCase("Name_Company")){
            options = "Name and Company";
        }else if(options.equalsIgnoreCase("Name_Email")){
            options = "Name and Email";
        }else if(options.equalsIgnoreCase("Name_Phone")){
            options = "Name and Default Phone";
        }
        common.clickStaticTextButton(options);
        Log.info("Set option > "+options);
        Log.info("Ended setDisplayInList keyword");
    }

    /**
     * Set Open To.do Create From Note option in Settings > Note
     * @param isOn (true: On / false: Off)
     */
    public void setOpenTodoCreateFromNote(boolean isOn){
        Log.info("Started setOpenTodoCreateFromNote keyword");
        String toggle = "Open ToDo's created from Notes";
        MobileElement SwitchToggle = driver.findElement(OR.settings.getLocatorDynamic("SwitchToggle",new String[]{toggle}));
        String ObserveStatus = SwitchToggle.getAttribute("value");
        if(isOn == ObserveStatus.equals("1")){
            // Don't switch toggle
            Log.info("Settings > Customization > "+ toggle +" = "+ isOn);
        }
        else{
            // Switch toggle
            SwitchToggle.click();
            Log.info("Settings > Customization > "+ toggle +" = "+ isOn);
        }
        Log.info("Ended setOpenTodoCreateFromNote keyword");
    }
}
