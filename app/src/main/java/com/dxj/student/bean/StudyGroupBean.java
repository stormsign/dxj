package com.dxj.student.bean;

/**
 * Created by khb on 2015/9/2.
 */
public class StudyGroupBean extends BaseBean{

    private StudyGroup group;

    public StudyGroup getGroup() {
        return group;
    }

    public void setGroup(StudyGroup group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "StudyGroupBean{" +
                "group=" + group +
                '}';
    }
}
