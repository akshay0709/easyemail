package com.easyemail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Properties;

public class EasyEmail {

    private static String FROM_EMAIL;
    private static String PASSWORD;
    private static String SMTP_HOST;
    private static String PORT;
    private static String AUTHENTICATION;
    private static String AUTH_TYPE;
    private static Properties properties = new Properties();
    private static Authenticator auth;
    private static Session session;
    private static Message message;
    private static String body;
    private static final String LABEL_SMTP_HOST = "mail.smtp.host";
    private static final String LABEL_SMTP_PORT = "mail.smtp.port";
    private static final String LABEL_SMTP_AUTH = "mail.smtp.auth";
    private static final String LABEL_SMTP_TLS = "mail.smtp.starttls.enable";
    private static final String LABEL_SOCKET_FACTORY = "mail.smtp.socketFactory.class";
    private static final String LABEL_SSL_SOCKET = "javax.net.ssl.SSLSocketFactory";
    private Flag flag;


    /** Set all the configurations at initialization
     * @param smtpHost SMTP host to be used
     * @param port Port to be used
     * @param authType TLS or SSL
     * */
    public EasyEmail(String smtpHost, String port, String authType) {
        SMTP_HOST = smtpHost;
        PORT = port;
        AUTHENTICATION = Boolean.toString(true);
        properties.put(LABEL_SMTP_HOST, SMTP_HOST);
        properties.put(LABEL_SMTP_PORT, PORT);
        properties.put(LABEL_SMTP_AUTH, AUTHENTICATION);
        AUTH_TYPE = authType;

        switch (AUTH_TYPE) {
            case "TLS":
                properties.put(LABEL_SMTP_TLS, true);
                break;
            case "SSL":
                properties.put(LABEL_SOCKET_FACTORY, LABEL_SSL_SOCKET);
                break;
            default:
                InvalidParameterException e = new InvalidParameterException();
                e.printStackTrace();
                break;
        }
    }


    /**
     * Configure sender's credentials,
     * establish TLS / SSL authentication and create a mail sesion
     * @param fromEmail sender's email address
     * @param password  senders's password for authenticationz
     **/
    public void setIdentity(String fromEmail, String password) {
        FROM_EMAIL = fromEmail;
        PASSWORD = password;
        auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        };

        switch (AUTH_TYPE) {
            case "TLS":
                session = Session.getInstance(properties, auth);
                break;
            case "SSL":
                session = Session.getInstance(properties, auth);
                break;
            default:
                InvalidParameterException e = new InvalidParameterException();
                e.printStackTrace();
                break;
        }

        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(FROM_EMAIL));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Set basic information for email.
     * @param subject email's subject
     * @param strBody email's body
     */
    public EasyEmail setBasicInfo(String subject, String strBody) {
        body = strBody;
        try {
            message.setSubject(subject);
            //message.setText(body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Sets email body to be plain text
     * Chained with setBasicInfo
     */
    public void toText() {
        try {
            message.setText(body);
            flag = Flag.TEXT;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets email body to be plain text
     * Chained with setBasicInfo
     */
    public void toHtml() {
        try {
            message.setContent(body, "text/html");
            flag = Flag.HTML;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attach file to the email
     * @param file Actual file to be sent
     */
    public void addAttachment(File file) {
        try {
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            //Message body part
            BodyPart bodyPart = new MimeBodyPart();
            if (flag == Flag.HTML) {
                bodyPart.setContent(body, "text/html");
            } else {
                bodyPart.setText(body);
            }

            //Multipart for attachment and body
            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(bodyPart);

            //Body part for attachment
            bodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            bodyPart.setDataHandler(new DataHandler(source));
            bodyPart.setFileName(file.getName());
            multiPart.addBodyPart(bodyPart);
            message.setContent(multiPart);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    /**
     * Attach list of files to the email
     * @param files List of files to be sent
     */
    public void addAttachment(List<File> files) {
        try {
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");
            //Message body part
            BodyPart bodyPart = new MimeBodyPart();
            if (flag == Flag.HTML) {
                bodyPart.setContent(body, "text/html");
            } else {
                bodyPart.setText(body);
            }
            //Multipart for attachment and body
            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(bodyPart);
            for(File file : files) {
                //Body part for attachment
                bodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                bodyPart.setDataHandler(new DataHandler(source));
                bodyPart.setFileName(file.getName());
                multiPart.addBodyPart(bodyPart);
            }
            message.setContent(multiPart);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Send an email
     * Takes recivers email address as a parameter
     * Can be invoked multiple times to send emails to multiple addresses
     * @param toEmail email address of the reciever
     */
    public void send(String toEmail) {
        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //Flags to check type of email
    enum Flag {
        TEXT, HTML
    }
}