package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class DetailsActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_details);

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

}
