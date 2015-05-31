/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Vital
 */
public class SendMessage implements Runnable{

    private String conType, host, login, password, from, to, subject, text;
    
    public SendMessage(String conType, String host, String login, String password, String from, String to, String subject, String text) {
        this.conType = conType;
        this.host = host;
        this.login = login;
        this.password = password;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
    
    @Override
    public void run() {
        
        switch (conType) {
            case "Pure" : send();
                          return;
            case "SSL"  : sendWithSSL();
                          return;
            case "TLS"  : sendWithTLS();
                          return;
        }
    }

    private void send() {
        Properties prp =  System.getProperties();
        prp.setProperty("mail.smtp.host", host);
        
        Session ses = Session.getDefaultInstance(prp);
        
        try {
            MimeMessage message = new MimeMessage(ses);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message, login, password);
            System.out.println("Sent message succesfuly");
        } catch(MessagingException ex) {
            ex.printStackTrace();
        }
    }
    
    private void sendWithSSL() {
        Properties prp =  new Properties();
        prp.put("mail.smtp.host", host);
        prp.put("mail.smtp.port", "465");
        prp.put("mail.smtp.auth", "true");
        prp.put("mail.smtp.socketFactory.port", "465");
        prp.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prp.put("mail.debug", "true");
        
        Session ses = Session.getInstance(prp, 
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password);
                    }
        });
        
        try {
            MimeMessage message = new MimeMessage(ses);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            System.out.println("Sent message succesfuly");
        } catch(MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendWithTLS() {
        Properties prp =  System.getProperties();
        prp.put("mail.smtp.auth", "true");
        prp.put("mail.smtp.starttls.enable", "true");
        prp.put("mail.smtp.host", host);
        prp.put("mail.smtp.port", "587");
        
        Session ses = Session.getDefaultInstance(prp);
        
        try {
            MimeMessage message = new MimeMessage(ses);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);
            
            Transport.send(message, login, password);
            System.out.println("Sent message succesfuly...");
        } catch(MessagingException ex) {
            ex.printStackTrace();
        } 
    }
}
