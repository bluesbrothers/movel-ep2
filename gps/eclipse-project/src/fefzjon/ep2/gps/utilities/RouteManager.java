package fefzjon.ep2.gps.utilities;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import fefzjon.ep2.gps.MainActivity;
import fefzjon.ep2.gps.R;

public class RouteManager {
	private static MainActivity parent;
	private static double routeLength8012;
	private static double routeLength8022;
	private static boolean calculatedLengths = false;

	public static void setMainActivity(final MainActivity activity) {
		parent = activity;
		calculateRouteLengths();
	}

	public static class PointArray {
		public TypedArray lats;
		public TypedArray lons;
	}

	public static PointArray getPoints() {
		return getPoints(parent.getBuspCode());
	}

	public static PointArray getPoints(final int code) {
		Resources res = parent.getResources();
		PointArray pts = new PointArray();
		if (code == 8012) {
			pts.lats = res.obtainTypedArray(R.array.latitudes8012);
			pts.lons = res.obtainTypedArray(R.array.longitudes8012);
		} else if (code == 8022) {
			pts.lats = res.obtainTypedArray(R.array.latitudes8022);
			pts.lons = res.obtainTypedArray(R.array.longitudes8022);
		} else {
			return null;
		}
		return pts;
	}

	public static double getRouteLength(final int code) {
		if (code == 8012) {
			return routeLength8012;
		} else if (code == 8022) {
			return routeLength8022;
		}
		return 0;
	}

	private static void calculateRouteLengths() {
		if (calculatedLengths) {
			return;
		}
		calculatedLengths = true;
		routeLength8012 = calculateRouteLength(8012);
		routeLength8022 = calculateRouteLength(8022);
	}

	public static double calculateRouteLength(final int code) {
		return calculateRouteLengthUpTo(code, null);
	}

	public static double calculateRouteLengthUpTo(final int code,
			final LatLng pos) {
		PointArray pts = getPoints(code);
		if (pts == null) {
			return 0;
		}
		Location prev = null;
		float length = 0;
		for (int i = 0; i < pts.lats.length(); i++) {
			Location loc = new Location("");
			loc.setLatitude(pts.lats.getFloat(i, -23));
			loc.setLongitude(pts.lons.getFloat(i, -46));
			if (prev == null) {
				prev = loc;
				continue;
			}

			length += prev.distanceTo(loc);
			prev = loc;

			if ((pos != null)
					&& pos.equals(new LatLng(loc.getLatitude(), loc
							.getLongitude()))) {
				return length;
			}
		}
		pts.lats.recycle();
		pts.lons.recycle();
		return length;
	}

	public static LatLng getPointClosestTo(final Location pos) {
		Location closest = new Location("");
		float dist = closest.distanceTo(pos);
		PointArray pts = getPoints();
		if (pts == null) {
			return null;
		}
		for (int i = 0; i < pts.lats.length(); i++) {
			double lat = pts.lats.getFloat(i, -23);
			double lon = pts.lons.getFloat(i, -46);
			Location loc = new Location("");
			loc.setLatitude(lat);
			loc.setLongitude(lon);
			float locDist = loc.distanceTo(pos);
			if (locDist < dist) {
				dist = locDist;
				closest = loc;
			}
		}
		pts.lats.recycle();
		pts.lons.recycle();
		return new LatLng(closest.getLatitude(), closest.getLongitude());
	}
}
