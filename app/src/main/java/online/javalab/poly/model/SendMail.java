package online.javalab.poly.model;

import com.google.gson.annotations.Expose;
/**
 * Todo by CanhNamDinh on: 1/10/2022
 */
public class SendMail {
    @Expose
    private String email;
    @Expose
    private String subject;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
