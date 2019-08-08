package TEST;

import Environments.Constants;
import Environments.MobileCapabilities;
import Support.Log;
import Support.SendEmailUtils;
import Support.SetupServer;
import org.testng.Assert;
import org.testng.annotations.*;
import Keywords.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sample2 extends SetupServer{
    MobileCapabilities capabilities;
    boolean flag = true;
    String username = "flo_automation";
    String password = "123456";
    SignInKeywords signin;
    SignUpKeywords signup;

    CommonKeywords common;
    HomeKeywords home;
    TodoKeywords todo;
    NoteKeywords note;

    String device = "iPhone 6s.11.4";
    String buildVersion = "Version 0.9.11 - build 201808060935";

    Date currentDate = new Date();
    String today = new SimpleDateFormat("EEE, dd MMM").format(currentDate);

    @BeforeClass
    public void setUp(){
        capabilities = new MobileCapabilities(device,false);
        SetUp(capabilities);

        signin = new SignInKeywords(this);
        home = new HomeKeywords(this);
        todo = new TodoKeywords(this);
        common = new CommonKeywords(this);
        signup = new SignUpKeywords(this);
        note = new NoteKeywords(this);

        // Sign in Flo iPhone
        signin.skipIntroduce();
        signin.openLoginScreen();
        signin.login(username, password, true);

        // Close start Flo popup
        common.closeStartFlo();
    }

    @Test
    public void TS001_Sample02_Sample1(){
        Log.info("Run");
    }

    @Test
    public void TS001_Sample02_Sample2(){
        Log.info("Run");
    }

    @Test
    public void TS001_Sample02_Sample3(){
        Log.info("Run");
    }

    @Test
    public void TS001_Sample02_Sample4(){
        Log.info("Run");
    }

    @Test
    public void TS001_Sample02_Sample5(){
        Log.info("Run");
    }

    @AfterClass
    public void cleanUp(){
        driver.closeApp();
    }

//    @AfterSuite
//    public void sendMail(){
//        if(flag)
//            sendEmail.sendGmail(this.getClass().getSimpleName(), device, buildVersion, Constants.EMAIL_CC, "", false);
//    }
}
