package rosehulman.edu.pictochat.model;
public class FriendListItemModel {
    private String name;
    private String email;

    public FriendListItemModel() {
        this.name = "John Doe";
        this.email = "default@gmail.com";
    }

    public FriendListItemModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
