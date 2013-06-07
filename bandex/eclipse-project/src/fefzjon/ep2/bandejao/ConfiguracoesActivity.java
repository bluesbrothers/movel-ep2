package fefzjon.ep2.bandejao;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class ConfiguracoesActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.configuracoes);

		final CheckBoxPreference central = (CheckBoxPreference) this
				.getPreferenceScreen().findPreference(
						this.getString(R.string.key_compare_central));
		final CheckBoxPreference quimica = (CheckBoxPreference) this
				.getPreferenceScreen().findPreference(
						this.getString(R.string.key_compare_quimica));
		final CheckBoxPreference fisica = (CheckBoxPreference) this
				.getPreferenceScreen().findPreference(
						this.getString(R.string.key_compare_fisica));
		final CheckBoxPreference pco = (CheckBoxPreference) this
				.getPreferenceScreen().findPreference(
						this.getString(R.string.key_compare_pco));

		ListPreference listPreference = (ListPreference) this
				.getPreferenceScreen().findPreference(
						this.getString(R.string.pref_init_main_screen_key));

		if (listPreference.getValue().equals(
				this.getString(R.string.pref_list_screen_init_compare))) {
			central.setEnabled(true);
			fisica.setEnabled(true);
			pco.setEnabled(true);
			quimica.setEnabled(true);
		}

		listPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						if (newValue.equals(ConfiguracoesActivity.this
								.getString(R.string.pref_list_screen_init_compare))) {
							central.setEnabled(true);
							fisica.setEnabled(true);
							pco.setEnabled(true);
							quimica.setEnabled(true);
						} else {
							central.setEnabled(false);
							fisica.setEnabled(false);
							pco.setEnabled(false);
							quimica.setEnabled(false);
						}
						return true;
					}
				});
	}
}
