package online.javalab.poly.model.lesson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Quiz {
    @SerializedName("_id")
    private String id;
    @SerializedName("lessonId")
    private String lessonId;
    @SerializedName("name")
    private String name;
    @SerializedName("question")
    private List<Question> questions;

    public Quiz() {
    }

    public Quiz(String id, String lessonId, String name, List<Question> questions) {
        this.id = id;
        this.lessonId = lessonId;
        this.name = name;
        this.questions = questions;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
