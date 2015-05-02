/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author Darth Vader
 */
public class Utilities {
    
    public Properties defProp;
    
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
}
