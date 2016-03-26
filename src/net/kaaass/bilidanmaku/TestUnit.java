package net.kaaass.bilidanmaku;

import static org.junit.Assert.*;
import net.kaaass.bilidanmaku.data.User;
import net.kaaass.bilidanmaku.data.Video;
import net.kaaass.bilidanmaku.util.FileUtils;

import org.junit.Test;

public class TestUnit {
	private final String TEST_MID = "543732";
	private final String TEST_MID_INFO = "SCkkk lv.4"; // The maker ^_^
	private final String TEST_UID = "1ad5be0d";
	private final String TEST_UID_INFO = "±ÌÊ« lv.5"; // The owner of
													// Bilibili.com.
	private final String TEST_VIDEO_URL = "www.bilibili.com/video/av635066/";

	User u = User.getInstance();

	@Test
	public void testUserInfoFetch() {
		String user = User.fromMID(TEST_MID, u).toString();
		if (!user.equals(TEST_MID_INFO))
			fail("Error catching! It returns: " + user + ".");
	}

	@Test
	public void testUIDFetch() {
		String user = User.fromUID(TEST_UID, u).toString();
		if (!user.equals(TEST_UID_INFO))
			fail("Error catching! It returns: " + user + ".");
	}

	@Test
	public void testCIDFetch() {
		Video.fromURL(TEST_VIDEO_URL);
	}

	@Test
	public void testDanmakuParse() {
		Video.fromURL(TEST_VIDEO_URL).getDanmaku(true);
	}

	@Test
	public void testCacheSave() {
		FileUtils.saveCache();
	}
}
