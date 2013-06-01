package fefzjon.ep2.bandejao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.bandejao.utils.MarkerInfo;

public class MapActivity extends FragmentActivity {

	private GoogleMap map;

	public static final double USP_CENTER_LAT = -23.561706;
	public static final double USP_CENTER_LON = -46.725719;
	public static final float USP_CENTER_ZOOM = 14.0f;
	public static final float BANDEX_CENTER_ZOOM = 15.0f;

	private int bandecoId;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_map);

		Intent intent = this.getIntent();
		this.bandecoId = intent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);
		Bandecos bandex = Bandecos.getById(this.bandecoId);

		this.map = ((SupportMapFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		this.map.setMyLocationEnabled(true);
		this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		MarkerInfo markerInfo = new MarkerInfo(this, this.bandecoId);
		this.map.setInfoWindowAdapter(markerInfo);

		TextView tvMap = (TextView) this.findViewById(R.id.map_text);
		tvMap.setText("Restaurante " + bandex.nome);

		this.addMarker(bandex.pos);
		this.centerMapOnBandex();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.map_opt_centraliza_bandeco:
			this.centerMapOnBandex();
			return true;
		case R.id.map_opt_centraliza_usp:
			this.centerMapOnUSP();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public Marker addMarker(final LatLng p) {
		return this.map.addMarker(new MarkerOptions().position(p).draggable(
				false));
	}

	private void centerMapOnUSP() {
		LatLng center = new LatLng(USP_CENTER_LAT, USP_CENTER_LON);
		this.centerMapOn(center, USP_CENTER_ZOOM);
	}

	private void centerMapOnBandex() {
		this.centerMapOn(Bandecos.getById(this.bandecoId).pos,
				BANDEX_CENTER_ZOOM);
	}

	private void centerMapOn(final LatLng pos, final float zoom) {
		this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
	}
}
