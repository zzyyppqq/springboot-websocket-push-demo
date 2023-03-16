package com.example.websocket.controller;

import com.example.websocket.model.Message;
import com.example.websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
public class PushController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String sendMessage(Model model) {
        model.addAttribute("message", new Message());
        return "sendMessage";
    }

    @RequestMapping("/sendPushMessage")
    public String sendPushMessage(@ModelAttribute Message message) throws IOException {
        String token = message.getToken();
        String msg = message.getMsg();
        log.info("token: " + token + ", msg: " + msg);
        String result = WebSocketServer.sendMessageToUser(token, msg);
        return "sendMessage";
    }

}