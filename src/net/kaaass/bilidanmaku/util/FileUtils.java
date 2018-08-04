package net.kaaass.bilidanmaku.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

	public synchronized static boolean updateHostName(String hostName, String ip)
			throws Exception {
		if (StringUtils.isEmpty(hostName) || StringUtils.isEmpty(ip))
			throw new IllegalArgumentException("HostName or Ip can't be null.");
		if (StringUtils.isEmpty(hostName.trim()))
			throw new IllegalArgumentException("HostName or Ip can't be null.");
		String splitter = " ";
		String fileName = null;

		if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
			fileName = "/etc/hosts";
		} else {
			fileName = System.getenv("windir") + "/system32/drivers/etc/hosts";
		}

		List<?> lines = FileUtils.readLines(new File(fileName));
		List<String> newLines = new ArrayList<String>();
		boolean findFlag = false;
		boolean updateFlag = false;
		for (Object line : lines) {
			String strLine = (String) line;
			if (!StringUtils.isEmpty(strLine) && !strLine.startsWith("#")) {
				strLine = strLine.replaceAll("/t", splitter);
				int index = strLine.toLowerCase().indexOf(
						hostName.toLowerCase());
				if (index != -1) {
					String[] array = strLine.trim().split(splitter);
					for (String name : array) {
						if (hostName.equalsIgnoreCase(name)) {
							findFlag = true;
							if (array[0].equals(ip)) {
								newLines.add(strLine);
								break;
							}
							StringBuilder sb = new StringBuilder();
							sb.append(ip);
							for (int i = 1; i < array.length; i++)
								sb.append(splitter).append(array[i]);
							newLines.add(sb.toString());
							updateFlag = true;
							break;
						}
					}

					if (findFlag) {
						break;
					}
				}
			}
			newLines.add(strLine);
		}
		if (!findFlag) {
			newLines.add(new StringBuilder(ip).append(splitter)
					.append(hostName).toString());
		}

		if (updateFlag || !findFlag) {
			FileUtils.writeLines(new File(fileName), newLines);

			String formatIp = formatIpv6IP(ip);
			for (int i = 0; i < 20; i++) {
				try {
					boolean breakFlg = false;
					InetAddress[] addressArr = InetAddress
							.getAllByName(hostName);

					for (InetAddress address : addressArr) {
						if (formatIp.equals(address.getHostAddress())) {
							breakFlg = true;
							break;
						}
					}

					if (breakFlg) {
						break;
					}
				} catch (Exception e) {
					System.err.println(e);
				}
				Thread.sleep(3000);
			}
		}
		return updateFlag;
	}

	private static void writeLines(File file, List<String> newLines) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));) {
			for (String line : newLines) {
				writer.newLine();
				writer.write(line);
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<?> readLines(File file) {
		String encoding = "GBK";
		List<String> out = null;
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);) {
			if (file.isFile() && file.exists()) {
				out = new ArrayList<String>();
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null)
					out.add(lineTxt);
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	private static String formatIpv6IP(String ipV6Addr) {
		String strRet = ipV6Addr;
		StringBuffer replaceStr;
		int iCount = 0;
		char ch = ':';

		if (StringUtils.isEmpty(ipV6Addr) && ipV6Addr.indexOf("::") > -1) {
			for (int i = 0; i < ipV6Addr.length(); i++) {
				if (ch == ipV6Addr.charAt(i))
					iCount++;
			}

			if (ipV6Addr.startsWith("::")) {
				replaceStr = new StringBuffer("0:0:");
				for (int i = iCount; i < 7; i++) {
					replaceStr.append("0:");
				}
			} else if (ipV6Addr.endsWith("::")) {
				replaceStr = new StringBuffer(":0:0");
				for (int i = iCount; i < 7; i++) {
					replaceStr.append(":0");
				}
			} else {
				replaceStr = new StringBuffer(":0:");
				for (int i = iCount; i < 7; i++) {
					replaceStr.append("0:");
				}
			}
			strRet = ipV6Addr.trim().replaceAll("::", replaceStr.toString());
		}
		return strRet;
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
