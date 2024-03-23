package classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StreaksReponseServer {

    @SerializedName("message")
    String message;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    List<Streak> data; // Modifier le nom de la propriété et son type pour refléter le changement

    public StreaksReponseServer(boolean success, List<Streak> data, String message) {
        this.success = success;
        this.data = data; // Modifier l'assignation de la propriété pour refléter le changement
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(List<Streak> data) { // Modifier le nom de cette méthode pour refléter le changement
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Streak> getData() { // Modifier le nom de cette méthode pour refléter le changement
        return data;
    }
}
