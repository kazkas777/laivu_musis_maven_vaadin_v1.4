package lt.donatas.vaadin_laivu_musis.beans;

public class User {
    private String id;
    private String name;
    private String email;
    private String priesininkoid;

    public String getPriesininkoid() {
        return priesininkoid;
    }

    public void setPriesininkoid(String priesininkoid) {
        this.priesininkoid = priesininkoid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
