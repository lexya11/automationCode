package TEST;

import Environments.Constants;
import Support.SetupServer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;

public class SampleTu {
    String host = "mail.flomail.net";
    String port = "25";
    String hostGmail = "smtp.gmail.com";
    String portGmail = "587";

    String fromFlo = "tu_qa@flomail.net";
    String passFlo = "123456";
    String fromGmail = "automationqcflo@gmail.com";
    String passGmail = "Aa@12345";

    String hostOL = "smtp.office365.com";
    String portOL = "587";
    String fromOL = "tu_flo@outlook.com.vn";
    String passOL = "@Aa654321";

    String content = "This is automation report, please see details in attachment !!!";
    String prefix = "[Automation Report] - ";


    public SampleTu(){
        DOMConfigurator.configure("log4j.xml");
    }

    // Log variables
    static Logger Log = Logger.getLogger(SetupServer.class);

    public static void main(String[] args) throws Exception {
        SampleTu x = new SampleTu();

        for(int i = 1; i <= 10; i++ ) {
            Log.info("Send email "+ i);
            x.sendMail1(i);
        }
    }

    public void sendMail1(int num){
        // Generate String today
        Date currentDate = new Date();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(currentDate);

        String generatedString = RandomStringUtils.randomAlphanumeric(15);
        System.out.println(generatedString);

        String subject = prefix + dateTime + " - Email number " + num;

        Log.info("Started Send Flo Email keyword");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostOL);
        //props.put("mail.smtp.ssl.trust", hostGmail);
        props.put("mail.smtp.port", portOL);

        Authenticator auth = new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromOL, passOL);
            }
        };

        Session session = Session.getInstance(props, auth);
        Log.info("Session created");
        sendMail(session, generatedString + "@gmail.com", "automationqcflo@gmail.com", subject, content,null);
        String info = "Email "+ num +" is sent "+ generatedString + "@gmail.com OK";
        Log.info(info);
    }

    public void sendMail(Session session, String toEmail, String ccEmail, String subject, String content, String[] attachFiles){
        try {
            // Create object of MimeMessage class
            Message message = new MimeMessage(session);
            // Set the from address
            message.setFrom(new InternetAddress(fromOL,"NoReply - Flo Automation"));
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
