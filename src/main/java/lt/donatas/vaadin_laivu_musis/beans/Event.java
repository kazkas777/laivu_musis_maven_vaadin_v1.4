package lt.donatas.vaadin_laivu_musis.beans;

import java.util.Date;

public class Event {
    Date date;
    String coordinate;
    String userId;
    boolean hit;

    public Event(Date date, String coordinate, String userId, boolean hit) {
        this.date = date;
        this.coordinate = coordinate;
        this.userId = userId;
        this.hit = hit;
    }

    public Date getDate() {
        return date;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public String getUserId() {
        return userId;
    }

    public boolean getHit() {
        return hit;
    }
}
