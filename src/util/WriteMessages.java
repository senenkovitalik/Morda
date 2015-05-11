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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import morda.Morda;

/**
 *
 * @author man
 */
public class WriteMessages implements Runnable{

    private List<Message> list;
    private Properties properties;
    
    public WriteMessages(List<Message> list, Properties properties) {
        this.list = list;
        this.properties = properties;
    }
    
    @Override
    public void run() {
        //List<Message> list, Properties runProp
        
        for (Message m : list) {
            
            //<editor-fold defaultstate="collapsed" desc="date">
            Date d = null;
            try {
                d = m.getSentDate();
            } catch (MessagingException ex) {
                //Logger.getLogger(Morda.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Some problems with date");
            }
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);
            
            String smonth, sday, shour, smin, ssec;
            
            if (month < 10) {
                smonth = "0"+String.valueOf(month);
            } else smonth = String.valueOf(month);
            
            if (day < 10) {
                sday = "0"+String.valueOf(day);
            } else sday = String.valueOf(day);
            
            if(hour < 10) {
                shour = "0"+String.valueOf(hour);
            } else shour = String.valueOf(hour);
            
            if(min < 10) {
                smin = "0"+String.valueOf(min);
            } else smin = String.valueOf(min);
            
            if(sec < 10) {
                ssec = "0"+String.valueOf(sec);
            } else ssec = String.valueOf(sec);
//</editor-fold>
                    
            String name = year +"_"+ smonth +"_"+ sday +"_"+ shour +"_"+ smin +"_"+ ssec + ".mes";
            File file = new File(properties.getProperty("PathToMessages") + "//" + name);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Morda.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try (OutputStream os = new FileOutputStream(file);){
                m.writeTo(os);
                System.out.println("File " + file.getName() + " was wrote succesfuly");
            } catch (IOException | MessagingException ex) {
                Logger.getLogger(Morda.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        
    }
}
