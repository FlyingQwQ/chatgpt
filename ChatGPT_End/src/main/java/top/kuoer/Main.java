package top.kuoer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kuoer.parameter.GPTSetting;
import top.kuoer.parameter.Role;
import top.kuoer.result.Choices;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

//        GPTSetting setting = new GPTSetting();
//        setting.setApiUrl("https://api.openai-proxy.org/v1/chat/completions");
//        setting.setKey("sk-NJPRpBK7bgjbhy9Lk9yFAMtGDSa95RKGr3Vs123dYkWYiD9t");
//        setting.setModel("gpt-3.5-turbo");
//        setting.setMax_tokens(50);
//        setting.setTemperature(0.9);
//        setting.setStream(true);
//
//        GPT testGPT = new GPT(setting);
//
//        testGPT.addGPTMessageHandle(data -> {
//            for(Choices choices : data.getChoices()) {
//                if(null == choices.getDelta().getContent()) {
//                    System.out.println();
//                    break;
//                }
//                System.out.print(choices.getDelta().getContent());
//            }
//        });
//
//        testGPT.sendMessage(Role.USER, "早上好");
//        testGPT.sendMessage(Role.USER, "我刚刚说了什么");


    }
}