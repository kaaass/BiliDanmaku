package kaaass.bilidanmaku.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FileUtils {
	public static boolean saveXML(String xml) {
		OutputStreamWriter out;
		File f = new File(StringUtils.getCid(xml) + ".xml");
		try {
			if (f.exists())
				f.delete();
			if (f.createNewFile()) {
				out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
				out.write(xml);
				out.flush();
				out.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
