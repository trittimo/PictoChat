package rosehulman.edu.pictochat.model;

import java.util.ArrayList;

public class RoomModel {
    private String name;
    private ArrayList<FriendModel> members;
    public RoomModel() {
        this.name = "Default Name";
        this.members = new ArrayList<>();
        members.add(new FriendModel());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMemberCount() {
        return members.size();
    }

    public void addMember(FriendModel member) {
        members.add(member);
    }

    public FriendModel getMember(int index) {
        return members.get(index);
    }
}
