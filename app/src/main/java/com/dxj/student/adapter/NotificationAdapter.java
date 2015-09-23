package com.dxj.student.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dxj.student.R;
import com.dxj.student.base.BaseRecyclerViewAdapter;
import com.dxj.student.bean.Notification;
import com.easemob.chatuidemo.utils.DateUtils;

import java.sql.Date;
import java.util.List;

/**
 * Created by khb on 2015/9/1.
 */
public class NotificationAdapter extends BaseRecyclerViewAdapter<Notification> {

    public NotificationAdapter(Activity mContext, List<Notification> mList) {
        super(mContext, mList);
    }

    public interface OnNotificationItemClickListener {
        void onNotificationItemClick(View view, int position);
    }

    private OnNotificationItemClickListener mOnNotificationItemClickListener;

    public void setOnNotificationItemClickListener(OnNotificationItemClickListener mOnNotificationItemClickListener){
        this.mOnNotificationItemClickListener = mOnNotificationItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(mLayoutInflater.inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Notification notification = mList.get(position);
        if (null == notification){
            return ;
        }
        if (holder instanceof NotificationHolder){
            NotificationHolder mHolder = (NotificationHolder) holder;
            if (notification.isRead()){
                mHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.notification_read));
            }else{
                mHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.notification_unread));
            }
            mHolder.title.setText(notification.getTitle());
            mHolder.content.setText(notification.getContent());
            mHolder.receivedTime.setText(DateUtils.getTimestampString(new Date(notification.getTime())));
        }
    }

    public class NotificationHolder extends RecyclerView.ViewHolder{
        private TextView content;
        private TextView receivedTime;
        private TextView title;
        private CardView card;

        public NotificationHolder(View itemView){
            super(itemView);
            receivedTime = (TextView) itemView.findViewById(R.id.tv_received_time);
            title = (TextView) itemView.findViewById(R.id.notification_title);
            content = (TextView)itemView.findViewById(R.id.notification_content);
            card = (CardView) itemView.findViewById(R.id.card_notification);
            if (mOnNotificationItemClickListener != null){
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        card.setCardBackgroundColor(mContext.getResources().getColor(R.color.notification_read));
                        mOnNotificationItemClickListener.onNotificationItemClick(v, getLayoutPosition());
                    }
                });
            }
        }
    }
}
