package tokens;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;

/**
 * @author Lukas Amtoft Dahl
 */

public class TokenClient {

    WebTarget baseUrl;
    LinkedList<String> tokens = new LinkedList<>();
    String error = "";

    public TokenClient() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8015/");
        //baseUrl = client.target("http://g-15.compute.dtu.dk:8015/");
    }

    public void requestTokens(String id, int count) {

        JsonObject json = Json.createObjectBuilder()
                .add("count", count)
                .add("id", id)
                .build();

        Response response = baseUrl.
                path("tokens")
                .request()
                .post(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() != 200){
            error = response.readEntity(String.class);
            System.out.println(error);
            return;
        }

        JsonArray jsonArray = response.readEntity(JsonArray.class);
        System.out.println(jsonArray.toString());
        for (int i = 0; i < jsonArray.size(); i++)
            tokens.add(jsonArray.getString(i));

    }
}
