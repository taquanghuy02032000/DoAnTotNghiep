package online.javalab.poly.model.lesson;

import java.util.List;

public class BaseLessonProgress {

    private List<Lesson> lessonList;
    private List<LearnProgress> progressList;

    public BaseLessonProgress() {
    }

    public BaseLessonProgress(List<Lesson> lessonList, List<LearnProgress> progressList) {
        this.lessonList = lessonList;
        this.progressList = progressList;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public List<LearnProgress> getProgressList() {
        return progressList;
    }

    public void setProgressList(List<LearnProgress> progressList) {
        this.progressList = progressList;
    }
}
