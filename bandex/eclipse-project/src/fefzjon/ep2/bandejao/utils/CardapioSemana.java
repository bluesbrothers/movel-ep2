package fefzjon.ep2.bandejao.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fefzjon.ep2.bandejao.model.CardapioDia;

public class CardapioSemana implements Iterable<CardapioDia> {
	private Map<Date, Map<Integer, CardapioDia>>	cardapiosDias;
	private Integer									bandexId;

	private Integer									semanaReferente;

	private int										count;

	public CardapioSemana(final int bandexId) {
		this.cardapiosDias = new HashMap<Date, Map<Integer, CardapioDia>>();
		this.bandexId = bandexId;
		this.count = 0;
	}

	public void put(final Date date, final int tipoRefeicao, final CardapioDia cardapioDia) {
		Map<Integer, CardapioDia> refeicoesDeHoje = this.cardapiosDias.get(date);
		if (refeicoesDeHoje == null) {
			refeicoesDeHoje = new HashMap<Integer, CardapioDia>();
			this.cardapiosDias.put(date, refeicoesDeHoje);
		}
		refeicoesDeHoje.put(tipoRefeicao, cardapioDia);
		this.count++;
	}

	public CardapioDia get(final Date date, final int tipoRefeicao) {
		Map<Integer, CardapioDia> map = this.cardapiosDias.get(date);
		return map == null ? null : map.get(tipoRefeicao);
	}

	public Integer getBandexId() {
		return this.bandexId;
	}

	@Override
	public Iterator<CardapioDia> iterator() {
		return this.asList().iterator();
	}

	public List<CardapioDia> asList() {
		List<CardapioDia> list = new ArrayList<CardapioDia>();
		for (Map<Integer, CardapioDia> map : this.cardapiosDias.values()) {
			list.addAll(map.values());
		}
		return list;
	}

	public Integer getSemanaReferente() {
		return this.semanaReferente;
	}

	public void setSemanaReferente(final Integer semanaReferente) {
		this.semanaReferente = semanaReferente;
	}

	public int count() {
		return this.count;
	}

}
