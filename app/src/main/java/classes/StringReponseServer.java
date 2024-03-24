package classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StringReponseServer {

    @SerializedName("message")
    String message;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    String data;

    public StringReponseServer(boolean success, String data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
