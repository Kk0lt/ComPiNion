package classes.characters;

import com.google.gson.annotations.SerializedName;

public class Compinion {

    @SerializedName("name")
    String name;
    @SerializedName("img")
    String image;
    @SerializedName("id")
    int id;
    boolean selected;
    public Compinion(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
        selected = false;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getImgUrl() {
        return "https://172.16.87.61/img/" + getImage();
    }
}
