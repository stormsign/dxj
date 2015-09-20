package com.dxj.student.http;

public class FinalData {

    // 用户的状态
    public final static int ONLINE_VALUE = 1; // 在线
    public final static int OFFLINE_VALUE = 2; // 离线
    // 向后台请求的url
    public final static String IMAGE_URL_UPLOAD = "http://upload.miuhouse.com/app/";// 上传
    public final static String URL_VALUE_COMMON = "http://192.168.1.108:8080/appcommon/";
    // public final static String IMAGE_URL_UPLOAD = "http://192.168.1.124:8080/app/";// 上传
    // public final static String IMAGE_URL_UPLOAD = "http://192.168.1.124:8080/app/";// 上传
    /**
     * 向服务器请求图片的url头
     */
    public final static String IMAGE_URL = "http://img.miuhouse.com";

    public final static String URL_YUNDUO = "http://yunduo.miuhouse.com/app/";

     public final static String URL_VALUE = "http://192.168.1.108:8080/appstudent/";
//     public final static String URL_VALUE = "http://cloud.miuhouse.com/app/";
    // 分享链接
    public final static String URL_SHARE = "http://cloud.miuhouse.com/down";
    public final static String URL_INDEX = "http://cloud.miuhouse.com/index";

    // 向后台请求时发送的公共参数
    public final static String APP_KEY_VALUE = "hothz";
    public final static String PHONE_TYPE_VALUE = "3"; // 手机类型，1-安卓系统
    public static String VERSION_CODE_VALUE;
    public static String IMEI_VALUE;
    // 向后台请求时发送的参数字段
    public final static String MD5 = "md5";
    public final static String TRANSDATA = "transData";
    public final static String PHONE_TYPE = "deviceType";
    public final static String VERSION_CODE = "version_code";
    public final static String IMEI = "imei";
    public final static String CMD = "cmd";
    public final static String CONTENT = "content";
    public final static String PAGE = "page";
    public final static String PAGE_SIZE = "page_size";
    public final static String TYPE_LOGIN = "type_login";
    public final static String OPEN_ID = "open_id";
    public final static String USER_ID = "user_id";
    public final static String NEW_NICK_NAME = "new_nick_name";
    public final static String NICK_NAME = "nick_name";
    public final static String PICTURE_POSITION = "Picture_position";
    public final static String VERSION = "version";
    public final static String VERSION_NAME = "version_name";
    public final static String HZ_INFO = "hz_info";
    public final static String TYPE = "type";
    public final static String WISH_INFO = "wish";
    public final static String HEAD_URL = "head_url";
    public final static String USER_INFO = "user_info";
    public final static String THEME = "theme";
    public final static String HZ_SIMPLE = "hz_simple";
    public final static String TYPE_HZ = "type_hz";
    public final static String SCREEN_ID = "screen_id";
    public final static String BASE64STRING = "base64String";
    public final static String FILE_NAME = "file_name";
    // 后台响应的参数字段
    public final static String CODE = "code";
    public final static String MSG = "msg";
    public final static String THEME_LIST = "theme_list";
    public final static String THEME_ID = "theme_id";
    public final static String DEADLINE = "deadline";
    public final static String THEME_INFO = "theme_info";
    public final static String THEME_TIPS = "theme _tips";
    public final static String THEME_RULE = "theme_rule";
    public final static String PUBLISH_TIME = "publish_time";
    public final static String SHARE_TIPS = "share_tips";
    public final static String SHARE_CONTENT = "share_content";
    public final static String HZ_LIST = "hz_list";
    public final static String HZ_ID = "hz_id";
    public final static String HZ_CONTENT = "hz_content";
    public final static String HZ_TRADITION = "hz_tradition";
    public final static String HZ_EXPLAIN = "hz_explain";
    public final static String CONTENT_LIST = "content_list";
    public final static String COUNT_TOTAL = "count_total";
    public final static String WISH_LIST = "wish_list";
    public final static String SUBMIT_TIME = "submit_time";
    public final static String SENDER = "sender";
    public final static String RECEIVER = "receiver";
    public final static String NEW_VERSION_URL = "new_version_url";
    public final static String NICKNAME = "nickname";
    public final static String HEADIMGURL = "headimgurl";
    public final static String SEX = "sex";
    public final static String GENDER = "gender";
    public final static String FIGUREURL_QQ_1 = "figureurl_qq_1";
    public final static String CITY = "city";
    public final static String PROVINCE = "province";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String EXPIRES_IN = "expires_in";
    public final static String REFRESH_TOKEN = "refresh_token";
    public final static String OPENID = "openid";
    public final static String ERRCODE = "errcode";
    public final static String ACT_RECORD_ID = "act_record_id";
    public final static String ACT_RECORD_LIST = "act_record_list";
    public final static String COMMENT_SIZE = "comment_size";
    public final static String COMMENT_LIST = "comment_list";
    public final static String REC_USER_ID = "rec_user_id";
    public final static String REC_NICK_NAME = "rec_nick_name";
    public final static String CMT_USER_ID = "cmt_user_id";
    public final static String CMT_NICK_NAME = "cmt_nick_name";
    public final static String LIKE_ID = "like_id";
    public final static String COMMENT_ID = "comment_id";

    // cmd的值（与后台接口名一致）
    public final static String CMD_LOADBANNERLIST = "loadBannerList";
    public final static String CMD_TOHZINPUT = "toHzInput";
    public final static String CMD_SAVEHZ = "saveHz";
    public final static String CMD_LOADINTERESTHZLIST = "loadInterestHzList";
    public final static String CMD_LOADINTERESTHZDETAIL = "loadInterestHzDetail";
    public final static String CMD_lOADWISHLIST = "loadWishList";
    public final static String CMD_SAVEWISH = "saveWish";
    public final static String CMD_LOGIN = "login";
    public final static String CMD_LOADUSERHZLIST = "loadUserHzList";
    public final static String CMD_CHECKVERSION = "checkVersion";
    public final static String CMD_LOADUSERINFO = "loadUserInfo";

    // 错误码
    public final static int CODE_SUCCEED = 0; // 请求成功
    public final static int CODE_SERVER_ERROR = 500; // 服务器内部错误

    // 值
    public final static int USER_TYPE_VALUE_MY = 0;
    public final static int USER_TYPE_VALUE_ALL = 1;

    public final static int TYPE_LOGIN_VALUE_QQ = 0;
    public final static int TYPE_LOGIN_VALUE_WX = 1;

    public final static int MAN_VALUE = 1;
    public final static int FEMALE_VALUE = 2;

    public final static int FAIL_VALUE = -100;

    public final static int TAKEPHOTO_VALUE = 1;
    public final static int CHOOSEPHOTO_VALUE = 2;
    public final static int ZOOMEPHOTO_VALUE = 3;

    public final static int NET_STATE_CLOSE = -1;
    public final static int NET_STATE_OPEN = 0;
    public final static int NET_STATE_BAD = 1;

    // user.xml
    public final static String XML_USER = "user";
    public final static String CURRENT_USER_ID = "current_user_id"; // 最近一次登录的用户id
    public final static String CURRENT_USER_HEAD_URL = "current_user_head_url";
    public final static String CURRENT_USER_NICK_NAME = "current_user_nick_name";

}
