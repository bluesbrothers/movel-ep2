package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.bandejao.manager.StoaManager;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.BandexCalculator;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.exceptions.EpDoisConnectionException;
import fefzjon.ep2.exceptions.EpDoisException;
import fefzjon.ep2.utils.Utils;

public class DetailsActivity extends Activity {

	private TextView	txDetalheCardapioView;
	private TextView	txTituloData;

	private int			bandexId;

	private CardapioDia	cardapioDia;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_details);

		Intent intent = this.getIntent();

		this.bandexId = intent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);
		final int bandecoId = this.bandexId;

		CardapioSemana cardapioSemana;
		try {
			cardapioSemana = ContentManager.getIntance().getCardapioSemana(bandecoId, this.isOnline());
		} catch (EpDoisConnectionException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			cardapioSemana = new CardapioSemana(bandecoId);
		} catch (EpDoisException e) {
			e.printStackTrace();
			cardapioSemana = new CardapioSemana(bandecoId);
		}

		this.txDetalheCardapioView = (TextView) this.findViewById(R.id.cardapio_details);
		this.txTituloData = (TextView) this.findViewById(R.id.cardapio_data);

		//		int proximaRefeicao = BandexCalculator.nextMeal();
		int proximaRefeicao = 2;

		this.cardapioDia = cardapioSemana.get(Utils.parseAnoMesDia("2013-05-27"), proximaRefeicao);

		if (this.cardapioDia != null) {
			this.txTituloData.setText(BandexCalculator.dataApresentacaoCardapio(this.cardapioDia));

			StringBuilder builder = new StringBuilder();
			builder.append(this.cardapioDia.getCardapio());
			builder.append("\n").append(this.cardapioDia.getKcal()).append(" kcal");
			this.txDetalheCardapioView.setText(builder.toString());
		}

		Button btLocation = (Button) this.findViewById(R.id.bt_ver_localizacao);
		btLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent intent = new Intent(DetailsActivity.this, MapActivity.class);
				intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, bandecoId);
				DetailsActivity.this.startActivity(intent);
			}
		});

		Button btFullCardapio = (Button) this.findViewById(R.id.bt_cardapio_semana);
		btFullCardapio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent intent = new Intent(DetailsActivity.this, FullCardapioActivity.class);
				intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, bandecoId);
				DetailsActivity.this.startActivity(intent);
			}
		});

		Button btComentarios = (Button) this.findViewById(R.id.bt_ver_comentarios);
		btComentarios.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if ((DetailsActivity.this.cardapioDia == null)
						|| (DetailsActivity.this.cardapioDia.getCommentId() == null)) {
					Toast.makeText(DetailsActivity.this, "Nenhuma refeição sendo vista", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent;
				if (StoaManager.getInstance().isLogged()) {
					intent = new Intent(DetailsActivity.this, ComentariosActivity.class);
				} else {
					intent = new Intent(DetailsActivity.this, LoginActivity.class);
				}

				intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, bandecoId);
				intent.putExtra(IntentKeys.DETAILS_MEAL_ID, DetailsActivity.this.cardapioDia.getCommentId());
				DetailsActivity.this.startActivity(intent);
			}
		});
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
				try {
					ContentManager.getIntance().forceRefreshCardapioSemana(this.bandexId, this.isOnline());
				} catch (EpDoisException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(this, this.getClass());
				intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, this.bandexId);
				this.startActivity(intent);

				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null) && netInfo.isConnectedOrConnecting();
	}

}
