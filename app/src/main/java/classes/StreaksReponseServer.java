package classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StreaksReponseServer {

    @SerializedName("message")
    String message;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    List<Streak> streaks;

    public StreaksReponseServer(String message, boolean success, List<Streak> _streaks) {
        this.message = message;
        this.success = success;
        this.streaks = _streaks;
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

    public List<Streak> getStreaks() {
        return streaks;
    }

    public void setStreaks(List<Streak> streaks) {
        this.streaks = streaks;
    }

}
