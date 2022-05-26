package online.javalab.poly.model.lesson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LearnProgress {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("lessonId")
    @Expose
    private String lessonId;
    @SerializedName("completed")
    @Expose
    private List<String> completeList = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("quizStatus")
    @Expose
    private Integer quizStatus;
    @SerializedName("quizMarked")
    @Expose
    private Integer quizMarked;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("lastModify")
    @Expose
    private String lastModify;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public List<String> getCompleteList() {
        return completeList;
    }

    public void setCompleteList(List<String> completeList) {
        this.completeList = completeList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getQuizStatus() {
        return quizStatus;
    }

    public void setQuizStatus(Integer quizStatus) {
        this.quizStatus = quizStatus;
    }

    public Integer getQuizMarked() {
        return quizMarked;
    }

    public void setQuizMarked(Integer quizMarked) {
        this.quizMarked = quizMarked;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

}
