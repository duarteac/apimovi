
package com.movil.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Location {
    private double Lat;
    private double Lon;
    private Date locationtstamp;
    private String username;

    public Location(
        String Lat, String Lon, String location_timestamp, String username
    ) throws ParseException {
        this.Lat = Double.parseDouble(Lat);
        this.Lon = Double.parseDouble(Lon);
        this.locationtstamp 
            = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                .parse(location_timestamp);
        this.username = username;
    }

    public double getLat() {
        return Lat;
    }

    public String getLatString() {
        return Double.toString(Lat);
    }
    
    public void setLat(double lat) {
        this.Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public String getLonString() {
        return Double.toString(Lat);
    }
    
    public void setLon(double lon) {
        this.Lon = lon;
    }

    public Date getLocation_timestamp() {
        return locationtstamp;
    }

    public String getLocation_timestampISOFormatted(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(locationtstamp);
    }
    
    public void setLocation_timestamp(Date location_timestamp) {
        this.locationtstamp = location_timestamp;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
