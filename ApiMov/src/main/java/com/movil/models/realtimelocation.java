
package com.movil.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class realtimelocation {
    
    private String username;
    private double Lat;
    private double Lon;
    private Date lSeen;
    private String status;

    public realtimelocation(
        String username, String Lat, String Lon, 
        String status, String lSeen ) throws ParseException {
        this.username = username;
        this.Lat = Double.parseDouble(Lat);
        this.Lon = Double.parseDouble(Lon);
        this.lSeen = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(lSeen);
        this.status = status;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        this.Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        this.Lon = lon;
    }

    public Date getLastSeen() {
        return lSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lSeen = lastSeen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
