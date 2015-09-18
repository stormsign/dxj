package com.dxj.student.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.dxj.student.application.MyApplication;
import com.umeng.message.PushAgent;

/**项目
 * Created by khb on 2015/8/19.
 */
public abstract class BaseActivity extends FinalActivity {

    protected MyApplication mApplication;
    protected Context context;
    protected Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = MyApplication.getInstance();
        context = this;
        activity = this;
        PushAgent.getInstance(context).onAppStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void back(View view){
        finish();
    }

//        处理消息栏

    /**
     * 初始化标题栏
     */
    public abstract void initTitle();

    /**
     * 处理页面展示
     */
    public abstract void initView();

    /**
     * 处理数据
     */
    public abstract void initData();





}
