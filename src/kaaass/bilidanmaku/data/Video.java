package kaaass.bilidanmaku.data;

import java.util.HashMap;
import java.util.Map;

import kaaass.bilidanmaku.util.CodeUtils;
import kaaass.bilidanmaku.util.NetworkUtils;

import com.google.gson.Gson;

public class Video {
	int aid;
	int page;
	String cid;

	private Video(String av, int page) {
		this.aid = Integer.valueOf(av.substring(2));
		this.page = page;
		this.remote();
		System.out.println(this.aid);
		System.out.println(this.page);
	}

	public static Video fromAID(String av) {
		return new Video(av, 1);
	}

	public static Video fromAID(String av, int page) {
		return new Video(av, page);
	}

	public static Video fromURL(String url) {
		int i = url.indexOf("index_");
		if (i > 0) {
			i = Integer.valueOf(url.substring(i + 6, url.indexOf(".html")));
		} else {
			i = 1;
		}
		if (url.startsWith("http://")) {
			return new Video(url.split("/")[4], i);
		} else {
			return new Video(url.split("/")[2], i);
		}
	}

	/**
	 * Get video data from server.
	 */
	public void remote() {
		try {
			Gson gson = new Gson();
			Map<String, String> p = new HashMap<String, String>();
			p.put("id", String.valueOf(this.aid));
			p.put("page", String.valueOf(this.page));
			p.put("type", "json");
			String data = CodeUtils.getSign(p, CodeUtils.APPKEY,
					CodeUtils.APP_SECRET);
			System.out.println(data);
			data = NetworkUtils.getJsonString("http://api.bilibili.com/view?"
					+ data);
			Video u = gson.fromJson(data, Video.class);
			this.cid = u.cid;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return this.cid;
	}
}
