package com.dxj.student.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dxj.student.R;
import com.dxj.student.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class SecondGroupCategoryAdapter extends BaseRecyclerViewAdapter<String> {

    private Activity mContext;
    private List<String> mList;
    private List<Boolean> selectedList;
    private LayoutInflater mLayoutInflater;
    private SecondItemHolder mHolder;

    public SecondGroupCategoryAdapter(Activity mContext, List mList, List<Boolean> selectedList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
        this.selectedList = selectedList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static interface OnSecondClickListener {
        void onSecondClick(View view, int position);
    }

    private OnSecondClickListener mOnSecondClickListener;

    public void setOnSecondClickListener(OnSecondClickListener mOnSecondClickListener){
        this.mOnSecondClickListener = mOnSecondClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        填充item布局
        return new SecondItemHolder(mLayoutInflater.inflate(R.layout.item_second, parent, false));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String firstName = mList.get(position);
        boolean isSelected = selectedList.get(position);
        if (null == firstName){
            return ;
        }
        mHolder = (SecondItemHolder) holder;
        mHolder.tv_secondname.setText(firstName);
        if (position == 0){
            mHolder.isSelected = true;
        }
        if (isSelected){
            mHolder.tv_secondname.setTextColor(Color.WHITE);
            mHolder.tv_secondname.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_corner_orange_solid));
        }else{
            mHolder.tv_secondname.setTextColor(mContext.getResources().getColor(R.color.text_color_323232));
            mHolder.tv_secondname.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_corner_orange_border));
        }
    }

    public void setItemSelected(boolean isSelected){
        mHolder.isSelected = isSelected;
    }

    public class SecondItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_secondname;
        public boolean isSelected = false;

        public SecondItemHolder(View itemView) {
            super(itemView);

            tv_secondname = (TextView) itemView.findViewById(R.id.tv_secondname);

            if (mOnSecondClickListener != null){
                tv_secondname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    mOnSecondClickListener.onSecondClick(v, getLayoutPosition());
                    }
                });
            }
        }

    }
}
