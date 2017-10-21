package my.tlol.com.frighting.wxapi;

import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.activity.JiayouActivity;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.AliOrder;
import my.tlol.com.frighting.bean.WeChatOrder;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;

/**
 * Created by xiaoxin on 2017/10/21 0021
 */

public class PayDao {


    public static void WechatPay(final Activity activity, int mValue, int mIZhe1, int def) {
        int userId = MyApplication.getInstance().getUser().getUserVoLog().getUserId();
        int total_price = mValue * mIZhe1 * def;
        OkHttpHelper.getInstance().post(Contants.API.ORDER_PAY_WECHAT + "?body=便捷加油" + "&detial=充值" + "&total_fee=" + total_price + "&spbill_create_ip=47.92.136.132" + "&userId=" + userId, null, new SimpleCallback<String>(activity) {
            @Override
            public void onSuccess(Response response, final String pay_order) {
                Gson gson = new Gson();
                final WeChatOrder aliOrder = gson.fromJson(pay_order, WeChatOrder.class);
                final WeChatOrder.UnifiedOrderBean pay_num = aliOrder.getUnifiedOrder();
                //初始化微信api
                final IWXAPI api = WXAPIFactory.createWXAPI(activity, PayConstants.APP_ID);
                //注册appid  appid可以在开发平台获取
                api.registerApp(PayConstants.APP_ID);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //创建支付请求参数
                        PayReq req = new PayReq();
                        req.appId = PayConstants.APP_ID;
                        req.partnerId = pay_num.getPartnerid() + "";
                        req.prepayId = pay_num.getPrepayid() + "";
                        req.packageValue = "Sign=WXPay";
                        req.nonceStr = pay_num.getNoncestr() + "";
                        req.timeStamp = pay_num.getTimestamp() + "";
                        req.sign = pay_num.getSign();
                        //发起微信支付请求
                        api.sendReq(req);
                    }
                }).start();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.v("jiayouActivity", e.getMessage());
            }
        });
    }


    //支付宝支付
    public static void Alipays(final Activity activity, int mValue, int mIZhe1, int def) {
        Map<String, Object> params = new HashMap<>();
        params.put("body", "便捷加油");
        params.put("subject", "充值");
        params.put("total_amount", mValue * mIZhe1 / 100 * def);
        params.put("userId", MyApplication.getInstance().getUser().getUserVoLog().getUserId());

        OkHttpHelper.getInstance().post(Contants.API.ORDER_PAY_ALI, params, new SimpleCallback<String>(activity) {
            @Override
            public void onSuccess(Response response, final String pay_order) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        AliOrder aliOrder = gson.fromJson(pay_order, AliOrder.class);
                        PayTask alipay = new PayTask(activity);
                        //确认付款界面关闭时才会返回result
                        Map<String, String> result = alipay.payV2(aliOrder.getOrder(), true);
                        Message msg = Message.obtain();
                        msg.obj = result;
                        msg.what = 1;
                        ((JiayouActivity) activity).getHandler().sendMessage(msg);
                    }
                }).start();


            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
