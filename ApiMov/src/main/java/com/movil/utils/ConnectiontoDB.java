
package com.movil.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectiontoDB {
    public static Connection create(){
        Properties props = new Properties();
            
        try {
            System.out.println("cargando ...");
            InputStream is = ConnectiontoDB.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(is);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            props = null;
        }
        
        if(props != null){ 
            String url = "jdbc:sqlite:" +props.getProperty("DB_PATH");
            System.out.println("intentando conectar a la base de datos");
            Connection dbConnection = null;
            try {
                Class.forName("org.sqlite.JDBC");
                dbConnection = DriverManager.getConnection(url);
                System.out.println("conexion establecida");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectiontoDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            return dbConnection;
        }else{
            return null;
        }
    }
}
