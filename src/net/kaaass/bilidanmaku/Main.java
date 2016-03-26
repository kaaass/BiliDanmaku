package net.kaaass.bilidanmaku;

import net.kaaass.bilidanmaku.data.Video;
import net.kaaass.bilidanmaku.util.FileUtils;

public class Main {

	public static void main(String[] args) {
		if (args == null) {
			System.out.println("Invalid params.");
			return;
		}
		if (args.length != 1) {
			System.out.println("Invalid params.");
			return;
		}
		Video.fromURL(args[0]).getDanmaku();
		FileUtils.saveCache();
		System.exit(0);
	}
}
