package my.tlol.com.frighting.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.utils.PreferencesUtils;

/**
 * Created by tlol20 on 2017/6/21
 */
public class ChangePwdActivity extends AppCompatActivity{

    private TextView number;
    private EditText et_pwd,newPwd,sPwd;
    private SpotsDialog dialog;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new SpotsDialog(this);
        setContentView(R.layout.activity_change_pwd);
        number= (TextView) findViewById(R.id.phone);
//        number.setText("账户："+MyApplication.getInstance().getUser().getUserVoLog().getTelephone());
        if (MyApplication.getInstance().getUser()!=null){
            number.setText("账户："+MyApplication.getInstance().getUser().getUserVoLog().getTelephone());
        }

        et_pwd= (EditText) findViewById(R.id.pwd);
        sPwd= (EditText) findViewById(R.id.s_pwd);
        newPwd= (EditText) findViewById(R.id.new_pwd);
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chengePwd();
            }
        });

    }

    private void chengePwd() {

        String phone = number.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(ChangePwdActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = newPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(ChangePwdActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwdConfirm=sPwd.getText().toString().trim();
        if (!pwd.equals(pwdConfirm)) {
            Toast.makeText(ChangePwdActivity.this, "两次密码不相同！", Toast.LENGTH_SHORT).show();
            return;
        }



        Map<String,Object> params = new HashMap<>();
        params.put("telephone", phone);
        params.put("password", et_pwd.getText().toString());
        params.put("newPwdOne", pwd);
        params.put("newPwdTwo", pwdConfirm);


        okHttpHelper.post(Contants.API.CHANGE_PWD, params, new SpotsCallBack<Msg>(this) {
            @Override
            public void onSuccess(Response response, Msg user) {
                Log.d("register", "访问成功！");
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (user.getMsg().getCode() != 0) {
                    Toast.makeText(ChangePwdActivity.this, "修改失败:" + user.getMsg().getText(), Toast.LENGTH_SHORT).show();
                    return;
                }
                PreferencesUtils.putBoolean(mContext, "isLogin", false);
                Toast.makeText(ChangePwdActivity.this,user.getMsg().getText() , Toast.LENGTH_SHORT).show();
                finish();
                /*Headers headers = response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("register", "session" + session.substring(0, session.indexOf(";")));
                PreferencesUtils.putString(ForgetActivity.this, "session", session.substring(0, session.indexOf(";")));

                PreferencesUtils.putBoolean(ForgetActivity.this, "isLogin", true);
                Toast.makeText(ForgetActivity.this, "恭喜您！注册成功！", Toast.LENGTH_SHORT).show();
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg);

                startActivity(new Intent(ForgetActivity.this, MainActivity.class));*/

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                //Log.d("register","访问:"+code+"e:"+e.toString());
            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
                //Log.d("register","访问token:"+code);
            }
        });
    }
}
