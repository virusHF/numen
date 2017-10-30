package com.fan.numen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fan.numen.Numen;
import com.fan.numen.utils.Logger;

/**
 * Created by zihao.wang on 17/10/27.
 */

public class GarbageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(intent.getAction());
        Logger.i("GarbageReceiver try start work service");
        Numen.getInstance().startWorkService();
    }
}
