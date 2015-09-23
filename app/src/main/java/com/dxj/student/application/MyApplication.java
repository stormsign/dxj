package com.dxj.student.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.dxj.student.MainActivity;
import com.dxj.student.activity.NotificationsActivity;
import com.dxj.student.bean.Notification;
import com.dxj.student.bean.UserBean;
import com.dxj.student.db.AccountDBTask;
import com.dxj.student.db.dao.NoticeDao;
import com.dxj.student.factory.FragmentFactory;
import com.dxj.student.fragment.MessageFragment;
import com.dxj.student.manager.DBManager;
import com.dxj.student.utils.ExceptionHandler;
import com.dxj.student.utils.LogUtils;
import com.dxj.student.utils.MyUtils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by khb on 2015/8/20.
 */
public class MyApplication extends Application {

    public static Context applicationContext;
    private static MyApplication instance;
    private  UserBean mUserBean;
    private String id;

    private final static int HOME = 0;
    private final static int LOOKINGFORTEACHER = 1;
    private final static int MESSAGE = 2;
    private final static int MYINFO = 3;
    // login user name
    public final String PREF_USERNAME = "username";
    public static List<Activity> storedActivities = new ArrayList<>();

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

        PushAgent.getInstance(getApplicationContext()).setMessageHandler(mUmengMessageHandler);
        PushAgent.getInstance(getApplicationContext()).setNotificationClickHandler(umengNotificationClickHandler);
    }

    UmengMessageHandler mUmengMessageHandler = new UmengMessageHandler(){
        @Override
        public void dealWithNotificationMessage(Context context, UMessage uMessage) {
//            父类方法是在通知栏显示消息
            super.dealWithNotificationMessage(context, uMessage);
//            以下是将消息存入数据库
            LogUtils.d("dealWithNotificationMessage got umeng message : " + uMessage.text);
            NoticeDao noticeDao = new NoticeDao(context);
            Date currentDate = new Date(System.currentTimeMillis());
//            currentDate.getTime();
            Notification notification = new Notification();
            notification.setTitle(uMessage.title);
            notification.setContent(uMessage.text);
            notification.setTime(currentDate.getTime());
            notification.setRead(false);
            notification.setActivity(uMessage.activity);
            if (null != uMessage.extra){
                notification.setExtra(uMessage.extra.get(MyUtils.UMENG_MESSAGE_EXTRA));
            }
            noticeDao.saveNotice(notification.getTitle(), notification.getContent(), DBManager.NOTICE_UNREAD, String.valueOf(notification.getTime()),
                    notification.getActivity(), notification.getExtra());
            List<Notification> allNotices = noticeDao.getAllNotices();
            LogUtils.d("allNotices "+ allNotices.size());
            if (MainActivity.mainActivity != null){
                MainActivity.mainActivity.refreshUI();
            }
//            系统通知activity已打开，并且在栈顶显示时,直接刷新
            if (NotificationsActivity.notificationsActivity != null
                    && (NotificationsActivity.class.getName()
                    .equals(CommonUtils.getTopActivity(context)))){
                NotificationsActivity.notificationsActivity.addNewNotice(notification);
            }
        }
    };

    UmengNotificationClickHandler umengNotificationClickHandler = new UmengNotificationClickHandler(){
        @Override
        public void openUrl(Context context, UMessage uMessage) {
            super.openUrl(context, uMessage);
        }

        @Override
        public void openActivity(Context context, UMessage uMessage) {
            LogUtils.d("openActivity " + uMessage.activity);
//            super.openActivity(context, uMessage);
            if(uMessage.activity != null && !TextUtils.isEmpty(uMessage.activity.trim())) {
                if (uMessage.extra != null) {
                    String extraString = uMessage.extra.get(MyUtils.UMENG_MESSAGE_EXTRA);
                    Map<String, String> mapExtra = MyUtils.parseUmengExtra(extraString);
                    MyUtils.openActivityFromUmengMessage(context, uMessage.activity, mapExtra);
                }
            }
        }

    };



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

    //    把一个Activity加入到内存中
    public static void addActivity(Activity activity) {
        storedActivities.add(activity);
    }

    //    将内存记录的Activity全部退出
    public static void quitActivities() {
        for (Activity activity :
                storedActivities) {
            activity.finish();
        }
        storedActivities.clear();
    }
}
