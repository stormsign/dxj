package com.dxj.student.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxj.student.R;


/** 空内容界面
 * Created by khb on 2015/9/22.
 */
public class ExceptionView extends RelativeLayout {

    public final static int EMPTY = 1;
    public final static int NO_NETWORK = 2;
    public final static int LOADING = 3;

    private Context context;
    private TextView empty_text;
    private TextView empty_subtext;
    private TextView action;
    private ImageView exception_icon;

    public ExceptionView(Context context) {
        this(context, null);
        this.context = context;
        initView();
    }

    public ExceptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View emptyView = LayoutInflater.from(context).inflate(R.layout.exceptionview, this);
        action = (TextView) emptyView.findViewById(R.id.empty_action);
        exception_icon = (ImageView)emptyView.findViewById(R.id.exception_icon);
        empty_text = (TextView) emptyView.findViewById(R.id.tv_empty_text);
        empty_subtext = (TextView) emptyView.findViewById(R.id.tv_empty_subtext);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmptyActionListener != null) {
                    mOnEmptyActionListener.onEmptyAction();
                }
            }
        });
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setException(int exceptionType){
        switch (exceptionType){
            case EMPTY:
                break;
            case NO_NETWORK:
                setEmptyText(context.getResources().getString(R.string.no_network));
                setEmptySubtext(context.getResources().getString(R.string.no_network_sub));
                setEmptyActionText(context.getResources().getString(R.string.reload));
                exception_icon.setImageResource(R.mipmap.teshu_ico_wifi);
                break;
            default:
                break;
        }

    }

    public void setEmptyText(String text) {
        empty_text.setText(text);
    }

    public void setEmptySubtext(String text) {
        empty_subtext.setText(text);
    }

    public void setEmptyActionText(String text){
        action.setText(text);
    }

    public void showEmptyAction(boolean isShow){
        if (isShow){
            action.setVisibility(VISIBLE);
        }else {
            action.setVisibility(GONE);
        }
    }

    public interface OnEmptyActionListener{
        void onEmptyAction();
    }

    private OnEmptyActionListener mOnEmptyActionListener;

    public void setOnEmptyActionListener(OnEmptyActionListener mOnEmptyActionListener){
        this.mOnEmptyActionListener = mOnEmptyActionListener;
    }

}
