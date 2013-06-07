package fefzjon.ep2.gps;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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

import fefzjon.ep2.gps.MarkerInfo.MarkData;
import fefzjon.ep2.gps.utilities.ConnectionHandler;
import fefzjon.ep2.gps.utilities.Constants;
import fefzjon.ep2.gps.utilities.RouteManager;
import fefzjon.ep2.gps.utilities.RouteManager.PointArray;
import fefzjon.ep2.gps.utilities.Timer;
import fefzjon.ep2.gps.utilities.Timer.TimerCallback;
import fefzjon.ep2.gps.utilities.TimetableManager;

public class MainActivity extends FragmentActivity implements LocationListener,
		GoogleMap.OnMapLongClickListener, TimerCallback {

	private GoogleMap map;
	private ConnectionHandler connHandler;
	private int buspCode;
	private LatLng closestPointOnRoute;
	private MarkerInfo markerInfo;
	private Marker closestMarker;
	private String closestArrival;
	private Timer timer;

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
		this.map.setOnMapLongClickListener(this);
		this.buspCode = 8012;
		this.closestPointOnRoute = null;
		this.closestArrival = "";
		this.timer = new Timer(this, 60 * 1000);
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
			this.markerInfo.getData().clear();
			this.buspCode = 8012;
			this.closestPointOnRoute = null;
			this.closestArrival = "";
			this.updateMap();
			return true;
		case R.id.selectBusp8022:
			this.map.clear();
			this.markerInfo.getData().clear();
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
			intent.putExtra(Constants.BUSP_CODE, this.buspCode);
			this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapLongClick(final LatLng point) {
		Location loc = new Location("");
		loc.setLatitude(point.latitude);
		loc.setLongitude(point.longitude);
		LatLng p = RouteManager.getPointClosestTo(loc);
		MarkData mark = this.getMarkForPoint(p);
		String id = "userMark"
				+ String.valueOf(this.markerInfo.getData().size());
		this.markerInfo.setValues(id, mark);
		this.addMarker(p, id);
	}

	@Override
	public void onLocationChanged(final Location location) {
		LatLng p = RouteManager.getPointClosestTo(location);
		MarkData mark = this.getMarkForPoint(p);

		if ((this.closestPointOnRoute == null)
				|| !this.closestPointOnRoute.equals(p)) {
			this.closestPointOnRoute = p;

			this.map.clear();

			this.markerInfo.setValues(MarkerInfo.CLOSEST_ID, mark);
			this.closestMarker = this.addMarker(p, MarkerInfo.CLOSEST_ID);
			this.closestArrival = mark.arrival;

			this.recreateMarks();
			this.updateMap();
		} else if (!this.closestArrival.equals(mark.arrival)) {
			this.closestArrival = mark.arrival;
			this.markerInfo.setValues(MarkerInfo.CLOSEST_ID, mark);
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

	private void recreateMarks() {
		for (Map.Entry<String, MarkData> entry : this.markerInfo.getData()
				.entrySet()) {
			MarkData mark = this.getMarkForPoint(entry.getValue().pos);
			String id = entry.getKey();
			this.markerInfo.setValues(id, mark);

			this.addMarker(mark.pos, id);
		}
	}

	private MarkerInfo.MarkData getMarkForPoint(final LatLng point) {
		MarkData mark = new MarkData();
		mark.dist = RouteManager.calculateRouteLengthUpTo(this.buspCode, point);
		int duration = TimetableManager.getApproxRouteDuration();
		// speed in meters / minute
		double speed = RouteManager.getRouteLength(this.buspCode) / duration;
		mark.deltaT = (int) (mark.dist / speed);
		Date expectedDeparture = TimetableManager.getDateMinus(mark.deltaT);
		mark.nextBus = TimetableManager.getDepartureAfter(expectedDeparture);
		mark.arrival = TimetableManager
				.getDateStr(TimetableManager.getDateAdd(
						TimetableManager.getDateForDeparture(mark.nextBus),
						mark.deltaT));
		mark.pos = point;
		return mark;
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
		if (this.buspCode == Constants.BUSP_1) {
			routeOpt.color(0xffff0000);
		} else if (this.buspCode == Constants.BUSP_2) {
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
		line.position(p, 15);

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

	public Marker addMarker(final LatLng p, final String id) {
		return this.map.addMarker(new MarkerOptions().position(p)
				.draggable(false).snippet(id));
	}

	private void centerMapOnUSP() {
		LatLng center = new LatLng(Constants.USP_CENTER_LAT,
				Constants.USP_CENTER_LON);
		this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(center,
				Constants.USP_CENTER_ZOOM));
	}

	public int getBuspCode() {
		return this.buspCode;
	}

	@Override
	public void onTick() {
		this.map.clear();
		this.recreateMarks();
		this.updateMap();
	}

	@Override
	public void onResume() {
		super.onResume();
		this.timer.start();
		this.map.clear();
		this.recreateMarks();
		this.updateMap();
	}

	@Override
	public void onPause() {
		super.onPause();
		this.timer.stop();
	}
}
