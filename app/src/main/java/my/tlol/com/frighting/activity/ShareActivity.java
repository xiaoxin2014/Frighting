package my.tlol.com.frighting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import my.tlol.com.frighting.R;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;
import my.tlol.com.frighting.http.UnifiedOrderBean;
import my.tlol.com.frighting.utils.PayResult;
import my.tlol.com.frighting.widget.NumberAddSubView;


/**
 * Created by tlol20 on 2017/6/21
 */
public class ShareActivity extends AppCompatActivity {
//    private String appid = "wxf32d800355649b09";
//    private String noncestr = "m6yqvTparHOwbQpY";
//    private String packager = "Sign=WXPay";
//    private String partnerid = "1426848502";
//    private String prepayid = "wx201710201140044feffbe5320120837508";
//    private String sign = "A6098E4E7CD0FBCFC870B88F3034EA9F2CB13E1695B4BC4CDD45F5F3B2B2C913";
//    private String timestamp = "1508470619";
    private String test = "http://47.92.136.132:8080/jyb/alipay/callSpCartOrderPrepForWxpay?body=您正在向便捷加油进行充值操作&detial=充值&total_fee=49500&spbill_create_ip=113.87.43.149&userId=15";
     private OkHttpHelper ok =OkHttpHelper.getInstance();
//    "appid": "wxf32d800355649b09",
//            "noncestr": "m6yqvTparHOwbQpY",
//            "package": "Sign=WXPay",
//            "partnerid": "1426848502",
//            "prepayid": "wx201710201140044feffbe5320120837508",
//            "sign": "A6098E4E7CD0FBCFC870B88F3034EA9F2CB13E1695B4BC4CDD45F5F3B2B2C913",
//            "timestamp": 1508470619

    private Button mAlipay;
    private Button mWechar;
    private Button mBtn_qx;
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "ShareActivity";
    private NumberAddSubView numberAddSubView;

    private int def = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ShareActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ShareActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "handleMessage: " + resultInfo);
                    break;
                }
                default:
                    break;
            }
        }
    };


    private int mValue;
    private int mIZhe1;
    private String[] mPay_num1;
    private Context mContext;
    private String mPay_numm;
    private static final String APP_ID = "wxf32d800355649b09";
    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mIWXAPI = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mIWXAPI.registerApp(APP_ID);
        initview();
    }


    private void initview() {
        mAlipay = (Button) findViewById(R.id.alipay);
        mWechar = (Button) findViewById(R.id.wechar);
        mBtn_qx = (Button) findViewById(R.id.btn_qx);

//        微信支付
        mWechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击微信", Toast.LENGTH_SHORT).show();
                wechars();
            }
        });
//        取消按钮
        mBtn_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "您取消了支付", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, JiayouActivity.class));
            }
        });
    }

//    //微信支付
    private void wechars() {
//        Toast.makeText(mContext, "点击了微信支付", Toast.LENGTH_SHORT).show();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);
        params.put("body", "");
        params.put("total_fee", "");
        params.put("spbill_create_ip", "");
        ok.post(test, params, new SimpleCallback(ShareActivity.this) {
            @Override
            public void onSuccess(Response response, final Object o) {
                UnifiedOrderBean info =new UnifiedOrderBean();
                PayReq req = new PayReq();
                req.appId = info.getAppid();
                req.partnerId = info.getPartnerid();
                req.prepayId = info.getPrepayid();
                req.nonceStr = info.getNoncestr();
                req.timeStamp = String.valueOf(info.getTimestamp());
                req.packageValue = info.getPackageX();
                req.sign = info.getSign();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                //调用微信支付sdk支付方法
                mIWXAPI.sendReq(req);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
