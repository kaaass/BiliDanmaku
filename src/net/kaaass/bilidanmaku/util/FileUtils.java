package net.kaaass.bilidanmaku.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileUtils {

	private static Map<String, Integer> cache;

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

	private static void initCache() {
		if (cache == null)
			cache = new HashMap<String, Integer>();
		File f = new File("cache.map");
		if (!f.exists())
			return;
		String data = "";
		try (FileInputStream fi = new FileInputStream(f);
				InputStreamReader in = new InputStreamReader(fi, "UTF-8")) {
			int unicode;
			while ((unicode = in.read()) != -1)
				data += (char) unicode;
			for (String line : data.split("\n")) {
				String[] split = line.split(",");
				if (split == null)
					continue;
				cache.put(split[0], Integer.valueOf(split[1]));
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static boolean saveCache() {
		String data = "";
		Iterator<String> keyIterator = cache.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			int value = cache.get(key);
			data += key + "," + value + "\n";
		}
		File f = new File("cache.map");
		if (f.exists())
			f.delete();
		try (FileOutputStream fo = new FileOutputStream(f);
				OutputStreamWriter out = new OutputStreamWriter(fo, "UTF-8")) {
			out.write(data);
			out.flush();
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
