package com.dxj.student.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dxj.student.bean.SubjectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/31.
 */
public class SubjectDao {

    /**
     * 获取科目一级列表
     * @param db
     * @return
     */
    public static List<SubjectBean> getFirstCategory(SQLiteDatabase db){
        List<SubjectBean> subjects = new ArrayList<SubjectBean>();
        Cursor cursor = db.rawQuery("select * from t_subject where level = 1", null);
        while(cursor.moveToNext()){
            SubjectBean subject = new SubjectBean();
            subject.setId(cursor.getInt(0));
            subject.setParentId(cursor.getInt(1));
            subject.setName(cursor.getString(2));
            subject.setLevel(cursor.getInt(3));
            subjects.add(subject);
        }
        cursor.close();
        return subjects;
    }

    /**
     * 根据上级科目查找下级科目列表
     * @param db
     * @param parentId
     * @return
     */
    public static List<SubjectBean> getChildCategoryFromParent(SQLiteDatabase db, int parentId){
        List<SubjectBean> subjects = new ArrayList<SubjectBean>();
        Cursor cursor = db.rawQuery("select * from t_subject where parentId = ?", new String[]{String.valueOf(parentId)});
        while(cursor.moveToNext()){
            SubjectBean subject = new SubjectBean();
            subject.setId(cursor.getInt(0));
            subject.setParentId(cursor.getInt(1));
            subject.setName(cursor.getString(2));
            subject.setLevel(cursor.getInt(3));
            subjects.add(subject);
        }
        cursor.close();
        return subjects;
    }

    public static String getCategoryNameById(SQLiteDatabase db, int id){
        Cursor cursor = db.rawQuery("select name from t_subject where id = ?", new String[]{id+""});
        if (cursor.moveToNext()){
            return cursor.getString(0);
        }
        return null;
    }

}
