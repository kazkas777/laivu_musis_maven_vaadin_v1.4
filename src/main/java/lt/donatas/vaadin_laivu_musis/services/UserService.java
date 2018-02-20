package lt.donatas.vaadin_laivu_musis.services;

import lt.donatas.vaadin_laivu_musis.beans.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserService{
    private static final String SERVER_URL = "http://miskoverslas.lt/laivu_musis";
    private static final String CREATE_USER_METHOD_NAME = "/create_user";

    public boolean createUser(User user) {
        String url = SERVER_URL + CREATE_USER_METHOD_NAME + "?name=" + user.getName() + "&email=" + user.getEmail();
        System.out.println(url);
        try {
            convertUser(user,getResponceAsString(url));
            return true;
        } catch (Exception e) {
            System.out.println("Nepavyko prisijungti");
            e.printStackTrace(System.out);
            return false;
        }
    }

    private void convertUser(User user, String body) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(body);
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            String name = (String) jsonObject.get("name");
            String email = (String) jsonObject.get("email");
            String id = (String) jsonObject.get("id");
            user.setId(id);
            user.setEmail(email);
            user.setName(name);
        }
    }

    private String getResponceAsString(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
