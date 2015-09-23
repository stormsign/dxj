package com.dxj.student.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.student.R;
import com.dxj.student.application.MyApplication;
import com.dxj.student.bean.TeacherInfoBean;
import com.dxj.student.bean.TeacherUserBean;
import com.dxj.student.fragment.DetailCourseFragment;
import com.dxj.student.fragment.DetailFragment;
import com.dxj.student.fragment.DetailPhotoFragment;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by kings on 9/8/2015.
 * 老师详情
 */
public class TeacherDetailsActivity extends AppCompatActivity implements DetailFragment.OnPhotoClick,View.OnClickListener{
    private RadioGroup radioGroup;
    private Fragment[] fragments;
    private int currentTabIndex = 0;
    private TextView tvName;
    private ImageView imgAvatar;
    private TextView tvRecomment;//介绍
    private RadioButton radioButtonPhoto;//相册
    private RadioButton radioButtonHome;//主页
    private RadioButton radioButtonCourse;//课程
    private Toolbar mToolbar;//
    private CollapsingToolbarLayout mCollapsingToolbarLayout;//
    private AppBarLayout appbar;//
    //底部按钮
    private LinearLayout linearCollect;//收藏
//    private AppBarLayout appbar;
//    private AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);
        initData();
        initView();
//        initFragment();
        sendRequestData();

    }

    //    @Override
    public void initTitle() {

    }

    //    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        Random random = new Random();
        ranSwitch(random.nextInt(10), appbar);
//        mCollapsingToolbarLayout.setTitle("老师详情");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherDetailsActivity.this.onBackPressed();
            }
        });
        linearCollect =(LinearLayout)findViewById(R.id.linear_collect);
        radioGroup = (RadioGroup) this.findViewById(R.id.radio_group);
        tvName = (TextView) findViewById(R.id.tv_name);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        tvRecomment = (TextView) findViewById(R.id.tv_recomment);
        radioButtonPhoto = (RadioButton) findViewById(R.id.list_holder_radio_button);
        radioButtonHome = (RadioButton) findViewById(R.id.basic_holder_radio_button);
        radioButtonCourse = (RadioButton) findViewById(R.id.grid_holder_radio_button);
        //
        linearCollect.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.basic_holder_radio_button:
                        showFragment(0);
                        break;
                    case R.id.list_holder_radio_button:
                        showFragment(1);

                        break;
                    case R.id.grid_holder_radio_button:
                        showFragment(2);

                        break;
                }
            }
        });
    }

    /**
     * 随机显示背景图片
     * @param i
     * @param appbar
     */
    private void ranSwitch(int i, AppBarLayout appbar) {
        Log.i("TAG", "i=" + i);
        int mipmap = 0;
        switch (i) {
            case 0:
                mipmap = R.mipmap.laoshixq_top_bg01;
                break;
            case 1:
                mipmap = R.mipmap.laoshixq_top_bg02;
                break;
            case 2:
                mipmap = R.mipmap.laoshixq_top_bg03;

                break;
            case 3:
                mipmap = R.mipmap.laoshixq_top_bg04;

                break;
            case 4:
                mipmap = R.mipmap.laoshixq_top_bg05;

                break;
            case 5:
                mipmap = R.mipmap.laoshixq_top_bg06;

                break;
            case 6:
                mipmap = R.mipmap.laoshixq_top_bg07;

                break;
            case 7:
                mipmap = R.mipmap.laoshixq_top_bg08;

                break;
            case 8:
                mipmap = R.mipmap.laoshixq_top_bg09;

                break;
            case 9:
                mipmap = R.mipmap.laoshixq_top_bg10;

                break;
        }
        appbar.setBackgroundResource(mipmap);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mipmap);
//        Palette palette = Palette.generate(bitmap);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(final Palette palette) {
                int defaultColor = getResources().getColor(R.color.text_price);
                int defaultTitleColor = getResources().getColor(android.R.color.white);
                int bgColor = palette.getDarkVibrantColor(defaultColor);
                int titleColor = palette.getLightVibrantColor(defaultTitleColor);
                mCollapsingToolbarLayout.setContentScrimColor(bgColor);
                mCollapsingToolbarLayout.setCollapsedTitleTextColor(titleColor);
                mCollapsingToolbarLayout.setExpandedTitleColor(titleColor);
                tvRecomment.setTextColor(titleColor);
            }
        });

    }

    private void sendRequestData() {

        String urlPath = FinalData.URL_VALUE_COMMON + HttpUtils.TEACHERINFO;
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", "c19b900c-2305-4d65-b456-d74ed0e9c1b6");
        GsonRequest<TeacherInfoBean> teacherinfo = new GsonRequest(Request.Method.POST, urlPath, TeacherInfoBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(teacherinfo);
    }

    private Response.Listener<TeacherInfoBean> getListener() {
        return new Response.Listener<TeacherInfoBean>() {
            @Override
            public void onResponse(TeacherInfoBean str) {
                Log.i("TAG", "str=" + str.getUserInfo().getHeadUrl());
                TeacherUserBean.UserInfo userInfo = str.getUserInfo();
                fillUi(userInfo);
//                addStude(str.getGroup());
//                addSubject(str.getGoodSubject());
                initFragment(str);
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        };
    }

    private void fillUi(TeacherUserBean.UserInfo userInfo) {
        mCollapsingToolbarLayout.setTitle(userInfo.getNickName());
        mCollapsingToolbarLayout.setTitleEnabled(true);
//        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));
//        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mToolbar.setTitle(userInfo.getNickName());
        tvName.setText(userInfo.getNickName());
        Glide.with(this).load(userInfo.getHeadUrl()).centerCrop().into(imgAvatar);
        tvRecomment.setText(userInfo.getRemark());
    }

    private void showFragment(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        currentTabIndex = index;

    }

    //    @Override
    public void initData() {
    }

    private void initFragment(TeacherInfoBean str) {
        DetailFragment homeFragment = getDetailFragment();
        homeFragment.setOnPhotoClick(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("teacherInfoBean", str);
        homeFragment.setArguments(bundle);

        DetailPhotoFragment meFragmetn = getDetailPhotoFragment();
        Bundle photoBundle = new Bundle();
        photoBundle.putStringArrayList("photos", str.getUserInfo().getImages());
        meFragmetn.setArguments(photoBundle);

        DetailCourseFragment plazaFragment = getDetailCourseFragment();

        Bundle courseBundle = new Bundle();
        courseBundle.putSerializable("course", str.getGoodSubject());
        plazaFragment.setArguments(courseBundle);

        fragments = new Fragment[]{homeFragment, meFragmetn, plazaFragment};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!plazaFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, plazaFragment, DetailCourseFragment.class.getName());
            fragmentTransaction.hide(plazaFragment);
        }
        if (!homeFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, homeFragment, DetailFragment.class.getName());
        }
        if (!meFragmetn.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, meFragmetn, DetailPhotoFragment.class.getName());
            fragmentTransaction.hide(meFragmetn);
        }
        if (!fragmentTransaction.isEmpty()) {
            fragmentTransaction.show(homeFragment).commit();
        }

    }

    public DetailFragment getDetailFragment() {
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DetailFragment.class.getName());
        if (fragment == null) {
            fragment = new DetailFragment();
        }
        return fragment;
    }

    public DetailPhotoFragment getDetailPhotoFragment() {
        DetailPhotoFragment fragment = (DetailPhotoFragment) getSupportFragmentManager().findFragmentByTag(DetailPhotoFragment.class.getName());
        if (fragment == null) {
            fragment = new DetailPhotoFragment();
        }
        return fragment;
    }

    public DetailCourseFragment getDetailCourseFragment() {
        DetailCourseFragment fragment = (DetailCourseFragment) getSupportFragmentManager().findFragmentByTag(DetailCourseFragment.class.getName());
        if (fragment == null) {
            fragment = new DetailCourseFragment();
        }
        return fragment;
    }


    @Override
    public void setOnPhotoClick(int index) {
        if (index == DetailFragment.COURSE) {
            radioButtonCourse.setChecked(true);
            showFragment(2);
        } else if (index == DetailFragment.PHOTO) {
            radioButtonPhoto.setChecked(true);
            showFragment(1);
        }
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.linear_collect:

                break;
        }
    }
}
