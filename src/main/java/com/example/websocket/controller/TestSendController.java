package com.example.websocket.controller;

import com.example.websocket.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.websocket.server.WebSocketServer;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class TestSendController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/sendMessage")
    public String listUploadedFiles(Model model) {

        return "sendMessage";
    }

    @RequestMapping("/sendMessage/{token}/{message}")
    public String sendMessage(@PathVariable String token, @PathVariable String message) throws Exception {
        String result = WebSocketServer.sendMessageToUser(token, message);
        return result;
    }

    /**
     * From 接收表单数据
     *
     * @param token
     * @param message
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendMessage2", method = RequestMethod.POST, produces = "application/json")
    public String sendMessage2(@RequestParam("token") String token, @RequestParam("message") String message) throws Exception {
        log.info("token: " + token + ", message: " + message);
        String result = WebSocketServer.sendMessageToUser(token, message);
        return result;
    }

    /**
     * Form map接收表单参数
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("/sendMessage3")
    public String sendMessage3(@RequestParam Map<String, Object> map) throws Exception {
        String token = map.get("token").toString();
        String message = map.get("message").toString();
        log.info("token: " + token + ", message: " + message);
        String result = WebSocketServer.sendMessageToUser(token, message);
        return result;
    }


    /**
     * Form bean接收表单参数
     * @param message
     * @return
     * @throws Exception
     */
    @PostMapping("/sendMessage4")
    public String sendMessage4(Message message) throws Exception {
        String token = message.getToken();
        String msg = message.getMsg();
        log.info("token: " + token + ", message: " + message);
        String result = WebSocketServer.sendMessageToUser(token, msg);
        return result;
    }


    /**
     * Json bean接收表单参数
     * @param message
     * @return
     * @throws Exception
     */
    @PostMapping("/sendMessage5")
    public String sendMessage5(@RequestBody Message message) throws Exception {
        String token = message.getToken();
        String msg = message.getMsg();
        log.info("token: " + token + ", message: " + message);
        String result = WebSocketServer.sendMessageToUser(token, msg);
        return result;
    }


}