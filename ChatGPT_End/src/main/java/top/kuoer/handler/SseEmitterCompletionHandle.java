package top.kuoer.handler;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.kuoer.service.ChatGPTService;

public class SseEmitterCompletionHandle implements Runnable {

    private String id;
    private ChatGPTService chatGPTService;

    public SseEmitterCompletionHandle(String id, ChatGPTService chatGPTService) {
        this.id = id;
        this.chatGPTService = chatGPTService;
    }

    @Override
    public void run() {
//        System.out.println(id + " 断开连接");
    }

}
