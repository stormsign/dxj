package com.dxj.student.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.student.R;
import com.dxj.student.activity.ChatActivity;
import com.dxj.student.activity.StudyGroupDetailActivity;
import com.dxj.student.adapter.GroupAdapter;
import com.dxj.student.application.MyApplication;
import com.dxj.student.base.BaseFragment;
import com.dxj.student.bean.StudyGroup;
import com.dxj.student.bean.StudyGroupListBean;
import com.dxj.student.bean.SubjectBean;
import com.dxj.student.db.dao.SubjectDao;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.LogUtils;
import com.dxj.student.utils.MyUtils;
import com.dxj.student.utils.ToastUtils;
import com.dxj.student.widget.ExceptionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/9/5.
 */
public class GroupListFragment extends BaseFragment {

    private static final String SECOND_CATEGORY = "second_category";
    private static final String DBNAME = "subject.db";
    private int parentId;

    private int currentPage = 1;
    private final static int pageSize = 10;

    private List<StudyGroup> groupList;
    private RecyclerView rv_grouplist;
    private SwipeRefreshLayout refresh;
    private GroupAdapter gAdapter;
    private SQLiteDatabase db;
    private List<SubjectBean> secondList;
    private List<Integer> secondIdList;
    private View view;
    private ExceptionView emptyView;

    @Override
    public void initData() {
        parentId = getArguments().getInt(SECOND_CATEGORY);
        LogUtils.d("parentId :" + parentId);
        db = SQLiteDatabase.openDatabase(context.getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        showView(parentId);
    }

    private void showView(int parentId) {
        if (parentId == -1) {
            getRecommendedGroupList();
        } else {  //获取二级目录列表
            secondList = SubjectDao.getChildCategoryFromParent(db, parentId);
            secondIdList = new ArrayList<>();
            secondIdList.clear();
            for (SubjectBean subject :
                    secondList) {
                secondIdList.add(subject.getId());
            }
            getSecondGroups(parentId);
        }
    }

    @Override
    public void onResume() {
        showView(parentId);
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_group_list, null);
        rv_grouplist = (RecyclerView) view.findViewById(R.id.rv_grouplist);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        emptyView = (ExceptionView) view.findViewById(R.id.emptyview);
        rv_grouplist.setLayoutManager(new LinearLayoutManager(context));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showView(parentId);
            }
        });
//        空内容页面
        emptyView.setEmptyText("没有找到学团");
        emptyView.showEmptyAction(true);
        emptyView.setEmptyActionText("点击表示无奈");
        emptyView.setOnEmptyActionListener(new ExceptionView.OnEmptyActionListener() {
            @Override
            public void onEmptyAction() {
                ToastUtils.showToast(context, "无奈");
            }
        });
        if (!MyUtils.isMobileConnected(context) && !MyUtils.isWifiConnected(context) && !MyUtils.isNetworkConnected(context)) {
            showNoNetWorkView();
        }
        return view;
    }

    private void showNoNetWorkView() {
        emptyView.setException(ExceptionView.NO_NETWORK);
        emptyView.setOnEmptyActionListener(new ExceptionView.OnEmptyActionListener() {
            @Override
            public void onEmptyAction() {
                showView(parentId);
            }
        });
        refresh.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    public static GroupListFragment newInstance(int parentId) {
        Bundle bundle = new Bundle();
        bundle.putInt(SECOND_CATEGORY, parentId);
        GroupListFragment fragment = new GroupListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getRecommendedGroupList() {
        String url = FinalData.URL_VALUE_COMMON + "getAllGroupList";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", currentPage);
        map.put("pageSize", pageSize);
        GsonRequest<StudyGroupListBean> gRequest = new GsonRequest<StudyGroupListBean>(Request.Method.POST, url,
                StudyGroupListBean.class, map,
                onGetGroupList(),
                onGetGroupListError());
        VolleySingleton.getInstance(context).addToRequestQueue(gRequest);

    }

    private void getSecondGroups(int parentId) {
        String url = FinalData.URL_VALUE_COMMON + "getGroupBySubjectId";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", currentPage);
        map.put("pageSize", pageSize);
        map.put("subjectSecond", parentId);
        GsonRequest<StudyGroupListBean> gRequest = new GsonRequest<StudyGroupListBean>(Request.Method.POST, url,
                StudyGroupListBean.class, map,
                onGetGroupList(),
                onGetGroupListError());
//        CustomStringRequest cRequest = new CustomStringRequest(Request.Method.POST, url,
//                map,
//                onGetSecondListGroupList(),
//                onGetGroupListError());
        VolleySingleton.getInstance(context).addToRequestQueue(gRequest);
    }

//    private Response.Listener<String> onGetSecondListGroupList() {
//        return new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                //如果是获取二级目录下的学团，要手动解析json
//                List<StudyGroup> secondGroups = MyUtils.getSecondListGroupsFromJson(s, secondIdList);
//                processData(secondGroups);
//            }
//        };
//    }

    private Response.Listener<StudyGroupListBean> onGetGroupList() {
        return new Response.Listener<StudyGroupListBean>() {
            @Override
            public void onResponse(StudyGroupListBean s) {
                if (s.getList() != null && !s.getList().isEmpty()) {
                    groupList = s.getList();
                    processData(groupList);
                } else {
                    refresh.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private Response.ErrorListener onGetGroupListError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(volleyError.toString());
                showNoNetWorkView();
            }
        };
    }

    private void processData(final List<StudyGroup> groupList) {
//        getActivity()有时返回为null，这里要在宿主activity内做处理
        gAdapter = new GroupAdapter(getActivity(), groupList, parentId);
        gAdapter.notifyDataSetChanged();
        gAdapter.setOnGroupClickListener(new GroupAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(View view, int position) {
//
//                老师版只考虑登录的是老师账号
                if (MyUtils.isMember(MyUtils.getTeacherHX(MyApplication.getInstance().getUserBean().getUserInfo().getMobile()), groupList.get(position))) {
                    startActivity(new Intent(context, ChatActivity.class)
                            .putExtra("groupHead", groupList.get(position).getHeadUrl())
                            .putExtra("chatType", ChatActivity.CHATTYPE_GROUP)
                            .putExtra("groupHXId", groupList.get(position).getGroupId())
                            .putExtra("groupName", groupList.get(position).getGroupName())
                            .putExtra("groupId", groupList.get(position).getId()));
                } else {
                    startActivity(new Intent(context, StudyGroupDetailActivity.class).putExtra("groupId", groupList.get(position).getId()));
                }
            }

//            @Override
//            public void onMoreClick(View view, int position) {
//                ToastUtils.showToast(context, groupList.get(position).getSubjectSecond()+"");
//                startActivity(new Intent(context, MoreGroupsActivity.class).putExtra("secondId", groupList.get(position).getSubjectSecond()));
//            }
        });
        rv_grouplist.setAdapter(gAdapter);
//        如果是下拉刷新，加载完后隐藏加载动画
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        refresh.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
}
