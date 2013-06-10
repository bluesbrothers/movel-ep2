package fefzjon.ep2.bandejao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fefzjon.ep2.bandejao.manager.DialogManager;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.BandexCalculator;
import fefzjon.ep2.bandejao.utils.BandexConstants;
import fefzjon.ep2.bandejao.utils.CardapioAsync;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;

public class DetailsActivity extends BasicActivity implements
		RefreshableActivity {

	private TextView txDetalheCardapioView;
	private TextView txTituloData;

	private int bandexId;

	private Date dataCardapio;
	private int tipoRefeicao;

	private CardapioDia cardapioDia;

	private boolean forceRefresh;

	private List<Integer> mSelectedItems;
	private List<Bandecos> outrosBandecos;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_details);

		this.getIntentInfo();

		Button btLocation = (Button) this.findViewById(R.id.bt_ver_localizacao);
		Button btFullCardapio = (Button) this
				.findViewById(R.id.bt_cardapio_semana);
		Button btCompararRefeicao = (Button) this
				.findViewById(R.id.bt_comparar_refeicao);

		btLocation.setVisibility(View.GONE);
		btFullCardapio.setVisibility(View.GONE);
		btCompararRefeicao.setVisibility(View.GONE);

		new CardapioAsync(this).execute(this.bandexId);
	}

	@Override
	public void setContent(final CardapioSemana... cardapiosSemana) {
		CardapioSemana cardapioSemana = cardapiosSemana[0];
		this.txDetalheCardapioView = (TextView) this
				.findViewById(R.id.cardapio_details);
		this.txTituloData = (TextView) this.findViewById(R.id.cardapio_data);

		this.cardapioDia = cardapioSemana.get(this.dataCardapio,
				this.tipoRefeicao);

		this.txTituloData.setText(BandexCalculator.dataApresentacaoCardapio(
				this.dataCardapio, this.tipoRefeicao));

		if (this.cardapioDia == null) {
			Intent newIntent = new Intent(DetailsActivity.this,
					FullCardapioActivity.class);
			newIntent.putExtra(IntentKeys.DETAILS_BANDECO_ID, this.bandexId);
			DetailsActivity.this.startActivity(newIntent);
			DetailsActivity.this.finish();
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(this.cardapioDia.getCardapio());
		builder.append("\n").append(this.cardapioDia.getKcal()).append(" kcal");
		this.txDetalheCardapioView.setText(builder.toString());

		this.setupOutrosBandecos();

		this.setupButtons(this.bandexId);
	}

	private void getIntentInfo() {
		Intent intent = this.getIntent();

		this.bandexId = intent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);
		this.dataCardapio = (Date) intent
				.getSerializableExtra(IntentKeys.DATA_CARDAPIO);
		this.tipoRefeicao = intent.getIntExtra(IntentKeys.TIPO_REFEICAO,
				BandexConstants.ALMOCO); // default = almoco
		this.forceRefresh = intent.getBooleanExtra(IntentKeys.FORCE_REFRESH,
				false);
	}

	private void setupButtons(final int bandecoId) {
		Button btLocation = (Button) this.findViewById(R.id.bt_ver_localizacao);
		Button btFullCardapio = (Button) this
				.findViewById(R.id.bt_cardapio_semana);
		Button btCompararRefeicao = (Button) this
				.findViewById(R.id.bt_comparar_refeicao);

		btLocation.setVisibility(View.VISIBLE);
		btFullCardapio.setVisibility(View.VISIBLE);
		btCompararRefeicao.setVisibility(View.VISIBLE);

		btLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent intent = new Intent(DetailsActivity.this,
						MapActivity.class);
				intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, bandecoId);
				DetailsActivity.this.startActivity(intent);
			}
		});

		btFullCardapio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent intent = new Intent(DetailsActivity.this,
						FullCardapioActivity.class);
				intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, bandecoId);
				DetailsActivity.this.startActivity(intent);
				DetailsActivity.this.finish();
			}
		});

		btCompararRefeicao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Date dataReferente = DetailsActivity.this.cardapioDia
						.getDataReferente();
				int tipoRefeicao = DetailsActivity.this.cardapioDia
						.getTipoRefeicao();
				DetailsActivity.this.mSelectedItems = new ArrayList<Integer>();

				AlertDialog.Builder builder = DialogManager.buildAlertDialog(
						DetailsActivity.this, DetailsActivity.this.bandexId,
						DetailsActivity.this.mSelectedItems,
						DetailsActivity.this.outrosBandecos, dataReferente,
						tipoRefeicao);
				builder.create().show();
			}
		});
	}

	private void setupOutrosBandecos() {
		this.outrosBandecos = new ArrayList<Bandecos>();
		for (Bandecos b : Bandecos.values()) {
			if (b.id != this.bandexId) {
				this.outrosBandecos.add(b);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_force_refresh:
			Intent intent = new Intent(this, this.getClass());
			intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, this.bandexId);
			intent.putExtra(IntentKeys.TIPO_REFEICAO, this.tipoRefeicao);
			intent.putExtra(IntentKeys.DATA_CARDAPIO, this.dataCardapio);
			intent.putExtra(IntentKeys.FORCE_REFRESH, true);
			this.startActivity(intent);

			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean isForceRefresh() {
		return this.forceRefresh;
	}

}
