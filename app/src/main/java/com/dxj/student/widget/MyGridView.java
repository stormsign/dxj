package com.dxj.student.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义gridview ，高度自适应， 可在ListView中正常嵌套展示
 * @author zjb
 *
 */
public class MyGridView extends GridView {
	
	private boolean hasScrollBar = true;
	
	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 
        int expandSpec = heightMeasureSpec;
        if (hasScrollBar) {
            expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);// 注意这里,这里的意思是直接测量出GridView的高度
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
	
	
}
