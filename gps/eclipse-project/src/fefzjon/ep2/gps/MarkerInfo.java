package fefzjon.ep2.gps;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfo implements InfoWindowAdapter {

	private MainActivity parent;
	private double dist;
	private int deltaT;
	private String nextBus;
	private String arrival;

	public MarkerInfo(final MainActivity parent) {
		this.parent = parent;
	}

	@Override
	public View getInfoContents(final Marker marker) {
		LayoutInflater inflater = this.parent.getLayoutInflater();
		View markerDialog = inflater.inflate(R.layout.marker_dialog, null);

		TextView tvDist = (TextView) markerDialog.findViewById(R.id.md_dist);
		tvDist.setText(String.valueOf((int) this.dist) + "m");

		TextView tvTime = (TextView) markerDialog.findViewById(R.id.md_time);
		tvTime.setText(String.valueOf(this.deltaT) + "min");

		TextView tvExit = (TextView) markerDialog.findViewById(R.id.md_exit);
		tvExit.setText(this.nextBus);

		TextView tvArrival = (TextView) markerDialog
				.findViewById(R.id.md_arrival);
		tvArrival.setText(this.arrival);

		return markerDialog;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		return null;
	}

	public void setValues(final double dist, final int deltaT,
			final String nextBus, final String arrival) {
		this.dist = dist;
		this.deltaT = deltaT;
		this.nextBus = nextBus;
		this.arrival = arrival;
	}

}
