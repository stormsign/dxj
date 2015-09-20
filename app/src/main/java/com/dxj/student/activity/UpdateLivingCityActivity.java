package com.dxj.student.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dxj.student.R;
import com.dxj.student.application.MyApplication;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.BaseBean;
import com.dxj.student.db.AccountDBTask;
import com.dxj.student.db.AccountTable;
import com.dxj.student.http.CustomStringRequest;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.HttpUtils;
import com.dxj.student.utils.StringUtils;
import com.dxj.student.utils.ToastUtils;
import com.dxj.student.widget.TitleNavBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 * 家庭地址
 */
public class UpdateLivingCityActivity extends BaseActivity implements View.OnClickListener {
    private EditText etLivingCity;
    private String id;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nicename);
        initData();
        initView();
        initTitle();
    }

    @Override
    public void initTitle() {
        TitleNavBar title = (TitleNavBar) findViewById(R.id.title);
        title.disableBack(true);
        title.setTitle("家庭地址");
        title.setTitleNoRightButton();
        title.setOnTitleNavClickListener(new TitleNavBar.OnTitleNavClickListener() {
            @Override
            public void onNavOneClick() {

            }

            @Override
            public void onNavTwoClick() {

            }

            @Override
            public void onNavThreeClick() {

            }

            @Override
            public void onActionClick() {

            }

            @Override
            public void onBackClick() {
                sendRequestData();
            }
        });
    }

//    @Override
//    public void initTitle() {
//
//    }

    @Override
    public void initView() {
        etLivingCity = (EditText) findViewById(R.id.et);
        etLivingCity.setHint("家庭地址");
//        if (StringUtils.isEmpty(address)) {
//            etLivingCity.setText(MyApplication.getInstance().getLocationdescribe());
//        } else {
//            etLivingCity.setText(address);
//
//        }
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        address = getIntent().getStringExtra("address");
    }


//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.btn_back:
//                sendRequestData();
//                break;
//        }
//    }

    private void sendRequestData() {
        address = etLivingCity.getText().toString().trim();
        if (StringUtils.isEmpty(address)) {
            finish();
            return;
        }
        String urlPath = FinalData.URL_VALUE + HttpUtils.LIVING_CITY;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("livingCity", address);
        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<String> getListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                if (message.getCode() == 0) {
                    AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), address, AccountTable.LIVINGCITY);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("livingCity", address);
                    intent.putExtras(bundle);
                    UpdateLivingCityActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(UpdateLivingCityActivity.this, "修改失败");
                finish();
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                sendRequestData();
                break;
        }
    }
}
