package classes.characters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompinionReponseServer {
    @SerializedName("message")
    String message;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    Compinion compinion;

    public CompinionReponseServer(String message, boolean success, Compinion compinion) {
        this.message = message;
        this.compinion = compinion;
        this.success = success;
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

    public Compinion getCompinion() {
        return compinion;
    }

    public void setCompinion(Compinion compinion) {
        this.compinion = compinion;
    }
}
