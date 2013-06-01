package fefzjon.ep2.bandejao.utils;

import java.util.ArrayList;
import java.util.List;

import fefzjon.ep2.bandejao.model.CardapioDia;

public class CardapioSemana {
	private List<CardapioDia>	cardapiosDias;

	public CardapioSemana() {
		this.cardapiosDias = new ArrayList<CardapioDia>();
	}

	public void add(final CardapioDia cardapioDia) {
		this.cardapiosDias.add(cardapioDia);
	}

	public CardapioDia get(final int index) {
		return this.cardapiosDias.get(index);
	}
}
