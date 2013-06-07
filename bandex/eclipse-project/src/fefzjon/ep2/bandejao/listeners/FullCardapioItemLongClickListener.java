package fefzjon.ep2.bandejao.listeners;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import fefzjon.ep2.bandejao.CompareCardapioActivity;
import fefzjon.ep2.bandejao.R;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.IntentKeys;

public class FullCardapioItemLongClickListener implements
		OnItemLongClickListener {

	private List<CardapioDia> listCardapios;
	private int bandecoId;
	private List<Integer> mSelectedItems;
	private List<Bandecos> outrosBandecos;

	private Context context;

	public FullCardapioItemLongClickListener(final Context context,
			final List<CardapioDia> listCardapios, final int bandecoId,
			final List<Integer> mSelectedItems,
			final List<Bandecos> outrosBandecos) {
		this.context = context;
		this.listCardapios = listCardapios;
		this.bandecoId = bandecoId;
		this.mSelectedItems = mSelectedItems;
		this.outrosBandecos = outrosBandecos;
	}

	@Override
	public boolean onItemLongClick(final AdapterView<?> parent,
			final View view, final int position, final long id) {
		final CardapioDia cardapioDia = this.listCardapios.get(position);

		AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

		this.mSelectedItems = new ArrayList<Integer>();

		CharSequence[] outrosBandecosNomes = new CharSequence[this.outrosBandecos
				.size()];

		int b_index = 0;
		for (Bandecos b : this.outrosBandecos) {
			outrosBandecosNomes[b_index++] = (b.nome);
		}

		builder.setTitle(R.string.comparar_com_bandejao);
		builder.setMultiChoiceItems(outrosBandecosNomes, null,
				new OnMultiChoiceClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which, final boolean isChecked) {
						if (isChecked) {
							FullCardapioItemLongClickListener.this.mSelectedItems
									.add(which);
						} else if (FullCardapioItemLongClickListener.this.mSelectedItems
								.contains(which)) {
							FullCardapioItemLongClickListener.this.mSelectedItems
									.remove(Integer.valueOf(which));
						}
					}
				});
		builder.setPositiveButton(R.string.do_comparar,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						Intent intent = new Intent(
								FullCardapioItemLongClickListener.this.context,
								CompareCardapioActivity.class);
						intent.putExtra(
								IntentKeys.QTD_COMPARE_BANDECO,
								FullCardapioItemLongClickListener.this.mSelectedItems
										.size() + 1); // Os bandecos alvo para
														// comparação + o
														// bandeco base
						intent.putExtra(IntentKeys.DATA_CARDAPIO,
								cardapioDia.getDataReferente());
						int i = 0;
						intent.putExtra(
								String.format(
										IntentKeys.COMPARE_BANDECO_PATTERN_KEY,
										i++),
								FullCardapioItemLongClickListener.this.bandecoId);
						for (int item : FullCardapioItemLongClickListener.this.mSelectedItems) {
							Bandecos b = FullCardapioItemLongClickListener.this.outrosBandecos
									.get(item);
							intent.putExtra(
									String.format(
											IntentKeys.COMPARE_BANDECO_PATTERN_KEY,
											i++), b.id);
						}
						FullCardapioItemLongClickListener.this.context
								.startActivity(intent);
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						FullCardapioItemLongClickListener.this.mSelectedItems = null;
					}
				});
		builder.create().show();

		return true;
	}
}
