package classes.characters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import classes.User;

public class CompinionsReponseServer {
    @SerializedName("message")
    String message;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    List<Compinion> compinionList;

    public CompinionsReponseServer(String message, boolean success, List<Compinion> compinionList) {
        this.message = message;
        this.success = success;
        this.compinionList = compinionList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Compinion> getCompinionList() {
        return compinionList;
    }

    public void setCompinionList(List<Compinion> compinionList) {
        this.compinionList = compinionList;
    }
}
