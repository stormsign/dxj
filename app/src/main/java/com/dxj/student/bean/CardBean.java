package com.dxj.student.bean;

import java.io.Serializable;

/**
 * 图片实体
 */
public class CardBean extends BaseBean implements Serializable {

    private String id; //用户id
    private int cardType;//0:身份证 1:护照
    private String name;//证件照片
    private String img;//证件图片
    private int passCard;//认证是否通过
    private String number;//证件编号

    public void setId(String id) {
        this.id = id;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    @Override
    public void setCode(Long code) {
        super.setCode(code);
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public void setMsg(String msg) {
        super.setMsg(msg);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPassCard(int passCard) {
        this.passCard = passCard;
    }

    public String getId() {
        return id;
    }

    public int getCardType() {
        return cardType;
    }

    public int getPassCard() {
        return passCard;
    }

    @Override
    public Long getCode() {
        return super.getCode();
    }

    public String getImg() {
        return img;
    }

    @Override
    public String getMsg() {
        return super.getMsg();
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
