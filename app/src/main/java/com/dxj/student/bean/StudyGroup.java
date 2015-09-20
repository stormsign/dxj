package com.dxj.student.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by khb on 2015/9/2.
 */
public class StudyGroup implements Serializable{
    /**
     * 学团主键
     */
    private String id;
    /**
     * 创建学团的老师id
     */
    private String teacherId;
    /**
     * 环信的群id
     */
    private String groupId;
    /**
     * 学团名称
     */
    private String groupName;
    /**
     * 学团描述
     */
    private String description;
    /**
     * 学团人数上限
     */
    private int maxusers;
    /**
     * 与团长有关的字段， 老师姓名
     */
    private String owner;
    /**
     * 学团成员
     */
    private List<String> members;
    /**
     * 学团头像
     */
    private String headUrl;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 科目一级类别的id
     */
    private long subjectFirst;
    /**
     * 科目二级类别的id
     */
    private long subjectSecond;
    /**
     * 科目三级类别的id
     */
    private long subjectThree;
    /**
     * 学团的科目
     */
    private String subjectName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSubjectFirst() {
        return subjectFirst;
    }

    public void setSubjectFirst(long subjectFirst) {
        this.subjectFirst = subjectFirst;
    }

    public long getSubjectSecond() {
        return subjectSecond;
    }

    public void setSubjectSecond(long subjectSecond) {
        this.subjectSecond = subjectSecond;
    }

    public long getSubjectThree() {
        return subjectThree;
    }

    public void setSubjectThree(long subjectThree) {
        this.subjectThree = subjectThree;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "StudyGroup{" +
                "id='" + id + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", description='" + description + '\'' +
                ", maxusers=" + maxusers +
                ", owner='" + owner + '\'' +
                ", members=" + members +
                ", headUrl='" + headUrl + '\'' +
                ", createTime=" + createTime +
                ", subjectFirst=" + subjectFirst +
                ", subjectSecond=" + subjectSecond +
                ", subjectThree=" + subjectThree +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
