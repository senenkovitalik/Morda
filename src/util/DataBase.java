
package util;

import java.io.File;
import java.io.IOException;
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
    
    /*
    Перевіряємо чи існує файл БД. Якщо так, то підключпємося до нбого.
    Якщо ні, то створюємо його.
    */
    public Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String home = System.getProperty("user.home");
            String pathToDB = home+"\\Desktop\\db.sqlite";
            
            File f = new File(pathToDB);
            if(!f.exists()) {
                f.createNewFile();
            }
            util.print(pathToDB);
            
            con = DriverManager.getConnection("jdbc:sqlite:"+pathToDB);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Database is connected");
        return con;
    }
    
    /*
    Якщо таблиця у БД ще не створена, то створюємо її.
    */
    public void createDB() {
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE if not exists 'users' "
                    + "('id' INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " 'name' text,"
                    + " 'email' INT);");
            util.print("DB is created.");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    Запису\мо рядок у таблицю.
    */
    public void writeRow(String name, String email) {
        try {
            stmt.execute("INSERT INTO 'users' ('name', 'email') VALUES ('"+name+"', '"+email+"'); ");
            util.print("Row name="+name+", email="+email+" added");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    Зчитуємо рядок з БД та повертаємо його як обєкт Contact
    */
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
    
    /*
    Видаляємо рядок з таблиці.
    */
    public void removeRow(int id) {
        try {
            stmt = con.createStatement();
            stmt.execute("DELETE FROM 'users' WHERE id=" + id);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    Зчитуємо та виводимо всю таблицю.
    */
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
    
    /*
    Зчитуємо та повертаємо всю таблицю у вигляді List<Contact>.
    */
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
        
    /*
    Закриваємо підключення до БД.
    */
    public void close() {
        try {
            if(con != null) {
                con.close();
            }

            if(rs != null) {
                rs.close();
            }
            
            util.print("All connections are closed");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
