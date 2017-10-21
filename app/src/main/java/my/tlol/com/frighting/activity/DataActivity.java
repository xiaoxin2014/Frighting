package my.tlol.com.frighting.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Response;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.bean.Data;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;

/**
 * Created by tlol20 on 2017/6/21
 */
public class DataActivity extends AppCompatActivity{
    TextView endDay,
    every,
    money,
    people,
    startDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        endDay= (TextView) findViewById(R.id.endDay);
        every= (TextView) findViewById(R.id.every);
        money= (TextView) findViewById(R.id.money);
        people= (TextView) findViewById(R.id.people);
        startDay= (TextView) findViewById(R.id.startDay);
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        OkHttpHelper.getInstance().get(Contants.API.GET_DATA, new SimpleCallback<Data>(this) {
            @Override
            public void onSuccess(Response response, Data o) {
                endDay.setText(o.getJybDataVo().getEndDay());
                every.setText(o.getJybDataVo().getEvery());
                money.setText(o.getJybDataVo().getMoney());
                people.setText(o.getJybDataVo().getPeople());
                startDay.setText(o.getJybDataVo().getStartDay());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
