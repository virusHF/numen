package com.fan.numen.service;

import android.app.job.JobParameters;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.fan.numen.Numen;
import com.fan.numen.utils.Logger;

/**
 * Created by zihao.wang on 17/10/27.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobService extends android.app.job.JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Logger.i("job scheduler try start work service");
        Numen.getInstance().startWorkService();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Logger.i("job scheduler has stop");
        return false;
    }
}
