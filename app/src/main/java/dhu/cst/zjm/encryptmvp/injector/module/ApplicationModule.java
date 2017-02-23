package dhu.cst.zjm.encryptmvp.injector.module;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.injector.scope.PerApplication;

/**
 * Created by zjm on 2017/2/23.
 */
@Module
public class ApplicationModule {
    private final MyApplication myApplication;

    public ApplicationModule(MyApplication application) {
        this.myApplication = application;
    }

    @Provides
    @PerApplication
    public Application provideApplication() {
        return myApplication;
    }

    @Provides
    @PerApplication
    public MyApplication provideMyApplication() {
        return myApplication;
    }
}
