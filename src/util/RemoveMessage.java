/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import morda.Morda;

/**
 *
 * @author Vital
 */
public class RemoveMessage implements Runnable{ // This cass must be rewrited

    private int[] rows;
    private List<Message> list;
    private JLabel label;
    private DefaultTableModel tm;
    private File f = null;
    private Properties properties;
    private Utilities util = new Utilities();
    
    public RemoveMessage(int[] rows, List<Message> list, JLabel label, DefaultTableModel tm, Properties properties) {
        this.rows = rows;
        this.list = list;
        this.label = label;
        this.tm = tm;
        this.properties = properties;
    }
    
    @Override
    public void run() {
        int count = rows.length;
        util.print("Count: " + count);
        int rowIndex;
        
        for (int j = count-1; j>=0; j--) {
            
            rowIndex = rows[j];
            util.print("Row index: " + rowIndex);
            tm.removeRow(rowIndex);    //problem!!!
            String fileName = util.produceFileName(list.get(rowIndex));
            f = new File(properties.getProperty("PathToMessages") + "\\" + fileName);
            
            if(f.exists()) {
                System.out.println("File exist");
                System.gc();
                if(f.delete()) {
                    util.print(properties.getProperty("PathToMessages") + "\\" + fileName + " was deleted");
                } else {
                    util.print("File not deleted");
                };
            } else {
                System.out.println("File not exist");
            }
            
            list.remove(rowIndex);
            label.setText(String.valueOf(tm.getRowCount()));
            System.out.println("Row " +rows[j]+ " removed");
        }
    }
}
