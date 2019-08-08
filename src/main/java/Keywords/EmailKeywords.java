package Keywords;

import Environments.Constants;
import Locators.ObjectRepository;
import Support.SetupServer;
import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class EmailKeywords {
    // EmailKeywords variables
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    HomeKeywords home;

    // Logger variable
    static Logger Log = Logger.getLogger(EmailKeywords.class);

    public EmailKeywords(SetupServer driver) {
        this.driver = driver;
        this.common = new CommonKeywords(driver);
        this.home = new HomeKeywords(driver);
    }

    /**
     * Compose new Email from many locations
     * @param location: Compose Icon, Bottom Bar
     */
    public void openNewEmailView(String location){
        Log.info("Started openNewEmailView Keyword");
        // Compose new from Bottom Bar
        if(location.equalsIgnoreCase("Bottom Bar")){
            Log.info("Compose from Bottom Bar");
            common.clickViewBottomBar("Email");
        }
        // Compose New from Compose Icon
        else if(location.equalsIgnoreCase("Compose Icon")){
            Log.info("Compose from Compose Icon");
            // Clicking on "Compose" icon
            String composeIcon = "\uE04E";
            common.clickStaticTextButton(composeIcon);
            driver.Wait(2);
        }
        Log.info("Ended openNewEmailView Keyword");
    }

    /**
     * Input all information (Select From, Input To, Cc, Bcc, Subject, Content, attach Image) when user compose new Email
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param numImage
     */
    public void composeEmail(String from, String to, String cc, String bcc, String subject, String content, String numImage){
        Log.info("Started composeEmail Keyword");
        // Tap on From field
        if(!from.equals("")){
            Log.info("Select email for from to send email");
            MobileElement tbFrom = driver.findElement(OR.email.getLocator("txbFrom"));
            tbFrom.click();
            try {
                // Check email address for From to send email
                driver.isElementExist(OR.email.getLocator("selectEmailPopup"));
                try {
                    // select a email for From to send
                    driver.isElementExist(OR.email.getLocatorDynamic("selectEmail",new String[]{from}));
                    MobileElement SelectEmail = driver.findElement(OR.email.getLocatorDynamic("selectEmail",new String[]{from}));
                    SelectEmail.click();
                    Log.info("Select email address From > "+ from);
                }catch (Exception e){
                    Log.info("Email address not found");
                    MobileElement btnCancel = driver.findElement(OR.email.getLocator("optCancel"));
                    btnCancel.click();
                }
            } catch (Exception e) {
                Log.info("Flo app does not have 3rd party account");
            }
        }

        if(!to.equals("")){
            driver.Wait(3, OR.email.getLocator("txbTo"));
            // Input into TO
            MobileElement Tofield = driver.findElement(OR.email.getLocator("txbTo"));
            Tofield.click();
            // When input many recipient
            if(to.contains(",")){
                int length = to.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitTo = to.split(",")[i];
                    Log.info("Input To field > "+splitTo);
                    Tofield.setValue(splitTo);
                    Tofield.sendKeys(Keys.ENTER);
                }
            }else {
                Log.info("Input To field > "+to);
                Tofield.setValue(to);
                Tofield.sendKeys(Keys.ENTER);
            }
        }

        if(!cc.equals("") || !bcc.equals("")){
            Log.info("Expand CC & BCC fields");
            MobileElement expandrecipient = driver.findElement(OR.email.getLocator("btnExpand_Collapse"));
            expandrecipient.click();
            if(!cc.equals("")){
                // Input into CC
                MobileElement Ccfield = driver.findElement(OR.email.getLocator("txbCc"));
                if(cc.contains(",")){
                    int length = cc.split(",").length;
                    for(int i = 0; i < length; i++){
                        String splitCc = cc.split(",")[i];
                        Log.info("Input Cc field > "+splitCc);
                        Ccfield.setValue(splitCc);
                        Ccfield.sendKeys(Keys.ENTER);
                    }
                }else {
                    Log.info("Input Cc field > "+ cc);
                    Ccfield.setValue(cc);
                    Ccfield.sendKeys(Keys.ENTER);
                }
            }
            if(!bcc.equals("")){
                // Input into BCC
                MobileElement Bccfield = driver.findElement(OR.email.getLocator("txbBcc"));
                // When input many recipient
                if(bcc.contains(",")){
                    int length = bcc.split(",").length;
                    for(int i = 0; i < length; i++){
                        String splitBcc = bcc.split(",")[i];
                        Log.info("Input Bcc field > "+ splitBcc);
                        Bccfield.setValue(splitBcc);
                        Bccfield.sendKeys(Keys.ENTER);
                    }
                }else {
                    Log.info("Input Bcc field > "+ bcc );
                    Bccfield.setValue(bcc);
                    Bccfield.sendKeys(Keys.ENTER);
                }
            }
        }

        // Subject of Email
        if(!subject.equals("")){
            Log.info("Input Subject > "+subject);
            driver.Wait(2);
            MobileElement Subjectfield = driver.findElement(OR.email.getLocator("txbSubject"));
            Subjectfield.clear();
            Subjectfield.setValue(subject);
        }

        // Content of Email
        if(!content.equals("")){
            driver.Wait(4);
            driver.tapCoordinate(50,330);
            Log.info("Input Content > "+content);
            MobileElement Contentfield = driver.findElement(OR.email.getLocator("txaContent"));
            Contentfield.clear();
            Contentfield.setValue(content);
        }

        // Attach image
        if(!numImage.equals("")){
            Log.info("Attach image in Content of Email");
            attachImage(numImage);
        }
        Log.info("Ended composeEmail Keyword");
    }

    /**
     * Send email (Select From, Input To, Cc, Bcc, Subject, Content, attach Image and click Send)
     * @param from (From field) Tap on From field to show multi email address for from send email
     * @param to (To field)
     * @param cc (if CC = "", not input CC & BCC)
     * @param bcc (Bcc field)
     * @param subject (Subject field)
     * @param content (Content field)
     * @param numImage if numImage = "", not attach image to email's content. Ex: : 1, 2, 3, 4 ,5
     * @param option Add/Move
     * @param collections
     * @param isSend
     */
    public void sendEmail(String from, String to, String cc, String bcc, String subject, String content, String numImage, String option, String collections, boolean isSend) {
        Log.info("Started Send Email Keywords");

        // compose email
        composeEmail(from, to, cc, bcc, subject, content, numImage);

        // Clicking Send icon
        if(isSend == true) {
            Log.info("Click Send button");
            MobileElement SendIcon = driver.findElement(OR.email.getLocator("btnSend"));
            SendIcon.click();

            // Check show popup message after click 'Send' icon
            if(!driver.isElementPresent(OR.email.getLocator("pupAlertMessage"))){
                // Check Email Conversation View to Add/Move email to Collection
                if(driver.isElementPresent(OR.email.getLocator("EmailConversation"))){
                    // Wait for Email conversation
                    driver.Wait(10,OR.email.getLocator("EmailConversation"));
                    common.addMoveToCollection(false,option,collections);
                }
            }
        }
        // Click Cancel button
        else {
            MobileElement btnCancel = driver.findElement(OR.email.getLocator("btnCancel"));
            btnCancel.click();
        }
        Log.info("Ended Send Email Keywords");
    }

    /**
     * Check invalid email address popup
     * @param addressTo invalid address Ex: abc
     * @param message
     */
    public boolean checkInvalidAddress(String addressTo, String message, String testcase){
        Log.info("Started checkInvalidEmailAddress Keywords");
        boolean flag = true;
        try {
            // Check popup show Alert Message
            driver.isElementExist(OR.email.getLocator("pupAlertMessage"));
            Log.info("Invalid Email Address popup shows");
            try{
                String actualMess = driver.findElement(OR.email.getLocator("lblMessage")).getAttribute("value");
                String emailAddress = "\""+ addressTo +"\" " + message;
                Assert.assertEquals(actualMess, emailAddress);
                Log.info("Passed - Message shows: "+ actualMess);
            }catch (AssertionError aE){
                Log.info("Failed - Assert Message Error: " + aE.getMessage());
                flag = false;
                driver.captureScreenShot(testcase);
            }
            // Click on "OK" button
            common.clickElementButton("OK");

        }catch (Exception e){
            Log.info("Failed - Invalid Email Address popup not shows");
            driver.captureScreenShot(testcase);
            return flag;
        }
        Log.info("Ended checkInvalidEmailAddress Keywords");
        return flag;
    }

    /**
     * Save email as Draft or delete draft email
     * @param clickButton (Save, Delete or Cancel)
     */
    public void draftEmail(String clickButton){
        Log.info("Started draftEmail Keywords");
        if(clickButton.equals("Save")){
            Log.info("Save Draft Email");
            MobileElement btnSaveDraft = driver.findElement(OR.email.getLocator("optSaveDraft"));
            btnSaveDraft.click();
            driver.Wait(5);
        }
        else if (clickButton.equals("Delete")){
            Log.info("Delete Draft Email");
            MobileElement btnDeleteDraft = driver.findElement(OR.email.getLocator("optDeleteDraft"));
            btnDeleteDraft.click();
        }
        else if (clickButton.equals("Cancel")){
            Log.info("Click Cancel");
            MobileElement btnCancel = driver.findElement(OR.email.getLocator("optCancel"));
            btnCancel.click();
        }
        Log.info("Ended draftEmail Keywords");
    }

    /**
     * Attach Image to Body content of Email
     * @param numImage Select attached photo(s) based on order in Photo folder (ex: 1st Photo)
     */
    public void attachImage(String numImage){
        Log.info("Started attachImage keywords");
        // Click on Imagae icon in Content
        driver.Wait(5, OR.email.getLocator("icoImage"));
        MobileElement iconImage = driver.findElement(OR.email.getLocator("icoImage"));
        iconImage.click();

        // Allow access to Photos of iPhone
        closeAccessPhotos(true);

        // Select image in Camera Roll
        driver.Wait(5, OR.email.getLocator("cameraRoll"));
        MobileElement cameraRoll = driver.findElement(OR.email.getLocator("cameraRoll"));
        cameraRoll.click();

        // Select number of Photo: 1,2,3,4,5
        MobileElement selectPhoto = driver.findElement(OR.email.getLocatorDynamic("numPhoto",new String[]{numImage}));
        selectPhoto.click();

        // Click on "Done" button
        common.clickElementButton("Done");
        Log.info("Ended attachImage keywords");
    }

    /**
     * Close Allow Access to Photos popup (First time access to Photos of device)
     * @param isAllow (true: Allow / false: Don’t Allow)
     */
    public void closeAccessPhotos(boolean isAllow){
        Log.info("Started closeAccessPhotos keyword");
        driver.Wait(4,OR.email.getLocator("pupAccessPhotos"));
        try{
            // Check popup Allow to access Photos
            driver.isElementExist(OR.email.getLocator("pupAccessPhotos"));
            if (isAllow == true){
                Log.info("Allow to access Photos > OK");
                common.clickElementButton("OK");
            } else {
                Log.info("Not allow to access Photos > Don't Allow");
                common.clickElementButton("Don’t Allow");
            }
        }catch (Exception e){
            Log.info("Access Photos popup doesn't show");
        }
        Log.info("Ended closeAccessPhotos keyword");
    }

    /**
     * Check popup Email Empty Subject
     * @param message: This message has no subject. Do you want to send it anyway?
     * @param isSend: true - Send; false: Cancel
     * @param timeTracking:
     * @param testcase
     */
    public boolean checkEmailSubject(String message, boolean isSend, String timeTracking, String testcase){
        Log.info("Started checkEmailSubject keyword");
        boolean flag = true;
        try {
            // Check popup message is showing.
            driver.isElementExist(OR.email.getLocator("pupAlertMessage"));
            Log.info("Empty Email Subject");
            try{
                // Check content of message
                String actualMess = driver.findElement(OR.email.getLocator("lblMessage")).getAttribute("value");
                Assert.assertEquals(actualMess, message);
                Log.info("Passed - Message shows: "+ actualMess);

            }catch (AssertionError aE){
                Log.info("Failed - Assert Message Error: " + aE.getMessage());
                flag = false;
                driver.captureScreenShot(testcase);
            }
            if(isSend == true){
                Log.info("Send email without Subject");
                try {
                    // Check display 'Send and Track' button when send email by Bear Track
                    driver.isElementExist(OR.email.getLocator("btnSendAndTrack"));
                    driver.findElement(OR.email.getLocator("btnSendAndTrack")).click();
                    if(timeTracking.equals("1 hr")){
                        // select tracking time is 1 hr
                        driver.tapCoordinate(50,170);
                    }else {
                        // select others tracking time
                        driver.Wait(5,OR.common.getLocatorDynamic("btnStaticButton", new String[]{timeTracking}));
                        common.clickStaticTextButton(timeTracking);
                    }
                    // click send button
                    common.clickStaticTextButton("Send");
                }catch (Exception e){
                    // Click 'Send' when send email normal
                    driver.findElement(OR.email.getLocator("btnSend2")).click();
                }

            }else {
                Log.info("Cancel send email without Subject");
                driver.findElement(OR.email.getLocator("btnCancel2")).click();
            }
        }catch (Exception e){
            Log.info("Failed - Not show popup 'Empty Subject'");
            driver.captureScreenShot(testcase);
            return flag;
        }
        Log.info("Ended checkEmailSubject keyword");
        return flag;
    }

    /**
     *
     * @param folderName: IMAP folder to navigate Ex: All Inboxes, Flo>Inbox
     * @param emailAddress: the emailAddress in email to check
     * @param subject: Subject of email
     * @param isExist: Exist - check button exist / Not Exist - check button not exist
     * @param testcase
     * @return
     */
    public boolean checkEmailExist (String folderName, String emailAddress, String subject, String isExist, String testcase){
        Log.info("Started checkEmailExist keyword");
        driver.Wait(6);
        boolean flag = true;
        // Go to IMAP folder
        if(!folderName.equals("")){
            navigateToImapFolder(folderName);
        }

        // Check email exist or not
        String[] emailItem = {emailAddress, subject};
        if(isExist.equalsIgnoreCase("Exist")){
            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", emailItem)))
                Log.info("Passed - Email '" + emailAddress + "' and Subject '" + subject + "' is existed");
            else{
                driver.Wait(10,OR.email.getLocatorDynamic("emailItem", emailItem));
                if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", emailItem)))
                    Log.info("Passed - Email '"+ emailAddress +"' and Subject '" + subject + "' is existed");
                else {
                    Log.info("Failed - Email '" + emailAddress + "' and Subject '" + subject + "' is not existed");
                    flag = false;
                }
            }
        }
        else if(isExist.equalsIgnoreCase("Not Exist")){
            driver.Wait(5);
            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", emailItem))) {
                Log.info("Failed - Email '" + emailAddress + "' and Subject '" + subject + "' is existed");
                flag = false;
            }
            else{
                Log.info("Passed - Email '" + emailAddress + "' and Subject '" + subject + "' is not existed");
            }
        }

        // Capture screen shoot
        if(!flag)
            driver.captureScreenShot(testcase);
        Log.info("Ended checkEmailExist keyword");
        return flag;
    }

    /**
     *
     * @param subject
     * @param to
     * @param cc
     * @param bcc
     * @param isStar
     * @param content
     * @param collections
     * @param linkedItems
     * @param testcase
     */
    public boolean checkEmailItemDetail(String subject, String from, String to, String cc, String bcc, boolean isStar, String content, String collections, String linkedItems, String testcase){
        Log.info("Started checkEmailItemDetail keyword");
        boolean flag = true;
        // Check Subject of Email
        driver.touchActionSwipe(190,115,250,500);
        driver.Wait(3);
        driver.tapCoordinate(358,500);
        if(!subject.equals("")){
            try{
                String strSubject = driver.findElement(OR.email.getLocator("secSubject")).getAttribute("value");
                Assert.assertEquals(strSubject, subject);
                Log.info("Passed - Subject is "+ strSubject);
            } catch (AssertionError aE){
                Log.info("Failed - Assert Subject Error: "+aE.getMessage());
                flag = false;
            }
        }
        if(!from.equals("")|| !to.equals("") || !cc.equals("") || !bcc.equals("")){
            // Open Sender & Recipient list
            driver.findElement(OR.email.getLocator("secInfoList")).click();
            // Check email address in From field
            if(!from.equals("")){
                try{
                    String strFrom = driver.findElement(OR.email.getLocator("secFrom")).getAttribute("name");
                    Assert.assertEquals(strFrom, from);
                    Log.info("Passed - From is "+ strFrom);
                } catch (AssertionError aE){
                    Log.info("Failed - Assert From Error: "+aE.getMessage());
                    flag = false;
                }
            }
            // Check email address in To field
            if(!to.equals("")){
                if(to.equals("None")){
                    try {
                        driver.isElementExist(OR.email.getLocator("secTo"));
                        Log.info("Failed - Email has email address in To field");
                        flag = false;
                    }catch (Exception e){
                        Log.info("Passed - Email has no email address in To field");
                    }
                }else {
                    try{
                        driver.isElementExist(OR.email.getLocator("secTo"));
                        String strTo = driver.findElement(OR.email.getLocator("secTo")).getAttribute("name");
                        Assert.assertEquals(strTo, to);
                        Log.info("Passed - Email address in To field is: "+ to);
                    } catch (AssertionError aE){
                        Log.info("Failed - Assert Error: "+aE.getMessage());
                        flag = false;
                    }
                }
            }
            // Check email address in Cc field
            if(!cc.equals("")){
                if(cc.equals("None")){
                    try {
                        driver.isElementExist(OR.email.getLocator("secCc"));
                        Log.info("Failed - Email has email address in Cc field");
                        flag = false;
                    }catch (Exception e){
                        Log.info("Passed - Email has no email address in Cc field");
                    }
                }else {
                    try{
                        driver.isElementExist(OR.email.getLocator("secTo"));
                        try {
                            driver.isElementExist(OR.email.getLocator("secCc"));
                            String strCc = driver.findElement(OR.email.getLocator("secCc")).getAttribute("name");
                            Assert.assertEquals(strCc, cc);
                            Log.info("Passed - Email address in Cc field is: "+ cc);
                        }catch (AssertionError aE){
                            Log.info("Failed - Assert Error: "+aE.getMessage());
                            flag = false;
                        }
                    } catch (Exception e){
                        try {
                            driver.isElementExist(OR.email.getLocator("secOnlyCc"));
                            String strCc = driver.findElement(OR.email.getLocator("secOnlyCc")).getAttribute("name");
                            Assert.assertEquals(strCc, cc);
                            Log.info("Passed - Email address in Cc field is: "+ cc);
                        }catch (AssertionError aE){
                            Log.info("Failed - Assert Error: "+aE.getMessage());
                            flag = false;
                        }
                    }
                }
            }
            // Check email address in Bcc field
            if(!bcc.equals("")){
                if(cc.equals("None")){
                    try {
                        driver.isElementExist(OR.email.getLocator("secBcc"));
                        Log.info("Failed - Email has email address in Bcc field");
                        flag = false;
                    }catch (Exception e){
                        Log.info("Passed - Email has no email address in Bcc field");
                    }
                }else {
                    try{
                        driver.isElementExist(OR.email.getLocator("secTo"));
                        try {
                            driver.isElementExist(OR.email.getLocator("secBcc"));
                            String strBcc = driver.findElement(OR.email.getLocator("secBcc")).getAttribute("name");
                            Assert.assertEquals(strBcc, bcc);
                            Log.info("Passed - Email address in Bcc field is: "+ bcc);
                        }catch (AssertionError aE){
                            Log.info("Failed - Assert Error: "+aE.getMessage());
                            flag = false;
                        }
                    } catch (Exception e){
                        try {
                            driver.isElementExist(OR.email.getLocator("secOnlyBcc"));
                            String strBcc = driver.findElement(OR.email.getLocator("secOnlyBcc")).getAttribute("name");
                            Assert.assertEquals(strBcc, bcc);
                            Log.info("Passed - Email address in Bcc field is: "+ bcc);
                        }catch (AssertionError aE){
                            Log.info("Failed - Assert Error: "+aE.getMessage());
                            flag = false;
                        }
                    }
                }
            }
            // Close popup Sender & Recipient list
//            driver.findElement(OR.email.getLocator("secInfoList")).click();
            driver.tapCoordinate(200,560);

        }
        // Check Star icon
        String strStar = driver.findElement(OR.email.getLocator("icoStarInDetail")).getAttribute("name");
        if(isStar == true){
            try {
                Assert.assertEquals(strStar, "\ue075");
                Log.info("Passed - Email is Star");
            }catch (AssertionError e){
                Log.info("Failed - Email is Unstar " + e.getMessage());
                flag = false;
            }
        }else {
            try {
                Assert.assertEquals(strStar, "\ue074");
                Log.info("Passed - Email is Unstar");
            }catch (AssertionError e){
                Log.info("Failed - Email is Star " + e.getMessage());
                flag = false;
            }
        }

        // Check Email's content
        if(!content.equals("")){
            try {
                driver.isElementExist(OR.email.getLocator("secContent"));
                String strContent = driver.findElement(OR.email.getLocator("secContent")).getAttribute("name");
                Assert.assertEquals(strContent, content);
                Log.info("Passed - Content of Email is: "+ content);
            }catch (AssertionError aE){
                Log.info("Failed - Assert Error: "+aE.getMessage());
                flag = false;
            }
        }

        // Check Collection(s) in Collection View
        if(!collections.equals("")){
            // None Collection
            if(collections.equals("None")){
                try {
                    driver.isElementExist(OR.email.getLocator("itmNoneCollection"));
                    Log.info("Passed - Email is None Collection");
                } catch (Exception e){
                    Log.info("Failed - Email is not None Collection");
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
                        driver.isElementExist(OR.email.getLocatorDynamic("itmcollection", new String[]{splitCollection}));
                        Log.info("Passed - Email belongs Collection "+splitCollection);
                    }catch (Exception e){
                        Log.info("Failed - Email not belong Collection "+splitCollection);
                        flag = false;
                    }
                }
            }
            // Check a Collection in Collection field
            else {
                // Convert String collections, get space in text
                String convertCollection = collections;
                try {
                    driver.isElementExist(OR.email.getLocatorDynamic("itmcollection", new String[]{convertCollection}));
                    Log.info("Passed - Email belongs Collection " + collections);
                } catch (Exception e) {
                    Log.info("Failed - Email not belong Collection " + collections);
                    flag = false;
                }
            }
        }

        // Check Items(s)in Links section
        if(!linkedItems.equals("")){
            if (linkedItems.equals("None")) {
                try {
                    driver.isElementExist(OR.email.getLocator("txbItemLinked"));
                    Log.info("Failed - Email is linked others");
                    flag = false;
                } catch (Exception e) {
                    Log.info("Passed - Email is not linked others");
                }
            } else if (linkedItems.contains(",")) {
                // Count length of String Linked Items
                int length = linkedItems.split(",").length;
                for (int i = 0; i < length; i++) {
                    String splitLinkedItems = linkedItems.split(",")[i];
                    try {
                        driver.isElementExist(OR.email.getLocatorDynamic("txbLink", new String[]{splitLinkedItems}));
                        Log.info("Passed - Email is linked with: " + splitLinkedItems);
                    } catch (Exception e) {
                        Log.info("Failed - Email does not link with: " + splitLinkedItems);
                        flag = false;
                    }
                }
            } else {
                try {
                    driver.isElementExist(OR.email.getLocatorDynamic("txbLink", new String[]{linkedItems}));
                    Log.info("Passed - Email is linked with: " + linkedItems);
                } catch (Exception e) {
                    Log.info("Failed - Email does not link with: " + linkedItems);
                    flag = false;
                }
            }
        }
        if(flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkEmailItemDetail keyword");
        return flag;
    }

    /**
     *
     */
    public void checkEmailErrorMessage(){
        Log.info("Started Send Email Keywords");
        // Todo
        Log.info("Ended Send Email Keywords");
    }

    /**
     * Filter Email By Type
//     * @param from (Top: filter by type from Navigation Bar/ Bottom: filter by type from Bottom Bar)
     * @param type (All, VIP, Star, Unread)
     */
    public void filterByType(String type){
        Log.info("Started filterByType keywords");
        if(type.equalsIgnoreCase("Vip")){
            type = "VIP";
        }
        driver.Wait(3, OR.email.getLocator("btnFilterbyType"));
        MobileElement btnFilter = driver.findElement(5,OR.email.getLocator("btnFilterbyType"));
        btnFilter.click();
        driver.Wait(1);
        // Update Sometimes click at first time not action, click 2 times
//        if(!driver.isElementPresent(OR.email.getLocatorDynamic("typeFilterTop",new String[]{type}))){
//            driver.findElement(OR.email.getLocator("btnFilterbyType")).click();
//        }
        Log.info("Select filter > "+type);
        driver.findElement(OR.email.getLocatorDynamic("typeFilterTop",new String[]{type})).click();
        boolean status = driver.findElement(5,OR.common.getLocatorDynamic("btnElementButton",new String[]{"Done"})).isEnabled();
        if(status){
            common.clickElementButton("Done");
        }else
            driver.tapCoordinate(350,40);
        Log.info("Ended filterByType keywords > "+ type);
    }

    /**
     * Check email is trashed
     * @param emailAddress
     * @param subject (Subject Email)
     * @param testCase
     * @return
     */
    public boolean checkEmailTrashed(String emailAddress, String subject, String testCase){
        boolean flag = true;
        Log.info("Start checkEmailTrashed keyword");
        return flag;
    }

    /**
     * Scroll up Imap Folder list until folder can click
     * @param folderName
     */
    public void scrollUpImapFolder(String folderName){
        // Todo scrollUpImapFolder
    }

    /**
     * Navigate to Imap Folder
     * @param folderName: Name of folder Ex: All Inboxes, All Sent...; Ex: Flo>Inbox
     */
    public void navigateToImapFolder(String folderName){
        Log.info("Started navigateToImapFolder keyword > " + folderName);
        // Open Local Filter in Email Screen
        driver.Wait(2);
        driver.findElement(OR.email.getLocator("btnFilterbyType")).click();

        driver.Wait(2);
        if(folderName.contains(">")){
            String account = folderName.split(">")[0];
            String folder = folderName.split(">")[1];
            if(driver.isElementPresent(OR.email.getLocator("iconLower"))){
                // collapse all folders are belonged to an Account (for case have 3rd party account)
                driver.findElement(OR.email.getLocator("iconLower")).click();
            }
            if(!driver.isElementPresent(OR.email.getLocatorDynamic("btnLowerItem", new String[]{account}))){
                // expand all folders are belonged to an Account
                driver.findElement(OR.email.getLocatorDynamic("btnExpandItem", new String[]{account})).click();
            }
            Log.info("Open IMAP folder: "+folderName);
            driver.Wait(2);
            driver.findElement(OR.email.getLocatorDynamic("imapFolder",new String[]{folder})).click();
        }else {
            // Use Dynamic Xpath to get Name of Folder
            driver.Wait(4);
            Log.info("Open IMAP folder: "+folderName);
            driver.findElement(5,OR.email.getLocatorDynamic("imapFolder",new String[]{folderName})).click();
        }

        // Check Button 'Done' is enabled
        boolean statusBtnDone = driver.findElement(OR.common.getLocatorDynamic("btnElementButton",new String[]{"Done"})).isEnabled();
        if(statusBtnDone){
            common.clickElementButton("Done");
        }else
            driver.tapCoordinate(350,40);
        Log.info("Ended navigateToImapFolder keyword");
    }

    /**
     * Open a folder in IMAP then check folder screen is opened
     * @param folderName: All Inboxes; All Sent; All Drafts; All Trash; Flo>Inbox; Flo>Sent...
     */
    public boolean checkImapFolderScreen(String folderName, String testcase){
        Log.info("Started checkImapFolderScreen keyword");
        boolean flag = true;
        String FolderName = "     "+folderName;
        MobileElement localFilterEmail = driver.findElement(OR.email.getLocator("secLocalFilter"));
        String actualText = localFilterEmail.getAttribute("name");
        try{
            Assert.assertEquals(actualText,FolderName);
            Log.info("Passed - IMAP folder of Email view is: "+folderName);
        }catch (AssertionError aE){
            Log.info("Failed - Error IMAP folders: "+aE.getMessage());
            flag = false;
            return flag;
        }
        // Open Local filter
        localFilterEmail.click();
        // Check IMAP folder is selected
        if(driver.isElementPresent(OR.email.getLocatorDynamic("itmImapTicked",new String[]{folderName}))){
            Log.info("Passed - IMAP folder is selected: "+folderName);
        }else {
            Log.info("Failed - IMAP folder is not selected "+folderName);
            flag = false;
        }
        if(flag == false)
            driver.captureScreenShot(testcase);
        // close local filter
        driver.tapCoordinate(333,425);
        Log.info("Ended checkImapFolderScreen keyword");
        return flag;
    }

    /**
     * get Number Emails of Imap Folder
     * @param folderName
     */
    public void getNumberEmailsFolder(String folderName){
        // Todo getNumberEmailsFolder
    }

    /**
     * Open detail of Email from list in Email view
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A, No Recipient
     * @param subject: Subject of email
     */
    public void openEmailItem(String emailAddress, String subject){
        Log.info("Started openEmailDetail keyword");
        String[] email = {emailAddress,subject};
        // Waiting email show in List
        driver.Wait(25);
        if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem",email))){
            Log.info("Open detail email has address '"+emailAddress+"' - Subject '"+subject+"'");
            driver.findElement(5,OR.email.getLocatorDynamic("emailItem",email)).click();
          }else{
            Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"' to open detail");
            driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"' to open detail");
        }
        Log.info("Ended openEmailDetail keyword");
    }

    /**
     * Send email by BearTrack (Select From, Input To, Cc, Bcc, Subject, Content, attach Image and click BearTrack)
     * @param from (From field) Tap on From field to show multi email address for from send email
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param numImage: 1, 2, 3, 4 ,5
     * @param isSendBT
     * @param timeTracking: 1 hr, 2 hrs, 5 hrs; 1 day, 2 days; 3 days, 5 days, 10 days, 30 days
     * @param option
     * @param collections
     */
    public void sendEmailByBearTrack(String from, String to, String cc, String bcc, String subject, String content, String numImage, boolean isSendBT, String timeTracking, String option, String collections){
        Log.info("Started SendEmailByBearTrack Keyword");
        // compose email
        composeEmail(from,to,cc,bcc,subject,content,numImage);
        // Clicking BearTrack icon
        if(isSendBT == true) {
            Log.info("Click Bear Track icon");
            MobileElement bearTrackIcon = driver.findElement(OR.email.getLocator("btnBearTrack"));
            bearTrackIcon.click();
            // Close popup Send Notification
            common.closeSendNotification(true);
            // Select time to tracking email.
            try {
                // Check show Tracking time list
                driver.isElementExist(OR.email.getLocator("pupBearTrack"));
                Log.info("Remind me if I don't get a reply within: "+timeTracking);
                if(timeTracking.equals("1 hr")){
                    // Select tracking time is 1hr
                    driver.tapCoordinate(50,170);
                }else {
//                    driver.Wait(5,OR.common.getLocatorDynamic("btnStaticButton", new String[]{timeTracking}));
                    common.clickStaticTextButton(timeTracking);
                }
                // Clicking Send button
                common.clickStaticTextButton("Send");
            }catch (Exception e){
                Log.info("Not show Tracking time list");
            }
            // Check show popup message after click 'BearTrack' icon
            if(!driver.isElementPresent(OR.email.getLocator("pupAlertMessage"))){
                // Check Email Conversation View to Add/Move email to Collection
                if(driver.isElementPresent(OR.email.getLocator("EmailConversation"))){
                    // Wait for Email conversation
                    driver.Wait(10,OR.email.getLocator("EmailConversation"));
                    common.addMoveToCollection(false,option,collections);
                }
            }
        }else {
            MobileElement btnCancel = driver.findElement(OR.email.getLocator("btnCancel"));
            btnCancel.click();
        }
        Log.info("Ended SendEmailByBearTrack Keyword");
    }

    /**
     * Check New Email View
     * @param from
     * @param signature
     * @param testCase
     */
    public boolean checkNewEmailView(String from , String signature, String testCase){
        Log.info("Started checkNewEmailView keyword");
        boolean flag = true;
//        driver.Wait(30,OR.email.getLocator("txaContent"));
//        MobileElement Subjectfield = driver.findElement(OR.email.getLocator("txaContent"));
//        Subjectfield.click();
        driver.tapCoordinate(150,200);
        //Check default from
        if(!from.equals(""))
        {
            String defaultFrom = driver.findElement(OR.email.getLocator("infoFrom")).getAttribute("value");
            try {
                Assert.assertEquals(defaultFrom,from);
                Log.info("Passed - From Email is "+ defaultFrom + " as expected");
            }
            catch (AssertionError aE){
                Log.info("Show The Sender incorrect: " + aE.getMessage());
                Log.info("Failed - The Sender is "+ defaultFrom );
                flag = false;
            }
        }
        if(!signature.equals(""))
        {
            signature = "\n\n"+signature;
            String defaultSignature = driver.findElement(OR.email.getLocator("txaContent")).getAttribute("value");
            try {
                Assert.assertEquals(defaultSignature,signature);
                Log.info("Passed - Signature is "+ defaultSignature + " as expected");
            }
            catch (AssertionError aE){
                Log.info("Show The Signature  incorrect: " + aE.getMessage());
                Log.info("Failed - The Signature Email is "+ defaultSignature );
                flag = false;
            }
        }
        if(flag == false){
            driver.captureScreenShot(testCase);
        }
        common.clickElementButton("Cancel");
        Log.info("Ended checkNewEmailView keyword");
        return flag;
    }

    /**
     * Check Collection Is Available When Sending
     * @return boolean true/false
     */
    public boolean checkCollectionIsAvailableWhenSending(){
        Log.info("Started checkCollectionIsAvailableWhenSending Keywords");
        boolean flag = false;
        driver.findElement(OR.email.getLocator("txbTo")).sendKeys(Constants.USERNAME+Constants.MAIL_SERVER,Keys.ENTER);
        driver.Wait(1);
        driver.findElement(OR.email.getLocator("txbSubject")).setValue("checkCollectionIsAvailableWhenSending");
        driver.Wait(1);
        // tap on Content field
        driver.tapCoordinate(50,330);
        driver.findElement(OR.email.getLocator("txaContent")).setValue("Content");
        driver.Wait(1);
        driver.findElement(OR.email.getLocator("btnSend")).click();
        driver.Wait(10,OR.common.getLocatorDynamic("btnStaticButton", new String[]{"Flo Collections"}));
        if(driver.isElementPresent(OR.common.getLocatorDynamic("btnStaticButton", new String[]{"Flo Collections"}))){
            flag = true;
            Log.info("Collection is Available When Sending Email");
            common.clickElementButton("Cancel");
        }else {
            flag = false;
            Log.info("Collection is Not Available When Sending Email");
        }
        driver.Wait(3);
        Log.info("Ended checkCollectionIsAvailableWhenSending Keywords");
        return flag;
    }

    /**
     * Delete Email by Swipe
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     * @param isOK :true/false
     */
    public void deleteEmailBySwipe(String emailAddress, String subject, boolean isOK){
        Log.info("Started deleteEmailBySwipe keyword");
        MobileElement emailItem;

        if(emailAddress.equals("")) {
            driver.Wait(10, OR.email.getLocatorDynamic("itmEmail", new String[]{subject}));
            emailItem = driver.findElement(5,OR.email.getLocatorDynamic("itmEmail", new String[]{subject}));
        }
        else if(subject.equals("")) {
            driver.Wait(10, OR.email.getLocatorDynamic("itmEmail", new String[]{emailAddress}));
            emailItem = driver.findElement(5,OR.email.getLocatorDynamic("itmEmail", new String[]{emailAddress}));
        }
        else {
            String[] email = {emailAddress, subject};
            driver.Wait(10, OR.email.getLocatorDynamic("emailItem", email));
            emailItem = driver.findElement(5,OR.email.getLocatorDynamic("emailItem", email));
        }
        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        X = X + 340;
        Y = Y + 15;

        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;

        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        Log.info("Swipe left to right on email '"+ subject+ "' from "+ emailAddress);
        driver.touchActionSwipe(swipeRightToLeftToDo);

        Log.info("Clicking Trash icon");
        driver.tapElement(OR.email.getLocator("icoTrash"));
        // Click Ok on popup
        if (isOK == true) {
            if(driver.isElementPresent(OR.common.getLocatorDynamic("btnElementButton", new String[]{"OK"})))
            common.clickElementButton("OK");
        }
        // Wait for delete item
        driver.Wait(1);
        Log.info("Ended deleteEmailBySwipe keyword");
    }

    /**
     *
     * Open move/add to collection by swiping on an to.do and click on Collection icon
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     */
    public void openCollectionBySwipe(String emailAddress, String subject){
        Log.info("Started openCollectionBySwipe keyword");
        String[] email = {emailAddress,subject};
        driver.Wait(10,OR.email.getLocatorDynamic("emailItem",email));
        MobileElement emailItem = driver.findElement(5,OR.email.getLocatorDynamic("emailItem", email));

        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};

        // Action swipe right to left on Email
        Log.info("Swipe right to left on Email: "+subject+" From "+ emailAddress);
        driver.touchActionSwipe(swipeRightToLeftToDo);
        Log.info("Clicking Collection icon");
        driver.tapElement(OR.email.getLocator("icoCollection"));
        Log.info("Ended openCollectionBySwipe keyword");
    }

    /**
     * Move email to other Imap Folder
     * @param account: only work with Flo, Gmail
     * @param folder: Inbox, [Gmail]/All Mail, [Gmail]/Drafts
     * @param isMove: true: OK /false: Cancel
     */
    public void moveToImapFolder(String account, String folder, boolean isMove){
        Log.info("Started moveToImapFolder keyword");
        driver.Wait(5);
        // Close Flo's IMAP Folder
        driver.findElement(OR.common.getLocator("iconLower")).click();

        driver.Wait(3);
        common.expandItem(account);

        driver.Wait(10,OR.email.getLocatorDynamic("imapFolder",new String[]{folder}));
        Log.info("Move Email to '"+folder+"' folder of "+account+" account");
        MobileElement IMAPFolder =  driver.findElement(OR.email.getLocatorDynamic("imapFolder",new String[]{folder}));
        IMAPFolder.click();

        driver.Wait(3);
        if(isMove == true)
            common.clickElementButton("OK");
        else
            common.clickElementButton("Cancel");
        Log.info("Ended moveToImapFolder keyword");
    }

    /**
     *
     * Move email to other Imap Folder by swiping.
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     * @param account : (Flo, Gmail, Yahoo, ...)
     * @param folder: (Inbox, [Gmail]/All Mail, [Gmail]/Drafts )
     * @param isMove : true: OK /false: Cancel, then back to
     *
     */
    public void moveEmailToOtherImapFolderBySwipe(String emailAddress, String subject, String account, String folder, boolean isMove){
        Log.info("Started moveEmailToOtherImapFolderBySwipe keyword");
        String[] email = {emailAddress,subject};
        driver.Wait(10);
        MobileElement emailItem = null ;
        for(int i =0 ; i<3 ; i++){
            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", email))){
                emailItem = driver.findElement(OR.email.getLocatorDynamic("emailItem", email));
                Log.info("Found email has address '"+emailAddress+"' - Subject '"+subject+"'");
                break;
            }else{
                driver.Wait(1);
            }
            if(i==2){
                Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
                driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
            }
        }
        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 =Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};

        // Action swipe right to left on Email
        Log.info("Swipe right to left on Email: "+subject+" From "+ emailAddress);
        driver.touchActionSwipe(swipeRightToLeftToDo);

        Log.info("Clicking IMAP Folder icon");
        driver.tapElement(OR.email.getLocator("icoFolder"));

        moveToImapFolder(account,folder,isMove);
        Log.info("Ended moveEmailToOtherImapFolderBySwipe keyword");
    }

    /**
     *
     * Create To do From Email
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     * @param dueDateTodo : due Date of To.do
     * @param collectionTodo : collection of To.do
     */
    public void addNewTodoBySwipe(String emailAddress, String subject, String dueDateTodo, String collectionTodo ){
        Log.info("Started addNewTodoBySwipe keyword");

        String[] email = {emailAddress,subject};
        driver.Wait(10);
//        MobileElement emailItem = null ;
//        for(int i =0 ; i<3 ; i++){
//            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", email))){
//                emailItem = driver.findElement(OR.email.getLocatorDynamic("emailItem", email));
//                Log.info("Found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                break;
//            }else{
//                driver.Wait(2);
//                Log.info(i);
//            }
//            if(i==2){
//                Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//            }
//        }
        MobileElement emailItem = driver.findElement(5,OR.email.getLocatorDynamic("emailItem", email));
        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};

        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on Email: "+subject + " From " + emailAddress );
        driver.touchActionSwipe(swipeLeftToRightToDo);

        Log.info("Clicking Todo icon");
        driver.tapElement(OR.email.getLocator("icoToDo"));

        Log.info("Select Due Date: "+dueDateTodo);
        try{
            driver.isElementExist(OR.email.getLocatorDynamic("icoCheck",new String[]{dueDateTodo}));
        }
        catch (Exception e){
            driver.findElement(OR.email.getLocatorDynamic("optTodoView",new String[]{dueDateTodo})).click();
        }

        Log.info("Select Collection "+ collectionTodo);
        try{
            driver.isElementExist(OR.email.getLocatorDynamic("icoCheck",new String[]{collectionTodo}));
        }
        catch (Exception e){
            Log.info("Click Collection "+ collectionTodo);
            driver.findElement(OR.email.getLocatorDynamic("optTodoView",new String[]{collectionTodo})).click();
        }

        common.clickStaticTextButton("Create ToDo");
        Log.info("Ended addNewTodoBySwipe keyword");
    }

    /**
     *
     * Open "Create New Event" Screen From Email By Swipe
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     */
    public void addNewEventBySwipe(String emailAddress, String subject){
        Log.info("Started addNewEventBySwipe keyword");
        String[] email = {emailAddress,subject};
        driver.Wait(10, OR.email.getLocatorDynamic("emailItem",email));

//        MobileElement emailItem = null ;
//        for(int i =0 ; i<3 ; i++){
//            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", email))){
//                emailItem = driver.findElement(OR.email.getLocatorDynamic("emailItem", email));
//                Log.info("Found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                break;
//            }else{
//                driver.Wait(2);
//            }
//            if(i==2){
//                Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//            }
//        }
        MobileElement emailItem = driver.findElement(5,OR.email.getLocatorDynamic("emailItem", email));
        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};
        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on Email: "+subject + " From " + emailAddress );
        driver.touchActionSwipe(swipeLeftToRightToDo);
        Log.info("Clicking Event icon");
        driver.tapElement(OR.email.getLocator("icoEvent"));
        // Create new Event
        driver.Wait(2);

        Log.info("Ended addNewEventBySwipe keyword");
    }

    /**
     *
     * Forward email by swipe
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     * @param emailaddress receiver
     * @param content content email
     */
    public void forwardEmailBySwipe(String emailAddress, String subject, String emailaddress, String content){
        Log.info("Started forwardEmailBySwipe keyword");
        String[] email = {emailAddress,subject};
        driver.Wait(10,OR.email.getLocatorDynamic("emailItem",email));
//        MobileElement emailItem = null ;
//        for(int i =0 ; i<3 ; i++){
//            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", email))){
//                emailItem = driver.findElement(OR.email.getLocatorDynamic("emailItem", email));
//                Log.info("Found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                break;
//            }else{
//                driver.Wait(2);
//            }
//            if(i==2){
//                Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//            }
//        }
        MobileElement emailItem = driver.findElement(5,OR.email.getLocatorDynamic("emailItem", email));
        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};

        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on Email: "+subject + " From " + emailAddress );
        driver.touchActionSwipe(swipeLeftToRightToDo);

        Log.info("Clicking Forward icon");
        driver.tapElement(OR.email.getLocator("icoForward"));

        // Load to compose email
        driver.Wait(5);
        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,315);

        // Input info and send mail
        sendEmail("",emailaddress,"","","",content,"","Add","General",true);

        Log.info("Ended forwardEmailBySwipe keyword");
    }
    /**
     *
     * Reply email by swipe
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     * @param content content email
     */
    public void replyEmailBySwipe(String emailAddress, String subject, String content){
        Log.info("Started replyEmailBySwipe keyword");
        String[] email = {emailAddress,subject};
        driver.Wait(10, OR.email.getLocatorDynamic("emailItem",email));

        // Swipe left to right on Email
//        MobileElement emailItem = null ;
//        for(int i =0 ; i<3 ; i++){
//            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", email))){
//                emailItem = driver.findElement(OR.email.getLocatorDynamic("emailItem", email));
//                Log.info("Found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                break;
//            }else{
//                driver.Wait(2);
//            }
//            if(i==2){
//                Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//            }
//        }
        MobileElement emailItem = driver.findElement(5,OR.email.getLocatorDynamic("emailItem", email));
        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};

        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on Email: "+subject + "From " + emailAddress );
        driver.touchActionSwipe(swipeLeftToRightToDo);

        Log.info("Clicking Reply icon");
        driver.tapElement(OR.email.getLocator("icoReply"));
        driver.Wait(2);

        // Action to show header of Compose Email view
 //       driver.touchActionSwipe(190,115,0,200);

        // send Email
        sendEmail("","","","","",content,"","Add","General",true);

        Log.info("Ended replyEmailBySwipe keyword");
    }
    /**
     *
     * Reply All email by swipe
     * @param emailAddress: Email address or contact's name, Ex: abc@gmail.com or Contact A
     * @param subject: Subject of email
     * @param content content email
     */
    public void replyAllEmailBySwipe(String emailAddress, String subject, String content){
        Log.info("Started replyAllEmailBySwipe keyword");
        String[] email = {emailAddress, subject};
        driver.Wait(10, OR.email.getLocatorDynamic("emailItem",email));

//        MobileElement emailItem = null ;
//        for(int i =0 ; i<3 ; i++){
//            if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem", email))){
//                emailItem = driver.findElement(OR.email.getLocatorDynamic("emailItem", email));
//                Log.info("Found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                break;
//            }else{
//                driver.Wait(2);
//            }
//            if(i==2){
//                Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//                driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"'");
//            }
//        }
        MobileElement emailItem = driver.findElement(5,OR.email.getLocatorDynamic("emailItem", email));
        int X = emailItem.getLocation().getX(); //0
        int Y = emailItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X, Y, X1, Y1};
        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on Email: "+subject + " From " + emailAddress );
        driver.touchActionSwipe(swipeLeftToRightToDo);
        Log.info("Clicking Reply All icon");
        driver.tapElement(OR.email.getLocator("icoReplyAll"));
        driver.Wait(5);

        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,0,300);
        // Crate new Event
        sendEmail("",emailAddress,"","","",content,"","Add","General",true);

        Log.info("Ended replyAllEmailBySwipe keyword");
    }

    /**
     * Mark Star or Un-Star an Email in detail
     * @param emailAddress
     * @param subject
     * @param isStar True: Mark Star / False: Un-Star
     */
    public void markStarEmail(String emailAddress, String subject, boolean isStar){
        Log.info("Started markStarEmail keyword");
        String [] emailItem = {emailAddress,subject};
        Log.info("Mark Star or Un-Star Email from detail");
        driver.Wait(30,OR.email.getLocatorDynamic("emailItem",emailItem));
        try{
            driver.isElementExist(OR.email.getLocatorDynamic("emailItem",emailItem));
            // Open email detail
            driver.findElement(OR.email.getLocatorDynamic("emailItem",emailItem)).click();
            String strStar = driver.findElement(OR.email.getLocator("icoStarInDetail")).getAttribute("name");
            if(isStar == true){
                if(strStar.equals("\uE074")){
                    Log.info("Mark Star Email");
                    driver.findElement(OR.email.getLocator("icoStarInDetail")).click();
                }else {
                    Log.info("Email is marked as Star");
                }
            }else {
                if(strStar.equals("\uE075")){
                    Log.info("Un-Star Email");
                    driver.findElement(OR.email.getLocator("icoStarInDetail")).click();
                }else {
                    Log.info("Email is not marked as Star");
                }
            }
                // Back to email list
            common.clickElementButton("Emails");
            }catch (Exception e){
                Log.info("Not found email to open detail");
            }
        Log.info("Ended markStarEmail keyword");
    }

    /**
     * Mark Star or Un-Star an Email in list view
     * @param emailAddress
     * @param subject
     */
    public void clickStarInListView(String emailAddress, String subject){
        Log.info("Started clickStarInListView keyword");
        String[] email = {emailAddress,subject};
        driver.Wait(15);
        if(driver.isElementPresent(OR.email.getLocatorDynamic("emailItem",email))){
            Log.info("click star icon to mark star or unmark  star "+emailAddress+"' - Subject '"+subject+"'");
            driver.findElement(OR.email.getLocatorDynamic("icoStarredInList",email)).click();
        }else{
            Log.info("Not found email has address '"+emailAddress+"' - Subject '"+subject+"' in List");
            driver.captureScreenShot("Not found email has address '"+emailAddress+"' - Subject '"+subject+"' in List");
        }
        Log.info("Ended clickStarInListView keyword");
    }

    /**
     * Add New Contact from email's detail
     * @param emailAddress: Email address
     * @param addTo: Vip Contact, New Contact, Existing Contact
     * @param first
     * @param last
     * @param company
     * @param Phone
     * @param isVip
     * @param isDone
     */
    public void addNewContactFromDetail(String emailAddress, String addTo, String first, String last, String company, String Phone, boolean isVip, boolean isDone){
        Log.info("Started addNewContactFromDetail keyword");
        // Tap on Recipients and Sender Section
        driver.findElement(OR.email.getLocator("secInfoList")).click();
        Log.info("Select email address '"+emailAddress+"'");
        driver.findElement(OR.email.getLocatorDynamic("selEmailAddress",new String[]{emailAddress})).click();
        switch (addTo){
            case "Vip Contact":
                addTo = "Create New VIP Contact";
                break;
            case "New Contact":
                addTo = "Create New Contact";
                break;
            case "Existing Contact":
                addTo = "Add To Existing Contact";
                break;
            case "Cancel":
                addTo = "Cancel";
                break;

        }
        common.clickElementButton(addTo);
        driver.Wait(3);
        if(addTo.equals("Cancel")){
            Log.info("Cancel add '"+emailAddress+"' to Flo's Contact");
        }else if (addTo.contains("Existing")){
            Log.info("Add email address '"+emailAddress+"' to Existing Flo's Contact");
            String[]nameContact = {first,last};
            driver.Wait(10,OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            try {
                Log.info("Add To Existing Contact: "+first+" "+last);
                // Display 'First Last'
                driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
                driver.findElement(OR.contact.getLocatorDynamic("txtFirstLast",nameContact)).click();
                common.clickElementButton("Done");
            }catch (Exception e){
                try {
                    // Display 'Last, First'
                    driver.isElementExist(OR.contact.getLocatorDynamic("txtLastFirst",nameContact));
                    driver.findElement(OR.contact.getLocatorDynamic("nameLastFirst",nameContact)).click();
                    common.clickElementButton("Done");
                }catch (Exception e1){
                    Log.info("Not found Contact: "+first+" "+last);
                    driver.findElement(OR.email.getLocator("btnCancel")).clear();
                }
            }
        }else {
            Log.info(addTo+" for email address '"+emailAddress+"'");
            // Input First Name
            if(!first.equals("")){
                Log.info("Input First Name: "+first);
                MobileElement tbFirst = driver.findElement(OR.contact.getLocator("tbFirstName"));
                tbFirst.clear();
                tbFirst.setValue(first);
            }
            // Input Last Name
            if(!last.equals("")){
                Log.info("Input Last Name: "+last);
                MobileElement tbLast = driver.findElement(OR.contact.getLocator("tbLastName"));
                tbLast.clear();
                tbLast.setValue(last);
            }
            // Input Company
            if(!company.equals("")){
                Log.info("Input Company: "+company);
                MobileElement tbCom = driver.findElement(OR.contact.getLocator("tbCompany"));
                tbCom.clear();
                tbCom.setValue(company);
            }
            // Add new Phone
            if(!Phone.equals("")){
                Log.info("Add Phone for Contact: "+Phone);
                driver.findElement(OR.contact.getLocator("icoPlusPhone")).click();
                MobileElement tbPhone = driver.findElement(OR.contact.getLocator("tbPhone"));
                tbPhone.clear();
                tbPhone.setValue(Phone);
            }
            if(isVip == true){
                Log.info("Set VIP for Contact");
                try {
                    driver.isElementExist(OR.contact.getLocator("icoCrown"));
                    driver.findElement(OR.contact.getLocator("icoCrown")).click();
                }catch (Exception e){
                    // Contact is marked as VIP
                }
            }
            if(isDone == true){
                // tap on Done button
                common.clickElementButton("Done");
            }
        }
        Log.info("Ended addNewContactFromDetail keyword");
    }

    /**
     * Open detail an Email, then move that email to other folder
     * @param emailAddress
     * @param subject
     * @param account
     * @param folder
     * @param isMove
     */
    public void moveEmailToOtherImapFolderByDetail(String emailAddress, String subject, String account, String folder, boolean isMove){
        Log.info("Started moveEmailToOtherImapFolderByDetail keyword");
        openEmailItem(emailAddress,subject);
        // Clicking Folder icon
        driver.findElement(OR.email.getLocator("iconFolder")).click();
        moveToImapFolder(account,folder,isMove);
        driver.Wait(2);
        Log.info("Ended moveEmailToOtherImapFolderByDetail keyword");
    }

    /**
     * Reply from email's detail and click on Reply icon
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param numImage
     * @param option
     * @param collections
     * @param isSend
     */
    public void replyEmailByDetail(String from, String to, String cc, String bcc, String subject, String content, String numImage, String option, String collections, boolean isSend){
        Log.info("Started replyEmailByDetail keyword");
        Log.info("Tap on Reply icon");
        driver.findElement(OR.email.getLocator("toolBarReply")).click();
        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,315);
        sendEmail(from,to,cc,bcc,subject,content,numImage,option,collections,isSend);
        Log.info("Ended replyEmailByDetail keyword");
    }

    /**
     * Reply All from email's detail and click on Reply All icon
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param numImage
     * @param option
     * @param collections
     * @param isSend
     */
    public void replyAllEmailByDetail(String from, String to, String cc, String bcc, String subject, String content, String numImage, String option, String collections, boolean isSend){
        Log.info("Started replyAllEmailByDetail keyword");
        Log.info("Tap on Reply All icon");
        driver.findElement(OR.email.getLocator("toolBarReplyAll")).click();
        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,315);
        sendEmail(from,to,cc,bcc,subject,content,numImage,option,collections,isSend);
        Log.info("Ended replyAllEmailByDetail keyword");
    }

    /**
     * Forward from email's detail and click on Forward icon
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param numImage
     * @param option
     * @param collections
     * @param isSend
     */
    public void forwardEmailByDetail(String from, String to, String cc, String bcc, String subject, String content, String numImage, String option, String collections, boolean isSend){
        Log.info("Started forwardEmailByDetail keyword");
        Log.info("Tap on Forward icon");
        driver.findElement(OR.email.getLocator("toolBarForward")).click();
        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,315);
        sendEmail(from,to,cc,bcc,subject,content,numImage,option,collections,isSend);
        Log.info("Ended forwardEmailByDetail keyword");
    }

    /**
     * Add new Event from email's detail and click on Event icon
     * @param newTitle: Input New Event's title
     * @param newIsAllDay: True: set All Day for Event
     * @param isDone: True - Tap on "Done" button/ False: click Cancel
     */
    public void addNewEventByDetail(String newTitle, boolean newIsAllDay, boolean isDone){
        Log.info("Started addNewEventByDetail keyword");
        Log.info("Tap on Event icon");
        driver.findElement(OR.email.getLocator("toolBarEvent")).click();
        driver.Wait(5);
        common.closeSendNotification(true);
        // Edit, input New Event's title
        Log.info("Edit, input New Event's title");
        MobileElement TitleEvent  =  driver.findElement(OR.agenda.getLocator("txbTitle"));
        // Delete all text in Title field
        int maxChars = TitleEvent.getAttribute("value").length();
        for (int i = 0; i < maxChars; i++)
        {
            TitleEvent.sendKeys(Keys.DELETE);
        }
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
        Log.info("Ended addNewEventByDetail keyword");
    }

    /**
     * Add new to.do from email's detail and click on To.Do icon
     * @param dueDate: Today, Tomorrow, Next Week, None
     * @param collection
     */
    public void addNewTodoByDetail(String dueDate, String collection){
        Log.info("Started addNewTodoByDetail keyword");
        Log.info("Tap on ToDo icon");
        driver.findElement(OR.email.getLocator("toolBarTodo")).click();
        driver.Wait(5);
        Log.info("Select Due Date: "+ dueDate);
        try{
            // Due Date is selected
            driver.isElementExist(OR.email.getLocatorDynamic("icoCheck",new String[]{dueDate}));
        }
        catch (Exception e){
            // Click on Due Date
            driver.findElement(OR.email.getLocatorDynamic("optTodoView",new String[]{dueDate})).click();
        }
        Log.info("Select Collection: "+ collection);
        try{
            // Collection is selected
            driver.isElementExist(OR.email.getLocatorDynamic("icoCheck",new String[]{collection}));
        }
        catch (Exception e){
            // Click on Collection
            driver.findElement(OR.email.getLocatorDynamic("optTodoView",new String[]{collection})).click();
        }
        common.clickStaticTextButton("Create ToDo");
        Log.info("Ended addNewTodoByDetail keyword");
    }

    /**
     * Check to.do item in to.do view
     * @param emailAddress
     * @param subject
     * @param content
//     * @param isStar
     * @param isLinked
     * @param testcase
     */
    public boolean checkEmailItemView(String emailAddress, String subject, String content ,boolean isLinked, String testcase){
        Log.info("Started checkEmailItemView keyword > " + subject + " with "+ emailAddress);
        boolean flag = true;
        Log.info("Check subject and emailAddress");
        String[] email = {emailAddress,subject,content};
        driver.Wait(10, OR.email.getLocatorDynamic("emailItem",email));
        try{
            driver.isElementExist(OR.email.getLocatorDynamic("emailItem",email));
            Log.info("Passed - The email with subject: "+ subject+ " and Email: "+ emailAddress+ " is available");
        }catch (Exception e){
            Log.info("Failed - Not found email with subject: "+subject);
            flag = false;
            driver.captureScreenShot(testcase);
            return flag;
        }
        if(!content.equals("")){
            try{
                driver.isElementExist(OR.email.getLocatorDynamic("txaContentView",email));
                Log.info("Passed - The content is match : "+content);
            }catch (Exception e){
                Log.info("Failed - The content is not match: "+content);
                flag = false;
            }
        }
        // New build cannot check star in list view, xpath star icon is XCUIElementTypeStaticText
//        if(isStar == true) {
//            try {
//                driver.isElementExist(OR.email.getLocatorDynamic("icoStarredInList", email));
//                Log.info("Passed - The email is mark Star");
//            } catch (Exception e) {
//                Log.info("Failed - The email is not mark Star ");
//                flag = false;
//            }
//        }else {
//            try {
//                driver.isElementExist(OR.email.getLocatorDynamic("icoStarredInList", email));
//                Log.info("Failed - The email is mark Star");
//                flag = false;
//            } catch (Exception e) {
//                Log.info("Passed  - The email is not mark Star ");
//            }
//        }
        if(isLinked == true) {
            try {
                driver.isElementExist(OR.email.getLocatorDynamic("icoLinkedInList", email));
                Log.info("Passed - The email is Linked");
            } catch (Exception e) {
                Log.info("Failed - The email is not Linked ");
                flag = false;
            }
        }else {
            try {
                driver.isElementExist(OR.email.getLocatorDynamic("icoLinkedInList", email));
                Log.info("Failed - The email is Linked");
                flag = false;
            } catch (Exception e) {
                Log.info("Passed  - The email is not Linked ");
            }
        }
        if(flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkEmailItemView keyword");
        return flag;
    }
    /**
     * Open search section and search
     * @param by (ex: "Body";"Body,From";"All")
     * @param keyword
     */
    public void searchEmailItem(String by, String keyword){
        Log.info("Started searchEmailItem keyword");
        //Swipe up to down to show Search field
        driver.Wait(5);
        driver.touchActionSwipe(OR.common.getSwipeCoordinate("swipeUpDownItemView"));
        driver.Wait(2);

        // Click to search textbox
        Log.info("Open search Email view");
        driver.tapCoordinate(OR.email.getCoordinate("SearchEmail"));
        driver.tapCoordinate(OR.email.getCoordinate("SearchEmail"));

        // Input keyword to in 'Search' field
        Log.info("Search by keyword > " +keyword);
        driver.findElement(OR.email.getLocator("txtSearchEmail")).sendKeys(keyword);
        common.clickElementButton("All");
        if(!by.contains(",")){
            common.clickElementButton(by);
        }else{
            int length = by.split(",").length;
            for(int i = 0; i < length; i++){
                String splitItem = by.split(",")[i];
                common.clickElementButton(splitItem);
            }
        }
        Log.info("Ended searchEmailItem keyword");
    }

    /**
     * Open multi-select mode in Email View, then select an option (Ex: Mark as read, Mark as unread, File, Collect, Trash)
     * @param items: 1 or multi emails have ‘comma’ for each item. Ex: email1,email2
     * @param isSelectAll: True - select all emails
     * @param options: Mark as read, Mark as unread, File, Collect, Trash/Delete
     */
    public void multiSelectEmail(String items, boolean isSelectAll, String options){
        Log.info("Started multiSelectEmail keyword");

        switch(options.toLowerCase()) {
            case "mark as read":
                options = "Mark";
                common.multiSelectMode(items, isSelectAll, options);
                try {
                    driver.isElementExist(OR.common.getLocatorDynamic("btnElementButton", new String[]{"Mark as read"}));
                    Log.info("Click Mark icon");
                    common.clickElementButton("Mark as read");
                } catch (Exception e) {
                    Log.info("No display 'Marked as read' button => cancel multi mode");
                    common.clickElementButton("Cancel");
                    // Exit multi-select mode
                    common.clickElementButton("Cancel");
                }
                break;
            case "mark as unread":
                options = "Mark";
                common.multiSelectMode(items, isSelectAll, options);
                try {
                    driver.isElementExist(OR.common.getLocatorDynamic("btnElementButton", new String[]{"Mark as unread"}));
                    Log.info("Click Unmark icon");
                    common.clickElementButton("Mark as unread");
                } catch (Exception e) {
                    Log.info("No display 'Marked as unread' button => cancel multi mode");
                    common.clickElementButton("Cancel");
                    // Exit multi-select mode
                    common.clickElementButton("Cancel");
                }
                break;
            case "file":
                Log.info("Open File in IMAP folder view");
                common.multiSelectMode(items, isSelectAll, options);
                break;
            case "trash":
                common.multiSelectMode(items, isSelectAll, options);
                break;
            case "collect":
                common.multiSelectMode(items, isSelectAll, options);
                break;
        }
        Log.info("Ended multiSelectEmail keyword");
    }
}
