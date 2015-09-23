package com.dxj.student.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.dxj.student.R;
import com.dxj.student.adapter.NotificationAdapter;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.Notification;
import com.dxj.student.db.dao.NoticeDao;
import com.dxj.student.utils.MyUtils;
import com.dxj.student.widget.TitleNavBar;

import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/9/16.
 */
public class NotificationsActivity extends BaseActivity {

    public static NotificationsActivity notificationsActivity;

    private static final String BROWSER_ACTIVITY = "com.dxj.teacher.activity.BrowserActivity";

    private List<Notification> noticesList;
    private RecyclerView rv_notice;
    private SwipeRefreshLayout refresh;
    private NotificationAdapter mAdapter;
    private NoticeDao noticeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notificationsActivity = this;
        initTitle();
        initView();
        initData();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitleNoRightButton();
        title.setTitle("系统消息");
    }

    @Override
    public void initView() {
        rv_notice = (RecyclerView) findViewById(R.id.rv_noticeList);
        refresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void initData() {
        noticeDao = new NoticeDao(this);
        noticesList = noticeDao.getAllNotices();
        mAdapter = new NotificationAdapter(this, noticesList);
        rv_notice.setLayoutManager(new LinearLayoutManager(this));
        rv_notice.setAdapter(mAdapter);
        mAdapter.setOnNotificationItemClickListener(new NotificationAdapter.OnNotificationItemClickListener() {
            @Override
            public void onNotificationItemClick(View view, int position) {
                noticeDao.changeNoticeReadState(noticesList.get(position));
                Notification notification = noticesList.get(position);
                showToast("假装进入通知详情页面");
                if (notification.getActivity()!=null && !TextUtils.isEmpty(notification.getActivity()))
                    switch (notification.getActivity()) {
                        case BROWSER_ACTIVITY:
                            Map<String, String> map = MyUtils.parseUmengExtra(notification.getExtra());
                            MyUtils.openActivityFromUmengMessage(context, notification.getActivity(), map);
                            break;
                    }
            }
        });
    }

    @Override
    protected void onResume() {
        refreshAll();
        super.onResume();
    }

    private void refreshAll(){
        noticesList.clear();
        noticesList.addAll(noticeDao.getAllNotices());
        mAdapter.notifyDataSetChanged();
    }

    public void addNewNotice(Notification notification){
        noticesList.add(0, notification);
        mAdapter.notifyItemInserted(0);
        rv_notice.scrollToPosition(0);
    }
}
