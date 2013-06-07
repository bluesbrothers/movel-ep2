package fefzjon.ep2.bandejao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import fefzjon.ep2.bandejao.adapter.CompareAdapter;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.BandexCalculator;
import fefzjon.ep2.bandejao.utils.BandexConstants;
import fefzjon.ep2.bandejao.utils.CardapioAsync;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;

public class CompareCardapioActivity extends BasicListActivity implements
		RefreshableActivity {

	private List<CardapioDia> listCardapios;
	private List<Integer> bandecosIdList;
	private Date compareDate;
	private int tipoRefeicao;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_full_cardapio);

		this.recuperaInfosIntent();

		this.generateTitulo(this.tipoRefeicao);

		new CardapioAsync(this).execute(this.bandecosIdList
				.toArray(new Integer[1]));
	}

	private void generateTitulo(final int tipoRefeicao) {
		TextView tituloView = (TextView) this
				.findViewById(R.id.cardapio_details);
		tituloView.setText(BandexCalculator.dataApresentacaoCardapio(
				this.compareDate, tipoRefeicao));
	}

	@SuppressLint("DefaultLocale")
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

		this.tipoRefeicao = incomingIntent.getIntExtra(
				IntentKeys.TIPO_REFEICAO, BandexConstants.ALMOCO);
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

	@Override
	public void setContent(final CardapioSemana... cardapiosSemana) {
		this.listCardapios = new ArrayList<CardapioDia>();

		for (CardapioSemana cardapioSemana : cardapiosSemana) {
			CardapioDia cardapioDia = cardapioSemana.get(this.compareDate,
					this.tipoRefeicao);
			if (cardapioDia != null) {
				this.listCardapios.add(cardapioDia);
			} else {
				Toast.makeText(
						this,
						"Sem informações sobre algum dos bandejões selecionados",
						Toast.LENGTH_SHORT).show();
			}
		}
		this.findViewById(R.id.atualizando_cardapio).setVisibility(View.GONE);

		ListAdapter adapter = new CompareAdapter(this, this.listCardapios);

		this.setListAdapter(adapter);

	}

	@Override
	public boolean isForceRefresh() {
		return false;
	}

}
