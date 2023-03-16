package com.example.websocket.server;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyClientApp {

	public static void main(String[] args){
        Timer timer = new Timer();  
        timer.schedule(new MyTask(), 1000, 2000);  
	}
	static int num = 0;
	static class MyTask extends TimerTask {  
		  
	    @Override  
	    public void run() {  
	    	MyClient client = new MyClient();
			String uri = "ws://localhost:8088/ws/push/123";
			client.start(uri);
			try {
				num++;
				client.sendMessage("消息测试"+num);
				client.closeSocket();
				System.out.println(num);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	    }
	} 
}
