package fefzjon.ep2.bandejao.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fefzjon.ep2.bandejao.model.CardapioDia;

public class CardapioSemana {
	private Map<Date, Map<Integer, CardapioDia>>	cardapiosDias;
	private Integer									bandexId;

	public CardapioSemana(final int bandexId) {
		this.cardapiosDias = new HashMap<Date, Map<Integer, CardapioDia>>();
		this.bandexId = bandexId;
	}

	public void put(final Date date, final int tipoRefeicao, final CardapioDia cardapioDia) {
		Map<Integer, CardapioDia> refeicoesDeHoje = this.cardapiosDias.get(date);
		if (refeicoesDeHoje == null) {
			refeicoesDeHoje = new HashMap<Integer, CardapioDia>();
			this.cardapiosDias.put(date, refeicoesDeHoje);
		}
		refeicoesDeHoje.put(tipoRefeicao, cardapioDia);
	}

	public CardapioDia get(final Date date, final int tipoRefeicao) {
		Map<Integer, CardapioDia> map = this.cardapiosDias.get(date);
		return map == null ? null : map.get(tipoRefeicao);
	}

	public Integer getBandexId() {
		return this.bandexId;
	}

}
