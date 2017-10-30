package com.fan.myapplication;

import com.fan.numen.service.BaseWorkService;
import com.fan.numen.utils.Logger;

/**
 * Created by zihao.wang on 17/10/27.
 */

public class MyService extends BaseWorkService {
    @Override
    protected void work() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    i ++;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Logger.i("第" + i + "次执行任务");
                }
            }
        }).start();
    }
}
