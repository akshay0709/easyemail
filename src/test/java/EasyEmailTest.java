import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Security;

public class EasyEmailTest {

    private static final String USER_PASSWORD = "userpassword@123";
    private static final String USER_NAME = "sender@localhost.com";
    private static final String EMAIL_USER_ADDRESS = "sender@localhost.com";
    private static final String EMAIL_TO = "receiver@localhost.com";
    private static final String EMAIL_SUBJECT = "Test E-Mail";
    private static final String EMAIL_TEXT = "This is a test e-mail.";
    private static final String LOCALHOST = "127.0.0.1";
    private GreenMail testServer;


    @Test
    public void sendEmailWithSSL() throws MessagingException, IOException {

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();

        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()), true, "SSL");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.send(EMAIL_TO, EMAIL_SUBJECT, EMAIL_TEXT);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertTrue(String.valueOf(m.getContent()).contains(EMAIL_TEXT));
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());

        testServer.stop();
    }

    @Test
    public void sendEmailWithTLS() throws MessagingException, IOException {

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();

        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()), true, "TLS");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.send(EMAIL_TO, EMAIL_SUBJECT, EMAIL_TEXT);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertTrue(String.valueOf(m.getContent()).contains(EMAIL_TEXT));
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());

        testServer.stop();
    }
}
