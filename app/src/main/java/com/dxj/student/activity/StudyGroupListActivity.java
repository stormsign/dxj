package com.dxj.student.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxj.student.R;
import com.dxj.student.adapter.FirstGroupCategoryAdapter;
import com.dxj.student.adapter.SecondGroupCategoryAdapter;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.StudyGroup;
import com.dxj.student.bean.SubjectBean;
import com.dxj.student.db.dao.SubjectDao;
import com.dxj.student.fragment.GroupListFragment;
import com.dxj.student.widget.TitleNavBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/31.
 */
public class StudyGroupListActivity extends BaseActivity {

    private int currentPage = 1;
    private final static int pageSize = 10;
    private SQLiteDatabase db;
    private List<SubjectBean> firstList;

    private static final String DBNAME = "subject.db";
    private static final String RECOMMENDED = "recommended" ;

    private List<StudyGroup> groupList;
    private DrawerLayout mDrawerLayout;
    private TextView tv_category;
    private ImageView iv_category;
    private RelativeLayout rl_gragment;
    private RelativeLayout drawer;
    private TextView drawer_title;
    private RecyclerView rv_first;
    private RecyclerView rv_second;
    private FirstGroupCategoryAdapter firstAdapter;
    private List<String> titleList;
    private List<Boolean> titleSelectedList;
    private List<SubjectBean> secondList;
    private List<String> secondTitleList;
    private List<Boolean> secondTitleSelectedList;
    private SecondGroupCategoryAdapter secondAdapter;
    private String firstName;
    private String secondName;
    private final static String DEFAULTNAME = "推荐学团";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studygruoplist);
        initData();
        initTitle();
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        注释此行，不保存Fragment状态，这样当该activity被回收时，attach的Fragment也会一通被回收
//        此段代码是为了解决fragment.getActivity()返回为null的问题
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.setTitle("交友学团");
        title.showSearchBar(true);
//        title.showNavThree(false);
        title.setSearchHint("输入老师手机号或学团名称");
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {
            }

            @Override
            public void onNavTwoClick() {
            }

            @Override
            public void onNavThreeClick() {
                startActivity(new Intent(context, CreateStudyGroupActivity.class));
            }

            @Override
            public void onActionClick() {
            }

            @Override
            public void onBackClick() {
            }
        });
        findViewById(R.id.et_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GroupSearchActivity.class));
            }
        });
    }

    @Override
    public void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_groups);
        tv_category = (TextView) findViewById(R.id.tv_group_category);
        iv_category = (ImageView) findViewById(R.id.iv_group_category);
        rl_gragment = (RelativeLayout) findViewById(R.id.rl_fragment_groups);
        drawer = (RelativeLayout) findViewById(R.id.rl_drawer_category);
        drawer_title = (TextView) findViewById(R.id.tv_drawer_title);
        rv_first = (RecyclerView) findViewById(R.id.rv_first_category);
        rv_second = (RecyclerView) findViewById(R.id.rv_second_category);

        mDrawerLayout.setDrawerElevation(0.5f);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        rv_first.setLayoutManager(new LinearLayoutManager(this));
        rv_first.setAdapter(firstAdapter);
        rv_second.setLayoutManager(new GridLayoutManager(this, 2));
        rv_second.setAdapter(secondAdapter);

        tv_category.setText("推荐学团");
//        初始化时显示推荐学团
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_fragment_groups, GroupListFragment.newInstance(-1), RECOMMENDED).commit();
    }

    @Override
    public void initData() {
        groupList = new ArrayList<StudyGroup>();
        titleList = new ArrayList<>();
        titleSelectedList = new ArrayList<>();
        secondTitleList = new ArrayList<>();
        secondTitleSelectedList = new ArrayList<>();

        db = SQLiteDatabase.openDatabase(getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
//        获取一级分类对象
        firstList = SubjectDao.getFirstCategory(db);
//        获取一级分类名称，和是否选中的记录表
        titleList.clear();
        titleSelectedList.clear();
        for (SubjectBean subject:firstList) {
            titleList.add(subject.getName());
            titleSelectedList.add(false);
        }
//        初始化时选中第一个
//        titleSelectedList.set(0, true);
        titleList.add(0, DEFAULTNAME);
        titleSelectedList.add(0,true);
//        secondTitleList.add(DEFAULTNAME);
//        secondTitleSelectedList.add(false);

        getSecondListByParentId(-1);

        firstName = titleList.get(0);
        secondName = secondTitleList.get(0);

        List<GroupListFragment> fList = new ArrayList<>();
//        for (int i = 0; i< titleList.size();i++){
////            i == 0时是推荐学团
////            if (i == 0){    //-1表示fragment应该显示推荐学团
////                GroupListFragment fragment = GroupListFragment.newInstance(-1);
////                fList.add(fragment);
////            }
////            i > 0时是一级目录，这时由于有推荐学团，i-1对应一级目录的记录
////            else{
//                GroupListFragment fragment = GroupListFragment.newInstance(firstList.get(i-1).getId());
//                fList.add(fragment);
////            }
//        }
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
        if (parentId == -1){
            secondTitleList.add(DEFAULTNAME);
            secondTitleSelectedList.add(false);
        }else {
            secondList = SubjectDao.getChildCategoryFromParent(db, parentId);
//        获取二级分类名称，和是否选中的记录表
            for (SubjectBean subject : secondList) {
                secondTitleList.add(subject.getName());
                secondTitleSelectedList.add(false);
            }
//        secondTitleSelectedList.set(0, true);
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
                for (int i = 0; i < titleSelectedList.size(); i++) {
                    titleSelectedList.set(i, false);
                }
                firstName = titleList.get(position);
                titleSelectedList.set(position, true);
                firstAdapter.notifyDataSetChanged();
                if (position == 0){
                    getSecondListByParentId(-1);
                }else{
                    getSecondListByParentId(firstList.get(position-1).getId());
                }

                secondAdapter.notifyDataSetChanged();
            }
        });

        secondAdapter = new SecondGroupCategoryAdapter(this, secondTitleList, secondTitleSelectedList);
        secondAdapter.setOnSecondClickListener(new SecondGroupCategoryAdapter.OnSecondClickListener() {
            @Override
            public void onSecondClick(View view, int position) {
                for (int i = 0; i < secondTitleList.size(); i++){
                    secondTitleSelectedList.set(i, false);
                }
                if (!secondTitleList.get(position).equals(DEFAULTNAME)) {
                    secondName = secondTitleList.get(position);
                    getSupportFragmentManager().beginTransaction().replace(R.id.rl_fragment_groups, GroupListFragment.newInstance(secondList.get(position).getId()), secondName).commit();
//                展示该二级目录下的学团
                }else{
                    secondName = "";    //避免重复出现 推荐学团
                    getSupportFragmentManager().beginTransaction().replace(R.id.rl_fragment_groups, GroupListFragment.newInstance(-1), RECOMMENDED).commit();
                }
                secondTitleSelectedList.set(position, true);
                secondAdapter.notifyDataSetChanged();
                tv_category.setText(firstName + " " + secondName);
//                选完后关闭侧滑菜单
                mDrawerLayout.closeDrawer(drawer);
            }
        });


    }

    public void showMenu(View view){
        if (!mDrawerLayout.isDrawerOpen(drawer)){
            mDrawerLayout.openDrawer(drawer);
        }

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
