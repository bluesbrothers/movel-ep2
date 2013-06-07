package fefzjon.ep2.bandejao;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fefzjon.ep2.bandejao.adapter.RefeicoesAdapter;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.exceptions.EpDoisConnectionException;
import fefzjon.ep2.exceptions.EpDoisException;

public class FullCardapioActivity extends BasicListActivity {

	private List<CardapioDia>	listCardapios;
	private int					bandecoId;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_full_cardapio);

		Intent incomingIntent = this.getIntent();
		this.bandecoId = incomingIntent.getIntExtra(IntentKeys.DETAILS_BANDECO_ID, 1);

		TextView tituloView = (TextView) this.findViewById(R.id.cardapio_details);
		tituloView.setText(Bandecos.getById(this.bandecoId).nome);

		CardapioSemana cardapioSemana;
		try {
			cardapioSemana = ContentManager.getIntance().getCardapioSemana(this.bandecoId, this.isOnline());
		} catch (EpDoisConnectionException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			cardapioSemana = new CardapioSemana(this.bandecoId);
		} catch (EpDoisException e) {
			e.printStackTrace();
			cardapioSemana = new CardapioSemana(this.bandecoId);
		}

		this.listCardapios = cardapioSemana.asList();
		ListAdapter adapter = new RefeicoesAdapter(this, this.listCardapios);

		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
		CardapioDia cardapioDia = this.listCardapios.get(position);
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, this.bandecoId);
		intent.putExtra(IntentKeys.DATA_CARDAPIO, cardapioDia.getDataReferente());
		intent.putExtra(IntentKeys.TIPO_REFEICAO, cardapioDia.getTipoRefeicao());
		this.startActivity(intent);
		this.finish();
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null) && netInfo.isConnectedOrConnecting();
	}

}
