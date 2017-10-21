package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.bean.Reg;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.utils.JSONUtil;
import my.tlol.com.frighting.utils.PreferencesUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by tlol20 on 2017/7/1
 */
public class ForgetActivity  extends AppCompatActivity {
    private EditText number,et_code,et_pwd,sPwd;
    private Reg reg;
    int smYzmId;
    private SpotsDialog dialog;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new SpotsDialog(this);
        setContentView(R.layout.activity_forget);

        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        number= (EditText) findViewById(R.id.number);
        et_code= (EditText) findViewById(R.id.code);
        et_pwd= (EditText) findViewById(R.id.pwd);
        sPwd= (EditText) findViewById(R.id.s_pwd);

        findViewById(R.id.get_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });
    }

    private void register() {
        String phone = number.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(ForgetActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = et_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(ForgetActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwdConfirm=sPwd.getText().toString().trim();
        if (!pwd.equals(pwdConfirm)) {
            Toast.makeText(ForgetActivity.this, "两次密码不相同！", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = et_code.getText().toString().trim();
        if(code.length() != 6) {
            Toast.makeText(ForgetActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String,Object> params = new HashMap<>();
        params.put("telephone", phone);
        params.put("smYzmId", smYzmId);
        params.put("smYzm", code);
        params.put("password", pwd);
        params.put("pwdConfirm", pwdConfirm);

        PreferencesUtils.putBoolean(this, "isLogin", false);
        okHttpHelper.post(Contants.API.FORGET_REG, params, new SpotsCallBack<Msg>(this) {
            @Override
            public void onSuccess(Response response, Msg user) {
                Log.d("register", "访问成功！");
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (user.getMsg().getCode() != 0) {
                    Toast.makeText(ForgetActivity.this, "修改失败:" + user.getMsg().getText(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ForgetActivity.this,user.getMsg().getText() , Toast.LENGTH_SHORT).show();

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

    private void getCode() {

        String phone = number.getText().toString().trim();

        checkPhoneNum(phone);
        Map<String, Object> params = new HashMap<>(1);
        params.put("phone", phone);
        Log.d("okhttp", "phone=" + phone);
        /*okHttpHelper.post(Contants.API.CODE, params, new SimpleCallback<Msg>(this) {
            @Override
            public void onSuccess(Response response, Msg o) {
                if (o.getMsg().getCode() == 0) {
                    Toast.makeText(RegisterActivity.this, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });*/
        OkHttpClient okClient = new OkHttpClient();


        FormBody body = new FormBody.Builder()
                .add("telephone", phone)
                .build();


        Request request = new Request.Builder()
                .url(Contants.API.LOGIN_CODE)
                .post(body)
                .build();

        Call call = okClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                Log.d("okhttp", "response2=" + s);
                if (s != null)
                  reg = JSONUtil.fromJson(s, Reg.class);

                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (reg.getMsg().getCode() == 0) {
                            smYzmId = reg.getSmYzm().getSmYzmId();
                            Toast.makeText(ForgetActivity.this, reg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetActivity.this, reg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Looper.loop();
            }


        });
    }

    private void checkPhoneNum(String phone) {

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            Toast.makeText(this, "您输入的手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }


    }

}
