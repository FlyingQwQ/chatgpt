package top.kuoer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.kuoer.GPT;
import top.kuoer.common.Result;
import top.kuoer.common.ResultCode;
import top.kuoer.handler.GPTMessageHandle;
import top.kuoer.handler.SseEmitterCompletionHandle;
import top.kuoer.parameter.GPTSetting;
import top.kuoer.parameter.Role;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatGPTService {

    public final Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();
    private final Map<String, GPT> gptMap = new ConcurrentHashMap<>();
    

    public ChatGPTService() {
        
    }

    public SseEmitter connect(String id) {
        SseEmitter sseEmitter = new SseEmitter(5_60_000L);
        sseEmitter.onCompletion(new SseEmitterCompletionHandle(id, this));

        GPT gpt = this.gptMap.get(id);
        if(null == gpt) {
            GPTSetting setting = new GPTSetting();
            setting.setApiUrl("https://api.openai-proxy.org/v1/chat/completions");
            setting.setKey("");
            setting.setModel("gpt-3.5-turbo");
            setting.setMax_tokens(1024);
            setting.setTemperature(0.9);
            setting.setStream(true);

            gpt = new GPT(setting);
            gpt.addGPTMessageHandle(new GPTMessageHandle(sseEmitter));
            this.gptMap.put(id, gpt);
        } else {
            gpt.clearAllGPTMessageHandle();
            gpt.addGPTMessageHandle(new GPTMessageHandle(sseEmitter));
        }
        this.sseCache.put(id, sseEmitter);

        try {
            // 提醒客户端连接成功！
            sseEmitter.send("over");
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
        }
        return new Result(ResultCode.SUCCESS, "删除成功！");
    }


    public Result sendMsg(String id, String content) {
        SseEmitter sse = this.sseCache.get(id);
        if(null != sse) {
            GPT gpt = this.gptMap.get(id);
            gpt.sendMessage(Role.USER, content);

            return new Result(ResultCode.SUCCESS, "发送成功！");
        }
        return new Result(ResultCode.NOTFOUND, "找不到这个id");
    }

    public Result getMessageItems(String id) {
        SseEmitter sse = this.sseCache.get(id);
        if(null != sse) {
            GPT gpt = this.gptMap.get(id);
            return new Result(ResultCode.SUCCESS, gpt.getGPTSetting().getMessages());
        }
        return new Result(ResultCode.NOTFOUND, "找不到这个id");
    }
}
