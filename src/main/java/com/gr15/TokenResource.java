package com.gr15;

import com.gr15.exceptions.QueueException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * @author Lukas Amtoft Dahl
 */

@Path("/tokens")
public class TokenResource {

    HashMap<String, LinkedList<String>> tokenMap = new HashMap<>();
    TokenManager tokenManager = TokenManager.getInstance();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonArray createTokens(JsonObject json) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        try {
            if (!tokenManager.accountExists(json.getString("id"))) {
                JsonObject response = Json.createObjectBuilder().add("errorMessage", "Account does not exist").build();
                throw new BadRequestException(Response.status(400).entity(response).type(MediaType.APPLICATION_JSON).build());
            }
        } catch (QueueException e) {
            JsonObject response = Json.createObjectBuilder().add("errorMessage", e.getMessage()).build();
            throw new InternalServerErrorException(Response.status(500).entity(response).type(MediaType.APPLICATION_JSON).build());
        }

        String[] stringArray = tokenManager.generateTokens(json.getInt("count"), json.getString("id"));

        for (String token: stringArray)
            arrayBuilder.add(token);

        return arrayBuilder.build();
    }
}