package top.kuoer.entity;

public class GPTMessage {

    private String id;
    private String type;
    private String content;

    public GPTMessage(String id, String type, String content) {
        this.id = id;
        this.type = type;
        this.content = content;
    }
}
