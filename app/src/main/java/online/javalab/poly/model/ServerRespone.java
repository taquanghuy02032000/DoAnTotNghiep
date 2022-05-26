package online.javalab.poly.model;

public class ServerRespone {

    private int success;
    private String message;

    public ServerRespone(int success, String message) {
        this.success = success;
        this.message = message;
    }

    public ServerRespone() {
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
