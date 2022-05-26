package online.javalab.poly.model.lesson;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("quizId")
    private String quizId;
    @SerializedName("STT")
    private int stt;
    @SerializedName("question")
    private String question;
    @SerializedName("answer")
    private List<String> answers;
    @SerializedName("correctAnswer")
    private String correctAnswer;
    private boolean isCorrected=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public boolean isCorrected() {
        return isCorrected;
    }

    public void setCorrected(boolean corrected) {
        isCorrected = corrected;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean checkAnswer(String ans) {
        if (TextUtils.isEmpty(getCorrectAnswer())) return false;
       int correctAnswerPosition=Integer.parseInt(getCorrectAnswer()) ;
       String correctAnswer = getAnswers().get(correctAnswerPosition-1).trim();
        return ans.equals(correctAnswer);
    }

    public String getTextAnswer(){
        if (TextUtils.isEmpty(getCorrectAnswer())) return "";
        int correctAnswerPosition= Integer.parseInt(getCorrectAnswer()) ;
        String mCorrectAnswer = getAnswers().get(correctAnswerPosition-1).trim();

        return correctAnswerPosition+". "+mCorrectAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", quizId='" + quizId + '\'' +
                ", stt='" + stt + '\'' +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                ", correctAnswer=" + correctAnswer +
                ", isCorrected=" + isCorrected +
                '}';
    }
}
