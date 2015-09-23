package com.dxj.student.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxj.student.R;
import com.dxj.student.base.BaseRecyclerViewAdapter;
import com.dxj.student.bean.Notice;

import java.util.List;

/**
 * Created by khb on 2015/9/4.
 */
public class NoticeAdapter extends BaseRecyclerViewAdapter<Notice> {

    private Activity mContext;
    private List<Notice> mList;
    private LayoutInflater mLayoutInflater;
    private final int FOOTERVIEW = 2;
//    private boolean isShowFooterView = false;
    private boolean hasFooterView = false;

    public NoticeAdapter(Activity mContext, List mList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static interface OnNoticeClickListener {
        void onNoticeClick(View view, int position);
    }

    private OnNoticeClickListener mOnNoticeClickListener;

    public void setOnNoticeClickListener(OnNoticeClickListener mOnNoticeClickListener){
        this.mOnNoticeClickListener = mOnNoticeClickListener;
    }

    @Override
    public int getItemCount() {
//        if (hasFooterView) {
//            return mList.size() + 1;
//        }else{
            return mList.size();
//        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == mList.size()){
//                return FOOTERVIEW;
//        }else{
            return super.getItemViewType(position);
//        }
    }

    public void hasFooterView(boolean hasFooterView){
        this.hasFooterView = hasFooterView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == FOOTERVIEW){
//            return new FooterHolder(mLayoutInflater.inflate(R.layout.loadingview_footer, parent, false));
//        }else {
            return new NoticeItemHolder(mLayoutInflater.inflate(R.layout.item_notice, parent, false));
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoticeItemHolder) {
            Notice notice = mList.get(position);
            if (null == notice) {
                return;
            }
            NoticeItemHolder mHolder = (NoticeItemHolder) holder;
            mHolder.tv_title.setText(notice.getName());
            mHolder.tv_content.setText(notice.getDescription());
        }
//        else if (holder instanceof  FooterHolder) {
//            FooterHolder footerHolder = (FooterHolder) holder;
//            if (!isShowFooterView){
//                footerHolder.footer.setVisibility(View.GONE);
//            }
//        }
    }

    public class NoticeItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_title;
        public LinearLayout ll_notice;
        private TextView tv_content;


        public NoticeItemHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ll_notice = (LinearLayout) itemView.findViewById(R.id.lv_card);

            if (mOnNoticeClickListener != null){
                ll_notice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnNoticeClickListener.onNoticeClick(v, getLayoutPosition());
                    }
                });
            }
        }

    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        private RelativeLayout footer;
        public FooterHolder(View view) {
            super(view);
           footer = (RelativeLayout) view.findViewById(R.id.footer);
        }
    }
}
