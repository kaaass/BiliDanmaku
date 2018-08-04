package net.kaaass.bilidanmaku;

import java.util.Scanner;

import net.kaaass.bilidanmaku.data.User;
import net.kaaass.bilidanmaku.server.CommentServer;
import net.kaaass.bilidanmaku.util.FileUtils;
import net.kaaass.bilidanmaku.util.NetworkUtils;

public class Main {

	// private static final boolean DEBUG = true;

	public static void main(String[] args) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
				
		try {
			// FileUtils.updateHostName("comment.bilibili.com", "127.0.0.1");
		} catch (Exception e) {
			System.exit(0);
		}
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				CommentServer server = new CommentServer();
				server.start();
			}
		});
		t.start();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String line;
		while(true) {
			line = scanner.nextLine();
			if (line.equals("exit"))
				break;
		}
		try {
			FileUtils.updateHostName("comment.bilibili.com", " ");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		/*
		if (args == null) {
			System.out.println("Invalid params.");
			return;
		}
		if (args.length != 1) {
			System.out.println("Invalid params.");
			return;
		}
		long time;
		if (DEBUG)
			time = System.currentTimeMillis();
		Video.fromURL(args[0]).getDanmaku(true);
		if (DEBUG)
			System.out.println("Time cost: "
					+ (float) (System.currentTimeMillis() - time) / 1000F
					+ "s.");
		System.exit(0);
		*/
	}
}
