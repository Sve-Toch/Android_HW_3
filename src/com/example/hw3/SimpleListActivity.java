package com.example.hw3;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.style.UpdateAppearance;
import android.util.Log;

public class SimpleListActivity extends ListActivity implements
LoaderManager.LoaderCallbacks<Cursor> {
	private final static String TAG = "SimpleListActivity";
	private static final String[] PROJECTION = new String[] { "_id", "username" };
	private static final int LOADER_ID = 1;
	private static final String myURI ="content://com.example.hw3/MyDateBase";
	private static final Uri CONTENT_URI= Uri.parse(myURI);
	private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
	private SimpleCursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		 // Инициализируем наш класс-обёртку
	  
	super.onCreate(savedInstanceState);
	  MyDataBase db = new MyDataBase(this);	
	    // База нам нужна для записи и чтения
	   db.open();
	   db.insertUser("rfhiuh", 18);
	   db.insertUser("jkij", 20);
	//   Cursor  cursor = db.getAllUsersCursor();
	  // startManagingCursor(cursor);
	   
	 //  sqh.close();
	String[] dataColumns = {"username"};
	int[] viewIDs = { R.id.textView };
	mAdapter = new SimpleCursorAdapter(this, R.layout.list_row,
	null, dataColumns, viewIDs,0);
	setListAdapter(mAdapter);
	mCallbacks = this;
	LoaderManager lm = getLoaderManager();
	lm.initLoader(LOADER_ID, null, mCallbacks);
	}
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onCreateLoader");		
		return new CursorLoader(SimpleListActivity.this,CONTENT_URI,PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onLoadFinished");	
		switch (loader.getId()) {
			case LOADER_ID:
			mAdapter.swapCursor(cursor);
			break;
			}
		}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onLoaderReset");	
		mAdapter.swapCursor(null);		
	}
	
	
}
