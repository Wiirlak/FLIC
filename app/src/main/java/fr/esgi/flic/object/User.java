package fr.esgi.flic.object;

public class User {

    public String id;
    public String partner_id;

    public User() {
    }

    public User(String id, String partner_id) {
        this.id = id;
        this.partner_id = partner_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }
}