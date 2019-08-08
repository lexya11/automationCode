package Keywords;

import Locators.ObjectRepository;
import Support.SetupServer;
import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

public class ContactKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    HomeKeywords home;
    EmailKeywords email;
    AgendaKeywords agenda;
    static Logger Log = Logger.getLogger(ContactKeywords.class);

    public ContactKeywords(SetupServer driver){
        this.driver = driver;
        this.common = new CommonKeywords(driver);
        this.home = new HomeKeywords(driver);
        this.email = new EmailKeywords(driver);
        this.agenda = new AgendaKeywords(driver);
    }

    /**
     * Filter Contact By Type
     * @param type: All, Vip, Recent
     */
    public void filterByType (String type){
        Log.info("Started filterByType keywords > "+ type);
        driver.Wait(2);
        if(type.equalsIgnoreCase("Vip")){
            type = "VIP";
        }
        Log.info("Filter Contact from Navigation Bar");
        driver.findElement(OR.contact.getLocator("btnFilter")).click();
        MobileElement selectTypeToFilter = driver.findElement(OR.contact.getLocatorDynamic("typeFilterTop",new String[]{type}));
        selectTypeToFilter.click();
        boolean status = driver.findElement(OR.common.getLocatorDynamic("btnElementButton",new String[]{"Done"})).isEnabled();
        if(status){
            common.clickElementButton("Done");
        }else
            driver.tapCoordinate(350,40);
        Log.info("Ended filterByType keywords > "+ type);
    }

    /**
     * Check current filter by type
     * @param type: All, Vip, Recent
     * @param testcase
     */
    public boolean checkFilterByType(String type, String testcase){
        Log.info("Started checkFilterByType keywords > "+ type);
        driver.Wait(2);
        boolean flag;
        if(type.equalsIgnoreCase("Vip")){
            type = "VIP";
        }
        Log.info("Filter Contact from Navigation Bar");
        driver.findElement(OR.contact.getLocator("btnFilter")).click();
        MobileElement selectTypeToFilter = driver.findElement(OR.contact.getLocatorDynamic("typeFilterTop",new String[]{type}));
        selectTypeToFilter.click();
        boolean status = driver.findElement(OR.common.getLocatorDynamic("btnElementButton",new String[]{"Done"})).isEnabled();
        if(!status){
            flag = true;
            Log.info("Passed - Filter By Type: " +type);
        }else{
            flag= false;
            Log.info("Failed - Not Filter By Type: " +type);
            driver.captureScreenShot(testcase);
        }
        driver.tapCoordinate(350,40);
        Log.info("Ended checkFilterByType keywords > "+ type);
        return flag;
    }

    /**
     * Check current local filter of contact view and number contact
     * @param collection
     * @param numContact
     * @param testcase
     */
    public boolean checkContactLocalFilter(String collection, int numContact, String testcase){
        Log.info("Started checkContactLocalFilter Keyword" );
        String localfilter = "     Flo - "+collection+" ";

        boolean flag = true;
        // Get value text in Element
        String txtLocalFilter = driver.findElement(OR.common.getLocator("localFilter")).getAttribute("value");
        // Check local filter
        if(!txtLocalFilter.contains(")")){
            Log.info("Cannot check number of Contact because there is no contact in list view");
            flag = false;
        }else {
            // Split get text Local filter
            String actualText1 = txtLocalFilter.split("\\(")[0];
            // Split get text number Contact
            String actualText2 = txtLocalFilter.split("\\(")[1];
            String getNum = actualText2.substring(0,1);
            // Convert String to int
            int actualNum = Integer.parseInt(getNum);
            if(collection.equals("All Collections")){
                try {
                    // Check Local filter
                    Assert.assertEquals(actualText1,localfilter);
                    Log.info("Passed - Current local filter: "+collection);
                }catch (AssertionError aE){
                    Log.info("Failed - Current local filter is not: "+collection+ " - "+aE.getMessage());
                    flag = false;
                }
                try {
                    // check number of Contact
                    Assert.assertEquals(actualNum,numContact);
                    Log.info("Passed - Number of Contact: "+numContact);
                }catch (AssertionError aE){
                    Log.info("Failed - Number of Contact is not: "+numContact+ " - "+aE.getMessage());
                    flag = false;
                }
            }
            else{
                try {
                    // Check Local filter
                    Assert.assertEquals(actualText1,localfilter);
                    Log.info("Passed - Current local filter: "+collection);
                }catch (AssertionError aE){
                    Log.info("Failed - Current local filter is not: "+collection+ " - "+aE.getMessage());
                    flag = false;
                }
                try {
                    // check number of Contact
                    Assert.assertEquals(actualNum,numContact);
                    Log.info("Passed - Number of Contact: "+numContact);
                }catch (AssertionError aE){
                    Log.info("Failed - Number of Contact is not: "+numContact+ " - "+aE.getMessage());
                    flag = false;
                }
            }
        }
        if(flag==false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkContactLocalFilter Keyword" );
        return flag;
    }

    /**
     * Open a contact from list on contact view
     * @param first
     * @param last
     */
    public void openContactItem(String first, String last){
        Log.info("Started openContactItem Keyword" );
        driver.Wait(5);
        String[]nameContact = {first,last};
        try {
            // Displayed First Last
            Log.info("Open Contact: "+first+" "+last);
            driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            driver.findElement(OR.contact.getLocatorDynamic("txtFirstLast",nameContact)).click();
        }catch (Exception e){
            // Displayed Last, First
            Log.info("Open Contact: "+last+", "+first);
            driver.findElement(OR.contact.getLocatorDynamic("txtLastFirst",nameContact)).click();
        }
        Log.info("Ended openContactItem Keyword" );
    }

    /**
     * Check contact item in contact view
     * @param first
     * @param last
     * @param moreInformation : "" no check
     * @param isVip
     * @param testcase
     */
    public boolean checkContactItemView(String first, String last, String moreInformation, boolean isVip, String testcase){
        Log.info("Started checkContactItemView Keyword" );

        String[]nameContact = {first,last};
        boolean flag = true;
        boolean firstLast = true;
        // Check Contact exist
        try{
            driver.Wait(10,OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            Log.info("Display First - Last");
            Log.info("Passed - Contact: "+first+" "+last+" is found");
        }
        catch(Exception e)
        {
            try {
                driver.isElementExist(OR.contact.getLocatorDynamic("txtLastFirst",nameContact));
                Log.info("Display Order Contact as: Last, First");
                Log.info("Passed - Contact has First Name: "+first+" and Last Name: "+last+" is found");
                firstLast = false;
            }catch (Exception e1){
                Log.info("Failed - Contact has First Name: "+first+" and Last Name: "+last+" is not found");
                driver.captureScreenShot(testcase);
                flag = false;
                return flag;
            }
        }
        // check More Information is displayed
        if(!moreInformation.equals("")){
            String actualInformation;
            if(firstLast == true){
                actualInformation = driver.findElement(OR.contact.getLocatorDynamic("contactItemFL",nameContact)).getAttribute("name");
            } else actualInformation = driver.findElement(OR.contact.getLocatorDynamic("contactItemLF",nameContact)).getAttribute("name");
            try {
                Assert.assertEquals(actualInformation, moreInformation);
                Log.info("Passed - More Information: " + moreInformation + " in list");
            }catch(AssertionError e){
                Log.info("Failed - More Information is not: "+moreInformation+ " - " +e.getMessage());
                flag = false;
            }
        }

        // Check VIP icon
        if(isVip == true){
            try {
                driver.isElementExist(OR.contact.getLocatorDynamic("icoMarkedVipFL",nameContact));
                Log.info("Passed - Contact: "+first+" "+last+" is marked as VIP");
            } catch (Exception e){
                try {
                    driver.isElementExist(OR.contact.getLocatorDynamic("icoMarkedVipLF",nameContact));
                    Log.info("Passed - Contact: "+first+" "+last+" is marked as VIP");
                }catch (Exception e1){
                    Log.info("Failed - Contact: "+first+" "+last+" is not marked as VIP");
                    flag = false;
                }
            }
        } else {
            try {
                driver.isElementExist(OR.contact.getLocatorDynamic("icoVipFL",nameContact));
                Log.info("Passed - Contact: "+first+" "+last+" is not marked as VIP");
            } catch (Exception e){
                try {
                    driver.isElementExist(OR.contact.getLocatorDynamic("icoVipLF",nameContact));
                    Log.info("Passed - Contact: "+first+" "+last+" is not marked as VIP");
                }catch (Exception e1){
                    Log.info("Failed - Contact: "+first+" "+last+" is marked as VIP");
                    flag = false;
                }
            }
        }
        if(flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkContactItemView Keyword" );
        return flag;
    }

    /**
     * Send an email by swiping on contact and click on Email icon
     * @param first
     * @param last
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param option Move/Add
     * @param collections
     * @param isSent
     */
    public void sendEmailBySwipe(String first, String last, String from, String to, String cc, String bcc, String subject, String content, String numImage, String option, String collections, boolean isSent){
        Log.info("Started sendEmailBySwipe keyword");
        String[]nameContact = {first,last};
        MobileElement contactItem;

        // Find contact by FL or LF name
        try{
            driver.Wait(5);
            driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            Log.info("Contact displays First + Last");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameFirstLast", nameContact));
        }
        catch(Exception e){
            Log.info("Contact displays Last + First");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameLastFirst", nameContact));
        }

        int X = contactItem.getLocation().getX(); //0
        int Y = contactItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        X = X + 150;
        Y = Y + 22;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+250;
        int Y1 = Y;

        int[] swipeLeftToRightContact = {X,Y,X1,Y1};
        Log.info("Swipe left => right on Contact > "+ first +" "+ last);
        driver.touchActionSwipe(swipeLeftToRightContact);

        Log.info("Clicking Email icon");
        driver.tapElement(OR.contact.getLocator("icoEmail"));

        // Wait for compose email load
        driver.Wait(5);
        email.sendEmail(from, to, cc, bcc, subject, content, numImage, option, collections, isSent);
        Log.info("Ended sendEmailBySwipe keyword");
    }

    /**
     * @param first
     * @param last
     * @param newTitle
     * @param newIsAllDay
     * @param ClickButton
     */
    public void addEventBySwipe(String first, String last, String newTitle, boolean newIsAllDay, String clickButton, boolean isIcs, String ClickButton){
        Log.info("Started addEventBySwipe keyword");
        String[]nameContact = {first,last};
        MobileElement contactItem;

        // Find contact by FL or LF name
        try {
            driver.Wait(5);
            driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            Log.info("Contact displays First + Last");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameFirstLast", nameContact));
        }catch (Exception e){
            Log.info("Contact displays Last + First");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameLastFirst", nameContact));
        }

        int X = contactItem.getLocation().getX(); //0
        int Y = contactItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        X = X+150;
        Y = Y + 22;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+250;
        int Y1 = Y;

        int[] swipeLeftToRightContact = {X,Y,X1,Y1};
        // Action swipe left to right on Contact
        Log.info("Swipe left => right on Contact > "+first+" "+last);
        driver.touchActionSwipe(swipeLeftToRightContact);

        // Tap on Event icon
        Log.info("Clicking Event icon");
        driver.tapElement(OR.contact.getLocator("icoEvent"));

        common.closeSendNotification(true);
        agenda.addSimpleEvent(newTitle,newIsAllDay,ClickButton);
        agenda.sendEmailInvite(clickButton,isIcs);

        Log.info("Ended addEventBySwipe keyword");
    }

    /**
     * Delete contact by swiping on a contact and click on Delete icon
     * @param first
     * @param last
     * @param isOK: true - click Ok to delete/ false - no action
     */
    public void deleteContactBySwipe(String first, String last, boolean isOK){
        Log.info("Started deleteContactBySwipe keyword");
        // Find Coordinates Contact item in list
        String[] nameContact = {first, last};
        MobileElement contactItem;

        // Find contact by FL or LF name
        try {
            driver.Wait(10);
            driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            Log.info("Contact displays First + Last");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameFirstLast", nameContact));
        }catch (Exception e){
            Log.info("Contact displays Last + First");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameLastFirst", nameContact));
        }

        int X = contactItem.getLocation().getX(); //0
        int Y = contactItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 200 pixel (X = -200, Y = 0)
        int X1 = X-300;
        int Y1 = Y;

        int[] swipeRightToLeftContact = {X,Y,X1,Y1};
        Log.info("Swipe right => left on Contact > "+first+" "+last);
        driver.touchActionSwipe(swipeRightToLeftContact);

        Log.info("Clicking Trash icon");
        driver.tapElement(OR.contact.getLocator("icoTrash"));
        // Click Ok button
        if (isOK == true)
            common.clickElementButton("OK");

        Log.info("Ended deleteContactBySwipe keyword");
    }

    /**
     * Open move/add collection by swiping on a contact and click on Collection icon
     * @param first
     * @param last
     */
    public void openCollectionBySwipe(String first, String last){
        Log.info("Started openCollectionBySwipe keyword");
        // Find Coordinates Contact item in list
        String[]nameContact = {first,last};
        MobileElement contactItem;

        // Find contact by FL or LF name
        try {
            driver.Wait(5);
            driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
            Log.info("Contact displays First + Last");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameFirstLast", nameContact));
        }catch (Exception e){
            Log.info("Contact displays Last + First");
            contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameLastFirst", nameContact));
        }

        int X = contactItem.getLocation().getX(); //0
        int Y = contactItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 200 pixel (X = -200, Y = 0)
        int X1 = X-300;
        int Y1 = Y;

        int[] swipeRightToLeftContact = {X,Y,X1,Y1};
        Log.info("Swipe right => left on Contact > "+first+" "+last);
        driver.touchActionSwipe(swipeRightToLeftContact);

        Log.info("Clicking Collection icon");
        driver.tapElement(OR.contact.getLocator("icoCollection"));

        Log.info("Ended openCollectionBySwipe keyword");
    }

    /**
     * Add new Contact from many locations
//     * @param location: Plus Icon, Bottom Bar, None
     * @param first
     * @param last
     * @param company
     * @param Phone
     * @param email
     * @param isVip
     * @param isDone
     */
    public void addNewContact(String first, String last, String company, String Phone, String email, boolean isVip, boolean isDone){
        Log.info("Started addNewContact keyword");

//        // Open new contact view
//        if(location.equalsIgnoreCase("Plus Icon")){
//            Log.info("Add from Plus Icon");
//            // Clicking on "Plus" icon
//            driver.findElement(OR.common.getLocator("icoPlus")).click();
//            driver.Wait(4);
//            common.closeSendNotification(true);
//        }
//        else if(location.equalsIgnoreCase("Bottom Bar")){
//            Log.info("Add from Bottom Bar");
//            common.clickViewBottomBar("Contact");
//            driver.Wait(5);
//            common.closeSendNotification(true);
//        }
//        else if(location.equalsIgnoreCase("None")){
//            // Add New Contact
//            Log.info("New contact was opened");
//        }

        if(!first.equals("")){
            Log.info("Input First Name > "+first);
            MobileElement tbFirst = driver.findElement(OR.contact.getLocator("tbFirstName"));
            tbFirst.click();
            tbFirst.setValue(first);
        }

        if(!last.equals("")){
            Log.info("Input Last Name > "+last);
            MobileElement tbLast = driver.findElement(OR.contact.getLocator("tbLastName"));
            tbLast.click();
            tbLast.setValue(last);
        }

        if(!company.equals("")){
            Log.info("Input Company > "+company);
            MobileElement tbCom = driver.findElement(OR.contact.getLocator("tbCompany"));
            tbCom.click();
            tbCom.setValue(company);
        }

        if(!Phone.equals("")){
            Log.info("Add Phone > "+Phone);
            driver.findElement(OR.contact.getLocator("icoPlusPhone")).click();
            MobileElement tbPhone = driver.findElement(OR.contact.getLocator("tbPhone"));
            tbPhone.click();
            tbPhone.setValue(Phone);
        }

        if(!email.equals("")){
            Log.info("Add Email > "+email);
            driver.findElement(OR.contact.getLocator("icoPlusEmail")).click();
            MobileElement tbEmail = driver.findElement(OR.contact.getLocator("tbEmail"));
            tbEmail.setValue(email);
        }

        if(isVip == true){
            Log.info("Mark VIP");
            try {
                driver.touchActionSwipe(190,115,190,415);
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
        Log.info("Ended addNewContact keyword");
    }

    /**
     * Check contact item in Contact details view
     * @param first
     * @param last
     * @param company
     * @param Phone
     * @param email
     * @param isVip
     * @param linkedItems: 1 or many linked items, MUST have ‘comma’ for each item. Ex: Event1,Contact,Note
     * @param testcase
     * @return
     */
    public boolean checkContactItemDetails(String first, String last, String company, String Phone, String email, boolean isVip, String linkedItems, String collections, String testcase){
        Log.info("Started checkContactItemDetails keyword");
        boolean flag = true;
        // Check first name
        if(!first.equals("")){
            driver.Wait(2);
            String actualText = driver.findElement(OR.contact.getLocator("tbFirstName")).getAttribute("value");
            if(first.equals("None")){
                String First = "First";
                try {
                    Assert.assertEquals(actualText,First);
                    Log.info("Passed - First Name is > "+first);
                }catch (AssertionError aE){
                    Log.info("Failed - First Name is not > "+first+ " - " +aE.getMessage());
                    flag = false;
                }

            }else {
                try {
                    Assert.assertEquals(actualText,first);
                    Log.info("Passed - First Name is > "+first);

                }catch (AssertionError aE){
                    Log.info("Failed - First Name is not > "+first+ " - " +aE.getMessage());
                    flag = false;
                }
            }
        }

        // Check last name
        if(!last.equals("")){
            driver.Wait(2);
            String actualText = driver.findElement(OR.contact.getLocator("tbLastName")).getAttribute("value");
            if(last.equals("None")){
                String Last = "Last";
                try {
                    Assert.assertEquals(actualText,Last);
                    Log.info("Passed - Last Name is > "+last);
                }catch (AssertionError aE){
                    Log.info("Failed - Last Name is not > "+last+ " - " +aE.getMessage());
                    flag = false;
                }
            }else {
                try {
                    Assert.assertEquals(actualText,last);
                    Log.info("Passed - Last Name is > "+last);
                }catch (AssertionError aE){
                    Log.info("Failed - Last Name is not > "+last+ " - " +aE.getMessage());
                    flag = false;
                }
            }

        }
        // Check Company
        if(!company.equals("")){
            driver.Wait(2);
            String actualText = driver.findElement(OR.contact.getLocator("tbCompany")).getAttribute("value");
            if(company.equals("None")){
                String Company = "Company";
                try{
                    Assert.assertEquals(actualText,Company);
                    Log.info("Passed - Company is > "+company);
                }catch (AssertionError aE){
                    Log.info("Failed - Company is not > "+company+ " - " +aE.getMessage());
                    flag = false;
                }
            }else {
                try {
                    Assert.assertEquals(actualText,company);
                    Log.info("Passed - Company is > "+company);
                }catch (AssertionError aE){
                    Log.info("Failed - Company is not > "+company+ " - " +aE.getMessage());
                    flag = false;
                }
            }
        }

        // Check Phone
        if(!Phone.equals("")){
            if(Phone.equals("None")){
                try {
                    driver.isElementExist(OR.contact.getLocator("txtPhone"));
                    String actualText = driver.findElement(OR.contact.getLocator("txtPhone")).getAttribute("value");
                    Log.info("Failed - Phone number is > "+ actualText);
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Phone number is > None");
                }
            }else{
                try {
                    // Find Exist Phone
                    driver.isElementExist(OR.contact.getLocator("txtPhone"));
                    try {
                        String actualText = driver.findElement(OR.contact.getLocator("txtPhone")).getAttribute("value");
                        Assert.assertEquals(actualText,Phone);
                        Log.info("Passed - Phone number is > "+Phone);
                    }catch (AssertionError aE){
                        Log.info("Failed - Phone number is not > "+Phone+ " - " +aE.getMessage());
                        flag = false;
                    }
                }catch (Exception e){
                    Log.info("Failed - Not have Phone number");
                    flag = false;
                }
            }
        }
        // Check Email
        if(!email.equals("")){
            if(email.equals("None")){
                try {
                    driver.isElementExist(OR.contact.getLocator("txtEmail"));
                    String actualText = driver.findElement(OR.contact.getLocator("txtEmail")).getAttribute("value");
                    Log.info("Failed - Email address is > "+actualText);
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Email address is > None");
                }
            }else {
                try {
                    // Find Exist Email
                    driver.isElementExist(OR.contact.getLocator("txtEmail"));
                    try {
                        String actualText = driver.findElement(OR.contact.getLocator("txtEmail")).getAttribute("value");
                        Assert.assertEquals(actualText,email);
                        Log.info("Passed - Email address is > "+email);
                    }catch (AssertionError aE){
                        Log.info("Failed - Email address is not > "+email+ " - " +aE.getMessage());
                        flag = false;
                    }
                }catch (Exception e){
                    Log.info("Failed - Not have email address");
                    flag = false;
                }
            }
        }

        // Check VIP contact
        if(isVip == true){
            try {
                driver.isElementExist(OR.contact.getLocator("icoCrownMarked"));
                Log.info("Passed - Contact is > VIP");
            }catch (Exception e){
                Log.info("Failed - Contact is not > VIP");
                flag = false;
            }
        }else {
            try {
                driver.isElementExist(OR.contact.getLocator("icoCrown"));
                Log.info("Passed - Contact is not > VIP");
            }catch (Exception e){
                Log.info("Failed - Contact is > VIP");
                flag = false;
            }
        }
        // Check Item(s) Linked
        if(!linkedItems.equals("")){
            if(linkedItems.equals("None")){
                try {
                    driver.isElementExist(OR.contact.getLocator("tbItemLinked"));
                    Log.info("Failed - Contact is > linked");
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Contact is not > linked");
                }
                // check linked with multi items
            } else {
                // Count length of String Linked Items
                int length = linkedItems.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitLinkedItems = linkedItems.split(",")[i];
                    try {
                        driver.isElementExist(OR.contact.getLocatorDynamic("linkedItem", new String[]{splitLinkedItems}));
                        Log.info("Passed - Contact is linked > "+splitLinkedItems);
                    }catch (Exception e){
                        Log.info("Failed - Contact is not linked > "+splitLinkedItems);
                        flag = false;
                    }
                }
            }
        }

        // Check contact belongs collection(s)
        if(!collections.equals("")){
            if(collections.equals("None")){
                try {
                    driver.isElementExist(OR.contact.getLocator("noneCollection"));
                    Log.info("Passed - Collection is > None");
                } catch (Exception e){
                    Log.info("Failed - Collection is not > None");
                    flag = false;
                }
            } else if(collections.contains(",")){
                // Count length of String Collection
                int length = collections.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitCollection = collections.split(",")[i];
                    try {
                        driver.isElementExist(OR.contact.getLocatorDynamic("itmCollection", new String[]{splitCollection}));
                        Log.info("Passed - Collection is > "+splitCollection);
                    }catch (Exception e){
                        Log.info("Failed - Collection is not > "+splitCollection);
                        flag = false;
                    }
                }
            } else {
                try {
                    driver.isElementExist(OR.contact.getLocatorDynamic("itmCollection", new String[]{collections}));
                    Log.info("Passed - Collection is > "+collections);
                } catch (Exception e){
                    Log.info("Failed - Collection is not > "+collections);
                    flag = false;
                }
            }
        }

        // ScreenShot if fail
        if (flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkContactItemDetails keyword");
        return flag;
    }

    /**
     * Add new event from contact details and click on Event icon
     * @param newTitle
     * @param newIsAllDay
     * @param ClickButton
     */
    public void addEventByDetails(String newTitle, boolean newIsAllDay, String clickButton, boolean isIcs, String ClickButton){
        Log.info("Started addEventByDetails keyword");

        Log.info("Clicking on Event icon");
        driver.findElement(OR.contact.getLocator("icoEventDetails")).click();

        common.closeSendNotification(true);

        agenda.addSimpleEvent(newTitle,newIsAllDay,ClickButton);
        agenda.sendEmailInvite(clickButton,isIcs);

        Log.info("Ended addEventByDetails keyword");
    }

    /**
     * Send an email from contact details and click on Email icon
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
    public void sendEmailByDetails(String from, String to, String cc, String bcc, String subject, String content, String numImage, String option,String collections ,boolean isSent){
        Log.info("Started sendEmailByDetails keyword");

        Log.info("Clicking on Email icon");
        driver.findElement(OR.contact.getLocator("icoEmailDetails")).click();

        // input info in Compose Email view
        email.sendEmail(from,to,cc,bcc,subject,content,numImage,option,collections,isSent);
        Log.info("Ended EmailByDetails keyword");
    }

    /**
     * Mark VIP for Contact in List or in detail of Contact
     * @param isInDetail True: Action in Detail / False: Action in List
     * @param first
     * @param last
     * @param isVip True: Mark Vip / False: Un-Vip Contact
     */
    public void setVipContact(boolean isInDetail, String first, String last, boolean isVip){
        Log.info("Started setVipContact keyword");

        // Mark in detail
        if(isInDetail == true){

            if(isVip == true){
                try{
                    driver.isElementExist(OR.contact.getLocator("icoCrown"));
                    Log.info("Set VIP > "+first+" "+last);
                    driver.findElement(OR.contact.getLocator("icoCrown")).click();
                }catch (Exception e){
                    Log.info("Contact was VIP");
                }
            }else{
                try{
                    driver.isElementExist(OR.contact.getLocator("icoCrownMarked"));
                    Log.info("Set UnVIP > "+first+" "+last);
                    driver.findElement(OR.contact.getLocator("icoCrownMarked")).click();
                }catch (Exception e){
                    Log.info("Contact was normal");
                }
            }
        }
        // Mark in contact list
        else{
            String[] nameContact = {first, last};

            if(isVip == true){
                // Display order "First Last"
                driver.Wait(5);
                if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownInListFL", nameContact))) {
                    Log.info("Contact displays First + Last");
                    Log.info("Set VIP > " + first + " " + last);
                    driver.findElement(OR.contact.getLocatorDynamic("icoCrownInListFL", nameContact)).click();
                }
                else if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownMarkedInListFL",nameContact))){
                    Log.info("Contact displays First + Last");
                    Log.info("Contact was VIP");
                }

                // Display order "Last, First"
                if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownInListLF",nameContact))) {
                    Log.info("Contact displays Last + First");
                    Log.info("Set VIP > " + last + ", " + first);
                    driver.findElement(OR.contact.getLocatorDynamic("icoCrownInListLF", nameContact)).click();
                }
                else if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownMarkedInListLF",nameContact))) {
                    Log.info("Contact displays Last + First");
                    Log.info("Contact was VIP");
                }
            }
            else {
                 // Display order "First Last"
                if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownMarkedInListFL",nameContact))) {
                    Log.info("Contact displays First + Last");
                    Log.info("Set UnVIP > " + first + " " + last);
                    driver.findElement(OR.contact.getLocatorDynamic("icoCrownMarkedInListFL", nameContact)).click();
                }
                else if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownInListFL",nameContact))){
                    Log.info("Contact displays First + Last");
                    Log.info("Contact was normal");
                }

                // Display order "Last, First"
                if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownMarkedInListLF",nameContact))) {
                    Log.info("Contact displays Last + First");
                    Log.info("Set UnVIP > " + last + ", " + first);
                    driver.findElement(OR.contact.getLocatorDynamic("icoCrownMarkedInListLF", nameContact)).click();
                }
                else if(driver.isElementPresent(OR.contact.getLocatorDynamic("icoCrownInListLF",nameContact))){
                    Log.info("Contact displays Last + First");
                    Log.info("Contact was normal");
                }
            }
        }
        Log.info("Ended setVipContact keyword");
    }

    /**
     * Check icon not exist by swiping on contact list
     * @param first
     * @param last
     * @param icon: phone, chat, email
     * @param testcase
     */
    public boolean checkIconNotExistBySwipe(String first, String last, String icon, String testcase){
        Log.info("Started checkIconNotExistBySwipe keyword");
        boolean flag = true;
        String[]nameContact = {first,last};

        driver.Wait(7);
        driver.isElementExist(OR.contact.getLocatorDynamic("txtFirstLast",nameContact));
        MobileElement contactItem = driver.findElement(5,OR.contact.getLocatorDynamic("nameFirstLast", nameContact));

        int X = contactItem.getLocation().getX(); //0
        int Y = contactItem.getLocation().getY();
        // Point in left X, Y = Y + 20
        X = X + 150;
        Y = Y + 22;
        // Swipe right 256 pixel (X = 256, Y = 0)
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightContact = {X,Y,X1,Y1};

        // Action swipe left to right on Contact
        Log.info("Swipe left to right on Contact: "+first+" "+last);
        driver.touchActionSwipe(swipeLeftToRightContact);
        String Icon = icon;

        switch (icon.toLowerCase()){
            case "phone":
                icon = "\uE08A";
                break;
            case "chat":
                icon = "\uE087";
                break;
            case "email":
                icon = "\uE037";
                break;
        }
        try {
            driver.isElementExist(OR.contact.getLocatorDynamic("icoNotExist",new String[]{icon}));
            Log.info("Failed - Icon is exist > "+ Icon);
            flag = false;
            driver.captureScreenShot(testcase);
        }catch (Exception e){
            Log.info("Passed - Icon isn't exist > "+ Icon);
        }
        // Wait for swipe close
        driver.Wait(7);

        Log.info("Ended checkIconNotExistBySwipe keyword");
        return flag;
    }
}
