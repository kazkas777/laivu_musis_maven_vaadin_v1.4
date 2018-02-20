package lt.donatas.vaadin_laivu_musis.services;

import lt.donatas.vaadin_laivu_musis.beans.Event;
import lt.donatas.vaadin_laivu_musis.beans.GameData;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameService {
    //private static final String SERVER_URL = "http://192.168.1.34:8080";
    private static final String SERVER_URL = "http://miskoverslas.lt/laivu_musis";
    private static final String JOIN_METHOD_NAME = "/join";
    private static final String STATUS_METHOD_NAME = "/status";
    private static final String SETUP_METHOD_NAME = "/setup";
    private static final String TURN_METHOD_NAME = "/turn";

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

    private GameData parseResponse(String userId, String body) {
        try {
            JSONParser parser = new JSONParser();
            Object obj;
            try {
                obj = parser.parse(body);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return null;
            }
            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                String gameId = (String) jsonObject.get("id");
                String nextTurnForUserId = (String) jsonObject.get("nextTurnForUserId");
                String status = (String) jsonObject.get("status");
                String winnerUserId = (String) jsonObject.get("winnerUserId");
                JSONArray events = (JSONArray) jsonObject.get("events");

                boolean isMyTurn;

                if (nextTurnForUserId.equals(userId)) {
                    isMyTurn= true;
                } else  {
                   // nextTurnForUserId;
                    isMyTurn=false;
                }

                List<Event> manoEvents = parseManoEvents(userId, events);
                List<Event> priesininkoEvents = parsePriesininkoEvents(userId, events);

                return new GameData(gameId, status, manoEvents, priesininkoEvents, isMyTurn, winnerUserId);
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    private List<Event> parseManoEvents(String userId, JSONArray events){
        return parseEvents(userId, events, true);
    }

    private List<Event> parsePriesininkoEvents(String userId, JSONArray events){
        return parseEvents(userId, events, false);
    }

    private List<Event> parseEvents(String userId, JSONArray events, boolean arManoEvents) {
        List<Event> playerEvents=new ArrayList<>();
        if (events != null) {
            for (Object event : events) {
                JSONObject jsonObj = (JSONObject) event;

                Date date = new Date((Long) jsonObj.get("date"));
                String playerId = (String) jsonObj.get("userId");
                String coordinate = (String) ((JSONObject) jsonObj.get("coordinate")).get("column") + ((JSONObject) jsonObj.get("coordinate")).get("row");
                boolean hit = (boolean) jsonObj.get("hit");


                if (arManoEvents && userId.equals(playerId)) {
                    playerEvents.add(new Event(date, coordinate, playerId, hit));
                }
                if (!arManoEvents && !userId.equals(playerId)){
                    playerEvents.add(new Event(date, coordinate, userId, hit));
                }
            }
        }
        return playerEvents;
    }

    public GameData join(String userId) {
        String url = SERVER_URL + JOIN_METHOD_NAME + "?user_id=" + userId;
        try {
           return parseResponse(userId, getResponceAsString(url));
        } catch (Exception e) {
            System.out.println("Join no response");
            e.printStackTrace(System.out);
        }
        return null;
    }

    public GameData status(String userId, String gameId) {
        String url = SERVER_URL + STATUS_METHOD_NAME + "?game_id=" + gameId;
        try {
            return parseResponse(userId, getResponceAsString(url));
        } catch (Exception e) {
            System.out.println("Status no response");
            e.printStackTrace(System.out);
            return null;
        }
    }

    public GameData setup(String userId, String gameId, String VisiLaivai) {
        String url = SERVER_URL + SETUP_METHOD_NAME + "?game_id=" + gameId + "&user_id=" + userId + "&data=" + VisiLaivai;
        try {
            return parseResponse(userId, getResponceAsString(url));
        } catch (Exception e) {
            System.out.println("Setup no response");
            e.printStackTrace(System.out);
            return null;
        }
    }

    public GameData turn(String userId, String gameId, String coordinate) {
        String url = SERVER_URL + TURN_METHOD_NAME + "?game_id=" + gameId + "&user_id=" + userId + "&data=" + coordinate;
        try {
            return parseResponse(userId, getResponceAsString(url));
        } catch (Exception e) {
            System.out.println("Turn no response");
            e.printStackTrace(System.out);
            return null;
        }
    }

    public void turnCheat(String priesininkoId, String gameId) {
        String url = SERVER_URL + TURN_METHOD_NAME + "?game_id=" + gameId + "&user_id=" + priesininkoId + "&data=K0";
        try {
            parseResponse(priesininkoId, getResponceAsString(url));
        } catch (Exception e) {
            System.out.println("Turn no response");
            e.printStackTrace(System.out);
        }
    }




}
