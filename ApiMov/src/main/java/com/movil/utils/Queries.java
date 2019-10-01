
package com.movil.utils;

import com.movil.exceptions.InvalidPasswordException;
import com.movil.exceptions.WrongPasswordException;
import com.movil.models.Location;
import com.movil.models.Message;
import com.movil.models.realtimelocation;
import com.movil.models.user;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Queries {
    public static final ArrayList<user> selectodosusr() throws ParseException{
        ArrayList<user> qr = new ArrayList<>();
        String query = "SELECT * FROM users;";
        Connection dbConnection = ConnectiontoDB.create();
        try(
            Statement stmt = dbConnection.createStatement();
            ResultSet rst = stmt.executeQuery(query);
        ){
            System.out.println("obteniendo resultado");
            while (rst.next()) {
                qr.add(new user(
                        rst.getString("username"),
                        rst.getString("Lat"),
                        rst.getString("Lon"),
                        rst.getString("status"),
                        rst.getString("lSeen")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                    return qr;
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return qr;
    }

    public static final user selecusrcontra(String username, String pwd) 
    throws ParseException, WrongPasswordException, NoSuchAlgorithmException, 
    UnsupportedEncodingException, InvalidPasswordException {
        user qr = new user();
        String query = "SELECT * FROM users WHERE username == ? ;";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, username);
            ResultSet rs = prst.executeQuery();
            System.out.println("obteniendo resultados");
            while (rs.next()) {
                if(pwd.length() > 3){
                    if(rs.getString("pwd").length() > 3){
                        if(SHA256.hash(pwd).equals(rs.getString("pwd"))){
                            qr.setUsername(rs.getString("username"));
                            qr.setLastLat(rs.getString("Lat"));
                            qr.setLastLon(rs.getString("Lon"));
                            qr.setStatus(rs.getString("status"));
                            qr.setLastSeen(rs.getString("lSeen"));
                        }else{
                            throw new WrongPasswordException("contraseña incorrecta");
                        }
                    }else{
                        throw new InvalidPasswordException("la contraseña es invalida");
                    }
                }else{
                    throw new InvalidPasswordException("la contraseña es invalida");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println(" Cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return qr;
    }
    
    public static final user selecunusr(String username) throws ParseException {
        user qr = new user();
        String query = "SELECT * FROM users WHERE username == ? ;";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, username);
            ResultSet rs = prst.executeQuery();
            System.out.println(" obteniendo resultados");
            while (rs.next()) {
                qr.setUsername(rs.getString("username"));
                qr.setLastLat(rs.getString("Lat"));
                qr.setLastLon(rs.getString("Lon"));
                qr.setStatus(rs.getString("status"));
                qr.setLastSeen(rs.getString("lSeen"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("Cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return qr;
    }

    public static final boolean registrarusr(user newUser, String hash){
        boolean success = true;
        String query 
            = "INSERT INTO users(" +
                "username, " +
                "Lat, " +
                "Lon, " +
                "status, " +
                "lSeen, " +
                "pwd" +
            ") VALUES(?, ?, ?, ?, ?, ?);";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            System.out.println("holas");
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, newUser.getUsername());
            prst.setString(2, newUser.getLastLatString());
            prst.setString(3, newUser.getLastLonString());
            prst.setString(4, newUser.getStatus());
            prst.setString(5, newUser.getLastSeenISOFormatted());
            prst.setString(6, hash);
            System.out.println("obteniendo actualizacion");
            prst.executeUpdate();
            System.out.println("actualizado");
        }catch(SQLException e){
            success = false;
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return success;
    }

    public static final boolean actualizausr(String username, HashMap<String, String> changes){
        boolean success = true;
        boolean once = true;
        int index = 1;
        String query = "UPDATE users SET ";
        Set set = changes.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry currentEntry = (Map.Entry)iterator.next();
            if(once){
                once = false;
                query += currentEntry.getKey().toString() +" = ?";
            }else{
                query += ", " +currentEntry.getKey().toString() +" = ?";
            }
        }
        query += " WHERE username = ?;";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            Set set2 = changes.entrySet();
            Iterator iterator2 = set2.iterator();
            while(iterator2.hasNext()) {
                Map.Entry currentEntry = (Map.Entry)iterator2.next();
                prst.setString(index, currentEntry.getValue().toString());
                index++;
            }
            prst.setString(index, username);
            System.out.println("obteniendo actualizacion");
            prst.executeUpdate();
            System.out.println("actualizado");
        }catch(SQLException e){
            success = false;
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return success;
    }
    
    public static final boolean eliminarusr(String username){
        boolean success = true;
        String query = "delete FROM  users WHERE username == ? ;";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, username);
            System.out.println("obteniendo actualizacion");
            prst.executeUpdate();
            System.out.println("actualizado");
        }catch(SQLException e){
            success = false;
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return success;
    }

    public static final ArrayList<realtimelocation> obtenerlocact() throws ParseException{
        ArrayList<realtimelocation> qr = new ArrayList<>();
        String query = "SELECT * FROM v_current_locations;";
        Connection dbConnection = ConnectiontoDB.create();
        try(
            Statement stmt = dbConnection.createStatement();
            ResultSet rst = stmt.executeQuery(query);
        ){
            System.out.println("obteniendo actualizacion");
            while (rst.next()) {
                qr.add(new realtimelocation(
                        rst.getString("username"),
                        rst.getString("Lat"),
                        rst.getString("Lon"),
                        rst.getString("status"),
                        rst.getString("lSeen")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return qr;
    }

    public static final ArrayList<Location> obtenerlocusr(String username) throws ParseException{
        ArrayList<Location> queryResult = new ArrayList<>();
        String query = "SELECT * FROM locations WHERE username = ?;";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, username);
            ResultSet rst = prst.executeQuery();
            System.out.println("obteniendo actualizacion");
            while (rst.next()) {
                queryResult.add(
                    new Location(
                        rst.getString("Lat"),
                        rst.getString("Lon"),
                        rst.getString("locationtstamp"),
                        rst.getString("username")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return queryResult;
    }

    public static final ArrayList<Location> obtenerlocconfecha(
        String username, String initialDate, String lastDate
    ) throws ParseException {
        ArrayList<Location> queryResult = new ArrayList<>();
        String query 
            = "SELECT * FROM locations WHERE username = ? AND (location_timestamp between ? AND ?);";
        
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, username);
            prst.setString(2, initialDate);
            prst.setString(3, lastDate);
            ResultSet rst = prst.executeQuery();
            System.out.println("obteniendo actualizacion");
            while (rst.next()) {
                queryResult.add(
                    new Location(
                        rst.getString("Lat"),
                        rst.getString("Lon"),
                        rst.getString("locationtstamp"),
                        rst.getString("username")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion ");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return queryResult;
    }

    public static final boolean escribirloc(Location newLocation){
        boolean success = true;
        String query 
            = "INSERT INTO locations(" +
                "Lat, " +
                "Lon, " +
                "locationtstamp, " +
                "username" +
            ") VALUES(?, ?, ?, ?);";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, newLocation.getLatString());
            prst.setString(2, newLocation.getLonString());
            prst.setString(3, newLocation.getLocation_timestampISOFormatted());
            prst.setString(4, newLocation.getUsername());
            System.out.println("obteniendo actualizacion");
            prst.executeUpdate();
            System.out.println("actualizado");
        }catch(SQLException e){
            success = false;
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return success;
    }
    
    public static final ArrayList<Message> obtenermensajes() throws ParseException{
        ArrayList<Message> queryResult = new ArrayList<>();
        String query = "SELECT * FROM messages;";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            ResultSet rs = prst.executeQuery();
            System.out.println("obteniendo actualizacion");
            while (rs.next()) {
                queryResult.add(
                    new Message(
                        rs.getString("body"),
                        rs.getString("messagetstamp"),
                        rs.getString("sender")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return queryResult;
    }
    
    public static final boolean escribirmensajes(Message newMessage){
        boolean success = true;
        String query 
            = "INSERT INTO messages(" +
                "body, " +
                "messagetstamp, " +
                "sender" +
            ") VALUES(?, ?, ?);";
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, newMessage.getBody());
            prst.setString(2, newMessage.getMessage_timestampISOFormatted());
            prst.setString(3, newMessage.getSender());
            System.out.println("obteniendo actualizacion");
            prst.executeUpdate();
            System.out.println("actualizado");
        }catch(SQLException e){
            success = false;
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return success;
    }

    public static final ArrayList<Message> obtenermensajesporfecha(
        String initialDate, String lastDate
    ) throws ParseException {
        ArrayList<Message> queryResult = new ArrayList<>();
        String query 
            = "SELECT * FROM messages WHERE message_timestamp between ? AND ? ;";
        
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, initialDate);
            prst.setString(2, lastDate);
            ResultSet rst = prst.executeQuery();
            System.out.println("obteniendo actualizacion");
            while (rst.next()) {
                queryResult.add(
                    new Message(
                        rst.getString("body"),
                        rst.getString("messagetstamp"),
                        rst.getString("sender")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return queryResult;
    }
    
    public static final ArrayList<Message> obtenermensajesrecientes(
        String initialDate, String lastDate
    ) throws ParseException{
        ArrayList<Message> queryResult = new ArrayList<>();
        String query 
            = "SELECT * FROM messages WHERE message_timestamp between ? AND ? LIMIT ?;";
        
        Connection dbConnection = ConnectiontoDB.create();
        try{
            PreparedStatement prst = dbConnection.prepareStatement(query);
            prst.setString(1, initialDate);
            prst.setString(2, lastDate);
            prst.setInt(3, Constants.MESSAGE_LIMIT);
            ResultSet rst = prst.executeQuery();
            System.out.println("obteniendo actualizacion");
            while (rst.next()) {
                queryResult.add(
                    new Message(
                        rst.getString("body"),
                        rst.getString("messagetstamp"),
                        rst.getString("sender")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("cerrando conexion");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return queryResult;
    }
} 
