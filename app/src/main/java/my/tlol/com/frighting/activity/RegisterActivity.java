package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import my.tlol.com.frighting.bean.Reg;
import my.tlol.com.frighting.bean.Register;
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
 * Created by tlol20 on 2017/6/30
 */
public class RegisterActivity extends AppCompatActivity{
    private EditText number,et_code,et_pwd,sPwd,yaoq;
    private RadioButton radioButton;
    private Button button;
    private boolean flag=false;
    private Reg reg;
    private Register register;
    int smYzmId;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private SpotsDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog=new SpotsDialog(this);
        setContentView(R.layout.activity_register);
        number= (EditText) findViewById(R.id.number);
        et_code= (EditText) findViewById(R.id.code);
        et_pwd= (EditText) findViewById(R.id.pwd);
        sPwd= (EditText) findViewById(R.id.s_pwd);
        yaoq= (EditText) findViewById(R.id.yaoq);
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.get_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();

            }
        });
        button= (Button) findViewById(R.id.commit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        radioButton=(RadioButton)findViewById(R.id.radio);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    radioButton.setChecked(false);
                    button.setEnabled(false);
                    flag = false;
                } else {
                    radioButton.setChecked(true);
                    button.setEnabled(true);
                    flag = true;
                }
            }
        });
        findViewById(R.id.tv_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void register() {
        String phone = number.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = et_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwdConfirm=sPwd.getText().toString().trim();
        if (!pwd.equals(pwdConfirm)) {
            Toast.makeText(RegisterActivity.this, "两次密码不相同！", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = et_code.getText().toString().trim();
        if(code.length() != 6) {
            Toast.makeText(RegisterActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
            return;
        }


            Map<String,Object> params = new HashMap<>();
            params.put("telephone",phone);
            params.put("smYzmId", smYzmId);
            params.put("smYzm", code);
            params.put("password", pwd);
            params.put("pwdConfirm", pwdConfirm);
        /*OkHttpClient okClient = new OkHttpClient();


        FormBody body = new FormBody.Builder()
                .add("telephone", phone)
                .add("smYzmId", smYzmId+"")
                .add("smYzm", code)
                .add("password", pwd)
                .add("pwdConfirm", pwdConfirm)
                .build();


        Request request = new Request.Builder()
                .url(Contants.API.REG)
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

                register = JSONUtil.fromJson(s, Register.class);

                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (register.getMsg().getCode() == 0) {

                            Toast.makeText(RegisterActivity.this, register.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, register.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Looper.loop();
            }


        });*/
        PreferencesUtils.putBoolean(this, "isLogin", false);
        okHttpHelper.post(Contants.API.REG, params, new SpotsCallBack<User>(this) {
            @Override
            public void onSuccess(Response response, User userLoginRespMsg) {
                Log.d("register", "访问成功！");
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (userLoginRespMsg.getMsg().getCode() != 0) {
                    Toast.makeText(RegisterActivity.this, "注册失败:" + userLoginRespMsg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Headers headers = response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("register", "session" + session.substring(0, session.indexOf(";")));
                PreferencesUtils.putString(RegisterActivity.this, "session", session.substring(0, session.indexOf(";")));

                PreferencesUtils.putBoolean(RegisterActivity.this, "isLogin", true);
                Toast.makeText(RegisterActivity.this, "恭喜您！注册成功！", Toast.LENGTH_SHORT).show();
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg);
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();

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


    public void getCode(){

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
                .url(Contants.API.CODE)
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
                if (s!=null)
                reg = JSONUtil.fromJson(s, Reg.class);

                Looper.prepare();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (reg.getMsg().getCode() == 0) {
                            smYzmId = reg.getSmYzm().getSmYzmId();
                            Toast.makeText(RegisterActivity.this, reg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, reg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Looper.loop();
            }


        });

    }


    private void checkPhoneNum(String phone) {

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            Toast.makeText(RegisterActivity.this, "您输入的手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }


    }
}
