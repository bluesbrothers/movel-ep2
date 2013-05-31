package fefzjon.ep2.persist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fefzjon.ep2.exceptions.EpDoisException;
import fefzjon.ep2.persist.model.BaseEntity;

public class DBManager {
	private DatabaseHelper								mDbHelper;
	private SQLiteDatabase								mDb;

	private static Context								mCtx	= null;

	private static List<Class<? extends BaseEntity>>	classes	= null;

	public static void initializeModule(final Context ctx, final String dbName, final int version) {
		DBManager.mCtx = ctx;
		DBManager dbManager = new DBManager();
		dbManager.mDbHelper = new DatabaseHelper(DBManager.mCtx, dbName, version, DBManager.classes);
		dbManager.mDb = dbManager.mDbHelper.getWritableDatabase();

		DBManager.instance = dbManager;
	}

	public static boolean isInitialized() {
		return DBManager.instance != null;
	}

	public static void registerModel(final Class<? extends BaseEntity> clazz) {
		if (DBManager.classes == null) {
			DBManager.classes = new ArrayList<Class<? extends BaseEntity>>();
		}
		DBManager.classes.add(clazz);
	}

	private DBManager() {

	}

	private static DBManager	instance	= null;

	public static DBManager getInstance() throws EpDoisException {
		if (DBManager.instance == null) {
			throw new EpDoisException("You must initialize DBManager first");
		}
		return DBManager.instance;
	}

	public long create(final BaseEntity entity) {
		String tableName = DBReflectionHelper.getTableName(entity);

		if (tableName == null) {
			return -1;
		}

		return this.mDb.insert(tableName, null, DBReflectionHelper.createValues(entity));
	}

	public boolean update(final BaseEntity entity) {
		String tableName = DBReflectionHelper.getTableName(entity);

		if (tableName == null) {
			return false;
		}

		return this.mDb.update(tableName, DBReflectionHelper.updateValues(entity),
				DBReflectionHelper.constraintEntity(entity), null) > 0;
	}

	public boolean delete(final BaseEntity entity) {
		String tableName = DBReflectionHelper.getTableName(entity);

		if (tableName == null) {
			return false;
		}
		return this.mDb.delete(tableName, DBReflectionHelper.constraintEntity(entity), null) > 0;
	}

	public <S extends BaseEntity> boolean deleteAll(final S representante) {
		String tableName = DBReflectionHelper.getTableName(representante);
		if (tableName == null) {
			return false;
		}
		return this.mDb.delete(tableName, null, null) > 0;
	}

	public <S extends BaseEntity> List<S> getAll(final S representante) {
		List<S> list = new ArrayList<S>();

		String tableName = DBReflectionHelper.getTableName(representante);

		if (tableName != null) {
			Cursor cursor = this.mDb.query(tableName, null, null, null, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				int count = cursor.getCount();
				for (int i = 0; i < count; i++) {
					try {
						S entity = DBReflectionHelper.createFromCursor(representante, cursor);
						list.add(entity);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cursor.moveToNext();
				}
			}
		}

		return list;
	}

}
