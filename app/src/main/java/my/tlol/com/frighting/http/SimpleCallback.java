package my.tlol.com.frighting.http;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import my.tlol.com.frighting.R;
import my.tlol.com.frighting.activity.LoginActivity;
import my.tlol.com.frighting.application.MyApplication;



public abstract class SimpleCallback<T> extends BaseCallback<T> {

    protected Context mContext;

    public SimpleCallback(Context context){

        mContext = context;

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        Toast.makeText(mContext, mContext.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyApplication.getInstance().clearUser();

    }


}
