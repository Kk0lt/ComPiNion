package classes;

import com.google.gson.annotations.SerializedName;

public class UserResponseServer {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private User user;

    public UserResponseServer(boolean success, User user, String message) {
        this.success = success;
        this.user = user;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
