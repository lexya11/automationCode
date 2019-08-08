package TestCasesManual;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.ExcelUtils;
import Support.Log;
import Support.SetupServer;
import org.junit.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TS001_GmailScripts extends SetupServer {
    //     Pre-condition:
    //     Login User, Flo acc added@ Gmail before run script
    //     User :automationqcflo@gmail.com
    //     PWD : Aa@12345
    MobileCapabilities capabilities;
    SettingsKeywords settings;
    HomeKeywords home;
    CommonKeywords common;
    SignInKeywords signin;
    CollectionKeywords collection;
    TodoKeywords todo;
    NoteKeywords note;
    AgendaKeywords agenda;
    EmailKeywords email;

    // Test case variables
    boolean flag = true;
    String username = "auto_qc1";
    String password = "qwe123";

    @BeforeClass
    public void setUp(){
        capabilities = new MobileCapabilities("iPhone 6s.1",true);
        SetUp(capabilities);
        signin = new SignInKeywords(this);
        home = new HomeKeywords(this);
        settings = new SettingsKeywords(this);
        common = new CommonKeywords(this);
        collection = new CollectionKeywords(this);
        todo = new TodoKeywords(this);
        note = new NoteKeywords(this);
        agenda = new AgendaKeywords(this);
        email = new EmailKeywords(this);

        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Email");
        common.closeSendNotification(true);
    }


    /**************************
     * Setting > Email
     */
    String testCase1 = "Set Default From";
    String gmail = "automationqcflo@gmail.com";
    String floEmail= "auto_qc1@123flo.com";
    @Test
    public void SettingEmail1_setDefaultFrom(){
        Log.startTestCase(testCase1);
        //Set Default Email is Gmail
        settings.setDefaultEmail("Gmail");
        common.swipeBottomLeftToRight();
        home.openNewScreenPlusIcon("Email");
        flag = email.checkNewEmailView(gmail,"",testCase1);
        if(!flag)
            Assert.fail();
        //Set Default Email is Flo
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Email");
        settings.setDefaultEmail("Flo");
        common.swipeBottomLeftToRight();
        home.openNewScreenPlusIcon("Email");
        flag = email.checkNewEmailView(floEmail,"",testCase1);
        if(!flag)
            Assert.fail();
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Email");

    }

    String testCase2 = "Set Default From";
    @Test
    public void SettingEmail2_setSignatureForGmail(){
        Log.startTestCase(testCase2);
        //Set Signature for FloEmail
        settings.setDefaultEmail("Gmail");
        settings.addEmailSignature(gmail,"Gmail Signature");
        common.swipeBottomLeftToRight();
        home.openNewScreenPlusIcon("Email");
        // Check Signature is available
        flag = email.checkNewEmailView(gmail,"Gmail Signature",testCase2);
        if(!flag)
            Assert.fail();
        home.clickTopLeftbutton("Settings");
        settings.clickSettingOptionList("Email");
    }

    @AfterClass
    public void clearUp(){
        driver.closeApp();
    }

}
