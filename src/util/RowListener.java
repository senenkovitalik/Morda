/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import static mail.GetMessages.writeEnvelope;
import morda.Morda;

/**
 *
 * @author man
 * Відображення вмісту повідомлення при позиціонуванні курсору на ньому
 */
public class RowListener implements ListSelectionListener {
        
    private final JTable table;
    private List<Message> messageList = new CopyOnWriteArrayList<>();
    private JLabel labelFrom, labelSubject;
    private JEditorPane pane;
    
    public RowListener(JTable table, List<Message> messageList, JLabel labelFrom, JLabel labelSubject, JEditorPane pane) {
        this.table = table;
        this.messageList = messageList;
        this.labelFrom = labelFrom;
        this.labelSubject = labelSubject;
        this.pane = pane;
    }
    
    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
            return;
        }

        showMessage();
    }

    public void showMessage() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                int row = table.getSelectedRow();

                System.out.println("Working");

                String contentType = null, from = null, subject = null;

                Message m = messageList.get(row);
                try {
                    contentType = m.getContentType();
                    Address[] a = m.getFrom(); 
                    from = a[0].toString();
                    subject = m.getSubject();
                } catch (MessagingException ex) {
                    Logger.getLogger(Morda.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    writePart(m);
                } catch (Exception ex) {
                    Logger.getLogger(Morda.class.getName()).log(Level.SEVERE, null, ex);
                }

                if(from.length() > 40) {
                    String cutFrom = from.substring(0, 40) + "...";
                    labelFrom.setText(cutFrom);
                    labelFrom.setToolTipText(from);
                } else labelFrom.setText(from);

                if(subject.length() > 40) {
                    String cutSubject = subject.substring(0, 40) + "...";
                    labelSubject.setText(cutSubject);
                    labelSubject.setToolTipText(subject);
                } else labelSubject.setText(subject);

            }
        });        
    }

    public void writePart(Part p) throws Exception {
    if (p instanceof Message)
        // Call methos writeEnvelope
        writeEnvelope((Message) p);

    System.out.println("----------------------------");
    System.out.println("CONTENT-TYPE: " + p.getContentType());

    //check if the content is plain text
    if (p.isMimeType("text/plain")) {
        System.out.println("This is plain text");
        System.out.println("---------------------------");
        System.out.println((String) p.getContent());
        pane.setContentType("text/plain");
        pane.setText((String) p.getContent());
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
        FileOutputStream f2 = new FileOutputStream("C:\\Users\\man\\Desktop\\archive\\image.jpg");
        f2.write(bArray);
    } 
    else if (p.getContentType().contains("image/")) {
        System.out.println("content type " + p.getContentType());
        File f = new File("C:\\Users\\man\\Desktop\\archive\\" + p.getFileName());
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
            pane.setEditable(false);
            pane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
            pane.setText((String) o);
            pane.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(HyperlinkEvent he) {
                    if(he.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        if(Desktop.isDesktopSupported()) {
                            try {
                                System.out.println("Hyperlink activated");
                                Desktop.getDesktop().browse(he.getURL().toURI());
                            } catch (URISyntaxException | IOException ex) {
                                Logger.getLogger(Morda.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });
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
}   
