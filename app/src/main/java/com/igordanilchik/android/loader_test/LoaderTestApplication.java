package com.igordanilchik.android.loader_test;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;


public class LoaderTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        final boolean isDebuggable = 0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);

        if (isDebuggable) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    //.penaltyDeath()
                    .build());
        }
    }
}
