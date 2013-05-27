package fefzjon.ep2.rssfeed.model;

public class FeedItem {
	private String	description;
	private String	link;
	private String	title;
	private String	categoria;

	private String	dataPalestra;
	private String	dataBaixado;

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

	public String getDataBaixado() {
		return this.dataBaixado;
	}

	public void setDataBaixado(final String dataBaixado) {
		this.dataBaixado = dataBaixado;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(final String categoria) {
		this.categoria = categoria;
	}

}
