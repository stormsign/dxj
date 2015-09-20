package com.dxj.student.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 9/7/2015.
 */
public class TeacherInfoBean extends BaseBean implements Serializable {
    private ArrayList<StudyGroup> group = new ArrayList<>(); //学团
    private ArrayList<CourseSubjectBean> goodSubject=new ArrayList<>();//科目
//    private CommentBean comment;//评论
    private int studentNum;//数目
    private int goodRate;//好评
    private TeacherUserBean.UserInfo userInfo;
    public TeacherUserBean.UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(TeacherUserBean.UserInfo userInfo) {
        this.userInfo = userInfo;
    }

//    public CommentBean getClassOrderComment() {
//        return comment;
//    }
//
//    public void setClassOrderComment(CommentBean comment) {
//        this.comment = comment;
//    }




    public void setGoodRate(int goodRate) {
        this.goodRate = goodRate;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public int getGoodRate() {
        return goodRate;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setGroup(ArrayList<StudyGroup> group) {
        this.group = group;
    }

    public ArrayList<StudyGroup> getGroup() {
        return group;
    }

    public void setGoodSubject(ArrayList<CourseSubjectBean> goodSubject) {
        this.goodSubject = goodSubject;
    }

    public ArrayList<CourseSubjectBean> getGoodSubject() {
        return goodSubject;
    }
}
