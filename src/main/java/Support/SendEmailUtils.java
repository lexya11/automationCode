package Support;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import Environments.Constants;
import org.apache.log4j.Logger;
import org.testng.ITestContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class SendEmailUtils {
    final String host = "mail.flomail.net";
    final String port = "25";
    final String hostGmail = "smtp.gmail.com";
    final String portGmail = "587";

    final String fromFlo = "tu_qa@flomail.net";
    final String passFlo = "123456";
    final String fromGmail = "automationqcflo@gmail.com";
    final String passGmail = "Aa@12345";
    ZipFileUtils zip;
    String[] results;

    // Generate String today
    Date currentDate = new Date();
    String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(currentDate);

    final String content = "This is automation report, please see details in attachment !!!";
    final String prefix = "[Automation Report] - ";

    // Log variables
    static Logger Log = Logger.getLogger(SetupServer.class);

    public void getResults(ITestContext context){
        Log.info("Get run automation results from context");
        String pass = String.valueOf(context.getPassedTests().size());
        String fail = String.valueOf(context.getFailedTests().size());
        String skip = String.valueOf(context.getSkippedTests().size());
        String total = String.valueOf(context.getPassedTests().size() + context.getFailedTests().size() + context.getSkippedTests().size());
        results = new String[] {total, pass, fail, skip};
    }

    public void sendGmail(String scriptName, String device, String buildVerion, String toMail, String ccMail) {
        String subject = prefix + "Script: "+ scriptName +" - Result: " + results[1] + " Passed/ "+ results[2] + " Failed - Device: "
                + device + " - " + buildVerion + " - Run at: " + dateTime ;
        String content =
                "Hi all,<br><br>"+
                "This is automation report of <b style='color:blue;'>"+ scriptName +"</b> on device <b style='color:red;'>"+
                device + " - " + buildVerion + "</b>.<br>Script is run at "+dateTime +
                ".<br>**********************************************<br>" +
                "<b>Total test cases: "+ results[0] +
                ".</b><br><b style='color:blue;'>Passed: "+ results[1] +
                ".</b><br><b style='color:red;'>Failed: "+ results[2] +
                ".</b><br><b style='color:magenta;'>Skipped: "+ results[3] +
                ".</b><br>**********************************************" +
                "<br>Please see details in attachment !!!<br><br>" +
                "Thanks,<br>" +
                "Automation Team<br>";

        Log.info("Started Send Gmail Email keyword");
        // Create object of Property file
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostGmail);
        props.put("mail.smtp.port", portGmail);

        // create session authentication
        Authenticator auth = new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromGmail, passGmail);
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        Log.info("Session created");

        // Generate file attachment string
        zip = new ZipFileUtils();
        String htmlReport;
        String zipFile = Constants.REPORT_PATH +".zip";
        if(Constants.RUN_XML == true)
            htmlReport = Constants.HTML_FILE_XML_PATH + scriptName + ".html";
        else
            htmlReport = Constants.HTML_FILE;
        // Zip file attachment
        zip.zipFileReport(htmlReport);
        Log.info("Report attachment created");

        // Send mail report
        sendMail(session, fromGmail, toMail, ccMail, subject, content,new String[]{htmlReport, zipFile});
        String info = "Report is sent to: " + toMail + (ccMail.equalsIgnoreCase("") ? "" : " and cc to: "+ ccMail);
        Log.info(info);
    }

    public void sendFlomail(String toMail, String ccMail){
        String subject = prefix + dateTime;

        Log.info("Started Send Flo Email keyword");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromFlo, passFlo);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        Log.info("Session created");
        sendMail(session, fromFlo, toMail, ccMail, subject, content,new String[]{Constants.HTML_FILE, Constants.REPORT_PATH+".zip"});
        Log.info("=> Report is sent to: " + toMail + " and cc to: "+ ccMail +" ==");
    }

    public void sendMail(Session session, String fromEmail, String toEmail, String ccEmail, String subject, String content, String[] attachFiles){
        try {
            // Create object of MimeMessage class
            Message message = new MimeMessage(session);
            // Set the from address
            message.setFrom(new InternetAddress(fromEmail,"NoReply - Flo Automation"));

            // Set the recipient address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            if(!ccEmail.equalsIgnoreCase(""))
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));

            // Add the subject
            message.setSubject(subject);
            // Create object to text content
            BodyPart messageBodyPart1 = new MimeBodyPart();
            // Set the body of email
            messageBodyPart1.setContent(content,"text/html");

            // Create object of MimeMultipart class
            Multipart multipart = new MimeMultipart();
            // add body text
            multipart.addBodyPart(messageBodyPart1);

            // adds attachments
            if (attachFiles != null && attachFiles.length > 0) {
                for (String filePath : attachFiles) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    try {
                        attachPart.attachFile(filePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    multipart.addBodyPart(attachPart);
                }
            }
            // set the content
            message.setContent(multipart);
            // finally send the email
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException Ue){
            throw new RuntimeException(Ue);
        }
    }
}
