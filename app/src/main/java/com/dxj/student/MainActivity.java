package com.dxj.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dxj.student.activity.ChatActivity;
import com.dxj.student.activity.LoginAndRightActivity;
import com.dxj.student.activity.UpdateImageActivity;
import com.dxj.student.application.MyApplication;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.factory.FragmentFactory;
import com.dxj.student.utils.SPUtils;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private Button bt_user, bt_message, bt_search, bt_home;
    private FragmentManager fm;
    private Context context = this;
    private PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

    }

    @Override
    public void initView() {

        fm = getSupportFragmentManager();
        bt_home = (Button) findViewById(R.id.bt_home);
        bt_search = (Button) findViewById(R.id.bt_search);
        bt_message = (Button) findViewById(R.id.bt_message);
        bt_user = (Button) findViewById(R.id.bt_user);
        showLogD("==============================================");
    }

    @Override
    public void initData() {
        mPushAgent = PushAgent.getInstance(context);
        if (!mPushAgent.isEnabled()) {
            mPushAgent.enable(mRegisterCallback);
        }

        String device_token = UmengRegistrar.getRegistrationId(context);
        showLogD("device_token " + device_token);
        SPUtils.saveSPData("token", device_token);
//        环信账号登录
        EMChatManager.getInstance().login("S1", "123456", new EMCallBack() {//回调
            @Override
            public void onSuccess() {

                // 登陆成功，保存用户名密码
                MyApplication.getInstance().setUserName("S1");
                MyApplication.getInstance().setPassword("123456");
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                            // ** manually load all local groups and
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            Log.d("main", "登陆聊天服务器成功！");
                            // 处理好友和群组
                            initializeContacts();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 取好友或者群聊失败，不让进入主页面
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    DemoHXSDKHelper.getInstance().logout(true,null);
                                    Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
                                }
                            });
                            return;
                        }

                    }
                });
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！");
            }
        });

//        register(device_token);

    }
//    环信账号登录后的处理
    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 添加"Robot"
        User robotUser = new User();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }


    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            String pkgName = getApplicationContext().getPackageName();
            String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s " +
                            "SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
                    mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                    mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
                    UmengMessageDeviceConfig.getAppVersionCode(MainActivity.this), UmengMessageDeviceConfig.getAppVersionName(MainActivity.this));
            SPUtils.saveSPData("token", mPushAgent.getRegistrationId());
            Log.i("TAG", "updateStatus:" + info);
            Log.i("TAG", "=============================");
        }
    };


    public void showFragment(View v) {
        switch (v.getId()) {
            case R.id.bt_home:
                Toast.makeText(context, "HOME", Toast.LENGTH_SHORT).show();
                fm.beginTransaction()
                        .replace(R.id.rl_fragment_contanier, FragmentFactory.getFragment(0), "HOME")
                        .commit();
                break;
            case R.id.bt_search:
                Toast.makeText(context, "SEARCH", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_user:
                Intent intent = new Intent(this, LoginAndRightActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_message:
                Intent intentMulti = new Intent(this, UpdateImageActivity.class);
                startActivity(intentMulti);
                break;
        }
    }

    public void show(View v) {
        startActivity(new Intent(context, ChatActivity.class));
    }

}
