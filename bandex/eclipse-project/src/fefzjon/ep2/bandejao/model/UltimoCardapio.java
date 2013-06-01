package fefzjon.ep2.bandejao.model;

import java.util.Date;

import fefzjon.ep2.persist.annotation.Column;
import fefzjon.ep2.persist.annotation.Id;
import fefzjon.ep2.persist.annotation.Table;
import fefzjon.ep2.persist.model.BaseEntity;

@Table(name = "ultimo_cardapio", version = 1)
public class UltimoCardapio implements BaseEntity {
	@Id
	@Column(name = "id", version = 1)
	private Integer	id;

	@Column(name = "data_baixado", version = 1)
	private Date	dataBaixado;

	@Column(name = "semana_referente", version = 1)
	private Integer	semanaReferente;

	@Column(name = "bandex_id", version = 1)
	private Integer	bandexId;

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Date getDataBaixado() {
		return this.dataBaixado;
	}

	public void setDataBaixado(final Date dataBaixado) {
		this.dataBaixado = dataBaixado;
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
