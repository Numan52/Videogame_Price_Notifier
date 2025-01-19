package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.simplejavamail.MailException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.util.Properties;

public class EmailSender {
    private String host;
    private int port;
    private String emailAddress;
    private String password;
    private String from;
    private String to;
    private String subject;
    private String content;


    public EmailSender(String host, int port, String emailAddress, String password, String from, String to, String subject, String content) {
        this.host = host; // smtp.gmail.com
        this.port = port; // 587 (TLS) / 465 (SSL)
        this.emailAddress = emailAddress;
        this.password = password;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }


    public void send() {
        try {
            Email email = EmailBuilder.startingBlank()
                    .from(emailAddress)
                    .to(to)
                    .withSubject(subject)
                    .withPlainText(content)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(host, port, emailAddress, password)
                    .buildMailer();

            mailer.sendMail(email);

        } catch (MailException e) {
            System.err.println(e.getMessage());
        }




    }





    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
