package com.dxj.student.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;


import com.dxj.student.R;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.fragment.LoginFragment;
import com.dxj.student.fragment.RightFragment;
import com.dxj.student.widget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 登录和注册
 *
 * @author Administrator
 */
public class LoginAndRightActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<String> titleList;
    private PagerSlidingTabStrip mTabs;
    private MyPagerAdapters adapter;
    private ViewPager mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        initView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        init();
    }

    private void init() {

        titleList = new ArrayList<String>();
        titleList.add("登陆");
        titleList.add("注册");
        // tab
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPage = (ViewPager) findViewById(R.id.vPagerS);

        adapter = new MyPagerAdapters(getSupportFragmentManager(), new SparseArray<Fragment>());
        // 根据title添加界面
        addFragment();
        // 设置tab选中底部颜色
        mTabs.setIndicatorColorResource(R.color.msg_color);
        // 设置tab长度
        mTabs.setInfoDataNum(adapter.fragmentsList.size());
        // 设置Viewpage属性
        mPage.setAdapter(adapter);
        mPage.setCurrentItem(0);
        mPage.setOffscreenPageLimit(adapter.fragmentsList.size());
        // 关联ViewPage
        // mTabs.setPageChangeView(mActivity, multipleActions);
//	mTabs.setPageChangeView(this);
        mTabs.setViewPager(mPage);
        mTabs.setCheckTextTab(0);
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // multipleActions.collapse();
    }

    @Override
    public void initTitle() {

    }

    private void addFragment() {
        LoginFragment loginFragment = new LoginFragment();
        RightFragment rightFragment = new RightFragment();
//	// NeighboursFragment neighboursFragment = NeighboursFragment.getInstance(multipleActions, flag, userId);
//	NeighboursFragment neighboursFragment = NeighboursFragment.getInstance(flag, userId);
//	if (flag == 1) {
//	    actionFragmentOne = ActionFragment.getInstance(0, flag, userId);
//	    adapter.fragmentsList.append(ACTION_BY_ME_CHILD_POSITION, actionFragmentOne);
//
//	} else {
//	    actionFragment = PlazaActionFragment.getInstance();
//	    adapter.fragmentsList.append(ACTION_BY_ME_CHILD_POSITION, actionFragment);
//	}
        adapter.fragmentsList.append(0, loginFragment);
        adapter.fragmentsList.append(1, rightFragment);
    }

    /**
     * ViewPages Fragment Adapter
     */
    public class MyPagerAdapters extends FragmentStatePagerAdapter {

        private SparseArray<Fragment> fragmentsList;

        public MyPagerAdapters(FragmentManager fm) {
            super(fm);
        }

        public MyPagerAdapters(FragmentManager fm, SparseArray<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        public SparseArray<Fragment> getSparseFragments() {
            return this.fragmentsList;
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
