package TestCasesManual;

import Environments.Constants;
import Environments.MobileCapabilities;
import Keywords.*;
import Support.SetupServer;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TS002_SendEmail extends SetupServer {
    MobileCapabilities capabilities;
    HomeKeywords home;
    SignInKeywords signin;
    EmailKeywords email;
    CommonKeywords common;
    boolean flag = true;

    // Logger variable
    static Logger Log = Logger.getLogger(EmailKeywords.class);

    String username = Constants.USERNAME;
    String password = Constants.PASSWORD;
    String device = Constants.DEVICE;

    String emailTo = "automation_01@123flo.com,anhthuy@leftcoastlogic.com";
    String emailCC = "automation_02@123flo.com";
    String emailBCC = "automation_03@123flo.com";

    @BeforeClass
    public void SetUp(){
        capabilities = new MobileCapabilities(device, true);
        common = new CommonKeywords(this);
        home = new HomeKeywords(this);
        email = new EmailKeywords(this);
        signin = new SignInKeywords(this);
        SetUp(capabilities);

//        // Sign in Flo iPhone
//        signin.skipIntroduce();
//        signin.openLoginScreen();
//        signin.login(username, password, true);
//
//        // Close Start Flo popup
//        home.closeStartFlo();

        // Open Email view
        common.clickViewBottomBar("Email");
    }

    @Test
    public void Email1_sendEmail(){
        for(int i=1; i<=50; i++) {
            Log.info("Send email > "+i);
            // 1. Tap on compose icon on top right
            common.openNewItemView();
            // 2. Input the recipient in To field, then tap on "Send" button
            email.sendEmail("", emailTo, emailCC, emailBCC, "Email "+i, "Send email number "+i, "1", "Add",
                    "General", true);
        }
    }
}
