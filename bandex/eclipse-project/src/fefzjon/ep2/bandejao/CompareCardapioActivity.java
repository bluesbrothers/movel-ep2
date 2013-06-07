package fefzjon.ep2.bandejao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fefzjon.ep2.bandejao.adapter.CompareAdapter;
import fefzjon.ep2.bandejao.manager.ContentManager;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.BandexCalculator;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;
import fefzjon.ep2.exceptions.EpDoisConnectionException;
import fefzjon.ep2.exceptions.EpDoisException;

public class CompareCardapioActivity extends BasicListActivity {

	private List<CardapioDia> listCardapios;
	private List<Integer> bandecosIdList;
	private Date compareDate;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_full_cardapio);

		this.recuperaInfosIntent();

		int tipoRefeicao = BandexCalculator.tipoRefeicao(this.compareDate);
		this.generateTitulo(tipoRefeicao);

		this.recuperaCardapiosDia(tipoRefeicao);

		ListAdapter adapter = new CompareAdapter(this, this.listCardapios);

		this.setListAdapter(adapter);
	}

	private void recuperaCardapiosDia(final int tipoRefeicao) {
		this.listCardapios = new ArrayList<CardapioDia>();
		for (int bandecoId : this.bandecosIdList) {
			CardapioSemana cardapioSemana;
			try {
				cardapioSemana = ContentManager.getIntance().getCardapioSemana(
						bandecoId, this.isOnline());
			} catch (EpDoisConnectionException e) {
				e.printStackTrace();
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				cardapioSemana = new CardapioSemana(bandecoId);
			} catch (EpDoisException e) {
				e.printStackTrace();
				cardapioSemana = new CardapioSemana(bandecoId);
			}
			CardapioDia cardapioDia = cardapioSemana.get(this.compareDate,
					tipoRefeicao);
			if (cardapioDia != null) {
				this.listCardapios.add(cardapioDia);
			}
		}
	}

	private void generateTitulo(final int tipoRefeicao) {
		TextView tituloView = (TextView) this
				.findViewById(R.id.cardapio_details);
		tituloView.setText(BandexCalculator.dataApresentacaoCardapio(
				this.compareDate, tipoRefeicao));
	}

	private void recuperaInfosIntent() {
		Intent incomingIntent = this.getIntent();

		/* recupera informações de quais bandejões comparar */
		int qtdBandeco = incomingIntent.getIntExtra(
				IntentKeys.QTD_COMPARE_BANDECO, 0);
		Set<Integer> bandecosId = new TreeSet<Integer>();
		for (int i = 0; i < qtdBandeco; i++) {
			String intentKey = String.format(
					IntentKeys.COMPARE_BANDECO_PATTERN_KEY, i);
			bandecosId.add(incomingIntent.getIntExtra(intentKey, 1));
		}

		this.bandecosIdList = new ArrayList<Integer>(bandecosId);

		this.compareDate = (Date) incomingIntent
				.getSerializableExtra(IntentKeys.DATA_CARDAPIO);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		CardapioDia cardapioDia = this.listCardapios.get(position);
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(IntentKeys.DETAILS_BANDECO_ID,
				cardapioDia.getBandexId());
		intent.putExtra(IntentKeys.DATA_CARDAPIO,
				cardapioDia.getDataReferente());
		intent.putExtra(IntentKeys.TIPO_REFEICAO, cardapioDia.getTipoRefeicao());
		this.startActivity(intent);
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
