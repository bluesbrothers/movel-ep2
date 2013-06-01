package fefzjon.ep2.gps;

import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class ConnectionHandler implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private MainActivity parent;
	private LocationClient locationClient;

	ConnectionHandler(final MainActivity parent) {
		this.parent = parent;
		this.locationClient = new LocationClient(parent, this, this);
	}

	public void connect() {
		this.locationClient.connect();
	}

	public void disconnect() {
		this.locationClient.disconnect();
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(final Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this.parent, "Connected", Toast.LENGTH_SHORT).show();

		LocationRequest req = LocationRequest.create();
		// accuracy of location received
		req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		req.setInterval(1000 * 5); // ideal rate to receive updates
		// fastest rate at which the app can received updates without bugging
		// (screen flickering, data overflow, etc...)
		req.setFastestInterval(1000 * 1);
		this.locationClient.requestLocationUpdates(req, this.parent);
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this.parent, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
		this.locationClient.removeLocationUpdates(this.parent);
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this.parent, 9000);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			Toast.makeText(
					this.parent,
					"An Error occured. Code = "
							+ connectionResult.getErrorCode(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
