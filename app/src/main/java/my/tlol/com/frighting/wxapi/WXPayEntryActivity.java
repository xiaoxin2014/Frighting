package my.tlol.com.frighting.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PayConstants.APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }


    //发起支付请求在这里回调
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Log.d(TAG, "onPayFinish, errCode = " + resp);
        Log.d(TAG, "onPayFinish, errCode = " + resp.transaction);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            switch (code) {
                case 0:
//                    PayDao.queryResult(PayConstants.orderNo, null);
                    Log.d(TAG, "支付成功");
                    finish();
                    break;
                case -1:
                    Log.d(TAG, "支付失败");
                    makeText(this, "充值失败", LENGTH_SHORT).show();
                    finish();
                    break;
                case -2:
                    Log.d(TAG, "支付取消");
                    makeText(this, "充值失败", LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    Log.d(TAG, "支付失败");
                    setResult(RESULT_OK);
                    makeText(this, "充值失败", LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }

    }
}