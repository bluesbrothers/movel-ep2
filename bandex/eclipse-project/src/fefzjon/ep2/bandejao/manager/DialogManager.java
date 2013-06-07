package fefzjon.ep2.bandejao.manager;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import fefzjon.ep2.bandejao.CompareCardapioActivity;
import fefzjon.ep2.bandejao.R;
import fefzjon.ep2.bandejao.utils.Bandecos;
import fefzjon.ep2.bandejao.utils.IntentKeys;

public class DialogManager {

	public static AlertDialog.Builder buildAlertDialog(final Context context,
			final int bandecoId, final List<Integer> mSelectedItems,
			final List<Bandecos> outrosBandecos, final Date dataReferente,
			final int tipoRefeicao) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		CharSequence[] outrosBandecosNomes = new CharSequence[outrosBandecos
				.size()];

		int b_index = 0;
		for (Bandecos b : outrosBandecos) {
			outrosBandecosNomes[b_index++] = (b.nome);
		}

		builder.setTitle(R.string.comparar_com_bandejao);
		builder.setMultiChoiceItems(outrosBandecosNomes, null,
				new OnMultiChoiceClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which, final boolean isChecked) {
						if (isChecked) {
							mSelectedItems.add(which);
						} else if (mSelectedItems.contains(which)) {
							mSelectedItems.remove(Integer.valueOf(which));
						}
					}
				});
		builder.setPositiveButton(R.string.do_comparar,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						Intent intent = new Intent(context,
								CompareCardapioActivity.class);
						intent.putExtra(IntentKeys.QTD_COMPARE_BANDECO,
								mSelectedItems.size() + 1); // Os bandecos alvo
															// para
															// comparação + o
															// bandeco base
						intent.putExtra(IntentKeys.DATA_CARDAPIO, dataReferente);
						intent.putExtra(IntentKeys.TIPO_REFEICAO, tipoRefeicao);
						int i = 0;
						intent.putExtra(String.format(
								IntentKeys.COMPARE_BANDECO_PATTERN_KEY, i++),
								bandecoId);
						for (int item : mSelectedItems) {
							Bandecos b = outrosBandecos.get(item);
							intent.putExtra(
									String.format(
											IntentKeys.COMPARE_BANDECO_PATTERN_KEY,
											i++), b.id);
						}
						context.startActivity(intent);
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
					}
				});
		return builder;
	}
}
