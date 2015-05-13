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
import static morda.Morda.print;

/**
 *
 * @author Vital
 */
public class RemoveMessage implements Runnable{

    private int[] rows;
    private List<Message> list;
    private JLabel label;
    private DefaultTableModel tm;
    private File f = null;
    private Properties properties;
    
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
        for (int j = count-1; j>=0; j--) {
            tm.removeRow(j);
            String fileName = Morda.produceFileName(list.get(j));
            f = new File(properties.getProperty("PathToMessages") + "\\" + fileName);
            
            if(f.exists()) {
                System.out.println("File exist");
                if(f.delete()) {
                    print(properties.getProperty("PathToMessages") + "\\" + fileName + " was deleted");
                } else {
                    print("File not deleted");
                };
            } else {
                System.out.println("File not exist");
            }
            
            list.remove(j);
            label.setText(String.valueOf(tm.getRowCount()));
            System.out.println("Row " +rows[j]+ " removed");
        }
    }
}
