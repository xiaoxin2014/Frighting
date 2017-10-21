package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.adapter.IMListAdapter;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.Mess;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.utils.PreferencesUtils;

/**
 * Created by tlol20 on 2017/6/21
 */
//我的客服界面
public class ServiceActivity extends AppCompatActivity{
    private EasyRecyclerView recyclerView;
    private IMListAdapter adapter;
    private Button bt;
    private EditText et;
    TextView tips,content;
    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        bt= (Button) findViewById(R.id.button);
        bt.setEnabled(false);
        /*if (PreferencesUtils.getBoolean(this,"service",false)) {
            bt.setEnabled(false);
        }else {
            bt.setEnabled(true);
        }*/
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView= (EasyRecyclerView)findViewById(R.id.recyclerView);
        et= (EditText) findViewById(R.id.et);
        tips= (TextView) findViewById(R.id.tips);
        content= (TextView) findViewById(R.id.content);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        adapter = new IMListAdapter(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*Intent intent = new Intent(NewCarActivity.this, NewCarDetailsActivity.class);
                intent.putExtra("carId",list.get(position).getCarId());
                NewCarActivity.this.startActivity(intent);*/
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        okData();
        recyclerView.setAdapter(adapter);
    }

    private void send() {
        Map<String,Object> params = new HashMap<>(1);
        params.put("content",et.getText().toString().trim() );
        Mess.CustomerVoEntity m=new Mess.CustomerVoEntity();
        m.setContent(et.getText().toString().trim());
        m.setStatus("Y");
        adapter.add(m);
        //tips.setVisibility(View.VISIBLE);
        tips.setText(et.getText().toString().trim());
        content.setText(list.get(0).getContent());
        bt.setEnabled(false);
        et.setText("");
        PreferencesUtils.putBoolean(ServiceActivity.this,"service",true);
        okHttpHelper.post(Contants.API.CUSTIMER, params, new SimpleCallback<Msg>(this) {
            @Override
            public void onSuccess(Response response, Msg o) {

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    List<Mess.CustomerVoEntity> list;
    private void okData() {
        okHttpHelper.get(Contants.API.CUSTIMER_LIST, new SpotsCallBack<Mess>(this) {
            @Override
            public void onSuccess(Response response, Mess o) {
                if (o.getMsg().getCode() == 0) {
                    list = o.getCustomerVo();
                    if (list.get(0)!=null&&list.get(0).getStatus().equals("Y")) {
                        //tips.setVisibility(View.VISIBLE);
                        tips.setText("Tips:待客服回复");
                        content.setText(list.get(0).getContent());
                        bt.setEnabled(false);
                        PreferencesUtils.putBoolean(ServiceActivity.this,"service",true);
                    }else {
                        tips.setVisibility(View.GONE);
                        content.setText(list.get(0).getContent());
                        PreferencesUtils.putBoolean(ServiceActivity.this,"service",false);
                        bt.setEnabled(true);
                    }
                    adapter.addAll(list);
                } else {
                    //Toast.makeText(context, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                    if (o.getMsg().getCode() == 101) {
                        if (PreferencesUtils.getBoolean(ServiceActivity.this, "isLogin")) {
                            login(PreferencesUtils.getString(MyApplication.getInstance().getContext(), "telephone"), PreferencesUtils.getString(MyApplication.getInstance().getContext(), "password"));
                        } else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    } else {
                        Toast.makeText(mContext, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void login(String telephone, String password) {
        Map<String,Object> params = new HashMap<>(2);
        params.put("telephone",telephone);
        params.put("password", password);
        PreferencesUtils.putBoolean(this, "isLogin", false);
        okHttpHelper.post(Contants.API.LOGIN, params, new SimpleCallback<User>(this) {

            @Override
            public void onSuccess(Response response, User userLoginRespMsg) {
                //result={"Msg":{"code":11004010,"text":"手机号格式错误"}}
                if (userLoginRespMsg.getMsg().getCode() == 0) {

                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    if (cookies.size()>0) {
                        String session = cookies.get(0);
                        Log.e("login", "session" + session.substring(0, session.indexOf(";")));
                        PreferencesUtils.putString(getApplicationContext(), "session", session.substring(0, session.indexOf(";")));
                    }
                    PreferencesUtils.putBoolean(getApplicationContext(), "isLogin", true);
                    MyApplication application = MyApplication.getInstance();
                    application.putUser(userLoginRespMsg);
                } else {
                    Toast.makeText(getApplicationContext(), "登入失败！" + userLoginRespMsg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getApplicationContext(), "error:"+code, Toast.LENGTH_SHORT).show();
            }

        });
    }

}
