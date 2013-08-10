package com.guest.cnbeta.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDBHelper extends SQLiteOpenHelper {

	public BaseDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE 'article' ( 'articleid' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 'fromid' VARCHAR ( 50 ) NOT NULL , 'title' VARCHAR ( 255 ) NOT NULL , 'info' TEXT, 'avatar' VARCHAR ( 255 ) NOT NULL ) ;");
		db.execSQL("CREATE UNIQUE INDEX article_fromid ON 'article'('fromid');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
