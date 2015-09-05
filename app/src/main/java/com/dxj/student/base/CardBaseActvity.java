package com.dxj.student.base;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dxj.student.R;
import com.dxj.student.activity.UpdateUserInfoActivity;
import com.dxj.student.bean.BaseBean;
import com.dxj.student.bean.HeadUrl;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.GsonRequest;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.HttpUtils;
import com.dxj.student.utils.MyAsyn;
import com.dxj.student.utils.MyUtils;
import com.dxj.student.utils.StringUtils;
import com.dxj.student.utils.ToastUtils;
import com.dxj.student.utils.UpdatePhotoUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 9/4/2015.
 */
public abstract class CardBaseActvity extends BaseActivity {
    public static final int TAKE_PICTURE = 0;// 拍照
    public static final int RESULT_LOAD_IMAGE = 1;// 从相册中选择
    public static final int CUT_PHOTO_REQUEST_CODE = 2;
    private ImageView imgCard;
    private TextView update;
    private int cardType;
    private String imageUrl;
    private Uri photoUri;
    private String picturePath;

    abstract protected int provideContentViewId();

    abstract protected String getUrl();

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
//                isBitmap = true;
                imgCard.setImageBitmap((Bitmap) msg.obj);
                new MyAsyn(context, getAsynResponse(), picturePath, HttpUtils.UPADTE_MULT_IMAGE).execute();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
//        initTitle();
        initData();
        initView();

    }

//    @Override
//    public void initTitle() {
//
//    }

    @Override
    public void initView() {
        imgCard = (ImageView) findViewById(R.id.img_card_teahcher);
        update = (TextView) findViewById(R.id.creategroup);
        update.setOnClickListener(updateCardListener);
        imgCard.setOnClickListener(updateImageListener);
    }

    private MyAsyn.AsyncResponse getAsynResponse() {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Log.i("TAG", "Update+result=" + result);
//                tv.setVisibility(View.GONE);
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
                Log.i("TAG", "headUrl=" + headUrl.getImages().get(0));
                imageUrl = headUrl.getImages().get(0);
            }
        };

    }

    View.OnClickListener updateImageListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //获取照片
            UpdatePhotoUtils.startPhotoZoom(CardBaseActvity.this);

        }
    };
    View.OnClickListener updateCardListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            sendRequestData();
        }
    };

    public void initData() {

    }

    private void sendRequestData() {

        String urlPath = FinalData.URL_VALUE + getUrl();
        if (StringUtils.isEmpty(picturePath)) {
            showToast("请选择照片'");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        map.put("img", imageUrl);
        GsonRequest<BaseBean> custom = new GsonRequest(Request.Method.POST, urlPath, BaseBean.class, map, getListener(), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    private Response.Listener<BaseBean> getListener() {
        return new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean str) {
                Log.i("TAG", "str=" + str);
                if (str.getCode() == 0) {
                    showToast(str.getMsg());
                }
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError)
                    ToastUtils.showToast(context, "请求超时");
                else if (volleyError instanceof NoConnectionError)
                    ToastUtils.showToast(context, "没有网络连接");
                else if (volleyError instanceof ServerError)
                    ToastUtils.showToast(context, "服务器异常 登录失败");
            }
        };
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UpdateUserInfoActivity.RESULT_LOAD_IMAGE || requestCode == UpdateUserInfoActivity.TAKE_PICTURE || requestCode == UpdateUserInfoActivity.CUT_PHOTO_REQUEST_CODE) {
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    //获取图片路径
                    Log.i("TAG", "bitmap=");
                    if (UpdateUserInfoActivity.RESULT_LOAD_IMAGE == requestCode) {
                        if (data == null) {
                            return;
                        }
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        if (bitmap == null && !StringUtils.isEmpty(picturePath)) {
                            try {
                                bitmap = MyUtils.createImageThumbnail(context, picturePath, 600);
                                Log.i("TAG", "bitmap=" + bitmap);
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = bitmap;
                                handler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                ;
            }.start();
        }
    }

//    public void photo() {
//        //获取文件
//        File out = UpdatePhotoUtils.photo(this);
//        photoUri = Uri.fromFile(out);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//        startActivityForResult(intent, TAKE_PICTURE);
//    }
}
