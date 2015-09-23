package com.dxj.student.db.dao;

import android.content.Context;

import com.dxj.student.bean.Notification;
import com.dxj.student.manager.DBManager;

import java.util.List;

/**
 * Created by khb on 2015/9/15.
 */
public class NoticeDao {

    public final static String NOTICE_TABLE_NAME = "notices";
    public final static String NOTICE_COLUMN_ID = "id";
    public final static String NOTICE_COLUMN_TITLE = "title";
    public final static String NOTICE_COLUMN_CONTENT = "content";
    public final static String NOTICE_COLUMN_TIME = "time";
    public final static String NOTICE_COLUMN_READ = "read";
    public final static String NOTICE_COLUMN_ACTIVITY = "activity";
    public final static String NOTICE_COLUMN_EXTRA = "extra";

    public NoticeDao(Context context){
        DBManager.getInstance().onInit(context);
    }

    public void saveNotice(String title, String content, int read, String time, String activity, String extra){
        DBManager.getInstance().insertNotice(title, content, read, time, activity, extra);
    }

    public void changeNoticeReadState(Notification notification){
        DBManager.getInstance().updateNoticeReadState(notification);
    }

    public List<Notification> getAllNotices(){
        return DBManager.getInstance().getAllNoticeList();
    }

    public int getUnreadNoticesCount(){
        return DBManager.getInstance().getUnreadNoticeCount();
    }

    public  Notification getLastNotice(){
        return (getAllNotices()!=null && getAllNotices().size()>0)?getAllNotices().get(0):null;
    }
}
