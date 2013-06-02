package fefzjon.ep2.bandejao;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.TextView;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.exceptions.EpDoisException;

public class FullCardapioActivity extends ListActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_full_cardapio);

		Intent incomingIntent = this.getIntent();
		int bandecoId = incomingIntent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);

		TextView tituloView = (TextView) this.findViewById(R.id.cardapio_details);
		tituloView.setText(Bandecos.getById(bandecoId).nome);

		CardapioSemana cardapioSemana;
		try {
			cardapioSemana = ContentManager.getIntance().getCardapioSemana(bandecoId);
		} catch (EpDoisException e) {
			e.printStackTrace();
			cardapioSemana = new CardapioSemana(bandecoId);
		}

		ListAdapter adapter = new MyAdapter(this, cardapioSemana);

		this.setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.full_cardapio, menu);
		return true;
	}

}
