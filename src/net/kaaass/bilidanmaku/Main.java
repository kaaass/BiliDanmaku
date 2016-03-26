package net.kaaass.bilidanmaku;

import net.kaaass.bilidanmaku.data.Video;
import net.kaaass.bilidanmaku.util.FileUtils;

public class Main {

	private static final boolean DEBUG = false;

	public static void main(String[] args) {
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
		Video.fromURL(args[0]).getDanmaku();
		if (DEBUG)
			System.out.println("Time cost: "
					+ (float) (System.currentTimeMillis() - time) / 1000F
					+ "s.");
		FileUtils.saveCache();
		System.exit(0);
	}
}
