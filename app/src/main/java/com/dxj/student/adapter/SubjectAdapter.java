package com.dxj.student.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dxj.student.R;
import com.dxj.student.base.BaseRecyclerViewAdapter;
import com.dxj.student.bean.SubjectBean;

import java.util.List;

/**
 * Created by khb on 2015/9/1.
 */
public class SubjectAdapter extends BaseRecyclerViewAdapter<SubjectBean> {

    public SubjectAdapter(Activity mContext, List<SubjectBean> mList) {
        super(mContext, mList);
    }

    public interface OnSubjectItemClickListener{
        void onSubjectItemClick(View view, int position);
    }

    private OnSubjectItemClickListener mOnSubjectItemClickListener;

    public void setOnSubjectItemClickListener(OnSubjectItemClickListener mOnSubjectItemClickListener){
        this.mOnSubjectItemClickListener = mOnSubjectItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubjectHolder(mLayoutInflater.inflate(R.layout.item_subject, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SubjectBean subjectBean = mList.get(position);
        if (null == subjectBean){
            return ;
        }
        if (holder instanceof SubjectHolder){
            SubjectHolder subjectHolder = (SubjectHolder) holder;
            subjectHolder.name.setText(subjectBean.getName());
            if (subjectBean.isSelected()){
                subjectHolder.name.setTextColor(mContext.getResources().getColor(R.color.orange));
            }else{
                subjectHolder.name.setTextColor(mContext.getResources().getColor(R.color.abc_primary_text_material_light));
            }
        }
    }

    public class SubjectHolder  extends RecyclerView.ViewHolder{
        public TextView name;
        public boolean isSelected;

        public SubjectHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            if (mOnSubjectItemClickListener != null){
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isSelected = true;
                        mOnSubjectItemClickListener.onSubjectItemClick(v, getLayoutPosition());
                    }
                });
            }
        }
    }
}
