package lt.donatas.vaadin_laivu_musis.beans;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public static final String LOGIN = "LOGIN";
    public static final String READY_FOR_SHIPS = "READY_FOR_SHIPS";
    public static final String READY_FOR_SECOND_PLAYER = "READY_FOR_SECOND_PLAYER";
    public static final String READY_TO_PLAY = "READY_TO_PLAY";
    public static final String FINISHED = "FINISHED";

    public static final int MAKSIMALUS_SKAICIUS_KETURVIECIU = 1;    // todo perkelti i beansus
    public static final int MAKSIMALUS_SKAICIUS_TRIVIECIU = 2;
    public static final int MAKSIMALUS_SKAICIUS_DVIVIECIU = 3;
    public static final int MAKSIMALUS_SKAICIUS_VIENVIECIU = 4;

    private String gameID;
    private String status;
    private List<Event> manoEvents = new ArrayList<Event>();
    private List<Event> priesininkoEvents = new ArrayList<Event>();
    private boolean isMyTurn = false;
    private String winnerId;

    public GameData(){
    }

    public GameData(String gameID, String status, List<Event> manoEvents, List<Event> priesininkoEvents, boolean isMyTurn, String winnerId) {
        this.gameID = gameID;
        this.status = status;
        this.manoEvents = manoEvents;
        this.priesininkoEvents = priesininkoEvents;
        this.isMyTurn = isMyTurn;
        this.winnerId = winnerId;
    }

    public List<Event> getManoEvents() {
        return manoEvents;
    }

    public List<Event> getPriesininkoEvents() {
        return priesininkoEvents;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public boolean getMyLastHit() {
        return getManoEvents().get(getManoEvents().size() - 1).getHit();
    }


}
