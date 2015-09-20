package com.dxj.student.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.student.R;
import com.dxj.student.application.MyApplication;
import com.dxj.student.base.BaseFragment;
import com.dxj.student.bean.ClassWayBean;
import com.dxj.student.bean.CourseSubjectBean;
import com.dxj.student.bean.StudyGroup;
import com.dxj.student.bean.TeacherInfoBean;
import com.dxj.student.bean.TeacherUserBean;
import com.dxj.student.bean.UserBean;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.HttpUtils;
import com.dxj.student.utils.MyUtils;
import com.dxj.student.widget.FlowLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/8/19.
 */
public class DetailFragment extends BaseFragment implements View.OnClickListener {
    public final static int PHOTO = 1;//点击进入相册页
    public final static int COURSE = 0;//点击进入课程页
    /**/
    private TextView tvAddressValue;//地址
    private TextView tvConstellation;//星座
    private TextView tvTeachAge;//教龄
    private TextView tvSchool;//学校
    private TextView tvMajor;//专业
    private TextView tvTeachCity;//授课区域
    private TextView tvExperience;//授课方式
    private TextView tvResult;//授课方式
    private FlowLayout alreadyFlowlayout;
    private FlowLayout flowlayoutSolveLabel;
    private LinearLayout linearPhoto;
    private LinearLayout linearStudy;
    private LinearLayout linearSubject;
    private TeacherInfoBean teacher;
    private TextView tvPhoto;//点击进入相册页面

    @Override
    public void initData() {
        teacher = (TeacherInfoBean) getArguments().getSerializable("teacherInfoBean");
        fillUi(teacher.getUserInfo());
        addStude(teacher.getGroup());
        addSubject(teacher.getGoodSubject());
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.activity_home_details, null);
//        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvAddressValue = (TextView) view.findViewById(R.id.tv_address_value);
        tvConstellation = (TextView) view.findViewById(R.id.tv_constellation_value);
//        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
//        tvRecomment = (TextView) view.findViewById(R.id.tv_recomment);
        tvTeachAge = (TextView) view.findViewById(R.id.tv_teachage_value);
        tvMajor = (TextView) view.findViewById(R.id.tv_major_value);
        tvTeachCity = (TextView) view.findViewById(R.id.tv_teach_city_value);
        tvSchool = (TextView) view.findViewById(R.id.tv_school_value);
        tvExperience = (TextView) view.findViewById(R.id.tv_experience);
        tvResult = (TextView) view.findViewById(R.id.tv_result);
        linearPhoto = (LinearLayout) view.findViewById(R.id.linear_photo);
        linearStudy = (LinearLayout) view.findViewById(R.id.linear_study);
        linearSubject = (LinearLayout) view.findViewById(R.id.linear_subject);
        alreadyFlowlayout = (FlowLayout) view.findViewById(R.id.already_flowlayout);
        flowlayoutSolveLabel = (FlowLayout) view.findViewById(R.id.flowlayout_solve_label);
        tvPhoto = (TextView) view.findViewById(R.id.tv_photo);
        tvPhoto.setOnClickListener(this);
//        sendRequestData();
        return view;
    }

    private void sendRequestData() {

        String urlPath = FinalData.URL_VALUE_COMMON + HttpUtils.TEACHERINFO;
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", MyApplication.getInstance().getUserId());
        GsonRequest<TeacherInfoBean> teacherinfo = new GsonRequest(Request.Method.POST, urlPath, TeacherInfoBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(teacherinfo);
    }

    private Response.Listener<TeacherInfoBean> getListener() {
        return new Response.Listener<TeacherInfoBean>() {
            @Override
            public void onResponse(TeacherInfoBean str) {
                Log.i("TAG", "str=" + str.getUserInfo().getHeadUrl());
                TeacherUserBean.UserInfo userInfo = str.getUserInfo();
                fillUi(userInfo);
                addStude(str.getGroup());
                addSubject(str.getGoodSubject());
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
//        tvName.setText(userInfo.getNickName());
//        Glide.with(getActivity()).load(userInfo.getHeadUrl()).centerCrop().into(imgAvatar);
//        tvRecomment.setText(userInfo.getRemark());
        tvAddressValue.setText(userInfo.getLivingCity());
        tvConstellation.setText(userInfo.getHoroscope());
        tvSchool.setText(userInfo.getSchool());
        tvTeachAge.setText(userInfo.getSchoolAge() + "年");
        tvMajor.setText(userInfo.getMajor());
        tvTeachCity.setText(userInfo.getLivingCity());
        tvExperience.setText(userInfo.getExperience());
        tvResult.setText(userInfo.getResult());
//        if (userInfo.get)
//        tvTeachType.setText(user);
        addChildTo(userInfo.getLabel());
        addChildToTwo(userInfo.getSolveLabel());
        showPhoto(userInfo.getImages());

    }

    /**
     * 他的学团
     *
     * @param list
     */
    private void addStude(ArrayList<StudyGroup> list) {
        for (int i = 0; i < list.size(); i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_detail_group, null);
            ImageView groupHead = (ImageView) view.findViewById(R.id.group_head);
            TextView groupName = (TextView) view.findViewById(R.id.tv_group_name);
            TextView groupDesc = (TextView) view.findViewById(R.id.group_desc);
            TextView groupNumber = (TextView) view.findViewById(R.id.group_number);
            groupName.setText(list.get(i).getSubjectName());
            groupDesc.setText(list.get(i).getDescription());
            groupNumber.setText(list.get(i).getMembers().size() + "/" + list.get(i).getMaxusers());
            Glide.with(getActivity()).load(list.get(i).getHeadUrl()).centerCrop().into(groupHead);
            linearStudy.addView(view);
        }
    }

    /**
     * 最牛科目 最多显示三个 超过三个显示更多按钮
     *
     * @param list
     */
    private void addSubject(ArrayList<CourseSubjectBean> list) {
        int size = list.size();
        if (size > 3)
            size = 3;

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
            linearSubject.addView(view);
        }
        if (list.size() > 3) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MyUtils.dip2px(getActivity(), 30));
            lp.gravity = Gravity.BOTTOM;
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER);
            textView.setText("全部");
            textView.setTextSize(13);
            textView.setTextColor(getResources().getColor(R.color.text_show_8b));
            textView.setBackgroundResource(R.color.text_bg_details);
            linearSubject.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击进入课程页面
                    if (onPhotoClick != null) {
                        onPhotoClick.setOnPhotoClick(COURSE);
                    }
                }
            });
        }
    }

    private void addChildTo(List<String> lists) {
        for (int i = 0; i < lists.size(); i++) {
            LinearLayout.LayoutParams layoutParamsImageMain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, MyUtils.dip2px(getActivity(), 30));

            Button btn = new Button(getActivity());
            btn.setHeight(MyUtils.dip2px(getActivity(), 30));
            btn.setTextSize(12);
            btn.setTextColor(getResources().getColorStateList(R.color.text_label_checked));
            btn.setBackgroundResource(R.drawable.checkable_selected_border_two);
//            btn.setLayoutParams(linearParams);
            StringBuilder sb = new StringBuilder();
            sb.append(lists.get(i));
            btn.setText(sb.toString());
            alreadyFlowlayout.addView(btn, layoutParamsImageMain);
            btn.setTag(i);
//            btn.setOnCheckedChangeWidgetListener(this);


        }
    }

    private void addChildToTwo(List<String> lists) {
        Log.i("TAG", "lists");
        for (int i = 0; i < lists.size(); i++) {
            LinearLayout.LayoutParams layoutParamsImageMain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, MyUtils.dip2px(getActivity(), 30));
            Button btn = new Button(getActivity());
            btn.setTextSize(12);
            btn.setTextColor(getResources().getColorStateList(R.color.text_labe_details));
            btn.setBackgroundResource(R.drawable.checkable_selected_border_three);
//            btn.setLayoutParams(linearParams);
            StringBuilder sb = new StringBuilder();
            sb.append(lists.get(i));
            btn.setText(sb.toString());
            flowlayoutSolveLabel.addView(btn, layoutParamsImageMain);
            btn.setTag(i);
//            btn.setOnCheckedChangeWidgetListener(this);
        }
    }

    private void showPhoto(List<String> lists) {
        if (lists != null && lists.size() > 0) {
            int count = lists.size();
            if (count > 4) {
                count = 4;
            }
            for (int i = 0; i < count; i++) {
                final ImageView pic = (ImageView) linearPhoto.getChildAt(i);
                if (lists.get(i) != null) {
                    Glide.with(getActivity()).load(lists.get(i)).centerCrop().into(pic);
                }
            }
            if (count <= 4) {
                ImageView pic;
                switch (count) {
                    case 0:
                        linearPhoto.setVisibility(View.GONE);
                        break;
                    case 1:
                        for (int i = 3; i > 0; i--) {
                            pic = (ImageView) linearPhoto.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        for (int i = 3; i > 1; i--) {
                            pic = (ImageView) linearPhoto.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                    case 3:
                        for (int i = 3; i > 2; i--) {
                            pic = (ImageView) linearPhoto.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_photo:
                if (onPhotoClick != null)
                    onPhotoClick.setOnPhotoClick(PHOTO);
                break;
        }
    }

    public interface OnPhotoClick {
        void setOnPhotoClick(int index);
    }

    private OnPhotoClick onPhotoClick;

    public void setOnPhotoClick(OnPhotoClick mOnPhotoClick) {
        this.onPhotoClick = mOnPhotoClick;
    }
}
