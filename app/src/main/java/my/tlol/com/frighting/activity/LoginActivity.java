package my.tlol.com.frighting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.utils.PreferencesUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by tlol20 on 2017/6/10
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText number;
    private EditText password;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private SpotsDialog dialog;
    private String phone;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (PreferencesUtils.getBoolean(this, "isLogin", false)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }*/
        setContentView(R.layout.activity_login);
        number= (EditText) findViewById(R.id.number);
        password= (EditText) findViewById(R.id.password);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.forgetpassword).setOnClickListener(this);
        findViewById(R.id.tv_msg).setOnClickListener(this);
        findViewById(R.id.out).setOnClickListener(this);
        dialog = new SpotsDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_msg:{
                startVx(this);
            }break;
            case R.id.out:{
                finish();
            }break;
            case R.id.forgetpassword:{
                startActivity(new Intent(this,ForgetActivity.class));
            }break;
            case R.id.login:{
                login();
            }break;
            case R.id.register:{
                startActivityForResult(new Intent(this,RegisterActivity.class),1);
            }break;
        }
    }

    private void startVx(Context context) {



        /*Platform wechat= ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(paListener);
        wechat.authorize();*/
        /*LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(Wechat.NAME);
        api.setOnLoginListener(new OnLoginListener() {
            public boolean onLogin(String platform, HashMap<String, Object> res) {

                Toast.makeText(LoginActivity.this, "onLogin", Toast.LENGTH_SHORT).show();
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                return true;
            }

            public boolean onRegister(UserInfo info) {
                Toast.makeText(LoginActivity.this, "onRegister", Toast.LENGTH_SHORT).show();
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                return true;
            }
        });
        api.login(this);*/
    }

    private void login() {
        phone = number.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        pwd = password.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        checkPhoneNum(phone, "+86");
        //Test(phone, pwd);
        Map<String,Object> params = new HashMap<>(2);
        params.put("telephone", phone);
        params.put("password", pwd);

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<User>(this) {


            @Override
            public void onSuccess(Response response, User userLoginRespMsg) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                //result={"Msg":{"code":11004010,"text":"手机号格式错误"}}
                if (userLoginRespMsg.getMsg().getCode() == 0) {

                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    if (cookies.size()>0) {
                    String session = cookies.get(0);
                    Log.e("login", "session" + session.substring(0, session.indexOf(";")));
                    PreferencesUtils.putString(LoginActivity.this, "session", session.substring(0, session.indexOf(";")));
                    }
                    //Log.d("login", userLoginRespMsg.getUserVoLogin().toString());

                    PreferencesUtils.putBoolean(LoginActivity.this, "isLogin", true);
                    PreferencesUtils.putString(LoginActivity.this, "telephone", phone);
                    PreferencesUtils.putString(LoginActivity.this, "password", pwd);
                    MyApplication application = MyApplication.getInstance();
                    application.putUser(userLoginRespMsg);
                    Toast.makeText(LoginActivity.this, "登入成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登入失败！" + userLoginRespMsg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(LoginActivity.this, code+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);


            }
        });
    }

    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return ;
        }

        if (code == "86") {
            if(phone.length() != 11) {
                Toast.makeText(LoginActivity.this, "手机号码长度不对", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            Toast.makeText(LoginActivity.this, "您输入的手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

    }
    public void Test(String phone,String pwd){

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("telephone", phone)
                .add("password", pwd)
                .build();
        Request request = new Request.Builder()
                .url(Contants.API.LOGIN)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("网络请求", "上传失败" + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                String resultValue = response.body().string();
                Log.e("网络请求", "上传照片成功" + resultValue);
                /*final LoginRespMsg uploadImg = JSONUtil.fromJson(resultValue, LoginRespMsg.class);
                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (uploadImg.getMsg().getCode() == 0) {
                            okhttp3.Headers headers = response.headers();
                            List<String> cookies = headers.values("Set-Cookie");
                            String session = cookies.get(0);
                            Log.d("register", "session" + session.substring(0, session.indexOf(";")));
                            PreferencesUtils.putString(LoginActivity.this, "session", session.substring(0, session.indexOf(";")));

                            Log.d("login", uploadImg.getUserVoLogin().toString());
                            PreferencesUtils.putBoolean(LoginActivity.this, "isLogin", true);
                            MyApplication application = MyApplication.getInstance();
                            application.putUser(uploadImg.getUserVoLogin(), uploadImg.getRongYunToken());
                            Toast.makeText(LoginActivity.this, "登入成功！", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "登入失败！" + uploadImg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Looper.loop();*/
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==11){
            //setResult(11);
            finish();
        }

    }
}
