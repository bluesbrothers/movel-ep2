package fefzjon.ep2.bandejao.model;

import java.util.Date;

import fefzjon.ep2.persist.annotation.Column;
import fefzjon.ep2.persist.annotation.Table;
import fefzjon.ep2.persist.model.BaseEntity;

@Table(name = "cardapio_dia", version = 1)
public class CardapioDia implements BaseEntity {
	@Column(name = "data_baixado", version = 1)
	private Date	dataBaixado;

	@Column(name = "data_referente", version = 1)
	private Date	dataReferente;
	@Column(name = "semana_referente", version = 1)
	private Integer	semanaReferente;

	@Column(name = "tipo_refeicao", version = 1)
	private Integer	tipoRefeicao;

	@Column(name = "cardapio", version = 1)
	private String	cardapio;

	@Column(name = "kcal", version = 1)
	private Integer	kcal;

	@Column(name = "bandex_id", version = 1)
	private Integer	bandexId;

	public Date getDataReferente() {
		return this.dataReferente;
	}

	public void setDataReferente(final Date dataReferente) {
		this.dataReferente = dataReferente;
	}

	public Date getDataBaixado() {
		return this.dataBaixado;
	}

	public void setDataBaixado(final Date dataBaixado) {
		this.dataBaixado = dataBaixado;
	}

	public Integer getTipoRefeicao() {
		return this.tipoRefeicao;
	}

	public void setTipoRefeicao(final Integer tipoRefeicao) {
		this.tipoRefeicao = tipoRefeicao;
	}

	public String getCardapio() {
		return this.cardapio;
	}

	public void setCardapio(final String cardapio) {
		this.cardapio = cardapio;
	}

	public Integer getKcal() {
		return this.kcal;
	}

	public void setKcal(final Integer kcal) {
		this.kcal = kcal;
	}

	public Integer getSemanaReferente() {
		return this.semanaReferente;
	}

	public void setSemanaReferente(final Integer semanaReferente) {
		this.semanaReferente = semanaReferente;
	}

	public Integer getBandexId() {
		return this.bandexId;
	}

	public void setBandexId(final Integer bandexId) {
		this.bandexId = bandexId;
	}

}
