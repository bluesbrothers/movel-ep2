package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.BandexCalculator;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.exceptions.EpDoisException;
import fefzjon.ep2.utils.Utils;

public class DetailsActivity extends Activity {

	private TextView	txDetalheCardapioView;
	private TextView	txTituloData;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_details);

		Intent intent = this.getIntent();

		final int bandecoId = intent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);

		CardapioSemana cardapioSemana;
		try {
			cardapioSemana = ContentManager.getIntance().getCardapioSemana(bandecoId);
		} catch (EpDoisException e) {
			e.printStackTrace();
			cardapioSemana = new CardapioSemana(bandecoId);
		}

		this.txDetalheCardapioView = (TextView) this.findViewById(R.id.cardapio_details);
		this.txTituloData = (TextView) this.findViewById(R.id.cardapio_data);

		//		int proximaRefeicao = BandexCalculator.nextMeal();
		int proximaRefeicao = 2;

		CardapioDia cardapioDia = cardapioSemana.get(Utils.parseAnoMesDia("2013-05-28"), proximaRefeicao);

		if (cardapioDia != null) {
			this.txTituloData.setText(BandexCalculator.dataApresentacaoCardapio(cardapioDia));

			StringBuilder builder = new StringBuilder();
			builder.append(cardapioDia.getCardapio());
			builder.append("\n").append(cardapioDia.getKcal()).append(" kcal");
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
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

}
