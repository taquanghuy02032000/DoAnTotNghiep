package online.javalab.poly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {

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
    private Integer mark;
    @SerializedName("tokenDevice")
    @Expose
    private String tokenDevice;

    public String getTokenDevice() {
        return tokenDevice;
    }

    public void setTokenDevice(String tokenDevice) {
        this.tokenDevice = tokenDevice;
    }

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

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

}
