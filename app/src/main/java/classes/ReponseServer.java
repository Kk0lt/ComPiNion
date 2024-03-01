package classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReponseServer {

    public ReponseServer(boolean success, List<User> users, String message) {
        this.success = success;
        this.users = users;
        this.message = message;
    }

    @SerializedName("success")
    boolean success;
    @SerializedName("users")
    List<User> users;

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

    @SerializedName("message")
    String message;

}
