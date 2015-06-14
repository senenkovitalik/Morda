/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.mail.Message;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author man
 * Відображення вмісту повідомлення при позиціонуванні курсору на ньому
 */
public class RowListener implements ListSelectionListener {
        
    private final JTable table;
    private List<Message> messageList = new CopyOnWriteArrayList<>();
    private final JLabel labelFrom, labelSubject;
    private final JEditorPane pane;
    
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

        (new Thread(new ShowMessage(table, messageList, labelFrom, labelSubject, pane))).start();
    }
}   
