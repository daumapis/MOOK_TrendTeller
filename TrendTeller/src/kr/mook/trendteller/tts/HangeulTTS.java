package kr.mook.trendteller.tts;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class HangeulTTS {
	
	private static TextToSpeech tts;

	
	public static void read(Context context, String title) {
		speak (context, title);
	}

	private static void speak(Context context, final String str) {
		
		tts = new TextToSpeech(context, new OnInitListener() {

			@Override
			public void onInit(int status) {

				if (status == TextToSpeech.SUCCESS) {
					tts.setLanguage(Locale.KOREA);

					if (tts.isLanguageAvailable(Locale.KOREAN) == TextToSpeech.LANG_NOT_SUPPORTED) {
						String fixedStr = HangeulConvert.convert(appendSpaceToString(str));
						tts.speak(fixedStr, TextToSpeech.QUEUE_FLUSH, null);
					} else {
						tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
					}
				} else {

				}
				
				

			}
		});
	}
	
	private static String appendSpaceToString(String str) {
		int N = str.length();
		StringBuilder builder = new StringBuilder("");
		
		for (int i=0; i<N; i++) {
			builder.append(str.charAt(i));
			builder.append(" ");
		}
		
		return builder.toString();
	}
	
	public static boolean isTTSSpeaking() {
		return (tts != null)?tts.isSpeaking():false;
	}
	
	public static void stop() {
		if (tts !=  null) {
			tts.shutdown();
		}
	}
	
	

}
