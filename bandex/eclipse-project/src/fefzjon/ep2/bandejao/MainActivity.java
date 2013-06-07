package fefzjon.ep2.bandejao;

import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.BandexCalculator;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.utils.Utils;

public class MainActivity extends BasicActivity {

	private Button	btCentral;
	private Button	btPCO;
	private Button	btFisica;
	private Button	btQuimica;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		this.btCentral = (Button) this.findViewById(R.id.btCentral);
		this.btPCO = (Button) this.findViewById(R.id.btPCO);
		this.btFisica = (Button) this.findViewById(R.id.btFisica);
		this.btQuimica = (Button) this.findViewById(R.id.btQuimica);

		this.setupButtons();

		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		String initOnChoose = preference.getString(this.getString(R.string.pref_init_main_screen_key),
				this.getString(R.string.pref_list_screen_init_bandejao));

		if (initOnChoose.equals(this.getString(R.string.pref_list_screen_init_compare))) {
			Intent intent = new Intent(this, CompareCardapioActivity.class);
			int i = 0;
			if (preference.getBoolean(this.getString(R.string.key_compare_central), true)) {
				intent.putExtra(String.format(IntentKeys.COMPARE_BANDECO_PATTERN_KEY, i++), Bandecos.CENTRAL.id);
			}
			if (preference.getBoolean(this.getString(R.string.key_compare_pco), true)) {
				intent.putExtra(String.format(IntentKeys.COMPARE_BANDECO_PATTERN_KEY, i++), Bandecos.PCO.id);
			}
			if (preference.getBoolean(this.getString(R.string.key_compare_fisica), true)) {
				intent.putExtra(String.format(IntentKeys.COMPARE_BANDECO_PATTERN_KEY, i++), Bandecos.FISICA.id);
			}
			if (preference.getBoolean(this.getString(R.string.key_compare_quimica), true)) {
				intent.putExtra(String.format(IntentKeys.COMPARE_BANDECO_PATTERN_KEY, i++), Bandecos.QUIMICA.id);
			}
			intent.putExtra(IntentKeys.QTD_COMPARE_BANDECO, i);
			intent.putExtra(IntentKeys.DATA_CARDAPIO, new Date());
			this.startActivity(intent);
		}

	}

	private void handleBandejaoButtonClick(final Bandecos bandeco) {
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, bandeco.id);
		intent.putExtra(IntentKeys.DATA_CARDAPIO, Utils.today());
		intent.putExtra(IntentKeys.TIPO_REFEICAO, BandexCalculator.proximaRefeicao());
		this.startActivity(intent);
	}

	private void setupButtons() {
		this.setupCentralButton();
		this.setupPCOButton();
		this.setupFisicaButton();
		this.setupQuimicaButton();
	}

	private void setupCentralButton() {
		this.btCentral.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				MainActivity.this.handleBandejaoButtonClick(Bandecos.CENTRAL);
			}
		});
	}

	private void setupPCOButton() {
		this.btPCO.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				MainActivity.this.handleBandejaoButtonClick(Bandecos.PCO);
			}
		});
	}

	private void setupFisicaButton() {
		this.btFisica.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				MainActivity.this.handleBandejaoButtonClick(Bandecos.FISICA);
			}
		});
	}

	private void setupQuimicaButton() {
		this.btQuimica.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				MainActivity.this.handleBandejaoButtonClick(Bandecos.QUIMICA);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_configuracoes:
				Intent intent = new Intent(this, ConfiguracoesActivity.class);
				this.startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
