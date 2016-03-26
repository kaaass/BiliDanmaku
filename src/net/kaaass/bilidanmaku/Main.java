package net.kaaass.bilidanmaku;

import net.kaaass.bilidanmaku.data.User;
import net.kaaass.bilidanmaku.data.Video;
import net.kaaass.bilidanmaku.util.FileUtils;

public class Main {

	public static void main(String[] args) {
		FileUtils.initCache();

		test();

		if (args == null) {
			System.out.println("Invalid params.");
			return;
		}
		if (args.length != 1) {
			System.out.println("Invalid params.");
			return;
		}
		System.out.println("Catching data from server, please wait...");
		Video.fromURL(args[0]).getDanmaku();
		System.out.println("Complete.");
		FileUtils.saveCache();
		System.exit(0);
	}

	public static void test() {
		long time = System.currentTimeMillis();
		System.out.println(User.fromMID("543732")); // The maker ^_^
		System.out.println("Time cost: "
				+ (float) (System.currentTimeMillis() - time) / 1000F + " s.");
		time = System.currentTimeMillis();
		System.out.println(User.fromUID("1ad5be0d")); // The owner of Bilibili
		System.out.println("Time cost: "
				+ (float) (System.currentTimeMillis() - time) / 1000F + " s.");

		time = System.currentTimeMillis();
		// A test video
		Video.fromURL("http://www.bilibili.com/video/av2608412/").getDanmaku();
		System.out.println("Time cost: "
				+ (float) (System.currentTimeMillis() - time) / 1000F + " s.");

		System.exit(0);
	}

}
