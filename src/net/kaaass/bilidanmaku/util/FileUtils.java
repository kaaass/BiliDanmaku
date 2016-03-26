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
		try (FileOutputStream fo = new FileOutputStream(f);
				OutputStreamWriter out = new OutputStreamWriter(fo, "UTF-8")) {
			f.createNewFile();
			out.write(xml);
			out.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void initCache() {
		if (cache == null)
			cache = new HashMap<String, Integer>();
		// Get cache from file
	}

	public static boolean saveCache() {
		try {
			// Write cache to file
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	public static boolean writeCache(String key, int value) {
		synchronized (cache) {
			cache.put(key, value);
		}
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
