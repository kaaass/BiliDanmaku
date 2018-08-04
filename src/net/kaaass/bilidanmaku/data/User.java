package net.kaaass.bilidanmaku.data;

import java.util.HashMap;
import java.util.Map;

import net.kaaass.bilidanmaku.util.Crc32;
import net.kaaass.bilidanmaku.util.NetworkUtils;

import org.json.JSONObject;

public class User {
	public static final int UID = 0;
	public static final int MID = 1;
	public static final int SPACE = 2;

	private String mid;
	private String name;
	private int current_level;
	private boolean anonymous = false;

	private User() {
	}

	private User(String input, int type) {
		this.init(input, type);
	}

	private User init(String input, int type) {
		switch (type) {
		case UID:
			this.mid = Crc32.deCrc32(input);
			/*
			 * //旧版本FB服务器查询 try { int p = FileUtils.readCache("uid" + input); if
			 * (p == -1) { String data = NetworkUtils
			 * .getJsonString("http://biliquery.typcn.com/api/user/hash/" +
			 * input); p = data.indexOf("\"id\":"); if (p > 0) { this.mid =
			 * data.substring(p + 5, data.indexOf("}]}"));
			 * FileUtils.writeCache("uid" + input, Integer.valueOf(this.mid)); }
			 * else { // Don't write anonymous in cache. this.anonymous = true;
			 * } } else { this.mid = String.valueOf(p); } } catch (Exception e)
			 * { e.printStackTrace(); this.mid = null; }
			 */
			break;
		case MID:
			this.mid = input;
			break;
		case SPACE:
			this.mid = input.substring(
					input.indexOf("space.bilibili.com/") + 19, input.length());
			break;
		}
		this.remote();
		return this;
	}

	public static User getInstance() {
		return new User();
	}

	public static User fromUID(String uid, User user) {
		return user.init(uid, UID);
	}

	public static User fromMID(String mid, User user) {
		return user.init(mid, MID);
	}

	public static User fromSpaceUrl(String url, User user) {
		if (url.indexOf("space.bilibili.com/") > 0) {
			return user.init(url, SPACE);
		} else {
			return null;
		}
	}

	/**
	 * Get user data from server.
	 */
	public void remote() {
		if (!this.anonymous) {
			try {
				String data = fetchUsrInfo(this.mid);
				JSONObject json = new JSONObject(data).getJSONObject("data")
						.getJSONObject("card");
				this.name = json.getString("name");
				this.current_level = json.getJSONObject("level_info").getInt(
						"current_level");
			} catch (Exception e) {
				e.printStackTrace();
				this.name = null;
				this.current_level = -1;
			}
		}
	}

	@Override
	public String toString() {
		if (this.anonymous)
			return "游客弹幕";
		if (this.mid == null)
			return null;
		return this.name + " lv." + this.current_level;
	}

	private final static String AK = "1d8b6e7d45233436";
	private final static String SK = "560c52ccd288fed045859ed18bffd973";

	private static String fetchUsrInfo(String mid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("vmid", mid);
		params.put("actionKey", "appkey");
		params.put("build", "414000");
		params.put("platform", "android");
		params.put("mobi_app", "android");
		params.put("ts", "233333333");
		String p = NetworkUtils.getSign(params, AK, SK);
		return NetworkUtils.getJsonString("http://app.bilibili.com/x/v2/space?"
				+ p);
	}
}
