package my.tlol.com.frighting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.adapter.NewCarAdapter;
import my.tlol.com.frighting.adapter.TaoCanListAdapter;
import my.tlol.com.frighting.adapter.WalletListAdapter;
import my.tlol.com.frighting.bean.NewCar;
import my.tlol.com.frighting.bean.TaoCan;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SpotsCallBack;

/**
 * Created by tlol20 on 2017/6/21
 */
public class TaocanActivity extends AppCompatActivity{


    private EasyRecyclerView recyclerView;
    private TaoCanListAdapter adapter;
    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taocan);
        getDara();
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDara() {
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView= (EasyRecyclerView)findViewById(R.id.recyclerView);
        adapter = new TaoCanListAdapter(this);
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
    }

    Context context=this;
    List<TaoCan.ActivityEntity> list;
    private void okData() {
        Map<String,Object> params = new HashMap<>(2);
        params.put("page", 1);
        params.put("rows", 20);
        okHttpHelper.post(Contants.API.ACTIVITYLIST, params, new SpotsCallBack<TaoCan>(this) {
            @Override
            public void onSuccess(Response response, TaoCan o) {
                if (o.getMsg().getCode() == 0) {
                    list=o.getActivity();
                    adapter.addAll(list);
                } else {
                    Toast.makeText(context, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
