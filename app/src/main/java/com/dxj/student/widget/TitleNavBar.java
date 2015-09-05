package com.dxj.student.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxj.student.R;


/**低級自定義標題欄
 * Created by khb on 2015/8/28.
 */
public class TitleNavBar extends RelativeLayout {

    private Context mContext;
    private LayoutInflater inflater;
    private TextView title;
    private RelativeLayout search_container;
    private EditText search;
    private ImageView nav_1;
    private ImageView nav_2;
    private ImageView nav_3;

    private boolean isSearchBar = false;
    private RelativeLayout container;

    public TitleNavBar(Context context) {
        this(context, null);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);

    }

    public TitleNavBar(Context context, AttributeSet attr){
        super(context, attr);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);

        initView();

    }

    public interface OnTitleNavClickListener{
        void onNavOneClick();
        void onNavTwoClick();
        void onNavThreeClick();
        void onBackClick();
    }

    private OnTitleNavClickListener mOnTitleNavClickListener;

    public void setOnTitleNavClickListener(OnTitleNavClickListener mOnTitleNavClickListener){
        this.mOnTitleNavClickListener = mOnTitleNavClickListener;
    }

    private void initView() {
        View view = inflater.inflate(R.layout.layout_title, this);
        container = (RelativeLayout) view.findViewById(R.id.rl_title_container);
        ImageView back = (ImageView) view.findViewById(R.id.back);
        title = (TextView) view.findViewById(R.id.tv_title);
        search_container = (RelativeLayout) view.findViewById(R.id.rl_search_title_container);
        search = (EditText) view.findViewById(R.id.et_search);
        nav_1 = (ImageView) view.findViewById(R.id.iv_nav_1);
        nav_1.setClickable(true);
        nav_2 = (ImageView) view.findViewById(R.id.iv_nav_2);
        nav_2.setClickable(true);
        nav_3 = (ImageView) view.findViewById(R.id.iv_nav_3);
        nav_3.setClickable(true);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTitleNavClickListener != null){
                    mOnTitleNavClickListener.onBackClick();
                }
                Activity activity = (Activity) mContext;
                activity.finish();
            }
        });
        if (!isSearchBar){
            search_container.setVisibility(View.GONE);
        }
        if (nav_1.getVisibility() == View.VISIBLE){
            nav_1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTitleNavClickListener != null){
                        mOnTitleNavClickListener.onNavOneClick();
                    }
                }
            });
        }
        if (nav_2.getVisibility() == View.VISIBLE){
            nav_2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTitleNavClickListener != null) {
                        mOnTitleNavClickListener.onNavTwoClick();
                    }
                }
            });
        }
        if (nav_3.getVisibility() == View.VISIBLE){
            nav_3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTitleNavClickListener != null){
                        mOnTitleNavClickListener.onNavThreeClick();
                    }
                }
            });
        }
    }


    /**
     * 設置標題
     * @param titleStr
     */
    public void setTitle(String titleStr){
        if (title != null) {
            title.setText(titleStr);
        }
    }

    /**
     * 設置是否顯示搜索條
     * @param isShow
     */
    public void showSearchBar(boolean isShow){
        if (isShow) {
            search_container.setVisibility(View.VISIBLE);
            nav_1.setVisibility(View.GONE);
            nav_2.setVisibility(View.GONE);
            isSearchBar = true;
        }else{
            search_container.setVisibility(View.GONE);
            nav_1.setVisibility(View.VISIBLE);
            nav_2.setVisibility(View.VISIBLE);
            isSearchBar = false;
        }
    }
//    設置按鈕圖片
    public void setNavOneImageResource(int resource){
        nav_1.setImageResource(resource);
    }
    public void setNavTwoImageResource(int resource){
        nav_2.setImageResource(resource);
    }
    public void setNavThreeImageResource(int resource){
        nav_3.setImageResource(resource);
    }
    /**
     *設置搜索條提示信息
     */
    public void setSearchHint(String hint){
        search.setHint(hint);
    }

    /**
     * 設置標題欄背景圖片
     * @param resource
     */
    public void setContainerBackgroundResource(int resource){
        container.setBackgroundResource(resource);
    }

    public void setTitleNoRightButton(){
        nav_1.setVisibility(GONE);
        nav_2.setVisibility(GONE);
    }
//    public void setContainerBackgroundColor(int resource){
//        container.set
//    }

}
