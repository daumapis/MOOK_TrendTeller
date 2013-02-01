package kr.mook.trendteller.tts;

import java.util.HashMap;

public class HangeulConvert {

	public static String convert(String hangul) {
		if (hangul == null || hangul.length() <= 0)
			return "";

		StringBuffer alpha = new StringBuffer();

		for (int i = 0; i < hangul.length(); i++) {
			char c = hangul.charAt(i);

			// 자음 혹은 모음 한글자로 이루어진 한글이면 바로 KEY_MAP에서 찾아서 추가한다.
			if (isSingleHangul(c)) {
				String s1 = (String) CHOSUNG_KEY_MAP.get(new Character(c));
				String s2 = (String) JUNGSUNG_KEY_MAP.get(new Character(c));
				String s3 = (String) JONGSUNG_KEY_MAP.get(new Character(c));

				if (s1 != null)
					alpha.append(s1);
				else if (s2 != null)
					alpha.append(s2);
				else if (s3 != null)
					alpha.append(s3);
				continue;
			}

			// 한글이 아니면 그 글자를 바로 추가한다.
			if (!isHangul(c)) {
				alpha.append(c);
				continue;
			}

			int code = (int) c;

			// 초성분리
			code -= 0xAC00;
			int chosung = code / (21 * 28);
			alpha.append((String) CHOSUNG_KEY_MAP.get(new Character(
					CHOSUNG[chosung])));

			// 중성분리
			code -= chosung * 21 * 28;
			int jungsung = code / 28;
			alpha.append((String) JUNGSUNG_KEY_MAP.get(new Character(
					JUNGSUNG[jungsung])));

			// 종성분리
			int jongsung = (code % 28) - 1;
			if (jongsung >= 0)
				alpha.append((String) JONGSUNG_KEY_MAP.get(new Character(
						JONGSUNG[jongsung])));
		}
		return alpha.toString();
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isSingleHangul(char c) {
		int code = (int) c;
		if (code >= 0x3131 && code <= 0x3163)
			return true;
		return false;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isHangul(char c) {
		int code = (int) c;
		if (code >= 0xAC00 && code <= 0xD7A3)
			return true;
		return false;
	}

	// --------------------------------------------------------------------------------

	private static final char[] CHOSUNG = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ',
			'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
	private static final char[] JUNGSUNG = { 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ',
			'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ',
			'ㅣ' };
	private static final char[] JONGSUNG = { 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ',
			'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ',
			'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
	private static final HashMap CHOSUNG_KEY_MAP;
	static {
		CHOSUNG_KEY_MAP = new HashMap();
		CHOSUNG_KEY_MAP.put(new Character('ㄱ'), "g");
		CHOSUNG_KEY_MAP.put(new Character('ㄲ'), "gg");
		CHOSUNG_KEY_MAP.put(new Character('ㄴ'), "n");
		CHOSUNG_KEY_MAP.put(new Character('ㄷ'), "d");
		CHOSUNG_KEY_MAP.put(new Character('ㄸ'), "dd");
		CHOSUNG_KEY_MAP.put(new Character('ㄹ'), "r");
		CHOSUNG_KEY_MAP.put(new Character('ㅁ'), "m");
		CHOSUNG_KEY_MAP.put(new Character('ㅂ'), "b");
		CHOSUNG_KEY_MAP.put(new Character('ㅃ'), "bb");
		CHOSUNG_KEY_MAP.put(new Character('ㅅ'), "s");
		CHOSUNG_KEY_MAP.put(new Character('ㅆ'), "ss");
		CHOSUNG_KEY_MAP.put(new Character('ㅇ'), "w");
		CHOSUNG_KEY_MAP.put(new Character('ㅈ'), "j");
		CHOSUNG_KEY_MAP.put(new Character('ㅉ'), "jj");
		CHOSUNG_KEY_MAP.put(new Character('ㅊ'), "ch");
		CHOSUNG_KEY_MAP.put(new Character('ㅋ'), "k");
		CHOSUNG_KEY_MAP.put(new Character('ㅌ'), "t");
		CHOSUNG_KEY_MAP.put(new Character('ㅍ'), "p");
		CHOSUNG_KEY_MAP.put(new Character('ㅎ'), "h");
	}

	private static final HashMap JUNGSUNG_KEY_MAP;
	static {
		JUNGSUNG_KEY_MAP = new HashMap();
		JUNGSUNG_KEY_MAP.put(new Character('ㅏ'), "a");
		JUNGSUNG_KEY_MAP.put(new Character('ㅐ'), "ae");
		JUNGSUNG_KEY_MAP.put(new Character('ㅑ'), "ya");
		JUNGSUNG_KEY_MAP.put(new Character('ㅒ'), "yae");
		JUNGSUNG_KEY_MAP.put(new Character('ㅓ'), "u");
		JUNGSUNG_KEY_MAP.put(new Character('ㅔ'), "e");
		JUNGSUNG_KEY_MAP.put(new Character('ㅕ'), "oe");
		JUNGSUNG_KEY_MAP.put(new Character('ㅖ'), "ye");
		JUNGSUNG_KEY_MAP.put(new Character('ㅗ'), "o");
		JUNGSUNG_KEY_MAP.put(new Character('ㅘ'), "oa");
		JUNGSUNG_KEY_MAP.put(new Character('ㅙ'), "oae");
		JUNGSUNG_KEY_MAP.put(new Character('ㅚ'), "oi");
		JUNGSUNG_KEY_MAP.put(new Character('ㅛ'), "yo");
		JUNGSUNG_KEY_MAP.put(new Character('ㅜ'), "u");
		JUNGSUNG_KEY_MAP.put(new Character('ㅝ'), "uo");
		JUNGSUNG_KEY_MAP.put(new Character('ㅞ'), "yei");
		JUNGSUNG_KEY_MAP.put(new Character('ㅟ'), "ui");
		JUNGSUNG_KEY_MAP.put(new Character('ㅠ'), "ou");
		JUNGSUNG_KEY_MAP.put(new Character('ㅡ'), "eu");
		JUNGSUNG_KEY_MAP.put(new Character('ㅢ'), "ui");
		JUNGSUNG_KEY_MAP.put(new Character('ㅣ'), "i");

	}

	private static final HashMap JONGSUNG_KEY_MAP;
	static {
		JONGSUNG_KEY_MAP = new HashMap();
		JONGSUNG_KEY_MAP.put(new Character('ㄱ'), "g");
		JONGSUNG_KEY_MAP.put(new Character('ㄲ'), "gg");
		JONGSUNG_KEY_MAP.put(new Character('ㄳ'), "gs");
		JONGSUNG_KEY_MAP.put(new Character('ㄴ'), "n");
		JONGSUNG_KEY_MAP.put(new Character('ㄵ'), "ng");
		JONGSUNG_KEY_MAP.put(new Character('ㄶ'), "nh");
		JONGSUNG_KEY_MAP.put(new Character('ㄷ'), "d");
		JONGSUNG_KEY_MAP.put(new Character('ㄹ'), "l");
		JONGSUNG_KEY_MAP.put(new Character('ㄺ'), "lg");
		JONGSUNG_KEY_MAP.put(new Character('ㄻ'), "lm");
		JONGSUNG_KEY_MAP.put(new Character('ㄼ'), "lm");
		JONGSUNG_KEY_MAP.put(new Character('ㄽ'), "ls");
		JONGSUNG_KEY_MAP.put(new Character('ㄾ'), "lt");
		JONGSUNG_KEY_MAP.put(new Character('ㄿ'), "lp");
		JONGSUNG_KEY_MAP.put(new Character('ㅀ'), "lh");
		JONGSUNG_KEY_MAP.put(new Character('ㅁ'), "m");
		JONGSUNG_KEY_MAP.put(new Character('ㅂ'), "b");
		JONGSUNG_KEY_MAP.put(new Character('ㅄ'), "bs");
		JONGSUNG_KEY_MAP.put(new Character('ㅅ'), "s");
		JONGSUNG_KEY_MAP.put(new Character('ㅆ'), "ss");
		JONGSUNG_KEY_MAP.put(new Character('ㅇ'), "ng");
		JONGSUNG_KEY_MAP.put(new Character('ㅈ'), "j");
		JONGSUNG_KEY_MAP.put(new Character('ㅊ'), "ch");
		JONGSUNG_KEY_MAP.put(new Character('ㅋ'), "k");
		JONGSUNG_KEY_MAP.put(new Character('ㅌ'), "th");
		JONGSUNG_KEY_MAP.put(new Character('ㅍ'), "p");
		JONGSUNG_KEY_MAP.put(new Character('ㅎ'), "");
	}
}