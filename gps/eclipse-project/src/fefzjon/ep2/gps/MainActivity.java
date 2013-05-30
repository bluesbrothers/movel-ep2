package fefzjon.ep2.gps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity {

	private GoogleMap map;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		this.map = ((SupportMapFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		this.map.setMyLocationEnabled(true);
		this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		LatLng center = new LatLng(-23.561706, -46.725719);
		this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 13.5f));
	}

	@Override
	public void onStart() {
		super.onStart();
		// Intent intent = new Intent(this, MapActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		// this.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
