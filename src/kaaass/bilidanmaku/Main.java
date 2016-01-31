package kaaass.bilidanmaku;

import kaaass.bilidanmaku.data.User;
import kaaass.bilidanmaku.data.Video;

public class Main {

	public static void main(String[] args) {
		System.out.println(User.fromMID("543732")); //The maker ^_^
		System.out.println(User.fromUID("1ad5be0d")); //The owner of Bilibili
		
		System.out.println(Video.fromURL("http://www.bilibili.com/video/av12450/")); //A good video ^_^
	}

}
