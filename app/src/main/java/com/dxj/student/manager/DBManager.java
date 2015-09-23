package com.dxj.student.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dxj.student.bean.Notification;
import com.dxj.student.db.DatabaseHelper;
import com.dxj.student.db.dao.NoticeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/9/15.
 */
public class DBManager {

    private static DBManager dbManager = new DBManager();
    private DatabaseHelper dbHelper;

    public final static int NOTICE_READ = 1;
    public final static int NOTICE_UNREAD = 0;

    public static synchronized DBManager getInstance(){
        return dbManager;
    }

    public void onInit(Context context){
        dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * 根据id获取一条通知
     * @param id
     * @return
     */
    public synchronized Notification getNoticeById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + NoticeDao.NOTICE_TABLE_NAME
                + " where " + NoticeDao.NOTICE_COLUMN_ID + " = ?", new String[]{id + ""});
        Notification notification = new Notification();
        if (cursor.moveToNext()){
            notification.setId(cursor.getInt(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_ID)));
            notification.setTitle(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_TITLE)));
            notification.setContent(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_CONTENT)));
//            read为0则未读 false，1为已读 true
            notification.setRead(cursor.getInt(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_READ)) == NOTICE_UNREAD ? false : true);
            notification.setTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_TIME))));
            notification.setActivity(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_ACTIVITY)));
            notification.setExtra(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_EXTRA)));
        }
        return notification;
    }

    /**
     * 获取所有通知,按时间由新到旧排序
     * @return
     */
    public synchronized List<Notification> getAllNoticeList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + NoticeDao.NOTICE_TABLE_NAME
                + " order by " + NoticeDao.NOTICE_COLUMN_TIME
                + " desc "
                , null);
        List<Notification> list = new ArrayList<>();
        if (db.isOpen()) {
            while (cursor.moveToNext()) {
                Notification notification = new Notification();
                notification.setId(cursor.getInt(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_ID)));
                notification.setTitle(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_TITLE)));
                notification.setContent(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_CONTENT)));
//            read为0则未读 false，1为已读 true
                notification.setRead(cursor.getInt(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_READ)) == 0 ? false : true);
                notification.setTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_TIME))));
                notification.setActivity(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_ACTIVITY)));
                notification.setExtra(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_EXTRA)));
                list.add(notification);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 添加一条消息
     * @param title
     * @param content
     * @param read
     * @param time
     * @return
     */
    public synchronized long insertNotice(String title, String content, int read, String time, String activity, String extra){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoticeDao.NOTICE_COLUMN_TITLE, title);
        values.put(NoticeDao.NOTICE_COLUMN_CONTENT, content);
        values.put(NoticeDao.NOTICE_COLUMN_READ, NOTICE_UNREAD);
        values.put(NoticeDao.NOTICE_COLUMN_TIME, time);
        if (activity != null) {
            values.put(NoticeDao.NOTICE_COLUMN_ACTIVITY, activity);
        }
        if (extra != null) {
            values.put(NoticeDao.NOTICE_COLUMN_EXTRA, extra);
        }
        return  db.insert(NoticeDao.NOTICE_TABLE_NAME, null, values);
    }

    /**
     *  将一条消息设为已读
     */
    public synchronized int updateNoticeReadState(Notification notification){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoticeDao.NOTICE_COLUMN_ID, notification.getId());
        values.put(NoticeDao.NOTICE_COLUMN_TITLE, notification.getTitle());
        values.put(NoticeDao.NOTICE_COLUMN_CONTENT, notification.getContent());
        values.put(NoticeDao.NOTICE_COLUMN_READ, NOTICE_READ);
        values.put(NoticeDao.NOTICE_COLUMN_TIME, String.valueOf(notification.getTime()));
        if (notification.getActivity()!=null && !notification.getActivity().equals("")) {
            values.put(NoticeDao.NOTICE_COLUMN_ACTIVITY, notification.getActivity());
        }
        if (notification.getExtra()!=null && !notification.getExtra().equals("")) {
            values.put(NoticeDao.NOTICE_COLUMN_EXTRA, notification.getExtra());
        }

        return db.update(NoticeDao.NOTICE_TABLE_NAME, values, NoticeDao.NOTICE_COLUMN_ID + " = ?", new String[]{String.valueOf((notification.getId()))});
    }

    /**
     * 获取未读消息数
     * @return
     */
    public synchronized  int getUnreadNoticeCount(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + NoticeDao.NOTICE_TABLE_NAME
                + " where read = ?"
                , new String[]{String.valueOf(NOTICE_UNREAD)});
        List<Notification> list = new ArrayList<>();
        if (db.isOpen()) {
            while (cursor.moveToNext()) {
                Notification notification = new Notification();
                notification.setId(cursor.getInt(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_ID)));
                notification.setTitle(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_TITLE)));
                notification.setContent(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_CONTENT)));
//            read为0则未读 false，1为已读 true
                notification.setRead(cursor.getInt(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_READ)) == NOTICE_UNREAD ? false : true);
                notification.setTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_TIME))));
                notification.setActivity(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_ACTIVITY)));
                notification.setExtra(cursor.getString(cursor.getColumnIndex(NoticeDao.NOTICE_COLUMN_EXTRA)));
                list.add(notification);
            }
            cursor.close();
        }
        return list.size();
    }

    /**
     * 删除所有通知
     */
    public synchronized void deleteALlNotices(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete(NoticeDao.NOTICE_TABLE_NAME, null, null);
        db.execSQL("DELETE FROM " + NoticeDao.NOTICE_TABLE_NAME);
        db.execSQL("update sqlite_sequence set seq=0 where name='"+NoticeDao.NOTICE_TABLE_NAME+"'");

    }

    public synchronized int deleteNoticeById(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(NoticeDao.NOTICE_TABLE_NAME, NoticeDao.NOTICE_COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});
    }

}
