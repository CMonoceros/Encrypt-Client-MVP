package dhu.cst.zjm.encryptmvp.injector.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;

/**
 * Created by zjm on 2017/2/24.
 */

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }
}
