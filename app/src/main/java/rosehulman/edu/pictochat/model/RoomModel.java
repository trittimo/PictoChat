package rosehulman.edu.pictochat.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class RoomModel {
    private String title;
    private String id;
    private long lastMessage;
    private boolean isBold;

    public RoomModel() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(long lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Exclude
    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }
}
