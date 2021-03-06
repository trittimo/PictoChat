package rosehulman.edu.pictochat.model;

import com.google.firebase.database.Exclude;

public class FriendModel {
    private String name;
    private String email;
    private String key;

    public FriendModel() {}

    public FriendModel(String email) {
        this.email = email;
    }

    public FriendModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
