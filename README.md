# EasyEmail

[![Build Status](https://travis-ci.com/akshay0709/easyemail.svg?token=odjGeysjx21sHAzR3xQp&branch=master)](https://travis-ci.com/akshay0709/easyemail)

## Summary

EasyEmail is an open source and easy to use library to send event driven emails via Java code.
EasyEmail is a wrapper around JavaMail which allows developers to send event driven emails with less than 10 lines of code.
As a developer you do not need to understand or know the internal configurations to send an email over SMTP in Java, EasyEmail will do it for you behind the scenes. 
Simply tell EasyEmail what to use and what you want and you are all set.

#### Currently, EasyEmail supports following features:
 - Text/Html email over SSL / TLS
 - Text/Html email with attachments over SSL / TLS
 
**Note:** EasyEmail doesn't support sending emails without authentication.

## Usage

To send simple emails using EasyEmail, refer the following code snippet:

```java
// Import package
import com.easyemail.*;

// Your SMTP server configurations (authtype = SSL or TLS)
EasyEmail em = new EasyEmail("smtp.yourhost.com", "yourport", true, "authtype");

// Senders email and password
em.setIdentity("sender@xyz.com", "senderspassword");

//To send email body as text
em.setBasicInfo("subject", "emailbody").isText();

// recipient
em.send("reciever@xyz.com");
```

***To send an email as HTML***

```java
//To send email body as html
em.setBasicInfo("subject", "emailbody").isHtml();
```

***To send email with attachments***
```java
// To send single attachment of type File
em.addAttachment(file)

// List of attachments of type File to send multiple attachments
// List<Files> files = new ArrayList<>();
em.addAttachment(files)
```
 
***To send email to multiple recipients***
```java
// Multiple recipients
//List<String> emails = new ArrayList<>()
for (String email : emails) {
    em.send(email);
}
```
