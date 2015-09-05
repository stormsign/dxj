package com.dxj.student.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class HeadUrl extends BaseBean implements Serializable {
    // 字段名称 数据类型 说明
    // code int 用于标识请求是否成功，0-成功，非0-失败。
    // msg String code的相关说明 或 给用户的提示。
    // url String 图片地址
    private ArrayList<String> images = new ArrayList<>();

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImages() {
        return images;
    }
}
