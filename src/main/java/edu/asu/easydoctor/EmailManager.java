package edu.asu.easydoctor;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailManager {

    public static void sendEmail(String recipient, String subject, String body) throws MessagingException {
        
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(App.properties.getProperty("email_address"), App.properties.getProperty("email_app_password"));
            }
        });
        
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        message.setText(body);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        Transport.send(message);
    }

    public static void sendResetPasswordEmail(String recipient, String username, int token, long expiration) throws SQLException, MessagingException, UnknownHostException {
        Date expirationDate = new Date(expiration);
        String expirationDateString = new SimpleDateFormat("hh:mm a").format(expirationDate);

        String subject = "Password Reset Request";
        String body = String.format(
                                    "Your username is %s" +
                                    "\nYour password reset token is %d" +
                                    "\nEnter in the token before %s to reset your password.", username, token, expirationDateString);

        sendEmail(recipient, subject, body);
    }
}
