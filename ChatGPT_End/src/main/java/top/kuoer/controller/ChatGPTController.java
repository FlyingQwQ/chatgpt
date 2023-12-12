package top.kuoer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.kuoer.common.Result;
import top.kuoer.service.ChatGPTService;

@RestController
@RequestMapping("/chatgpt")
public class ChatGPTController {

    private ChatGPTService chatGPTService;

    @Autowired
    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @RequestMapping(value="/connect", method={RequestMethod.GET}, produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam("id") String id) {
        return this.chatGPTService.connect(id);
    }

    @RequestMapping(value="/close", method={RequestMethod.GET})
    public Result close(@RequestParam("id") String id) {
        return this.chatGPTService.close(id);
    }

    @RequestMapping(value="/remove", method={RequestMethod.GET})
    public Result remove(@RequestParam("id") String id) {
        return this.chatGPTService.remove(id);
    }

    @RequestMapping(value="/sendmsg", method={RequestMethod.GET})
    public Result sendMsg(@RequestParam("id") String id, @RequestParam("content") String content) {
        return this.chatGPTService.sendMsg(id, content);
    }

    @RequestMapping(value="/msgitems", method={RequestMethod.GET})
    public Result getMessageItems(@RequestParam("id") String id) {
        return this.chatGPTService.getMessageItems(id);
    }

}
