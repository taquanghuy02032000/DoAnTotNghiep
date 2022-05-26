package online.javalab.poly.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProgramDetail implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("programId")
    private String programId;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

    public String getId() {
        return id;
    }

    public String getProgramId() {
        return programId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
