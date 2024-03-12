package classes.characters;

import com.google.gson.annotations.SerializedName;

public class Compinion {

    @SerializedName("name")
    String name;
    @SerializedName("img")
    String image;
    @SerializedName("id")
    int id;
    public Compinion(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImgUrl() {
        // Assuming your Laravel backend is running on http://yourdomain.com
        return "http://10.0.2.2/ComPiNion/public/img/" + image;
    }
}
