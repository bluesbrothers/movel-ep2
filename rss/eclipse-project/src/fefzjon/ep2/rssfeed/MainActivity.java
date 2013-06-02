package fefzjon.ep2.rssfeed;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import fefzjon.ep2.persist.DBManager;
import fefzjon.ep2.rssfeed.manager.ContentManager;
import fefzjon.ep2.rssfeed.model.FeedItem;
import fefzjon.ep2.rssfeed.utils.FeedUrls;
import fefzjon.ep2.rssfeed.utils.IntentKeys;
import fefzjon.ep2.rssfeed.utils.UpdateFeedAsyncTask;

public class MainActivity extends ListActivity {

	private List<FeedItem> feedItems;

	private List<String> headlines;

	private boolean shouldUpdate;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		if (!DBManager.isInitialized()) {
			DBManager.registerModel(FeedItem.class);
			DBManager.initializeModule(this, "FEFZJON_PALESTRAS_IME", 1);
		}
		this.headlines = new ArrayList<String>();

		this.updateContent();

		// Binding data
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, this.headlines);

		this.setListAdapter(adapter);

	}

	private void updateContent() {
		this.shouldUpdate = false;

		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(this);
		List<String> urlsToFetch = new ArrayList<String>();
		if (preference
				.getBoolean(this.getString(R.string.key_fetch_dcc), false)) {
			urlsToFetch.add(FeedUrls.DCC);
		}
		if (preference
				.getBoolean(this.getString(R.string.key_fetch_mae), false)) {
			urlsToFetch.add(FeedUrls.MAE);
		}
		if (preference
				.getBoolean(this.getString(R.string.key_fetch_mat), false)) {
			urlsToFetch.add(FeedUrls.MAT);
		}
		if (preference
				.getBoolean(this.getString(R.string.key_fetch_map), false)) {
			urlsToFetch.add(FeedUrls.MAP);
		}
		new UpdateFeedAsyncTask(this).execute(urlsToFetch
				.toArray(new String[0]));
	}

	public class FeedData {
		public List<String> headlines;
		public List<FeedItem> items;
	}

	public FeedData getFeedData(final List<String> urlsToFetch) {
		FeedData data = new FeedData();
		if (this.isOnline()) {
			data.items = ContentManager.fetchAndParseFeed(urlsToFetch);
		} else {
			data.items = ContentManager.getLastList();
		}
		data.headlines = new ArrayList<String>();

		for (FeedItem f : data.items) {
			data.headlines.add(f.getTitle());
		}
		return data;
	}

	public void finishUpdateContents(final FeedData data) {
		this.headlines = data.headlines;
		this.feedItems = data.items;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, this.headlines);

		this.setListAdapter(adapter);
		this.getListView().invalidateViews();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (this.shouldUpdate) {
			Log.i("RSSFeed", "Resumindo app - refresh automatico");
			this.updateContent();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		this.shouldUpdate = true;
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_refresh:
			this.updateContent();
			return true;
		case R.id.action_settings:
			this.startActivity(new Intent(this, Configuracoes.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		// Uri uri = Uri.parse(this.feedItems.get(position).getLink());
		// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// this.startActivity(intent);
		FeedItem item = this.feedItems.get(position);

		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(IntentKeys.DETAILS_FEED_TITLE, item.getTitle());
		intent.putExtra(IntentKeys.DETAILS_FEED_DESCRIPTION,
				item.getDescription());
		intent.putExtra(IntentKeys.DETAILS_FEED_LINK, item.getLink());
		intent.putExtra(IntentKeys.DETAILS_FEED_CATEGORY, item.getCategory());

		this.startActivity(intent);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null) && netInfo.isConnectedOrConnecting();
	}
}
