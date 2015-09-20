package com.dxj.student.bean;

import java.util.List;

/**
 * Created by khb on 2015/9/2.
 */
public class StudyGroupListBean extends BaseBean{

    private List<StudyGroup> list;

    public List<StudyGroup> getList() {
        return list;
    }

    public void setList(List<StudyGroup> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "StudyGroupListBean{" +
                "list=" + list +
                '}';
    }
}
