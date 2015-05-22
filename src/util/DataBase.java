/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vital
 */
public class DataBase {
    
    private Connection con;
    public Statement stmt;
    public ResultSet rs;
    private final Utilities util = new Utilities();
    public Contact contact;
    
    
    public Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Vital\\Desktop\\db.sqlite");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Database is connected");
        return con;
    }
    
    public void createDB() {
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE if not exists 'users' "
                    + "('id' INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " 'name' text,"
                    + " 'email' INT);");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeRow(String name, String email) {
        try {
            stmt.execute("INSERT INTO 'users' ('name', 'email') VALUES ('"+name+"', '"+email+"'); ");
            util.print("Row name="+name+", email="+email+" added");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Contact readRow(String name, String email) {
        Contact c = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE email='"+email+"';");
            
            int id = rs.getInt("id");
            String n = rs.getString("name");
            String e = rs.getString("email");
            
            c = new Contact(id, n, e);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
    
    public void removeRow(int id) {
        try {
            stmt = con.createStatement();
            stmt.execute("DELETE FROM 'users' WHERE id=" + id);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readDB() {
        
        
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users;");
            
            while(rs.next())
            {
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                String  phone = rs.getString("email");
                System.out.println( "ID = " + id );
                System.out.println( "name = " + name );
                System.out.println( "email = " + phone );
                System.out.println();
            }
            
            System.out.println("Таблица выведена");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public List<Contact> readAllDB() {
        
        List<Contact> contactList = new ArrayList<>();
        
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users;");
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                String  email = rs.getString("email");
                
                contact = new Contact(id, name, email);
                contactList.add(contact);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return contactList;
    }
        
    public void close() {
        try {
            if(con != null) {
                con.close();
            }

//            if(stmt != null && !stmt.isClosed()) {
//                stmt.close();
//            }
            
            if(rs != null) {
                rs.close();
            }
            
            util.print("All connections are closed");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
