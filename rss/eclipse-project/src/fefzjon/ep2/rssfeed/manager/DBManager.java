package fefzjon.ep2.rssfeed.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import fefzjon.ep2.rssfeed.model.BaseEntity;
import fefzjon.ep2.rssfeed.model.FeedItem;

public class DBManager {
	private DatabaseHelper		mDbHelper;
	private SQLiteDatabase		mDb;

	private static final String	dbName		= "FEFZJON_PALESTRAS_IME";
	private static final int	dbVersion	= 1;

	private static Context		mCtx		= null;

	private static List<String>	dbInit;

	public static void registerContext(final Context ctx) {
		DBManager.mCtx = ctx;
	}

	private static void registerModels() {
		DBManager.dbInit = new ArrayList<String>();

		DBManager.dbInit.addAll(FeedItem.createDBQuery);
	}

	private DBManager() {

	}

	public static DBManager open() throws SQLException {
		DBManager.registerModels();
		DBManager dbManager = new DBManager();
		dbManager.mDbHelper = new DatabaseHelper(DBManager.mCtx, DBManager.dbName, DBManager.dbVersion,
				DBManager.dbInit);
		dbManager.mDb = dbManager.mDbHelper.getWritableDatabase();
		return dbManager;
	}

	public long create(final BaseEntity entity) {
		return this.mDb.insert(entity.getTableName(), null, entity.createValues());
	}

	public boolean update(final BaseEntity entity) {
		return this.mDb.update(entity.getTableName(), entity.updateValues(), entity.getConstraintThisEntity(), null) > 0;
	}

	public boolean delete(final BaseEntity entity) {
		return this.mDb.delete(entity.getTableName(), entity.getConstraintThisEntity(), null) > 0;
	}

	public <S extends BaseEntity> List<S> getAll(final S representante) {
		Cursor cursor = this.mDb.query(representante.getTableName(), null, null, null, null, null, null, null);
		List<S> list = new ArrayList<S>();
		if (cursor != null) {
			cursor.moveToFirst();
			int count = cursor.getCount();
			for (int i = 0; i < count; i++) {
				S entity = representante.createFromCursor(cursor);
				cursor.moveToNext();
				list.add(entity);
			}
		}

		return list;
	}
}
