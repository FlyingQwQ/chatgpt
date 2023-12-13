package top.kuoer.handler;

import com.google.gson.Gson;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.kuoer.entity.GPTMessage;
import top.kuoer.event.GPTAction;
import top.kuoer.parameter.Role;
import top.kuoer.result.Choices;
import top.kuoer.result.Data;
import top.kuoer.service.ChatGPTService;

import java.io.IOException;

public class GPTMessageHandle implements GPTAction {

    private final String id;
    private final SseEmitter sseEmitter;
    private final ChatGPTService chatGPTService;
    private StringBuffer lastContent = new StringBuffer();

    public GPTMessageHandle(String id, SseEmitter sseEmitter, ChatGPTService chatGPTService) {
        this.id = id;
        this.sseEmitter = sseEmitter;
        this.chatGPTService = chatGPTService;
    }

    @Override
    public void handleMessage(Data data) {
        String type;

        if(null != data.getError()) {
            // 处理对话接口异常
            type = "error";
            this.lastContent = new StringBuffer(data.getError().getMessage());
        } else {
            // 对话正常
            type = "normal";
            for(Choices choices : data.getChoices()) {
                String content = choices.getDelta().getContent();
                if(null != content) {
                    this.lastContent.append(content);
                } else {
                    // 将gpt信息存入数据库
                    this.chatGPTService.chatGPTMapper.addDialogueMessage(id, Role.SYSTEM.getName(), this.lastContent.toString());
                    this.lastContent = new StringBuffer("[over]");
                    break;
                }
            }
        }

        try {
            this.sseEmitter.send(new Gson().toJson(new GPTMessage(id, type, this.lastContent.toString())));

            if(this.lastContent.toString().equals("[over]")) {
                this.lastContent = new StringBuffer();
            } else if(type.equals("error")) {
                this.lastContent = new StringBuffer();
                sseEmitter.send("{ \"content\":\"[over]\" }");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
