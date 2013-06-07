package fefzjon.ep2.bandejao.listeners;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import fefzjon.ep2.bandejao.manager.DialogManager;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.Bandecos;

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
		this.mSelectedItems = new ArrayList<Integer>();

		AlertDialog.Builder builder = DialogManager.buildAlertDialog(
				this.context, this.bandecoId, this.mSelectedItems,
				this.outrosBandecos, cardapioDia.getDataReferente(),
				cardapioDia.getTipoRefeicao());
		builder.create().show();

		return true;
	}

}
