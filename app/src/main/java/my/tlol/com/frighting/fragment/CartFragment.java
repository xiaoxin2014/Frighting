package my.tlol.com.frighting.fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.activity.LoginActivity;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.CarSpinner;
import my.tlol.com.frighting.bean.CartF;
import my.tlol.com.frighting.bean.SGetCode;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;
import my.tlol.com.frighting.http.SpotsCallBack;
import my.tlol.com.frighting.utils.JSONUtil;
import my.tlol.com.frighting.utils.PreferencesUtils;


public class CartFragment extends BaseFragment{

    private Spinner provinceSpinner,citySpinner,countySpinner;
    private ArrayAdapter<String> prov_adapter;
    private ArrayAdapter<String> city_adapter;
    private ArrayAdapter<String> countyAdapter;
    private CarSpinner carSpinner;
    OkHttpHelper ok=OkHttpHelper.getInstance();
    private String[] province;
    EditText lsnum,frameno,engineno,imgYzm
            ;
    ImageView tv;
    int imgYzmId;
    private SpotsDialog mDialog;
    //private ArrayAdapter<DiQu> diQu_adapter;
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_query,container,false);
        //第一步，得到容器视图
        provinceSpinner = (Spinner) view.findViewById(R.id.sp_pro);
        citySpinner = (Spinner) view.findViewById(R.id.sp_city);
        countySpinner = (Spinner) view.findViewById(R.id.sp_diQu);
        lsnum = (EditText) view.findViewById(R.id.lsnum);
        frameno = (EditText) view.findViewById(R.id.frameno);
        engineno = (EditText) view.findViewById(R.id.engineno);
        imgYzm = (EditText) view.findViewById(R.id.imgYzm);
        tv = (ImageView) view.findViewById(R.id.code);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });
        view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
        carSpinner = JSONUtil.fromJson(s,CarSpinner.class);
        setSpinner();
        getCode();
        return view;
    }

    private void commit() {
        Map<String,Object> params = new HashMap<>();

        params.put("carorg",carSpinner.getResult().getData().get(provinceSpinner.getSelectedItemPosition()).getCarorg());
        params.put("lsprefix", countySpinner.getSelectedItem().toString());
        params.put("lsnum",lsnum.getText().toString().trim());
        params.put("frameno",frameno.getText().toString().trim());
        params.put("engineno",engineno.getText().toString().trim());
        params.put("imgYzm",imgYzm.getText().toString().trim());
        params.put("imgYzmId", imgYzmId);
        Log.d("commit",params.toString());
        ok.post(Contants.API.SEARCH, params, new SpotsCallBack<CartF>(getActivity()) {
            @Override
            public void onSuccess(Response response, CartF o) {
                if (o.getMsg().getCode()==0) {
                    mDialog=new SpotsDialog(getActivity(),o.getResult().getLsprefix()+o.getResult().getLsnum()
                            +"\n于"+o.getResult().getList().getTime()
                            +"\n在"+o.getResult().getList().getAddress()
                            +"发生"+o.getResult().getList().getContent());
                    mDialog.show();
                }else {
                    if (o.getMsg().getCode()==101) {
                        if (PreferencesUtils.getBoolean(getActivity(),"isLogin",false)) {
                            login(PreferencesUtils.getString(MyApplication.getInstance().getContext(),"telephone"),PreferencesUtils.getString(MyApplication.getInstance().getContext(),"password"));
                        }else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
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
        ok.post(Contants.API.LOGIN, params, new SimpleCallback<User>(getActivity()) {

            @Override
            public void onSuccess(Response response, User userLoginRespMsg) {
                //result={"Msg":{"code":11004010,"text":"手机号格式错误"}}
                if (userLoginRespMsg.getMsg().getCode() == 0) {

                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    if (cookies.size()>0) {
                        String session = cookies.get(0);
                        Log.e("login", "session" + session.substring(0, session.indexOf(";")));
                        PreferencesUtils.putString(getActivity(), "session", session.substring(0, session.indexOf(";")));
                    }
                    PreferencesUtils.putBoolean(getActivity(), "isLogin", true);
                    MyApplication application = MyApplication.getInstance();
                    application.putUser(userLoginRespMsg);
                } else {
                    Toast.makeText(getActivity(), "登入失败！" + userLoginRespMsg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getActivity(), "error:"+code, Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void getCode() {
        Log.d("commit",carSpinner.getResult().getData().get(provinceSpinner.getSelectedItemPosition()).getCarorg());
        ok.get(Contants.API.GET_S_CODE, new SimpleCallback<SGetCode>(getActivity()) {
            @Override
            public void onSuccess(Response response, SGetCode o) {
                if (o.getCode()==0) {
                    imgYzmId=o.getRtnImgYzm().getId();
                    tv.setImageBitmap(base64ToBitmap(o.getRtnImgYzm().getImgBase64()));
                }else {
                    Toast.makeText(mContext, o.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public  Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    /*
     * 设置下拉框
     */
        private void setSpinner()
        {
                /*projectSpinner=getSharedPreferences("projectSpinner", Context.MODE_PRIVATE);
                provinceSpinner = (Spinner)findViewById(R.id.spin_province);
                citySpinner = (Spinner)findViewById(R.id.spin_city);
                countySpinner = (Spinner)findViewById(R.id.spin_county);*/
            province = new String[carSpinner.getResult().getData().size()];
             final List<CarSpinner.ResultEntity.DataEntity> city = new ArrayList<>();
            for (int i = 0; i < carSpinner.getResult().getData().size(); i++) {
                province[i]=carSpinner.getResult().getData().get(i).getProvince();
                city.add(carSpinner.getResult().getData().get(i));
            }
                //绑定适配器和值
                prov_adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, province);
                provinceSpinner.setAdapter(prov_adapter);
                provinceSpinner.setSelection(4,true);  //设置默认选中项，此处为默认选中第5个值

             String[] newCity = new String[city.get(4).getList().size()];
             String[] county = new String[1];
                county[0]=city.get(4).getLsprefix();
            for (int i = 0; i < city.get(4).getList().size(); i++) {
                newCity[i]=city.get(4).getList().get(i).getCity();
            }
                city_adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, newCity);
                citySpinner.setAdapter(city_adapter);
                citySpinner.setSelection(0,true);  //默认选中第0个

                countyAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, county);
                countySpinner.setAdapter(countyAdapter);
                countySpinner.setSelection(0, true);
                //省级下拉框监听
            provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                            List<CarSpinner.ResultEntity.DataEntity.ListEntity> list=city.get(position).getList();
                            if (list!=null){
                                String[] newCity = new String[list.size()];
                                //String[] county = new String[list.size()];
                                for (int i = 0; i < list.size(); i++) {
                                    newCity[i]=list.get(i).getCity();
                                    //county[i]=list.get(i).getLsprefix()+city.get(4).getList().get(i).getLsnum();
                                }
                                city_adapter = new ArrayAdapter<>(getActivity(),
                                        android.R.layout.simple_spinner_item, newCity);
                                citySpinner.setAdapter(city_adapter);
                                citySpinner.setSelection(0,true);  //默认选中第0个

                                /*countyAdapter = new ArrayAdapter<>(getActivity(),
                                        android.R.layout.simple_spinner_item, county);
                                countySpinner.setAdapter(countyAdapter);
                                countySpinner.setSelection(0, true);*/
                            }else {
                                String[] newCity = new String[1];

                                newCity[0]=city.get(position).getLsnum();

                                city_adapter = new ArrayAdapter<>(getActivity(),
                                        android.R.layout.simple_spinner_item, newCity);
                                citySpinner.setAdapter(city_adapter);
                                citySpinner.setSelection(0,true);  //默认选中第0个


                            }
                            String[] county = new String[1];
                            county[0]=city.get(position).getLsprefix();
                            countyAdapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_spinner_item, county);
                            countySpinner.setAdapter(countyAdapter);

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                });

                /*//地级下拉监听
                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int position, long arg3)
                        {
                            countySpinner.setSelection(position, true);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0)
                        {

                        }
                });*/
        }
    @Override
    public void init() {

    }



    String s="{\n" +
            "status: \"0\",\n" +
            "msg: \"ok\",\n" +
            "result: {\n" +
            "data: [\n" +
            "{\n" +
            "province: \"北京\",\n" +
            "lsprefix: \"京\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"beijing\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "province: \"安徽\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"anhui\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"安庆\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"anqing\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"蚌埠\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"bengbu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"池州\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"chizhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"滁州\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"chuzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阜阳\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"fuyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"淮北\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"huaibei\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"淮南\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"huainan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黄山\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"huangshan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"六安\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"luan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"马鞍山\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"maanshan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宿州\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"suzhou2\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"铜陵\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"tongling\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"芜湖\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"wuhu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宣城\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"xuancheng\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"亳州\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"S\",\n" +
            "carorg: \"bozhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"合肥\",\n" +
            "lsprefix: \"皖\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"hefei\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"福建\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"fujian\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"福州\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"fuzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"龙岩\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"longyan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"南平\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"nanping\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宁德\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"ningde\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"莆田\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"putian\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"泉州\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"quanzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"三明\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"sanming\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"厦门\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"xiamen\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"漳州\",\n" +
            "lsprefix: \"闽\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"zhangzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"甘肃\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"gansu\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"兰州\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"lanzhou\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"白银\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"baiyin\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"定西\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"dingxi\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"甘南州\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"gannan\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"嘉峪关\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"jiayuguan\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"金昌\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"jinchang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"酒泉\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"jiuquan\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"临夏\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"linxia\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"陇南\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"longnan\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"平凉\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"pingliang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"庆阳\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"qingyang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"天水\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"tianshui\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"武威\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"wuwei\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"张掖\",\n" +
            "lsprefix: \"甘\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"zhangye\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"广东\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"guangdong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"广州\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"guangzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"深圳\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"shenzhen\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"潮州\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"U\",\n" +
            "carorg: \"chaozhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"东莞\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"S\",\n" +
            "carorg: \"dongguan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"佛山\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"fushan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"河源\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"heyuan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"惠州\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"huizhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"江门\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"jiangmen\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"揭阳\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"V\",\n" +
            "carorg: \"jieyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"茂名\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"maoming\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"梅州\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"meizhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"清远\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"qingyuan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"汕头\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"shantou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"汕尾\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"shanwei\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"韶关\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"shaoguan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阳江\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"yangjiang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"云浮\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"W\",\n" +
            "carorg: \"yunfu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"湛江\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"zhanjiang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"肇庆\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"zhaoqing\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"中山\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"T\",\n" +
            "carorg: \"zhongshan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"珠海\",\n" +
            "lsprefix: \"粤\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"zhuhai\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"广西\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"南宁\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"nanning\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"桂林\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"百色\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"北海\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"崇左\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"防城港\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"贵港\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"河池\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"贺州\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"hezhou\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"来宾\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"柳州\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"钦州\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"梧州\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"玉林\",\n" +
            "lsprefix: \"桂\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"贵州\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"guizhou\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"贵阳\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"guiyang\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"安顺\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"anshun\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"毕节\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"bijie\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"六盘水\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"liupanshui\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黔东南\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"qiandongnan\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黔南\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"qiannan\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黔西南\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"qianxinan\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"铜仁\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"tongren\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"遵义\",\n" +
            "lsprefix: \"贵\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"zunyi\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"海南\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"hainan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"海口\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"haikou\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"三亚\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"sanya\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"白沙县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"baisha\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"保亭县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"baoting\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"昌江县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"changjiang\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"澄迈县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"chengmai\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"定安县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"dingan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"东方\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"dongfang\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"乐东县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"ledong\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"临高县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"lingao\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"陵水县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"lingshui\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"琼海\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"qionghai\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"琼中\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"qiongzhong\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"屯昌县\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"tunchang\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"万宁\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"wanning\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"文昌\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"wenchang\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"五指山\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"wuzhishan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"儋州\",\n" +
            "lsprefix: \"琼\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"danzhou\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"三沙\",\n" +
            "lsprefix: \"\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"河北\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"hebei\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"石家庄\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"shijiazhuang\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"保定\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"baoding\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"沧州\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"cangzhou\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"承德\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"chengde\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"邯郸\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"handan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"衡水\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"T\",\n" +
            "carorg: \"hengshui\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"廊坊\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"langfang\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"秦皇岛\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"qinhuangdao\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"唐山\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"tangshan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"邢台\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"xingtai\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"张家口\",\n" +
            "lsprefix: \"冀\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"zhangjiakou\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"河南\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"henan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"郑州\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"zhengzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"洛阳\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"luoyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"开封\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"kaifeng\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"安阳\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"anyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"鹤壁\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"hebi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"济源\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"U\",\n" +
            "carorg: \"jiyuan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"焦作\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"jiaozuo\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"南阳\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"nanyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"平顶山\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"pingdingshan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"三门峡\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"sanmenxia\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"商丘\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"shangqiu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"新乡\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"xinxiang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"信阳\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"S\",\n" +
            "carorg: \"xinyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"许昌\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"xuchang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"周口\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"zhoukou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"驻马店\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"zhumadian\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"漯河\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"luohe\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"濮阳\",\n" +
            "lsprefix: \"豫\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"puyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"黑龙江\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"heilongjiang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"哈尔滨\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"haerbin\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"大庆\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"daqing\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"大兴安岭\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"daxinganling\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"鹤岗\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"hegang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黑河\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"heihe\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"鸡西\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"jixi\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"佳木斯\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"jiamusi\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"牡丹江\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"mudanjiang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"七台河\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"qitaihe\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"齐齐哈尔\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"qiqihaer\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"双鸭山\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"shuangyashan\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"绥化\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"suihua\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"伊春\",\n" +
            "lsprefix: \"黑\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"yichun2\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"湖北\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"hubei\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"武汉\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"wuhan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"仙桃\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"xiantao\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"鄂州\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"ezhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黄冈\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"huanggang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黄石\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"huangshi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"荆门\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"jingmen\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"荆州\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"jingzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"潜江\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"qianjiang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"神农架林区\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"shennongjia\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"十堰\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"shiyan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"随州\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"S\",\n" +
            "carorg: \"suizhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"天门\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"tianmen\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"咸宁\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"xianning\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"襄阳\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"xiangyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"孝感\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"xiaogan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宜昌\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"yichang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"恩施\",\n" +
            "lsprefix: \"鄂\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"enshi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"湖南\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"长沙\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"changsha\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"5\"\n" +
            "},\n" +
            "{\n" +
            "city: \"张家界\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"常德\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"郴州\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"衡阳\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"怀化\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"娄底\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"邵阳\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"湘潭\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"湘西\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"U\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"益阳\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"永州\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"岳阳\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"株洲\",\n" +
            "lsprefix: \"湘\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"zhuzhou\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"吉林\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"jilin\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"长春\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"changchun\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"吉林市\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"jilin2\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"白城\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"baicheng\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"白山\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"baishan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"辽源\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"liaoyuan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"四平\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"siping\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"松原\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"songyuan\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"通化\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"tonghua\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"延边\",\n" +
            "lsprefix: \"吉\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"yanbian\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"江苏\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"jiangsu\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"南京\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"nanjing\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"苏州\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"suzhou\",\n" +
            "frameno: \"7\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"无锡\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"wuxi2\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"常州\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"changzhou\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"淮安\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"huaian\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"连云港\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"lianyungang\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"南通\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"nantong\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宿迁\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"suqian\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"泰州\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"taizhou2\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"徐州\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"xuzhou\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"盐城\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"yancheng\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"扬州\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"yangzhou\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"镇江\",\n" +
            "lsprefix: \"苏\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"zhenjiang\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"江西\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"南昌\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"A,M\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"抚州\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"赣州\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"吉安\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"景德镇\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"九江\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"萍乡\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"上饶\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"新余\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宜春\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"鹰潭\",\n" +
            "lsprefix: \"赣\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"辽宁\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"沈阳\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"shenyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"大连\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"dalian\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"鞍山\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"anshan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"本溪\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"benxi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"朝阳\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"chaoyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"丹东\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"dandong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"抚顺\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阜新\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"fuxin\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"葫芦岛\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"huludao\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"锦州\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"jinzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"辽阳\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"liaoyang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"盘锦\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"panjin\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"铁岭\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"\",\n" +
            "frameno: \"\",\n" +
            "engineno: \"\"\n" +
            "},\n" +
            "{\n" +
            "city: \"营口\",\n" +
            "lsprefix: \"辽\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"yingkou\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"内蒙古\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"neimenggu\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"呼和浩特\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"huhehaote\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阿拉善盟\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"alashan\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"巴彦淖尔\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"bayan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"包头\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"baotou\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"赤峰\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"chifeng\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"鄂尔多斯\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"eerduosi\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"呼伦贝尔\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"hulunbeier\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"通辽\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"tongliao\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"乌海\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"wuhai\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"乌兰察布\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"wulanchabu\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"锡林郭勒\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"xilinguole\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"兴安盟\",\n" +
            "lsprefix: \"蒙\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"xinganmeng\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"宁夏\",\n" +
            "lsprefix: \"宁\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"ningxia\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"银川\",\n" +
            "lsprefix: \"宁\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"yinchuan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"固原\",\n" +
            "lsprefix: \"宁\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"guyuan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"石嘴山\",\n" +
            "lsprefix: \"宁\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"shizuishan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"吴忠\",\n" +
            "lsprefix: \"宁\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"wuzhong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"中卫\",\n" +
            "lsprefix: \"宁\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"zhongwei\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"青海\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"qinghai\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"西宁\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"xining\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"果洛\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"guoluo\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"海北\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"haibei\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"海东\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"haidong\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"海南州\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"hainan2\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"海西\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"haixi\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"黄南\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"huangnan\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"玉树\",\n" +
            "lsprefix: \"青\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"yushu\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"山东\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"shandong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"济南\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"jinan\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"青岛\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"B,U\",\n" +
            "carorg: \"qingdao\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"滨州\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"binzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"德州\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"dezhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"东营\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"dongying\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"菏泽\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"heze\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"济宁\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"jining\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"莱芜\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"S\",\n" +
            "carorg: \"laiwu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"聊城\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"liaocheng\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"临沂\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"linyi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"日照\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"rizhao\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"泰安\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"taian\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"威海\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"weihai\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "},\n" +
            "{\n" +
            "city: \"潍坊\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"G,V\",\n" +
            "carorg: \"weifang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"烟台\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"F,Y\",\n" +
            "carorg: \"yantai\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"枣庄\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"zaozhuang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"淄博\",\n" +
            "lsprefix: \"鲁\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"zibo\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"4\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"山西\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"shanxi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"太原\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"taiyuan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"长治\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"changzhi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"大同\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"datong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"晋城\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"jincheng\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"晋中\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"jinzhong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"临汾\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"linfen\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"吕梁\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"lvliang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"朔州\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"shuozhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"忻州\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"xinzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阳泉\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"yangquan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"运城\",\n" +
            "lsprefix: \"晋\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"yuncheng\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"陕西\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"shannxi\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"西安\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"xian\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"安康\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"ankang\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宝鸡\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"baoji\",\n" +
            "frameno: \"4\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"汉中\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"hanzhong\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"商洛\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"shangluo\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"铜川\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"tongchuan\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"渭南\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"weinan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"咸阳\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"xianyang\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"延安\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"yanan\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"榆林\",\n" +
            "lsprefix: \"陕\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"yulin2\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"上海\",\n" +
            "lsprefix: \"沪\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"shanghai\",\n" +
            "frameno: \"0\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "province: \"四川\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"sichuan\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"成都\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"chengdu\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"绵阳\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"mianyang\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阿坝\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"U\",\n" +
            "carorg: \"aba\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"巴中\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"Y\",\n" +
            "carorg: \"bazhong\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"达州\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"S\",\n" +
            "carorg: \"dazhou\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"德阳\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"deyang\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"甘孜\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"V\",\n" +
            "carorg: \"ganzi\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"广安\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"X\",\n" +
            "carorg: \"guangan\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"广元\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"guangyuan\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"乐山\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"leshan\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"凉山\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"W\",\n" +
            "carorg: \"liangshan\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"眉山\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"Z\",\n" +
            "carorg: \"meishan\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"南充\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"nanchong\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"内江\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"najiang\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"攀枝花\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"panzhihua\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"遂宁\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"suining\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"雅安\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"T\",\n" +
            "carorg: \"yaan\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宜宾\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"yibin\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"资阳\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"ziyang\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"自贡\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"zigong\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"泸州\",\n" +
            "lsprefix: \"川\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"luzhou\",\n" +
            "frameno: \"8\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"天津\",\n" +
            "lsprefix: \"津\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"tianjin\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "province: \"西藏\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"xizang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"拉萨\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"lasa\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阿里\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"ali\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"昌都\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"changdu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"林芝\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"linzhi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"那曲\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"naqu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"日喀则\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"rikaze\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"山南\",\n" +
            "lsprefix: \"藏\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"shannan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"新疆\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"xinjiang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"乌鲁木齐\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"wulumuqi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阿克苏\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"akesu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"阿拉尔\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"alaer\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"巴音郭楞\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"bayin\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"博尔塔拉\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"boertala\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"昌吉\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"changji\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"哈密\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"hami\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"和田\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"hetian\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"喀什\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"kashi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"克拉玛依\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"kelamayi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"克孜勒苏\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"kezilesu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"石河子\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"shihezi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"图木舒克\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"tumushuke\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"吐鲁番\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"tulufan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"五家渠\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"wujiaqu\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"伊犁\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"yili\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"北屯\",\n" +
            "lsprefix: \"新\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"beitun\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"云南\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"yunnan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"昆明\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"kunming\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"怒江\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"Q\",\n" +
            "carorg: \"nujiang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"普洱\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"puer\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"丽江\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"P\",\n" +
            "carorg: \"lijiang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"保山\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"M\",\n" +
            "carorg: \"baoshan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"楚雄\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"chuxiong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"大理\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"dali\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"德宏\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"N\",\n" +
            "carorg: \"dehong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"迪庆\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"R\",\n" +
            "carorg: \"diqing\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"红河\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"honghe\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"临沧\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"S\",\n" +
            "carorg: \"lincang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"曲靖\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"qujing\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"文山\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"wenshan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"西双版纳\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"xishuangbanna\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"玉溪\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"yuxi\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "},\n" +
            "{\n" +
            "city: \"昭通\",\n" +
            "lsprefix: \"云\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"zhaotong\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"6\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"浙江\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"zhejiang\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\",\n" +
            "list: [\n" +
            "{\n" +
            "city: \"杭州\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"A\",\n" +
            "carorg: \"hangzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"湖州\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"E\",\n" +
            "carorg: \"huzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"嘉兴\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"F\",\n" +
            "carorg: \"jiaxing\",\n" +
            "frameno: \"100\",\n" +
            "engineno: \"100\"\n" +
            "},\n" +
            "{\n" +
            "city: \"金华\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"G\",\n" +
            "carorg: \"jinhua\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"丽水\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"K\",\n" +
            "carorg: \"lishui\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"宁波\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"B\",\n" +
            "carorg: \"ningbo\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"绍兴\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"D\",\n" +
            "carorg: \"shaoxing\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"台州\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"J\",\n" +
            "carorg: \"taizhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"温州\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"C\",\n" +
            "carorg: \"wenzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"舟山\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"L\",\n" +
            "carorg: \"zhoushan\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "},\n" +
            "{\n" +
            "city: \"衢州\",\n" +
            "lsprefix: \"浙\",\n" +
            "lsnum: \"H\",\n" +
            "carorg: \"quzhou\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "province: \"重庆\",\n" +
            "lsprefix: \"渝\",\n" +
            "lsnum: \"\",\n" +
            "carorg: \"chongqing\",\n" +
            "frameno: \"6\",\n" +
            "engineno: \"0\"\n" +
            "}\n" +
            "],\n" +
            "updatetime: \"2016-09-20\",\n" +
            "updatemsg: \"接入直连通道，交管局参数变化较大，多为增加参数。云南、海南、河南、安徽等交管局，可按原有参数查询，不受影响。\"\n" +
            "}\n" +
            "}";
}
