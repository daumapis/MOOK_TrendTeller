package kr.mook.trendteller.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import kr.mook.trendteller.R;

import org.holoeverywhere.LayoutInflater;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * 
 * @author Geng, Nolleh ( Kyeong mi kim )
 * 
 */
public class TTSearchResultListActivity extends SherlockFragmentActivity
		implements OnItemClickListener {

	private static String LOG_TAG = "test";
	private static final String JSON_KEY_TITLE = "title";
	private static final String JSON_KEY_CHANNEL = "channel";
	private static final String JSON_KEY_ITEM = "item";
	private static final String JSON_KEY_DESC = "description";
	private static final String JSON_KEY_LINK = "link";

	private LayoutInflater mInflater;
	private JsonAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_list);

		mInflater = LayoutInflater.from(getApplicationContext());

		JSONObject result = getInput();
		try {
			Log.d("test", "searchResult = " + result.toString(6));
			setList(result);

		} catch (JSONException e) {
			Log.e("test", "error : ", e);
		}
	}

	private void setList(JSONObject result) {

		try {
			ListView listView = (ListView) findViewById(R.id.result_list);
			JSONArray list = result.getJSONObject(JSON_KEY_CHANNEL)
					.getJSONArray(JSON_KEY_ITEM);

			mAdapter = new JsonAdapter(list);

			listView.setAdapter(mAdapter);
			listView.setOnItemClickListener(this);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "setList", e);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long id) {
		// Ŭ���ϸ� > �ٷ� webView.

		Log.d(LOG_TAG, "ItemClik");
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(getUrlString(pos)));
		startActivity(intent);
	}

	private String getUrlString(int pos) {
		try {
			JSONObject listItem = mAdapter.getItem(pos);
			String url = listItem.getString(JSON_KEY_LINK);
			return url;
		} catch (JSONException e) {
			Log.e(LOG_TAG, "onItemClick", e);
		}
		return null;
	}

	private class JsonAdapter extends BaseAdapter {

		JSONArray mList;

		JsonAdapter(JSONArray list) {
			mList = list;

			try {
				Log.d(LOG_TAG, mList.toString(6));
			} catch (JSONException e) {
				Log.e(LOG_TAG, "JsonAdapter", e);
			}
		}

		@Override
		public int getCount() {
			return mList.length();
		}

		@Override
		public JSONObject getItem(int position) {
			try {
				return mList.getJSONObject(position);
			} catch (JSONException e) {
				Log.e(LOG_TAG, "getItem", e);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			try {
				ViewHolder viewHolder;
				if (convertView == null) {
					viewHolder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.result_list_item);

					viewHolder.txtTitle = (TextView) convertView
							.findViewById(R.id.result_item_txt_title);
					viewHolder.btnPlay = (Button) convertView
							.findViewById(R.id.result_item_btn_play);
					viewHolder.btnPlay
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {
									Log.d(LOG_TAG, "onClick Play");
									try {
										JSONObject object = mAdapter
												.getItem(position);
										String desc = object
												.getString(JSON_KEY_DESC);

										Log.d(LOG_TAG, removeHtmlTag(desc));
									} catch (JSONException e) {

									}
									// TODO 기욱님께 전달!
								}
							});
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}

				viewHolder.txtTitle.setText(getItem(position).getString(
						JSON_KEY_TITLE));
				return convertView;
			} catch (JSONException e) {
				Log.e(LOG_TAG, "getView", e);
			}
			return null;
		}

	}

	// private void getMainText(String url, final int pos) {
	//
	// Log.i(LOG_TAG, "url : " + url);
	// new HTMLAsync(url) {
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	//
	// try {
	// String html = result;
	//
	// Log.d(LOG_TAG, "html : ?" + html);
	// JSONObject object = mAdapter.getItem(pos);
	// String desc = object.getString(JSON_KEY_DESC);
	// String mainText = null;
	// int mainTextStartPoint = html.indexOf(desc);
	//
	// Log.i(LOG_TAG, "desc ? :"+ desc);
	//
	// int tagEndPoint = 0;
	//
	// StringBuilder htmlTag = new StringBuilder();
	// // find HTML Tag.
	// for (int i = mainTextStartPoint; i > 0; i--) {
	// char chAt = html.charAt(i);
	// if (htmlTag.length() == 0) {
	// char chAtNext = html.charAt(i + 1);
	// if (chAt == '<' && chAtNext != 'p'
	// && chAtNext != 'b') {
	// // check for normal html tag?
	// htmlTag.append(chAt);
	// Log.d(LOG_TAG, "append this  : ?" + chAt);
	// }
	// } else {
	// if (chAt != '=') {
	// htmlTag.append(chAt);
	// Log.d(LOG_TAG, "append this  : ?" + chAt);
	// } else {
	// tagEndPoint = i;
	// break;
	// }
	// }
	// }
	//
	// mainText = html.substring(tagEndPoint);
	//
	// Log.d(LOG_TAG, "tag : " + htmlTag);
	// Log.d(LOG_TAG, "tagEnd : " + tagEndPoint);
	//
	// mainText = mainText.substring(0,
	// mainText.indexOf(tagEndPoint));
	// mainText = removeHtmlTag(mainText);
	//
	// Log.d(LOG_TAG, "main : " + mainText);
	//
	// } catch (JSONException e) {
	// Log.e(LOG_TAG, "getMainText", e);
	// }
	//
	// }
	//
	// }.execute();
	// }

	private String removeHtmlTag(String text) {

		// Remove HTML tag from java String

		text = android.text.Html.fromHtml(text).toString();
		;
		return text.replaceAll(
				"<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}

	private String downloadURL(String addr) {
		String doc = "";
		try {
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			if (conn != null) {
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { // ������
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					for (;;) {
						String line = br.readLine();
						if (line == null)
							break;
						doc = doc + line + "\n";
					}
					br.close();
				}
				// conn.disconnect();
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "downloadURL", e);
		}

		return doc;

	}

	private class HTMLAsync extends AsyncTask<Void, Integer, String> {

		String url;

		HTMLAsync(String url) {
			this.url = url;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			return downloadURL(url);
		}
	}

	private static class ViewHolder {
		TextView txtTitle;
		Button btnPlay;
	}

	private JSONObject getInput() {
		try {

			String str = "{\"channel\":{\"pageCount\":\"1\",\"result\":\"1\",\"title\":\"Search Daum Open API\",\"totalCount\":\"2\",\"description\":\"Daum Open API search result\",\"link\":\"http://dna.daum.net/apis\",\"lastBuildDate\":\"Sat, 02 Feb 2013 00:22:59 +0900\",\"item\":[{\"pubDate\":\"2012-10-07 08:57:04\",\"title\":\"[게임/무료] 터치 터치 사다리\",\"description\":\"복불복에 유용한 사다리 게임입니다. 2 ~ 9 인이 한 게임에 참여할 수 있습니다. 현재 마켓에서 &#39;사다리&#39; 나 &#39;&lt;b&gt;터터사&lt;/b&gt;&#39; 라고 검색시 최상위에 노출됩니다. 사다리 게임에 대해 특별히 다른 설명은 필요하지 않을것 같습니다...\",\"link\":\"http://www.androidpub.com/1918743\",\"url\":\"www.androidpub.com\"}],\"generator\":\"Daum Open API\"}}";
			return new JSONObject(str);

			// Scanner scanner = new Scanner(new
			// File(Environment.getExternalStorageDirectory() + "/file.txt"),
			// "UTF8");
			//
			// StringBuilder sb = new StringBuilder();
			//
			// while (scanner.hasNext()) {
			// sb.append(scanner.next());
			// }
			//
			// return new JSONObject(sb.toString());

			// } catch (FileNotFoundException e) {
			// Log.e(LOG_TAG, "error", e);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "json ", e);
		}
		return null;

	}

}
