package net.kaaass.bilidanmaku.util;

import java.security.MessageDigest;
import java.util.zip.CRC32;

import net.kaaass.bilidanmaku.data.User;

public class StringUtils {
	// APP Key from fuckbilibili.com
	public final static String APPKEY = "85eb6835b0a1034e";
	public final static String APP_SECRET = "2ad42749773c441109bdc0191257a664";

	public static String md5(String s) {
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

	public static String getCrc32b(String in) {
		CRC32 crc32 = new CRC32();
		crc32.update(in.getBytes());
		return Long.toHexString(crc32.getValue());
	}

	public static String getCid(String comment) {
		return comment.substring(comment.indexOf("<chatid>") + 8,
				comment.indexOf("</chatid>"));
	}

	public static int getPoolByComment(String comment) {
		return Integer.valueOf(comment.split(",", 8)[5]);
	}

	public static User getUserByComment(String comment) {
		return User.fromUID(comment.split(",", 8)[6], User.getInstance());
	}

	public static User getUserByComment(String comment, User user) {
		return User.fromUID(comment.split(",", 8)[6], user);
	}

	public static String commentDeAnonymous(String comment) {
		if (getPoolByComment(comment) == 0) {
			return comment.substring(0, comment.indexOf("</d>")) + " ("
					+ getUserByComment(comment) + ")</d>";
		} else {
			return comment;
		}
	}

	public static String commentDeAnonymous(String comment, User user) {
		if (getPoolByComment(comment) == 0) {
			return comment.substring(0, comment.indexOf("</d>")) + " ("
					+ getUserByComment(comment, user) + ")</d>";
		} else {
			return comment;
		}
	}

	public static String addXMLHeader(String comment, String origin) {
		return origin.substring(0, origin.indexOf("<d p=\"")) + comment
				+ "</i>";
	}

	public static String deleteXMLHeader(String comment) {
		return comment.substring(comment.indexOf("<d p=\""),
				comment.length() - 6);
	}

	public static boolean isEmpty(String hostName) {
		return hostName == null ? true : hostName.equals("");
	}
}
