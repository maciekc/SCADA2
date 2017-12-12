package com.scada.server;

import com.scada.model.dataBase.Andon.Andon;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {

    private final String from;
    private final String destination;
    private final String host;
    private final Properties props;
    private final Session session;

    public Mail(String from, String destination) {
        this.from = from;
        this.destination = destination;
        this.host = "localhost";
        this.props = System.getProperties();
        this.props.setProperty("mail.smtp.host", host);
        this.session = Session.getDefaultInstance(props);
    }

    public Mail() {
        this.from = "scada2agh@gmail.com";
        this.destination = "macceb01@gmail.com";
        this.host = "smtp.gmail.com";

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smpt.user", "scada2agh");
//        props.put("mail.password", "qwert2017");
//        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.port", "587");

//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        this.session = Session.getInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "qwert2017");
            }
        });
    }

    public String getFrom() {
        return from;
    }

    public String getDestination() {
        return destination;
    }

    public String getHost() {
        return host;
    }

    public Properties getPropertise() {
        return props;
    }

    public Session getSession() {
        return session;
    }

    public void sendMail(Andon andon) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
            message.setSubject("Przekroczenie " + andon.getLimitTag() + " , zbiornik " + andon.getStateSpaceTag());

            String content = "<div>Obiekt " + andon.getStateSpaceTag() + "</div>" +
                    "<div>Limit: " + andon.getLimitTag() + "</div>" +
                    "<div>Data: " + andon.getDate() + "</div>" +
                    "<div>Wartość: " + andon.getValue() + "</div>";

            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Send message successfully.");

        } catch (MessagingException mes) {
            mes.printStackTrace();
        }
    }
}
