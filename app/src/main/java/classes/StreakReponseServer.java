package classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StreakReponseServer {

    @SerializedName("message")
    String message;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    Streak streak;

    public StreakReponseServer(String message, boolean success, Streak streak) {
        this.message = message;
        this.success = success;
        this.streak = streak;
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

    public Streak getStreak() {
        return streak;
    }

    public void setStreak(Streak streak) {
        this.streak = streak;
    }
}
