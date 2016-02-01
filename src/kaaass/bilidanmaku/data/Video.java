package kaaass.bilidanmaku.data;

import java.util.HashMap;
import java.util.Map;

import kaaass.bilidanmaku.util.FileUtils;
import kaaass.bilidanmaku.util.StringUtils;
import kaaass.bilidanmaku.util.NetworkUtils;

import com.google.gson.Gson;

public class Video {
	private int aid;
	private int page;
	private String cid;

	private Video(String av, int page) {
		this.aid = Integer.valueOf(av.substring(2));
		this.page = page;
		this.remote();
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
			String data = NetworkUtils.getSign(p, StringUtils.APPKEY,
					StringUtils.APP_SECRET);
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

	/**
	 * Get danmaku from server and process de-anonymous.
	 * 
	 * @return danmaku data
	 */
	public String getDanmaku() {
		String origin = NetworkUtils
				.getJsonString("http://comment.bilibili.com/" + this.cid
						+ ".xml");
		String data = "";
		for (String c : StringUtils.deleteXMLHeader(origin).split("\n"))
			data += StringUtils.commentDeAnonymous(c) + "\n";
		data = StringUtils.addXMLHeader(data, origin);
		FileUtils.saveXML(data);
		return data;
	}
}
