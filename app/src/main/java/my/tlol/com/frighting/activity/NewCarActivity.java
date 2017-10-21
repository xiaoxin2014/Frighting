package my.tlol.com.frighting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.adapter.NewCarAdapter;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.HePage;
import my.tlol.com.frighting.bean.NewCar;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.utils.PreferencesUtils;
import my.tlol.com.frighting.widget.SpacesItemDecoration;



public class NewCarActivity extends AppCompatActivity {

    private EasyRecyclerView recyclerView;
    private NewCarAdapter adapter;
    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        init();

    }
    private DisplayImageOptions options;
    private void init() {
        options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.drawable.load_fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView= (EasyRecyclerView)findViewById(R.id.recyclerView);
        //recyclerView.setPadding(4, 4, 4, 4);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(6));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NewCarAdapter(this,options);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(NewCarActivity.this, NewCarDetailsActivity.class);
                intent.putExtra("carId",list.get(position).getCarId());
                NewCarActivity.this.startActivity(intent);
            }
        });
        okData();
        recyclerView.setAdapter(adapter);
        //adapter.addAll(getData());

    }


    List<NewCar.CarMainVoEntity> list;
    private void okData() {
        Map<String,Object> params = new HashMap<>(2);
        params.put("page", 1);
        params.put("rows", 20);
        okHttpHelper.post(Contants.API.MY_COLLECT, params, new SpotsCallBack<NewCar>(this) {
            @Override
            public void onSuccess(Response response, NewCar o) {
                if (o.getMsg().getCode()==0) {
                    list=o.getCarMainVo();
                    adapter.addAll(list);
                }else {
                    if (o.getMsg().getCode()==101) {
                        if (PreferencesUtils.getBoolean(mContext,"isLogin",false)) {
                            login(PreferencesUtils.getString(MyApplication.getInstance().getContext(),"telephone"),PreferencesUtils.getString(MyApplication.getInstance().getContext(),"password"));
                        }else {
                            Toast.makeText(mContext, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    }else {
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
                        PreferencesUtils.putString(mContext, "session", session.substring(0, session.indexOf(";")));
                    }
                    PreferencesUtils.putBoolean(mContext, "isLogin", true);
                    MyApplication application = MyApplication.getInstance();
                    application.putUser(userLoginRespMsg);
                    getData();
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

    public List<HePage.NoteVoListEntity> getData(){
        List<HePage.NoteVoListEntity> listItem=new ArrayList<>();
        listItem.add(new HePage.NoteVoListEntity());
        listItem.add(new HePage.NoteVoListEntity());
        listItem.add(new HePage.NoteVoListEntity());
        listItem.add(new HePage.NoteVoListEntity());
        listItem.add(new HePage.NoteVoListEntity());
        listItem.add(new HePage.NoteVoListEntity());
        listItem.add(new HePage.NoteVoListEntity());

        return listItem;
    }
}
