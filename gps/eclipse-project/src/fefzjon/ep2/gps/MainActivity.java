package fefzjon.ep2.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

	}

	@Override
	public void onStart() {
		super.onStart();
		Intent intent = new Intent(this, MapActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		this.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
