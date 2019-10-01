package com.movil.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movil.models.Location;
import com.movil.models.realtimelocation;
import com.movil.models.response;
import com.movil.models.user;
import com.movil.utils.Queries;
import com.movil.utils.DualPropertyRequest;
import java.text.ParseException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("location")
public class LocationController {
    private final Gson gson;

    public LocationController() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .create();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() throws ParseException{
        ArrayList<realtimelocation> response = Queries.obtenerlocact();
        return gson.toJson(new response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String writeLocation(String body)
        throws ParseException{
        Location location = gson.fromJson(body, Location.class);
        if(Queries.escribirloc(location)){
            System.out.println("[API] Location written!");
            user oldUser = Queries.selecunusr(location.getUsername());
            user user = Queries.selecunusr(location.getUsername());
            user.setLastLat(location.getLat());
            user.setLastLon(location.getLon());
            user.setLastSeen(location.getLocation_timestamp());
            if(Queries.actualizausr(user.getUsername(), user.compare(oldUser, user))){
                return gson.toJson(new response(true, "", "Location successfully updated!", 200));
            }else{
                return gson.toJson(new response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
            }
        }else{
            return gson.toJson(new response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
        }
    }
    
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserLocations(@PathParam("username") String username) throws ParseException{
        ArrayList<Location> response = Queries.obtenerlocusr(username);
        return gson.toJson(new response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserLocationsWithinDate(@PathParam("username") String username, String body)
        throws ParseException{
        DualPropertyRequest dpr = gson.fromJson(body, DualPropertyRequest.class);
        ArrayList<Location> response 
            = Queries.obtenerlocconfecha(username, dpr.getFirst_value(), dpr.getLast_value());
        return gson.toJson(new response(true, "", gson.toJson(response), 200));
    }
}
