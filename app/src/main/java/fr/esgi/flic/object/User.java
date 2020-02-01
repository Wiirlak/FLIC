package fr.esgi.flic.object;
import com.google.firebase.database.DatabaseReference;


public class User {

    public String id;
    public double age;
    public String gender;
    public String userName;
    public DatabaseReference partner_id;

    //required default constructor
    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, double age, String gender, String userName, DatabaseReference partner_id) {
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.userName = userName;
        this.partner_id = partner_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DatabaseReference getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(DatabaseReference partner_id) {
        this.partner_id = partner_id;
    }
}