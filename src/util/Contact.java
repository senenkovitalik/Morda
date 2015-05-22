/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author Vital
 */
public class Contact {
    
    public String name, email;
    public int id;
    
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    } 
}
