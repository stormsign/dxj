package com.dxj.student.activity;

import android.os.Bundle;

import com.dxj.student.R;
import com.dxj.student.base.CardBaseActvity;
import com.dxj.student.utils.HttpUtils;


/**
 * Created by kings on 9/4/2015.
 */
public class CardDegreesActvity extends CardBaseActvity {

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_card_teacher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getUrl() {
        return HttpUtils.CARD;
    }
}
