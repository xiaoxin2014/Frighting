package my.tlol.com.frighting;


import okhttp3.MediaType;

public class Contants {


    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final MediaType MEDIA_TYPE_MP4 = MediaType.parse("video/mp4");
    public static final String COMPAINGAIN_ID = "compaigin_id";
    public static final String WARE = "ware";
    public static final String USER_JSON = "user_json";
    public static final String TOKEN = "token";

    public static final String DES_KEY = "Cniao5_123456";
    public static int FLAG = 620;

    public static final int REQUEST_CODE = 0;
    public static final int REQUEST_CODE_PAYMENT = 1;

    public static class API {
        public static final String BASE_URL = "http://47.92.136.132:8080/jyb/";
//        public static final String BASE_URL="http://192.168.0.254:8080/jyb/";

        public static final String MESSAGE = BASE_URL + "home/daysMessage";
        public static final String SHARE = BASE_URL + "home/shareMoment";
        public static final String PRICE = BASE_URL + "home/everyOilPrc";
        public static final String WXSHARE = BASE_URL + "home/wxShare";
        public static final String MESSAGEDETAIL = BASE_URL + "home/dayMessageDetail";

        public static final String GET_S_CODE = BASE_URL + "user/getImgYzm?imgYzmId=0";
        public static final String DELETE_CARD = BASE_URL + "user/delGasCard";
        public static final String SEARCH = BASE_URL + "home/validSelect";
        public static final String GET_DATA = BASE_URL + "home/jybData";


        public static final String LOGIN = BASE_URL + "user/pwdLogin";
        public static final String RED_LIST = BASE_URL + "user/aWardList";
        public static final String SHARE_LIST = BASE_URL + "user/shareList";
        public static final String WTS_LIST = BASE_URL + "user/wtSaveList";
        public static final String ADD_YCAR = BASE_URL + "user/yGasCard";
        public static final String ADD_HCAR = BASE_URL + "user/hGasCard";
        public static final String REG = BASE_URL + "user/telRegist?";
        public static final String CHANGE_PWD = BASE_URL + "user/changePwd?";
        public static final String FORGET_REG = BASE_URL + "user/forgetPwd?";
        public static final String CODE = BASE_URL + "user/getSmYzmForRegist?";
        public static final String LOGIN_CODE = BASE_URL + "user/getSmYzmForLogin?";
        public static final String CARD_LIST = BASE_URL + "user/GasCardList?";
        public static final String RESETCODE = BASE_URL + "user/getSmYzmForUpdPassword?";

        public static final String MY_COLLECT = BASE_URL + "home/carShowList?";
        public static final String BANNER = BASE_URL + "home/mainImg?";


        public static final String CUSTIMER_LIST = BASE_URL + "home/customerList";
        public static final String CUSTIMER = BASE_URL + "home/customer";

        public static final String DELETE_CAR = BASE_URL + "home/carDetails";
        public static final String ACTIVITYLIST = BASE_URL + "home/activityList";
        //阿里支付
        public static final String ORDER_PAY_ALI = BASE_URL + "alipay/alipayurl";
        //微信支付
        public static final String ORDER_PAY_WECHAT = BASE_URL + "alipay/callSpCartOrderPrepForWxpay";
    }

}
