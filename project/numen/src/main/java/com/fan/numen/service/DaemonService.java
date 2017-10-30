package com.fan.numen.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.fan.numen.Numen;
import com.fan.numen.utils.Logger;

public class DaemonService extends BaseService {
    private final IBinder binder = new DaemonService.MyBinder();


    protected static final int NOTIFICATION_ID = 5457;
    protected static final int JOB_ID = 5656;

    private boolean mRunning = false;

    @Override
    public void onCreate() {
        Logger.i("daemon service onCreate");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("daemon service onStartCommand");

        // 使用foreGroundService 提高进程优先级

        if (!mRunning) {
            mRunning = true;

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                startForeground(NOTIFICATION_ID, new Notification());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    ServiceHelper.startService(Numen.getInstance().getContext(),
                            new Intent(Numen.getInstance().getContext(), NotificationService.class));
                }
            }

            // 5.0+ jobScheduler
            // 其他alarmManager
            // 目的:定时查看work Service 运行状况
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(Numen.getInstance().getContext(), JobService.class));
                builder.setPeriodic(ServiceHelper.WAKE_UP_INTERVAL);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
                }
                builder.setPersisted(true);
                JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                scheduler.schedule(builder.build());
            } else {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent alarmIntent = new Intent(Numen.getInstance().getContext(), Numen.getInstance().getWorkService());
                PendingIntent pendingIntent = PendingIntent.getService(Numen.getInstance().getContext(), NOTIFICATION_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ServiceHelper.WAKE_UP_INTERVAL, ServiceHelper.WAKE_UP_INTERVAL, pendingIntent);
            }

            // 子线程定时间检查
            mCountDownTimer.start();

            getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(), Numen.getInstance().getWorkService().getName()),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        }

        return START_STICKY;
    }

    @Override
    public void onOver() {
        Logger.i("daemon service over");
        Context context = Numen.getInstance().getContext();
        Class<? extends BaseWorkService> workService = Numen.getInstance().getWorkService();
        ServiceHelper.startService(context, workService);
        ServiceHelper.startService(context, DaemonService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i("daemon service onBind");
        return binder;
    }

    public class MyBinder extends Binder {

        public DaemonService getService() {
            return DaemonService.this;
        }
    }

    @Override
    public void onDestroy() {
        Logger.i("daemon service destroy");
        super.onDestroy();
    }


    public static class NotificationService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Logger.i("daemon inner service onStartCommand");
            startForeground(NOTIFICATION_ID, new Notification());
            stopSelf();
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            Logger.i("daemon inner service onBind");
            return null;
        }
    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(Long.MAX_VALUE, ServiceHelper.WAKE_UP_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            Logger.i("daemon service timer check");
            Numen.getInstance().startWorkService();
        }

        @Override
        public void onFinish() {

        }
    };
}
