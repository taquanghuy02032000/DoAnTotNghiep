package online.javalab.poly.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopLesson implements Parcelable {
    @SerializedName("title")
    String title;
    @SerializedName("score")
    int score;
    @SerializedName("topic")
    String topic;
    @SerializedName("date")
    String date;

    public TopLesson(String title, int score, String topic, String date) {
        this.title = title;
        this.score = score;
        this.topic = topic;
        this.date = date;
    }

    protected TopLesson(Parcel in) {
        title = in.readString();
        score = in.readInt();
        topic = in.readString();
        date = in.readString();
    }

    public static final Creator<TopLesson> CREATOR = new Creator<TopLesson>() {
        @Override
        public TopLesson createFromParcel(Parcel in) {
            return new TopLesson(in);
        }

        @Override
        public TopLesson[] newArray(int size) {
            return new TopLesson[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(score);
        dest.writeString(topic);
        dest.writeString(date);
    }
}
