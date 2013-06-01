package fefzjon.ep2.rssfeed.model;

import java.util.Date;

import fefzjon.ep2.persist.annotation.Column;
import fefzjon.ep2.persist.annotation.Id;
import fefzjon.ep2.persist.annotation.NotNull;
import fefzjon.ep2.persist.annotation.Table;
import fefzjon.ep2.persist.model.BaseEntity;

@Table(name = "feedItem", version = 1)
public class FeedItem implements BaseEntity {

	@Id
	@Column(name = "id", version = 1)
	private Integer	id;

	@Column(name = "description", version = 1)
	private String	description;

	@Column(name = "link", version = 1)
	private String	link;

	@Column(name = "title", version = 1)
	private String	title;

	@Column(name = "category", version = 1)
	private String	category;

	@Column(name = "data_palestra", version = 1)
	private String	dataPalestra;

	@NotNull
	@Column(name = "data_baixado", version = 1)
	private Date	dataBaixado;

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDataPalestra() {
		return this.dataPalestra;
	}

	public void setDataPalestra(final String dataPalestra) {
		this.dataPalestra = dataPalestra;
	}

	public Date getDataBaixado() {
		return this.dataBaixado;
	}

	public void setDataBaixado(final Date dataBaixado) {
		this.dataBaixado = dataBaixado;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

}
