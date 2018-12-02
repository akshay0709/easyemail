import com.easyemail.EasyEmail;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class EasyEmailTest {

    private static final String USER_PASSWORD = "userpassword@123";
    private static final String USER_NAME = "sender@localhost.com";
    private static final String EMAIL_USER_ADDRESS = "sender@localhost.com";
    private static final String EMAIL_TO = "receiver@localhost.com";
    private static final String EMAIL_SUBJECT = "Test E-Mail";
    private static final String EMAIL_TEXT = "This is a test e-mail.";
    private static final String LOCALHOST = "127.0.0.1";
    private GreenMail testServer;
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Test
    public void sendEmailWithSSLText() throws MessagingException, IOException {

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()), "SSL");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toText();
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertTrue(messages[0].getContentType().contains("text/plain"));
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertTrue(String.valueOf(m.getContent()).contains(EMAIL_TEXT));
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());

        testServer.stop();
    }

    @Test
    public void sendEmailWithTLSText() throws MessagingException, IOException {

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()),"TLS");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toText();
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertTrue(messages[0].getContentType().contains("text/plain"));
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertTrue(String.valueOf(m.getContent()).contains(EMAIL_TEXT));
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());
        testServer.stop();
    }

    @Test
    public void sendEmailWithSSLHtml() throws MessagingException, IOException {

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()), "SSL");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toHtml();
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertTrue(messages[0].getContentType().contains("text/html"));
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertTrue(String.valueOf(m.getContent()).contains(EMAIL_TEXT));
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());

        testServer.stop();
    }

    @Test
    public void sendEmailWithTLSHtml() throws MessagingException, IOException {

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()),"TLS");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toHtml();
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertTrue(messages[0].getContentType().contains("text/html"));
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertTrue(String.valueOf(m.getContent()).contains(EMAIL_TEXT));
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());
        testServer.stop();
    }

    @Test
    public void sendEmailWithAttachmentSSL() throws MessagingException, IOException {

        File file = testFolder.newFile("abc.txt");

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()),"SSL");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toText();
        easyemail.addAttachment(file);
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());
        final Multipart part = (Multipart) m.getContent();
        Assert.assertTrue(String.valueOf(part.getBodyPart(0).getContent()).contains(EMAIL_TEXT));
        //Expected 2 because email body + attachment = 2 count
        Assert.assertEquals(2,part.getCount());
        testServer.stop();
    }

    @Test
    public void sendEmailWithAttachmentTLS() throws MessagingException, IOException {

        File file = testFolder.newFile("abc.txt");

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()), "TLS");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toText();
        easyemail.addAttachment(file);
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());
        final Multipart part = (Multipart) m.getContent();
        Assert.assertTrue(String.valueOf(part.getBodyPart(0).getContent()).contains(EMAIL_TEXT));
        //Expected 2 because email body + attachment = 2 count
        Assert.assertEquals(2,part.getCount());
        testServer.stop();
    }

    @Test
    public void sendEmailWithMultipleAttachmentSSL() throws MessagingException, IOException {

        List<File> files = new ArrayList<>();
        files.add(testFolder.newFile("abc.txt"));
        files.add(testFolder.newFile("xyz.txt"));
        files.add(testFolder.newFile("lmn.pdf"));

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()), "SSL");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toText();
        easyemail.addAttachment(files);
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());
        final Multipart part = (Multipart) m.getContent();
        Assert.assertTrue(String.valueOf(part.getBodyPart(0).getContent()).contains(EMAIL_TEXT));
        //Expected 4 because email body + 3 attachment = 4 count
        Assert.assertEquals(4,part.getCount());
        testServer.stop();
    }

    @Test
    public void sendEmailWithMultipleAttachmentTLS() throws MessagingException, IOException {

        List<File> files = new ArrayList<>();
        files.add(testFolder.newFile("abc.txt"));
        files.add(testFolder.newFile("xyz.txt"));
        files.add(testFolder.newFile("lmn.pdf"));

        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        testServer = new GreenMail(ServerSetupTest.SMTP);
        testServer.start();
        testServer.setUser(EMAIL_USER_ADDRESS, USER_NAME ,USER_PASSWORD);
        EasyEmail easyemail = new EasyEmail(LOCALHOST, Integer.toString(ServerSetupTest.SMTP.getPort()), "TLS");
        easyemail.setIdentity(EMAIL_USER_ADDRESS, USER_PASSWORD);
        easyemail.setBasicInfo(EMAIL_SUBJECT, EMAIL_TEXT).toText();
        easyemail.addAttachment(files);
        easyemail.send(EMAIL_TO);

        MimeMessage[] messages = testServer.getReceivedMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        Assert.assertEquals(EMAIL_SUBJECT, m.getSubject());
        Assert.assertEquals(EMAIL_USER_ADDRESS, m.getFrom()[0].toString());
        final Multipart part = (Multipart) m.getContent();
        Assert.assertTrue(String.valueOf(part.getBodyPart(0).getContent()).contains(EMAIL_TEXT));
        //Expected 4 because email body + 3 attachment = 4 count
        Assert.assertEquals(4,part.getCount());
        testServer.stop();
    }
}
