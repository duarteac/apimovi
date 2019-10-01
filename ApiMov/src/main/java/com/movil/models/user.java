/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.models;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;


public class user {
    private String username;
    private double Lat;
    private double Lon;
    private String status;
    private Date lSeen;

    public user() throws ParseException {
    }
    
    public user(String username,String Lat, String Lon, String status,String lSeen) throws ParseException {
        this.username = username;
        this.Lat = Double.parseDouble(Lat);
        this.Lon = Double.parseDouble(Lon);
        this.status = status;
        this.lSeen = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ").parse(lSeen);
    }

    public static HashMap<String, String> compare(user oldOne, user newOne){
        HashMap<String, String> differences = new HashMap<>();
        if(oldOne.username.equals(newOne.username)){
             if(!oldOne.getLastLatString().equals(newOne.getLastLatString())){
                differences.put("Lat", newOne.getLastLatString());
            }else if(!oldOne.getLastLonString().equals(newOne.getLastLonString())){
                differences.put("Lon", newOne.getLastLonString());
            }else if(!oldOne.status.equals(newOne.status)){
                differences.put("status", newOne.status);
            }else if(!oldOne.getLastSeenISOFormatted().equals(newOne.getLastSeenISOFormatted())){
                differences.put("lSeen", newOne.getLastSeenISOFormatted());
            }
        }
        return differences;
    }
    
    public boolean isOnline(){
        return this.status.toLowerCase().trim().equals("online");
    }
    
    public void setLastLat(String lastLat) {
        this.Lat = Double.parseDouble(lastLat);;
    }
    
    public void setLastLon(String lastLon) {
        this.Lon = Double.parseDouble(lastLon);;
    }
    
    public void setLastSeen(String lastSeen) throws ParseException {
        this.lSeen = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(lastSeen);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLastLat() {
        return Lat;
    }
    
    public String getLastLatString() {
        return Double.toString(Lat);
    }

    public void setLastLat(double Lat) {
        this.Lat = Lat;
    }

    public double getLastLon() {
        return Lon;
    }

    public String getLastLonString() {
        return Double.toString(Lon);
    }
    
    public void setLastLon(double Lon) {
        this.Lon = Lon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastSeen() {
        return lSeen;
    }

    public String getLastSeenISOFormatted(){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ").format(lSeen);
    }
    
    public void setLastSeen(Date lSeen) {
        this.lSeen = lSeen;
    }

    
    
    
}
