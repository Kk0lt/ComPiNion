package classes;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.time.LocalDate;

public class Streak {

    @SerializedName("id")
    int id;
    @SerializedName("user_id")
    int user_id;
    @SerializedName("start_date")
    LocalDate start_date;
    @SerializedName("end_date")
    LocalDate end_date;

    public Streak(int id, int user_id, LocalDate start_date, LocalDate end_date) {
        this.id = id;
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }
}
