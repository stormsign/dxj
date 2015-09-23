package com.dxj.student.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.student.R;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.BaseBean;
import com.dxj.student.http.CustomStringRequest;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.widget.TitleNavBar;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2015/8/29.
 */
public class CreateStudyGroupActivity extends BaseActivity implements View.OnClickListener {

    private static final int RETURN_SUBJECT = 1;
    private ImageView groupHead;

    /**
     * 一级科目id
     */
    private long subjectFirst;
    /**
     * 二级科目id
     */
    private long subjectSecond;
    /**
     * 三级科目id
     */
    private long subjectThree;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 学团头像
     */
    private String headUrl = "http://img.miuhouse.com/upload/head/20150901/a0b3d48e-325b-4d17-94fd-9d1980015cd3.JPEG";
    private String groupName;
    private String description;

    private ImageView arrow;
    private TextView tv_subjectName;
    private EditText et_groupName;
    private EditText et_description;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createstudygroup);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("创建学团");
        title.setTitleNoRightButton();
    }

    @Override
    public void initView() {
        groupHead = (ImageView) findViewById(R.id.group_head);
        Glide.with(this).load(headUrl).into(groupHead);
        LinearLayout groupSubject = (LinearLayout) findViewById(R.id.ll_group_subject);
        arrow = (ImageView) findViewById(R.id.arrow);
        tv_subjectName = (TextView) findViewById(R.id.tv_subjectName);
        et_groupName = (EditText) findViewById(R.id.et_groupName);
        et_description = (EditText) findViewById(R.id.et_description);
        groupSubject.setClickable(true);

        groupHead.setOnClickListener(this);
        groupSubject.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    public void createGroup(View view){
        showLogD(" ------- create group");
        if (TextUtils.isEmpty(headUrl)){
            showToast("请给学团设置一个头像");
            return ;
        }
        if (TextUtils.isEmpty(subjectName)){
            showToast("你还没有选择学团科目");
            return ;
        }
        if (!TextUtils.isEmpty(et_groupName.getText())){
            groupName = et_groupName.getText().toString().trim();
        }else{
            showToast("学团名字还没起呢");
            return ;
        }
        if (!TextUtils.isEmpty(et_description.getText())) {
            description = et_description.getText().toString().trim();
        }else{
            description = getResources().getString(R.string.leader_is_very_very_lazy);
        }
        create();
    }

    private void create() {
        String url = FinalData.URL_VALUE+"createGroup";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("teacherId", mApplication.getUserId());
        map.put("groupName", groupName);
        map.put("headUrl", headUrl);
        map.put("description", description);
        map.put("subjectFirst", subjectFirst);
        map.put("subjectSecond", subjectSecond);
        map.put("subjectThree", subjectThree);
        map.put("subjectName", subjectName);
        map.put("mobile", mApplication.getUserBean().getUserInfo().getMobile());
        showLogI("MAP "+groupName+" "+headUrl+" "+description+" "+subjectFirst+" "+subjectSecond+" "+subjectThree+" "+subjectName+" "+mobile);
        CustomStringRequest cRequest = new CustomStringRequest(Request.Method.POST, url, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(context).addToRequestQueue(cRequest);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                showLogD("response "+s);
                Gson gson = new Gson();
                BaseBean msg= gson.fromJson(s, BaseBean.class);
                if (msg.getCode() == 0){
                    showToast(msg.getMsg());
                    finish();
                }
            }
        };
    }


    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showLogE(volleyError.toString());
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == RETURN_SUBJECT){
//                判断第一次选了科目没提交，第二次进了科目页面却没有选科目的情况，此时保留第一次的科目
                if (!TextUtils.isEmpty(data.getStringExtra("subjectName"))){
                    subjectFirst = data.getLongExtra("subjectFirst", -1);
                    subjectSecond = data.getLongExtra("subjectSecond", -1);
                    subjectThree = data.getLongExtra("subjectThree", -1);
                    subjectName = data.getStringExtra("subjectName");
                    tv_subjectName.setText(subjectName);
                    tv_subjectName.setVisibility(View.VISIBLE);
                    arrow.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_head:
                showLogD(" 上传群头像");
                uploadImg();
                break;
            case R.id.ll_group_subject:
                showLogD(" 科目");
                startActivityForResult(new Intent(context, SubjectListActivity.class), RETURN_SUBJECT);
                break;
        }

    }

    private void uploadImg() {

    }
}
