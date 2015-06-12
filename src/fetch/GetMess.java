/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fetch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author Vital
 */
public class GetMess implements Runnable{

    private String host, port, user, passwd;
    int archive;
    private Date lastDate = new Date(0l);
    
    public GetMess(String host, String port, String user, String passwd) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
    }
    
    @Override
    public void run() {
        try {
            Properties prop = new Properties();
            prop.put("mail.pop3.host", host);
            prop.put("mail.pop3.port", port);
            prop.put("mail.pop3.srarttls.enable", "true");
            
            Session session = Session.getDefaultInstance(prop);
            
            Store store = session.getStore("pop3s");
            
            File f = new File("C:\\Users\\Vital\\Desktop\\archive");
            
            while(!Thread.interrupted()) {  
                System.out.println("Try to connect to " + user+"@"+host+ " with passwd " + passwd);
                store.connect(host, user, passwd);

                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                
                Message[] mess = folder.getMessages();
                archive = f.listFiles().length;
                System.out.println("There are " + archive + " messages in archive.");
                
                for(int i = 0; i < mess.length; i++) {
                    
                    Date d = mess[i].getSentDate();
                    
                    if (d.after(lastDate)) {

                        //<editor-fold defaultstate="collapsed" desc="comment">
                        Address[] a = mess[i].getFrom();
                        System.out.println(i + " From : " + a[0].toString());
                        System.out.println("---------------------------------------");

                        if (i == mess.length-1) {
                            lastDate = mess[i].getSentDate();
                            System.out.println("Last mess date : " + lastDate.toString());
                        }

                        File file = new File("C:\\Users\\Vital\\Desktop\\archive\\" + mess[i].getSubject());
                        file.createNewFile();

                        try (OutputStream os = new FileOutputStream(file);){
                            mess[i].writeTo(os);
                            System.out.println("File " + file.getName() + " was wrote succesfuly");
                        } catch (IOException | MessagingException ex) {
                            ex.printStackTrace();
                        }
//</editor-fold>
                    }  
                } 
                folder.close(false);
                store.close();
                
                sleep(10000);
            //} else System.out.println("We don't have new messages. Try to check after 1 minute.");
            }
            
            
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(GetMess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(GetMess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GetMess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
