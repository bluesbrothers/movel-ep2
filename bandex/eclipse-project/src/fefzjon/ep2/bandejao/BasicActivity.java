package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.model.UltimoCardapio;
import fefzjon.ep2.persist.DBManager;

public abstract class BasicActivity extends Activity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!DBManager.isInitialized()) {
			DBManager.registerModel(CardapioDia.class);
			DBManager.registerModel(UltimoCardapio.class);
			DBManager.initializeModule(this, "FEFZJON_BANDECO", 2);
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null) && netInfo.isConnectedOrConnecting();
	}
}
