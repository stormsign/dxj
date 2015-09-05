package com.dxj.student.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dxj.student.application.MyApplication;


/**
 * User: qii Date: 12-7-30
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper singleton = null;

    private static final String DATABASE_NAME = "dxjteacher.db";

    private static final int DATABASE_VERSION = 1;

//	public static final String TABLE_NAME = "account_table";
//	public static final  String ID ="id"; //用户id/
//	public static final String NICKNAME="nickName";//用户昵称/
//	public static final String HEADURL="headUrl";//头像/
//	public static final String SET="sex";//性别/
//	public static final String TYPE="type";//学生or家长/
//	public static final String MOBILE="mobile";//手机号码//
//	public static final String HOROSCOPE="horoscope";//星座/
//	public static final String GRADE="grade";//年级
//	public static final String SCHOOL="school";//学校/
//	public static final String LIVINGCITY="livingCity";//现居城市
    static final String CREATE_ACCOUNT_TABLE_SQL = "create table " + AccountTable.TABLE_NAME + "(" + AccountTable.ID + " text primary key,"
	    + AccountTable.NICKNAME + " text," + AccountTable.SET + " text," + AccountTable.TYPE + " text,"  + AccountTable.MOBILE
	    + " text," +AccountTable.HEADURL+" text," + AccountTable.SCHOOL + " text," +AccountTable.NAME+ " text,"+ AccountTable.LIVINGCITY + " text," +AccountTable.GRADE+" text,"+ AccountTable.HOROSCOPE + " text" +");";

    DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

	db.execSQL(CREATE_ACCOUNT_TABLE_SQL);

	// createOtherTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	Log.i("TAG", "oldVersion=" + oldVersion);
	if (oldVersion > 1) {
	    deleteAllTable(db);
	    onCreate(db);
	}
	if (oldVersion <= 36) {
	    // Upgrade36to37.upgrade(db);
	}

	if (oldVersion <= 35) {
	    // Upgrade35to36.upgrade(db);
	}

	if (oldVersion <= 34) {
	    // upgrade34To35(db);
	}

	if (oldVersion <= 33) {
	    deleteAllTable(db);
	    onCreate(db);
	}

    }

    public static synchronized DatabaseHelper getInstance() {
	if (singleton == null) {
	    singleton = new DatabaseHelper(MyApplication.getInstance());
	}
	return singleton;
    }

    private void deleteAllTable(SQLiteDatabase db) {
	db.execSQL("DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME);

	// deleteAllTableExceptAccount(db);

    }

    // private void upgrade34To35(SQLiteDatabase db) {
    // db.execSQL(CREATE_NOTIFICATION_TABLE_SQL);
    // }
}
