/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dxj.student.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dxj.student.R;
import com.dxj.student.utils.MyUtils;

import java.util.Locale;


public class PagerSlidingTabStrip extends HorizontalScrollView {


    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;
    private LinearLayout tabsContainer;
    private View view;
    private ViewPager pager;
    private int tabCount;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private Paint rectPaint;
    private boolean checkedTabWidths = false;
    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private boolean shouldExpand = false;
    private boolean textAllCaps = true;
    private int scrollOffset = 105;
    private int indicatorHeight = 1;
    private int underlineHeight = 1;
    private int tabPadding = 0;
    private int tabTextSize = 15;
    private int tabTextColor = 0xFF666666;
    private int lastScrollX = 0;
    private int tabBackgroundResId = R.mipmap.asv;
    private Locale locale;
    private DisplayMetrics dm;
    private Context context;

    public PagerSlidingTabStrip(Context c) {
        this(c, null);
        this.context = c;
    }

    public PagerSlidingTabStrip(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
        this.context = c;
    }

    public PagerSlidingTabStrip(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        this.context = c;
        setFillViewport(true);
        setWillNotDraw(false);
        view = LayoutInflater.from(getContext()).inflate(R.layout.login_bar, null);
        Log.i("TAG", "view=" + view);
        tabsContainer = (LinearLayout) view.findViewById(R.id.linear_bar);

//		tabsContainer = new LinearLayout(context);
//		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
//		tabsContainer.setBackgroundResource(android.R.color.transparent);
//		tabsContainer.setLayoutParams(new LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(view);

        dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        tabPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        tabTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();
//		a = context.obtainStyledAttributes(attrs,
//				R.styleable.PagerSlidingTabStrip);
//
//		indicatorColor = a
//				.getColor(R.styleable.PagerSlidingTabStrip_indicatorColor,
//						indicatorColor);
//		underlineColor = a
//				.getColor(R.styleable.PagerSlidingTabStrip_underlineColor,
//						underlineColor);
//		indicatorHeight = a.getDimensionPixelSize(
//				R.styleable.PagerSlidingTabStrip_indicatorHeight,
//				indicatorHeight);
//		underlineHeight = a.getDimensionPixelSize(
//				R.styleable.PagerSlidingTabStrip_underlineHeight,
//				underlineHeight);
//		tabPadding = a.getDimensionPixelSize(
//				R.styleable.PagerSlidingTabStrip_tabPaddingLeftRight,
//				tabPadding);
//		tabBackgroundResId = a.getResourceId(
//				R.styleable.PagerSlidingTabStrip_tabBackground,
//				tabBackgroundResId);
//		shouldExpand = a.getBoolean(
//				R.styleable.PagerSlidingTabStrip_shouldExpand, shouldExpand);
//		scrollOffset = a.getDimensionPixelSize(
//				R.styleable.PagerSlidingTabStrip_scrollOffset, scrollOffset);
//		textAllCaps = a.getBoolean(
//				R.styleable.PagerSlidingTabStrip_textAllCapss, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);
        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    private int infoDataNum;

    public void setInfoDataNum(int infoDataNum) {
        this.infoDataNum = infoDataNum;
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (infoDataNum >= 4) {
            defaultTabLayoutParams = new LinearLayout.LayoutParams(
                    (MyUtils.getScreenWidth(getContext()) / 4), LayoutParams.MATCH_PARENT);
        } else if (infoDataNum == 2) {
            Log.i("TAG", "infoDataNum==2");
            defaultTabLayoutParams = new LinearLayout.LayoutParams(
                    (MyUtils.dip2px(getContext(), 100)), LayoutParams.MATCH_PARENT);
        } else if (infoDataNum == 1) {
            defaultTabLayoutParams = new LinearLayout.LayoutParams(
                    (MyUtils.getScreenWidth(getContext()) / 1), LayoutParams.MATCH_PARENT);
        } else {
            defaultTabLayoutParams = new LinearLayout.LayoutParams(
                    (MyUtils.getScreenWidth(getContext()) / 3), LayoutParams.MATCH_PARENT);
        }
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        if (pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
//		tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        view.findViewById(R.id.tv_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);

            }
        });
        view.findViewById(R.id.tv_right).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);

            }
        });
//		updateTabStyles();
        checkedTabWidths = false;
        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(
                                    this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(
                                    this);
                        }
                        currentPosition = pager.getCurrentItem() == tabCount ? pager
                                .getCurrentItem() - 1 : pager.getCurrentItem();
                        scrollToChild(currentPosition, 0);
                    }
                });
    }

    public void setCheckTextTab(int position) {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                if (i == position) {
                    tab.setTextColor(context.getResources().getColor(
                            R.color.msg_color));
                } else {
                    tab.setTextColor(context.getResources().getColor(
                            R.color.rbuttonNo));
                }
            }
        }
        pager.setCurrentItem(position);
    }

//	private void addTextTab(final int position, String title) {
//		TextView tab = new TextView(getContext());
//		tab.setLayoutParams(new LayoutParams(
//				MyUtils.dip2px(getContext(),20), LayoutParams.WRAP_CONTENT));
//		tab.setText(title);
//		tab.setFocusable(true);
//		tab.setGravity(Gravity.CENTER);
//		tab.setSingleLine();
//		tab.setBackgroundResource(android.R.color.transparent);
//		tab.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				pager.setCurrentItem(position);
//			}
//		});
//		tabsContainer.addView(tab);
//	}

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setFocusable(true);
        tab.setImageResource(resId);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
        tabsContainer.addView(tab);
    }

//	private void updateTabStyles() {
//		for (int i = 0; i < tabCount; i++) {
//			View v = tabsContainer.getChildAt(i);
//
//			if(defaultTabLayoutParams == null){
//				System.out.println("defaultTabLayoutParams null");
//			}else{
//				System.out.println("defaultTabLayoutParams not null");
//			}
//
//			v.setLayoutParams(defaultTabLayoutParams);
//			v.setBackgroundResource(tabBackgroundResId);
//			if (v instanceof TextView) {
//				TextView tab = (TextView) v;
//				tab.setGravity(Gravity.CENTER);
//				tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
//				tab.setTextColor(tabTextColor);
//				if (textAllCaps) {
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//						tab.setAllCaps(true);
//					} else {
//						tab.setText(tab.getText().toString()
//								.toUpperCase(locale));
//					}
//				}
//			}
//		}
//	}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!shouldExpand
                || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            return;
        }

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {
            if (childWidth <= myWidth) {
                for (int i = 0; i < tabCount; i++) {
                    tabsContainer.getChildAt(i).setLayoutParams(
                            expandedTabLayoutParams);
                }
            }
            checkedTabWidths = true;
        }
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();
        // draw indicator line
        rectPaint.setColor(indicatorColor);
        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        // if there is an offset, start interpolating left and right coordinates
        // between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
                    * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
                    * lineRight);
        }
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
                rectPaint);
        // draw underline
        rectPaint.setColor(underlineColor);
//		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
//				height, rectPaint);
    }

    private class PageListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            for (int i = 0; i < tabCount; i++) {
                View v = tabsContainer.getChildAt(i);
                if (v instanceof TextView) {
                    TextView tab = (TextView) v;
                    if (i == position) {
                        tab.setTextColor(context.getResources().getColor(R.color.msg_color));
                    } else {
                        tab.setTextColor(context.getResources().getColor(R.color.rbuttonNo));
                    }
                }
            }
            currentPosition = position;
            currentPositionOffset = positionOffset;
            invalidate();
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

//	public void setTextSize(int textSizePx) {
//		this.tabTextSize = textSizePx;
//		updateTabStyles();
//	}

    public int getTextSize() {
        return tabTextSize;
    }

//	public void setTextColor(int textColor) {
//		this.tabTextColor = textColor;
//		updateTabStyles();
//	}

//	public void setTextColorResource(int resId) {
//		this.tabTextColor = getResources().getColor(resId);
//		updateTabStyles();
//	}

    public int getTextColor() {
        return tabTextColor;
    }

//	public void setTabBackground(int resId) {
//		this.tabBackgroundResId = resId;
//	}
//
//	public int getTabBackground() {
//		return tabBackgroundResId;
//	}

//	public void setTabPaddingLeftRight(int paddingPx) {
//		this.tabPadding = paddingPx;
//		updateTabStyles();
//	}

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
