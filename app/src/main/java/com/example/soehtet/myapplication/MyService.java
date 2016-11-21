package com.example.soehtet.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    int value = 0;

    private final IBinder  myBinder = new MyBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return myBinder;
    }

    public class MyBinder extends Binder {

        MyService getService(){
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTimer();

        return START_REDELIVER_INTENT;
    }


    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 2000, 2000);
    }


    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        sendBroadcasting();
                    }
                });
            }
        };
    }


    public void sendBroadcasting(){

        Intent intent = new Intent("GETVALUE");
        value = value + 10;
        intent.putExtra("value",value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
