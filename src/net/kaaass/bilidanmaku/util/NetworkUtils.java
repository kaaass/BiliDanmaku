package net.kaaass.bilidanmaku.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class NetworkUtils {
	public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.99 Safari/537.36";
	public static final String COOKIE = "DedeUserID=8926815; DedeUserID__ckMd5=7a15e38c8988dd51; SESSDATA=f3723f8c%2C1445522963%2Ce07d220f;";

	/**
	 * Get json data from remote server.
	 * 
	 * @author SuperFashi(Python), KAAAsS(translated into Java)
	 * @param urlPath
	 *            The address of server.
	 * @return Json data in string.
	 * @throws Exception
	 */
	public static String getJsonString(String url) {
		String ip = String.valueOf((new Random()).nextInt(254) + 1);
		if ((new Random()).nextInt(1) == 1) {
			ip = "220.181.111." + ip;
		} else {
			ip = "59.152.193." + ip;
		}
		Map<String, String> header = new HashMap<String, String>();
		Proxy proxy = null;
		header.put("Accept-Encoding", "gzip, deflate");
		header.put("User-Agent", USER_AGENT);
		header.put("Client-IP", ip);
		header.put("X-Forwarded-For", ip);
		header.put("Cookie", COOKIE);
		if (url.indexOf("comment.bilibili.com") != -1) {
			header.put("Host", "comment.bilibili.com");
			url.replace("comment.bilibili.com", "127.0.0.1");
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("112.16.88.131", 8003));
		}
		try {
			return getFromUrl(header, new URL(url), proxy);
		} catch (MalformedURLException e) {
			System.err.println(e);
			return null;
		}
	}
	
	private static String getFromUrl(Map<String, String> headerMap, URL loc, Proxy proxy) {
		String encode = "UTF-8";
		String result = "";
		try {
			HttpURLConnection urlCon;
			if (proxy == null) {
				urlCon = (HttpURLConnection)loc.openConnection();
			} else {
				urlCon = (HttpURLConnection)loc.openConnection(proxy);
			}
			Iterator<String> header = headerMap.keySet().iterator();
			while (header.hasNext()) {
				String key = (String) header.next();
				urlCon.addRequestProperty(key, headerMap.get(key));
			}
			String cType = urlCon.getHeaderField("Content-Type");
			int i = cType.indexOf("charset=");
			if (i != -1)
				encode = cType.substring(i + 8);
			InputStream input;
			cType = urlCon.getContentEncoding();			
			input = new BufferedInputStream(urlCon.getInputStream());
			if (cType != null) {
				if (cType.indexOf("gzip") != -1) {
					input = new GZIPInputStream(input);
				} else if (cType.indexOf("deflate") != -1) {
					input = new InflaterInputStream(input, new Inflater(true));
				}
			}
			Reader reader = new InputStreamReader(input, encode);
			while ((i = reader.read()) != -1)
				result += (char) i;
			reader.close();
		} catch (IOException e) {
			System.err.println(e);
		}
		return result;
	}

	/**
	 * A method to get the sign to bilibili's api. Copy from(Python):
	 * www.fuckbilibili.com/biliapi.html#bapi-1. Translated by: KAAAsS
	 * 
	 * @param params
	 * @param appKey
	 * @param appSecret
	 * @return
	 */
	public static String getSign(Map<String, String> params, String appKey,
			String appSecret) {
		String data = "";
		params.put("appkey", appKey);
		for (String key : params.keySet()) {
			if (data != "")
				data += "&";
			data += key + "=" + params.get(key);
		}
		if (appSecret == null)
			return data;
		return data + "&sign=" + StringUtils.MD5(data + appSecret);
	}
}
