package com.example.soehtet.myapplication;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by soehtet on 18/11/16.
 */

public class MyApplication extends Application{

    private static MyApplication myapplication;

    public static MyApplication getInstance(){
        return myapplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myapplication = this;

    }

}
