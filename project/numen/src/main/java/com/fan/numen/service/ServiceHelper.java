package com.fan.numen.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.fan.numen.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zihao.wang on 17/10/27.
 */

public class ServiceHelper {
//    static final int WAKE_UP_INTERVAL = 10 * 60 * 1000;
    static final int WAKE_UP_INTERVAL = 5000;

    private static Map<Class<? extends Service>, ServiceConnection> sConnections = new HashMap<>();


    public static void startService(final Context context, final Class<? extends Service> serviceClass) {
        final Intent intent = new Intent(context, serviceClass);
        final ServiceConnection connection = sConnections.get(serviceClass);
        if (connection == null) {
            startService(context, intent);
            context.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Logger.i(serviceClass.getSimpleName() + " onServiceConnected");
                    sConnections.put(serviceClass, this);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Logger.i(serviceClass.getSimpleName() + " onServiceDisconnected");
                    sConnections.remove(serviceClass);
                    startService(context, intent);
                    context.bindService(intent, this, Context.BIND_AUTO_CREATE);
                }
            }, Context.BIND_AUTO_CREATE);
        }
    }


    static void startService(Context context, Intent intent) {
        try {
            context.startService(intent);
        } catch (Exception ignored) {

        }
    }
}
