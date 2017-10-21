package my.tlol.com.frighting.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import my.tlol.com.frighting.R;
import my.tlol.com.frighting.activity.ChangePwdActivity;
import my.tlol.com.frighting.activity.InfoActivity;
import my.tlol.com.frighting.activity.LoginActivity;
import my.tlol.com.frighting.activity.MessageListActivity;
import my.tlol.com.frighting.activity.MyCardActivity;
import my.tlol.com.frighting.activity.MyWalletActivity;
import my.tlol.com.frighting.application.MyApplication;
import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.utils.PreferencesUtils;
import my.tlol.com.frighting.view.SelectPicPopupWindow;


public class MineFragment extends BaseFragment implements View.OnClickListener {


    private CircleImageView icon;

    private TextView name, award, share, focusTotal, upvoteByTotal, favoByTotal, currentWhere;
    private String filepath;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_me, container, false);
        takePhoto = getTakePhoto();
        view.findViewById(R.id.info).setOnClickListener(this);
        view.findViewById(R.id.system).setOnClickListener(this);
        view.findViewById(R.id.change_pwd).setOnClickListener(this);
        view.findViewById(R.id.my_order).setOnClickListener(this);
        view.findViewById(R.id.my_car).setOnClickListener(this);
        view.findViewById(R.id.my_money).setOnClickListener(this);


        icon = (CircleImageView) view.findViewById(R.id.icon);
        icon.setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.me_title);
        view.findViewById(R.id.my_order).setOnClickListener(this);
        currentWhere = (TextView) view.findViewById(R.id.currentWhere);
        share = (TextView) view.findViewById(R.id.share);
        award = (TextView) view.findViewById(R.id.award);
        icon.setOnClickListener(this);
        showUser();
        return view;
    }

    private void selectImage() {
        String filepath = "/temp/" + System.currentTimeMillis() + ".jpg";
        File file = new File(Environment.getExternalStorageDirectory(), filepath);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon: {
                if (PreferencesUtils.getBoolean(getActivity(), "isLogin", false)) {
                    //showPopupWindow();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
            break;
            case R.id.pickPhotoBtn: {
                selectImage();
            }
            break;
            case R.id.cancelBtn: {
                mPopWindow.dismiss();
            }
            break;
            case R.id.info: {
                startActivity(new Intent(getActivity(), InfoActivity.class));
            }
            break;
            case R.id.change_pwd: {
                startActivity(new Intent(getActivity(), ChangePwdActivity.class));
            }
            break;
            case R.id.my_order: {
                Toast.makeText(getContext(), "客服正忙", Toast.LENGTH_SHORT).show();
//                if (PreferencesUtils.getBoolean(getActivity(), "isLogin", false)) {
//                    startActivity(new Intent(getActivity(), ServiceActivity.class));
//                } else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
            }
            break;
            case R.id.my_car: {
                if (PreferencesUtils.getBoolean(getActivity(), "isLogin", false)) {
                    startActivity(new Intent(getActivity(), MyCardActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
            break;
            case R.id.my_money: {
                if (PreferencesUtils.getBoolean(getActivity(), "isLogin", false)) {
                    startActivity(new Intent(getActivity(), MyWalletActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
            break;
            case R.id.system: {
                startActivity(new Intent(getActivity(), MessageListActivity.class));
            }
            break;


        }
    }

    @Override
    public void init() {

        showUser();
    }

    private TakePhoto takePhoto;

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        filepath = result.getImage().getCompressPath();
        Glide.with(this).load(new File(filepath)).into(icon);

    }

    private CropOptions getCropOptions() {
        int height = 600;
        int width = 600;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        //builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(true);
        return builder.create();
    }

    //压缩处理
    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 102400;
        int width = 600;
        int height = 600;
        //是否显示进度条
        boolean showProgressBar = false;
        //压缩后是否保存原图
        boolean enableRawFile = true;
        CompressConfig config;
        //使用自带相册
        config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        //不使用自带相册
            /*LubanOptions option=new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config=CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);*/
        takePhoto.onEnableCompress(config, showProgressBar);

    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //使用自带相册
        builder.setWithOwnGallery(true);
        //拍照后矫正角度
        //builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    private void showUser() {

        User user = MyApplication.getInstance().getUser();
        if (user == null) {
            name.setText("未登录");

        } else {
            String s = user.getUserVoLog().getPhotoUrl();
            Log.d("user", s);
            ImageLoader.getInstance().displayImage(s, icon);
            //Glide.with(this).load(new File(s)).into(icon);
            name.setText(user.getUserVoLog().getTelephone() + "");
            currentWhere.setText(user.getUserVoLog().getWtSave() + ".00");
            award.setText(user.getUserVoLog().getAward() + ".00");
            share.setText(user.getUserVoLog().getShare() + ".00");

        }

    }

    /*public void Test(String imagePath){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File f = new File(imagePath);
        if (f != null) {
            builder.addFormDataPart("photo", f.getName(), RequestBody.create(Contants.MEDIA_TYPE_PNG, f));
        }
        OkHttpClient client = new OkHttpClient();
        MultipartBody requestBody = builder.build();

        //构建请求
        Request request = new Request.Builder()
                .addHeader("cookie", PreferencesUtils.getString(getActivity(), "session"))
                .url(Contants.API.CHANGEICON)//地址
                .post(requestBody)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("网络请求", "上传失败" + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String resultValue = response.body().string();
                Log.e("网络请求", "上传照片成功" + resultValue);
                final Ad uploadImg = JSONUtil.fromJson(resultValue, Ad.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (uploadImg.getMsg().getCode() == 0) {
                            Glide.with(getActivity()).load(user.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).crossFade().into(icon);
                            Toast.makeText(getActivity(), uploadImg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                            PreferencesUtils.putString(getActivity(), "photoUrl", uploadImg.getPhotoUrl());
                            mPopWindow.dismiss();
                        } else {
                            Toast.makeText(getActivity(), uploadImg.getMsg().getText(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });

    }*/


    SelectPicPopupWindow mPopWindow;

    private void showPopupWindow() {
        //设置contentView
        mPopWindow = new SelectPicPopupWindow(getActivity(), this, MyApplication.getInstance().getUser().getUserVoLog().getPhotoUrl());
        mPopWindow.showAsDropDown(LayoutInflater.from(getActivity()).inflate(R.layout.activity_me, null));

    }
}
