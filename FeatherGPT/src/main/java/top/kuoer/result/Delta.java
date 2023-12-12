package top.kuoer.result;

public class Delta {

    private String role;
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Delta{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
