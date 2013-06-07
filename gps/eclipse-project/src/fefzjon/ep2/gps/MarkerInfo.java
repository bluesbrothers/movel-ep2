package fefzjon.ep2.gps;

import java.util.HashMap;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfo implements InfoWindowAdapter {

	public static final String CLOSEST_ID = "closest";

	private MainActivity parent;

	public static class MarkData {
		public double dist;
		public int deltaT;
		public String nextBus;
		public String arrival;
		public String destination;
		public LatLng pos;
	}

	private HashMap<String, MarkData> data;

	public MarkerInfo(final MainActivity parent) {
		this.parent = parent;
		this.data = new HashMap<String, MarkerInfo.MarkData>();
	}

	@Override
	public View getInfoContents(final Marker marker) {
		LayoutInflater inflater = this.parent.getLayoutInflater();
		View markerDialog = inflater.inflate(R.layout.marker_dialog, null);

		MarkData entry = this.data.get(marker.getSnippet());
		if (entry == null) {
			return markerDialog;
		}

		TextView tvTitle = (TextView) markerDialog.findViewById(R.id.md_title);
		if (marker.getSnippet().equals(CLOSEST_ID)) {
			tvTitle.setText(R.string.pontoProx);
		} else {
			tvTitle.setText(R.string.pontoUser);
		}

		TextView tvDist = (TextView) markerDialog.findViewById(R.id.md_dist);
		tvDist.setText(String.valueOf((int) entry.dist) + "m");

		TextView tvTime = (TextView) markerDialog.findViewById(R.id.md_time);
		tvTime.setText(String.valueOf(entry.deltaT) + "min");

		TextView tvExit = (TextView) markerDialog.findViewById(R.id.md_exit);
		tvExit.setText(entry.nextBus);

		TextView tvArrival = (TextView) markerDialog
				.findViewById(R.id.md_arrival);
		tvArrival.setText(entry.arrival);

		TextView tvDest = (TextView) markerDialog
				.findViewById(R.id.md_destination);
		tvDest.setText(entry.destination);

		return markerDialog;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		return null;
	}

	public void setValues(final String id, final MarkData mark) {
		this.data.put(id, mark);
	}

	public HashMap<String, MarkData> getData() {
		return this.data;
	}
}
