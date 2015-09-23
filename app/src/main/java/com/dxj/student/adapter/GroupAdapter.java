package com.dxj.student.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.student.R;
import com.dxj.student.base.BaseRecyclerViewAdapter;
import com.dxj.student.bean.StudyGroup;
import com.dxj.student.bean.UserBean;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2015/9/4.
 */
public class GroupAdapter extends BaseRecyclerViewAdapter<StudyGroup> {

    private SQLiteDatabase db;
    private Activity mContext;
    private List<StudyGroup> mList;
    private LayoutInflater mLayoutInflater;
    private static final String DBNAME = "subject.db";
    private int parentId;

    public GroupAdapter(Activity mContext, List mList, int parentId) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        db = SQLiteDatabase.openDatabase(mContext.getFilesDir() + "/" + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        this.parentId = parentId;
    }

    public static interface OnGroupClickListener {
        void onGroupClick(View view, int position);
//        void onMoreClick(View view, int position);
    }

    private OnGroupClickListener mOnGroupClickListener;

    public void setOnGroupClickListener(OnGroupClickListener mOnGroupClickListener){
        this.mOnGroupClickListener = mOnGroupClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new GroupItemHolder(mLayoutInflater.inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StudyGroup group = mList.get(position);
        if (null == group){
            return ;
        }
        GroupItemHolder mHolder = (GroupItemHolder) holder;
        if (!TextUtils.isEmpty(mList.get(position).getHeadUrl())) {
            Glide.with(mContext).load(mList.get(position).getHeadUrl()).placeholder(R.mipmap.default_image).into(mHolder.groupHead);
        }
        /**
         * 获取团长，也就是老师的资料，要另外再请求
         */
//        getLeaderName(mList.get(position).getTeacherId(), mHolder.leaderName);
//        if (parentId != -1) {
//            if (position == 0 || mList.get(position - 1).getSubjectSecond() != mList.get(position).getSubjectSecond()) {
//                mHolder.category.setText(SubjectDao.getCategoryNameById(db, (int) mList.get(position).getSubjectSecond()));
//                mHolder.second.setVisibility(View.VISIBLE);
//            }
//        }
        mHolder.groupName.setText(mList.get(position).getGroupName());
        mHolder.groupDesc.setText(mList.get(position).getDescription());
//        mHolder.leaderName.setText(mList.get(position).);
        if (mList.get(position).getMembers()!=null) {
            mHolder.memberCount.setText("团员："+mList.get(position).getMembers().size()+"人");
        }
    }

    /**
     * 获取这个团的团长资料
     * @param owner
     * @param tv
     */
    private void getLeaderName(String owner, TextView tv) {
        String url = FinalData.URL_VALUE_COMMON+"teacherInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("teacherId", owner);
        GsonRequest<UserBean.UserInfo> gsonRequest = new GsonRequest(Request.Method.POST, url,UserBean.UserInfo.class, map, getTeacherListener(tv), getTeacherErrorListener());
        VolleySingleton.getInstance(mContext).addToRequestQueue(gsonRequest);
    }

    private Response.Listener<UserBean.UserInfo> getTeacherListener(final TextView tv) {
        return new Response.Listener<UserBean.UserInfo>() {
            @Override
            public void onResponse(UserBean.UserInfo userInfo) {
                LogUtils.d(userInfo.toString());
                tv.setText(userInfo.getNickName());
            }
        };
    }

    private Response.ErrorListener getTeacherErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
    }


    public class GroupItemHolder extends RecyclerView.ViewHolder {

        public ImageView groupHead;
        public CardView cv_group;
        private TextView groupName;
        private final TextView leaderName;
        private final TextView memberCount;
        private final TextView groupDesc;
        /**
         * 显示二级目录
         */
//        private RelativeLayout second;
//        private TextView more;
//        private TextView category;

        public GroupItemHolder(View itemView) {
            super(itemView);
//            second = (RelativeLayout) itemView.findViewById(R.id.rl_second_category);
//            category = (TextView) itemView.findViewById(R.id.category_name);
//            more = (TextView) itemView.findViewById(R.id.more_group);
            groupHead = (ImageView) itemView.findViewById(R.id.group_head);
            groupName = (TextView) itemView.findViewById(R.id.group_name);
            leaderName = (TextView) itemView.findViewById(R.id.leader_name);
            memberCount = (TextView) itemView.findViewById(R.id.member_count);
            groupDesc = (TextView) itemView.findViewById(R.id.group_desc);

            cv_group = (CardView) itemView.findViewById(R.id.cv_group);

            if (mOnGroupClickListener != null){
                cv_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupClickListener.onGroupClick(v, getLayoutPosition());
                    }
                });
//                more.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mOnGroupClickListener.onMoreClick(v, getLayoutPosition());
//                    }
//                });
            }

        }

    }
}
