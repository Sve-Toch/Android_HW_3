package com.example.hw3;

import javax.swing.text.AbstractDocument.Content;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBase extends SQLiteOpenHelper {
	private final static String TAG = "MyDataBase";
	// константы для конструктора
	private static final String DATABASE_NAME = "my_database.db";
	private static final int DATABASE_VERSION = 2;
	public static final String TABLE_NAME = "user_table";
	// индекс
	public static final String KEY_ID = "_id";
	//имя и индес столбца
	public static final String USER_NAME = "username";
	public static final int NAME_COLUMN=1;
	public static final String USER_AGE = "userage";
	// создание новой бд
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ MyDataBase.TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ USER_NAME + " text not null, "+USER_AGE+" Integer);";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	private SQLiteDatabase db;
    private final Context mContext;
    private myDbHelper dbHelper;
    
	public MyDataBase(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.v(TAG, "constructor called");			
        mContext = context;
        dbHelper =new myDbHelper(context, TABLE_NAME, null, DATABASE_VERSION);
        
	}
	public void open() throws SQLiteException
	{
		try {
			db= dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			e.printStackTrace();
			Log.v(TAG, "Error ");
			db=dbHelper.getReadableDatabase();
		}
	}
	public void close()
	{
		db.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Обновление базы данных с версии " + oldVersion
				+ " до версии " + newVersion + ", которое удалит все старые данные");
		// Удаляем предыдущую таблицу при апгрейде
		db.execSQL(SQL_DELETE_ENTRIES);
		// Создаём новый экземпляр таблицы
		onCreate(db);
	}
	public long insertUser(String username, Integer age)
	{
		ContentValues newUserValues= new ContentValues();
		newUserValues.put(USER_NAME, username);
		newUserValues.put(USER_AGE, age);
		return db.insert(TABLE_NAME, null, newUserValues);
	}
	public boolean removeUser(long _rowIndex)
	{
		return db.delete(TABLE_NAME,KEY_ID+"="+_rowIndex, null)>0;
	}
	public boolean updeateUseAge(long _rowIndex,Integer _age)
	{
		ContentValues newUserValues= new ContentValues();
		newUserValues.put(USER_AGE, _age);
		return db.update(TABLE_NAME, newUserValues, KEY_ID+"="+_rowIndex, null)>0;

	}
	public Cursor getAllUsersCursor()
	{ 
		return db.query(TABLE_NAME, new String[] {KEY_ID, USER_NAME, USER_AGE }, null, null, null, null, null);
	}
	public Cursor setCursorUser(long _rowIndex)throws SQLException
	{
		Cursor result =db.query(true,TABLE_NAME,
				new String[] {KEY_ID, USER_NAME, USER_AGE } ,
				KEY_ID+"="+_rowIndex, null, null, null, null, null);
		if ((result.getCount()==0)||!result.moveToFirst())
		{
			throw new SQLException("ERRor"+_rowIndex);
		}
		return result;
		
	}
	private static class myDbHelper extends SQLiteOpenHelper {

	    public myDbHelper(Context context, String name, 
	                      CursorFactory factory, int version) {
	      super(context, name, factory, version);
	    }

	   //если  бд не найдена на носители, то новую
	    @Override
	    public void onCreate(SQLiteDatabase _db) {
	      _db.execSQL(DATABASE_CREATE);
	    }
	    //если версии баз данных не совпадают

	    @Override
	    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
	      Log.w("TaskDBAdapter", "Upgrading from version " + 
	                             _oldVersion + " to " +
	                             _newVersion + ", which will destroy all old data");
	        
	     _db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	      onCreate(_db);
	    }
	  }

}
