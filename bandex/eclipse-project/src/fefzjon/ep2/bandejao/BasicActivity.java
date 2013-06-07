package fefzjon.ep2.bandejao;

import android.app.Activity;
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
}
