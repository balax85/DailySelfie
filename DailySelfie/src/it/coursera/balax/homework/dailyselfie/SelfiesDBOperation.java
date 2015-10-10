package it.coursera.balax.homework.dailyselfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SelfiesDBOperation
{
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_PICTURE_PATH = "imagePath";

	private static final String DATABASE_NAME = "SelfiesDB";
	private static final String DATABASE_TABLE = "selfies";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table selfies (_id integer primary key autoincrement, "
			+ "name text not null, imagePath text not null);";

	private final Context context;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public SelfiesDBOperation(Context ctx) {
		context = ctx;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long insertSelfie(String name, String picturePath) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_PICTURE_PATH, picturePath);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteSelfie(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean updateSelfie(long rowId, String name, String picturePath) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_PICTURE_PATH, picturePath);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteAllSelfies() {
		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public Cursor getAllSelfies() {
		String orderBy =  KEY_ROWID + " DESC";
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_PICTURE_PATH }, null, null, null, null, orderBy);
	}

	public Cursor getSelfie(long rowId) throws SQLException {
		Cursor cursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_PICTURE_PATH }, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		
		if(cursor != null) {
			cursor.moveToFirst();
		}
		
		return cursor;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS selfies");
			onCreate(db);
		}
	}
}