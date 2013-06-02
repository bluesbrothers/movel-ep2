package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.model.UltimoCardapio;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.persist.DBManager;

public class MainActivity extends Activity {

	private Button	btCentral;
	private Button	btPCO;
	private Button	btFisica;
	private Button	btQuimica;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		if (!DBManager.isInitialized()) {
			DBManager.registerModel(CardapioDia.class);
			DBManager.registerModel(UltimoCardapio.class);
			DBManager.initializeModule(this, "FEFZJON_BANDECO", 2);
		}

		this.btCentral = (Button) this.findViewById(R.id.btCentral);
		this.btPCO = (Button) this.findViewById(R.id.btPCO);
		this.btFisica = (Button) this.findViewById(R.id.btFisica);
		this.btQuimica = (Button) this.findViewById(R.id.btQuimica);

		this.setupButtons();

	}

	private void handleBandejaoButtonClick(final Bandecos bandeco) {
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, bandeco.id);
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

}
