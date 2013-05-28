package fefzjon.ep2.rssfeed.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class FeedItem implements BaseEntity {
	public static final String	tableName			= "feedItem";

	private static final String	ID_KEY				= "id";
	private Long				id;

	private static final String	DESCRIPTION_KEY		= "description";
	private String				description;
	private static final String	LINK_KEY			= "link";
	private String				link;
	private static final String	TITLE_KEY			= "title";
	private String				title;
	private static final String	CATEGORY_KEY		= "category";
	private String				category;

	private static final String	DATA_PALESTRA_KEY	= "data_palestra";
	private String				dataPalestra;
	private static final String	DATA_BAIXADO_KEY	= "data_baixado";
	private String				dataBaixado;

	public static List<String>	createDBQuery;

	static {
		FeedItem.createDBQuery = new ArrayList<String>();

		StringBuilder builder = new StringBuilder().append("CREATE TABLE ");
		builder.append(FeedItem.tableName + "(");
		builder.append(FeedItem.ID_KEY + " integer primary key autoincrement, ");
		builder.append(FeedItem.DESCRIPTION_KEY + " varchar(255), ");
		builder.append(FeedItem.LINK_KEY + " varchar(255), ");
		builder.append(FeedItem.TITLE_KEY + " varchar(255), ");
		builder.append(FeedItem.CATEGORY_KEY + " varchar(255), ");
		builder.append(FeedItem.DATA_PALESTRA_KEY + " varchar(255), ");
		builder.append(FeedItem.DATA_BAIXADO_KEY + " varchar(255) NOT NULL);");

		FeedItem.createDBQuery.add(builder.toString());
	}

	@Override
	public ContentValues createValues() {
		ContentValues content = new ContentValues();
		content.put(FeedItem.DESCRIPTION_KEY, this.description);
		content.put(FeedItem.LINK_KEY, this.link);
		content.put(FeedItem.TITLE_KEY, this.title);
		content.put(FeedItem.CATEGORY_KEY, this.category);
		content.put(FeedItem.DATA_PALESTRA_KEY, this.dataPalestra);
		content.put(FeedItem.DATA_BAIXADO_KEY, this.dataBaixado);
		return content;
	}

	@Override
	public ContentValues updateValues() {
		ContentValues content = this.createValues();
		content.put(FeedItem.ID_KEY, this.id);
		return content;
	}

	@Override
	public String getConstraintThisEntity() {
		return FeedItem.ID_KEY + "=" + this.id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FeedItem createFromCursor(final Cursor cursor) {
		FeedItem item = new FeedItem();
		item.setId(cursor.getLong(cursor.getColumnIndex(FeedItem.ID_KEY)));
		item.setTitle(cursor.getString(cursor.getColumnIndex(FeedItem.TITLE_KEY)));
		item.setLink(cursor.getString(cursor.getColumnIndex(FeedItem.LINK_KEY)));
		item.setDescription(cursor.getString(cursor.getColumnIndex(FeedItem.DESCRIPTION_KEY)));
		item.setCategory(cursor.getString(cursor.getColumnIndex(FeedItem.CATEGORY_KEY)));
		item.setDataBaixado(cursor.getString(cursor.getColumnIndex(FeedItem.DATA_BAIXADO_KEY)));
		item.setDataPalestra(cursor.getString(cursor.getColumnIndex(FeedItem.DATA_PALESTRA_KEY)));
		return item;
	}

	@Override
	public String getTableName() {
		return FeedItem.tableName;
	}

	@Override
	public List<String> getCreateDBQuery() {
		return FeedItem.createDBQuery;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
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

	public String getDataBaixado() {
		return this.dataBaixado;
	}

	public void setDataBaixado(final String dataBaixado) {
		this.dataBaixado = dataBaixado;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

}
