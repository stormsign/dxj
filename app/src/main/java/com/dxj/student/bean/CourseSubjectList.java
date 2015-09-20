package com.dxj.student.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 9/3/2015.
 */
public class CourseSubjectList extends BaseBean implements Serializable{
  private List<CourseSubjectBean> list = new ArrayList<>();

    public void setList(List<CourseSubjectBean> list) {
        this.list = list;
    }

    public List<CourseSubjectBean> getList() {
        return list;
    }

}
