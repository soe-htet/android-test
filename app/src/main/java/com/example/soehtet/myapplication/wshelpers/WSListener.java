package com.example.soehtet.myapplication.wshelpers;


import android.content.Context;


import java.lang.ref.WeakReference;


public class WSListener {

    private WeakReference<Context> ctx;

    public WSListener(Context ctx){
        this.ctx = new WeakReference<Context>(ctx);
    }

    public Context getContext(){
        return ctx.get();
    }


    public void onWSError(String string, String url) {
        // check maintenance message from S3 when onFailure get called

    }

    public void onLoginSuccess(String str){


    }

    public void onRegisterSuccess(String str){

    }
}
