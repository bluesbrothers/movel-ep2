package fefzjon.ep2.bandejao;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fefzjon.ep2.bandejao.adapter.RefeicoesAdapter;
import fefzjon.ep2.bandejao.listeners.FullCardapioItemLongClickListener;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.CardapioAsync;
import fefzjon.ep2.bandejao.utils.CardapioSemana;
import fefzjon.ep2.bandejao.utils.IntentKeys;

public class FullCardapioActivity extends BasicListActivity implements
		RefreshableActivity {

	private List<CardapioDia> listCardapios;
	private int bandexId;
	private List<Integer> mSelectedItems;
	private List<Bandecos> outrosBandecos;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_full_cardapio);

		Intent incomingIntent = this.getIntent();
		this.bandexId = incomingIntent.getIntExtra(
				IntentKeys.DETAILS_BANDECO_ID, 1);

		TextView tituloView = (TextView) this
				.findViewById(R.id.cardapio_details);
		tituloView.setText(Bandecos.getById(this.bandexId).nome);

		new CardapioAsync(this).execute(this.bandexId);
	}

	private void setupOutrosBandecos() {
		this.outrosBandecos = new ArrayList<Bandecos>();
		for (Bandecos b : Bandecos.values()) {
			if (b.id != this.bandexId) {
				this.outrosBandecos.add(b);
			}
		}
	}

	private void setupLongClick() {
		this.getListView().setLongClickable(true);

		this.getListView()
				.setOnItemLongClickListener(
						new FullCardapioItemLongClickListener(this,
								this.listCardapios, this.bandexId,
								this.mSelectedItems, this.outrosBandecos));

	}

	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		CardapioDia cardapioDia = this.listCardapios.get(position);
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(IntentKeys.DETAILS_BANDECO_ID, this.bandexId);
		intent.putExtra(IntentKeys.DATA_CARDAPIO,
				cardapioDia.getDataReferente());
		intent.putExtra(IntentKeys.TIPO_REFEICAO, cardapioDia.getTipoRefeicao());
		this.startActivity(intent);
		this.finish();
	}

	@Override
	public void setContent(final CardapioSemana... cardapiosSemana) {
		CardapioSemana cardapioSemana = cardapiosSemana[0];
		this.listCardapios = cardapioSemana.asList();
		ListAdapter adapter = new RefeicoesAdapter(this, this.listCardapios);

		this.setupOutrosBandecos();

		this.setListAdapter(adapter);

		this.setupLongClick();

		this.findViewById(R.id.atualizando_cardapio).setVisibility(View.GONE);
	}

	@Override
	public boolean isForceRefresh() {
		return false;
	}

}
