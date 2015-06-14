/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import morda.Morda;
import util.Utilities;
import util.WriteMessages;
/**
 *
 * @author Vital
 */
public class GetMessages implements Runnable {

    private final Mail mail;
    private final List<Message> mesList;
    private final JLabel lblIn;
    private final JTable table;
    private final Utilities util = new Utilities();
    private final Properties prop;
    private final String conType, server, port; 
    private Date lastDate = new Date(0l);
    
    public GetMessages(String conType, Mail mail, String server, String port, List<Message> mesList, JLabel lblIn, JTable table, Properties prop) {
        this.conType = conType;
        this.mesList = mesList;
        this.lblIn = lblIn;
        this.table = table;
        this.mail = mail;
        this.prop = prop;
        this.server = server;
        this.port = port;
    }
    
    @Override
    public void run() {

        String userHome = System.getProperty("user.home");
        File f = new File(userHome+"\\Desktop\\archive");
            
        switch (conType) {
                    case "Pure" :   mail.connect(server, port);
                    break;
                    case "SSL" :    mail.connectWithSSL(server, port);
                    break;
                    case "TLS" :    break;
                }
        
        mail.openFolder("INBOX");
                
        while(!Thread.interrupted()) {  
            try {

                Message[] mess = mail.getMessages();
                int archive = f.listFiles().length;
                System.out.println("There are " + archive + " messages in archive.");

                for(int i = 0; i < mess.length; i++) {

                    Date d = mess[i].getSentDate();

                    if (d.after(lastDate)) {

                        //<editor-fold defaultstate="collapsed" desc="comment">
                        Address[] a = mess[i].getFrom();
                        System.out.println(i + " From : " + a[0].toString());
                        System.out.println("---------------------------------------");

                        mesList.add(mess[i]);
                        addDataToTable(mess[i]);

                        (new Thread(new WriteMessages(mess[i], f.getAbsolutePath()))).start();
                        
//                        File file = new File(userHome+ "\\Desktop\\archive\\" + mess[i].getSubject());
//                        file.createNewFile();
//
//                        try (OutputStream os = new FileOutputStream(file);){
//                            mess[i].writeTo(os);
//                            System.out.println("File " + file.getName() + " was wrote succesfuly");
//                        } catch (IOException | MessagingException ex) {
//                        }

                        if (i == mess.length-1) {
                            lastDate = mess[i].getSentDate();
                            System.out.println("Last mess date : " + lastDate.toString());
                        }
    //</editor-fold>
                    }  

                } 
                //mail.closeFolderStore();

                sleep(20000);
//        
            } catch (InterruptedException | MessagingException ex) {
                Logger.getLogger(GetMessages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*
    * This method checks for content-type 
    * based on which, it processes and
    * fetches the content of the message
    */
          
    private void addDataToTable(Message nm) {

        try {
            String subject = nm.getSubject();

            Address[] a = nm.getFrom();
            String from = a[0].toString();

            Date d = nm.getSentDate();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] obj = {null, subject, from, df.format(d)};
            model.addRow(obj);

            lblIn.setText(String.valueOf(model.getRowCount()));
        } catch (MessagingException ex) {
            Logger.getLogger(Morda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
