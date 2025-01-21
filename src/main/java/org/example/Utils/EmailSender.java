package org.example.Utils;

import org.simplejavamail.MailException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailSender {
    private String host;
    private int port;
    private String emailAddress;
    private String password;
    private String to;
    private String subject;
    private String textContent;
    private String htmlContent;


    public EmailSender(String host, int port, String emailAddress, String password, String to, String subject, String textContent, String htmlContent) {
        this.host = host; // smtp.gmail.com
        this.port = port; // 587 (TLS) / 465 (SSL)
        this.emailAddress = emailAddress;
        this.password = password;
        this.to = to;
        this.subject = subject;
        this.textContent = textContent;
        this.htmlContent = htmlContent;
    }


    public void send() {
        try {
            Email email = EmailBuilder.startingBlank()
                    .from(emailAddress)
                    .to(to)
                    .withSubject(subject)
                    .withHTMLText(htmlContent)
                    .withPlainText(textContent)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(host, port, emailAddress, password)
                    .buildMailer();

            mailer.sendMail(email);

        } catch (MailException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("EMAIL SENT");
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

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
