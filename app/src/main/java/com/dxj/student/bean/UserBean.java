package com.dxj.student.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 图片实体
 */
public class UserBean extends BaseBean implements Serializable{
    public UserInfo userInfo;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public class UserInfo implements Serializable{
       private String id; //用户id
        private String nickName;//用户昵称
        private String headUrl;//头像
        private String name;//真实姓名
        private String sex;//性别
        private Long type;//学生or家长
        private String mobile;//手机
        private String horoscope;//星座
        private String grade;//年级
        private ArrayList<String> images = new ArrayList<>();
        private String school;//学校
        private String livingCity;//现居城市
//        private String card;//学生证

//        public void setCard(String card) {
//            this.card = card;
//        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public void setHoroscope(String horoscope) {
            this.horoscope = horoscope;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        public void setLivingCity(String livingCity) {
            this.livingCity = livingCity;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public Long getType() {
            return type;
        }

//        public String getCard() {
//            return card;
//        }

        public String getGrade() {
            return grade;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public String getHoroscope() {
            return horoscope;
        }

        public String getId() {
            return id;
        }

        public String getLivingCity() {
            return livingCity;
        }

        public String getMobile() {
            return mobile;
        }

        public String getName() {
            return name;
        }

        public String getNickName() {
            return nickName;
        }

        public String getSchool() {
            return school;
        }

        public String getSex() {
            return sex;
        }
    }
}
