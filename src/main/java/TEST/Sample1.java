package TEST;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.Log;
import Support.SendEmailUtils;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sample1 extends SetupServer{
    MobileCapabilities capabilities;
    boolean flag = false;
    String username = "flo_automation";
    String password = "123456";
    String device = Constants.DEVICE;

    SignInKeywords signin;
    CommonKeywords common;
    HomeKeywords home;
    TodoKeywords todo;
    AgendaKeywords agenda;
    CollectionKeywords collection;
    ContactKeywords contact;
    NoteKeywords note;
    EmailKeywords email;
    SettingsKeywords settings;

    Date currentDate = new Date();
    String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

    @BeforeClass
    public void setUp(){
        capabilities = new MobileCapabilities(device, false);
        SetUp(capabilities);

        signin = new SignInKeywords(this);
        home = new HomeKeywords(this);
        todo = new TodoKeywords(this);
        common = new CommonKeywords(this);
        agenda = new AgendaKeywords(this);
        collection = new CollectionKeywords(this);
        contact = new ContactKeywords(this);
        note = new NoteKeywords(this);
        email = new EmailKeywords(this);
        settings = new SettingsKeywords(this);

        // Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);
        common.closeStartFlo();
        home.openNewScreenPlusIcon("Event");
    }

    @Test
    public void TS001_Sample_Sample1(){
        driver.launchApp();
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);
        common.closeStartFlo();
    }

    @AfterClass
    public void cleanUp(){
//        driver.closeApp();
    }
}
