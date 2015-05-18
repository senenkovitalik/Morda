/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 *
 * @author Darth Vader
 */
public class Utilities {
    
    private Properties defProp;
    
    public Properties rewrite(String key, String value, Properties sourceProp) {
        defProp = new Properties();
        copyProperties(sourceProp, defProp);
        defProp.put(key, value);
        sourceProp.clear();
        copyProperties(defProp, sourceProp);
        return sourceProp;
    }
    
    public void copyProperties(Properties src_prop, Properties dest_prop) {
        for (Enumeration propertyNames = src_prop.propertyNames(); propertyNames.hasMoreElements();) {
            Object key = propertyNames.nextElement();
            dest_prop.put(key, src_prop.get(key));
        }
    }
    
    public void print(String str) {
        System.out.println(str);
    }
    
    public String produceFileName(Message message) {
        //<editor-fold defaultstate="collapsed" desc="date">
                Date d = null;
                try {
                    d = message.getSentDate();
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
        
        return name;
    }
}
