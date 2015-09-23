package com.dxj.student.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dxj.student.R;
import com.dxj.student.adapter.NoticeAdapter;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.Notice;
import com.dxj.student.widget.MySwipeRefreshLayout;
import com.dxj.student.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class GroupNoticeActivity extends BaseActivity {

    private RecyclerView rv_notices;
    private String groupId;
    private List<Notice> list;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                srl.setRefreshing(false);
                list.clear();
                list.addAll(getList(10));
                adapter.notifyDataSetChanged();
                super.handleMessage(msg);
            }else{
                srl.setLoading(false);

                if (list.size()<30) {
                    int positionStart = list.size()+1;
                    list.addAll(getList(10));
                    int positionEnd = list.size();
                    adapter.notifyItemRangeInserted(positionStart, positionEnd);
                }else{
                    showToast("meiyoule");
                }
//                adapter.showFooterView(false);
//                adapter.notifyItemRemoved(list.size());
//                adapter.showFooterView(false);
//                adapter.notifyDataSetChanged();
            }
        }
    };
    private NoticeAdapter adapter;
    private MySwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice);
        initData();
        initTitle();
        initView();

    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("团公告");
//        title.showActionOnly();
//        title.setActionText("发布");
//        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
//            @Override
//            public void onNavOneClick() {
//            }
//
//            @Override
//            public void onNavTwoClick() {
//            }
//
//            @Override
//            public void onNavThreeClick() {
//            }
//
//            @Override
//            public void onActionClick() {
//                showLogD("?????????????????????????????????");
////                showToast("发布公告");
////                startActivity(new Intent(activity, CreateNoticeActivity.class));
//            }
//
//            @Override
//            public void onBackClick() {
//            }
//        });

    }

    @Override
    public void initView() {
        srl = (MySwipeRefreshLayout) findViewById(R.id.srl);
        rv_notices = (RecyclerView) findViewById(R.id.rv_notices);
        adapter = new NoticeAdapter(this, list);

        adapter.setOnNoticeClickListener(new NoticeAdapter.OnNoticeClickListener() {
            @Override
            public void onNoticeClick(View view, int position) {
                showToast(position + " clicked");
            }
        });
        srl.setColorSchemeResources(R.color.text_orange);
        rv_notices.setLayoutManager(new LinearLayoutManager(this));
        rv_notices.setAdapter(adapter);
        srl.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showToast("刷新数据");
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        });
        srl.setOnLoadListener(new MySwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
//                View view = getLayoutInflater().inflate(R.layout.loadingview_footer, null, false);
//                rv_notices.addView(view, list.size()-1);
//                adapter.notifyItemInserted(list.size()-1);
//                adapter.notifyDataSetChanged();
                handler.sendEmptyMessageDelayed(2, 2000);
            }
        });



        // 这句话是为了，第一次进入页面的时候显示加载进度条
//        srl.setProgressViewOffset(false, 0, (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                        .getDisplayMetrics()));

//        rv_notices.setOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView,
//                                             int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    srl.setRefreshing(true);
//                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                }
//            }
//        });
    }

    @Override
    public void initData() {
        groupId = getIntent().getStringExtra("groupId");
        list = getList(5);
    }

    private List<Notice> getList(int n){
        List<Notice> list = new ArrayList<>();
        for (int i = 0 ; i < n; i++){
            Notice notice = new Notice();
            notice.setName(i+1+"");
            notice.setDescription("sdfsfsdfsfasdffsadfsafdsafssdfasfgdgds");
            list.add(notice);
        }
        return list;
    }
}
