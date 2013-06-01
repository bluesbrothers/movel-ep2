package fefzjon.ep2.bandejao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.BandexCalculator;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.utils.Utils;

public class DetailsActivity extends Activity {

	private TextView	textView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_details);

		Intent intent = this.getIntent();

		int bandecoId = intent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);

		CardapioSemana cardapioSemana = ContentManager.getIntance().getCardapioSemana(bandecoId);

		this.textView = (TextView) this.findViewById(R.id.cardapio_details);

		int proximaRefeicao = BandexCalculator.nextMeal();

		Log.i("BANDEX", Utils.formatDate(Utils.today()));
		Log.i("BANDEX", "" + proximaRefeicao);

		CardapioDia cardapioDia = cardapioSemana.get(Utils.parseAnoMesDia("2013-05-28"), proximaRefeicao);

		if (cardapioDia != null) {
			this.textView.setText(cardapioDia.getCardapio());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

}
