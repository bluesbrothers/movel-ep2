package fefzjon.ep2.bandejao.model;

import java.util.Date;

import fefzjon.ep2.persist.model.BaseEntity;

public class CardapioDia implements BaseEntity {
	private Date	dataReferente;
	private Date	dataBaixado;

	private Integer	tipoRefeicao;

	private String	cardapio;

	private Integer	kcal;
}
