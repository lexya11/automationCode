package TestCasesManual;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.SetupServer;
import org.testng.annotations.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SendEmailLoopInTime extends SetupServer {
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


    String[] floEmails  = {
            "khoi.floware@gmail.com",
            "mik.tran6670@gmail.com",
            "miktran1994@yahoo.com",
            "khoi.tm6670@gmail.com"
            };
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
    String testCase = "sendEmailHasImageAttached";

    @Test
    public void TS_sendEmailHasImageAttached(){
        Log.startTestCase(testCase);

        String content = "\n" +
                "This screenshot shows the Inbox page of an email client, where users can see new emails and take actions, such as reading, deleting, saving, or responding to these messages.\n" +
                "The at sign, a part of every SMTP email address[1]\n" +
                "Electronic mail (email or e-mail) is a method of exchanging messages (\"mail\") between people using electronic devices. Invented by Ray Tomlinson, email first entered limited use in the 1960s \n" +
                "and by the mid-1970s had taken the form now recognized as email.";
        int j = 0;
        for (int i = 1; i<= Constants.NUM_LOOP; i++) {
            // 1. Tap on compose icon on top right
            Log.info(floEmails[j]);
            common.openNewItemView();
            Calendar cal = Calendar.getInstance();
            String subject = "[Automation iPhone] - Email "+ i +" to "+ floEmails[j] +" - "+ date.format(cal.getTime());

            // 2. Input all info, add an image in content
            email.sendEmail("", floEmails[j], cc, "", subject, content, "1", "Add", "General", true);
            Wait(180);
            j++;
            if(j>4) j = 0;
        }
    }

    @AfterClass
    public void clearUp(){
    }
}
