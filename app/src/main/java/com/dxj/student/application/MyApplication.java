package com.dxj.student.application;

import android.app.Application;
import android.content.Context;

import com.dxj.student.bean.UserBean;
import com.dxj.student.db.AccountDBTask;
import com.dxj.student.utils.ExceptionHandler;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;


/**
 * Created by khb on 2015/8/20.
 */
public class MyApplication extends Application {

    public static Context applicationContext;
    private static MyApplication instance;
    private  UserBean mUserBean;
    private String id;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

    @Override
    public void onCreate() {
        super.onCreate();
//        applicationContext = getApplicationContext();
        applicationContext = this;
        instance = this;
        ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
        exceptionHandler.init(instance);
//        环信初始化
        hxSDKHelper.onInit(applicationContext);
    }
    /**
     * 获取用户信息
     *
     * @return
     */
    public UserBean getUserBean() {
        if (mUserBean == null) {
            // 从文件获取

            mUserBean = AccountDBTask.getAccount();
        }
        return mUserBean;
    }
    public String getUserId() {
        if (id == null) {
            // 从文件获取

            id = getUserBean().getUserInfo().getId();
        }
        return id;
    }
    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     * @param user
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final boolean isGCM,final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(isGCM, emCallBack);
    }

    public static MyApplication getInstance(){
        return instance;
    }
    public void setUserBean(UserBean userbean) {
        this.mUserBean = userbean;
    }

}
