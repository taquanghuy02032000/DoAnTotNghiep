package online.javalab.poly.model.rank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("gmail")
    @Expose
    private String gmail;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("mark")
    @Expose
    private int mark;

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    @SerializedName("top")
    @Expose
    private Integer top;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}
