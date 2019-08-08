package TEST;
import Environments.*;
import Keywords.*;
import Support.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.*;
import java.util.*;



/**
 * Demotestcase
 */

public class TestBi extends SetupServer {

    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    EmailKeywords email;
    SignInKeywords  signin;
    AgendaKeywords agenda;
    TodoKeywords todo;
    CommonKeywords common;
    ContactKeywords contact;
    CollectionKeywords collection;
    NoteKeywords note;


    String testCase="demo";
    String today = "";
    String tomorrow = "";
    String eventTime ="";
    String username = "bi_test23";
    String password = "123456";
    boolean flag = true;
    String gmailAccount = "flo.automationtest@gmail.com";

    String slogan = "Flo is simple and easy to learn.";
    String fristTip = "Tap the icons below to see emails, meetings, notes, contacts, etc.";
    String secondTip = "Swipe the bottom bar right to go Home (this screen.)";
    String thirdTip= "Swipe the bottom bar left for Settings.";

    String gmailAddress = "abc@gmail.com";
    String flomailAddress = username+Constants.MAIL_SERVER;
    boolean reset = true;
    String device = "iPhone 6s.12.2";
    @BeforeClass
    public void Setup(){

        settings = new SettingsKeywords(this);
        home = new HomeKeywords(this);
        email = new EmailKeywords(this);
        signin = new SignInKeywords(this);
        agenda = new AgendaKeywords(this);
        todo = new TodoKeywords(this);
        common = new CommonKeywords(this);
        contact = new ContactKeywords(this);
        collection = new CollectionKeywords(this);
        note = new NoteKeywords(this);
        if(reset == true){
            capabilities = new MobileCapabilities(device,reset);
            SetUp(capabilities);
            common.ignoreConnectAcountPopup();
            common.closeAccountNotification(false);

        } else {
            capabilities = new MobileCapabilities(device,reset);
            SetUp(capabilities);
//            signin.skipIntroduce();
            signin.openLoginScreen();
            signin.login(username, password, true);
            common.closeStartFlo();
            common.ignoreConnectAcountPopup();
            common.closeAccountNotification(false);

        }

        // Generate String today
        Date currentDate = new Date();
        today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
        Calendar cal = Calendar.getInstance();
        // 3
        cal.add(Calendar.DATE, 3);
        tomorrow = new SimpleDateFormat("EEE, dd MMM").format(cal.getTime());
    }



    String firstName1 = "Mary";
    String firstName2 = "Peter";
    String lastName1 = "Jane";
    String lastName2 = "Parker";
    String titleTodo = "ContactScripts05_Todo 1";
    String titleNote = "ContactScripts05_Note 1";
    // TC: Add Linked Contact for Contact in Detail
    String testCase3 = "TS008_ContactScripts05_Contact3_addLinkedContactInDetail";

@Test
public  void asd(){
    common.clickViewBottomBar("ToDo");
    todo.filterByType("Top","All");
    common.selectLocalFilter("abc,Home");
}
    @Test
    public void bi(){
        common.clickViewBottomBar("ToDo");
//        todo.addQuickTodo("Bi","None");
        todo.openCollectionBySwipe("Bi");
        common.clickElementButton("Cancel");
        todo.setDueDateBySwipe("Bi","3 days");
        todo.addNoteBySwipe("Bi","Note","",false);
        common.clickElementButton("Cancel");
        todo.sendEmailBySwipe("Bi","","abc@gmail.com","","","ABC"," bi 1","","Add","Home",false);
        common.clickElementButton("Delete Draft");
        todo.deleteTodoBySwipe("Bi",false);
        common.clickElementButton("Cancel");
    }
    String titleEvent1 = "CollectionScripts04_Event A";
    String firstName = "Contact";
    String lastName11 = "CollectionScripts04_A";
    String subjectEmail = "CollectionScripts04_Email A";
    // TC: Swipe left to right Contact and send an Email
    // TC: Compose new Email, Input all info then cancel save draft email
    String testCase31 = "TS005_EmailScripts01_Email3_composeThenCancelSaveDraftEmail";
    String subject = "TS005_EmailScripts01";
    String floEmailAddress  = "khuong001@123flo.com";
    String floEmailAddress1 = "khuong002@123flo.com";
    String floEmailAddress2 = "khuong003@123flo.com";
    // TC: Reply All Email By Swipe
    String testCase2 = "TS005_EmailScripts02_Email2_replyAllEmailBySwipe";
    String subject2 = "TS005_EmailScripts02_Email 2";
    // TC: Add To.do By Swipe --> Due Date: None
    String testCase64 = "TS005_EmailScripts02_Email6_addTodoBySwipe4";
    @Test
    public void TS005_EmailScripts02_Email6_addTodoBySwipe4(){
        Log.startTestCase(testCase64);
        common.clickViewBottomBar("Email");
        common.openNewItemView();
        email.sendEmail("",floEmailAddress1,"","",subject,"content","","Add","Home",true);
        // 2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",floEmailAddress1,subject,"Exist",testCase64);
        // 1. Add new To.do by swipe
        email.addNewTodoBySwipe(floEmailAddress1,subject,"None", "Play");
        // 2. Check To.do is existed
        common.clickViewBottomBar("ToDo");
        flag = todo.checkTodoItemView(subject, "Normal","",false,false,false,true,testCase64);
        // 3. Clean: Delete To.do by swipe
        todo.deleteTodoBySwipe(subject,true);
        // 4. Back to Emails
        common.clickViewBottomBar("Email");
        // 5. Delete Email
        email.deleteEmailBySwipe(floEmailAddress1,subject,true);
        Log.endTestCase(testCase64);
        if(!(flag))
            Assert.fail("Test case is failed");

    }
    String testCase9 = "TS005_EmailScripts02_Email9_moveEmailToOtherCollectionBySwipe";
    String subject8 = "TS005_EmailScripts02_Email 8";
    @Test
    public void TS005_EmailScripts02_Email9_moveEmailToOtherCollectionBySwipe(){
        Log.startTestCase(testCase9);
        // 1. Open create new email
        common.clickViewBottomBar("Email");
        common.openNewItemView();
        email.sendEmail("",floEmailAddress1,"","",subject8,"content","","Add","Home",true);
        //2. Check email is existed
        boolean flag1 = email.checkEmailExist("Sent",floEmailAddress1,subject8,"Exist",testCase9);
        //3. Open Collection By swipe
        email.openCollectionBySwipe(floEmailAddress1,subject8);
        //4. Move Email to "Play "Collection
        common.addMoveToCollection(false,"Move","Play");
        //5. Check Email is Existed in "Play "Collection
        boolean flag2 = email.checkEmailExist("Flo Collections>Play",floEmailAddress,subject8,"Exist",testCase9);
        //6. Check Detail Email
        email.openEmailItem(flomailAddress,subject8);
        flag = email.checkEmailItemDetail("","","","","",false,"","Play","",testCase9);
        //7. Delete email
        common.trashItemByDetails(true);
        Log.endTestCase(testCase9);
        if(!(flag&flag1&flag2))
            Assert.fail("Test case is failed");
    }

//    public static void main(String[] args) {
//        // Get and return dateTime
//        Date currentDate = new Date();
//       String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);
//        String strDateTime = "Today at 19:05AM";
//        // Cut time string
//        String strTime = strDateTime.split(" at ", 2)[1];
//        System.out.println(strTime);
//        String strTime1 = (strTime.length() == 6) ? "0" + strTime : strTime;
//        System.out.println(strTime1);
//        // Get value AM or PM
//        String AMPM = strTime1.substring(5);
//        System.out.println(AMPM);
//
//        // Convert format string Ex: 12:00PM -> 12:00 PM
//        String noteTime = strTime.replace(AMPM," " +AMPM);
//        System.out.println("DateTime is " + strDateTime);
//        System.out.println("NoteTime is " + noteTime);
//
//        System.out.println("----------");
//
//        String NoteTime = (noteTime.length() == 7) ? "0" + noteTime : noteTime;
//        System.out.println(NoteTime);
//        int hour = Integer.parseInt(NoteTime.substring(0,2));
//        System.out.println(hour);
//        String strTime2 = (hour > 9) ? NoteTime.substring(0,5) : NoteTime.substring(1,5);
//        System.out.println(strTime2);
//        String AMPM3 = NoteTime.substring(6);
//        System.out.println(AMPM3);
//        String  noteDateTime = today + " at " + strTime2 + AMPM3;
//        System.out.println(noteDateTime);
//    }

}
