/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.mail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 *
 * @author wiesbob
 */
public class Mailer {

    private final Properties prop;

    public Mailer() {
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.web.de");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.web.de");
    }

    public Mailer(String smtpHost, int port, String sslTrust) {
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.port", Integer.toString(port));
        prop.put("mail.smtp.ssl.trust", sslTrust);
    }

    public boolean writeEmail(String subject, String messageText) {
        Session session = Session.getInstance(getProp(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("christopher.mightybot", "christopher1337");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("christopher.mightybot@web.de"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("wiesbob@hotmail.de"));
            // String subject = ;
            message.setSubject(subject);

            // String msg = ;
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception ae) {
            System.out.println("Error occured while sending message.");
            return false;
        }
        return true;

    }

    /**
     * @return the prop
     */
    private Properties getProp() {
        return prop;
    }

}
