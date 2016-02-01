package kaaass.bilidanmaku;

import kaaass.bilidanmaku.data.User;
import kaaass.bilidanmaku.data.Video;

public class Main {

	public static void main(String[] args) {
		//test();
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
	}

	public static void test() {
		System.out.println(User.fromMID("543732")); // The maker ^_^
		System.out.println(User.fromUID("1ad5be0d")); // The owner of Bilibili

		// A test video
		System.out.println(Video.fromURL(
				"http://www.bilibili.com/video/av635857/").getDanmaku());
	}

}
