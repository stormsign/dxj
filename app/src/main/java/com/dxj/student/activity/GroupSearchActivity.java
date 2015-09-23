package com.dxj.student.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.student.R;
import com.dxj.student.adapter.GroupAdapter;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.StudyGroup;
import com.dxj.student.bean.StudyGroupListBean;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/9/10.
 */
public class GroupSearchActivity extends BaseActivity {

    private RecyclerView groups;
    private List<StudyGroup> groupList;
    private GroupAdapter gAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.showSearchBar(true);
        final EditText et_search = (EditText) findViewById(R.id.et_search);
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {}

            @Override
            public void onNavTwoClick() {}

            @Override
            public void onNavThreeClick() {
                searchGroup(et_search.getText().toString());
            }

            @Override
            public void onActionClick() {}

            @Override
            public void onBackClick() {}
        });
    }

    private void searchGroup(String s) {
        String url = FinalData.URL_VALUE_COMMON+"searchGroup";
        Map<String, Object> map = new HashMap<>();
        map.put("nameOrOwner", s);
        GsonRequest<StudyGroupListBean> gRequest = new GsonRequest<StudyGroupListBean>(Request.Method.POST, url,
                StudyGroupListBean.class, map,
                onGetGroupList(),
                onGetGroupListError());
        VolleySingleton.getInstance(context).addToRequestQueue(gRequest);
    }

    private Response.Listener<StudyGroupListBean> onGetGroupList() {
        return new Response.Listener<StudyGroupListBean>() {
            @Override
            public void onResponse(StudyGroupListBean s) {
                if (s.getList()!=null && !s.getList().isEmpty()){
                    groupList = s.getList();
                    processData(groupList);
                }
            }
        };
    }

    private Response.ErrorListener onGetGroupListError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showLogE(volleyError.toString());
                showToast(getResources().getString(R.string.error_msg));
            }
        };
    }

    private void processData(final List<StudyGroup> groupList) {
        gAdapter = new GroupAdapter(this, groupList, -1);
        gAdapter.notifyDataSetChanged();
        gAdapter.setOnGroupClickListener(new GroupAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(View view, int position) {
                startActivity(new Intent(context, StudyGroupDetailActivity.class).putExtra("groupId", groupList.get(position).getId()));
            }

//            @Override
//            public void onMoreClick(View view, int position) {
//                ToastUtils.showToast(context, groupList.get(position).getSubjectSecond()+"");
//                startActivity(new Intent(context, MoreGroupsActivity.class).putExtra("secondId", groupList.get(position).getSubjectSecond()));
//            }
        });
        groups.setAdapter(gAdapter);
    }

    @Override
    public void initView() {
        groups = (RecyclerView)findViewById(R.id.rv_groups);
        groups.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        groupList = new ArrayList<>();
    }
}
