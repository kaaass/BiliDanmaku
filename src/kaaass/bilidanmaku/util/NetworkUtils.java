package kaaass.bilidanmaku.util;

import java.util.Map;
import java.util.Random;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class NetworkUtils {
	public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.99 Safari/537.36";
	private static CloseableHttpClient httpclient = HttpClients.createDefault();

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
		CloseableHttpResponse response = null;
		HttpGet get = new HttpGet(url);
		get.setHeader("Accept-Encoding", "gzip, deflate");
		get.setHeader("User-Agent", USER_AGENT);
		get.setHeader("Client-IP", ip);
		get.setHeader("X-Forwarded-For", ip);
		get.setHeader(
				"Cookie",
				"DedeUserID=8926815; DedeUserID__ckMd5=7a15e38c8988dd51; SESSDATA=f3723f8c%2C1445522963%2Ce07d220f;");
		response = execute(get);
		try {
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static CloseableHttpResponse execute(HttpGet httpGet) {
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				return response;
			}
			response.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
