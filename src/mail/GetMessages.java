/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mail;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import javax.mail.Multipart;
import javax.mail.Part;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import morda.Morda;
import util.Utilities;
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
    
    public GetMessages(Mail mail, List<Message> mesList, JLabel lblIn, JTable table, Properties prop) {
        this.mesList = mesList;
        this.lblIn = lblIn;
        this.table = table;
        this.mail = mail;
        this.prop = prop;
    }
    
    @Override
    public void run() {

        Message[] m = mail.getMessages();   
        System.out.println("Count of messages " + m.length);

        int count = m.length;
        int j = 0;
        while (j < count && !Thread.interrupted()) {
            
            if(j == count) {
                break;
            }
            
            try {
                //writePart(m[j]);
            } catch (Exception ex) {
                Logger.getLogger(GetMessages.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(mesList.add(m[j])) {
                System.out.println("Message "+ j +" added succesfuly.");
                addDataToTable(m[j]);
            } else {
                System.out.println("Something wrong!!!");
            };
            j++;
        } 
    }

    /*
    * This method checks for content-type 
    * based on which, it processes and
    * fetches the content of the message
    */
    public static void writePart(Part p) throws Exception {
        if (p instanceof Message)
             //Call methos writeEnvelope
            writeEnvelope((Message) p);

        System.out.println("----------------------------");
        System.out.println("CONTENT-TYPE: " + p.getContentType());

        //check if the content is plain text
        if (p.isMimeType("text/plain")) {
            System.out.println("This is plain text");
            System.out.println("---------------------------");
            System.out.println((String) p.getContent());
        } 
        //check if the content has attachment
        else if (p.isMimeType("multipart/*")) {
            System.out.println("This is a Multipart");
            System.out.println("---------------------------");
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
            writePart(mp.getBodyPart(i));
        } 
        //check if the content is a nested message
        else if (p.isMimeType("message/rfc822")) {
            System.out.println("This is a Nested Message");
            System.out.println("---------------------------");
            writePart((Part) p.getContent());
        } 
        //check if the content is an inline image
        else if (p.isMimeType("image/jpeg")) {
            System.out.println("--------> image/jpeg");
            Object o = p.getContent();

            InputStream x = (InputStream) o;
            // Construct the required byte array
            System.out.println("x.length = " + x.available());
            int i = 0;
            byte[] bArray = null;
            while ((i = ((InputStream) x).available()) > 0) {
                int result = (int) (((InputStream) x).read(bArray));
                if (result == -1)
                
                    bArray = new byte[x.available()];

                break;
            }
            FileOutputStream f2 = new FileOutputStream("C:\\Users\\Vital\\Desktop\\archive\\image.jpg");
            f2.write(bArray);
        } 
        else if (p.getContentType().contains("image/")) {
            System.out.println("content type " + p.getContentType());
            File f = new File("C:\\Users\\Vital\\Desktop\\archive\\" + p.getFileName());
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p.getContent();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = test.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } 
        else {
            Object o = p.getContent();
            if (o instanceof String) {
                System.out.println("This is a string");
                System.out.println("---------------------------");
                System.out.println((String) o);
            } 
            else if (o instanceof InputStream) {
                System.out.println("This is just an input stream");
                System.out.println("---------------------------");
                InputStream is = (InputStream) o;
                is = (InputStream) o;
                int c;
                while ((c = is.read()) != -1)
                    System.out.write(c);
            } 
            else {
                System.out.println("This is an unknown type");
                System.out.println("---------------------------");
                System.out.println(o.toString());
            }
        }
    }
      
      /*
   * This method would print FROM,TO and SUBJECT of the message
   */
    public static void writeEnvelope(Message m) throws Exception {
        System.out.println("This is the message envelope");
        System.out.println("---------------------------");
        Address[] a;

        // FROM
        if ((a = m.getFrom()) != null) {
             for (int j = 0; j < a.length; j++)
             System.out.println("FROM: " + a[j].toString());
        }

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
             for (int j = 0; j < a.length; j++)
             System.out.println("TO: " + a[j].toString());
        }

         // SUBJECT
        if (m.getSubject() != null)
            System.out.println("SUBJECT: " + m.getSubject());

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
