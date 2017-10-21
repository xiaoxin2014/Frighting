package my.tlol.com.frighting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.bean.AddCar;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SpotsCallBack;

/**
 * Created by tlol20 on 2017/6/21
 */
public class AddCardActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout zgsy,zgsh;
    View vYou,vHua;
    EditText
            h_car_number,h_r_number,h_name,h_number,
    idNumber,cardNum,reCardNum,userName,telephone;
    boolean flag=false;
    OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);
        zgsh= (LinearLayout) findViewById(R.id.zgsh);
        zgsy= (LinearLayout) findViewById(R.id.zgsy);
        h_car_number= (EditText) findViewById(R.id.h_car_number);
        h_r_number= (EditText) findViewById(R.id.h_r_number);
        h_name= (EditText) findViewById(R.id.h_name);
        h_number= (EditText) findViewById(R.id.h_number);
        telephone= (EditText) findViewById(R.id.telephone);
        userName= (EditText) findViewById(R.id.userName);
        reCardNum= (EditText) findViewById(R.id.reCardNum);
        cardNum= (EditText) findViewById(R.id.cardNum);
        idNumber= (EditText) findViewById(R.id.idNumber);
        vYou=  findViewById(R.id.vYou);
        vHua=  findViewById(R.id.vHua);
        findViewById(R.id.bind_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AddCardActivity.this.startActivity(new Intent(AddCardActivity.this,));
                bindCar(flag);
            }
        });
        findViewById(R.id.tv_zgsh).setOnClickListener(this);
        findViewById(R.id.tv_zgsy).setOnClickListener(this);
        findViewById(R.id.out).setOnClickListener(this);

    }

    private void bindCar(boolean flag) {

        Map<String,Object> params = new HashMap<>();
        String url;
        if (flag) {
            params.put("cardNum",h_car_number.getText().toString());
            params.put("reCardNum", h_r_number.getText().toString());
            params.put("userName", h_name.getText().toString());
            params.put("telephone", h_number.getText().toString());
            url=Contants.API.ADD_HCAR;
        }else {
            url=Contants.API.ADD_YCAR;
            params.put("idNumber",idNumber.getText().toString());
            params.put("cardNum",cardNum.getText().toString());
            params.put("reCardNum",reCardNum.getText().toString());
            params.put("userName",userName.getText().toString());
            params.put("telephone",telephone.getText().toString());
        }
        okHttpHelper.post(url, params, new SpotsCallBack<AddCar>(this) {
            @Override
            public void onSuccess(Response response, AddCar o) {
                if (o.getMsg().getCode()==0) {
                    Toast.makeText(AddCardActivity.this, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                    setResult(11);
                    finish();
                }else {
                    Toast.makeText(AddCardActivity.this, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_zgsh:{
                zgsh.setVisibility(View.VISIBLE);
                vHua.setVisibility(View.VISIBLE);
                vYou.setVisibility(View.GONE);
                zgsy.setVisibility(View.GONE);
                flag=true;
            }break;
            case R.id.tv_zgsy:{
                zgsh.setVisibility(View.GONE);
                vHua.setVisibility(View.GONE);
                vYou.setVisibility(View.VISIBLE);
                zgsy.setVisibility(View.VISIBLE);
                flag=false;
            }break;
            case R.id.out:{
                finish();
            }break;

        }
    }
}
