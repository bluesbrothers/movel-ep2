package fefzjon.ep2.bandejao.utils;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import fefzjon.ep2.bandejao.MapActivity;
import fefzjon.ep2.bandejao.R;

public class MarkerInfo implements InfoWindowAdapter {

	private MapActivity parent;
	private int bandecoId;

	public MarkerInfo(final MapActivity parent, final int bandecoId) {
		this.parent = parent;
		this.bandecoId = bandecoId;
	}

	@Override
	public View getInfoContents(final Marker marker) {
		LayoutInflater inflater = this.parent.getLayoutInflater();
		View markerView = inflater.inflate(R.layout.map_marker_info, null);

		Bandecos bandex = Bandecos.getById(this.bandecoId);

		TextView tvEnd = (TextView) markerView.findViewById(R.id.mmi_endereco);
		tvEnd.setText(bandex.endereco);

		TextView tvExpUtil = (TextView) markerView
				.findViewById(R.id.mmi_expediente_util);
		this.setTextTo(tvExpUtil, bandex.expedienteDiaUtil);

		TextView tvExpSab = (TextView) markerView
				.findViewById(R.id.mmi_expediente_sabado);
		this.setTextTo(tvExpSab, bandex.expedienteSabado);

		TextView tvExpDom = (TextView) markerView
				.findViewById(R.id.mmi_expediente_domingo);
		this.setTextTo(tvExpDom, bandex.expedienteDomingo);

		return markerView;
	}

	private void setTextTo(final TextView tv, final String text) {
		String[] lines = text.split(";");
		tv.setSingleLine(lines.length == 1);
		tv.setLines(lines.length);
		tv.setText(Html.fromHtml(text.replace(";", "<br/>")));
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		return null;
	}
}
