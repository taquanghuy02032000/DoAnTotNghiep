package online.javalab.poly.model.lesson;
import com.google.gson.annotations.SerializedName;

public class ProgressRequest {

    private String userId;

    private String lessonId;

    @SerializedName("completed")
    private String topicId;

    private Integer status;

    private Integer quizStatus;

    private Integer quizMarked;

    public ProgressRequest() {
    }

    public ProgressRequest(String userId, String lessonId, String topicId, Integer status, Integer quizStatus, Integer quizMarked) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.topicId = topicId;
        this.status = status;
        this.quizStatus = quizStatus;
        this.quizMarked = quizMarked;
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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
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
}
