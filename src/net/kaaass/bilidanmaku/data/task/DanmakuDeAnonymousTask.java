package net.kaaass.bilidanmaku.data.task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import net.kaaass.bilidanmaku.data.User;
import net.kaaass.bilidanmaku.util.StringUtils;

public class DanmakuDeAnonymousTask implements Callable<String> {

	private List<String> danmaku;
	private User user;

	public DanmakuDeAnonymousTask(String[] source, int offset, int length) {
		this.danmaku = Arrays.asList(source).subList(offset, offset + length);
		this.user = User.getInstance();
	}

	public DanmakuDeAnonymousTask(String[] source, int offset, int length,
			User user) {
		this.danmaku = Arrays.asList(source).subList(offset, offset + length);
		this.user = user;
	}

	@Override
	public String call() throws Exception {
		String result = "";
		for (String c : this.danmaku)
			result += StringUtils.commentDeAnonymous(c, user) + "\n";
		return result;
	}

}
