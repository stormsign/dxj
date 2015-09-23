package com.dxj.student.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.student.R;
import com.dxj.student.adapter.MemberAdapter;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.UserBean;
import com.dxj.student.effectdialog.Effectstype;
import com.dxj.student.effectdialog.NiftyDialogBuilder;
import com.dxj.student.http.CustomStringRequest;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.ToastUtils;
import com.dxj.student.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/9/6.
 */
public class GroupMemberListActivity extends BaseActivity {

    private List<UserBean.UserInfo> members;
    private SwipeRefreshLayout refresh;
    private RecyclerView rv_members;
    private MemberAdapter mAdapter;
    /**
     * 当前用户是否是团长
     */
    private boolean isOwner;
    private String groupId;
    private ArrayList<String> memberHXIds;
    /**
     * 要被删除的对象的位置
     */
    private int positionToBeDeleted;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        initData();
        initTitle();
        initView();
    }

    @Override
    public void initTitle() {
        final TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("团员");
        title.setTitleNoRightButton();
        if (isOwner) {
            title.showAction(true);
            title.setActionText("编辑");
        }
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {

            }

            @Override
            public void onNavThreeClick() {

            }

            @Override
            public void onActionClick() {
                if (mAdapter.isCheckable()) {
                    title.setActionText("编辑");
                    mAdapter.setIsCheckable(false);
                } else {
                    title.setActionText("完成");
                    mAdapter.setIsCheckable(true);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBackClick() {

            }
        });
    }

    @Override
    public void initView() {

//        refresh = (SwipeRefreshLayout) findViewById(R.id.srl);
        rv_members = (RecyclerView) findViewById(R.id.rv_members);
        mAdapter = new MemberAdapter(this, members);
        rv_members.setAdapter(mAdapter);
        rv_members.setLayoutManager(new LinearLayoutManager(this));


        mAdapter.setOnMemberClickListener(new MemberAdapter.OnMemberClickListener() {
            @Override
            public void onMemberClick(View view, int position) {
//                当显示选择删除图标时，即进入编辑状态，此时屏蔽点击团成员的事件
                if (!mAdapter.isCheckable()) {
                    ToastUtils.showToast(context, "member");
                }
            }

            @Override
            public void onMemberDeleteClick(View view, final int position) {
//                减去显示团员数目的position
                currentPosition = position;
                positionToBeDeleted = position-1;
                showLogD("positionToBeDeleted : "+positionToBeDeleted);
//                自定义dialog
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle("删除团员")                                  //.withTitle(null)  no title
                        .withTitleColor("#FFFFFF")                                  //def
                        .withDividerColor("#11000000")                              //def
                        .withMessage("确定要删除该团员吗？")                     //.withMessage(null)  no Msg
                        .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                        .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                        .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                        .withDuration(500)                                          //def
                        .withEffect(Effectstype.Fadein)                                         //def Effectstype.Slidetop
                        .withButton1Text("确定")                                      //def gone
                        .withButton2Text("取消")                                  //def gone
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteAMember(memberHXIds.get(positionToBeDeleted));
                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void initData() {
        members = (List<UserBean.UserInfo>)getIntent().getSerializableExtra("members");
//        members对应的hxid
        memberHXIds = getIntent().getStringArrayListExtra("memberHXIds");
        groupId = getIntent().getStringExtra("groupId");
        isOwner = getIntent().getBooleanExtra("isOwner", false);
    }

    public void deleteAMember(String HXId){
        String url = FinalData.URL_VALUE+"delGroupMember";
        Map<String, Object> map = new HashMap<>();
        map.put("id", groupId);
        map.put("imId", HXId);

        CustomStringRequest cRequest = new CustomStringRequest(Request.Method.POST, url, map, getRespListener(), getErrorListener());
        VolleySingleton.getInstance(this).addToRequestQueue(cRequest);
    }

    private Response.Listener<String> getRespListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mAdapter.notifyItemRemoved(currentPosition);
//                数据列表内的值不包含“团员”这一item
                members.remove(positionToBeDeleted);
                memberHXIds.remove(positionToBeDeleted);
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast(getResources().getString(R.string.error_msg));
            }
        };
    }
}
