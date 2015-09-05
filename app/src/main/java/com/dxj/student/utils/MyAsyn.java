package com.dxj.student.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


import com.dxj.student.bean.PhotoBean;
import com.dxj.student.bean.Photos;
import com.dxj.student.http.MyRequest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


// 网络请求
public class MyAsyn extends AsyncTask<String, String, String> {
    //    private MyException e;
//    private ProgressFragment progressFragment = ProgressFragment.newInstance();
    private WeakReference<FragmentActivity> oAuthActivityWeakReference;
    private String returnStr; // 请求后的返回值
    private String folder;
    private Photos photos;
    private Context recordDetailActivity;
    private String url;

    public MyAsyn(Context recordDetailActivity, AsyncResponse mAsyncResPonse, String folder, String url) {
//        oAuthActivityWeakReference = new WeakReference<FragmentActivity>(recordDetailActivity);
        this.url=url;
        this.recordDetailActivity =recordDetailActivity;
        this.mAsyncResponse = mAsyncResPonse;
        this.folder = folder;
    }



    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        ArrayList<Photos> photosList = new ArrayList<Photos>();
        String bitStr;
//	Photos photos;
        Bitmap bitmap;
	try {
//	    for (int i = 0; i < Bimp.tempSelect
        bitmap =MyUtils.createImageThumbnail(recordDetailActivity,folder,800);
        bitStr = Base64.encode(MyUtils.Bitmap2Bytes(bitmap));
        photos = new Photos();
        photos.setBase64String(bitStr);
        photos.setFileName(folder);
        photosList.add(photos);
//	    }
	} catch (java.io.IOException e) {
//	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        PhotoBean mPhotoBean = new PhotoBean();
        mPhotoBean.setFolder("head");
        mPhotoBean.setImage(photosList);
        // 第一次请求，先将图片上传到服务器，获得返回 的图片url
        String returnStr = null;
        try {
            returnStr = MyRequest.getInstance().getRequest(HttpUtils.UPADTE_MULT_IMAGE, mPhotoBean,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return  returnStr;
            // TODO Auto-generated catch block
        // return returnStr;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        // if (progressFragment.isVisible()) {
//        progressFragment.dismissAllowingStateLoss();
        // }
//        Gson gson = new Gson();
//        MsgBean msg = gson.fromJson(result, MsgBean.class);
//        if (msg.getCode() != 0) {
//            Toast.makeText(oAuthActivityWeakReference.get(), msg.getMsg(), Toast.LENGTH_LONG).show();
//            return;
//        }
        if (result != null) {
            if (mAsyncResponse != null) {
                mAsyncResponse.processFinish(result);
            }
        } else {
            Toast.makeText(recordDetailActivity, "提交时发生错误，请稍后再试", Toast.LENGTH_LONG).show();
        }
//        if (adapter != null) {
//            for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//                Bimp.tempSelectBitmap.remove(i);
//                adapter.notifyDataSetChanged();
//            }
//        }
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        // progressFragment.setAsyncTask(this);
//
//        FragmentActivity activity = oAuthActivityWeakReference.get();
//        if (linearInputContent != null) {
//            linearInputContent.setVisibility(View.GONE);
//
//        }
//        if (activity != null) {
//            progressFragment.show(activity.getSupportFragmentManager(), "");
//        }
    }

    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
//        super.onCancelled();
//        if (progressFragment != null) {
//            progressFragment.dismissAllowingStateLoss();
//        }
//        FragmentActivity activity = oAuthActivityWeakReference.get();
//        if (activity == null) {
//            return;
//        }
//
//        if (e != null) {
//            Toast.makeText(activity, e.getError(), Toast.LENGTH_SHORT).show();
//        }
    }

    // 监听器
    private AsyncResponse mAsyncResponse = null;

    public interface AsyncResponse {

        public void processFinish(String result);

    }
}
