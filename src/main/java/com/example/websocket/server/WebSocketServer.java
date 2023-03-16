package com.example.websocket.server;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/ws/push/{token}")
@Component
public class WebSocketServer {

	static Logger logger =LoggerFactory.getLogger(WebSocketServer.class);

    private Session session;
    
    private String token;

    private static ConcurrentHashMap<String,String> tokenMap = new ConcurrentHashMap<String,String>();
    private static ConcurrentHashMap<String,WebSocketServer> webSocketSet = new ConcurrentHashMap<String,WebSocketServer>();

    
    @OnOpen
    public void onOpen(Session session,@PathParam(value = "token")String token) {
        this.session = session;
        tokenMap.put(token, session.getId());
        webSocketSet.put(session.getId(),this);     
        this.token=token;
        logger.info("{} connected",token);
    }
 
    
	@OnClose
    public void onClose() {
		tokenMap.remove(this.getToken());
        webSocketSet.remove(this.session.getId()); 
        logger.info("client : {} onClose ",this.getToken());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
    	logger.error("receive message ==>> {}",message);
    	
        //广播消息
        for (Map.Entry<String, WebSocketServer> item : webSocketSet.entrySet()) {
            try {
                item.getValue().sendMessage(message);
            } catch (IOException e) {
            	logger.error("onMessage Error ",e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
    	logger.error("onError  ",error);
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    
    public static String sendMessageToUser(String token,String message) throws IOException {
    	String sendResult="send OK";
    	String sessionId=tokenMap.get(token);
    	if(sessionId==null) {
    		sendResult="token ["+token+"] is not exists ";
    		logger.error(sendResult);
    		return sendResult;
    	}
    	WebSocketServer websocket=webSocketSet.get(sessionId);
    	if(websocket==null) {
    		sendResult=token+" => sessionid ["+sessionId+"] is not exists ";
    		logger.error(sendResult);
    		return sendResult;
    	}
    	websocket.session.getBasicRemote().sendText(message);
    	return sendResult;
    }

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
}
