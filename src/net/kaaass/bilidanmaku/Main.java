package net.kaaass.bilidanmaku;

import net.kaaass.bilidanmaku.server.CommentServer;

public class Main {

	// private static final boolean DEBUG = true;

	public static void main(String[] args) {
		CommentServer server = new CommentServer();
		server.start();
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
