package com.dxj.student.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dxj.student.R;
import com.dxj.student.adapter.SubjectAdapter;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.SubjectBean;
import com.dxj.student.db.dao.SubjectDao;
import com.dxj.student.widget.TitleNavBar;

import java.util.List;

/**选择学团的科目
 * Created by khb on 2015/8/31.
 */
public class SubjectListActivity extends BaseActivity implements View.OnClickListener {

    private PopupWindow subjectList;
    private static final String DBNAME = "subject.db";
    private TextView subjectTitle;
    private List<SubjectBean> firstList;
    private SQLiteDatabase db;
    private List<SubjectBean> secondList;
    private List<SubjectBean> thirdList;
    private RecyclerView rv_first;
    private RecyclerView rv_second;
    private RecyclerView rv_thrid;
    private SubjectAdapter firstAdapter;
    private SubjectAdapter secondAdapter;
    private SubjectAdapter thirdAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        initTitle();
        initData();
        initView();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("选择科目");
        title.setTitleNoRightButton();
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {}

            @Override
            public void onNavTwoClick() { }

            @Override
            public void onNavThreeClick() {  }

            @Override
            public void onActionClick() { }

            @Override
            public void onBackClick() {
                setResult(RESULT_OK, new Intent().putExtra("subjectName", subjectName)
                        .putExtra("subjectFirst", subjectFirst)
                        .putExtra("subjectSecond", subjectSecond)
                        .putExtra("subjectThree", subjectThree));
                if (subjectList != null && subjectList.isShowing()) {
                    subjectList.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra("subjectName", subjectName)
                .putExtra("subjectFirst", subjectFirst)
                .putExtra("subjectSecond", subjectSecond)
                .putExtra("subjectThree", subjectThree));
        if (subjectList != null && subjectList.isShowing()){
            subjectList.dismiss();
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public void initView() {
        subjectTitle = (TextView) findViewById(R.id.tv_subject_title);
        subjectTitle.setOnClickListener(this);
//        subjectList = getPopWindow();
    }

    public PopupWindow getPopWindow() {
        PopupWindow popSubject = new PopupWindow(this);
        View popView = getLayoutInflater().inflate(R.layout.popup_subject_list, null);
        showLogD("subjectTitle.getWidth()" + subjectTitle.getWidth());
        popSubject.setWidth(subjectTitle.getWidth());
        popSubject.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popSubject.setFocusable(true);
        popSubject.setTouchable(true);
        popSubject.setBackgroundDrawable(new BitmapDrawable());
        popSubject.setOutsideTouchable(false);
        popSubject.setContentView(popView);
        popSubject.setAnimationStyle(R.style.popwindow_anim_scale_slide);

        rv_first = (RecyclerView) popView.findViewById(R.id.rv_first_category);
        rv_second = (RecyclerView) popView.findViewById(R.id.rv_second_category);
        rv_thrid = (RecyclerView) popView.findViewById(R.id.rv_third_category);
        firstAdapter = new SubjectAdapter(this, firstList);
        rv_first.setLayoutManager(new LinearLayoutManager(this));
        rv_first.setAdapter(firstAdapter);
        firstAdapter.setOnSubjectItemClickListener(new SubjectAdapter.OnSubjectItemClickListener() {

            @Override
            public void onSubjectItemClick(View view, int position) {
                SubjectBean firstSubject = firstList.get(position);
//                TextView name = (TextView) view;
//                showLogD("firstList.get(position) " + firstSubject.getName() + " name " + name.getText());
                subjectFirst = firstSubject.getId();
                setSelectedItem(firstSubject, firstList, position);

                secondList = SubjectDao.getChildCategoryFromParent(db, firstSubject.getId());
                if (secondList.get(0) != null) {
                    thirdList = SubjectDao.getChildCategoryFromParent(db, secondList.get(0).getId());
                }
                secondAdapter = new SubjectAdapter(activity, secondList);
                thirdAdapter = new SubjectAdapter(activity, thirdList);
                rv_second.setAdapter(secondAdapter);
                rv_second.setLayoutManager(new LinearLayoutManager(activity));
                rv_thrid.setAdapter(thirdAdapter);
                rv_thrid.setLayoutManager(new LinearLayoutManager(activity));
                if (secondList.size() == 1){    //如果二级目录只有一个，直接设为选中
                    subjectSecond = secondList.get(0).getId();
                    setSelectedItem(secondList.get(0), secondList, 0);
                }
                secondAdapter.setOnSubjectItemClickListener(new SubjectAdapter.OnSubjectItemClickListener() {
                    @Override
                    public void onSubjectItemClick(View view, int position) {
                        setSecondAdapterClick(view, position);
                    }
                });
                thirdAdapter.setOnSubjectItemClickListener(new SubjectAdapter.OnSubjectItemClickListener() {
                    @Override
                    public void onSubjectItemClick(View view, int position) {
                        setThirdAdapterClick(view, position);
                    }
                });
                firstAdapter.notifyDataSetChanged();
                secondAdapter.notifyDataSetChanged();
                thirdAdapter.notifyDataSetChanged();

            }
        });
        return popSubject;
    }

    /**
     * 设置该item是否被选中 ，其它item被选择状态重置为未选中
     * @param subjectBean
     * @param list
     * @param position
     */
    private void setSelectedItem(SubjectBean subjectBean, List<SubjectBean> list, int position) {
        subjectBean.setIsSelected(true);
        for (int i = 0; i < list.size(); i++) {
            if (i != position) {
                list.get(i).setIsSelected(false);
            }
        }
    }

    /**
     * 设置二级目录item点击事件
     * @param view
     * @param position
     */
    private void setSecondAdapterClick(View view, int position) {
        SubjectBean secondSubject = secondList.get(position);
//        TextView name = (TextView) view;
        subjectSecond = secondSubject.getId();
        setSelectedItem(secondSubject, secondList, position);
        thirdList.clear();
        thirdList.addAll(SubjectDao.getChildCategoryFromParent(db, secondSubject.getId()));
        secondAdapter.notifyDataSetChanged();
        thirdAdapter.notifyDataSetChanged();

    }
    /**
     * 设置三级目录item点击事件
     * @param view
     * @param position
     */
    private void setThirdAdapterClick(View view, int position) {
        SubjectBean thirdSubject = thirdList.get(position);
//        TextView name = (TextView) view;
        subjectThree = thirdSubject.getId();
        subjectName = thirdSubject.getName();
        setSelectedItem(thirdSubject, thirdList, position);
        thirdAdapter.notifyDataSetChanged();
//        if (subjectList != null && subjectList.isShowing()){
//            subjectList.dismiss();
//        }
    }


    @Override
    public void initData() {
        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        firstList = SubjectDao.getFirstCategory(db);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_subject_title:
                showLogD("subject");
                if (subjectList != null && subjectList.isShowing()){
                    subjectList.dismiss();
                }else {
                    subjectList = getPopWindow();
                    subjectList.showAsDropDown(subjectTitle);
                }
                break;


        }
    }


}
