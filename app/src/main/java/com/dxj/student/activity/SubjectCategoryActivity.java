package com.dxj.student.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.dxj.student.R;
import com.dxj.student.adapter.FirstGroupCategoryAdapter;
import com.dxj.student.adapter.SecondGroupCategoryAdapter;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.SubjectBean;
import com.dxj.student.db.dao.SubjectDao;
import com.dxj.student.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/31.
 * 科目
 */
public class SubjectCategoryActivity extends BaseActivity {


    private SQLiteDatabase db;
    private List<SubjectBean> firstList;

    private static final String DBNAME = "subject.db";


    private RecyclerView rv_first;
    private RecyclerView rv_second;
    private FirstGroupCategoryAdapter firstAdapter;
    private List<String> titleList;
    private List<Boolean> titleSelectedList;
    private List<SubjectBean> secondList;
    private List<String> secondTitleList;
    private List<Boolean> secondTitleSelectedList;//记录是否选中
    private SecondGroupCategoryAdapter secondAdapter;
    private String strTitle;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_category);
        initData();
        initTitle();
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("科目");
        title.showSearchBar(false);
        title.setTitleNoRightButton();
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
            }

            @Override
            public void onBackClick() {
            }
        });
    }

    @Override
    public void initView() {

        rv_first = (RecyclerView) findViewById(R.id.rv_first_category);
        rv_second = (RecyclerView) findViewById(R.id.rv_second_category);


        rv_first.setLayoutManager(new LinearLayoutManager(this));
        rv_first.setAdapter(firstAdapter);
        rv_second.setLayoutManager(new GridLayoutManager(this, 2));
        rv_second.setAdapter(secondAdapter);

    }

    @Override
    public void initData() {
        int category = getIntent().getIntExtra("category", -1);
        category=1;
        strTitle = getIntent().getStringExtra("categoryTitle");
        titleList = new ArrayList<>();
        titleSelectedList = new ArrayList<>();
        secondTitleList = new ArrayList<>();
        secondTitleSelectedList = new ArrayList<>();

        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
//        获取一级分类对象
        firstList = SubjectDao.getChildCategoryFromParent(db, category);
//        获取一级分类名称，和是否选中的记录表
        for (SubjectBean subject : firstList) {
            titleList.add(subject.getName());
            titleSelectedList.add(false);
        }
//        初始化时选中第一个
        titleSelectedList.set(0, true);


        getSecondListByParentId(firstList.get(0).getId());


//        List<GroupListFragment> fList = new ArrayList<>();

        initMenu();

    }

    private void getSecondListByParentId(int parentId) {
        if (secondList != null) {
            secondList.clear();
        }
        if (secondTitleList != null) {
            secondTitleList.clear();
        }
        if (secondTitleSelectedList != null) {
            secondTitleSelectedList.clear();
        }
//        获取二级分类对象

        secondList = SubjectDao.getChildCategoryFromParent(db, parentId);
//        获取二级分类名称，和是否选中的记录表
        for (SubjectBean subject : secondList) {
            secondTitleList.add(subject.getName());
            secondTitleSelectedList.add(false);
        }
    }

    /**
     * 设置侧滑菜单的点击事件
     */
    private void initMenu() {

        firstAdapter = new FirstGroupCategoryAdapter(this, titleList, titleSelectedList);
        firstAdapter.setOnFirstClickListener(new FirstGroupCategoryAdapter.OnFirstClickListener() {
            @Override
            public void onFirstClick(View view, int position) {
                if (index >= 0) {
                    titleSelectedList.set(index, false);
                } else {
                    titleSelectedList.set(0, false);
                }
                if (index == position) {
                    return;
                }
                index = position;
                titleSelectedList.set(position, true);
                firstAdapter.notifyDataSetChanged();
                getSecondListByParentId(firstList.get(position).getId());
                secondAdapter.notifyDataSetChanged();
            }
        });

        secondAdapter = new SecondGroupCategoryAdapter(this, secondTitleList, secondTitleSelectedList);
        secondAdapter.setOnSecondClickListener(new SecondGroupCategoryAdapter.OnSecondClickListener() {
            @Override
            public void onSecondClick(View view, int position) {
                for (int i = 0; i < secondTitleList.size(); i++) {
                    secondTitleSelectedList.set(i, false);
                }
                secondTitleSelectedList.set(position, true);
                secondAdapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
