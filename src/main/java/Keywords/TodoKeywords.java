package Keywords;

import Locators.ObjectRepository;
import Support.SetupServer;
import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TodoKeywords {
    SetupServer driver;
    ObjectRepository OR;
    CommonKeywords common;
    HomeKeywords home;
    EmailKeywords email;
    static Logger Log = Logger.getLogger(TodoKeywords.class);

    public TodoKeywords(SetupServer driver){
        this.driver = driver;
        this.common = new CommonKeywords(driver);
        this.home = new HomeKeywords(driver);
        this.email = new EmailKeywords(driver);
    }

    /**
     * Add new to.do by quick add section
     * @param name
     * @param dueDate: None, Today, In 3 days, In 10 days, In 30 days
     */
    public void addQuickTodo(String name, String dueDate){
        Log.info("Started addQuickTodo keyword");
        Log.info("Quick add ToDo: "+name);
        driver.findElement(OR.todo.getLocator("txbQuickAdd")).sendKeys(name,Keys.ENTER);
        switch (dueDate){
            case "In 3 days":
                dueDate = "In 3 Days";
                break;
            case "In 10 days":
                dueDate = "In 10 Days";
                break;
            case "In 30 days":
                dueDate = "In 30 Days";
                break;
            case "Today":
                dueDate = "Today";
                break;
            case "None":
                dueDate = "Today";
                break;
        }
        try{
            driver.isElementExist(OR.todo.getLocator("filterByDue"));
            Log.info("Select Due date for this ToDo: "+dueDate);
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
        }catch (Exception e){}
        Log.info("Ended addQuickTodo keyword");
    }

    /**
     * Mark Done/Undone a to.do in list
     * @param name Title of To.Do
     * @param type done/ undone
     * @param isOK true - click Ok/ false - no action
     */
    public void markTodo(String name, String type, boolean isOK){
        Log.info("Started markTodo keyword");
        String iconDone = "\uE094";
        String iconMarkedDone = "\uE084";
        String[] todoItem1 = {iconDone, name};
        String[] todoItem2 = {iconMarkedDone, name};

        // find To.do in list
        driver.Wait(5,OR.todo.getLocatorDynamic("todoInList",new String[]{name}));
        if(driver.isElementPresent(OR.todo.getLocatorDynamic("todoInList",new String[]{name}))) {
            if (type.equals("done")) {
                // check to.do is not marked as Done
                if (driver.isElementPresent(OR.todo.getLocatorDynamic("icoUnDone", todoItem1))) {
                    // Mark Done a To.Do in list
                    MobileElement MarkDone = driver.findElement(OR.todo.getLocatorDynamic("icoUnDone", todoItem1));
                    MarkDone.click();
                    Log.info("Mark Done ToDo > " + name);
                    if (isOK == true) {
                        common.clickElementButton("OK");
                    }
                } else {
                    Log.info("ToDo '" + name + "' is already marked as Done");
                }
            } else if (type.equals("undone")) {
                // check to.do is not marked as UnDone
                if (driver.isElementPresent(OR.todo.getLocatorDynamic("icoMarkedDone", todoItem2))) {
                    // Mark UnDone a To.Do in list
                    MobileElement MarkUnDone = driver.findElement(OR.todo.getLocatorDynamic("icoMarkedDone", todoItem2));
                    MarkUnDone.click();
                    Log.info("Mark UnDone ToDo > " + name);
                    if (isOK == true) {
                        common.clickElementButton("OK");
                    }
                } else {
                    Log.info("ToDo '" + name + "' is already marked as UnDone");
                }
            }
        }
        Log.info("Ended markTodo keywords");
    }

    /**
     * Check popup Message will display on to.do View when check Done, check Undone to.do
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
            // Get message on static test 1
            String actualText = driver.findElement(OR.todo.getLocator("lblMessage")).getAttribute("value");
            Log.info("popup Message show: "+actualText);
            Assert.assertEquals(actualText,message);
        } catch (AssertionError aE){
            Log.info("Assert Error: "+ aE.getMessage());
            flag = false;
            driver.captureScreenShot(testcase);
        }
        // Click button on popup
        if(clickButton.equals("OK"))
            common.clickElementButton("OK");
        else if(clickButton.equals("OK - Not Show"))
            common.clickElementButton("Ok. Don't show again.");
        else
            Log.info("Parameter input failed, please check again!!!");
        Log.info("Ended checkMessage keyword");
        return flag;
    }

    /**
     * Filter To.do By Type
     * @param from Top - filter by type from Navigation Bar / Bottom - filter by type from Bottom Bar
     * @param type All, Star, Due, Done, Collection
     */
    public void filterByType (String from , String type){
        Log.info("Started filterByType keywords");
        // Filter by top
        if (from.equalsIgnoreCase("Top")){
            Log.info("Filter from Navigation Bar");
            driver.findElement(OR.common.getLocator("secNavigationBar")).click();
        }
        // Filter by bottom
        else if (from.equalsIgnoreCase("Bottom")){
            Log.info("Filter from Bottom Bar");
            //Long press on To.do icon in bottom bar
            common.longPressBottomBar("Todo");
        }

        Log.info("Select filter > " + type );
        driver.findElement(OR.todo.getLocatorDynamic("typeFilter",new String[]{type})).click();
        if(driver.isElementPresent(OR.common.getLocator("secLocalFilter"))){
            MobileElement iconPlus = driver.findElement(OR.common.getLocator("icoPlus"));
            int X = iconPlus.getLocation().getX();
            int Y = iconPlus.getLocation().getY();
            driver.tapCoordinate(X,Y);
        }
        Log.info("Ended filterByType keywords");
    }

    /**
     * Check current filter by type
     * @param type: All, Star, Due, Done, Collection
     * @return
     */
    public boolean checkFilterByType(String type, String testcase){
        Log.info("Started checkFilterByType Keyword" );
        boolean flag = true;
        // Open Local filter
        driver.findElement(OR.common.getLocator("secNavigationBar")).click();
        driver.findElement(OR.todo.getLocatorDynamic("typeFilter",new String[]{type})).click();
        MobileElement btnDone = driver.findElement(OR.todo.getLocator("btnDoneInLocal"));
        // Check Button 'Done' is enabled
        if(btnDone.isEnabled()){
            Log.info("Failed - ToDo's list not filter by: "+type);
            driver.captureScreenShot(testcase);
            flag = false;
        }else {
            Log.info("Passed - Filter ToDo's List by: " +type);
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
     * Check to.do item in to.do view
     * @param name Title of To.Do
     * @param status Normal, Due, Overdue
     * @param dueDate Ex: Mon, 16 Apr
     * @param isCheckList
     * @param isStar
     * @param isDone
     * @param isLinked
     * @param testcase
     */
    public boolean checkTodoItemView(String name, String status, String dueDate, boolean isDone, boolean isCheckList, boolean isStar, boolean isLinked, String testcase){
        Log.info("Started checkTodoItemView keyword > " + name);
        driver.Wait(3);
        boolean flag = true;
        // Check exist
        try {
            driver.isElementExist(OR.common.getLocatorDynamic("itmTitleItem", new String[]{name}));
            Log.info("Passed - Title is > "+ name);
        }catch (Exception e){
            Log.info("Failed - Not found Todo has Title is > "+ name);
            flag = false;
            driver.captureScreenShot(testcase);
            return flag;
        }
        String expectDueDate;
        // Find Due Date of To.do
        String strDueDate = driver.findElement(OR.todo.getLocatorDynamic("itmDueDateTodo",new String[]{name})).getAttribute("name");
        // Check To.Do Status
        // Don't have due date
        if (strDueDate.equals("\ue094")) {
            // To.do is Normal
            if(status.equals("Normal"))
                Log.info("Passed - ToDo is > "+ status);
            else {
                Log.info("Failed - ToDo is not > "+ status);
                flag = false;
            }
        }
        // Have due date
        else {
            // Current date
            Calendar cal = Calendar.getInstance();
            String dateCurrent = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
            // Tomorrow
            cal.add(Calendar.DATE, 1);
            String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
            // Yesterday
            cal.add(Calendar.DATE, -2);
            String yesterday = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

            // Generate expected String for due date
            if (dueDate.equals(dateCurrent))
                expectDueDate = "Today " + dueDate;
            else if(dueDate.equals(tomorrow))
                expectDueDate = "Tomorrow " + dueDate;
            else if(dueDate.equals(yesterday))
                expectDueDate = "Yesterday " + dueDate;
            else
                expectDueDate = dueDate;

            // Get status & due date
            String actualStatus = strDueDate.split(" ", 2)[0];
            String actualDueDate = strDueDate.split(" ", 2)[1];
            //Log.info(actualStatus + " " + actualDueDate);

            if(status.equals("Normal")) {
                Log.info("Failed - ToDo is > '"+ status);
                flag = false;
            }
            // To.do is Due
            else if(status.equals("Due")){
                if(actualStatus.equals("Due")){
                    Log.info("Passed - ToDo is > "+ status);
                    Log.info("Actual Due Date: "+actualDueDate);
                    // Check Due date of to.do
                    if (expectDueDate.equals(actualDueDate))
                        Log.info("Passed - Due date is > "+actualDueDate);
                    else{
                        Log.info("Failed - Due date is not > "+actualDueDate);
                        flag = false;
                    }
                }else{
                    Log.info("Failed - ToDo is not > "+ status);
                    flag = false;
                }
            }
            // To.do is Overdue
            else if(status.equals("Overdue")){
                if(actualStatus.equals("Overdue")){
                    Log.info("Passed - ToDo is > "+ status);
                    // Check Due date of to.do
                    if (expectDueDate.equals(actualDueDate))
                        Log.info("Passed - Due date is "+ actualDueDate);
                    else{
                        Log.info("Failed - Due date is not > "+ actualDueDate);
                        flag = false;
                    }
                }else{
                    Log.info("Failed - ToDo is not > "+ status);
                    flag = false;
                }
            }
        }
        // Check Done To.Do
        String iconDone = "\uE084";
        String[] doneTodo = {iconDone, name};

        if(isDone == true){
            // check icon marked as Done
            if(driver.isElementPresent(OR.todo.getLocatorDynamic("icoMarkedDone", doneTodo))){
                Log.info("Passed - ToDo is > Done");
            }else {
                Log.info("Failed - ToDo is > Undone");
                flag = false;
            }
        } else {
            // check icon Not marked as Done
            if(!driver.isElementPresent(OR.todo.getLocatorDynamic("icoMarkedDone", doneTodo))){
                Log.info("Passed - ToDo is > Undone.");
            }else {
                Log.info("Failed - ToDo is > Done.");
                flag = false;
            }
        }

        // Check CheckList and Linked
        String bothOfIcon = "\uE076 \uE05D";
        String Linked = "\uE05D";
        String CheckList = "\uE076";
        String [] LinkedAndChecklist = {bothOfIcon,name};
        String [] Checklist = {CheckList,name};
        String [] linked = {Linked,name};

        // Check To.Do has checkList and Linked icon
        if(isCheckList == true && isLinked == true){
            try {
                // Check Linked icon and checkList icon
                driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",LinkedAndChecklist));
                Log.info("Passed - ToDo has Checklist");
                Log.info("Passed - ToDo has Linked");
            }catch (Exception e){
                try {
                    // Check checkList icon
                    driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                    Log.info("Passed - ToDo has Checklist");
                    Log.info("Failed - ToDo has no Linked");
                }catch (Exception e1){
                    // Check Linekd icon
                    try {
                        driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",linked));
                        Log.info("Passed - ToDo has Linked");
                        Log.info("Failed - ToDo has no Checklist");
                    }catch (Exception e2){
                        Log.info("Failed - ToDo has no Checklist");
                        Log.info("Failed - ToDo has no Linked");
                    }
                }
                flag = false;
            }
        // Check To.Do only having Checklist icon
        }else if(isCheckList == true && isLinked == false){
            try {
                // Check checkList icon
                driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                Log.info("Passed - ToDo has Checklist");
                Log.info("Passed - ToDo has no Linked");
            }catch (Exception e) {
                try {
                    // Check Linked icon and checkList icon
                    driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList", LinkedAndChecklist));
                    Log.info("Passed - ToDo has Checklist");
                    Log.info("Failed - ToDo has Linked");
                } catch (Exception e1) {
                    // Check Linked icon
                    try {
                        driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList", linked));
                        Log.info("Failed - ToDo has no Checklist");
                        Log.info("Failed - ToDo has Linked");
                    }catch (Exception e2){
                        Log.info("Failed - ToDo has no Checklist");
                        Log.info("Passed - ToDo has no Linked");
                    }
                }
                flag = false;
            }
        // Check To.Do only having Linked icon
        }else if(isCheckList == false && isLinked == true){
            try {
                // Check Linked icon
                driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",linked));
                Log.info("Passed - ToDo has Linked");
                Log.info("Passed - ToDo has no Checklist");
            }catch (Exception e){
                try {
                    // Check Linked icon and checkList icon
                    driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",LinkedAndChecklist));
                    Log.info("Passed - ToDo has Linked");
                    Log.info("Failed - ToDo has Checklist");
                }catch (Exception e1){
                    // Check checkList icon
                    try {
                        driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                        Log.info("Failed - ToDo has Checklist");
                        Log.info("Failed - ToDo has no Linked");
                    }catch (Exception e2){
                        Log.info("Passed - ToDo has no Checklist");
                        Log.info("Failed - ToDo has no Linked");
                    }
                }
                flag = false;
            }
        // Check To.Do has no Linked and no CheckList icon
        }else if(isCheckList == false && isLinked == false){
            try {
                driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",LinkedAndChecklist));
                Log.info("Failed - ToDo has Linked");
                Log.info("Failed - ToDo has Checklist");
                flag = false;
            }catch (Exception e){
                try {
                    // Check CheckList icon
                    driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",Checklist));
                    Log.info("Failed - ToDo has Checklist");
                    flag = false;
                }catch (Exception e1){
                    Log.info("Passed - ToDo has no Checklist");
                }
                try {
                    // Check Linked icon
                    driver.isElementExist(OR.todo.getLocatorDynamic("icoLinkAndCheckList",linked));
                    Log.info("Failed - ToDo has Linked");
                    flag = false;
                }catch (Exception e2){
                    Log.info("Passed - ToDo has no Linked");
                }
            }
        }
        // Check To.Do marked as Star or Not marked as Star
        if(isStar == true){
            // Found icon Starred
            if(driver.isElementPresent(OR.todo.getLocatorDynamic("icoStarView",new String[]{name}))){
                Log.info("Passed - ToDo is Star.");
            }else {
                Log.info("Failed - ToDo is Unstar.");
                flag = false;
            }
        } else {
            // Found icon Not Starred
            if(!driver.isElementPresent(OR.todo.getLocatorDynamic("icoStarView",new String[]{name}))){
                Log.info("Passed - ToDo is Unstar.");
            }else {
                Log.info("Failed - ToDo is Star.");
                flag = false;
            }
        }
        // Take screen shoot 1 time
        if(flag == false)
            driver.captureScreenShot(testcase);
        Log.info("Ended checkTodoItemView keyword");
        return flag;
    }

    /**
     * Check to.do item in to.do details view
     * @param name  Title of To.Do
     * @param dueDate   Ex: Thu, 03 May
     * @param isStar    True: is Starred - False is not Starred
     * @param duration  None, Short, Medium, Long, Manual
     * @param description   Text in description - None: Has no text in Description field
     * @param collection    1 or many collections, MUST have ‘comma’ for each item. Ex: None or General,Play,Home
     * @param linkedItems   1 or many linked items, MUST have ‘comma’ for each item. Ex: None or Event1,Contact,Note
     * @param testcase
     * @return
     */
    public boolean checkTodoItemDetails(String name, String dueDate, boolean isStar, String duration, String description,
                                        String collection, String linkedItems, String testcase){
        Log.info("Started checkTodoItemDetails keyword");
        boolean flag = true;

        // Check title of To.Do
        if(!name.equals("")){
            try{
                String txtTitle = driver.findElement(OR.todo.getLocator("txbTitleEditView")).getAttribute("name");
                Assert.assertEquals(txtTitle,name);
                Log.info("Passed - Title is: "+name);
            } catch (AssertionError aE){
                flag = false;
                Log.info("Failed - Assert Title Error: "+aE.getMessage());
            }
        }

        // Check Due date
        if(!dueDate.equals("")) {
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
            if (dueDate.equals(dateCurrent)) {
                dueDate = "Due Today";
            } else if (dueDate.equals(tomorrow)) {
                dueDate = "Due Tomorrow";
            } else if (dueDate.equals(yesterday)) {
                dueDate = "Due Yesterday";
            } else if (dueDate.equals("None")) {
                dueDate = null;
            } else {
                dueDate = "Due " + dueDate;
            }
            try {
                String txtDueDate = driver.findElement(OR.todo.getLocator("txbDueDate")).getAttribute("name");
                Assert.assertEquals(txtDueDate, dueDate);
                Log.info("Passed - Due date is: " + dueDate);
            } catch (AssertionError aE) {
                flag = false;
                Log.info("Failed - Assert Due date Error: " + aE.getMessage());
            }
        }

        // Check Star icon
        if(isStar == true){
            try {
                // Found icon Starred
                driver.isElementExist(OR.todo.getLocator("icoStar"));
                Log.info("Passed - ToDo is marked as Star.");
            }catch (Exception e){
                Log.info("Failed - ToDo is not marked as Star.");
                flag = false;
            }
        }else {
            try {
                // Found icon Star
                driver.isElementExist(OR.todo.getLocator("icoUnStar"));
                Log.info("Passed - ToDo is not marked as Star.");
            }catch (Exception e){
                Log.info("Failed - ToDo is marked as Star.");
                flag = false;
            }
        }
        if(!duration.equals("")){
            // Check Duration
            switch (duration){
                case "None":
                    duration = null;
                    break;
                case "Short":
                    duration = "30 mins";
                    break;
                case "Medium":
                    duration = "2 hrs";
                    break;
                case "Long":
                    duration = "5 hrs";
                    break;
                case "Manual":
                    duration = "6 hrs";
                    break;
            }
            try{
                String txtDuration = driver.findElement(OR.todo.getLocator("txbDuration")).getAttribute("value");
                Assert.assertEquals(txtDuration,duration);
                Log.info("Passed - Duration is: "+duration);
            } catch (AssertionError aE){
                flag = false;
                Log.info("Failed - Assert Duration Error: "+aE.getMessage());
            }
        }
        if(!description.equals("")){
            // Check Description
            if(description.equals("None")){
                description = null;
            }
            try{
                String txtDescription = driver.findElement(OR.todo.getLocator("tbDescription")).getAttribute("value");
                Assert.assertEquals(txtDescription,description);
                Log.info("Passed - Description is: "+description);
            } catch (AssertionError aE){
                flag = false;
                Log.info("Failed - Assert Description Error: "+aE.getMessage());
            }
        }

        if(!collection.equals("")){
            // Check none Collection in Collection field
            if(collection.equals("None")){
                try {
                    driver.isElementExist(OR.todo.getLocator("noneCollection"));
                    Log.info("Passed - ToDo does not contain Collection");
                } catch (Exception e){
                    Log.info("Failed - ToDo contains Collection");
                    flag = false;
                }
                // Check Multiple Collections in Collection field
            } else if(collection.contains(",")){
                // Count length of String Collection
                int length = collection.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitCollection = collection.split(",")[i];
                    try {
                        driver.isElementExist(OR.todo.getLocatorDynamic("itmCollection", new String[]{splitCollection}));
                        Log.info("Passed - ToDo contains Collections: "+splitCollection);
                    }catch (Exception e){
                        Log.info("Failed - ToDo does not contain Collections: "+splitCollection);
                        flag = false;
                    }
                }
                // Check a Collection in Collection field
            } else {
                try {
                    driver.isElementExist(OR.todo.getLocatorDynamic("itmCollection", new String[]{collection}));
                    Log.info("Passed - ToDo contains Collection: "+collection);
                } catch (Exception e){
                    Log.info("Failed - ToDo does not contain Collection: "+collection);
                    flag = false;
                }
            }
        }

        // Check Items(s) linked in Links section
        if(!linkedItems.equals("")){
            if(linkedItems.equals("None")){
                try {
                    driver.isElementExist(OR.todo.getLocator("txbItemLinked"));
                    Log.info("Failed - Todo is linked with other item.");
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Todo is not linked with other item");
                }
            } else if(linkedItems.contains(",")){
                // Count length of String Linked Items
                int length = linkedItems.split(",").length;
                for(int i = 0; i < length; i++){
                    String splitLinkedItems = linkedItems.split(",")[i];
                    try {
                        driver.isElementExist(OR.todo.getLocatorDynamic("txbLink", new String[]{splitLinkedItems}));
                        Log.info("Passed - Todo is linked with item: "+splitLinkedItems);
                    }catch (Exception e){
                        Log.info("Failed - Todo is not linked with item: "+splitLinkedItems);
                        flag = false;
                    }
                }
            }else {
                try {
                    driver.isElementExist(OR.todo.getLocatorDynamic("txbLink", new String[]{linkedItems}));
                    Log.info("Passed - Todo is linked with item: "+linkedItems);
                } catch (Exception e){
                    Log.info("Failed - Todo is not linked with item: "+linkedItems);
                    flag = false;
                }
            }
        }

        // Take screen shoot 1 time
        if (flag == false){
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended checkTodoItemDetails keyword");
        return flag;
    }

    /**
     * Add new to.do from many locations
     * @param title
     * @param isStar
     * @param isDone
     */
    public void addNewTodo(String title, boolean isStar, boolean isDone){
        Log.info("Started addNewTodo keyword");
        // New To.Do view is opened
        driver.Wait(3, OR.todo.getLocator("viewToDo"));
        driver.findElement(OR.todo.getLocator("viewToDo")).click();

        if(!title.equals("")){
            Log.info("Input Title > "+ title);
            MobileElement tbTitle = driver.findElement(OR.todo.getLocator("txbTitle"));
            tbTitle.setValue(title+"\n");
        }

        if(isStar == true){
            Log.info("Mark Star ToDo");
            String Star = "\uE074";
            // Check To.do is not Star
            if(driver.isElementPresent(OR.common.getLocatorDynamic("btnStaticButton", new String[]{Star}))){
                common.clickStaticTextButton(Star);
            }
        }

        if (isDone == true){
            Log.info("Click Done button");
            common.clickElementButton("Done");
        }
        Log.info("Ended addNewTodo Keyword");
    }

    /**
     * Move To.Do to top of the list
     * @param title
     * @param isOK
     */
    public void moveTopList(String title, boolean isOK){
        Log.info("Started moveTopList keyword");
        MobileElement toDoItem = driver.findElement(OR.todo.getLocatorDynamic("todoInList", new String[]{title}));
        int X = toDoItem.getLocation().getX(); //0
        int Y = toDoItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};
        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on ToDo: "+title);
        driver.touchActionSwipe(swipeLeftToRightToDo);
        Log.info("Clicking Move to Top icon");
        driver.tapElement(OR.todo.getLocator("icoMoveToTop"));

        if(isOK == true){
            common.clickElementButton("OK");
        }
        Log.info("Ended moveTopList keyword");
    }

    /**
     * Set due date for to.do by swiping on to.do and click on Due icon
     * @param title
     * @param dueDate Today, 3 days, 10 days, 30 days
     */
    public void setDueDateBySwipe(String title, String dueDate){
        Log.info("Started setDueDateBySwipe keyword");
        driver.Wait(6, OR.todo.getLocatorDynamic("todoInList", new String[]{title}));

        MobileElement toDoItem = driver.findElement(OR.todo.getLocatorDynamic("todoInList", new String[]{title}));
        int X = toDoItem.getLocation().getX(); //0
        int Y = toDoItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};

        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on ToDo: "+title);
        driver.touchActionSwipe(swipeLeftToRightToDo);
        Log.info("Clicking Due date icon");
        driver.tapElement(OR.todo.getLocator("icoDue"));
        Log.info("Set a Due date for this ToDo: "+dueDate);

        if(dueDate.equalsIgnoreCase("3 days")){
            dueDate = "In 3 Days";
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
        } else if(dueDate.equalsIgnoreCase("10 days")){
            dueDate = "In 10 Days";
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
        } else if(dueDate.equalsIgnoreCase("30 days")){
            dueDate = "In 30 Days";
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
        } else if(dueDate.equalsIgnoreCase("Today")){
            dueDate = "Today";
            driver.findElement(OR.todo.getLocatorDynamic("selDueDate",new String[]{dueDate})).click();
        }
        else {
            Log.info("Parameter input failed, please check again!!!");
        }
        Log.info("Ended setDueDateBySwipe keyword");
    }

    /**
     * Send an email by swiping on to.do and click on Email icon
     * @param title
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param content
     * @param collections
     * @param option Add/Move
     * @param isSent
     */
    public void sendEmailBySwipe(String title, String from, String to, String cc, String bcc, String subject, String content, String numImage,String option,String collections, boolean isSent){
        Log.info("Started sendEmailBySwipe keyword");
        MobileElement toDoItem = driver.findElement(OR.todo.getLocatorDynamic("todoInList", new String[]{title}));
        int X = toDoItem.getLocation().getX(); //0
        int Y = toDoItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+250;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};
        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on ToDo: "+title);
        driver.touchActionSwipe(swipeLeftToRightToDo);
        Log.info("Clicking Email icon");
        driver.tapElement(OR.todo.getLocator("icoEmail"));
        driver.Wait(5);
        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,415);
        email.sendEmail(from,to,cc,bcc,subject,content,numImage, option,collections,isSent);
        Log.info("Ended sendEmailBySwipe keyword");
    }

    /**
     * ToDo Review again
     * Add new simple note by swiping on to.do and click on Note icon
     * @param title
     * @param newNoteTitle
     * @param content
     * @param isDone
     */
    public void addNoteBySwipe(String title, String newNoteTitle, String content, boolean isDone){
        Log.info("Started addNoteBySwipe keyword");
        MobileElement toDoItem = driver.findElement(OR.todo.getLocatorDynamic("todoInList", new String[]{title}));
        int X = toDoItem.getLocation().getX(); //0
        int Y = toDoItem.getLocation().getY();
        X = X + 150;
        Y = Y + 22;
        int X1 = X+350;
        int Y1 = Y;
        int[] swipeLeftToRightToDo = {X,Y,X1,Y1};
        // Action swipe left to right on To.Do
        Log.info("Swipe left to right on ToDo: "+title);
        driver.touchActionSwipe(swipeLeftToRightToDo);
        Log.info("Clicking Note icon");
        driver.tapElement(OR.todo.getLocator("icoNote"));
        // Wait and go to header Note View
        driver.Wait(2);
        driver.touchActionSwipe(190,115,190,415);
        // Edit, input New Note's title
        Log.info("Edit, input New Note's title > "+newNoteTitle);
        MobileElement TitleNote  =  driver.findElement(OR.note.getLocator("txbTitle"));
        // Delete all text in Title field
        if(!driver.isElementPresent(OR.common.getLocatorDynamic("btnMenuItemButton", new String[]{"Select All"}))) {
            // long press on title of Note
            TitleNote.click();
            driver.longPressElement(OR.note.getLocator("txbTitle"));
            driver.Wait(2);
            common.clickMenuItemTextButton("Select All");
            driver.Wait(1);
            common.clickMenuItemTextButton("Cut");
        }
        TitleNote.setValue(newNoteTitle);
        MobileElement tbContent  =  driver.findElement(OR.note.getLocator("txaContent"));
        tbContent.click();
        tbContent.click();
        tbContent.setValue(content);
        if (isDone == true){
            // Tap on "Done" button
            common.clickElementButton("Done");
        }
        Log.info("Ended addNoteBySwipe keyword");
    }
    /**
     * ToDo Review again
     * Delete to.do by swiping on an event and click on Delete icon
     * @param title
     * @param isOK true - click Ok to delete/ false - no action
     */
    public void deleteTodoBySwipe(String title, boolean isOK){
        Log.info("Started deleteTodoBySwipe keyword");
        MobileElement toDoItem = driver.findElement(OR.todo.getLocatorDynamic("todoInList", new String[]{title}));
        int X = toDoItem.getLocation().getX(); //0
        int Y = toDoItem.getLocation().getY();

        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;

        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        // Action swipe right to left on To.Do
        Log.info("Swipe right to left on ToDo: "+title);
        driver.touchActionSwipe(swipeRightToLeftToDo);
        Log.info("Clicking Trash icon");
        driver.tapElement(OR.todo.getLocator("icoTrash"));

        if (isOK == true) {
            common.clickElementButton("OK");
        }
        // Wait for delete item
        driver.Wait(2);
        Log.info("Ended deleteTodoBySwipe keyword");
    }

    /**
     * ToDo review again
     * Open move/add to collection by swiping on an to.do and click on Collection icon
     * @param title
     */
    public void openCollectionBySwipe(String title){
        Log.info("Started openCollectionBySwipe keyword");
        driver.Wait(3);
        MobileElement toDoItem = driver.findElement(OR.todo.getLocatorDynamic("todoInList", new String[]{title}));
        int X = toDoItem.getLocation().getX(); //0
        int Y = toDoItem.getLocation().getY();
        // Point in right X = 340, Y = Y + 15
        X = X + 340;
        Y = Y + 15;
        // Swipe left 250 pixel (X = -250, Y = 0)
        int X1 = X-250;
        int Y1 = Y;
        int[] swipeRightToLeftToDo = {X,Y,X1,Y1};
        // Action swipe right to left on To.Do
        Log.info("Swipe right to left on ToDo: "+title);
        driver.touchActionSwipe(swipeRightToLeftToDo);
        Log.info("Clicking Collection icon");
        driver.tapElement(OR.todo.getLocator("icoCollection"));
        Log.info("Ended openCollectionBySwipe keyword");
    }

    /**
     * Set due date for To.do
     * @param quickOption None, Today, Tomorrow, One Week
     * @param month
     * @param day
     * @param year
     * @param clickDone
     */
    public void setDueDate(String quickOption, String month, int day, int year, boolean clickDone){
        Log.info("Started setDueDate keyword");
        String Day = Integer.toString(day);
        String Year = Integer.toString(year);
        String iconDuedate = "\uE022";
        // Open Datetime picker
        driver.findElement(OR.common.getLocatorDynamic("btnStaticButton", new String[]{iconDuedate})).click();
        //Select date
        if(quickOption.equals("")){
            // Setting month
            driver.findElement(OR.todo.getLocator("pickerWheel_1")).setValue(month);
            Log.info("Select Month: " + month);
            // Setting day
            driver.findElement(OR.todo.getLocator("pickerWheel_2")).setValue(Day);
            Log.info("Select Day: " + day);
            // Setting year
            driver.findElement(OR.todo.getLocator("pickerWheel_3")).setValue(Year);
            Log.info("Select Year: " + year);
            // Close date time picker
            driver.tapCoordinate(100,140);
            Log.info("Set Due date for ToDo: " + month + " " + day + ", " + year);
        }
        //Select quick date (None, Today, Tomorrow, One Week)
        else {
            Log.info("Set Due date of ToDo by choosing "+quickOption+" option");
            common.clickElementButton(quickOption);
            Log.info("Set Due date of ToDo by choosing "+quickOption+" option");
//                common.clickElementButton(quickOption);
//                String txtMonth = driver.findElement(OR.todo.getLocator("pickerWheel_1")).getAttribute("value");
//                String txtDay =  driver.findElement(OR.todo.getLocator("pickerWheel_2")).getAttribute("value");
//                String txtYear = driver.findElement(OR.todo.getLocator("pickerWheel_3")).getAttribute("value");
//                common.clickStaticTextButton(iconDuedate);
//                Log.info("Set Due date for ToDo: " +txtMonth+" "+txtDay+", "+txtYear);
        }
        // Click Done button
        if(clickDone == true)
            common.clickElementButton("Done");
        Log.info("Ended setDueDate keyword");
    }

    /**
     * Open a to.do from list on to.do view
     * @param title
     */
    public void openTodoItem(String title){
        Log.info("Started openTodoItem keyword");
        Log.info("Open Todo > "+ title);
        driver.Wait(10, OR.todo.getLocatorDynamic("todoInList",new String[]{title}));
        driver.Wait(3);
        driver.findElement(OR.todo.getLocatorDynamic("todoInList",new String[]{title})).click();
        Log.info("Ended openTodoItem keyword");
    }

    /**
     * Add new check list item in to.do and click done
     * @param title
     * @param isDone
     */
    public void addCheckItem(String title, boolean isDone){
        Log.info("Started addCheckItem keyword");
        // input title of Checklist
        MobileElement txtChecklist = driver.findElement(OR.todo.getLocator("addChecklist"));
        txtChecklist.click();
        Log.info("Add Checklist > "+title);
        txtChecklist.setValue(title+"\n");
        // Mark done a Checklist
        if (isDone == true){
            MobileElement iconToDo = driver.findElement(OR.todo.getLocatorDynamic("iconToDo",new String[]{title}));
            Log.info("Set Done > "+title);
            iconToDo.click();
        }
        Log.info("Ended addCheckItem keyword");
    }

    /**
     * Swipe right to left on check list item and delete it
     * @param title
     */
    public void deleteCheckItem(String title){
        Log.info("Started deleteCheckItem keyword");
        // get coordinates of Checklist
        MobileElement ChecklistItem = driver.findElement(OR.todo.getLocatorDynamic("checklist", new String[]{title}));
        int X = ChecklistItem.getLocation().getX(); //0
        int Y = ChecklistItem.getLocation().getY();
        X = X + 155;
        Y = Y + 20;
        int X1 = X-200;
        int Y1 = Y;
        // Action swipe right to left
        int[] swipeRightToLeftChecklist = {X,Y,X1,Y1};
        driver.touchActionSwipe(swipeRightToLeftChecklist);
        Log.info("Delete item > "+title);
        // Delete Checklist
        driver.tapElement(OR.todo.getLocator("deleteChecklist"));
        Log.info("Ended deleteCheckItem keyword");
    }

    /**
     * Check a to.do on top of to.do list
     * @param title
     * @param testcase
     * @return
     */
    public boolean checkTodoOnTop(String title, String testcase){
        Log.info("Started checkTodoOnTop keyword");
        boolean flag = true;
        try{
            driver.isElementExist(OR.todo.getLocatorDynamic("todoOnTop",new String[]{title}));
            Log.info("Passed - ToDo "+title+" is on top");
        }catch (Exception e){
            Log.info("Failed - ToDo "+title+" is not on top");
            flag = false;
            driver.captureScreenShot(testcase);
        }
        Log.info("Ended CheckTodoOnTop keyword");
        return flag;
    }

    /**
     * Send an email from to.do details and click on Email icon
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
    public void sendEmailByDetails(String from, String to, String cc, String bcc, String subject, String content, String numImage, String option, String collections ,boolean isSent){
        Log.info("Started sendEmailByDetails keyword");
        Log.info("Clicking Email icon");
        driver.findElement(OR.todo.getLocator("toolBarEmail")).click();
        driver.Wait(5);
        // Action to show header of Compose Email view
        driver.touchActionSwipe(190,115,190,315);
        driver.Wait(2);
        email.sendEmail(from,to,cc,bcc,subject,content,numImage, option,collections,isSent);
        driver.Wait(10);
        Log.info("Ended sendEmailByDetails keyword");
    }

    /**
     * Add new simple note from to.do details and click on Note icon
     * @param newNoteTitle
     * @param content
     * @param isDone
     */
    public void addNoteByDetails(String newNoteTitle, String content, boolean isDone){
        Log.info("Started addNoteByDetails keyword");

        Log.info("Clicking Note icon");
        driver.findElement(OR.todo.getLocator("toolBarNote")).click();
        // Wait and go to header Note View
        driver.Wait(2);
        driver.touchActionSwipe(190,115,190,415);

        // Input New Note's title
        MobileElement TitleNote  =  driver.findElement(OR.note.getLocator("txbTitle"));

        Log.info("Delete old title");
        // long press + Select All + Cut
        TitleNote.click();
        driver.longPressElement(OR.note.getLocator("txbTitle"));
        driver.Wait(2);
        common.clickMenuItemTextButton("Select All");
        driver.Wait(2);
        common.clickMenuItemTextButton("Cut");

        Log.info("Input new title > "+ newNoteTitle);
        TitleNote.setValue(newNoteTitle);
        if(!content.equals("")){
            Log.info("Input content > "+ content);
            MobileElement tbContent  =  driver.findElement(OR.note.getLocator("txaContent"));
            tbContent.click();
            tbContent.setValue(content);
        }
        // Tap on "Done" button
        if (isDone == true)
            common.clickElementButton("Done");
        Log.info("Ended addNoteByDetails keyword");
    }

    /**
     * Check new to.do view show default as setting and click Cancel to back To.do view
     * For all params: """" - skip this check
     * @param done: Done/ UnDone
     * @param star: Star/ UnStar
     * @param dueDate: None, Today, Tomorrow, End of Work Week, End of Month
     * @param duration: None, Short, Medium, Long
     * @param alertDate: None, On Due Date, 1 Day Before, 2 Days Before, 3 Days Before, 7 Days Before, 1 Month Before
     * @param collection
     * @param testcase
     * @return
     */
    public boolean checkNewTodoView(String done, String star, String dueDate, String duration, String alertDate, String collection, boolean isCancel, String testcase){
        Log.info("Started checkNewTodoView keyword");
        boolean flag = true;
        // Check Done/Undone
        if(!done.equals("")){
            if(done.equals("Done")){
                try{
                    driver.isElementExist(OR.todo.getLocator("icoDone"));
                    Log.info("Passed - ToDo is marked as Done");
                }catch (Exception e){
                    Log.info("Failed - ToDo is not marked as Done");
                    flag = false;
                }
            }else if(done.equals("UnDone")){
                try{
                    driver.isElementExist(OR.todo.getLocator("icoUndone"));
                    Log.info("Passed - Default ToDo is not marked as Done");
                }catch (Exception e){
                    Log.info("Failed - Default ToDo is marked as Done");
                    flag = false;
                }
            }
        }
        // Check auto is Starred/is Not Starred
        if(!star.equals("")){
            if(star.equals("Star")){
                try{
                    driver.isElementExist(OR.todo.getLocator("icoStar"));
                    Log.info("Passed - Default ToDo is marked as Star");
                }catch (Exception e){
                    Log.info("Failed - Default ToDo is not marked as Star");
                    flag = false;
                }
            }else if(star.equals("UnStar")){
                try{
                    driver.isElementExist(OR.todo.getLocator("icoUnStar"));
                    Log.info("Passed - Default ToDo is not marked as Star");
                }catch (Exception e){
                    Log.info("Failed - Default ToDo is marked as Star");
                    flag = false;
                }
            }
        }
        // Check Default Due date
        Date date = new Date();
        String dateCurrent = new SimpleDateFormat("EEE, dd MMM").format(date);

        Calendar cal = Calendar.getInstance();
        // Tomorrow
        cal.add(Calendar.DATE, 1);
        String tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());

        // Date End of Work Week
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.DAY_OF_WEEK,cal1.SUNDAY);
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
        cal1.add(Calendar.DATE, 6);
        String dateEndWeek = dateFormat.format(cal1.getTime());

        // Date End of Month
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DAY_OF_MONTH,cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
        String dateEndMonth = new SimpleDateFormat("EEE, dd MMM").format(cal2.getTime());

        if(!dueDate.equals("")){
            // Check Default Due Date: Today
            if(dueDate.equals("Today")){
                dueDate = dateCurrent;
                if(dueDate == dateCurrent){
                    dueDate = "Due Today";
                }
            // Check Default Due Date: Tomorrow
            }else if(dueDate.equals("Tomorrow")){
                dueDate = tomorrow;
                if(dueDate == tomorrow){
                    dueDate = "Due Tomorrow";
                }
            // Check Default Due Date: End of Work Week
            }else if(dueDate.equals("End of Work Week")){
                if(dateEndWeek.equals(dateCurrent)){
                    dueDate = "Due Today";
                }else if(dateEndWeek.equals(tomorrow)){
                    dueDate = "Due Tomorrow";
                }else{
                    dueDate = "Due "+dateEndWeek;
                }
            }
            // Check Default Due Date: End of Month
            else if(dueDate.equals("End of Month")){
                if(dateEndMonth.equals(tomorrow)){
                    dueDate = "Due Tomorrow";
                }else if(dateEndMonth.equals(dateCurrent)){
                    dueDate = "Due Today";
                } else{
                    dueDate = "Due "+dateEndMonth;
                }
            }
            // Check Default Due Date: None
            else if(dueDate.equals("None")){
                dueDate = null;
            }
            try{
                String actualText = driver.findElement(OR.todo.getLocator("txbDueDate")).getAttribute("name");
                Assert.assertEquals(actualText,dueDate);
                Log.info("Passed - Default Due date: "+dueDate);
            }catch (AssertionError aE){
                Log.info("Failed - Default Due date - "+aE.getMessage());
                flag = false;
            }
        }

        // Check Default Duration
        if(!duration.equals("")){
            String[] Duration={};
            switch (duration){
                case "None":
                    Duration = new String[]{"\uE02A"};
                    duration = null;
                    break;
                case "Short":
                    Duration= new String[]{"\uE02B"};
                    duration = "30 mins";
                    break;
                case "Medium":
                    Duration= new String[]{"\uE028"};
                    duration = "2 hrs";
                    break;
                case "Long":
                    Duration= new String[]{"\uE024"};
                    duration = "5 hrs";
                    break;
            }
            try {
                String txtDuration = driver.findElement(OR.todo.getLocatorDynamic("txtDuration",Duration)).getAttribute("value");
                Assert.assertEquals(txtDuration,duration);
                Log.info("Passed - Default Duration is: "+duration);
            }catch (AssertionError aE){
                Log.info("Failed - Default Duration - "+aE.getMessage());
                flag = false;
            }
        }
        // Check Default Alert Date
        if(!alertDate.equals("")){
            if(alertDate.equals("None")){
                try {
                    driver.isElementExist(OR.todo.getLocator("alertDueDate"));
                    String actualText = driver.findElement(OR.todo.getLocator("alertDueDate")).getAttribute("value");
                    Log.info("Failed - Default ToDo has Due date Alert is "+actualText);
                    flag = false;
                }catch (Exception e){
                    Log.info("Passed - Default ToDo has None Due date Alert");
                }
            } else {
                switch (alertDate){
                    case "On Due Date":
                        alertDate = "On Due of ToDo";
                        break;
                    case "1 Day Before":
                        alertDate = "1 Day";
                        break;
                    case "2 Days Before":
                        alertDate = "2 Days";
                        break;
                    case "3 Days Before":
                        alertDate = "3 Days";
                        break;
                    case "7 Days Before":
                        alertDate = "7 Days";
                        break;
                    case "1 Month Before":
                        alertDate = "1 Month";
                        break;
                }
                try {
                    String actualText = driver.findElement(OR.todo.getLocator("alertDueDate")).getAttribute("value");
                    Assert.assertEquals(actualText,alertDate);
                    Log.info("Passed - Default Due date Alert is: "+alertDate);
                }catch (AssertionError aE){
                    Log.info("Failed - Default Due date Alert - "+aE.getMessage());
                    flag = false;
                }
            }
        }
        // Check Default Collection
        if(!collection.equals("")){
            String Collection = " "+collection+" ";
            try {
                String actualText = driver.findElement(OR.todo.getLocator("defautlCollection")).getAttribute("value");
                Assert.assertEquals(actualText,Collection);
                Log.info("Passed - ToDo belong to Default Collection: "+collection);
            }catch (AssertionError aE){
                Log.info("Failed - Default Collection Error - "+aE.getMessage());
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

    /**
     * ToDo review again
     * Check to.do item belong a group in due to.do view
     * @param group: Overdue,Today, Next 3 days, Next 10 days, More than 10 days, No due date
     * @param title
     */
    public boolean checkGroupDueTodo(String group, String title, String testCase){
        Log.info("Started checkGroupDueTodo keyword");
        boolean flag = true;
        if(group.equals("Overdue")){
            try{
                driver.isElementExist(OR.todo.getLocatorDynamic("groupOverdue1",new String[]{title}));
                Log.info("Passed - ToDo "+title+" in "+group+ " Group");
            }catch (Exception e){
                Log.info("Failed - ToDo "+title+" does not in "+group+ " Group");
                flag = false;
            }
        }else if(group.equals("Today")){
            try{
                driver.isElementExist(OR.todo.getLocatorDynamic("groupDueToday",new String[]{title}));
                Log.info("Passed - ToDo "+title+" in "+group+ " Group");
            }catch (Exception e){
                Log.info("Failed - ToDo "+title+" does not in "+group+ " Group");
                flag = false;
            }

        }else if(group.equals("Next 3 days")){
            try{
                driver.isElementExist(OR.todo.getLocatorDynamic("groupDueNext3days",new String[]{title}));
                Log.info("Passed - ToDo "+title+" in "+group+ " Group");
            }catch (Exception e){
                Log.info("Failed - ToDo "+title+" does not in "+group+ " Group");
                flag = false;
            }
        }else if(group.equals("Next 10 days")){
            try{
                driver.isElementExist(OR.todo.getLocatorDynamic("groupDueNext10days",new String[]{title}));
                Log.info("Passed - ToDo "+title+" in "+group+ " Group");
            }catch (Exception e){
                Log.info("Failed - ToDo "+title+" does not in "+group+ " Group");
                flag = false;
            }
        }else if(group.equals("More than 10 days")){
            try{
                driver.isElementExist(OR.todo.getLocatorDynamic("groupDueThan10days",new String[]{title}));
                Log.info("Passed - ToDo "+title+" in "+group+ " Group");
            }catch (Exception e){
                Log.info("Failed - ToDo "+title+" does not in "+group+ " Group");
                flag = false;
            }
        }else if(group.equals("No due date")){
            try{
                driver.isElementExist(OR.todo.getLocatorDynamic("groupNoDueDate1",new String[]{title}));
                Log.info("Passed - ToDo "+title+" in "+group+ " Group");
            }catch (Exception e){
                Log.info("Failed - ToDo "+title+" does not in "+group+ " Group");
                flag = false;
            }
        }
        if(flag == false){
            driver.captureScreenShot(testCase);
        }
        Log.info("Ended checkGroupDueTodo keyword");
        return flag;
    }

    /**
     * Add new full To.Do with some options
     * @param title Input To.Do's title
     * @param isStar True - mark Star To.Do/ False - not mark Star To.Do
     * @param isDone True - mark Done To.Do/ False - not mark Done To.Do
     * @param dueDate: None, Today, Tomorrow, One Week
     * @param duration: None, Short, Medium, Long
     * @param Description: input Description
     * @param collection: 1 or many collections, MUST have ‘comma’ for each item. Ex: General,Play,Home
     * @param isClickDone
     */
    public void addFullTodo(String title, boolean isStar, boolean isDone, String dueDate, String duration, String Description, String collection, boolean isClickDone){
        Log.info("Started addFullTodo keyword");
        driver.Wait(2);
        // Input Title
        if(!title.equals("")){
            Log.info("Input title > " + title);
            MobileElement tbTitle = driver.findElement(OR.todo.getLocator("txbTitle"));
            tbTitle.clear();
            tbTitle.setValue(title+"\n");
        }
        // Clicking Star icon
        if(isStar == true){
            Log.info("Set Star ToDo");
            driver.findElement(OR.todo.getLocator("icoUnStar")).click();
        }

        // Clicking Done/Undone icon
        if(isDone == true){
            if(driver.isElementPresent(OR.todo.getLocator("icoUndone"))) {
                Log.info("Set Done ToDo");
                driver.findElement(OR.todo.getLocator("icoUndone")).click();
            }
            else
                Log.info("Done is default");
        }
        else{
            if(driver.isElementPresent(OR.todo.getLocator("icoDone"))) {
                Log.info("Set Undone ToDo");
                driver.findElement(OR.todo.getLocator("icoDone")).click();
            }
            else
                Log.info("Undone is default");
        }


        // Input Due date
        if(!dueDate.equals("")){
            // Open popup Due Date
            MobileElement iconDueDate = driver.findElement(OR.todo.getLocator("txbDueDate"));
            iconDueDate.click();
            Log.info("Set Due date > "+dueDate);

            // Set due date
            common.clickElementButton(dueDate);
            if(!dueDate.equals("None")){
                Log.info("Close popup Due Date");
                driver.tapCoordinate(175, 385);
            }
        }

        // Input Duration
        if(!duration.equals("")){
            // Check Duration
            switch (duration.toLowerCase()){
                case "none":
                    duration = "None";
                    break;
                case "short":
                    duration = "Short (30 mins)";
                    break;
                case "medium":
                    duration = "Medium (2 hrs)";
                    break;
                case "long":
                    duration = "Long (5 hrs)";
                    break;
            }
            driver.findElement(OR.todo.getLocator("txbDuration")).click();
            Log.info("Select Duration > "+duration);
            driver.findElement(OR.todo.getLocatorDynamic("selDurationTime",new String[]{duration})).click();
        }

        // Input Description
        if(!Description.equals("")){
            Log.info("Input Descripntion > "+Description);
            MobileElement tbDescription = driver.findElement(OR.todo.getLocator("tbDescription"));
            tbDescription.setValue(Description);

            Log.info("Close keyboard");
            driver.tapCoordinate(175, 385);
            driver.Wait(1);
        }
        // input Collection
        if(!collection.equals("")){
            common.unselectCollection(true,false);
            common.addMoveToCollection(false,"Add", collection);
        }
        // Tap on Done button
        if (isClickDone == true){
            common.clickElementButton("Done");
        }
        Log.info("Ended addFullTodo Keyword");
    }

    /**
     * Set duration time for to.do
     * @param duration: None, Short, Medium, Long
     * @param clickDone
     */
    public void setDurationTime(String duration, boolean clickDone){
        Log.info("Started setDurationTime Keyword");
        String iconDuration = "\uE02B";
        // Open Datetime picker
        driver.findElement(OR.common.getLocatorDynamic("btnStaticButton", new String[]{iconDuration})).click();
        if(!duration.equals("")) {
            switch (duration) {
                case "None":
                    // Do nothing
                    break;
                case "Short":
                    duration = "Short (30 mins)";
                    break;
                case "Medium":
                    duration = "Medium (2 hrs)";
                    break;
                case "Long":
                    duration = "Long (5 hrs)";
                    break;
            }
            MobileElement txtDuration = driver.findElement(OR.todo.getLocatorDynamic("secDuration",new String[]{duration}));
            // Get coordinate Duration
            int X = txtDuration.getLocation().getX();
            int Y = txtDuration.getLocation().getY();
            driver.tapCoordinate(X,Y);
        }
        // Click Done button
        if(clickDone == true)
            common.clickElementButton("Done");
        Log.info("Ended setDurationTime Keyword");
    }

    /**
     * Open multi-select mode in To.Do View, then select an option (Ex: Mark Done, Mark UnDone, Collect, Trash)
     * @param items: 1 or multi To.do's have ‘comma’ for each item. Ex: Todo1,Todo2
     * @param isSelectAll: True - select all To.do's
     * @param options: Mark Done, Mark UnDone, Collect, Delete
     */
    public void selectMultiTodo(String items, boolean isSelectAll, String options){
        Log.info("Started selectMultiTodo Keyword");
        if(options.equals("Mark Done")){
            Log.info("Mark Done ToDo(s) selected");
            common.multiSelectMode(items,isSelectAll,options);
        }else if(options.equals("Mark UnDone")){
            Log.info("Mark UnDone ToDo(s) selected");
            common.multiSelectMode(items,isSelectAll,options);
        }else {
            common.multiSelectMode(items,isSelectAll,options);
        }
        Log.info("Ended selectMultiTodo Keyword");
    }
}

