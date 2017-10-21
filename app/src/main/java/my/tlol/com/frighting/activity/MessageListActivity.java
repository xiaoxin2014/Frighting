package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.adapter.FansListAdapter;
import my.tlol.com.frighting.bean.Message;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;

/**
 * Created by tlol20 on 2017/7/22
 */

public class MessageListActivity extends AppCompatActivity{
    private EasyRecyclerView recyclerView;
    private FansListAdapter adapter;
    private OkHttpHelper ok= OkHttpHelper.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView= (EasyRecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new FansListAdapter(this));
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(MessageListActivity.this,IntroductionActivity.class);
                intent.putExtra("userIdOth",list.get(position).getMesId());
                MessageListActivity.this.startActivity(intent);
            }
        });
        getData();
    }
    List<Message.DayMesVoEntity> list;
    private void getData() {
        ok.get(Contants.API.MESSAGE, new SimpleCallback<Message>(this) {
            @Override
            public void onSuccess(Response response, Message o) {
                list=o.getDayMesVo();
                adapter.addAll(list);
                /*List<String> list=new ArrayList<>();
                for (Message.MsgEntity entity : o.getMsg()) {
                    list.add(entity.getDaysMessage());
                }*/
                //daysMessage.setText(o.getMsg().get(0).getDaysMessage());
                //daysMessage.setTipList(list);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

}
