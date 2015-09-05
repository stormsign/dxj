package com.dxj.student.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.dxj.student.activity.UpdateUserInfoActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kings on 9/2/2015.
 */
public class UpdatePhotoUtils {
    //上传图片的第一步 从系统相册选择图片
    public static void startPhotoZoom(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), UpdateUserInfoActivity.RESULT_LOAD_IMAGE);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), UpdateUserInfoActivity.RESULT_LOAD_IMAGE);
        }
    }

    /**
     * 系统裁剪
     *
     * @param uri
     * @param activity
     */
    public static void startPhotoZoomOne(Uri uri, Activity activity) {
      Log.i("TAG","startPhotoZoomOne");
        String address = null;
        try {
            address = getImageAddress();
            Log.i("TAG","address="+address);
            Uri imageUri = Uri.parse("file:///sdcard/formats/" + address + ".JPEG");

            final Intent intent = new Intent("com.android.camera.action.CROP");
            // Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            // 照片URL地址
            intent.setDataAndType(uri, "image/*");

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 2);
            intent.putExtra("aspectY", 2);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            // 输出路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // 不启用人脸识别
            intent.putExtra("noFaceDetection", false);
            intent.putExtra("return-data", false);
            activity.startActivityForResult(intent, UpdateUserInfoActivity.CUT_PHOTO_REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getImageAddress() throws IOException {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String address = sDateFormat.format(new Date());

        return address;
    }

    /**
     * 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
     */
    public static String getImagePath() {
        try {
            String address = getImageAddress();
            if (!PhotoFileUtils.isFileExist("")) {
                PhotoFileUtils.createSDDir("");

            }
            String imagePath = PhotoFileUtils.SDPATH + address + ".JPEG";
            return imagePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File photo(Context context) {
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunduo/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
            ToastUtils.showToast(context, "无法保存照片，请检查SD卡是否挂载");
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "dxj_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        return out;
    }
}
