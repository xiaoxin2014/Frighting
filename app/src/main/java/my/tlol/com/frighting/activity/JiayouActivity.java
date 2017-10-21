package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import my.tlol.com.frighting.R;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.utils.PayResult;
import my.tlol.com.frighting.widget.NumberAddSubView;
import my.tlol.com.frighting.wxapi.PayPopupWindow;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * Created by tlol20 on 2017/6/21
 * 加油套餐界面
 */
public class JiayouActivity extends AppCompatActivity implements View.OnClickListener {
    private NumberAddSubView numberAddSubView;
    private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6;
    private int def = 1;
    private TextView yuanjia, zhekou, jies, value, moth, tv_yuanjia, tips;
    private Button mRecharge;
    private static final String TAG = "JiayouActivity";
    private String ali_test = "http://47.92.136.132:8080/jyb/alipay/callSpCartOrderPrepForWxpay?body=您正在向便捷加油进行充值操作&detial=充值&total_fee=49500&spbill_create_ip=113.87.43.149&userId=15";
    private String wechat_test = "47.92.136.132:8080/jyb/alipay/alipayurl?body=测试&subject=测试&total_amount=0.01";
    private static final int ALI_PAY = 1;

    public Handler getHandler() {
        return mHandler;
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ALI_PAY: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        makeText(JiayouActivity.this, "充值成功", LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        makeText(JiayouActivity.this, "充值失败", LENGTH_SHORT).show();
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
    private OkHttpHelper mOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiayoutaocan);
        numberAddSubView = (NumberAddSubView) findViewById(R.id.num_control);
        mOk = OkHttpHelper.getInstance();

        ll1 = (LinearLayout) findViewById(R.id.ll_01);
        ll2 = (LinearLayout) findViewById(R.id.ll_02);
        ll3 = (LinearLayout) findViewById(R.id.ll_03);
        ll4 = (LinearLayout) findViewById(R.id.ll_04);
        ll5 = (LinearLayout) findViewById(R.id.ll_05);
        ll6 = (LinearLayout) findViewById(R.id.ll_06);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);
        ll5.setOnClickListener(this);
        ll6.setOnClickListener(this);
        yuanjia = (TextView) findViewById(R.id.yuanjia);
        zhekou = (TextView) findViewById(R.id.zhekou);
        jies = (TextView) findViewById(R.id.jies);
        tips = (TextView) findViewById(R.id.tips);
        moth = (TextView) findViewById(R.id.moth);
        value = (TextView) findViewById(R.id.value);
        tv_yuanjia = (TextView) findViewById(R.id.tv_yuanjia);
        mRecharge = (Button) findViewById(R.id.recharge);
        initrecharge();


        numberAddSubView.setValue(500);
        numberAddSubView.setMaxValue(900);
        initdata();
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                initdata();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                initdata();
            }
        });
        findViewById(R.id.top_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JiayouActivity.this, StateActivity.class));
            }
        });
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initrecharge() {
        //点击充值按钮操作
        mRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().getUser() == null) {
                    Toast.makeText(JiayouActivity.this, "您还未登录，无法充值", Toast.LENGTH_SHORT).show();
                    return;
                }

                PayPopupWindow mPwPay = new PayPopupWindow(JiayouActivity.this);
                mPwPay.setValue(mValue);
                mPwPay.setZhe(mIZhe1);
                mPwPay.setNum(def);

                mPwPay.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

            }
        });
    }


    private void initdata() {
        mValue = numberAddSubView.getValue();
        mIZhe1 = 99;
        switch (def) {
            case 4:
                mIZhe1 = 98;
                break;
            case 6:
                mIZhe1 = 94;
                break;
            case 8:
                mIZhe1 = 90;
                break;
            case 10:
                mIZhe1 = 86;
                break;
            case 12:
                mIZhe1 = 82;
                break;
        }
        int iZhekou = mValue * mIZhe1 / 100 * def;
        Log.d("value", mValue + "---" + mIZhe1);
        value.setText(mValue + "");
        moth.setText(def + "");
        tips.setText(def + "个月充值 当天充值一笔");
        yuanjia.setText(def * mValue + "");
        tv_yuanjia.setText(def * mValue + "");
        zhekou.setText(iZhekou + "");
        jies.setText(def * mValue - iZhekou + "");

    }

    @Override
    public void onClick(View v) {
        ll1.setBackgroundResource(R.drawable.edittext_color_black);
        ll2.setBackgroundResource(R.drawable.edittext_color_black);
        ll3.setBackgroundResource(R.drawable.edittext_color_black);
        ll4.setBackgroundResource(R.drawable.edittext_color_black);
        ll5.setBackgroundResource(R.drawable.edittext_color_black);
        ll6.setBackgroundResource(R.drawable.edittext_color_black);
        switch (v.getId()) {
            case R.id.ll_01: {
                ll1.setBackgroundResource(R.drawable.edittext_color_orange);
                def = 1;
                initdata();
            }
            break;
            case R.id.ll_02: {
                def = 4;
                ll2.setBackgroundResource(R.drawable.edittext_color_orange);
                initdata();
            }
            break;
            case R.id.ll_03: {
                def = 6;
                ll3.setBackgroundResource(R.drawable.edittext_color_orange);
                initdata();
            }
            break;
            case R.id.ll_04: {
                def = 8;
                ll4.setBackgroundResource(R.drawable.edittext_color_orange);
                initdata();
            }
            break;
            case R.id.ll_05: {
                def = 10;
                ll5.setBackgroundResource(R.drawable.edittext_color_orange);
                initdata();
            }
            break;
            case R.id.ll_06: {
                def = 12;
                ll6.setBackgroundResource(R.drawable.edittext_color_orange);
                initdata();
            }
            break;

        }
    }

}
