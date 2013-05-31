package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import fefzjon.ep2.bandejao.enums.Bandecos;

public class MainActivity extends Activity {

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

	}

	private void handleBandejaoButtonClick(final Bandecos bandeco) {

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
			case R.id.action_settings:
				this.startActivity(new Intent(this, LoginActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
