package net.kaaass.bilidanmaku.data;

import com.google.gson.Gson;

import net.kaaass.bilidanmaku.util.FileUtils;
import net.kaaass.bilidanmaku.util.NetworkUtils;

public class User {
	public static final int UID = 0;
	public static final int MID = 1;
	public static final int SPACE = 2;

	private String mid;
	private String name;
	private Object level_info;
	private int current_level;
	private boolean anonymous = false;

	private User(String input, int type) {
		switch (type) {
		case UID:
			try {
				int p = FileUtils.readCache("uid" + input);
				if (p == -1) {
					String data = NetworkUtils
							.getJsonString("http://biliquery.typcn.com/api/user/hash/"
									+ input);
					p = data.indexOf("\"id\":");
					if (p > 0) {
						this.mid = data.substring(p + 5, data.indexOf("}]}"));
						FileUtils.writeCache("uid" + input,
								Integer.valueOf(this.mid));
					} else { // Don't write anonymous in cache.
						this.anonymous = true;
					}
				} else {
					this.mid = String.valueOf(p);
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.mid = null;
			}
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
	}

	public static User fromUID(String uid) {
		return new User(uid, UID);
	}

	public static User fromMID(String mid) {
		return new User(mid, MID);
	}

	public static User fromSpaceUrl(String url) {
		if (url.indexOf("space.bilibili.com/") > 0) {
			return new User(url, SPACE);
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
				Gson gson = new Gson();
				String data = NetworkUtils
						.getJsonString("http://api.bilibili.com/userinfo?mid="
								+ this.mid);
				User u = gson.fromJson(data, User.class);
				this.name = u.name;
				u = gson.fromJson(u.level_info.toString(), User.class);
				this.current_level = u.current_level;
			} catch (Exception e) {
				e.printStackTrace();
				this.name = null;
				this.current_level = -1;
				this.level_info = null;
			}
		}
	}

	@Override
	public String toString() {
		if (this.anonymous)
			return "ÓÎ¿Íµ¯Ä»";
		if (this.mid == null)
			return null;
		return this.name + " lv." + this.current_level;
	}
}
