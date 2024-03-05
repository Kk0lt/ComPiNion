package classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import classes.characters.Compinion;

public class ReponseServer {
    @SerializedName("message")
    String message;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    List<User> users;
    public ReponseServer(boolean success, List<User> users, String message) {
        this.success = success;
        this.users = users;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
