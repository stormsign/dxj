package com.dxj.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dxj.student.activity.StudyGroupListActivity;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.db.dao.NoticeDao;
import com.dxj.student.factory.FragmentFactory;
import com.dxj.student.fragment.HomeFragment;
import com.dxj.student.fragment.MessageFragment;
import com.dxj.student.fragment.MyFragment;
import com.dxj.student.utils.SPUtils;
import com.easemob.EMConnectionListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.exceptions.EaseMobException;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends BaseActivity implements EMEventListener {

    public static MainActivity mainActivity;

    private Button bt_user, bt_message, bt_search, bt_home;
    private FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;
    private Context context = this;
    private PushAgent mPushAgent;
    private final static int HOME = 0;
    private final static int LOOKINGFORTEACHER = 1;
    private final static int MESSAGE = 2;
    private final static int MYINFO = 3;
    private static final String DBNAME = "subject.db";
    private static final String SCHOOL = "t_city.db";
    private static final String TEACHER_TYPE = "t_teacher_role.db";
    private LocationClient mLocationClient;
    private Button startLocation;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private TextView unreadLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        initData();
        initView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        注释此行，不保存Fragment状态，这样当该activity被回收时，attach的Fragment也会一通被回收
//        此段代码是为了解决fragment.getActivity()返回为null的问题
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        bt_home = (Button) findViewById(R.id.bt_home);
        bt_search = (Button) findViewById(R.id.bt_search);
        bt_message = (Button) findViewById(R.id.bt_message);
        bt_user = (Button) findViewById(R.id.bt_user);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);

//        if (fm.findFragmentByTag("HOME") == null) {
        ft.add(R.id.rl_fragment_contanier, (HomeFragment) FragmentFactory.getFragment(HOME), "HOME")
                .add(R.id.rl_fragment_contanier, (MessageFragment) FragmentFactory.getFragment(MESSAGE), "MESSAGE")
                .hide((MessageFragment) FragmentFactory.getFragment(MESSAGE))
//                .add(R.id.rl_fragment_contanier, (FindFragment) FragmentFactory.getFragment(LOOKINGFORTEACHER), "LOOKINGFORTEACHER").hide((FindFragment) FragmentFactory.getFragment(LOOKINGFORTEACHER))
                .add(R.id.rl_fragment_contanier, FragmentFactory.getFragment(MYINFO), "MYINFO").hide(((MyFragment) FragmentFactory.getFragment(MYINFO)));
        ft.show((HomeFragment) FragmentFactory.getFragment(HOME)).commit();
        bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_home_pre), null, null);
//        bt_home.setSelected(true);
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
        copyDB(DBNAME);
        copyDB(SCHOOL);
        copyDB(TEACHER_TYPE);
//        initLocation();
//        mLocationClientlient.start();

        setListener();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，


        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    /**
     * 把apk中的数据库复制到手机内存
     *
     * @param dbName
     */
    private void copyDB(final String dbName) {
        new Thread() {
            public void run() {
                File file = new File(getFilesDir(), dbName);
                if (file.exists() && file.length() > 0) {
                    showLogI("已经拷贝过了数据库,无需重新拷贝");
                } else {
                    try {
                        InputStream is = getAssets().open(dbName);
                        byte[] buffer = new byte[1024];
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
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
                fm.beginTransaction()
                        .hide(FragmentFactory.getFragment(MESSAGE))
//                        .hide(FragmentFactory.getFragment(LOOKINGFORTEACHER))
                        .hide(FragmentFactory.getFragment(MYINFO))
                        .show(FragmentFactory.getFragment(HOME))
                        .commit();
                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_home_pre), null, null);
                bt_search.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_zhaolaoshi), null, null);
                bt_message.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_xiaoxi), null, null);
                bt_user.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_me), null, null);
                break;
            case R.id.bt_search:
                fm.beginTransaction()
                        .hide(FragmentFactory.getFragment(HOME))
                        .hide(FragmentFactory.getFragment(MESSAGE))
                        .hide(FragmentFactory.getFragment(MYINFO))
//                        .show(FragmentFactory.getFragment(LOOKINGFORTEACHER))
                        .commit();
//                startActivity(new Intent(this, SubjectFirstCategoryActivity.class));
                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_home), null, null);
                bt_search.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_zhaolaoshi_pre), null, null);
                bt_message.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_xiaoxi), null, null);
                bt_user.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_me), null, null);
                break;
            case R.id.bt_user:
//                Intent intent = new Intent(this, LoginAndRightActivity.class);
//                startActivity(intent);
                fm.beginTransaction()
                        .hide(FragmentFactory.getFragment(HOME))
                        .hide(FragmentFactory.getFragment(MESSAGE))
//                        .hide(FragmentFactory.getFragment(LOOKINGFORTEACHER))
                        .show(FragmentFactory.getFragment(MYINFO))
                        .commit();
                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_home), null, null);
                bt_search.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_zhaolaoshi), null, null);
                bt_message.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_xiaoxi), null, null);
                bt_user.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_me_pre), null, null);
                break;
            case R.id.bt_message:
                fm.beginTransaction()
                        .hide(FragmentFactory.getFragment(HOME))
//                        .hide(FragmentFactory.getFragment(LOOKINGFORTEACHER))
                        .hide(FragmentFactory.getFragment(MYINFO))
                        .show(FragmentFactory.getFragment(MESSAGE))
                        .commit();
                bt_home.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_home), null, null);
                bt_search.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_zhaolaoshi), null, null);
                bt_message.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_xiaoxi_pre), null, null);
                bt_user.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.tab_ico_me), null, null);
                break;
        }
    }

    public void show(View v) {
        startActivity(new Intent(context, StudyGroupListActivity.class));
    }

    public void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
//                if (fm.findFragmentByTag("MESSAGE").isVisible()) {
                // 当前页面如果为聊天历史页面，刷新此页面
//                    if (fm.findFragmentByTag("MESSAGE") != null) {
                ((MessageFragment) FragmentFactory.getFragment(MESSAGE)).refresh();
//                    }
//                }
            }
        });
    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {

        int count = getUnreadMsgCountTotal() + new NoticeDao(this).getUnreadNoticesCount();
        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!isConflict && !isCurrentAccountRemoved) {
        updateUnreadLabel();
//            updateUnreadAddressLable();
//            EMChatManager.getInstance().activityResumed();
//        }

        // unregister this event listener when this activity enters the
        // background
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
    }

    /**
     * 监听群，好友，连接 by zjb
     */
    public void setListener() {
        // if (MyApplication.getInstance().isLogin()) {
        Log.i("TAG", "HXSDKHelper.getInstance().isLogined() " + DemoHXSDKHelper.getInstance().isLogined());
        if (DemoHXSDKHelper.getInstance().isLogined()) {
//            inviteMessgeDao = new InviteMessgeDao(this);
//            userDao = new UserDao(this);
//            // setContactListener监听联系人的变化等
            EMContactManager.getInstance().setContactListener(new MyContactListener());
            // 注册一个监听连接状态的listener
            EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
            // 注册群聊相关的listener
            EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
            // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
            EMChat.getInstance().setAppInited();
        }
    }


    /***
     * 好友变化listener
     */
    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            Log.i("TAG", "onContactAdded " + usernameList.size());
            // 保存增加的联系人
//            Map<String, User> localUsers = MyApplication.getInstance().getContactList();
//            Map<String, User> toAddUsers = new HashMap<String, User>();
//            for (String username : usernameList) {
//                // 添加好友时可能会回调added方法两次
//                if (!localUsers.containsKey(username)) {
//                    User user = setUserHead(username);
//                    userDao.saveContact(user);
//                    toAddUsers.put(username, user);
//                }
//            }
//            localUsers.putAll(toAddUsers);

        }

        @Override
        public void onContactAgreed(String username) {
            Log.i("TAG", "onContactAgreed " + username + " ");
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(username)) {
//                    return;
//                }
//            }

            // 自己封装的javabean
//            InviteMessage msg = new InviteMessage();
//            msg.setHeader("");
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//            Log.d("TAG", username + "同意了你的好友请求");
//            msg.setStatus(InviteMesageStatus.BEAGREED);
//            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            Log.i("TAG", "onContactDeleted " + usernameList.size());
            // 被删除
//            Map<String, User> localUsers = MyApplication.getInstance().getContactList();
//            for (String username : usernameList) {
//                localUsers.remove(username);
//                userDao.deleteContact(username);
//                inviteMessgeDao.deleteMessage(username);
//            }
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    // 如果正在与此用户的聊天页面
//                    String st10 = getResources().getString(R.string.have_you_removed);
//                    if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
//                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, 1).show();
//                        ChatActivity.activityInstance.finish();
//                    }
//                    updateUnreadLabel();
//                }
//            });

        }

        @Override
        public void onContactInvited(String username, String reason) {
            Log.i("TAG", "onContactInvited " + username + " " + reason);
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
//                    inviteMessgeDao.deleteMessage(username);
//                }
//            }
//            // 自己封装的javabean
//            InviteMessage msg = new InviteMessage();
//            msg.setHeader("");
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//            msg.setReason(reason);
//            Log.d("TAG", username + "请求加你为好友,reason: " + reason);
//            // 设置相应status
//            msg.setStatus(InviteMesageStatus.BEINVITEED);
//
//            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactRefused(String username) {
            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }

    /**
     * 连接监听listener
     */
    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    Boolean isHXConnected = SettingHelper.getSharedPreferences(getApplicationContext(), "is_easemob_connection", false);
//                    if (isHXConnected != null && !isHXConnected) {
//                        // chatHistoryFragment.errorItem.setVisibility(View.GONE);
//                        ToastUtil.showToast(getApplicationContext(), "已连接上聊天服务器");
//                    }
//                    SettingHelper.setEditor(getApplicationContext(), "is_easemob_connection", true);
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
//            final String st1 = getResources().getString(R.string.Less_than_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    Boolean isHXConnected = SettingHelper.getSharedPreferences(getApplicationContext(), "is_easemob_connection", false);
//                    if (isHXConnected) {
//                        ToastUtil.showToast(getApplicationContext(), "与聊天服务器断开连接，请重新登录账号！");
//                    }
//                    SettingHelper.setEditor(getApplicationContext(), "is_easemob_connection", false);
                    // if(error == EMError.USER_REMOVED){
                    // // 显示帐号已经被移除
                    // showAccountRemovedDialog();
                    // }else if (error == EMError.CONNECTION_CONFLICT) {
                    // // 显示帐号在其他设备登陆dialog
                    // showConflictDialog();
                    // } else {
                    // chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
                    // if (NetUtils.hasNetwork(MainActivity.this))
                    // chatHistoryFragment.errorText.setText(st1);
                    // else
                    // chatHistoryFragment.errorText.setText(st2);
                    //
                    // }
                }

            });
        }
    }

    /**
     * MyGroupChangeListener
     */
    private class MyGroupChangeListener implements GroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            Log.i("TAG", "onInvitationReceived " + groupName + " " + inviter + " " + reason);
            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup)
                return;

            // 被邀请
//            String st3 = getResources().getString(R.string.Invite_you_to_join_a_group_chat);
//            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//            msg.setChatType(ChatType.GroupChat);
//            msg.setFrom(inviter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            getNickName(inviter, msg, st3, ONINVITATIONRECEIVED);

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            // 提示用户被T了，demo省略此步骤
            // 刷新ui
            Log.i("TAG", "onUserRemoved " + groupName);
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    try {
//                        updateUnreadLabel();
//
//                        // if (currentTabIndex == 0)
//                        // chatHistoryFragment.refresh();
//                        if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                            GroupsActivity.instance.onResume();
//                        }
//                    } catch (Exception e) {
//                        EMLog.e("TAG", "refresh exception " + e.getMessage());
//                    }
//                }
//            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            // 群被解散
            // 提示用户群被解散,demo省略
            // 刷新ui
            Log.i("TAG", "onGroupDestroy " + groupName);
            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    // if (currentTabIndex == 0)
                    // chatHistoryFragment.refresh();
//                    if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
                }
            });

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
            Log.i("TAG", "onApplicationReceived " + groupName + " " + applyer + " " + reason);
            // 用户申请加入群聊
//            InviteMessage msg = new InviteMessage();
//            msg.setHeader("");
//            msg.setFrom(applyer);
//            msg.setTime(System.currentTimeMillis());
//            msg.setGroupId(groupId);
//            msg.setGroupName(groupName);
//            msg.setReason(reason);
//            Log.d("TAG", applyer + " 申请加入群聊：" + groupName);
//            msg.setStatus(InviteMesageStatus.BEAPPLYED);
//            notifyNewIviteMessage(msg);
//            /************************* pengjun *************************/
//            updateUnreadLabel();
//            /************************* pengjun *************************/

        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            Log.i("TAG", "onApplicationAccept " + groupName + " " + accepter);
            String st4 = getResources().getString(R.string.Agreed_to_your_group_chat_application);
            // 加群申请被同意
//            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//            msg.setChatType(ChatType.GroupChat);
//            msg.setFrom(accepter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            getNickName(accepter, msg, st4, ONAPPLICATIONACCEPT);
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            // 加群申请被拒绝，demo未实现
        }

    }

    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();
                try {
                    showLogD("MESSAGE " + message.getStringAttribute("groupName"));
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }

            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

}
