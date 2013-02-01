package kr.mook.trendteller.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.mook.trendteller.R;
import kr.mook.trendteller.tts.HangeulTTS;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

public class TTMainActivity extends SherlockFragmentActivity implements
		OnQueryTextListener {

	private static final String SEARCH_KEYWORD = "searchKeyword";
	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		refreshListView();
	}

	private void searchQQ(String keyword) {
		InputStream is = null;
		String url = "http://211.43.193.21/examples/search/";
		HttpClient httpclient = new DefaultHttpClient();
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("q", keyword));
			// nameValuePairs.add(new BasicNameValuePair("apiKey",
			// "094a7f309318e5616f4cdc431cfd1c27118426ad"));
			// nameValuePairs.add(new BasicNameValuePair("apiKey",
			// "DAUM_SEARCH_DEMO_APIKEY"));

			String result = "";
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 5000);
			

			HttpPost httppost = new HttpPost(url);
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(
					nameValuePairs, "UTF-8");
			httppost.setEntity(entityRequest);

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entityResponse = response.getEntity();
			is = entityResponse.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			is.close();
			result = sb.toString();
			
			Log.d("Ryukw82",removeHtmlTag(result));
			HangeulTTS.read(TTMainActivity.this, removeHtmlTag(result));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

	}
	
	private String removeHtmlTag(String text) {

		// Remove HTML tag from java String

		text = android.text.Html.fromHtml(text).toString();
		;
		return text.replaceAll(
				"<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}



	private void refreshListView() {
		SharedPreferences prefs = getSharedPreferences(SEARCH_KEYWORD,
				MODE_PRIVATE);

		final List<String> list = new ArrayList<String>();
		for (String keyword : prefs.getAll().keySet()) {
			list.add(keyword);
		}

		Collections.sort(list);

		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);

		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String keyword = list.get(position);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							searchQQ(keyword);
							Log.d("Ryukw82",keyword);
						} catch (Exception e) {

						}
					}

				}).start();
			}

		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				SharedPreferences prefs = getSharedPreferences(SEARCH_KEYWORD,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.remove(list.get(position));
				editor.commit();
				refreshListView();

				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		searchView = new SearchView(getSupportActionBar().getThemedContext());
//		searchView.setQueryHint("Search for keyword…");
		searchView.setOnQueryTextListener(this);

		menu.add("추가")
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

//		menu.add("?").setShowAsAction(
//				MenuItem.SHOW_AS_ACTION_IF_ROOM
//						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

//		if (item.getTitle().equals("검색")) {
//
//		} 
//		else if (item.getTitle().equals("?")) {
//
//		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		System.out.println(searchView.getQuery().toString());

		SharedPreferences prefs = getSharedPreferences(SEARCH_KEYWORD,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(searchView.getQuery().toString(), searchView
				.getQuery().toString());
		editor.commit();
		editor.clear();

		refreshListView();

		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public void onBackPressed() {
		if (HangeulTTS.isTTSSpeaking()) {
			HangeulTTS.stop();
		} else {
			super.onBackPressed();
		}
	}
	
	
	
	

}


//private void searchQ(String keyword) throws IOException,
//ParserConfigurationException, SAXException {
//// 링크 주소 만들기
//String requestUrl = "http://apis.daum.net/search/web";
//requestUrl += "?apikey=" + "85316af50841e4fef45c329e8c4bb7ab9fdbbf36"; // 발급된
//																	// 키
//requestUrl += "&q=" + keyword; // 검색어
//URL url = new URL(requestUrl);
//
//// API 요청 및 반환
//URLConnection conn = url.openConnection();
//DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//DocumentBuilder builder = factory.newDocumentBuilder();
//Document doc = builder.parse(conn.getInputStream());
//
//StringBuilder sb = new StringBuilder();
//// channel노드를 객체화 하기
//Node node = doc.getElementsByTagName("channel").item(0);
//for (int i = 0; i < node.getChildNodes().getLength(); i++) {
//Node channelNode = node.getChildNodes().item(i);
//String nodeName = channelNode.getNodeName();
//if ("description".equals(nodeName)) {
//	// sb.append(channelNode.getTextContent());
//} else if ("item".equals(nodeName)) {
//	// item노드를 객체화 하기
//	for (int j = 0; j < channelNode.getChildNodes().getLength(); j++) {
//		Node itemNode = channelNode.getChildNodes().item(j);
//		if ("description".equals(itemNode.getNodeName()))
//			sb.append(itemNode.getTextContent());
//		System.out.println(itemNode.getTextContent());
//	}
//}
//}
//System.out.println(sb.toString());
//}


//private void searchQuery(String keyword) {
//	InputStream is = null;
//	String url = "http://apis.daum.net/search/web";
//	HttpClient httpclient = new DefaultHttpClient();
//	try {
//		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		nameValuePairs.add(new BasicNameValuePair("q", keyword));
//		nameValuePairs.add(new BasicNameValuePair("apiKey",
//				"094a7f309318e5616f4cdc431cfd1c27118426ad"));
//		// nameValuePairs.add(new BasicNameValuePair("apiKey",
//		// "DAUM_SEARCH_DEMO_APIKEY"));
//
//		String result = "";
//		HttpParams params = httpclient.getParams();
//		HttpConnectionParams.setConnectionTimeout(params, 5000);
//		HttpConnectionParams.setSoTimeout(params, 5000);
//
//		HttpPost httppost = new HttpPost(url);
//		UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(
//				nameValuePairs, "UTF-8");
//		httppost.setEntity(entityRequest);
//
//		HttpResponse response = httpclient.execute(httppost);
//		HttpEntity entityResponse = response.getEntity();
//		is = entityResponse.getContent();
//
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//				is, "UTF-8"), 8);
//		StringBuilder sb = new StringBuilder();
//		String line = null;
//		while ((line = reader.readLine()) != null) {
//			sb.append(line).append("\n");
//		}
//		is.close();
//		result = sb.toString();
//		System.out.println(result);
//	} catch (IOException e) {
//		e.printStackTrace();
//	} finally {
//		httpclient.getConnectionManager().shutdown();
//	}
//}