package com.dxj.student.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.dxj.student.bean.UserBean;
import com.dxj.student.utils.StringUtils;


/**
 * User
 */
public class AccountDBTask {

    private AccountDBTask() {

    }

    private static SQLiteDatabase getWsd() {
	DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
	return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getRsd() {
	DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
	return databaseHelper.getReadableDatabase();
    }

    /**
     * 保存用户信息
     */
    public static void saveUserBean(UserBean userBean) {
	if (getWsd().isOpen()) {
	    ContentValues values = new ContentValues();
	    values.put(AccountTable.ID, userBean.getUserInfo().getId());
	    values.put(AccountTable.GRADE, String.valueOf(userBean.getUserInfo().getGrade()));
	    values.put(AccountTable.HEADURL, userBean.getUserInfo().getHeadUrl());
	    values.put(AccountTable.HOROSCOPE, userBean.getUserInfo().getHoroscope());
	    values.put(AccountTable.LIVINGCITY, userBean.getUserInfo().getLivingCity());
	    values.put(AccountTable.MOBILE, userBean.getUserInfo().getMobile());
	    values.put(AccountTable.NICKNAME, userBean.getUserInfo().getNickName());
	    values.put(AccountTable.TYPE, userBean.getUserInfo().getType());
	    values.put(AccountTable.SET, userBean.getUserInfo().getSex());
	    values.put(AccountTable.SCHOOL, userBean.getUserInfo().getSchool());
	    values.put(AccountTable.NAME, userBean.getUserInfo().getName());

//		if (userBean.getUserInfo().getSolveLabel() != null && userBean.getUserInfo().getSolveLabel().size() > 0) {
//			StringBuffer strBuffer = new StringBuffer();
//			for (int i = 0; i <userBean.getUserInfo().getSolveLabel().size(); i++) {
//				if (i != userBean.getUserInfo().getSolveLabel().size() - 1) {
//					strBuffer.append(userBean.getUserInfo().getSolveLabel().get(i)).append(",");
//				} else {
//					strBuffer.append(userBean.getUserInfo().getSolveLabel().get(i));
//				}
//			}
//			Log.i("TAG", "strBuffer=" + strBuffer.toString());
//			values.put(AccountTable.SOLVELABEL, strBuffer.toString());
//
//		}
	    getWsd().insert(AccountTable.TABLE_NAME, null, values);
	}

    }


    /**
     * 查询用户信息
     * 
     * @return
     */
    public static UserBean getAccount() {

	String sql = "select * from " + AccountTable.TABLE_NAME;
	/** 获取游标 */
	Cursor c = getRsd().rawQuery(sql, null);
	if (c.moveToNext()) {
	    UserBean account = new UserBean();
		UserBean.UserInfo userinfo = account.new UserInfo();
	    int colid = c.getColumnIndex(AccountTable.ID);
		userinfo.setId(c.getString(colid));

	    colid = c.getColumnIndex(AccountTable.GRADE);
	    userinfo.setGrade(c.getString(colid));

		colid = c.getColumnIndex(AccountTable.HEADURL);
		userinfo.setHeadUrl(c.getString(colid));


		colid = c.getColumnIndex(AccountTable.HOROSCOPE);
		userinfo.setHoroscope(c.getString(colid));


		colid = c.getColumnIndex(AccountTable.LIVINGCITY);
		userinfo.setLivingCity(c.getString(colid));

		colid = c.getColumnIndex(AccountTable.MOBILE);
		userinfo.setMobile(c.getString(colid));


		colid = c.getColumnIndex(AccountTable.NICKNAME);
		userinfo.setNickName(c.getString(colid));


		colid = c.getColumnIndex(AccountTable.SCHOOL);
		userinfo.setSchool(c.getString(colid));


		colid = c.getColumnIndex(AccountTable.SET);
		userinfo.setSex(c.getString(colid));
//		colid = c.getColumnIndex(AccountTable.TYPE);
//		String string =c.getString(colid);
//		if (StringUtils.isEmpty(string))
//		userinfo.setType(Long.valueOf(string));

		colid = c.getColumnIndex(AccountTable.NAME);
		userinfo.setName(c.getString(colid));
//		colid = c.getColumnIndex(AccountTable.TYPE);
//		if (c.getString(colid)!=null)
//		userinfo.setType(Long.valueOf(c.getString(colid)));


//		colid = c.getColumnIndex(AccountTable.SOLVELABEL);
//		String strSolveLabel = c.getString(colid);
//		Log.i("TAG", "account=" + userinfo.getNickName());
//		if (!StringUtils.isEmpty(strSolveLabel)) {
//			List<String> strList = new ArrayList<>();
//			String[] array = strSolveLabel.split(Pattern.quote(","));
//			for (int i = 0; i < array.length; i++) {
//				Log.i("TAG", "strList");
//				strList.add(array[i]);
//			}
//			userinfo.setSolveLabel(strList);
//		}
		account.setUserInfo(userinfo);
	    return account;
	}
	return null;
    }
	public static void updateNickName(String uid,String nickName,String bigLetter ) {
		if (getWsd().isOpen()) {
			ContentValues values = new ContentValues();
			values.put(bigLetter, nickName);
			String[] whereArgs = { uid };
			getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.ID + "=?", whereArgs);
		}
	}

    public static void clear() {
	String sql = "delete from " + AccountTable.TABLE_NAME;

	getWsd().execSQL(sql);
    }
}
