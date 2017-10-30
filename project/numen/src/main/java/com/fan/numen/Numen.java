package com.fan.numen;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;

import com.fan.numen.receiver.OnePixelReceiver;
import com.fan.numen.service.BaseWorkService;
import com.fan.numen.service.ServiceHelper;
import com.fan.numen.utils.Logger;

/**
 * Created by zihao.wang on 17/10/27.
 */

public class Numen {
    private static final Numen sInstance = new Numen();

    private Numen() {
    }

    public static Numen getInstance() {
        return sInstance;
    }

    private Context mContext;
    private Class<? extends BaseWorkService> mWorkService;
    private boolean mHasInit = false;

    public void init(Context context, Class<? extends BaseWorkService> serviceClass) {
        if (context instanceof Activity) {
            throw new RuntimeException("context must application");
        }
        mHasInit = true;
        mWorkService = serviceClass;
        mContext = context;

        Logger.i("register one pixel receiver");
        registerReceiver(context);

        Logger.i("start work service");
        startWorkService();
    }

    private void registerReceiver(Context context) {
        OnePixelReceiver receiver = new OnePixelReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(receiver, intentFilter);
    }

    public void startWorkService() {
        ServiceHelper.startService(mContext, mWorkService);
    }

    public Context getContext() {
        return mContext;
    }

    public Class<? extends BaseWorkService> getWorkService() {
        return mWorkService;
    }

    public boolean isInit() {
        return mHasInit;
    }
}
