package net.kaaass.bilidanmaku.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommentServer {
		
	public CommentServer() {
	}
	
	public void start() {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		try (ServerSocket server = new ServerSocket(80)) {
			System.out.println("Open danmaku server at port 80.");
			
			while(true) {
				Socket connecion = server.accept();
				pool.submit(new RequestHandler(connecion));
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
