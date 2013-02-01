package kr.mook.trendteller.activity;

import java.util.ArrayList;
import java.util.Collections;

import kr.mook.trendteller.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

public class TTMainActivity extends SherlockFragmentActivity implements OnQueryTextListener {

	private static final String SEARCH_KEYWORD = "searchKeyword";
	SearchView searchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		refreshListView();
	}

	private void refreshListView() {
		SharedPreferences prefs = getSharedPreferences(SEARCH_KEYWORD, MODE_PRIVATE);

		ArrayList<String> list = new ArrayList<String>();
		for (String keyword : prefs.getAll().keySet()) {
			list.add(keyword);
		}
		
		Collections.sort(list);
		
		ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, list);

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search for keyword…");
		searchView.setOnQueryTextListener(this);
		
		menu.add("검색")
			.setIcon(R.drawable.ic_search)
        	.setActionView(searchView)
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		
		menu.add("?").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getTitle().equals("검색")) {
			
		} else if (item.getTitle().equals("?")) {
			
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		System.out.println(searchView.getQuery().toString());
		
		SharedPreferences prefs = getSharedPreferences(SEARCH_KEYWORD, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(searchView.getQuery().toString(), searchView.getQuery().toString());
		editor.commit();
		
		refreshListView();
		
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

}
