package com.dxj.student.adapter;

import android.app.Activity;
import android.graphics.Color;
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
public class FirstGroupCategoryAdapter extends BaseRecyclerViewAdapter<String> {

    private Activity mContext;
    private List<String> mList;
    private List<Boolean> selectedList;
    private LayoutInflater mLayoutInflater;
    private FirstItemHolder mHolder;

    public FirstGroupCategoryAdapter(Activity mContext, List mList, List<Boolean> selectedList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
        this.selectedList = selectedList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static interface OnFirstClickListener {
        void onFirstClick(View view, int position);
    }

    private OnFirstClickListener mOnFirstClickListener;

    public void setOnFirstClickListener(OnFirstClickListener mOnNoticeClickListener){
        this.mOnFirstClickListener = mOnNoticeClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FirstItemHolder(mLayoutInflater.inflate(R.layout.item_first, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String firstName = mList.get(position);
        boolean isSelected = selectedList.get(position);
        if (null == firstName){
            return ;
        }
        mHolder = (FirstItemHolder) holder;
        mHolder.tv_firstname.setText(firstName);
//        if (position == 0){
//            mHolder.isSelected = true;
//        }
        if (isSelected){
            mHolder.tv_firstname.setBackgroundColor(Color.WHITE);
            mHolder.tv_firstname.setTextColor(mContext.getResources().getColor(R.color.text_orange2));
        }else{
            mHolder.tv_firstname.setBackgroundColor(Color.TRANSPARENT);
            mHolder.tv_firstname.setTextColor(mContext.getResources().getColor(R.color.text_color_848484));
        }
    }

//    public void setItemSelected(boolean isSelected){
//        mHolder.isSelected = isSelected;
//    }

    public class FirstItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_firstname;
//        public boolean isSelected = false;

        public FirstItemHolder(View itemView) {
            super(itemView);

            tv_firstname = (TextView) itemView.findViewById(R.id.tv_firstname);

            if (mOnFirstClickListener != null){
                tv_firstname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    mOnFirstClickListener.onFirstClick(v, getLayoutPosition());
                    }
                });
            }
        }

    }
}
