package fefzjon.ep2.rssfeed.manager;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private List<String>	databaseCreate;

	public DatabaseHelper(final Context context, final String databaseName, final int version,
			final List<String> dbCreate) {
		super(context, databaseName, null, version);
		this.databaseCreate = dbCreate;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		for (int i = 0; i < this.databaseCreate.size(); i++) {
			db.execSQL(this.databaseCreate.get(i));
		}
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

	}
}
