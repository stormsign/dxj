package com.dxj.student.activity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dxj.student.R;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.base.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/19.
 */
public class CoursesActivity extends BaseActivity {

    private ListView lv_first;
    private GridView gv_second;
    private List<String> first;
    private List<String> second1;
    private List<String> second2;
    private List<String> gvList;
    private MyListAdapter madpater;
    private MyGridAdapter mgadpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(CoursesActivity.this, "courses", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_course);
        initData();
        initView();
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {

        lv_first = (ListView) findViewById(R.id.lv_first_category);
        gv_second = (GridView) findViewById(R.id.gv_second_category);
        madpater = new MyListAdapter(this, first);
        lv_first.setAdapter(madpater);
        madpater.notifyDataSetChanged();
        gvList = new ArrayList<String>();
        gvList.addAll(second1);
        mgadpater = new MyGridAdapter(this, gvList);
        gv_second.setAdapter(mgadpater);
        mgadpater.notifyDataSetChanged();
        lv_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CoursesActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                if (position == 0){
                    gvList.clear();
                    gvList.addAll(second1);
                    mgadpater.notifyDataSetChanged();
                }else if (position == 1){
                    gvList.clear();
                    gvList.addAll(second2);
                    mgadpater.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void initData() {
        first = new ArrayList<String>();
        first.add("course1");
        first.add("course2");
        first.add("course3");
        first.add("course4");
        second1 = new ArrayList<String>();
        second1.add("course11");
        second1.add("course12");
        second1.add("course13");
        second1.add("course14");

        second2 = new ArrayList<String>();
        second2.add("course21");
        second2.add("course22");
        second2.add("course23");


    }

    public class MyListAdapter extends BaseListAdapter<String> {

        public MyListAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView tv = new TextView(context);
            tv.setText(list.get(position));
            convertView = tv;
            return convertView;
        }
    }

    public class MyGridAdapter extends BaseListAdapter<String>{

        public MyGridAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(context);
            tv.setText(list.get(position));
            convertView = tv;
            return convertView;
        }
    }

}
