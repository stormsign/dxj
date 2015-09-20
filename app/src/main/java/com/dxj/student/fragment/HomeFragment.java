package com.dxj.student.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.dxj.student.R;
import com.dxj.student.activity.LoginAndRightActivity;
import com.dxj.student.activity.SubjectCategoryActivity;
import com.dxj.student.activity.TeacherDetailsActivity;
import com.dxj.student.activity.UpdateUserInfoActivity;
import com.dxj.student.base.BaseFragment;


/**首页
 * Created by khb on 2015/8/19.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{

    @Override
    public void initData() {
//        List<String>
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_home, null);
      view.findViewById(R.id.userinfo).setOnClickListener(this);
      view.findViewById(R.id.btn_login).setOnClickListener(this);
      view.findViewById(R.id.btn_teacher_detail).setOnClickListener(this);
      view.findViewById(R.id.btn_teacher_subject).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
     int id = v.getId();
        switch (id){
            case R.id.userinfo:
                startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginAndRightActivity.class));
                break;
            case R.id.btn_teacher_detail:
                startActivity(new Intent(getActivity(), TeacherDetailsActivity.class));
                break;
            case R.id.btn_teacher_subject:
                startActivity(new Intent(getActivity(), SubjectCategoryActivity.class));
                break;
        }
    }
}

