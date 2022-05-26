package online.javalab.poly.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Todo by CanhNamDinh on: 1/10/2022
 */
public class QA implements Serializable {


    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("idQuestionId")
    @Expose
    private String idQuestionId;
    @SerializedName("type")
    @Expose
    private boolean type;

    public QA() {
    }

    public QA(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getIdQuestionId() {
        return idQuestionId;
    }

    public void setIdQuestionId(String idQuestionId) {
        this.idQuestionId = idQuestionId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
