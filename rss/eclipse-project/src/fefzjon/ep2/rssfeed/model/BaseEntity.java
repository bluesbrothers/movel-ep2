package fefzjon.ep2.rssfeed.model;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public interface BaseEntity {
	public ContentValues createValues();

	public ContentValues updateValues();

	public String getConstraintThisEntity();

	public String getTableName();

	public List<String> getCreateDBQuery();

	public <S extends BaseEntity> S createFromCursor(Cursor cursor);
}
