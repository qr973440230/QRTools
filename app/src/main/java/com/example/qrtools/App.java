package com.example.qrtools;

import android.app.Application;
import android.os.strictmode.LeakedClosableViolation;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by QR on 2019/4/24 17:02
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }
}
