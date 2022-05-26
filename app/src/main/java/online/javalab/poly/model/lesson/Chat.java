package online.javalab.poly.model.lesson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import online.javalab.poly.R;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.Define;

/**
 * Created by CanhNamDinh
 * on 07,December,2021
 */

public class Chat {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("questionId")
    @Expose
    private String questionId;
    @SerializedName("quizId")
    @Expose
    private String quizId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("vote")
    @Expose
    private int vote;
    @SerializedName("userLiked")
    @Expose
    private ArrayList<String> userLiked;



    public ArrayList<String> getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(ArrayList<String> userLiked) {
        this.userLiked = userLiked;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int setImageResoure() {
        if (userLiked.contains(DataStorageManager.getStringValue(Define.StorageKey.USER_ID))) {
            return R.drawable.icon_like;
        } else {
            return R.drawable.icon_dis;
        }

    }




}
