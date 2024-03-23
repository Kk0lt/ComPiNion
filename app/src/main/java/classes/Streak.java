package classes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.time.LocalDate;

public class Streak {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("start_date")
    Date startDate;

    @SerializedName("end_date")
    Date endDate;

    public Streak(int id, int user_id, Date start_date, Date end_date) {
        this.id = id;
        this.user_id = user_id;
        this.startDate = start_date;
        this.endDate = end_date;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date start_date) {
        this.startDate = start_date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date end_date) {
        this.endDate = end_date;
    }
}
