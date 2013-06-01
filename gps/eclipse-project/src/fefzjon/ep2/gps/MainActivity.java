package fefzjon.ep2.gps;

import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import fefzjon.ep2.gps.utilities.ConnectionHandler;
import fefzjon.ep2.gps.utilities.RouteManager;
import fefzjon.ep2.gps.utilities.RouteManager.PointArray;
import fefzjon.ep2.gps.utilities.TimetableManager;

public class MainActivity extends FragmentActivity implements LocationListener {

	private GoogleMap map;
	private ConnectionHandler connHandler;
	private int buspCode;
	private LatLng closestPointOnRoute;
	private MarkerInfo markerInfo;
	private Marker closestMarker;
	private String closestArrival;

	public static final double USP_CENTER_LAT = -23.561706;
	public static final double USP_CENTER_LON = -46.725719;
	public static final float USP_CENTER_ZOOM = 13.5f;
	public static final String BUSP_CODE = "fefzjon.ep2.gps.BUSPCode";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		TimetableManager.setMainActivity(this);
		RouteManager.setMainActivity(this);
		this.markerInfo = new MarkerInfo(this);
		this.connHandler = new ConnectionHandler(this);

		this.map = ((SupportMapFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		this.map.setMyLocationEnabled(true);
		this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		this.map.setInfoWindowAdapter(this.markerInfo);
		this.buspCode = 8012;
		this.closestPointOnRoute = null;
		this.closestArrival = "";

		this.centerMapOnUSP();
		this.updateMap();
	}

	@Override
	public void onStart() {
		super.onStart();
		this.connHandler.connect();
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
		switch (item.getItemId()) {
		case R.id.selectBusp8012:
			this.map.clear();
			this.buspCode = 8012;
			this.closestPointOnRoute = null;
			this.closestArrival = "";
			this.updateMap();
			return true;
		case R.id.selectBusp8022:
			this.map.clear();
			this.buspCode = 8022;
			this.closestPointOnRoute = null;
			this.closestArrival = "";
			this.updateMap();
			return true;
		case R.id.centralizaMapaUSP:
			this.centerMapOnUSP();
			return true;
		case R.id.goToTimeTable:
			Intent intent = new Intent(this, TimeTableActivity.class);
			intent.putExtra(BUSP_CODE, this.buspCode);
			this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(final Location location) {
		LatLng p = RouteManager.getPointClosestTo(location);
		double dist = RouteManager.calculateRouteLengthUpTo(this.buspCode, p);
		int duration = TimetableManager.getApproxRouteDuration();
		// speed in meters / minute
		double speed = RouteManager.getRouteLength(this.buspCode) / duration;
		int deltaT = (int) (dist / speed);
		Date expectedDeparture = TimetableManager.getDateMinus(deltaT);
		String nextBus = TimetableManager.getDepartureAfter(expectedDeparture);
		String arrival = TimetableManager.getDateStr(TimetableManager
				.getDateAdd(TimetableManager.getDateForDeparture(nextBus),
						deltaT));

		if ((this.closestPointOnRoute == null)
				|| !this.closestPointOnRoute.equals(p)) {
			this.closestPointOnRoute = p;

			this.map.clear();

			this.markerInfo.setValues(dist, deltaT, nextBus, arrival);
			this.closestMarker = this.addMarker(p);
			this.closestArrival = arrival;

			this.updateMap();
		} else if (!this.closestArrival.equals(arrival)) {
			this.closestArrival = arrival;
			this.markerInfo.setValues(dist, deltaT, nextBus, arrival);
			if (this.closestMarker.isInfoWindowShown()) {
				this.closestMarker.hideInfoWindow();
				this.closestMarker.showInfoWindow();
			} else {
				this.closestMarker.showInfoWindow();
				this.closestMarker.hideInfoWindow();
			}
			this.setStatusText();
		}
	}

	private void updateMap() {
		this.addBUSProute();
		this.setStatusText();
	}

	private void setStatusText() {
		int id;
		if (this.buspCode == 8012) {
			id = R.string.busp8012;
		} else if (this.buspCode == 8022) {
			id = R.string.busp8022;
		} else {
			return;
		}
		Resources res = this.getResources();
		String statMsg = res.getString(id);
		if (this.closestArrival.length() > 0) {
			statMsg += " | Chega em " + this.closestArrival;
		}
		TextView stat = (TextView) this.findViewById(R.id.status_text);
		stat.setText(statMsg);
	}

	private void addBUSProute() {
		PointArray pts = RouteManager.getPoints();
		if (pts == null) {
			return;
		}
		PolylineOptions routeOpt = new PolylineOptions();
		if (this.buspCode == 8012) {
			routeOpt.color(0xffff0000);
		} else if (this.buspCode == 8022) {
			routeOpt.color(0xff00ff00);
		}
		routeOpt.width(5);
		int arrowMark = 0;
		ArrayList<GroundOverlayOptions> arrows = new ArrayList<GroundOverlayOptions>();
		for (int i = 0; i < pts.lats.length(); i++, arrowMark++) {
			double lat = pts.lats.getFloat(i, -23);
			double lon = pts.lons.getFloat(i, -46);
			LatLng p = new LatLng(lat, lon);
			routeOpt.add(p);

			if ((arrowMark == 3) && (i < (pts.lats.length() - 1))) {
				arrowMark = 0;
				lat = pts.lats.getFloat(i + 1, -23);
				lon = pts.lons.getFloat(i + 1, -46);
				LatLng p2 = new LatLng(lat, lon);
				arrows.add(this.createRouteArrow(p, p2));
			}
		}
		routeOpt.geodesic(true);
		this.map.addPolyline(routeOpt);

		for (GroundOverlayOptions arrow : arrows) {
			this.map.addGroundOverlay(arrow);
		}

		pts.lats.recycle();
		pts.lons.recycle();
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

	public Marker addMarker(final LatLng p) {
		return this.map.addMarker(new MarkerOptions().position(p).draggable(
				false));
	}

	private void centerMapOnUSP() {
		LatLng center = new LatLng(USP_CENTER_LAT, USP_CENTER_LON);
		this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(center,
				USP_CENTER_ZOOM));
	}

	public int getBuspCode() {
		return this.buspCode;
	}
}
