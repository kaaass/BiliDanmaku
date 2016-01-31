package kaaass.bilidanmaku.util;

import java.security.MessageDigest;
import java.util.Map;

public class CodeUtils {
	//APP Key from fuckbilibili.com
	public final static String APPKEY = "85eb6835b0a1034e";
	public final static String APP_SECRET = "2ad42749773c441109bdc0191257a664";
	
	public static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// Get MessageDigest object
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			// Get encoded bytes
			byte[] md = mdInst.digest();
			// Convert bytes to string (HEX)
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
		return data + "&sign=" + CodeUtils.MD5(data + appSecret);
	}
}
