package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
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
import my.tlol.com.frighting.adapter.CardListAdapter;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.CardList;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.listener.MyButtenClickListener;
import my.tlol.com.frighting.utils.PreferencesUtils;

/**
 * Created by tlol20 on 2017/6/21
 */
public class MyCardActivity extends AppCompatActivity{

    private OkHttpHelper ok;
    private EasyRecyclerView recyclerView;
    private CardListAdapter adapter;
    List<CardList.GasCardVoEntity> list;
    TextView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycard);
        findViewById(R.id.bind_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCardActivity.this.startActivityForResult(new Intent(MyCardActivity.this,AddCardActivity.class),1);
            }
        });
        add= (TextView) findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCardActivity.this.startActivityForResult(new Intent(MyCardActivity.this,AddCardActivity.class),1);
            }
        });
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDara();

    }
    private void getDara() {
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView= (EasyRecyclerView)findViewById(R.id.recyclerView);
        adapter = new CardListAdapter(this);
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
        adapter.setMyButtonClickListener(new MyButtenClickListener() {
            @Override
            public void onButtenClick(View view, int position) {
                        delete(position);
            }
        });
        getData();
    }
    private void delete(final int position) {

        Map<String,Object> params = new HashMap<>();
        params.put("gasCardId",list.get(position).getGasCardId());
        OkHttpHelper.getInstance().post(Contants.API.DELETE_CARD, params, new SpotsCallBack<Msg>(this) {
            @Override
            public void onSuccess(Response response, Msg o) {
                if (o.getMsg().getCode()==0) {
                    adapter.remove(position);
                    getData();
                }else {
                    Toast.makeText(mContext, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    private void getData() {

        ok = OkHttpHelper.getInstance();
        ok.get(Contants.API.CARD_LIST, new SimpleCallback<CardList>(this) {
            @Override
            public void onSuccess(Response response, CardList o) {
                if (o.getMsg().getCode()==0) {
                    if (o.getGasCardVo().size()>0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        list=o.getGasCardVo();
                        adapter.removeAll();
                        adapter.addAll(list);
                    }
                }else {
                    if (o.getMsg().getCode()==101) {
                        if (PreferencesUtils.getBoolean(mContext,"isLogin",false)) {
                            login(PreferencesUtils.getString(MyApplication.getInstance().getContext(),"telephone"),PreferencesUtils.getString(MyApplication.getInstance().getContext(),"password"));
                        }else {
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    }else {
                        recyclerView.setVisibility(View.GONE);
                        add.setVisibility(View.GONE);
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
        ok.post(Contants.API.LOGIN, params, new SimpleCallback<User>(this) {

            @Override
            public void onSuccess(Response response, User userLoginRespMsg) {
                //result={"Msg":{"code":11004010,"text":"手机号格式错误"}}
                if (userLoginRespMsg.getMsg().getCode() == 0) {
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    if (cookies.size()>0) {
                        String session = cookies.get(0);
                        Log.e("login", "session" + session.substring(0, session.indexOf(";")));
                        PreferencesUtils.putString(mContext, "session", session.substring(0, session.indexOf(";")));
                    }
                    PreferencesUtils.putBoolean(mContext, "isLogin", true);
                    MyApplication application = MyApplication.getInstance();
                    application.putUser(userLoginRespMsg);
                } else {
                    Toast.makeText(mContext, "登入失败！" + userLoginRespMsg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(mContext, "error:"+code, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult","requestCode="+requestCode+">>resultCode="+resultCode);
        if (requestCode==1&&resultCode==11){
            getData();
        }
    }
}
