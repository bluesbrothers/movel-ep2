package fefzjon.ep2.rssfeed;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Configuracoes extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.configuracoes);
	}
}
