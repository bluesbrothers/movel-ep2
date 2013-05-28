package fefzjon.ep2.rssfeed;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import fefzjon.ep2.persist.DBManager;
import fefzjon.ep2.rssfeed.manager.ContentManager;
import fefzjon.ep2.rssfeed.model.FeedItem;
import fefzjon.ep2.util.IntentKeys;

public class MainActivity extends ListActivity {

	private List<FeedItem>	feedItems;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		DBManager.initializeModule(this, "FEFZJON_PALESTRAS_IME", 1);

		if (this.isOnline()) {
			this.feedItems = ContentManager
					.fetchAndParseFeed("http://www.ime.usp.br/index.php?option=com_eventlist&view=categoryevents&format=feed&id=62&type=rss");
		} else {
			this.feedItems = ContentManager.getLastList();
			Toast.makeText(this, "Aparentemente você não está conectado...", Toast.LENGTH_SHORT).show();
		}
		List<String> headlines = new ArrayList<String>();

		for (FeedItem f : this.feedItems) {
			headlines.add(f.getTitle());
		}

		// Binding data
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, headlines);

		this.setListAdapter(adapter);

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
			case R.id.action_settings:
				this.startActivity(new Intent(this, Configuracoes.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
		//		Uri uri = Uri.parse(this.feedItems.get(position).getLink());
		//		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		//		this.startActivity(intent);
		FeedItem item = this.feedItems.get(position);

		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(IntentKeys.DETAILS_FEED_TITLE, item.getTitle());
		intent.putExtra(IntentKeys.DETAILS_FEED_DESCRIPTION, item.getDescription());
		intent.putExtra(IntentKeys.DETAILS_FEED_LINK, item.getLink());

		this.startActivity(intent);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null) && netInfo.isConnectedOrConnecting();
	}
}
