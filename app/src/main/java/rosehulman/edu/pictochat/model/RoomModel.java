package rosehulman.edu.pictochat.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class RoomModel {
    private String title;
    private String id;
    private ArrayList<FriendModel> users;

    public RoomModel() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }
    public ArrayList<FriendModel> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<FriendModel> users) {
        this.users = users;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
