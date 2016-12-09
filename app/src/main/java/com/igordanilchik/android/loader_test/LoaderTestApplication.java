package com.igordanilchik.android.loader_test;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


public class LoaderTestApplication extends Application {

    public static RefWatcher getRefWatcher(Context context) {
        LoaderTestApplication application = (LoaderTestApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

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
