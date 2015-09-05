package com.dxj.student.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dxj.student.R;
import com.dxj.student.application.MyApplication;
import com.dxj.student.base.BaseActivity;
import com.dxj.student.bean.BaseBean;
import com.dxj.student.bean.HeadUrl;
import com.dxj.student.bean.UserBean;
import com.dxj.student.db.AccountDBTask;
import com.dxj.student.db.AccountTable;
import com.dxj.student.dialogplus.SimpleAdapter;
import com.dxj.student.http.CustomStringRequest;
import com.dxj.student.http.FinalData;
import com.dxj.student.http.VolleySingleton;
import com.dxj.student.utils.HttpUtils;
import com.dxj.student.utils.MyAsyn;
import com.dxj.student.utils.MyUtils;
import com.dxj.student.utils.StringUtils;
import com.dxj.student.utils.UpdatePhotoUtils;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 8/27/2015.
 */
public class UpdateUserInfoActivity extends BaseActivity implements View.OnClickListener {
    public String[] strings = {"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天坪座", "天蝎座", "射手座", "摩羯座", "水平座", "双鱼座"};
    public String[] grades = {"小学一年级", "小学二年级", "小学三年级", "小学四年级", "小学五年级", "小学六年级", "初一", "初二", "初三", "高一", "高二", "高三"};
    public static final int TAKE_PICTURE = 10;// 拍照
    public static final int RESULT_LOAD_IMAGE = 11;// 从相册中选择
    public static final int CUT_PHOTO_REQUEST_CODE = 12;

    public static final int NICE_NAME = 1;
    public static final int MOBILE = 2;
    public static final int NATIONALITY = 3;
    public static final int NAME = 4;
    public static final int LIVING_CITY = 5;
    private RelativeLayout relativeNiceName;
    private RelativeLayout relativeSex;
    private RelativeLayout relativeMobile;
    private RelativeLayout relativeType;
    private RelativeLayout relativeName;
    private RelativeLayout relativeCard;
    private RelativeLayout relativeGrade;
    private RelativeLayout relativeLivingCity;
    private RelativeLayout relativeConstellation;
    private RelativeLayout relativerAvatar;
    private RelativeLayout relativerImages;
    private TextView tvNicename;
    private TextView tvSex;
    private TextView tvMobile;
    private TextView tvType;
    private TextView tvName;
    private TextView tvCard;
    private TextView tvGrade;
    private TextView tvLivingCity;
    private TextView tvConstellation;
    private TextView tvImages;
    private ImageView avatar;
    private DialogPlus dialogPlus;
    private UserBean userBean;
    private Uri photoUri;
    private String imagePath;
    private String strGrade;
    private String str;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
//                isBitmap = true;
                avatar.setImageBitmap((Bitmap) msg.obj);
                new MyAsyn(UpdateUserInfoActivity.this, getAsynResponse(), imagePath, HttpUtils.UPADTE_MULT_IMAGE).execute();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);
        initData();
        initView();
    }

    //    @Override
//    public void initTitle() {
//
//    }
    private MyAsyn.AsyncResponse getAsynResponse() {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
//                tv.setVisibility(View.GONE);
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
                if (headUrl.getCode() == 0) {
                    Log.i("TAG", "Update+=" + result);
                    sendRequestData(headUrl.getImages().get(0), 4);
                }
            }
        };
    }

    @Override
    public void initView() {
        avatar = (ImageView) findViewById(R.id.avatar);
        relativeNiceName = (RelativeLayout) findViewById(R.id.relative_nicename);
        relativeSex = (RelativeLayout) findViewById(R.id.relative_sex);
        relativeMobile = (RelativeLayout) findViewById(R.id.relative_mobile);
        relativeType = (RelativeLayout) findViewById(R.id.relative_type);
        relativeName = (RelativeLayout) findViewById(R.id.relative_name);
        relativeGrade = (RelativeLayout) findViewById(R.id.relative_grade);
        relativeLivingCity = (RelativeLayout) findViewById(R.id.relative_livingcity);
        relativeCard = (RelativeLayout) findViewById(R.id.relative_card);
        relativeConstellation = (RelativeLayout) findViewById(R.id.relative_constellation);
        relativerAvatar = (RelativeLayout) findViewById(R.id.relative_avatar);
        relativerImages = (RelativeLayout) findViewById(R.id.relative_images);
        //------------------------------------- 华丽分割线--------------------------------

        tvNicename = (TextView) findViewById(R.id.tv_nicename);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvMobile = (TextView) findViewById(R.id.tv_mobile);
        tvGrade = (TextView) findViewById(R.id.tv_grade);
        tvLivingCity = (TextView) findViewById(R.id.tv_livingcity);
        tvConstellation = (TextView) findViewById(R.id.tv_constellation);
        tvCard = (TextView) findViewById(R.id.tv_card);

        tvImages = (TextView) findViewById(R.id.tv_images);
        //------------------------- 赋值--------------------------

        if (userBean != null) {
            if (userBean.getUserInfo().getHeadUrl() != null)
            /** 加载头像 */
                Glide.with(MyApplication.getInstance()).load(userBean.getUserInfo().getHeadUrl()).centerCrop().placeholder(R.mipmap.default_avatar).into(avatar);
            else
                Glide.with(MyApplication.getInstance()).load(R.mipmap.default_avatar).centerCrop().into(avatar);
            tvNicename.setText(userBean.getUserInfo().getNickName());
            tvName.setText(String.valueOf(userBean.getUserInfo().getName()));
            tvSex.setText(userBean.getUserInfo().getSex());
            tvMobile.setText(userBean.getUserInfo().getMobile());
            tvLivingCity.setText(userBean.getUserInfo().getLivingCity());
            tvGrade.setText(userBean.getUserInfo().getGrade());
            tvConstellation.setText(userBean.getUserInfo().getHoroscope());


        }

        //------------------------添加点击事件-----------------------------
        relativeNiceName.setOnClickListener(this);
        relativeSex.setOnClickListener(this);
        relativeName.setOnClickListener(this);
        relativeMobile.setOnClickListener(this);
        relativeCard.setOnClickListener(this);
        relativeGrade.setOnClickListener(this);
        relativeLivingCity.setOnClickListener(this);
        relativeType.setOnClickListener(this);
        relativeConstellation.setOnClickListener(this);
        relativerAvatar.setOnClickListener(this);
        relativerImages.setOnClickListener(this);
    }

    @Override
    public void initData() {
        userBean = MyApplication.getInstance().getUserBean();
        Log.i("TAG", "userBean=" + userBean);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE || requestCode == TAKE_PICTURE || requestCode == CUT_PHOTO_REQUEST_CODE) {
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    //获取图片路径
                    if (RESULT_LOAD_IMAGE == requestCode) {
                        if (data == null) {
                            return;
                        }
                        Uri selectedImageUri = data.getData();
                        //图片裁剪
                        imagePath = UpdatePhotoUtils.getImagePath();
                        UpdatePhotoUtils.startPhotoZoomOne(selectedImageUri, UpdateUserInfoActivity.this);

                    } else {
                        if (requestCode == TAKE_PICTURE) {
                            // 拍摄图片
                            imagePath = UpdatePhotoUtils.getImagePath();
                            UpdatePhotoUtils.startPhotoZoomOne(photoUri, UpdateUserInfoActivity.this);
                        } else if (requestCode == CUT_PHOTO_REQUEST_CODE) {
                            if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                                Log.i("TAG", "imagePath=" + imagePath);
                                if (bitmap == null && !StringUtils.isEmpty(imagePath)) {
                                    bitmap = MyUtils.getBitmapByPath(imagePath);
                                    Log.i("TAG", "bitmap=" + bitmap);
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = bitmap;
                                    handler.sendMessage(msg);
                                }
                            }
                        }
                    }
                }

                ;
            }.start();
        }
        if (data == null)
            return;
        if (requestCode == NICE_NAME) {
            String niceName = data.getStringExtra("nicename");
            tvNicename.setText(niceName);
        } else if (requestCode == LIVING_CITY) {
            String strLivingCity = data.getStringExtra("livingCity");
            tvLivingCity.setText(strLivingCity);
        } else if (requestCode == NAME) {
            String strName = data.getStringExtra("name");
            tvName.setText(strName);
        } else if (requestCode == LIVING_CITY) {
            String strLivingCity = data.getStringExtra("livingCity");
            tvLivingCity.setText(strLivingCity);
        }
    }


    private void updateSex() {


        dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.sex_item))
                .setCancelable(true)
                .create();

        dialogPlus.show();

        dialogPlus.findViewById(R.id.tv_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData("女", 1);
            }
        });
        dialogPlus.findViewById(R.id.tv_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData("男", 1);
            }
        });
    }

    private void updateType() {

        dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.type_item))
                .setCancelable(true)
                .create();

        dialogPlus.show();

        dialogPlus.findViewById(R.id.tv_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData("家长", 2);
            }
        });
        dialogPlus.findViewById(R.id.tv_child).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestData("学生", 2);
            }
        });
    }

    private void sendRequestData(String strSex, int index) {
        String urlPath = null;
        Map<String, Object> map = new HashMap<>();
        map.put("id", "e1c380f1-c85e-4a0f-aafc-152e189d9d01");
        if (index == 5) {
            Log.i("TAG", "strSex=" + strSex);
            map.put("grade", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.GRADE;
        } else if (index == 1) {
            map.put("sex", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.SEX;
        } else if (index == 3) {
            map.put("horoscope", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.HOROSCOPE;
        } else if (index == 4) {
            Log.i("TAG", "headUrl=" + strSex);
            map.put("headUrl", strSex);
            urlPath = FinalData.URL_VALUE + HttpUtils.HEAD_URL;
        } else if (index == 2) {
            if (strSex.equals("家长")) {
                map.put("type", 2);
                strSex = "2";
            } else {
                map.put("type", 1);
                strSex = "1";
            }
            urlPath = FinalData.URL_VALUE + HttpUtils.HEAD_URL;
        }


        CustomStringRequest custom = new CustomStringRequest(Request.Method.POST, urlPath, map, getListener(strSex, index), getErrorListener());
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(custom);
    }

    //
    private Response.Listener<String> getListener(final String strSex, final int index) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Log.i("TAG", "str=" + str);
                BaseBean message = JSONObject.parseObject(str, BaseBean.class);
                String accountTable = null;
                if (message.getCode() == 0) {
                    if (index == 1) {
                        tvSex.setText(strSex);
                        accountTable = AccountTable.SET;
                    } else if (index == 3) {
                        tvConstellation.setText(strSex);
                        accountTable = AccountTable.HOROSCOPE;
                    } else if (index == 4) {
                        accountTable = AccountTable.HEADURL;
                        showToast("上传成功");
                    } else if (index == 2) {
                        if (strSex.equals("1")) {
                            tvType.setText("学生");
                        } else {
                            tvType.setText("家长");
                        }
                        accountTable = AccountTable.TYPE;
                    } else if (index == 5) {
                        tvGrade.setText(strGrade);
                        accountTable = AccountTable.GRADE;
                    }

                }
                AccountDBTask.updateNickName(MyApplication.getInstance().getUserId(), strSex, accountTable);

                if (index == 1 || index == 2)
                    dialogPlus.dismiss();
            }
        };
    }

    //
    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialogPlus.dismiss();
            }
        };
    }


    private void showOnlyContentDialog(Holder holder, int gravity, BaseAdapter adapter,
                                       OnItemClickListener itemClickListener, OnDismissListener dismissListener,
                                       OnCancelListener cancelListener, boolean expanded) {
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(expanded)
                .setCancelable(true)
                .create();
        dialog.show();
    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {

        }
    };

    private OnItemClickListener itemClickListener(final int index) {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//        TextView textView = (TextView) view.findViewById(R.id.text_view);
//        String clickedAppName = textView.getText().toString();
                //        dialog.dismiss();
                //        Toast.makeText(MainActivity.this, clickedAppName + " clicked", Toast.LENGTH_LONG).show();
                Log.i("TAG", "position=" + position);
                if (index == 0) {
                    str = strings[position];
                    sendRequestData(str, 3);
                } else {
                    strGrade = grades[position];
                    sendRequestData(strGrade, 5);
                }
                dialog.dismiss();
//            sendRequestData(str, 3);
            }

        };
    }

    OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogPlus dialog) {
            //        Toast.makeText(MainActivity.this, "dismiss listener invoked!", Toast.LENGTH_SHORT).show();
        }
    };

    OnCancelListener cancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogPlus dialog) {
            //        Toast.makeText(MainActivity.this, "cancel listener invoked!", Toast.LENGTH_SHORT).show();
        }
    };

    private void startPhotoZoom() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), RESULT_LOAD_IMAGE);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.i("TAG", "id=" + id);
        switch (id) {
            case R.id.relative_avatar:
                UpdatePhotoUtils.startPhotoZoom(this);
                break;
            case R.id.relative_nicename:
                Intent intentNiceName = new Intent(this, UpdateNiceNameActivity.class);
                startActivityForResult(intentNiceName, NICE_NAME);
                break;
            case R.id.relative_sex:
                updateSex();
                break;
            case R.id.relative_type:
                updateType();
                break;
            case R.id.relative_name:
                Intent intentName = new Intent(this, UpdateNameActivity.class);
                startActivityForResult(intentName, NAME);
                break;
            case R.id.relative_mobile:
                Intent intentLabel = new Intent(this, UpdateMobileActivity.class);
                startActivityForResult(intentLabel, MOBILE);
                break;
            case R.id.relative_livingcity:
                Intent intentLivingCity = new Intent(this, UpdateLivingCityActivity.class);
                startActivityForResult(intentLivingCity, LIVING_CITY);
                break;
            case R.id.relative_constellation:
                SimpleAdapter adapter = new SimpleAdapter(UpdateUserInfoActivity.this, false, strings);
                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapter, itemClickListener(0), dismissListener, cancelListener, true);
                break;
            case R.id.relative_grade:
                SimpleAdapter adapterGrade = new SimpleAdapter(UpdateUserInfoActivity.this, false, grades);
                showOnlyContentDialog(new ListHolder(), Gravity.BOTTOM, adapterGrade, itemClickListener(1), dismissListener, cancelListener, true);
                break;
            case R.id.relative_card:
                Intent intentCard = new Intent(this, CardDegreesActvity.class);
                startActivityForResult(intentCard, LIVING_CITY);
                break;
        }
    }
//    private void startPhotoZoomOne(Uri uri) {
//        try {
//            // 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
//            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
//            String address = sDateFormat.format(new Date());
//            if (!PhotoFileUtils.isFileExist("")) {
//                PhotoFileUtils.createSDDir("");
//
//            }
//            imagePath = PhotoFileUtils.SDPATH + address + ".JPEG";
//            Uri imageUri = Uri.parse("file:///sdcard/formats/" + address + ".JPEG");
//
//            final Intent intent = new Intent("com.android.camera.action.CROP");
//            // Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//Log.i("TAG","imagePaht="+imagePath);
//            // 照片URL地址
//            intent.setDataAndType(uri, "image/*");
//
//            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 2);
//            intent.putExtra("aspectY", 2);
//            intent.putExtra("outputX", 200);
//            intent.putExtra("outputY", 200);
//            // 输出路径
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            // 输出格式
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//            // 不启用人脸识别
//            intent.putExtra("noFaceDetection", false);
//            intent.putExtra("return-data", false);
//            startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void photo() {
//        String savePath = "";
//        String storageState = Environment.getExternalStorageState();
//        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunduo/Camera/";
//            File savedir = new File(savePath);
//            if (!savedir.exists()) {
//                savedir.mkdirs();
//            }
//        }
//
//        // 没有挂载SD卡，无法保存文件
//        if (StringUtils.isEmpty(savePath)) {
//            showToast("无法保存照片，请检查SD卡是否挂载");
//            return;
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        String fileName = "dxj_" + timeStamp + ".jpg";// 照片命名
//        File out = new File(savePath, fileName);
//        photoUri = Uri.fromFile(out);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//        startActivityForResult(intent, TAKE_PICTURE);
//    }


}
