package com.dxj.student.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dxj.student.R;
import com.dxj.student.adapter.DelatilAdapter;
import com.dxj.student.base.BaseFragment;
import com.dxj.student.bean.ClassWayBean;
import com.dxj.student.bean.CourseSubjectBean;

import java.util.ArrayList;

/**
 * Created by khb on 2015/8/19.
 */
public class DetailCourseFragment extends BaseFragment {
    private LinearLayout linearCourse;
    //    private DelatilCourseAdapter courseAdapter;
    private DelatilAdapter courseAdapter;
    private ArrayList<CourseSubjectBean> lists = new ArrayList<>();

    @Override
    public void initData() {
        lists = (ArrayList<CourseSubjectBean>) getArguments().getSerializable("course");
//        Log.i("TAG", "listsSSS=" + lists.size());
//        courseAdapter = new DelatilAdapter(getActivity(),lists);
//        recyclerViewCourse.setAdapter(courseAdapter);
        addSubject(lists);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_course, null);
        linearCourse = (LinearLayout) view.findViewById(R.id.linear_subject);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            recyclerViewCourse.setNestedScrollingEnabled(true);
//        }
        return view;
    }

    private void addSubject(ArrayList<CourseSubjectBean> list) {
        int size = list.size();


        for (int i = 0; i < size; i++) {
            int intPrice = 0;
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_detail_course, null);
            TextView name = (TextView) view.findViewById(R.id.tv_name);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
            TextView describe = (TextView) view.findViewById(R.id.tv_describe);
            name.setText(list.get(i).getSubjectName());
            describe.setText(list.get(i).getRemark());
            intPrice = list.get(i).getClassWay().get(0).getPrice();
            for (int p = 0; p < list.get(i).getClassWay().size(); p++) {
                ClassWayBean classWayBean = list.get(i).getClassWay().get(p);
                int classprice = Integer.valueOf(classWayBean.getPrice());
                if (classprice < intPrice) {
                    intPrice = classprice;
                }
            }
            tvPrice.setText("¥" + String.valueOf(intPrice));
            linearCourse.addView(view);
        }
    }

}
