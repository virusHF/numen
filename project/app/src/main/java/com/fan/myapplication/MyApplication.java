package com.fan.myapplication;

import android.app.Application;

import com.fan.numen.Numen;

/**
 * Created by zihao.wang on 17/10/27.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Numen.getInstance().init(this, MyService.class);
//        startService(new Intent(this, MyService2.class));
    }
}
