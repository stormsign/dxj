package com.dxj.student.bean;

/**
 * Created by khb on 2015/9/15.
 */
public class Notification {

    private int id;
    private String title;
    private String content;
    private long time;
    private boolean read;
    /**
     * 如果不为空表示点击通知要跳转的activity
     */
    private String activity;
    /**
     * 跳转activity时要传的值
     */
    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", read=" + read +
                ", activity='" + activity + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
