package TestCasesManual;

import Environments.Constants;
import Support.Log;
import Support.SendEmailUtils;
import org.testng.ITestContext;
import org.testng.annotations.*;

public class SendEmailScripts {
    SendEmailUtils email;
    String device = Constants.DEVICE;
    String buildVersion = Constants.BUILD_VERSION;

    @AfterSuite
    public void sendMail(ITestContext context) {
        Log.startTestCase("Send Email Report");
        email = new SendEmailUtils();
        email.getResults(context);
        email.sendGmail(context.getCurrentXmlTest().getName(), device, buildVersion, Constants.EMAIL_TO, Constants.EMAIL_CC);

        // Only send to automationqcflo@gmail.com
//        email.sendGmail(context.getCurrentXmlTest().getName(), device, buildVersion, "flo_automation@flomail.net","");
    }
}
