package com.dxj.student.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dxj.student.R;
import com.dxj.student.base.BaseFragment;
import com.dxj.student.bean.UserBean;
import com.dxj.student.db.AccountDBTask;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.AesUtil;
import com.dxj.student.utils.FileUtils;
import com.dxj.student.utils.HttpUtils;
import com.dxj.student.utils.SPUtils;
import com.dxj.student.utils.StringUtils;
import com.dxj.student.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 首页
 * Created by khb on 2015/8/19.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private EditText etPassword;
    private EditText etPhone;
    private Button btnLogin;
    private ProgressFragment progressFragment;

    @Override
    public void initData() {
//        List<String>

    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_login, null);
        etPhone = (EditText) view.findViewById(R.id.et_user);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        etPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:

                localLogin();

                break;
        }
    }

    private void localLogin() {
        progressFragment = ProgressFragment.newInstance();

        String strPassword = etPassword.getText().toString().trim();
        String strUser = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(strUser)) {
            etPhone.setError("请输入手机号码");
            etPhone.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(strUser)) {
            etPhone.setError("手机号码格式不对");
            etPhone.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(strPassword)) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return;
        }
        progressFragment.show(getFragmentManager(), "");
        String urlPath = FinalData.URL_VALUE + HttpUtils.LOGIN;
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", strUser);
        try {
            map.put("plainPassword", AesUtil.Encrypt(strPassword, AesUtil.cKey));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("TAG", "token=" + SPUtils.getSPData("token", "token"));
        map.put("deviceTokens", SPUtils.getSPData("token", "token"));
        initLogin(map, urlPath);
    }

    private void initLogin(Map<String, Object> map, String urlPath) {
        //();
        GsonRequest<UserBean> custom = new GsonRequest<>(Request.Method.POST, urlPath, UserBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener getListener() {
        return new Response.Listener<UserBean>() {

            @Override
            public void onResponse(UserBean userBean) {
                Log.i("TAG", "userBean=" + userBean.toString());
                progressFragment.dismissAllowingStateLoss();
                if (userBean != null) {
                    if (userBean.getCode() == 0){
                        AccountDBTask.saveUserBean(userBean);
                        ToastUtils.showToast(getActivity(), userBean.getMsg());}
                    else
                        ToastUtils.showToast(getActivity(), userBean.getMsg());
                } else {
                    ToastUtils.showToast(getActivity(), "登录失败");
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressFragment.dismissAllowingStateLoss();
                if (volleyError instanceof TimeoutError)
                    ToastUtils.showToast(getActivity(), "请求超时");
                else if (volleyError instanceof NoConnectionError)
                    ToastUtils.showToast(getActivity(), "没有网络连接");
                else if (volleyError instanceof ServerError)
                    ToastUtils.showToast(getActivity(), "服务器异常 登录失败");
            }
        };
    }

    public static class ProgressFragment extends DialogFragment {


        public static ProgressFragment newInstance() {
            ProgressFragment frag = new ProgressFragment();
            frag.setRetainInstance(true);
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("登录中...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);

            return dialog;
        }

        @Override
        public void onCancel(DialogInterface dialog) {


            super.onCancel(dialog);
        }
    }
}

