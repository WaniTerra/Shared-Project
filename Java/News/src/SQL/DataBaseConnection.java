/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author erens
 */
public class DataBaseConnection {

    private static Connection conn = null;
    

    private static void getConnection() {
        String url = "jdbc:mysql://localhost:3306/newssitedb";
        String user = "root";
        String password = "root";

        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, user, password);
               
            }
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                
            }
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public static Connection getConn() {
        getConnection();
        return conn;
    }
    
    public static void getCloseConnection(){
        closeConnection();
    }
}
