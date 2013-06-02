package fefzjon.ep2.rssfeed.utils;

import java.util.Arrays;

import android.os.AsyncTask;
import android.widget.Toast;
import fefzjon.ep2.rssfeed.MainActivity;
import fefzjon.ep2.rssfeed.MainActivity.FeedData;

public class UpdateFeedAsyncTask extends AsyncTask<String, Integer, FeedData> {

	private MainActivity parent;

	public UpdateFeedAsyncTask(final MainActivity parent) {
		this.parent = parent;
	}

	@Override
	protected void onPreExecute() {
		// Runs in UI thread before executing starts
		if (this.parent.isOnline()) {
			Toast.makeText(this.parent, "Buscando por items do feed...",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this.parent, "[Offline] Pegando feed do cache...",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected FeedData doInBackground(final String... urls) {
		// Runs in backgroung worker thread (possibly not really in a parallel
		// manner)
		/*
		 * if (this.isCancelled()) { break; }
		 */

		return this.parent.getFeedData(Arrays.asList(urls));
	}

	@Override
	protected void onProgressUpdate(final Integer... progress) {
		// Runs in UI thread periodically
	}

	@Override
	protected void onPostExecute(final FeedData result) {
		// Runs in UI thread after execution completes sucessfully
		this.parent.finishUpdateContents(result);
		Toast.makeText(this.parent, "Lista seminários atualizada!",
				Toast.LENGTH_SHORT).show();

	}

	protected void onCanceled(final FeedData result) {
		Toast.makeText(this.parent, "Atualização cancelada...",
				Toast.LENGTH_SHORT).show();
	}
}
