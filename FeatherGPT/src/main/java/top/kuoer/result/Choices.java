package top.kuoer.result;

import top.kuoer.parameter.Message;

public class Choices {

    private int index;
    private Delta delta;
    private String finish_reason;
    private Message message;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Delta getDelta() {
        return delta;
    }

    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    public String getFinish_reason() {
        return finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Choices{" +
                "index=" + index +
                ", delta=" + delta +
                ", finish_reason='" + finish_reason + '\'' +
                ", message=" + message +
                '}';
    }
}