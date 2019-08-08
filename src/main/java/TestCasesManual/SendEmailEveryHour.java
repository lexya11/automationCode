package TestCasesManual;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.SetupServer;
import org.testng.annotations.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SendEmailEveryHour extends SetupServer {
    MobileCapabilities capabilities;
    CollectionKeywords collection;
    HomeKeywords home;
    AgendaKeywords agenda;
    SignInKeywords signin;
    NoteKeywords note;
    TodoKeywords todo;
    EmailKeywords email;
    CommonKeywords common;
    ContactKeywords contact;

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;


    String floEmailAddress  = "flotest@iic.co.jp,htmh99999@flomail.net,htmh999@flomail.net,hanhhuynh15@gmail.com,flotest@flo.chat";
    String cc  = "automationqcflo@gmail.com";
    SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    @BeforeClass
    public void SetUp(){
        Log.startTestCase("Set Up "+this.getClass().getSimpleName());
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        home = new HomeKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);
        note = new NoteKeywords(this);
        todo = new TodoKeywords(this);
        signin = new SignInKeywords(this);
        contact = new ContactKeywords(this);
        capabilities = new MobileCapabilities(device, false);
        SetUp(capabilities);

        // Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);
        // Close Start Flo popup
        common.closeStartFlo();
        common.clickViewBottomBar("Email");
    }



    // TC: Compose new Email, send email has image in content
    String testCase9 = "sendEmailHasImageAttached";

    @Test
    public void TS_sendEmailHasImageAttached(){
        Log.startTestCase(testCase9);
        // 1. Tap on compose icon on top right
        common.openNewItemView();
        Calendar cal = Calendar.getInstance();
        String subject = "[Automation iPhone] - Email 1 - "+ date.format(cal.getTime());
        // 2. Input all info, add an image in content
        email.sendEmail("",floEmailAddress,cc,"",subject,"Hello","1","Add","Home",true);
        email.navigateToImapFolder("All Sent");
        Wait(120);
        email.checkEmailExist("","flotest@iic.co.jp",subject,"Exist",subject);
        for (int i=2 ; i<=2000;i++){
            Wait(3260);
            cal = Calendar.getInstance();
            common.openNewItemView();
            subject = "[Automation iPhone] - Email "+i+" - "+ date.format(cal.getTime());
            email.sendEmail("",floEmailAddress,cc,"",subject,"Hello","1","Add","Home",true);
            Wait(120);
            email.checkEmailExist("","flotest@iic.co.jp",subject,"Exist",subject);
        }
    }

    @AfterClass
    public void clearUp(){

    }


}
