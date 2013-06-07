package fefzjon.ep2.bandejao.adapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fefzjon.ep2.bandejao.R;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.BandexCalculator;

public class RefeicoesAdapter extends BaseAdapter {
	private Context context;
	private List<CardapioDia> listaCardapio;

	public RefeicoesAdapter(final Context context,
			final List<CardapioDia> listaCardapio) {
		this.context = context;
		this.listaCardapio = listaCardapio;
		Collections.sort(this.listaCardapio, new Comparator<CardapioDia>() {
			@Override
			public int compare(final CardapioDia lhs, final CardapioDia rhs) {
				Date lDate = lhs.getDataReferente();
				Date rDate = rhs.getDataReferente();

				if (lDate == null) {
					return -1;
				} else if (rDate == null) {
					return 1;
				}
				int difDate = lDate.compareTo(rDate);
				if (difDate != 0) {
					return difDate;
				}

				Integer lTipoRefeicao = lhs.getTipoRefeicao();
				Integer rTipoRefeicao = rhs.getTipoRefeicao();

				return (lTipoRefeicao == null ? 0 : lTipoRefeicao)
						- (rTipoRefeicao == null ? 0 : rTipoRefeicao);
			}
		});
	}

	@Override
	public int getCount() {
		return this.listaCardapio.size();
	}

	@Override
	public Object getItem(final int i) {
		return this.listaCardapio.get(i);
	}

	@Override
	public long getItemId(final int i) {
		return i;
	}

	protected String generateTitulo(final CardapioDia cDia) {
		return BandexCalculator.dataApresentacaoCardapio(
				cDia.getDataReferente(), cDia.getTipoRefeicao());
	}

	@Override
	public View getView(final int i, final View view, final ViewGroup viewGroup) {
		CardapioDia cDia = this.listaCardapio.get(i);

		LayoutInflater inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View aView = inflater.inflate(R.layout.view_cardapio_item, null);

		TextView tituloView = (TextView) aView
				.findViewById(R.id.item_cardapio_titulo);
		TextView cardapioView = (TextView) aView
				.findViewById(R.id.item_cardapio_details);

		tituloView.setText(this.generateTitulo(cDia));

		StringBuilder builder = new StringBuilder();
		builder.append(cDia.getCardapio());
		builder.append("\n").append(cDia.getKcal()).append(" kcal");
		cardapioView.setText(builder.toString());

		return aView;
	}

}
