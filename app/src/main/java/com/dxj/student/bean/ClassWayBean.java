package com.dxj.student.bean;

import java.io.Serializable;

/**
 * Created by khb on 2015/8/31.
 */
public class ClassWayBean extends  BaseBean implements Serializable{


    private int mode;
    private int price;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMode() {
        return mode;
    }

    public int getPrice() {
        return price;
    }
}
