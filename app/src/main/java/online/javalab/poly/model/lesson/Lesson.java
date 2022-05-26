package online.javalab.poly.model.lesson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import online.javalab.poly.utils.Define;

public class Lesson implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("totalTopic")
    private int totalTopic;
    @SerializedName("topic")
    private List<Topic> topics;
    @SerializedName("count")
    private int learnCount;
    @SerializedName("quiz")
    private Quiz quiz;
    private LearnProgress progress;

    private boolean isExpand = false;

    public Lesson() {
    }

    public Lesson(String id, String title, int totalTopic, List<Topic> topics, LearnProgress progress) {
        this.id = id;
        this.title = title;
        this.totalTopic = totalTopic;
        this.topics = topics;
        this.progress = progress;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalTopic() {
        return totalTopic;
    }

    public void setTotalTopic(int totalTopic) {
        this.totalTopic = totalTopic;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public LearnProgress getProgress() {
        return progress;
    }

    public int getLearnCount() {
        return learnCount;
    }

    public void setLearnCount(int learnCount) {
        this.learnCount = learnCount;
    }

    public void setProgress(LearnProgress progress) {
        this.progress = progress;
        if (getTopics() == null) return;

        for (String completedId : progress.getCompleteList()) {
            for (Topic topic : getTopics()) {
                if (completedId.equals(topic.getId())) {
                    topic.setCompleted(true);
                    topic.setEnable(true);
                }
            }
        }
        if (progress.getCompleteList() == null || progress.getCompleteList().isEmpty()) {
            if (getTopics().get(0) != null) {
                getTopics().get(0).setEnable(true);
            }
        } else {
            int nextIndex = progress.getCompleteList().size();
            if (nextIndex < getTopics().size()) {
                if (getTopics().get(nextIndex) != null) {
                    getTopics().get(nextIndex).setEnable(true);
                }
            }
        }

        if (progress.getStatus() > Define.Status.STATUS_NOT_STARTED) {
            setExpand(true);
        }
    }

    public boolean isCompleted() {
        if (progress == null) return false;

        if (progress.getCompleteList().size() == totalTopic && progress.getQuizStatus() == Define.Status.STATUS_COMPLETED) {
            progress.setStatus(Define.Status.STATUS_COMPLETED);
            progress.setQuizStatus(Define.Status.STATUS_COMPLETED);
            return true;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", totalTopic=" + totalTopic +
                ", topics=" + topics +
                ", quiz=" + quiz +
                ", isExpand=" + isExpand +
                '}';
    }
}
