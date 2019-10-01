
package com.movil.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movil.exceptions.InvalidPasswordException;
import com.movil.exceptions.WrongPasswordException;
import com.movil.models.response;
import com.movil.models.user;
import com.movil.utils.Queries;
import com.movil.utils.SHA256;
import com.movil.utils.SinglePRequest;
import com.movil.utils.Usregistrationrequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("users")
public class UserController {
    private final Gson gson;

    public UserController() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("dd-MM-yyyy HH:mm:ss")
            .create();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() throws ParseException{
        ArrayList<user> response = Queries.selectodosusr();
        return gson.toJson(new response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String body) 
    throws NoSuchAlgorithmException, UnsupportedEncodingException{
        Usregistrationrequest urr = gson.fromJson(body, Usregistrationrequest.class);
        if(urr.getPwd().length() > 3){
            if(urr.getPwd().equals(urr.getPwdConfirmation())){
               if(Queries.registrarusr(urr.getNewUser(), SHA256.hash(urr.getPwd()))){
                    return gson.toJson(new response(true, "", "se ha registrado el usuario", 200)); 
               }else{
                    return gson.toJson(new response(false, "tenemos un problema de conexion, por favor intente mas tarde", "", 200)); 
               }
            }else{
                return gson.toJson(new response(false, "la contraseña no coincide ", "", 200));  
            } 
        }else{
            return gson.toJson(new response(false, "la contraseña es invalida", "", 200));
        }
    }
    
    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@PathParam("username") String username, String body) 
    throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SinglePRequest spr 
            = gson.fromJson(body, SinglePRequest.class);
        user query;
        response response;
        try {
            query = Queries.selecusrcontra(username, spr.getData());
            response = new response(true, "", gson.toJson(query), 200);
        } catch (WrongPasswordException ex) {
            response = new response(false, ex.getErrormsg(), "", 200);
        } catch (InvalidPasswordException ex) {
            response = new response(false, ex.getErrormsg(), "", 200);
        }
        return gson.toJson(response);
    }
    
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@PathParam("username") String username) 
    throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException {
        user query;
        response response;
        query = Queries.selecunusr(username);
        response = new response(true, "", gson.toJson(query), 200);
        return gson.toJson(response);
    }
    
    @PUT
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUser(@PathParam("username") String username, String body) 
    throws ParseException{
        user oldOne = Queries.selecunusr(username);
        user newOne = gson.fromJson(body, user.class);
        HashMap<String, String> changes = user.compare(oldOne, newOne);
        if(changes.size() > 0){
            if(Queries.actualizausr(username, changes)){
                return gson.toJson(new response(true, "", "The changes have been successfully applied!", 200));
            }else{
                return gson.toJson(new response(false, "It seems to be a connection issue, please try again later.", "", 200));
            }
        }else{
            return gson.toJson(new response(false, "There is no changes to apply.", "", 200));
        }
    }
    
    @DELETE
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@PathParam("username") String username) {
        if(Queries.eliminarusr(username))
            return gson.toJson(new response(true, "", "The user has been successfully removed!", 200));
        else
            return gson.toJson(new response(false, "It seems to be a connection issue, please try again later.", "", 200));
    }
}
