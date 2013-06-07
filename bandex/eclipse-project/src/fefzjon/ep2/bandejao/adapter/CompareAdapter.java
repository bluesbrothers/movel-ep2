package fefzjon.ep2.bandejao.adapter;

import java.util.List;

import android.content.Context;
import fefzjon.ep2.bandejao.model.CardapioDia;
import fefzjon.ep2.bandejao.utils.Bandecos;

public class CompareAdapter extends RefeicoesAdapter {

	public CompareAdapter(final Context context, final List<CardapioDia> listaCardapio) {
		super(context, listaCardapio);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String generateTitulo(final CardapioDia cDia) {
		int bandexId = cDia.getBandexId();
		return Bandecos.getById(bandexId).nome;
	}
}
