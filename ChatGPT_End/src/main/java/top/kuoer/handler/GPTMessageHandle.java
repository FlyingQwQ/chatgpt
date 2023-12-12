package top.kuoer.handler;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.kuoer.event.GPTAction;
import top.kuoer.result.Choices;
import top.kuoer.result.Data;

import java.io.IOException;

public class GPTMessageHandle implements GPTAction {

    private final SseEmitter sseEmitter;
    private String lastContent = "";

    public GPTMessageHandle(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    @Override
    public void handleMessage(Data data) {
        for(Choices choices : data.getChoices()) {
            String content = choices.getDelta().getContent();
            if(null != content) {
                this.lastContent += content;
            } else {
                this.lastContent = "over";
                break;
            }
        }
        try {
            this.sseEmitter.send(this.lastContent);
            if(this.lastContent.equals("over")) {
                this.lastContent = "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
