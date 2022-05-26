package online.javalab.poly.model.lesson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("lessonId")
    @Expose
    private String lessonId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;

    private boolean isCompleted=false;

    private boolean isEnable=false;

    public Topic() {
      //  this.isCompleted = false;
    }

    public Topic(String id, String lessonId, String title, String content, boolean isCompleted) {
        this.id = id;
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isEnabled() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}


