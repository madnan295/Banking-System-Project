/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataBase;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author Mohammad Adnan
 */
public class connection {
    public static Connection connect() throws SQLException{
        try{
            String url = "jdbc:mysql://localhost:3306/banking_management_db?zeroDateTimeBehavior=CONVERT_TO_NULL";
            String user = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            return con;
        }catch(HeadlessException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Not Connected");
        }
        return null;
    }

}