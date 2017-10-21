package my.tlol.com.frighting.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.bean.Banner;
import my.tlol.com.frighting.bean.CarDetail;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SpotsCallBack;

/**
 * Created by tlol20 on 2017/6/22
 */
public class NewCarDetailsActivity extends AppCompatActivity{


    private SliderLayout mSliderLayout;
    private List<Banner> mBanner;
    TextView textView,tv,sPrice,referPrice,
            param,title,content,bPrice;
    ImageView iv;
    String carId;
    OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcar_details);
        carId=getIntent().getStringExtra("carId");
        mSliderLayout= (SliderLayout) findViewById(R.id.slider);
        textView= (TextView) findViewById(R.id.tv_orange);
        bPrice= (TextView) findViewById(R.id.bPrice);
        content= (TextView) findViewById(R.id.content);
        sPrice= (TextView) findViewById(R.id.sPrice);
        referPrice= (TextView) findViewById(R.id.referPrice);
        param= (TextView) findViewById(R.id.param);
        title= (TextView) findViewById(R.id.title);
        tv= (TextView) findViewById(R.id.tv_white);
        iv= (ImageView) findViewById(R.id.iv);
        mSliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        /*
        Banner banner=new Banner();
        banner.setImgUrl("http://o84n5syhk.bkt.clouddn.com/57154327_p0.png");
        banner.setName("第一张图片");
        banner.setDescription("这是第一只图片");
        mBanner.add(banner);
        Banner banner1=new Banner();
        banner1.setName("第二张图片");
        banner1.setDescription("这是第二只图片");
        banner1.setImgUrl("http://o84n5syhk.bkt.clouddn.com/57180221_p0.jpg");
        mBanner.add(banner1);
        initSlider();*/
        getData();
        findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        Map<String,Object> params = new HashMap<>(1);
        params.put("carId", carId);
        okHttpHelper.post(Contants.API.DELETE_CAR, params, new SpotsCallBack<CarDetail>(this) {
            @Override
            public void onSuccess(Response response, CarDetail o) {
                if (o.getMsg().getCode()==0) {
                    bindData(o.getCarDetailVo());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void bindData(CarDetail.CarDetailVoEntity vo) {
        ImageLoader.getInstance().displayImage(vo.getDetailUrl(), iv);
        title.setText(vo.getTitle());
        content.setText(vo.getContent());
        bPrice.setText(vo.getBPrice()+"万");
        param.setText(vo.getProductData().substring(0,4)+"年"+vo.getParam());
        sPrice.setText("新车总价："+vo.getSPrice()+"万（含税）");
        referPrice.setText("参考价格："+vo.getReferPrice()+"万");
        ArrayList<String> li=new ArrayList();
        li.add(vo.getPlayImgUrl1());
        li.add(vo.getPlayImgUrl2());
        li.add(vo.getPlayImgUrl3());
        li.add(vo.getPlayImgUrl4());
        li.add(vo.getPlayImgUrl5());
        List<Banner> Banner=new ArrayList<>();
        mBanner=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Banner banner=new Banner();
            banner.setImgUrl(li.get(i));
            mBanner.add(banner);
        }
        initSlider();

    }

    private void initSlider(){
        if(mBanner !=null){
            tv.setText("/"+mBanner.size());
            for (Banner banner : mBanner){


                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);

            }
        }
        //mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(position % mBanner.size() + 1 + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
    }

}
