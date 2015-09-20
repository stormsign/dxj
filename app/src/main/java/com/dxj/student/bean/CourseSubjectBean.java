package com.dxj.student.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 9/3/2015.
 * 科目
 */
public class CourseSubjectBean implements  Serializable{
    private String id;/*科目id*/
    private String teacherId;/*老师id*/
    private int subjectId;/*科目名字id*/
    private String subjectName;/*科目名字*/
    private String remark;/*专业背景*/
    private String fullName;/**/
    private List<ClassWayBean> classWay = new ArrayList<>();

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setClassWay(List<ClassWayBean> classWay) {
        this.classWay = classWay;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public List<ClassWayBean> getClassWay() {
        return classWay;
    }

    public String getId() {
        return id;
    }

    public String getRemark() {
        return remark;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeacherId() {
        return teacherId;
    }
}
