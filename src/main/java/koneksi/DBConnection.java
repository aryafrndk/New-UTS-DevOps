/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koneksi;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBConnection {
       
       static final String DB_URL = "jdbc:mysql://localhost:3306/db_mahasiswa";
       static final String USER = "root";
       static final String PASS = "";
       public static Connection connectDB(){
           Connection conn = null;
           try{
 //              Class.forName("com.mysql.jdbc.Driver");
               conn = DriverManager.getConnection(DB_URL,USER,PASS);
               return conn;
           }catch(SQLException ex){
               System.out.println("There were erorrs while connecting to db.");
               return null;
           }
       }

    
}
