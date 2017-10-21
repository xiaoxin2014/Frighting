package my.tlol.com.frighting.application;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.mob.MobApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import my.tlol.com.frighting.bean.User;
import my.tlol.com.frighting.bean.UserLocalData;
import my.tlol.com.frighting.service.LocationService;


public class MyApplication extends MobApplication {

    private User user;
    private static  MyApplication mInstance;
    public static  MyApplication getInstance(){

        return  mInstance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context context=this;
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        /* 初始化ImageLoader */
        initImageLoader(getApplicationContext());

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        /*locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());*/
        SDKInitializer.initialize(this);

        mInstance = this;
        initUser();
    }

    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();//不缓存图片的多种尺寸在内存中
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//将保存的时候的URI名称用MD5
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());// 初始化ImageLoader
    }


    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }


    public User getUser(){

        return user;
    }


    public void putUser(User user){
        this.user = user;
        UserLocalData.putUser(this,user);
        //UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }

    private  Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }

}
