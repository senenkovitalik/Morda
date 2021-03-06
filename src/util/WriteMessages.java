/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
/**
 *
 * @author man
 */
public class WriteMessages implements Runnable{

    private List<Message> list;
    private Properties properties;
    private final Utilities util = new Utilities();
    private Message m;
    private String archive;
    
    public WriteMessages(List<Message> list, Properties properties) {
        this.list = list;
        this.properties = properties;
    }
    
    public WriteMessages(Message m, String archive) {
        this.m = m;
        this.archive = archive;
    }
    
    @Override
    public void run() {
        
//        Iterator<Message> iter = list.iterator();
//        while (iter.hasNext()) {
//
//            Message m = iter.next();
//
//            String name = util.produceFileName(m);
//
//            File file = new File(properties.getProperty("PathToMessages") + "//" + name);
//            try {
//                file.createNewFile();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//
//            try (OutputStream os = new FileOutputStream(file);){
//                m.writeTo(os);
//                System.out.println("File " + file.getName() + " was wrote succesfuly");
//            } catch (IOException | MessagingException ex) {
//                ex.printStackTrace();
//            }
//        }
        
        String name = util.produceFileName(m);
        File file = new File(archive + "//" + name);
        try {
             file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try (OutputStream os = new FileOutputStream(file);){
            m.writeTo(os);
            System.out.println("File " + file.getName() + " was wrote succesfuly");
        } catch (IOException | MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
