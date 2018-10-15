import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.InvalidParameterException;
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
    private static final String LABEL_SMTP_HOST = "mail.smtp.host";
    private static final String LABEL_SMTP_PORT = "mail.smtp.port";
    private static final String LABEL_SMTP_AUTH = "mail.smtp.auth";
    private static final String LABEL_SMTP_TLS = "mail.smtp.starttls.enable";
    private static final String LABEL_SOCKET_FACTORY = "mail.smtp.socketFactory.class";
    private static final String LABEL_SSL_SOCKET = "javax.net.ssl.SSLSocketFactory";


    /** Set all the configurations at initialization
     * @param smtpHost - your host
     * @param port - your smtp port
     * @param authentication - true for secure
     * @param authType - TLS or SSL
     * */
    EasyEmail(String smtpHost, String port, boolean authentication, String authType){
        SMTP_HOST = smtpHost;
        PORT = port;
        AUTHENTICATION = Boolean.toString(authentication);
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
     * This method is called to configure sender's credentials,
     * establish TLS authentication and create a mail sesion
     * this should be called only once
     * @param fromEmail - sender's email
     * @param password - senders's password for authentication
     **/
    public void setIdentity(String fromEmail, String password){
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
     * This method can be invoked multiple times.
     * @param toEmail - receiver's email
     * @param subject - email's subject
     * @param body - email's body
     */
    public void send(String toEmail, String subject, String body){
        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}