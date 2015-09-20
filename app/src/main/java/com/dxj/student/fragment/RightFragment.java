package com.dxj.student.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dxj.student.R;
import com.dxj.student.base.BaseFragment;
import com.dxj.student.bean.BaseBean;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.AesUtil;
import com.dxj.student.utils.HttpUtils;
import com.dxj.student.utils.LogUtils;
import com.dxj.student.utils.MyCount;
import com.dxj.student.utils.MyUtils;
import com.dxj.student.utils.StringUtils;
import com.dxj.student.utils.ToastUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 首页
 * Created by khb on 2015/8/19.
 */
public class RightFragment extends BaseFragment implements View.OnClickListener {
    private final static int PASSWORD = 0;
    private final static int TWO_PASSWORD = 1;
    private final static int PHONE = 2;
    private final static int CODE = 3;
    /*验证码*/
    private final static String SMSSDK_APP_KEY = "a38245d89da2";
    private final static String SMSSDK_APP_SECRET = "84db45a5b7dcde895fc72f47edf53447";
    private EventHandler eventHandler;
    /*end*/
    private EditText etPhone;
    private EditText etPassword;
    private Button btnRight;
    private EditText etTwoPassword;
    private EditText etCode;
    private TextView btnSendCode;
    private MyCount mc;
    private boolean isMsg = false;
    private ProgressFragment progress;
    private String strPhone;
    private String password;
    private ImageView imgDelectPassword;
    private ImageView imgDelectPasswordTwo;
    private ImageView imgDelectPhone;
    private ImageView imgDelectCode;

    @Override
    public void initData() {
//        List<String>
//        SMSSDK.initSDK(context, MyUtils.SMSSDK_APP_KEY, MyUtils.SMSSDK_APP_SECRET);
//        eventHandler = new EventHandler() {
//            @Override
//            public void onRegister() {
//                super.onRegister();
//            }
//
//            @Override
//            public void onUnregister() {
//                super.onUnregister();
//            }
//
//            @Override
//            public void beforeEvent(int i, Object o) {
//                super.beforeEvent(i, o);
//            }
//
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    //回调完成
//                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        //提交验证码成功
//                        Map<String, Object> map = (HashMap<String, Object>) data;
////                        LogUtils.w("EVENT_SUBMIT_VERIFICATION_CODE " + map.toString());
//                        String urlPath = FinalData.URL_VALUE + HttpUtils.RIGISTER;
//                        Map<String, Object> maps = new HashMap<>();
//                        maps.put("mobile", etPhone.getText().toString().trim());
//                        try {
//
//                            maps.put("plainPassword", AesUtil.Encrypt(password, AesUtil.cKey));
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        GsonRequest<BaseBean> custom = new GsonRequest<>(Request.Method.POST, urlPath, BaseBean.class, maps, getVerifyListener(1), getVerifyErrorListener(1));
//                        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
//                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                        //获取验证码成功
//                        isMsg = true;
//                        LogUtils.w("get code success");
//                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
//                        //返回支持发送验证码的国家列表
//                        ArrayList<HashMap<String, Object>> countryList = (ArrayList<HashMap<String, Object>>) data;
//                        LogUtils.d("countryList = " + countryList.toString());
//                    }
//                } else {
//                    ((Throwable) data).printStackTrace();
//                }
//            }
//        };
//        SMSSDK.registerEventHandler(eventHandler);
//
//        SMSSDK.getSupportedCountries();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_right, null);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnRight = (Button) view.findViewById(R.id.btn_right);
        etTwoPassword = (EditText) view.findViewById(R.id.et_two_password);
        etCode = (EditText) view.findViewById(R.id.et_code);
        btnSendCode = (TextView) view.findViewById(R.id.btn_send_code);

        imgDelectPassword = (ImageView) view.findViewById(R.id.img_password);
        imgDelectPasswordTwo = (ImageView) view.findViewById(R.id.img_password_two);
        imgDelectPhone = (ImageView) view.findViewById(R.id.img_phone);
        imgDelectCode = (ImageView) view.findViewById(R.id.img_code);
        btnSendCode.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        imgDelectPassword.setOnClickListener(this);
        imgDelectPhone.setOnClickListener(this);
        imgDelectCode.setOnClickListener(this);
        imgDelectPasswordTwo.setOnClickListener(this);
        etPassword.addTextChangedListener(getTextWatcher(PASSWORD));
        etTwoPassword.addTextChangedListener(getTextWatcher(TWO_PASSWORD));
        etPhone.addTextChangedListener(getTextWatcher(PHONE));
        etCode.addTextChangedListener(getTextWatcher(CODE));
        return view;
    }

    private TextWatcher getTextWatcher(final int index) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (index) {
                    case PASSWORD:
                        if (StringUtils.isEmpty(etPassword.getText().toString()))
                            imgDelectPassword.setVisibility(View.GONE);
                        else
                            imgDelectPassword.setVisibility(View.VISIBLE);
                        break;
                    case TWO_PASSWORD:
                        if (StringUtils.isEmpty(etTwoPassword.getText().toString()))
                            imgDelectPasswordTwo.setVisibility(View.GONE);
                        else
                            imgDelectPasswordTwo.setVisibility(View.VISIBLE);
                        break;
                    case PHONE:
                        if (StringUtils.isEmpty(etPhone.getText().toString()))
                            imgDelectPhone.setVisibility(View.GONE);
                        else
                            imgDelectPhone.setVisibility(View.VISIBLE);
                        break;
                    case CODE:
                        if (StringUtils.isEmpty(etCode.getText().toString()))
                            imgDelectCode.setVisibility(View.GONE);
                        else
                            imgDelectCode.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_right:
                //注册
                right();
                break;
            case R.id.btn_send_code:
                sendCode();
                break;
            case R.id.img_password:
                etPassword.setText("");
                break;
            case R.id.img_password_two:
                etTwoPassword.setText("");
                break;
            case R.id.img_phone:
                etPhone.setText("");
                break;
            case R.id.img_code:
                etCode.setText("");
                break;
        }

    }

    private void sendCode() {
        strPhone = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(strPhone)) {
            etPhone.setError("请输入手机号码");
            etPhone.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(strPhone)) {
            etPhone.setError("手机号码格式不对");
            etPhone.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode("86", strPhone);
        if (mc == null) {
            mc = new MyCount(60000, 1000, btnSendCode, getActivity()); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
        }
        mc.start();
    }


    private void right() {
        password = etPassword.getText().toString().trim();
        String twoPassword = etTwoPassword.getText().toString().trim();
        String strCode = etCode.getText().toString().trim();
//        if (!isMsg) {
//            ToastUtils.showToast(getActivity(), "请先获取验证码");
//            return;
//        }
//        if (StringUtils.isEmpty(strCode)) {
//            etCode.setError("请输入您验证码");
//            etCode.requestFocus();
//            return;
//        }
        if (StringUtils.isEmpty(password)) {
            etPassword.setError("请输入您的密码");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("密码必须在6-12位之间");
            etPassword.requestFocus();
            return;
        }
        if (password.length() > 12) {
            etPassword.setError("密码必须在6-12位之间");
            etPassword.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(twoPassword)) {
            etTwoPassword.setError("请输入您的密码");
            etTwoPassword.requestFocus();
            return;
        }
        if (!twoPassword.equals(password)) {
            etTwoPassword.setError("两次密码不同");
            etTwoPassword.requestFocus();
            return;
        }
//        SMSSDK.submitVerificationCode("86", strPhone, strCode);

//        if (!IhomeUtils.checkPasswordSpecialChar(password)) {
//            ToastUtils.showToast(getActivity(), "密码仅允许字母及数字组合");
//            return;
//        }
//        if (!cbxPrivacy.isChecked()) {
//            ToastUtils.showToast(getActivity(), "请选择使用协议和隐私条款");
//            return;
//        }
        progress = ProgressFragment.newInstance();
        progress.show(getFragmentManager(), "right");
//        String urlPath = FinalData.URL_VALUE + "checkMsgCode";
//        Map<String, Object> map = new HashMap<>();
//        map.put("mobile", etPhone.getText().toString().trim());
//        map.put("code", strCode);
//        GsonRequest<BaseBean> custom = new GsonRequest<>(Request.Method.POST, urlPath, BaseBean.class,map, getVerifyListener(0), getVerifyErrorListener(0));

//        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
        String urlPath = FinalData.URL_VALUE + HttpUtils.RIGISTER;
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", etPhone.getText().toString().trim());
        try {

            map.put("plainPassword", AesUtil.Encrypt(password, AesUtil.cKey));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GsonRequest<BaseBean> custom = new GsonRequest<>(Request.Method.POST, urlPath, BaseBean.class, map, getVerifyListener(1), getVerifyErrorListener(1));
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
    }

    public Response.Listener<BaseBean> getVerifyListener(final int tag) {
        return new Response.Listener<BaseBean>() {

            @Override
            public void onResponse(BaseBean response) {
                // TODO Auto-generated method stub
                Log.i("TAG", "response=" + response);
                progress.dismissAllowingStateLoss();
                if (tag == 0) {
                    /** TAG==0表示这是验证验证码 */
//                    if (response.getCode() == 0) {
//                        String urlPath = FinalData.URL_VALUE + "register";
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("mobile", etPhone.getText().toString().trim());
//                        try {
//                            map.put("password", etPassword);
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        GsonRequest<BaseBean> custom = new GsonRequest<BaseBean>(Request.Method.POST, urlPath, BaseBean.class, map, getVerifyListener(1), getVerifyErrorListener(1));
//                        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
//                    } else {
//                        ToastUtils.showToast(getActivity(), response.getMsg());
//                    }
                } else if (tag == 1) {
                    /** TAG==1 表示这是注册 */
                    if (response.getCode() == 0) {
                    }
                }
            }
        };
    }

    public Response.ErrorListener getVerifyErrorListener(final int tag) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("TAG", "onErrorResponse=" + error.getMessage());
                progress.dismissAllowingStateLoss();
                if (error instanceof TimeoutError)
                    ToastUtils.showToast(getActivity(), "请求超时");
                else if (error instanceof NoConnectionError)
                    ToastUtils.showToast(getActivity(), "没有网络连接");
                else if (error instanceof ServerError)
                    ToastUtils.showToast(getActivity(), "服务器异常 注册失败");
                if (tag == 0) {

                }
            }
        };
    }

}
