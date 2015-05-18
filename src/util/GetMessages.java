/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import mail.Mail;
import morda.Morda;

/**
 *
 * @author Vital
 */
public class GetMessages implements Runnable {

    private final Mail mail;
    private final List<Message> mesList;
    private final JLabel lblIn;
    private final JTable table;
    private Utilities util = new Utilities();
    
    public GetMessages(Mail mail, List<Message> mesList, JLabel lblIn, JTable table) {
        this.mesList = mesList;
        this.lblIn = lblIn;
        this.table = table;
        this.mail = mail;
    }
    
    @Override
    public void run() {

        Message[] m = mail.getMessages();                                
        System.out.println("Count of messages " + m.length);

        int count = m.length;
        int j = 0;
        while (j <= count && !Thread.interrupted()) {

            String name = util.produceFileName(m[j]);
            if(mesList.add(m[j])) {
                System.out.println("Message "+ j +" added succesfuly.");
                addDataToTable(m[j]);
            } else {
                System.out.println("Something wrong!!!");
            };
            j++;
        }                              
    }

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
