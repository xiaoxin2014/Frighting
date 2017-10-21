package my.tlol.com.frighting.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.activity.LoginActivity;
import my.tlol.com.frighting.activity.MoneyHisVos;
import my.tlol.com.frighting.adapter.WalletListAdapter;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.MessageItem;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;
import my.tlol.com.frighting.utils.PreferencesUtils;


public class WalletFragment extends BaseHomeFragment{


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private EasyRecyclerView recyclerView;
    private WalletListAdapter adapter;
    //private List<MessageItem> list=new ArrayList<>();


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view=inflater.inflate(R.layout.fragment_hot,container,false);
        recyclerView= (EasyRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WalletListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        getData();
        return view;
    }
    List<MoneyHisVos.MoneyHisVosEntity> list;
    private void getData() {
        okHttpHelper.get(Contants.API.WTS_LIST, new SimpleCallback<MoneyHisVos>(getActivity()) {
            @Override
            public void onSuccess(Response response, MoneyHisVos o) {
                if (o.getMsg().getCode()==0){
                    list=o.getMoneyHisVos();
                    adapter.addAll(list);
                }
                else {
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
        PreferencesUtils.putBoolean(getActivity(), "isLogin", false);
        okHttpHelper.post(Contants.API.LOGIN, params, new SimpleCallback<User>(getActivity()) {

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


    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void initData() {
        /*List<MessageItem> listItem=new ArrayList<>();
         listItem.add(new MessageItem("","","",""));
         listItem.add(new MessageItem("","","",""));
         listItem.add(new MessageItem("","","",""));
             adapter.addAll(listItem);*/
    }


}
