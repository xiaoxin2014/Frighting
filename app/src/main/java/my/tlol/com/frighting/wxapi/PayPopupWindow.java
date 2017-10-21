package my.tlol.com.frighting.wxapi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import my.tlol.com.frighting.R;


/**
 * Created by felear on 2017/8/26 0026.
 */

public class PayPopupWindow extends PopupWindow {

    private final Activity mContext;

    public void setValue(int value) {
        this.value = value;
    }

    public void setZhe(int zhe) {
        this.zhe = zhe;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private final RadioGroup mRg_pay_way;
    private int value;
    private int zhe;
    private int num;

    public PayPopupWindow(final Activity activity) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_sel_pay, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        mContext = activity;
        setTouchable(true);
        // 设置背景颜色
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(R.style.pw_slide);

        //  弹出窗监听
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1;
                activity.getWindow().setAttributes(params);
            }
        });

//        mTvAmount = (TextView) getContentView().findViewById(R.id.tv_pw_amount);
        mRg_pay_way = (RadioGroup) getContentView().findViewById(R.id.rg_pay_way);

        getContentView().findViewById(R.id.tv_pw_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });

        //确认付款
        getContentView().findViewById(R.id.tv_pay_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRg_pay_way.getCheckedRadioButtonId() == R.id.rb_ali) {
                    PayDao.Alipays(activity, value, zhe, num);
                } else if (mRg_pay_way.getCheckedRadioButtonId() == R.id.rb_wechat) {
                    PayDao.WechatPay(activity, value, zhe, num);
                }

                if (isShowing()) {
                    dismiss();
                    WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                    params.alpha = 1;
                    activity.getWindow().setAttributes(params);
                }
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = 0.6f;
        mContext.getWindow().setAttributes(params);
    }

}
