package top.kuoer.result;

public class Error {

    private String message;
    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
