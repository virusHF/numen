package com.fan.numen.service;

import android.app.Service;
import android.content.Intent;

/**
 * Created by zihao.wang on 17/10/27.
 */

public abstract class BaseService extends Service {


    public abstract void onOver();

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        onOver();
    }

    @Override
    public void onDestroy() {
        onOver();
    }
}
