/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import com.sun.mail.pop3.POP3SSLStore;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/**
 *
 * @author man
 */
public class Mail {
    
    private String login, password;
    private Session session = null;
    private Store store = null;
    private Folder folder;
    private Message[] m;
    
    public void setUserPass(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public void connect(String server, String port){
        
        Properties pop3prop = System.getProperties();
        pop3prop.setProperty("mail.pop3.port",  port);
        
        session = Session.getDefaultInstance(pop3prop);
        try {       
            store = session.getStore("pop3");
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            store.connect(server, login, password);
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    public void connectWithSSL(String server, String port) {
        
        System.out.println("Try to connect to "+server+", port "+port+"...");
        
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        
        Properties pop3Props = new Properties();
        
        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port",  port);
        pop3Props.setProperty("mail.pop3.socketFactory.port", port);
        
        URLName url = new URLName("pop3", server, Integer.parseInt(port), "", login, password);
        
        session = Session.getInstance(pop3Props, null);
        store = new POP3SSLStore(session, url);
        System.out.println(login+" "+password);
        try {
            store.connect();
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void openFolder(String folderName) {
        
        try {
            folder = store.getDefaultFolder();
            folder = folder.getFolder(folderName);
            
            try {
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException ex) {
                folder.open(Folder.READ_ONLY);
            }
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Message[] getMessages() {
        try {
            m = folder.getMessages();
            System.out.println("Succesfuly get all messages");
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }
    
    public int messageCount() {
        int k = 0;
        try {
            k = folder.getMessageCount();
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return k;
    }
    
    public void closeFolderStore() {
        try {
            folder.close(false);
            store.close();
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

