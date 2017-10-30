package com.fan.numen.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fan.numen.Numen;
import com.fan.numen.utils.Logger;

/**
 * Created by zihao.wang on 17/10/27.
 */

public abstract class BaseWorkService extends BaseService {
    private final IBinder binder = new MyBinder();

    protected static final int NOTIFICATION_ID = 5450;
    protected static final int JOB_ID = 5652;

    protected boolean mRunning = false;

    protected abstract void work();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("work service onStartCommand");


        if (!mRunning) {
            mRunning = true;

            //启动守护进程
            ServiceHelper.startService(Numen.getInstance().getContext(), DaemonService.class);

            //后台任务执行
            Logger.i("work service start work");
            work();

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                startForeground(NOTIFICATION_ID, new Notification());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    ServiceHelper.startService(Numen.getInstance().getContext(), new Intent(Numen.getInstance().getContext(), NotificationService.class));
                }
            }

            getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(), DaemonService.class.getName()),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        }

        return START_STICKY;
    }


    @Override
    public void onOver() {
        Logger.i("work service onOver");
        Context context = Numen.getInstance().getContext();
        Class<? extends BaseWorkService> workService = Numen.getInstance().getWorkService();
        ServiceHelper.startService(context, workService);
        ServiceHelper.startService(context, DaemonService.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.i("work service onBind");
        return binder;
    }

    public class MyBinder extends Binder {

        public BaseWorkService getService(){
            return BaseWorkService.this;
        }
    }

    public static class NotificationService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Logger.i("work inner service onStartCommand");
            startForeground(NOTIFICATION_ID, new Notification());
            stopSelf();
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            Logger.i("work inner service onBind");
            return null;
        }
    }

}
