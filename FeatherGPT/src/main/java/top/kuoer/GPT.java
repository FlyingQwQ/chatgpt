package top.kuoer;

import com.google.gson.Gson;
import top.kuoer.event.GPTAction;
import top.kuoer.parameter.GPTSetting;
import top.kuoer.parameter.Message;
import top.kuoer.parameter.Role;
import top.kuoer.result.Choices;
import top.kuoer.result.Data;
import top.kuoer.utils.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GPT {

    private GPTSetting GPTSetting;
    private Request request;
    private Gson gson;
    private List<GPTAction> GPTMessageHandleList = new ArrayList<>();

    public GPT(GPTSetting setting) {
        this.GPTSetting = setting;
        if(null == this.GPTSetting.getMessages()) {
            this.GPTSetting.setMessages(new ArrayList<>());
        }
        this.request = Request.getInstance();
        this.gson = new Gson();
    }

    public Data sendMessage(Role role, String content) {
        this.GPTSetting.getMessages().add(new Message(role.getName(), content));
        if(!GPTSetting.getStream()) {
            try {
                return this.gson.fromJson(this.request.simpleRequest(this.GPTSetting), Data.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.handleStreamMsg(this.GPTSetting);
        }
        return null;
    }

    public void addGPTMessageHandle(GPTAction gptAction) {
        this.GPTMessageHandleList.add(gptAction);
    }

    public void clearAllGPTMessageHandle() {
        this.GPTMessageHandleList.clear();
    }

    public void handleStreamMsg(GPTSetting setting) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.request.streamRequest(setting), StandardCharsets.UTF_8));
            String line = "";
            StringBuilder GPTsay = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                line = line.replace("data: ", "").trim();
                if(!line.equals("[DONE]")) {
                    Data data = gson.fromJson(line, Data.class);
                    if(null != data) {
                        GPTMessageHandleList.forEach(item -> {
                            item.handleMessage(data);
                        });

                        // 记录GPT的回答，然后保存起来
                        for(Choices choices : data.getChoices()) {
                            if(null == choices.getDelta().getContent()) {
                                break;
                            }
                            GPTsay.append(choices.getDelta().getContent());
                        }

                    }
                }
            }

            this.GPTSetting.getMessages().add(new Message("system", GPTsay.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GPTSetting getGPTSetting() {
        return GPTSetting;
    }


}
