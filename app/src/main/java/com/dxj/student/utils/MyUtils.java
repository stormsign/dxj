package com.dxj.student.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dxj.student.bean.StudyGroup;
import com.dxj.student.bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.GZIPInputStream;

/**
 * Created by khb on 2015/8/20.
 */
public class MyUtils {

    public final static String SMSSDK_APP_KEY = "a38245d89da2";
    public final static String SMSSDK_APP_SECRET = "84db45a5b7dcde895fc72f47edf53447";
    public static final String UMENG_MESSAGE_EXTRA = "extra";

    public static void openActivityFromUmengMessage(Context context, String activity, Map<String, String> extra) {
        if(activity != null && !TextUtils.isEmpty(activity.trim())) {
            Intent intent = new Intent();
            if (extra!=null) {
//            实际情况下只传了一个参数
                setIntentFromUmengExtra(intent, extra);
            }
            intent.setClassName(context, activity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private static Intent setIntentFromUmengExtra(Intent intent, Map<String, String> extra) {
        if(intent != null && extra != null) {
            Iterator iterator = extra.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                if(key != null) {
                    intent.putExtra(key, value);
                }
            }
            return intent;
        } else {
            return intent;
        }
    }

    public static Map<String, String> parseUmengExtra(String extra){
        Map<String,String> m = new HashMap<String,String>();
        if (extra != null) {
            extra = extra.substring(1, extra.length() - 1);//去掉括号
            String[] kvs = extra.split(",");//拆成key 和value 的组合
            for (String kv : kvs) {
                //拆成key 和value 分别放好
                m.put(kv.substring(0, kv.indexOf('=')), kv.substring(kv.indexOf('=') + 1));
            }
        }
        return m;
    }

    public static List<StudyGroup> getSecondListGroupsFromJson(String json, List<Integer> secondIdList){
        try {
            JSONObject jObject = new JSONObject(json);
            List<StudyGroup> groupList = new ArrayList<>();
            for (int secondId :
                    secondIdList) {
                if (jObject.has(secondId+"")) {
                    JSONArray jGroupArray = (JSONArray) jObject.get(secondId + "");
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<StudyGroup>>() {}.getType();
                    List<StudyGroup> groups = gson.fromJson(jGroupArray.toString(), type);
                    groupList.addAll(groups);
                }
            }
            return groupList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StudyGroup getGroupDetailFromJson(String json){

        try {
            JSONObject jObject = new JSONObject(json);
            if (jObject.has("group")){
                JSONObject jGroup = (JSONObject) jObject.get("group");
                Gson gson = new Gson();
                StudyGroup studyGroup = gson.fromJson(jGroup.toString(), StudyGroup.class);
                return studyGroup;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return  null;
    }

    public static List<UserBean.UserInfo> getGroupMembersFromJson(StudyGroup group, String json){
        List<String> members = new ArrayList<String>();
        List<UserBean.UserInfo> groupMembers = new ArrayList<UserBean.UserInfo>();
        try {
            JSONObject jObject = new JSONObject(json);
//            团长排在团员第一位
            members.add(group.getOwner());
            if (group.getMembers()!=null) {
                members.addAll(group.getMembers());
            }
            for (int i = 0; i<members.size(); i++){
                if (jObject.has(members.get(i))){
                    JSONObject jUserInfo = (JSONObject) jObject.get(members.get(i));
                    Gson gUserInfo = new Gson();
                    UserBean.UserInfo userInfo = gUserInfo.fromJson(jUserInfo.toString(), UserBean.UserInfo.class);
                    groupMembers.add(userInfo);
                }
            }
            return groupMembers;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 根据手机号拼接老师环信账号
     * @param mobile
     * @return
     */
    public static String getTeacherHX(String mobile) {
        return "teacher"+mobile;
    }

    /**
     * 根据手机号拼接学生环信账号
     * @param mobile
     * @return
     */
    public static String getStudentHX(String mobile){
        return "student"+mobile;
    }


    /**
     * 判断是否是该团成员，包括团长
     * @param teacherHX
     * @param studyGroup
     * @return
     */
    public static boolean isMember(String teacherHX, StudyGroup studyGroup) {
        List<String> members =  studyGroup.getMembers();
        if (members!=null && !members.isEmpty()){
            return (!studyGroup.getOwner().equals(teacherHX) && !members.contains(teacherHX))?false:true;
        }else{
            return studyGroup.getOwner().equals(teacherHX);
        }
    }



    /**
     * 判断当前网络环境是否为wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (ni.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕的宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 对字符串进行MD5加密 输出：MD5加密后的大写16进制密文
     *
     * @param text
     * @return
     */
    public static String md5String(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 获取 摘要器
        byte[] result = digest.digest(text.getBytes()); // 通过 摘要器对指定的数据进行加密，并返回到byte[]。
        StringBuffer sb = new StringBuffer(); // 保存十六进制的密文

        // 将加密后的数据 由byte(二进制)转化成Integer(十六进制)。
        for (byte b : result) {
            int code = b & 0xff;
            String s = Integer.toHexString(code);
            if (s.length() == 1) {
                sb.append("0");
                sb.append(s);
            } else {
                sb.append(s);
            }
        }
        return sb.toString().toUpperCase(); // 返回数据加密后的十六进制密文
    }

    public static String streamToStringEn(HttpURLConnection urlConnection) {
        // private String readResult(HttpURLConnection urlConnection) throws WeiboException {
        InputStream is = null;
        BufferedReader buffer = null;
        // GlobalContext globalContext = GlobalContext.getInstance();
        // String errorStr = globalContext.getString(R.string.timeout);
        // globalContext = null;
        try {
            is = urlConnection.getInputStream();

            String content_encode = urlConnection.getContentEncoding();

            if (!TextUtils.isEmpty(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            // AppLogger.d("result=" + strBuilder.toString());
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            // throw new WeiboException(errorStr, e);
        } finally {
            closeSilently(is);
            closeSilently(buffer);
            urlConnection.disconnect();
        }
        return null;
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * 将数据缓存到文件
     *
     * @param context
     * @param name    缓存文件名
     * @param data    数据
     */
    public static void saveDataToFile(Context context, String name, String data) {
        String path = toCachePath(context, name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换缓存文件路径
     *
     * @param context
     * @param name
     * @return
     */
    public static String toCachePath(Context context, String name) {
        return context.getFilesDir().getAbsolutePath() + "/" + name + ".txt";
    }

    /**
     * 判断电话号码是否合法
     *
     * @param mobiles
     * @return
     */
    public static Boolean isPhoneNum(String str) {
        String strPattern = "^[1][3,4,5,7,8][0-9]{9}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断有特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean checkSpecialChar(String str) throws PatternSyntaxException {
        String regEx = ".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否符合密码格式
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean checkPasswordSpecialChar(String str) throws PatternSyntaxException {
        String regEx = "^([A-Za-z]|[0-9])+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否全中
     *
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否全英
     *
     * @param charaString
     * @return
     */
    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    /**
     *
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath, Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    public static Bitmap createImageThumbnail(Context context, String largeImagePath, int square_size) throws IOException {
        // int targetW = mImageView.getWidth();
        // int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(largeImagePath, bmOptions);
        // Log.i("TAG", "bitmap:width=" + bitmap.getWidth() + "height=" + bitmap.getHeight());
        // Determine how much to scale down the image

        // Log.i("TAG", "calculateInSampleSize=" + calculateInSampleSize(bmOptions, 300, 600));
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions, 600, 600);
        bmOptions.inJustDecodeBounds = false;
        // bmOptions.inPurgeable = true;
        // BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inJustDecodeBounds = false;
        // options.inSampleSize = 5;
        // 原始图片bitmap
        Bitmap cur_bitmap = getBitmapByPath(largeImagePath, bmOptions);
        // Bitmap cur_bitmap = revitionImageSize(largeImagePath);
        Log.i("TAG", "cur_bitmap=" + cur_bitmap.getByteCount());
        if (cur_bitmap == null)
            return null;
        Log.i("TAG", "cur_bitmap+width=" + cur_bitmap.getWidth());
        Log.i("TAG", "cur_bitmap+height=" + cur_bitmap.getHeight());
        // 原始图片的高宽
        int[] cur_img_size = new int[]{cur_bitmap.getWidth(), cur_bitmap.getHeight()};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);
        // 生成缩放后的bitmap
        Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0], new_img_size[1]);
        // thb_bitmap.recycle();
        // 生成缩放后的图片文件
        // saveImageToSD(null, thumbfilePath, thb_bitmap, quality);
        return thb_bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        Log.i("TAG", "filePath=" + filePath);
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);

            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            Log.i("TAG", "OUTOFmEMORYError=" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio), (int) (img_size[1] * ratio)};
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return newbmp;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void recycleViewGroupAndChildViews(ViewGroup viewGroup, boolean recycleBitmap) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            if (child instanceof ViewGroup) {
                recycleViewGroupAndChildViews((ViewGroup) child, true);
                continue;
            }

            if (child instanceof ImageView) {
                Log.i("TAG", "ImageView");
                ImageView iv = (ImageView) child;
                Drawable drawable = iv.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (recycleBitmap && bitmap != null) {
                        bitmap.recycle();
                    }
                }
                iv.setImageBitmap(null);
                iv.setBackground(null);
                continue;
            }

            child.setBackground(null);

        }

        viewGroup.setBackground(null);
    }
}
