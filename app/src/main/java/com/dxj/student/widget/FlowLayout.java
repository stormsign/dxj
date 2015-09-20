package com.dxj.student.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dxj.student.R;


public class FlowLayout extends ViewGroup {
    private static final int DEFAULT_HORIZONTAL_SPACING = 5;
    private static final int DEFAULT_VERTICAL_SPACING = 5;
    private int mVerticalSpacing;
    private int mHorizontalSpacing;

    public FlowLayout(Context context) {
	super(context);
	// TODO Auto-generated constructor stub
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
	super(context, attrs, defStyleAttr);
	// TODO Auto-generated constructor stub
    }

    public FlowLayout(Context context, AttributeSet attrs) {
	super(context, attrs);
	// TODO Auto-generated constructor stub
	TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
	mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontal_spacing, DEFAULT_HORIZONTAL_SPACING);
	mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_vertical_spacing, DEFAULT_VERTICAL_SPACING);
	a.recycle();
    }

    public void setHorizontalSpacing(int pixelSize) {
	mHorizontalSpacing = pixelSize;
    }

    public void setVerticalSpacing(int pixelSize) {
	mVerticalSpacing = pixelSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// TODO Auto-generated method stub
	int myWidth = resolveSize(0, widthMeasureSpec);
	Log.i("TAG", "myWidth=" + myWidth);
	int paddingLeft = getPaddingLeft();
	int paddingTop = getPaddingTop();
	int paddingRight = getPaddingRight();
	int paddingBottom = getPaddingBottom();
	int childLeft = paddingLeft;
	int childTop = paddingTop;
	int lineHeight = 0;

	// Measure each child and put the child to the right of previous child
	// if there's enough room for it, otherwise, wrap the line and put the child to next line
	for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {

	    View childView = getChildAt(i);
	    LayoutParams childLayoutParams = childView.getLayoutParams();
	    childView.measure(getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childLayoutParams.width),
		    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childLayoutParams.height));
	    int childWidth = childView.getMeasuredWidth();
	    int childHeight = childView.getMeasuredHeight();

	    lineHeight = Math.max(childHeight, lineHeight);

	    if (childLeft + childWidth + paddingRight > myWidth) {
		childLeft = paddingLeft;
		childTop += mVerticalSpacing + lineHeight;
		lineHeight = childHeight;
	    } else {
		childLeft += childWidth + mHorizontalSpacing;
	    }
	}

	int wantedHeight = childTop + lineHeight + paddingBottom;
	setMeasuredDimension(myWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	// TODO Auto-generated method stub

	int myWidth = r - l;
	int paddingLeft = getPaddingLeft();
	int paddingTop = getPaddingTop();
	int paddingRight = getPaddingRight();
	int childLeft = paddingLeft;
	int childTop = paddingTop;
	int lineHeight = 0;
	for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
	    View childView = getChildAt(i);
	    if (childView.getVisibility() == View.GONE) {
		continue;
	    }
	    int childWidth = childView.getMeasuredWidth();
	    int childHeight = childView.getMeasuredHeight();

	    lineHeight = Math.max(childHeight, lineHeight);
	    if (childLeft + childWidth + paddingRight > myWidth) {
		childLeft = paddingLeft;
		childTop += mVerticalSpacing + lineHeight;
		lineHeight = childHeight;
	    }
	    childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
	    childLeft += childWidth + mHorizontalSpacing;
	}
    }
}
