package net.kaaass.bilidanmaku.util;

/**
 * Crc32封装+破解类
 * 
 * 算法来自于：http://moepus.oicp.net/2016/11/27/crccrack/
 * JS版本作者：https://github.com/esterTion/BiliBili_crc2mid
 * 
 * @author KAAAsS
 *
 */
public class Crc32 {
	private static final int CRCPOLYNOMIAL = 0xEDB88320;
	private static int[] crctable;

	static {
		crctable = createTable();
	}

	/**
	 * CRC32打表
	 * 
	 * @return
	 */
	private static int[] createTable() {
		int[] r = new int[256];
		int crcreg, j;
		for (int i = 0; i < 256; ++i) {
			crcreg = i;
			for (j = 0; j < 8; ++j) {
				if ((crcreg & 1) != 0) {
					crcreg = CRCPOLYNOMIAL ^ (crcreg >>> 1);
				} else {
					crcreg >>>= 1;
				}
			}
			r[i] = crcreg;
		}
		return r;
	}

	private static int getcrcindex(int t) {
		// if(t>0)
		// t-=256;
		for (int i = 0; i < 256; i++) {
			if (crctable[i] >>> 24 == t)
				return i;
		}
		return -1;
	}

	private static int crc32lastindex(String input) {
		int crcstart = 0xFFFFFFFF, len = input.length(), index = 0;
		for (int i = 0; i < len; ++i) {
			index = (crcstart ^ input.charAt(i)) & 0xff;
			crcstart = (crcstart >>> 8) ^ crctable[index];
		}
		return index;
	}

	/**
	 * CRC32 计算
	 * 
	 * @param input
	 *            原文
	 * @return CRC32结果
	 */
	public static int crc32(String input) {
		int crcstart = 0xFFFFFFFF, index;
		for (int i = 0; i < input.length(); ++i) {
			index = (crcstart ^ input.charAt(i)) & 0xff;
			crcstart = (crcstart >>> 8) ^ crctable[index];
		}
		return crcstart;
	}

	private static String deepCheck(int i, int[] index) {
		int tc = 0x00;
		String str = "";
		int hash = crc32(String.valueOf(i));

		tc = hash & 0xff ^ index[2];
		if (!(tc <= 57 && tc >= 48))
			return null;
		str += tc - 48;
		hash = crctable[index[2]] ^ (hash >>> 8);
		tc = hash & 0xff ^ index[1];
		if (!(tc <= 57 && tc >= 48))
			return null;
		str += tc - 48;
		hash = crctable[index[1]] ^ (hash >>> 8);
		tc = hash & 0xff ^ index[0];
		if (!(tc <= 57 && tc >= 48))
			return null;
		str += tc - 48;
		hash = crctable[index[0]] ^ (hash >>> 8);
		return str;
	}

	public static String deCrc32(String input) {
		int ht = (int) (Long.parseLong(input, 16) ^ 0xffffffff), snum, lastindex, i;
		int[] index = new int[4];
		String deepCheckData = "";
		for (i = 3; i >= 0; i--) {
			index[3 - i] = getcrcindex(ht >>> (i * 8));
			snum = crctable[index[3 - i]];
			ht ^= snum >>> ((3 - i) * 8);
		}
		for (i = 0; i < 100000; i++) {
			lastindex = crc32lastindex(String.valueOf(i));
			if (lastindex == index[3]) {
				deepCheckData = deepCheck(i, index);
				if (deepCheckData != null)
					break;
			}
		}
		if (i == 100000)
			return null;
		return i + "" + deepCheckData;
	}
}
