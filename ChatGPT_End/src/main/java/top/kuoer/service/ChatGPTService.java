package top.kuoer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.kuoer.GPT;
import top.kuoer.common.Result;
import top.kuoer.common.ResultCode;
import top.kuoer.handler.GPTMessageHandle;
import top.kuoer.handler.SseEmitterCompletionHandle;
import top.kuoer.mapper.ChatGPTMapper;
import top.kuoer.parameter.GPTSetting;
import top.kuoer.parameter.Message;
import top.kuoer.parameter.Role;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatGPTService {

    public final Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();
    private final Map<String, GPT> gptMap = new ConcurrentHashMap<>();

    public ChatGPTMapper chatGPTMapper;

    @Autowired
    public ChatGPTService(ChatGPTMapper chatGPTMapper) {
        this.chatGPTMapper = chatGPTMapper;
    }

    public SseEmitter connect(String id, String model) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(new SseEmitterCompletionHandle(id, this));
        this.sseCache.put(id, sseEmitter);

        GPTSetting setting = new GPTSetting();
        setting.setApiUrl("https://api.openai-proxy.org/v1/chat/completions");
        setting.setKey("sk-NJPRpBK7bgjbhy9Lk9yFAMtGDSa95RKGr3Vs123dYkWYiD9t");
        setting.setModel(model);
        setting.setMax_tokens(1024);
        setting.setTemperature(0.9);
        setting.setStream(true);

        // 从数据库中恢复聊天
        List<Message> messages = this.chatGPTMapper.getDialogueMessageList(id);
        setting.setMessages(messages);

        GPT gpt = new GPT(setting);
        gpt.addGPTMessageHandle(new GPTMessageHandle(id, sseEmitter, this));
        this.gptMap.put(id, gpt);

        try {
            // 提醒客户端连接成功！
            sseEmitter.send("{ \"content\":\"[start]\" }");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sseEmitter;
    }

    public Result close(String id) {
        SseEmitter sse = this.sseCache.get(id);
        if(null != sse) {
            sse.complete();
            this.sseCache.remove(id);
        }
        return new Result(ResultCode.SUCCESS, "关闭成功！");
    }

    public Result remove(String id) {
        SseEmitter sse = this.sseCache.get(id);
        if(null != sse) {
            sse.complete();
            this.sseCache.remove(id);
            this.gptMap.remove(id);
            this.chatGPTMapper.removeDialogue(id);
            return new Result(ResultCode.SUCCESS, "删除成功！");
        }
        return new Result(ResultCode.NOTFOUND, "找不到这个id");
    }


    public Result sendMsg(String id, String content) {
        SseEmitter sse = this.sseCache.get(id);
        if(null != sse) {
            this.chatGPTMapper.addDialogueMessage(id, Role.USER.getName(), content);
            GPT gpt = this.gptMap.get(id);
            gpt.sendMessage(Role.USER, content);

            return new Result(ResultCode.SUCCESS, "发送成功！");
        }
        return new Result(ResultCode.NOTFOUND, "找不到这个id");
    }

    public Result getMessageItems(String id) {
        List<Message> messages = this.chatGPTMapper.getDialogueMessageList(id);
        if(null != messages) {
            return new Result(ResultCode.SUCCESS, messages);
        }
        return new Result(ResultCode.NOTFOUND, "找不到这个id");
    }
}
