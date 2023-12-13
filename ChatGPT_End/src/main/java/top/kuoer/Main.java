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
    }
}