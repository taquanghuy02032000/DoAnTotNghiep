package online.javalab.poly.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profile implements Parcelable {
    @SerializedName("mark")
    float mark;
    @SerializedName("date")
    String date;

    public Profile(float mark, String date) {
        this.mark = mark;
        this.date = date;
    }

    protected Profile(Parcel in) {
        mark = in.readFloat();
        date = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
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
        dest.writeFloat(mark);
        dest.writeString(date);
    }
}
