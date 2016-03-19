package net.kaaass.bilidanmaku.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

	public static Map<String, Integer> cache;

	public static boolean saveXML(String xml) {
		File f = new File(StringUtils.getCid(xml) + ".xml");
		if (f.exists())
			f.delete();
		try (OutputStreamWriter out = new OutputStreamWriter(
				new FileOutputStream(f), "UTF-8")) {
			if (f.createNewFile()) {
				out.write(xml);
				out.flush();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void initCache() {
		if (cache == null)
			cache = new HashMap<String, Integer>();
		// Get cache from file
	}

	public static boolean writeCache(String key, int value) {
		cache.put(key, value);
		// Write cache
		return true;
	}

	public static int readCache(String key) {
		if (cache == null)
			initCache();
		if (cache.containsKey(key)) {
			return cache.get(key);
		} else {
			return -1;
		}
	}
}
