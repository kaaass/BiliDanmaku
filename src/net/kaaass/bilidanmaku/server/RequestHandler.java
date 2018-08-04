package net.kaaass.bilidanmaku.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;

import net.kaaass.bilidanmaku.data.Video;
import net.kaaass.bilidanmaku.util.NetworkUtils;

public class RequestHandler implements Callable<Void> {

	private Socket conn;

	RequestHandler(Socket conn) {
		this.conn = conn;
	}

	@Override
	public Void call() throws Exception {
		try (OutputStream out = new BufferedOutputStream(
				this.conn.getOutputStream());
				InputStream in = new BufferedInputStream(
						this.conn.getInputStream());) {
			String request = "";
			while (true) {
				int c = in.read();
				if (c == '\r' || c == '\n' || c == -1)
					break;
				request += (char) c;
			}
			String content = handleFile(request);			
			byte[] cont = content.getBytes(Charset.forName("UTF-8"));
			Deflater compresser = new Deflater(1, true);
			compresser.setInput(cont);
			compresser.finish();
			int length = compresser.deflate(cont);
			if (request.indexOf("HTTP/") != -1) {
				String header = "HTTP/1.1 200 OK\r\n"
						+ "Date: "
						+ (new Date()).toString()
						+ "\r\n"
						+ "Content-Type: text/xml;charset=UTF-8\r\n"
						+ "Content-Length: "
						+ length
						+ "\r\n"
						+ "Access-Control-Allow-Origin: http://www.bilibili.com\r\n"
						+ "Connection: close\r\n" + "Server: BiliDanmaku\r\n"
						+ "Content-Encoding: deflate\r\n\r\n";
				out.write(header.getBytes(Charset.forName("US-ASCII")));
			}
			out.write(cont);
			out.flush();
		} finally {
			conn.close();
		}
		return null;
	}

	private String handleFile(String request) {
		String url = "http://comment.bilibili.com" + request.split(" ")[1];
		
		System.out.println(url);
		
		if (!url.endsWith(".xml") || url.endsWith("crossdomain.xml")) {
			System.out.println("Request handled: " + url);
			return NetworkUtils.getJsonString(url);
		} else {
			// 正则匹配弹幕文件
			Pattern pattern = Pattern.compile("rc\\/([0-9]+)\\.xml");
			Matcher matcher = pattern.matcher(url);
			if (!matcher.find())
				return NetworkUtils.getJsonString(url);

			return Video.fromCID(Integer.valueOf(matcher.group(1))).getDanmaku(
					true);
		}
	}
}
