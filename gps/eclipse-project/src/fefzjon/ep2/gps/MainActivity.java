package fefzjon.ep2.gps;

import java.util.ArrayList;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

	private GoogleMap map;
	private ConnectionHandler connHandler;

	public static final double USP_CENTER_LAT = -23.561706;
	public static final double USP_CENTER_LON = -46.725719;
	public static final float USP_CENTER_ZOOM = 13.5f;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		this.connHandler = new ConnectionHandler(this);

		this.map = ((SupportMapFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		this.map.setMyLocationEnabled(true);
		this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		this.centerMapOnUSP();
		this.addBUSProute(8012);
		this.setStatusText(R.string.selectBusp8012);
	}

	@Override
	public void onStart() {
		super.onStart();
		this.connHandler.connect();

		// Intent intent = new Intent(this, MapActivity.class);
		// intent.putExtra(EXTRA_MESSAGE, message);
		// this.startActivity(intent);
	}

	@Override
	protected void onStop() {
		this.connHandler.disconnect();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		this.connHandler.atualizaPos();
		switch (item.getItemId()) {
		case R.id.selectBusp8012:
			this.map.clear();
			this.addBUSProute(8012);
			this.setStatusText(R.string.selectBusp8012);
			return true;
		case R.id.selectBusp8022:
			this.map.clear();
			this.addBUSProute(8022);
			this.setStatusText(R.string.selectBusp8022);
			return true;
		case R.id.centralizaMapaUSP:
			this.centerMapOnUSP();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setStatusText(final int id) {
		Resources res = this.getResources();
		String statMsg = "Mostrando " + res.getString(id) + " | AMAGAD";
		TextView stat = (TextView) this.findViewById(R.id.status_text);
		stat.setText(statMsg);
	}

	private void addBUSProute(final int code) {
		Resources res = this.getResources();
		TypedArray lats;
		TypedArray lons;
		if (code == 8012) {
			lats = res.obtainTypedArray(R.array.latitudes8012);
			lons = res.obtainTypedArray(R.array.longitudes8012);
		} else if (code == 8022) {
			lats = res.obtainTypedArray(R.array.latitudes8022);
			lons = res.obtainTypedArray(R.array.longitudes8022);
		} else {
			return;
		}

		PolylineOptions routeOpt = new PolylineOptions();
		if (code == 8012) {
			routeOpt.color(0xffff0000);
		} else if (code == 8022) {
			routeOpt.color(0xff00ff00);
		}
		routeOpt.width(5);
		int arrowMark = 0;
		ArrayList<GroundOverlayOptions> arrows = new ArrayList<GroundOverlayOptions>();
		for (int i = 0; i < lats.length(); i++, arrowMark++) {
			double lat = lats.getFloat(i, -23);
			double lon = lons.getFloat(i, -46);
			LatLng p = new LatLng(lat, lon);
			routeOpt.add(p);

			if ((arrowMark == 5) && (i < (lats.length() - 5))) {
				arrowMark = 0;
				lat = lats.getFloat(i + 5, -23);
				lon = lons.getFloat(i + 5, -46);
				LatLng p2 = new LatLng(lat, lon);
				arrows.add(this.createRouteArrow(p, p2));
			}
		}
		routeOpt.geodesic(true);
		this.map.addPolyline(routeOpt);

		for (GroundOverlayOptions arrow : arrows) {
			this.map.addGroundOverlay(arrow);
		}

		lats.recycle();
		lons.recycle();
	}

	private GroundOverlayOptions createRouteArrow(final LatLng p,
			final LatLng p2) {
		GroundOverlayOptions line = new GroundOverlayOptions();
		line.image(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
		line.position(p, 10);

		double x1 = p.latitude;
		double y1 = p.longitude;
		double x2 = p2.latitude;
		double y2 = p2.longitude;
		double x = x2 - x1;
		double y = y2 - y1;

		double brng = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
		// Log.w("EP2.gps", "Bearing = " + ((Double) brng).toString());
		line.bearing((float) brng);

		line.zIndex(1);
		return line;
	}

	public void addMarker(final LatLng p, final String title) {
		this.map.addMarker(new MarkerOptions().position(p).title(title));
	}

	private void centerMapOnUSP() {
		LatLng center = new LatLng(USP_CENTER_LAT, USP_CENTER_LON);
		this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(center,
				USP_CENTER_ZOOM));
	}
}
