package dhu.cst.zjm.encryptmvp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import dhu.cst.zjm.encryptmvp.injector.component.ApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ApplicationModule;
import dhu.cst.zjm.encryptmvp.injector.module.NetworkModule;

/**
 * Created by zjm on 2017/2/23.
 */

public class MyApplication extends Application {

    private static Context mContext;
    private ApplicationComponent mApplicationComponent;
    private static RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        setStrictMode();
        setCrashHandler();
        initStetho();
        initLeakCanary();
        setupInjector();
    }

    /**
     *
     */
    private void setStrictMode() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    /**
     *
     */
    private void setCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    /**
     *
     */
    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    /**
     *
     */
    private void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    private void setupInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().
                applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return mContext;
    }
}
