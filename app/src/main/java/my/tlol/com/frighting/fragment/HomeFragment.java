package my.tlol.com.frighting.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import my.tlol.com.frighting.Contants;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.activity.DataActivity;
import my.tlol.com.frighting.activity.JiayouActivity;
import my.tlol.com.frighting.activity.LoginActivity;
import my.tlol.com.frighting.activity.MessageListActivity;
import my.tlol.com.frighting.activity.MyCardActivity;
import my.tlol.com.frighting.activity.MyWalletActivity;
import my.tlol.com.frighting.activity.NewCarActivity;
import my.tlol.com.frighting.activity.ServiceActivity;
import my.tlol.com.frighting.activity.StationActivity;
import my.tlol.com.frighting.activity.SystemActivity;
import my.tlol.com.frighting.activity.TaocanActivity;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.Banner;
import my.tlol.com.frighting.bean.Message;
import my.tlol.com.frighting.bean.Msg;
import my.tlol.com.frighting.bean.MyBanner;
import my.tlol.com.frighting.bean.Price;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.bean.WxShare;
import my.tlol.com.frighting.http.OkHttpHelper;
import my.tlol.com.frighting.http.SimpleCallback;
import my.tlol.com.frighting.utils.PreferencesUtils;
import my.tlol.com.frighting.widget.LooperTextView;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    /*public ImageCycleView mImage;
    private TextView fenlei;
    private GridView gridView;
    private DisplayImageOptions options;
    private String[] mTabTitles = new String[]{};
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    */
    private static final String APP_ID = "wxcd0959cc13aa4005";
    //  一  wx86f0a5e70ce7fc78
//  二  wxcd0959cc13aa4005
    private static final String TAG = "HomeFragment";
    /*private IWXAPI api;*/
    private SliderLayout mSliderLayout;
    private List<Banner> mBanner;
    private LooperTextView daysMessage;
    OkHttpHelper ok = OkHttpHelper.getInstance();
    TextView tv92, tv95, tv98;
    private String shareTitle;
    private String shareContent;
    private String shareUrl;
    private String text;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.new_car).setOnClickListener(this);
        view.findViewById(R.id.data).setOnClickListener(this);
        view.findViewById(R.id.station).setOnClickListener(this);
        view.findViewById(R.id.share).setOnClickListener(this);
        view.findViewById(R.id.taocan).setOnClickListener(this);
        view.findViewById(R.id.my_card).setOnClickListener(this);
        view.findViewById(R.id.jiayou).setOnClickListener(this);
        view.findViewById(R.id.service).setOnClickListener(this);
        daysMessage = (LooperTextView) view.findViewById(R.id.daysMessage);
        daysMessage.setOnClickListener(this);
        tv92 = (TextView) view.findViewById(R.id.tv_92);
        tv95 = (TextView) view.findViewById(R.id.tv_95);
        tv98 = (TextView) view.findViewById(R.id.tv_98);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        /*Banner banner=new Banner();
        banner.setImgUrl("http://o84n5syhk.bkt.clouddn.com/57154327_p0.png");
        banner.setName("第一张图片");
        banner.setDescription("这是第一只图片");
        mBanner.add(banner);
        Banner banner1=new Banner();
        banner1.setName("第二张图片");
        banner1.setDescription("这是第二只图片");
        banner1.setImgUrl("http://o84n5syhk.bkt.clouddn.com/57180221_p0.jpg");
        mBanner.add(banner1);*/
        getDaysMessage();
        return view;
    }

    private void getDaysMessage() {
        ok.get(Contants.API.MESSAGE, new SimpleCallback<Message>(getActivity()) {
            @Override
            public void onSuccess(Response response, Message o) {
                if (o.getMsg().getCode() == 0) {
                    List<String> list = new ArrayList<>();
                    for (Message.DayMesVoEntity entity : o.getDayMesVo()) {
                        list.add(entity.getDaysMessage());
                    }
                    //daysMessage.setText(o.getMsg().get(0).getDaysMessage());
                    daysMessage.setTipList(list);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
        ok.get(Contants.API.PRICE, new SimpleCallback<Price>(getActivity()) {
            @Override
            public void onSuccess(Response response, Price o) {
                if (o.getMsg().getCode() == 0) {
                    for (int i = 0; i < o.getEveryOil().size(); i++) {
                        switch (i) {
                            case 0: {
                                tv98.setText("" + o.getEveryOil().get(i).getPrice() + "");
                            }
                            break;
                            case 1: {
                                tv95.setText("" + o.getEveryOil().get(i).getPrice() + "");
                            }
                            break;
                            case 2: {
                                tv92.setText("" + o.getEveryOil().get(i).getPrice() + "");
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
        ok.get(Contants.API.BANNER, new SimpleCallback<MyBanner>(getActivity()) {
            @Override
            public void onSuccess(Response response, MyBanner o) {
                if (o.getMsg().getCode() == 0) {
                    if (o.getMainImgVo().size() > 0) {
                        mBanner = new ArrayList<>();
                        for (MyBanner.MainImgVoEntity imgVoEntity : o.getMainImgVo()) {
                            String name = imgVoEntity.getHerfUrl() + "|" + imgVoEntity.getPImagName();
                            Banner banner = new Banner();
                            banner.setImgUrl(imgVoEntity.getPImgUrl());
                            banner.setName(imgVoEntity.getPImagName());
                            banner.setDescription(imgVoEntity.getHerfUrl());
                            banner.setImgId(imgVoEntity.getPImgId());
                            mBanner.add(banner);
                        }
                        initSlider();
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_car: {
                if (!PreferencesUtils.getBoolean(getActivity(), "isLogin")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    return;
                }
                startActivity(new Intent(getActivity(), NewCarActivity.class));
            }
            break;
            case R.id.data: {
                startActivity(new Intent(getActivity(), DataActivity.class));
            }
            break;
            case R.id.station: {
                startActivity(new Intent(getActivity(), StationActivity.class));
            }
            break;
            case R.id.share: {
                //startActivity(new Intent(getActivity(), ShareActivity.class));
                getData();
//                分享填写分享的内容
//                WXTextObject textObject = new WXTextObject();
//                textObject.text = text;
////                用WXTextObject初始化 WXMediaMessage对象
//                WXMediaMessage msg = new WXMediaMessage();
//                msg.mediaObject = textObject;
//                msg.description = text;
//
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction =
//                req.message = msg;
//                req.scene =
//                SendMessageToWX.Req.WXSceneFavorite;
//                SendMessageToWX.Req.WXSceneSession;


            }
            break;
            case R.id.taocan: {
                startActivity(new Intent(getActivity(), TaocanActivity.class));
            }
            break;
            case R.id.my_card: {
                startActivity(new Intent(getActivity(), MyCardActivity.class));
            }
            break;
            case R.id.jiayou: {
                startActivity(new Intent(getActivity(), JiayouActivity.class));
            }
            break;
            case R.id.service: {
                startActivity(new Intent(getActivity(), ServiceActivity.class));
            }
            break;
            case R.id.daysMessage: {
                startActivity(new Intent(getActivity(), MessageListActivity.class));
            }
            break;
        }
    }

    private void login(String telephone, String password) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("telephone", telephone);
        params.put("password", password);
        PreferencesUtils.putBoolean(getActivity(), "isLogin", false);
        ok.post(Contants.API.LOGIN, params, new SimpleCallback<User>(getActivity()) {

            @Override
            public void onSuccess(Response response, User userLoginRespMsg) {
                //result={"Msg":{"code":11004010,"text":"手机号格式错误"}}
                if (userLoginRespMsg.getMsg().getCode() == 0) {

                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    if (cookies.size() > 0) {
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
                Toast.makeText(mContext, "error:" + code, Toast.LENGTH_SHORT).show();
            }

        });
    }

    //点击了会员分享
    private void getData() {
        if (!PreferencesUtils.getBoolean(getActivity(), "isLogin")) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return;
        }
        ok.get(Contants.API.WXSHARE, new SimpleCallback<WxShare>(getActivity()) {
            @Override
            public void onSuccess(Response response, WxShare o) {
                if (o.getMsg().getCode() == 0) {
                    Toast.makeText(getContext(), "点击了微信分享2", Toast.LENGTH_SHORT).show();

                    shareContent = o.getWxShareVo().getWxContent();
                    shareTitle = o.getWxShareVo().getWxTitle();
                    shareUrl = o.getWxShareVo().getWxUrl();
                    showShare();
                } else {
                    if (o.getMsg().getCode() == 101) {
                        if (PreferencesUtils.getBoolean(mContext, "isLogin", false)) {
                            login(PreferencesUtils.getString(MyApplication.getInstance().getContext(), "telephone"), PreferencesUtils.getString(MyApplication.getInstance().getContext(), "password"));
                        } else {
                            Toast.makeText(mContext, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    } else {
                        Toast.makeText(mContext, o.getMsg().getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.d("tzj", response.message() + code + e.toString() + "------");
            }
        });
    }

    private void showShare() {
        Toast.makeText(getContext(), "点击了微信分享", Toast.LENGTH_SHORT).show();
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(getActivity(), "分享成功！", Toast.LENGTH_SHORT).show();
                ok.get(Contants.API.SHARE, new SimpleCallback<Msg>(getActivity()) {
                    @Override
                    public void onSuccess(Response response, Msg o) {
                        showDialog();
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {
                        Log.d("tzj", response.message() + code + e.toString() + "=====");
                    }
                });

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(getActivity(), "分享错误！", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(getActivity(), "分享取消！", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getActivity(), ShareActivity.class));
            }
        });
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        // text是分享文本，所有平台都需要这个字段
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setTitle(shareTitle);
        oks.setText(shareContent);
        oks.setImageUrl(shareUrl);
        oks.setUrl("http://www.shenzhenzhixuan.com/");
        oks.setTitleUrl("http://www.shenzhenzhixuan.com/");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://www.shenzhenzhixuan.com/");

        // 启动分享GUI
        oks.show(getActivity());
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_dialog);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(getActivity(), R.layout.dailog_get_red, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        ImageView btnOK = (ImageView) view.findViewById(R.id.red);
        ImageView btnCancel = (ImageView) view.findViewById(R.id.delete);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyWalletActivity.class);
                i.putExtra("position", 1);
                startActivity(i);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }

    @Override
    public void init() {
        //initSlider();
    }


    private void initSlider() {
        if (mBanner != null && mBanner.size() > 0) {
            for (Banner banner : mBanner) {

                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Log.d("initSlider", "onSliderClick: " + mSliderLayout.getCurrentPosition());
                        Intent intent = new Intent(getActivity(), SystemActivity.class);
                        intent.putExtra("h5", mBanner.get(mSliderLayout.getCurrentPosition()).getDescription());
                        startActivity(intent);
                    }
                });
                mSliderLayout.addSlider(textSliderView);

            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }

}
