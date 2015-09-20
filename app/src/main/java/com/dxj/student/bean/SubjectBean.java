package com.dxj.student.bean;

/**
 * Created by khb on 2015/8/31.
 */
public class SubjectBean {

    private int id;
    private int parentId;
    private String name;
    private int level;
    private boolean isSelected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
