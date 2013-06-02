package fefzjon.ep2.bandejao;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import fefzjon.ep2.bandejao.adapter.RefeicoesAdapter;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.exceptions.EpDoisConnectionException;
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
			cardapioSemana = ContentManager.getIntance().getCardapioSemana(bandecoId, this.isOnline());
		} catch (EpDoisConnectionException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			cardapioSemana = new CardapioSemana(bandecoId);
		} catch (EpDoisException e) {
			e.printStackTrace();
			cardapioSemana = new CardapioSemana(bandecoId);
		}

		ListAdapter adapter = new RefeicoesAdapter(this, cardapioSemana);

		this.setListAdapter(adapter);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null) && netInfo.isConnectedOrConnecting();
	}

}
