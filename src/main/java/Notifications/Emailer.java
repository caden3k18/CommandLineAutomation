package Notifications;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Emailer {

    //These settings need to be changed to your own...
    private static String SMTP_HOST = "smtp-mail.outlook.com";
    private static String FROM_ADDRESS = "admin@supportemail.com";
    private static String PASSWORD = "YourPasswordHere";
    private static String FROM_NAME = "Admin";

    public static void sendMail(String[] recipients, String subject, String message) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "false");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new EmailAuth());

            Message body = new MimeMessage(session);
            body.setSubject(subject);
            body.setContent(message, "text/plain");

            InternetAddress from = new InternetAddress(FROM_ADDRESS, FROM_NAME);
            body.setFrom(from);

            InternetAddress[] toAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                toAddresses[i] = new InternetAddress(recipients[i]);
            }
            body.setRecipients(Message.RecipientType.TO, toAddresses);

            Transport.send(body);

        } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    static class EmailAuth extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(FROM_ADDRESS, PASSWORD);

        }
    }
}